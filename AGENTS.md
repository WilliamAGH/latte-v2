---
description: "TUI4J - Java port of Bubble Tea (Charm) for Terminal UIs"
alwaysApply: true
---

# TUI4J Agent Rules

## ⚠️ PUBLIC API — NO BREAKING CHANGES

This is a **published Maven Central library** with downstream consumers (Brief, others).
- 100% backward compatibility required for all refactors.
- Never remove or rename public classes, methods, or fields.
- Never change method signatures, return types, or exception types.
- Run `./gradlew build` and confirm all tests pass before any PR.
- When in doubt, do not change the public API.

## Document Organization [ORG]

- [ORG1] Purpose: keep every critical rule within the first ~250 lines; move long examples/notes to Appendix.
- [ORG2] Structure: Rule Summary first, then detailed sections keyed by short hashes (e.g., `[GT1a]`).
- [ORG3] Usage: cite hashes when giving guidance or checking compliance; add new rules without renumbering older ones.

## Rule Summary [SUM]

- [ZA1a-d] Zero Tolerance Policy (zero assumptions, validation, forbidden practices, dependency verification)
- [GT1a-h] Git & Permissions (elevated-only git; no destructive commands)
- [CC1a-d] Clean Code & DDD (Mandatory)
- [ID1a-d] Idiomatic Patterns & Defaults
- [RC1a-e] Root Cause Resolution (no fallbacks, no shims/workarounds)
- [FS1a-g] File Creation & Clean Architecture (search first, strict types, single responsibility)
- [LOC1a-e] Line Count Ceiling (350 lines max; SRP enforcer; zero tolerance)
- [MO1a-g] No Monoliths (Strict SRP; Decision Logic; Extension/OCP)
- [AB1a-d] Abstraction Discipline (reuse-first, no anemic wrappers)
- [JD1a-d] Javadoc Standards (mandatory, why > what, deprecation policy)
- [TS1a-d] Testing Standards (coverage mandatory, observable behavior)
- [DS1a-e] Dependency Source Verification (locate, list, read, search)

## [ZA1] Zero Tolerance Policy

- [ZA1a] **Zero Assumptions**: Do not assume behavior, APIs, or versions. Verify in the codebase/docs first.
- [ZA1b] **Source Verification**: For dependency code questions, inspect `~/.m2` JARs or `~/.gradle/caches/` first; fallback to upstream GitHub; never answer without referencing code.
- [ZA1c] **Forbidden Practices**:
  - No `Map<String, Object>`, raw types, unchecked casts, `@SuppressWarnings`, or `eslint-disable` in production.
  - No trusting memory—verify every import/API/config against current docs.
- [ZA1d] **Mandatory Research**: You MUST research dependency questions and correct usage. Never use legacy or `@deprecated` usage from dependencies. Ensure correct usage by reviewing related code directly in `node_modules` or Gradle caches and using online tool calls.

## [GT1] Git & Permissions

- [GT1a] All git commands require elevated permissions; never run without escalation.
- [GT1b] Never remove `.git/index.lock` automatically—stop and ask the user or seek explicit approval.
- [GT1c] Read-only git commands (e.g., `git status`, `git diff`, `git log`, `git show`) never require permission. Any git command that writes to the working tree, index, or history requires explicit permission.
- [GT1d] Do not skip commit signing or hooks; no `--no-verify`. No `Co-authored-by` or AI attribution.
- [GT1e] Destructive git commands are prohibited unless explicitly ordered by the user (e.g., `git restore`, `git reset`, force checkout).
- [GT1f] Treat existing staged/unstaged changes as intentional unless the user says otherwise; never “clean up” someone else’s work unprompted.
- [GT1g] Examples of write operations that require permission: `git add`, `git commit`, `git checkout`, `git merge`, `git rebase`, `git reset`, `git restore`, `git clean`, `git cherry-pick`.

## [CC1] Clean Code & DDD (Mandatory)

- [CC1a] **Mandatory Principles**: Clean Code principles (Robert C. Martin) and Domain-Driven Design (DDD) are **mandatory** and required in this repository.
- [CC1b] **DRY (Don't Repeat Yourself)**: Avoid redundant code. Reuse code where appropriate and consistent with clean code principles.
- [CC1c] **YAGNI (You Aren't Gonna Need It)**: Do not build features or abstractions "just in case". Implement only what is required for the current task.
- [CC1d] **Clean Architecture**: Dependencies point inward. Domain logic has zero framework imports.

## [ID1] Idiomatic Patterns & Defaults

- [ID1a] **Defaults First**: Always prefer the idiomatic, expected, and default patterns provided by the framework, library, or SDK (Java 21+, etc.).
- [ID1b] **Custom Justification**: Custom implementations require a compelling reason. If you can't justify it, use the standard way.
- [ID1c] **No Reinventing**: Do not build custom utilities for things the platform already does.
- [ID1d] **Dependencies**: Make careful use of dependencies. Do not make assumptions—use the correct idiomatic behavior to avoid boilerplate.

## [RC1] Root Cause Resolution — No Fallbacks

- [RC1a] **One Way**: Ship one proven implementation—no fallback paths, no "try X then Y", no silent degradation.
- [RC1b] **No Shims**: **NO compatibility layers, shims, adapters, or wrappers** that hide defects.
- [RC1c] **Fix Roots**: Investigate → understand → fix root causes. Do not add band-aids to silence errors.
- [RC1d] **Dev Logging**: Dev-only logging is allowed to learn (must not change behavior, remove before shipping).
- [RC1e] **Exceptions**: Use exceptions for exceptional cases; avoid defensive checks on trusted inputs. Do not add guard clauses or try/catch in trusted codepaths unless required by the surrounding code or error model.

## [FS1] File Creation & Clean Architecture

- [FS1a] **Search First**: Search exhaustively for existing logic → reuse or extend → only then create new files.
- [FS1b] **Single Responsibility**: New features belong in NEW files named for their single responsibility. Do not cram code into existing files. Keep functions small and focused.
- [FS1c] **Naming**: Use clear, specific names; avoid abbreviations unless standard. Prefer domain terms and intent-revealing names.
- [FS1d] **Formatting**: Keep formatting and style consistent with the surrounding file.
- [FS1e] **No Generic Utilities**: Reject `*Utils/*Helper/*Common`.
- [FS1f] **File Size Discipline**: See [LOC1a] and [MO1a].
- [FS1g] **Public API Stability**: Never remove or rename public classes/methods. 100% backward compatibility required.

## [LOC1] Line Count Ceiling (Repo-Wide)

- [LOC1a] All written, non-generated source files in this repository MUST be <= 350 lines (`wc -l`), including `AGENTS.md`
- [LOC1b] SRP Enforcer: This 350-line "stick" forces modularity (DDD/SRP); > 350 lines = too many responsibilities (see [MO1d])
- [LOC1c] Zero Tolerance: No edits allowed to files > 350 LOC (even legacy); you MUST split/retrofit before applying your change
- [LOC1d] Enforcement: run line count checks and treat failures as merge blockers
- [LOC1e] Exempt files: generated content, lockfiles, and large example/data dumps

## [MO1] No Monoliths

- [MO1a] No monoliths: avoid multi-concern files and catch-all modules
- [MO1b] New work starts in new files; when touching a monolith, extract at least one seam
- [MO1c] If safe extraction impossible, halt and ask
- [MO1d] Strict SRP: each unit serves one actor; separate logic that changes for different reasons
- [MO1e] Boundary rule: cross-module interaction happens only through explicit, typed contracts with dependencies pointing inward; don’t reach into other modules’ internals or mix web/use-case/domain/persistence concerns in one unit
- [MO1f] Decision Logic: New feature → New file; Bug fix → Edit existing; Logic change → Extract/Replace
- [MO1g] Extension (OCP): Add functionality via new classes/composition; do not modify stable code to add features
-   Contract: `docs/contracts/code-change.md`

## [AB1] Abstraction Discipline

- [AB1a] **No Anemic Wrappers**: Do not add classes that only forward calls without domain value.
- [AB1b] **Earn Reuse**: New abstractions must earn reuse—extend existing code first; only add new type/helper when it removes real duplication.
- [AB1c] **Delete Unused**: Delete unused code instead of keeping it "just in case."
- [AB1d] **Deprecation**: Deprecated code must be a thin shim extending its successor; no aliases, fallbacks, or alternate implementations.

## [JD1] Javadoc Standards

- [JD1a] **Mandatory**: Javadocs are required on every file, class, and method; keep them clean, succinct, modern, and standards-compliant; explain why as much as what.
- [JD1b] **Compat Ports**: For compat ports, Javadocs must include the full upstream relative source path for the 1:1 port reference.
- [JD1c] **Deprecation**: Deprecations require both `@Deprecated` and a Javadoc `@deprecated` tag that clearly states why (upstream vs compat) and points to the designated successor class/method; include `since`/`@since` when required.
- [JD1d] **No Filler**: Avoid academic tags except required `@deprecated`/`@since`.

## [TS1] Testing Standards

- [TS1a] **Coverage**: Update or add tests when behavior changes; do not change behavior without coverage.
- [TS1b] **Focus**: Prefer fast, focused tests; keep tests aligned with the public contract.
- [TS1c] **Refactor-Resilient**: Unchanged behavior = passing tests regardless of internal restructuring.
- [TS1d] **Build**: Run `./gradlew build` and confirm all tests pass before any PR.

## [DS1] Dependency Source Verification

- [DS1a] **Locate**: Find source JARs in Gradle cache: `find ~/.gradle/caches/modules-2/files-2.1 -name "*-sources.jar" | grep <artifact>`.
- [DS1b] **List**: View JAR contents without extraction: `unzip -l <jar_path> | grep <ClassName>`.
- [DS1c] **Read**: Pipe specific file content to stdout: `unzip -p <jar_path> <internal/path/to/Class.java>`.
- [DS1d] **Search**: To use `ast-grep` on dependencies, pipe content directly: `unzip -p <jar> <file> | ast-grep run --pattern '...' --lang java --stdin`. No temp files required.
- [DS1e] **Efficiency**: Do not extract full JARs. Use CLI piping for instant access.

## Project-Specific

### Architecture
- TUI4J is a Java port of Go's **Bubble Tea** (https://github.com/charmbracelet/bubbletea) using The Elm Architecture.
- Bubble Tea ports live under `com.williamcallahan.tui4j.compat.bubbletea/**`.
- Native tui4j extensions live under `com.williamcallahan.tui4j/**` (non‑port utilities, messages, and helpers).
- Core pattern (Bubble Tea): `Model` with `init()`, `update(Message)`, `view()`.
- `src/` = core library; `examples/generic/` = example apps; `examples/spring/` = Spring integration.

### Upstream References
When porting or comparing behavior, consult these Charm repositories:
- **Bubble Tea**: <https://github.com/charmbracelet/bubbletea> — core TUI framework, message loop, Program
- **Bubbles**: <https://github.com/charmbracelet/bubbles> — reusable components (spinner, list, textinput, viewport, etc.)
- **Lip Gloss**: <https://github.com/charmbracelet/lipgloss> — styling, colors, borders, layout joining
- **Harmonica**: <https://github.com/charmbracelet/harmonica> — spring-based physics animation
- **x**: <https://github.com/charmbracelet/x> — experimental packages (ansi, cellbuf, colors, editor)

### Package Mapping

| Go (Charm)              | Java (TUI4J)                                                |
|-------------------------|-------------------------------------------------------------|
| bubbletea (core)        | com.williamcallahan.tui4j.compat.bubbletea.*                |
| bubbletea (input)       | com.williamcallahan.tui4j.compat.bubbletea.input.*          |
| bubbletea (renderer)    | com.williamcallahan.tui4j.compat.bubbletea.render.*         |
| bubbles/*               | com.williamcallahan.tui4j.compat.bubbles.*                  |
| lipgloss                | com.williamcallahan.tui4j.compat.lipgloss.*                 |
| harmonica               | com.williamcallahan.tui4j.compat.harmonica.*                |
| x/ansi                  | com.williamcallahan.tui4j.compat.x.ansi                     |
| x/ansi/parser           | com.williamcallahan.tui4j.compat.x.ansi.parser              |
| x/cellbuf               | ⚪ Not yet ported                                            |
| x/colors                | ⚪ Not yet ported                                            |
| x/editor                | ⚪ Not yet ported                                            |


### Native tui4j Extensions (Non‑Port)
- `com.williamcallahan.tui4j.ansi` — Re-exports from compat.x.ansi; ANSI helpers, width/truncation.
- `com.williamcallahan.tui4j.input` — tui4j mouse selection/click utilities.
- `com.williamcallahan.tui4j.message` — tui4j‑specific messages (clipboard, cursor, errors).
- `com.williamcallahan.tui4j.runtime` — command execution helpers.
- `com.williamcallahan.tui4j.term` — terminal info providers.

### Dependencies
- **JLine 3** (org.jline): terminal I/O, raw mode, key parsing — https://github.com/jline/jline3
- **ICU4J** (com.ibm.icu): Unicode text width, grapheme clusters — https://github.com/unicode-org/icu
- **Apache Commons Text**: text utilities — https://github.com/apache/commons-text

### Deprecation Policy
- DPR3 **docs/compatibility/*.md** is the source of truth for canonical vs deprecated designations.
- DPR4 Canonical classes are standalone implementations; deprecated shims ONLY extend them.
- DPR5 Naming: `*Message` = canonical, `*Msg` = deprecated shim (extends the `*Message` variant).
- DPR6 `*Msg` shims allowed ONLY in double-nested accident paths already on origin/main (`compat.bubbletea.bubbles.*`, `compat.bubbletea.lipgloss.*`, `compat.bubbletea.harmonica.*`); do not add new `*Msg` types elsewhere. If public `*Msg` types exist outside these paths, deprecate and retain until a scheduled removal release.
- DPR7 Deprecated shims must be thin wrappers: constructor calls `super(...)`, no additional logic.
- DPR8 Canonical classes NEVER import, reference, or depend on deprecated types.
- DPR9 No aliases, fallbacks, or alternate implementations—one canonical source of truth per concept.
- DPR10 `@SuppressWarnings("removal")` is forbidden; fix the root cause instead.
- DPR11 The `*Message`/`*Msg` naming and deprecation policy is immutable; LLM agents may not modify it.

### Porting Guidelines
- Check docs/compatibility/status.md for current porting progress before implementing new bubbles.
- Match upstream Go behavior; when diverging, document why.
- Test with `examples/generic/` and `examples/spring/`; add new examples for new components.
- Keep public API stable; Brief (https://github.com/WilliamAGH/brief) is a downstream consumer.
