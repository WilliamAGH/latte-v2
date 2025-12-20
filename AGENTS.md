## ⚠️ PUBLIC API — NO BREAKING CHANGES

This is a **published Maven Central library** with downstream consumers (Brief, others).
- **100% backward compatibility required** for all refactors.
- Never remove or rename public classes, methods, or fields.
- Never change method signatures, return types, or exception types.
- Additive changes only; deprecate before removing (minimum 2 minor versions).
- Run `./gradlew build` and confirm all tests pass before any PR.
- When in doubt, do not change the public API.

---

- SRC1 Never make assumptions; if unsure, stop and verify.
- SRC2 For dependency code questions, inspect ~/.m2 JARs first; fallback to upstream GitHub; never answer without referencing code.
- NME1 Use clear, specific names; avoid abbreviations unless standard.
- FUN1 Keep functions small and focused; one responsibility per function.
- DRY1 Remove duplication; reuse existing utilities instead of rewriting logic.
- ERR1 Use exceptions for exceptional cases; avoid defensive checks on trusted inputs.
- CMT1 Comments and Javadocs only when they add clarity; avoid academic tags like @author/@since/@version.
- FMT1 Keep formatting and style consistent with the surrounding file.
- TST1 Update or add tests when behavior changes; do not change behavior without coverage.
- DEP1 Avoid unnecessary dependencies and unused code.

## Details

- SRC1 Verify with primary sources before answering; do not infer behavior without evidence.
- SRC2 Use dependency source JARs or decompiled classes from ~/.m2 to confirm behavior; if not available, consult the dependency's GitHub repo; always cite file paths or class names used.
- NME1 Prefer domain terms and intent-revealing names; rename unclear identifiers.
- FUN1 Split large methods; reduce branching and nested blocks when it improves readability.
- DRY1 Replace repeated logic with a shared function, utility, or existing helper.
- ERR1 Do not add guard clauses or try/catch in trusted codepaths unless required by the surrounding code or error model.
- CMT1 Keep documentation short and direct; explain why, not what; keep Javadocs concise and human.
- FMT1 Follow existing spacing, imports, and ordering; avoid style changes unrelated to the task.
- TST1 Prefer fast, focused tests; keep tests aligned with the public contract.
- DEP1 Remove unused imports, dependencies, and dead code.

## Project-Specific

### Architecture
- TUI4J is a Java port of Go's **Bubble Tea** (https://github.com/charmbracelet/bubbletea) using The Elm Architecture.
- Core pattern: `Model` interface with `init()`, `update(Message)`, `view()` methods.
- `src/` = core library; `examples/generic/` = example apps; `examples/spring/` = Spring integration.

### Upstream References
When porting or comparing behavior, consult these Charm repositories:
- **Bubble Tea**: https://github.com/charmbracelet/bubbletea — core TUI framework, message loop, Program
- **Bubbles**: https://github.com/charmbracelet/bubbletea — reusable components (spinner, list, textinput, viewport, etc.)
- **Lip Gloss**: https://github.com/charmbracelet/lipgloss — styling, colors, borders, layout joining

### Package Mapping
| Go (Charm)         | Java (TUI4J)                                     |
|--------------------|--------------------------------------------------|
| bubbletea          | com.williamcallahan.tui4j                        |
| bubbles/*          | com.williamcallahan.tui4j.compat.bubbletea.bubbles.*|
| lipgloss           | com.williamcallahan.tui4j.compat.bubbletea.lipgloss |

### Dependencies
- **JLine 3** (org.jline): terminal I/O, raw mode, key parsing — https://github.com/jline/jline3
- **ICU4J** (com.ibm.icu): Unicode text width, grapheme clusters — https://github.com/unicode-org/icu
- **Apache Commons Text**: text utilities — https://github.com/apache/commons-text

### Porting Guidelines
- Check STATUS.md for current porting progress before implementing new bubbles.
- Match upstream Go behavior; when diverging, document why.
- Test with `examples/generic/` and `examples/spring/`; add new examples for new components.
- Keep public API stable; Brief (https://github.com/williamcallahan/brief) is a downstream consumer.
