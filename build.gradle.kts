/*
 * TUI4J Root Build
 */

import org.gradle.api.attributes.java.TargetJvmEnvironment

plugins {
    id("java-library")
    id("maven-publish")
    signing
    id("com.gradleup.nmcp") version "1.2.1"
}

description = "TUI4J"

group = providers.gradleProperty("group").orNull ?: "com.williamcallahan"
version = providers.gradleProperty("version").orNull ?: "0.0.0-SNAPSHOT"

repositories {
    mavenCentral {
        // Avoid Gradle metadata variant selection issues (e.g., snakeyaml android classifier).
        metadataSources {
            mavenPom()
            artifact()
        }
    }
}

dependencies {
    components {
        withModule("org.yaml:snakeyaml") {
            allVariants {
                attributes.attribute(
                    TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
                    objects.named(TargetJvmEnvironment.STANDARD_JVM)
                )
            }
        }
    }
}

// Ensure we resolve standard JVM variants (avoid Android-only variants).
configurations.configureEach {
    attributes.attribute(
        TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
        objects.named(TargetJvmEnvironment.STANDARD_JVM)
    )
}

configurations.matching { it.name.startsWith("examples") && it.isCanBeResolved }.configureEach {
    attributes.attribute(
        TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
        objects.named(TargetJvmEnvironment.STANDARD_JVM)
    )
}

dependencies {
    api(libs.org.jline.jline.terminal.jni)
    api(libs.com.ibm.icu.icu4j)
    api(libs.org.apache.commons.commons.text)
    
    testImplementation(libs.org.junit.jupiter.junit.jupiter)
    testImplementation(libs.org.mockito.mockito.core)
    testImplementation(libs.org.mockito.mockito.junit.jupiter)
    testImplementation(libs.org.assertj.assertj.core)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withSourcesJar()
    withJavadocJar()
}

sourceSets {
    val main by getting

    val examplesGeneric by creating {
        java.srcDir("examples/generic/src/main/java")
        resources.srcDir("examples/generic/src/main/resources")
        compileClasspath += main.output + configurations["examplesGenericCompileClasspath"]
        runtimeClasspath += output + compileClasspath + configurations["examplesGenericRuntimeClasspath"]
    }

    val examplesSpring by creating {
        java.srcDir("examples/spring/src/main/java")
        resources.srcDir("examples/spring/src/main/resources")
        compileClasspath += main.output + configurations["examplesSpringCompileClasspath"]
        runtimeClasspath += output + compileClasspath + configurations["examplesSpringRuntimeClasspath"]
    }

    val examplesGenericTest by creating {
        java.srcDir("examples/generic/src/test/java")
        resources.srcDir("examples/generic/src/test/resources")
        compileClasspath += main.output + examplesGeneric.output + configurations["examplesGenericTestCompileClasspath"]
        runtimeClasspath += output + compileClasspath + configurations["examplesGenericTestRuntimeClasspath"]
    }

    val examplesSpringTest by creating {
        java.srcDir("examples/spring/src/test/java")
        resources.srcDir("examples/spring/src/test/resources")
        compileClasspath += main.output + examplesSpring.output + configurations["examplesSpringTestCompileClasspath"]
        runtimeClasspath += output + compileClasspath + configurations["examplesSpringTestRuntimeClasspath"]
    }
}

tasks.register("examplesClasses") {
    group = "build"
    description = "Compiles all example source sets."
    dependsOn("examplesGenericClasses", "examplesSpringClasses")
}

tasks.named<Test>("test") {
    dependsOn("examplesClasses")
    dependsOn("examplesGenericTest", "examplesSpringTest")
}

tasks.named<JavaCompile>("compileExamplesSpringJava") {
    options.annotationProcessorPath = configurations["examplesSpringAnnotationProcessor"]
}

dependencies {
    "examplesGenericImplementation"(sourceSets.main.get().output)
    "examplesGenericImplementation"(libs.com.squareup.okhttp3.okhttp)

    "examplesSpringImplementation"(platform("org.springframework.boot:spring-boot-dependencies:3.3.5"))
    "examplesSpringImplementation"(sourceSets.main.get().output)
    "examplesSpringImplementation"(libs.org.springframework.boot.spring.boot.starter.data.jpa) {
        exclude(group = "org.yaml", module = "snakeyaml")
    }
    "examplesSpringImplementation"(libs.com.github.javafaker.javafaker) {
        exclude(group = "org.yaml", module = "snakeyaml")
    }
    "examplesSpringImplementation"("org.yaml:snakeyaml:2.2@jar")
    "examplesSpringRuntimeOnly"(libs.com.h2database.h2)
    "examplesSpringCompileOnly"(libs.org.projectlombok.lombok)
    "examplesSpringAnnotationProcessor"(libs.org.projectlombok.lombok)
}

configurations["examplesGenericTestImplementation"].extendsFrom(
    configurations["examplesGenericImplementation"],
    configurations["testImplementation"]
)
configurations["examplesGenericTestRuntimeOnly"].extendsFrom(
    configurations["examplesGenericRuntimeOnly"],
    configurations["testRuntimeOnly"]
)
configurations["examplesSpringTestImplementation"].extendsFrom(
    configurations["examplesSpringImplementation"],
    configurations["testImplementation"]
)
configurations["examplesSpringTestRuntimeOnly"].extendsFrom(
    configurations["examplesSpringRuntimeOnly"],
    configurations["testRuntimeOnly"]
)

tasks.register<Test>("examplesGenericTest") {
    description = "Runs tests for examples/generic."
    group = "verification"
    testClassesDirs = sourceSets["examplesGenericTest"].output.classesDirs
    classpath = sourceSets["examplesGenericTest"].runtimeClasspath
}

tasks.register<Test>("examplesSpringTest") {
    description = "Runs tests for examples/spring."
    group = "verification"
    testClassesDirs = sourceSets["examplesSpringTest"].output.classesDirs
    classpath = sourceSets["examplesSpringTest"].runtimeClasspath
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        events(
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.SHORT
        showStandardStreams = false
    }
}

tasks.withType<Test>().configureEach {
    afterSuite(KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
        if (desc.parent == null) {
            logger.lifecycle(
                "TESTS: ${result.resultType} (${result.testCount} tests, " +
                    "${result.successfulTestCount} passed, " +
                    "${result.failedTestCount} failed, " +
                    "${result.skippedTestCount} skipped)"
            )
        }
    }))
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "tui4j"
            
            pom {
                name.set("TUI4J")
                description.set("Terminal User Interface framework for Java - a port of Bubble Tea")
                url.set("https://github.com/WilliamAGH/tui4j")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/license/mit")
                    }
                }

                developers {
                    developer {
                        name.set("William Callahan")
                        email.set("william@williamcallahan.com")
                        url.set("https://williamcallahan.com")
                    }
                    developer {
                        name.set("Lukasz Grabski")
                        email.set("activey@doaplatform.org")
                    }
                }

                scm {
                    url.set("https://github.com/WilliamAGH/tui4j")
                    connection.set("scm:git:git://github.com/WilliamAGH/tui4j.git")
                    developerConnection.set("scm:git:ssh://github.com:WilliamAGH/tui4j.git")
                }
            }
        }
    }

    repositories {
        maven {
            val releasesUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl

            credentials {
                username = providers.environmentVariable("SONATYPE_USERNAME").orNull
                password = providers.environmentVariable("SONATYPE_PASSWORD").orNull
            }
        }
    }
}

signing {
    val signingKey = providers.environmentVariable("GPG_PRIVATE_KEY").orNull
    val signingPassword = providers.environmentVariable("GPG_PASSPHRASE").orNull

    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}

nmcp {
    publishAllPublicationsToCentralPortal {
        username.set(
            providers.environmentVariable("CENTRAL_PORTAL_USERNAME").orNull
                ?: providers.environmentVariable("SONATYPE_USERNAME").orNull
        )
        password.set(
            providers.environmentVariable("CENTRAL_PORTAL_PASSWORD").orNull
                ?: providers.environmentVariable("SONATYPE_PASSWORD").orNull
        )
        publishingType.set("USER_MANAGED")
    }
}
