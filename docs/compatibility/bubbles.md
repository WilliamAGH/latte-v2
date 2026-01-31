# Bubbles Compatibility Map

- **Source Repo**: [charmbracelet/bubbles](https://github.com/charmbracelet/bubbles)
- **Copyright**: Copyright (c) 2020-2025 Charmbracelet, Inc
- **Java Package**: `com.williamcallahan.tui4j.compat.bubbles`

---

## Naming Convention

See AGENTS.md rules DPR5–DPR11 for naming and deprecation policy.

---

## Source File Mappings

- [ ] **bubbles.go** → `<unmapped>`

- [x] **cursor/cursor.go**
  - `cursor/Cursor.java`
  - `cursor/CursorMode.java`

- [x] **cursor/cursor_test.go** → `cursor/CursorTest.java`

- [x] **filepicker/filepicker.go**
  - `filepicker/FilePicker.java`
  - `filepicker/KeyMap.java`
  - `filepicker/Styles.java`
  - `filepicker/DidSelectFileMessage.java`
  - `filepicker/DidSelectDirectoryMessage.java`

- [ ] **filepicker/hidden_unix.go** → `<unmapped>`

- [ ] **filepicker/hidden_windows.go** → `<unmapped>`

- [x] **help/help.go**
  - `help/Help.java`
  - `help/KeyMap.java`
  - `help/Styles.java`

- [x] **help/help_test.go** → `help/HelpTest.java`

- [x] **key/key.go**
  - `key/Binding.java`
  - `key/Help.java`

- [x] **key/key_test.go** → `key/BindingTest.java`

- [x] **list/defaultitem.go**
  - `list/DefaultItem.java`
  - `list/DefaultItemStyles.java`
  - `list/DefaultDelegate.java`

- [x] **list/keys.go** → `list/KeyMap.java`

- [x] **list/list.go**
  - `list/List.java`
  - `list/Item.java`
  - `list/ItemDelegate.java`
  - `list/FilterState.java`
  - `list/FilterFunction.java`
  - `list/FilteredItem.java`
  - `list/FetchedItems.java`
  - `list/FetchedCurrentPageItems.java`
  - `list/ListDataSource.java`
  - `list/DefaultDataSource.java`
  - `list/Rank.java`
  - `list/StatusMessageTimeoutMessage.java`
  - `list/fuzzy/FuzzyFilter.java`
  - `list/fuzzy/Match.java`

- [x] **list/list_test.go** → `list/ListTest.java`

- [x] **list/style.go** → `list/Styles.java`

- [x] **paginator/paginator.go**
  - `paginator/Paginator.java`
  - `paginator/KeyMap.java`
  - `paginator/Bounds.java`
  - `paginator/Type.java`

- [x] **paginator/paginator_test.go** → `paginator/PaginatorTest.java`

- [x] **progress/progress.go**
  - `progress/Progress.java`
  - `progress/FrameMessage.java`
  - `progress/SetPercentMessage.java`
  - `progress/Spring.java`

- [x] **progress/progress_test.go** → `progress/ProgressTest.java`

- [x] **runeutil/runeutil.go** → `runeutil/Sanitizer.java`

- [x] **runeutil/runeutil_test.go** → `runeutil/SanitizerTest.java`

- [x] **spinner/spinner.go**
  - `spinner/Spinner.java`
  - `spinner/SpinnerType.java`
  - `spinner/TickMessage.java`

- [x] **spinner/spinner_test.go** → `spinner/SpinnerTest.java`

- [x] **stopwatch/stopwatch.go**
  - `stopwatch/Stopwatch.java`
  - `stopwatch/StartStopMessage.java`
  - `stopwatch/TickMessage.java`
  - `stopwatch/ResetMessage.java`

- [x] **table/table.go**
  - `table/Table.java`
  - `table/Column.java`
  - `table/Row.java`
  - `table/Keys.java`
  - `table/Styles.java`

- [x] **table/table_test.go** → `table/TableTest.java`

- [ ] **textarea/memoization/memoization.go** → `<unmapped>`

- [ ] **textarea/memoization/memoization_test.go** → `<unmapped>`

- [x] **textarea/textarea.go** → `textarea/Textarea.java`

- [x] **textarea/textarea_test.go** → `textarea/TextareaTest.java`

- [x] **textinput/textinput.go**
  - `textinput/TextInput.java`
  - `textinput/EchoMode.java`
  - `textinput/Keys.java`

- [x] **textinput/textinput_test.go** → `textinput/TextInputTest.java`

- [x] **timer/timer.go**
  - `timer/Timer.java`
  - `timer/StartStopMessage.java`
  - `timer/TickMessage.java`
  - `timer/TimeoutMessage.java`

- [x] **viewport/keymap.go** → `viewport/KeyMap.java`

- [x] **viewport/viewport.go** → `viewport/Viewport.java`

- [x] **viewport/viewport_test.go** → `viewport/ViewportTest.java`
