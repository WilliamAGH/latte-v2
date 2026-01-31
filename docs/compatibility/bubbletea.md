# Bubble Tea Compatibility Map

- **Source Repo**: [charmbracelet/bubbletea](https://github.com/charmbracelet/bubbletea)
- **Copyright**: Copyright (c) 2020-2025 Charmbracelet, Inc
- **Java Package**: `com.williamcallahan.tui4j.compat.bubbletea`

---

## Naming Convention

See AGENTS.md rules DPR5–DPR11 for naming and deprecation policy.

---

## Source File Mappings

### Root

- [x] **commands.go**
  - `Command.java`
  - `BatchMessage.java`
  - `SequenceMessage.java`
  - `SetWindowTitleMessage.java`
  - `OpenUrlMessage.java`
  - `CopyToClipboardMessage.java`
  - `ReadClipboardMessage.java`

- [x] **commands_test.go** → `CommandsParityTest.java`

- [x] **exec.go**
  - `ExecProcessMessage.java`
  - `ExecCompletedMessage.java`

- [x] **exec_test.go** → `ExecTest.java`

- [x] **focus.go**
  - `FocusMessage.java`
  - `BlurMessage.java`

- [x] **inputreader_other.go**
  - `input/InputHandler.java`
  - `input/NewInputHandler.java`
  - `input/OldLaggyInputHandler.java`
  - `input/NoopInputHandler.java`

- [x] **inputreader_windows.go** → `input/WindowsInputHandler.java`

- [x] **key.go**
  - `input/key/Key.java`
  - `input/key/KeyType.java`
  - `input/key/KeyNames.java`
  - `input/key/KeyAliases.java`
  - `KeyPressMessage.java`
  - `PasteMessage.java`
  - `UnknownInputByteMessage.java`
  - `UnknownSequenceMessage.java`

- [x] **key_other.go** → `input/NewInputHandler.java`

- [x] **key_sequences.go**
  - `input/key/Sequences.java`
  - `input/key/ExtendedSequences.java`

- [x] **key_test.go** → `input/key/KeyTest.java`

- [x] **key_windows.go** → `input/WindowsInputHandler.java`

- [x] **logging.go** → `Logging.java`

- [x] **logging_test.go** → `LoggingTest.java`

- [x] **mouse.go**
  - `input/MouseMessage.java`
  - `input/MouseButton.java`
  - `input/MouseAction.java`

- [x] **mouse_test.go** → `input/MouseTest.java`

- [x] **nil_renderer.go** → `render/NilRenderer.java`

- [x] **nil_renderer_test.go** → `render/NilRendererTest.java`

- [x] **options.go** → `ProgramOption.java`

- [x] **options_test.go** → `ProgramOptionsTest.java`

- [x] **renderer.go** → `render/Renderer.java`

- [x] **screen.go**
  - `WindowSizeMessage.java`
  - `ClearScreenMessage.java`
  - `EnterAltScreenMessage.java`
  - `ExitAltScreenMessage.java`
  - `EnableBracketedPasteMessage.java`
  - `DisableBracketedPasteMessage.java`
  - `EnableMouseCellMotionMessage.java`
  - `EnableMouseAllMotionMessage.java`
  - `DisableMouseMessage.java`
  - `CheckWindowSizeMessage.java`
  - `ResetMouseCursorMessage.java`
  - `SetMouseCursorPointerMessage.java`
  - `SetMouseCursorTextMessage.java`

- [x] **screen_test.go** → `ScreenTest.java`

- [x] **signals_unix.go** → `Program.java`

- [x] **signals_windows.go** → `Program.java`

- [x] **standard_renderer.go**
  - `render/StandardRenderer.java`
  - `PrintLineMessage.java`

- [x] **tea.go**
  - `Program.java`
  - `Model.java`
  - `Message.java`
  - `MessageShim.java`
  - `UpdateResult.java`
  - `QuitMessage.java`
  - `SuspendMessage.java`
  - `ResumeMessage.java`
  - `ProgramException.java`
  - `ErrorMessage.java`

- [x] **tea_init.go** → `Program.java`

- [x] **tea_test.go** → `TeaTest.java`

- [x] **tty.go** → `Program.java`

- [x] **tty_unix.go** → `Program.java`

- [x] **tty_windows.go** → `Program.java`

---

### Tutorials

- [x] **tutorials/basics/main.go** → `src/main/resources/examples/compat/bubbletea/tutorials/basics/BasicsTutorialExample.java`
- [x] **tutorials/commands/main.go** → `src/main/resources/examples/compat/bubbletea/tutorials/commands/CommandsTutorialExample.java`

---

### Examples

- [x] **examples/altscreen-toggle/main.go** → `src/main/resources/examples/compat/bubbletea/altscreentoggle/AltScreenToggleExample.java`
- [x] **examples/autocomplete/main.go** → `src/main/resources/examples/compat/bubbletea/autocomplete/AutocompleteExample.java`
- [x] **examples/cellbuffer/main.go** → `src/main/resources/examples/compat/bubbletea/cellbuffer/CellExample.java`
- [x] **examples/chat/main.go** → `src/main/resources/examples/compat/bubbletea/chat/ChatExample.java`
- [x] **examples/composable-views/main.go** → `src/main/resources/examples/compat/bubbletea/composableviews/ComposableViewsExample.java`
- [x] **examples/credit-card-form/main.go** → `src/main/resources/examples/compat/bubbletea/creditcardform/CreditCardFormExample.java`
- [x] **examples/debounce/main.go** → `src/main/resources/examples/compat/bubbletea/debounce/DebounceExample.java`
- [x] **examples/exec/main.go** → `src/main/resources/examples/compat/bubbletea/exec/ExecExample.java`
- [x] **examples/eyes/main.go** → `src/main/resources/examples/compat/bubbletea/eyes/EyesExample.java`
- [x] **examples/file-picker/main.go** → `src/main/resources/examples/compat/bubbletea/filepicker/FilePickerExample.java`
- [x] **examples/focus-blur/main.go** → `src/main/resources/examples/compat/bubbletea/focusblur/FocusBlurExample.java`
- [x] **examples/fullscreen/main.go** → `src/main/resources/examples/compat/bubbletea/fullscreen/FullscreenExample.java`
- [x] **examples/glamour/main.go** → `src/main/resources/examples/compat/bubbletea/glamour/GlamourExample.java`
  - Note: Glamour is not ported; Java example renders raw markdown in a viewport.
- [x] **examples/help/main.go** → `src/main/resources/examples/compat/bubbletea/help/HelpExample.java`
- [x] **examples/http/main.go** → `src/main/resources/examples/compat/bubbletea/http/HttpExample.java`
- [x] **examples/list-default/main.go** → `src/main/resources/examples/compat/bubbletea/listdefault/ListDefaultExample.java`
- [x] **examples/list-fancy/delegate.go** → `src/main/resources/examples/compat/bubbletea/listfancy/Delegate.java`
- [x] **examples/list-fancy/main.go** → `src/main/resources/examples/compat/bubbletea/listfancy/ListFancyExample.java`
  - `src/main/resources/examples/compat/bubbletea/listfancy/FancyItem.java`
  - `src/main/resources/examples/compat/bubbletea/listfancy/Keys.java`
  - `src/main/resources/examples/compat/bubbletea/listfancy/Styles.java`
- [x] **examples/list-fancy/randomitems.go** → `src/main/resources/examples/compat/bubbletea/listfancy/RandomItemGenerator.java`
- [x] **examples/list-simple/main.go** → `src/main/resources/examples/compat/bubbletea/listsimple/ListSimpleExample.java`
- [x] **examples/mouse/main.go** → `src/main/resources/examples/compat/bubbletea/mouse/MouseExample.java`
- [x] **examples/package-manager/main.go** → `src/main/resources/examples/compat/bubbletea/progress/packagemanager/PackageManagerExample.java`
- [x] **examples/package-manager/packages.go** → `src/main/resources/examples/compat/bubbletea/progress/packagemanager/PackageManagerExample.java`
- [x] **examples/pager/main.go** → `src/main/resources/examples/compat/bubbletea/pager/PagerExample.java`
- [x] **examples/paginator/main.go** → `src/main/resources/examples/compat/bubbletea/paginator/PaginatorExample.java`
- [x] **examples/pipe/main.go** → `src/main/resources/examples/compat/bubbletea/pipe/PipeExample.java`
- [x] **examples/prevent-quit/main.go** → `src/main/resources/examples/compat/bubbletea/preventquit/PreventQuitExample.java`
- [x] **examples/progress-animated/main.go** → `src/main/resources/examples/compat/bubbletea/progress/animated/ProgressAnimatedExample.java`
- [x] **examples/progress-download/main.go** → `src/main/resources/examples/compat/bubbletea/progress/download/ProgressDownloadExample.java`
- [x] **examples/progress-download/tui.go** → `src/main/resources/examples/compat/bubbletea/progress/download/ProgressDownloadExample.java`
- [x] **examples/progress-static/main.go** → `src/main/resources/examples/compat/bubbletea/progress/staticview/ProgressStaticExample.java`
- [x] **examples/realtime/main.go** → `src/main/resources/examples/compat/bubbletea/realtime/RealtimeExample.java`
  - `src/main/resources/examples/compat/bubbletea/realtime/RealtimeMessage.java`
- [x] **examples/result/main.go** → `src/main/resources/examples/compat/bubbletea/result/ResultExample.java`
- [x] **examples/send-msg/main.go** → `src/main/resources/examples/compat/bubbletea/sendmsg/SendMessageExample.java`
  - `src/main/resources/examples/compat/bubbletea/sendmsg/SendMessage.java`
- [x] **examples/sequence/main.go** → `src/main/resources/examples/compat/bubbletea/sequence/SequenceExample.java`
- [x] **examples/set-window-title/main.go** → `src/main/resources/examples/compat/bubbletea/setwindowtitle/SetWindowTitleExample.java`
- [x] **examples/simple/main.go** → `src/main/resources/examples/compat/bubbletea/simple/SimpleExample.java`
  - `src/main/resources/examples/compat/bubbletea/simple/TickMessage.java`
- [x] **examples/simple/main_test.go** → `src/test/java/com/williamcallahan/tui4j/compat/bubbletea/examples/SimpleExampleTest.java`
- [x] **examples/spinner/main.go** → `src/main/resources/examples/compat/bubbletea/spinner/SpinnerExample.java`
- [x] **examples/spinners/main.go** → `src/main/resources/examples/compat/bubbletea/spinners/SpinnersExample.java`
- [x] **examples/split-editors/main.go** → `src/main/resources/examples/compat/bubbletea/split/SplitEditorsExample.java`
- [x] **examples/stopwatch/main.go** → `src/main/resources/examples/compat/bubbletea/stopwatch/StopwatchExample.java`
- [x] **examples/suspend/main.go** → `src/main/resources/examples/compat/bubbletea/suspend/SuspendExample.java`
- [x] **examples/table-resize/main.go** → `src/main/resources/examples/compat/bubbletea/tableresize/TableResizeExample.java`
- [x] **examples/table/main.go** → `src/main/resources/examples/compat/bubbletea/table/TableExample.java`
- [x] **examples/tabs/main.go** → `src/main/resources/examples/compat/bubbletea/tabs/TabsExample.java`
- [x] **examples/textarea/main.go** → `src/main/resources/examples/compat/bubbletea/textarea/TextareaExample.java`
- [x] **examples/textinput/main.go** → `src/main/resources/examples/compat/bubbletea/textinput/TextInputExample.java`
- [x] **examples/textinputs/main.go** → `src/main/resources/examples/compat/bubbletea/textinputs/TextInputsExample.java`
- [x] **examples/timer/main.go** → `src/main/resources/examples/compat/bubbletea/timer/TimerExample.java`
- [x] **examples/tui-daemon-combo/main.go** → `src/main/resources/examples/compat/bubbletea/tuidemoncombo/DaemonComboExample.java`
- [x] **examples/views/main.go** → `src/main/resources/examples/compat/bubbletea/views/ViewsExample.java`
- [x] **examples/window-size/main.go** → `src/main/resources/examples/compat/bubbletea/windowsize/WindowSizeExample.java`

---

### TUI4J Extensions (No Upstream Equivalent)

- [x] `src/main/resources/examples/compat/bubbletea/BorderTest.java`
- [x] `src/main/resources/examples/compat/bubbletea/ExamplesRunner.java`
- [x] `src/main/resources/examples/compat/bubbletea/conway/Conway.java`
- [x] `src/main/resources/examples/compat/bubbletea/conway/ConwayGame.java`
- [x] `src/main/resources/examples/compat/bubbletea/counter/CounterExample.java`
- [x] `src/main/resources/examples/compat/bubbletea/counter/CounterMessage.java`
- [x] `src/main/resources/examples/compat/bubbletea/cursor/CursorExample.java`
- [x] `src/main/resources/examples/compat/bubbletea/demo/Demo.java`
- [x] `src/main/resources/examples/compat/bubbletea/error/ErrorExample.java`
- [x] `src/main/resources/examples/compat/bubbletea/fireworks/Fireworks.java`
- [x] `src/main/resources/examples/compat/bubbletea/progress/staticbar/ProgressStaticExample.java`
- [x] `src/main/resources/examples/compat/bubbletea/terminfo/TermInfoExample.java`
- [x] `src/main/resources/examples/compat/bubbletea/tetris/Block.java`
- [x] `src/main/resources/examples/compat/bubbletea/tetris/GameOverMessage.java`
- [x] `src/main/resources/examples/compat/bubbletea/tetris/Grid.java`
- [x] `src/main/resources/examples/compat/bubbletea/tetris/Position.java`
- [x] `src/main/resources/examples/compat/bubbletea/tetris/TetrisGame.java`
- [x] `src/main/resources/examples/compat/bubbletea/tetris/Tetromino.java`
- [x] `src/main/resources/examples/compat/bubbletea/tetris/TetrominoInstance.java`
- [x] `src/main/resources/examples/compat/bubbletea/width/WidthExample.java`
