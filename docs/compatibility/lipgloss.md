# Lipgloss Compatibility Map

- **Source Repo**: [charmbracelet/lipgloss](https://github.com/charmbracelet/lipgloss)
- **Copyright**: Copyright (c) 2021-2025 Charmbracelet, Inc
- **Java Package**: `com.williamcallahan.tui4j.compat.lipgloss`

See AGENTS.md rules DPR5–DPR11 for naming and deprecation policy.

---

## Source File Mappings

### Root

- [x] **align.go**
  - `Alignment.java`
  - `align/AlignmentDecorator.java`

- [x] **align_test.go** → `align/AlignmentDecoratorTest.java`

- [ ] **ansi_unix.go** → `<unmapped>`

- [ ] **ansi_windows.go** → `<unmapped>`

- [x] **borders.go**
  - `border/Border.java`
  - `border/StandardBorder.java`
  - `Borders.java`

- [x] **borders_test.go** → `BordersTest.java`

- [x] **color.go**
  - `color/Color.java`
  - `color/RGB.java`
  - `color/HSL.java`
  - `color/ANSIColor.java`
  - `color/ANSI256Color.java`
  - `color/AdaptiveColor.java`
  - `color/ColorProfile.java`
  - `color/TerminalColor.java`
  - `color/ANSIColors.java`
  - `color/ColorApplyStrategy.java`
  - `color/NoColor.java`
  - `color/RGBColor.java`
  - `color/RGBSupplier.java`

- [x] **color_test.go**
  - `color/RGBTest.java`
  - `color/HSLTest.java`

- [x] **get.go** → `Style.java`

- [x] **join.go**
  - `Join.java`
  - `join/HorizontalJoinDecorator.java`
  - `join/VerticalJoinDecorator.java`

- [x] **join_test.go**
  - `join/HorizontalJoinDecoratorTest.java`
  - `join/VerticalJoinDecoratorTest.java`

- [x] **lipgloss.go**
  - `Style.java`
  - `Renderer.java`

- [x] **position.go** → `Position.java`

- [x] **ranges.go** → `TextLines.java`

- [x] **ranges_test.go** → `TextLinesTest.java`

- [x] **renderer.go**
  - `Renderer.java`
  - `Output.java`

- [x] **renderer_test.go** → `RendererTest.java`

- [x] **runes.go** → `Runes.java`

- [x] **runes_test.go** → `RunesTest.java`

- [x] **set.go** → `Style.java`

- [x] **size.go**
  - `Size.java`
  - `Dimensions.java`

- [x] **style.go**
  - `Style.java`
  - `MarginDecorator.java`
  - `PaddingDecorator.java`
  - `PlacementDecorator.java`

- [x] **style_test.go** → `StyleTest.java`

- [x] **unset.go** → `Style.java`

- [x] **whitespace.go** → `Whitespace.java`

---

### List

- [x] **list/enumerator.go** → `ListEnumerator.java`

- [x] **list/list.go** → `List.java`

- [x] **list/list_test.go** → `list/ListTest.java`

---

### Table

- [x] **table/resizing.go** → `table/Table.java`

- [x] **table/rows.go**
  - `table/Table.java`
  - `table/Data.java`
  - `table/StringData.java`
  - `table/Filter.java`

- [x] **table/table.go**
  - `table/Table.java`
  - `table/Data.java`
  - `table/StringData.java`
  - `table/StyleFunc.java`

- [x] **table/table_test.go** → `table/TableTest.java`

- [x] **table/util.go** → `table/Table.java`

---

### Tree

- [x] **tree/children.go**
  - `tree/Children.java`
  - `tree/NodeChildren.java`
  - `tree/Filter.java`

- [x] **tree/enumerator.go**
  - `tree/TreeEnumerator.java`
  - `tree/TreeIndenter.java`

- [x] **tree/example_test.go** → `tree/TreeExampleTest.java`

- [x] **tree/renderer.go**
  - `tree/Renderer.java`
  - `tree/StyleFunction.java`

- [x] **tree/tree.go**
  - `tree/Tree.java`
  - `tree/TreeStyle.java`
  - `tree/Node.java`
  - `tree/Leaf.java`

- [x] **tree/tree_test.go** → `tree/TreeTest.java`

---

### Examples

- [x] **examples/layout/main.go**
  - `src/main/resources/examples/compat/lipgloss/layout/StatusBarExample.java`
  - `src/main/resources/examples/compat/lipgloss/layout/TabsExample.java`
- [ ] **examples/list/duckduckgoose/main.go** → `<unmapped>`
- [x] **examples/list/glow/main.go** → `src/main/resources/examples/compat/lipgloss/list/ListGlowExample.java`
- [x] **examples/list/grocery/main.go** → `src/main/resources/examples/compat/lipgloss/list/ListGroceryExample.java`
- [x] **examples/list/roman/main.go** → `src/main/resources/examples/compat/lipgloss/list/ListRomanExample.java`
- [x] **examples/list/simple/main.go** → `src/main/resources/examples/compat/lipgloss/list/ListSimpleExample.java`
- [x] **examples/list/sublist/main.go** → `src/main/resources/examples/compat/lipgloss/list/ListSublistExample.java`
- [ ] **examples/ssh/main.go** → `<unmapped>`
- [x] **examples/table/ansi/main.go** → `src/main/resources/examples/compat/lipgloss/table/TableAnsiExample.java`
- [x] **examples/table/chess/main.go** → `src/main/resources/examples/compat/lipgloss/table/TableChessExample.java`
- [x] **examples/table/languages/main.go** → `src/main/resources/examples/compat/lipgloss/table/TableLanguagesExample.java`
- [x] **examples/table/mindy/main.go** → `src/main/resources/examples/compat/lipgloss/table/TableMindyExample.java`
- [x] **examples/table/pokemon/main.go** → `src/main/resources/examples/compat/lipgloss/table/TablePokemonExample.java`
- [x] **examples/tree/background/main.go** → `src/main/resources/examples/compat/lipgloss/tree/TreeBackgroundExample.java`
- [x] **examples/tree/files/main.go** → `src/main/resources/examples/compat/lipgloss/tree/TreeFilesExample.java`
- [x] **examples/tree/makeup/main.go** → `src/main/resources/examples/compat/lipgloss/tree/TreeMakeupExample.java`
- [x] **examples/tree/rounded/main.go** → `src/main/resources/examples/compat/lipgloss/tree/TreeRoundedExample.java`
- [x] **examples/tree/simple/main.go** → `src/main/resources/examples/compat/lipgloss/tree/TreeSimpleExample.java`
- [x] **examples/tree/styles/main.go** → `src/main/resources/examples/compat/lipgloss/tree/TreeStylesExample.java`
- [x] **examples/tree/toggle/main.go** → `src/main/resources/examples/compat/lipgloss/tree/TreeToggleExample.java`

---

### TUI4J Extensions (No Upstream Equivalent)

- [x] `src/test/java/com/williamcallahan/tui4j/compat/lipgloss/SizeTest.java`
