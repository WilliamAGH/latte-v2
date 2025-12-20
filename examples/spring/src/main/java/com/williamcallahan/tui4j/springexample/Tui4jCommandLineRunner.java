package com.williamcallahan.tui4j.springexample;

import com.williamcallahan.tui4j.Program;
import com.williamcallahan.tui4j.springexample.view.MainViewModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Example Spring Boot command line runner for TUI4J.
 */
@Component
public class Tui4jCommandLineRunner implements CommandLineRunner {

    private final MainViewModel tui4jApplication;

    public Tui4jCommandLineRunner(MainViewModel tui4jApplication) {
        this.tui4jApplication = tui4jApplication;
    }

    @Override
    public void run(String... args) throws Exception {
        new Program(tui4jApplication)
                .withAltScreen()
                .run();
    }
}
