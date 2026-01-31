# Porting Status

This page covers the current status of porting the [charmbracelet](https://github.com/charmbracelet) ecosystem to TUI4J.

## Upstream Repositories

TUI4J ports from these charmbracelet repositories:

| Repository | Description | Java Package |
|------------|-------------|--------------|
| [bubbletea](https://github.com/charmbracelet/bubbletea) | Core TUI framework | `com.williamcallahan.tui4j.compat.bubbletea.*` |
| [bubbles](https://github.com/charmbracelet/bubbles) | Reusable UI components | `com.williamcallahan.tui4j.compat.bubbles.*` |
| [lipgloss](https://github.com/charmbracelet/lipgloss) | Styling and layout | `com.williamcallahan.tui4j.compat.lipgloss.*` |
| [x](https://github.com/charmbracelet/x) | Experimental packages | `com.williamcallahan.tui4j.compat.x.*` |
| [harmonica](https://github.com/charmbracelet/harmonica) | Spring physics animation | `com.williamcallahan.tui4j.compat.harmonica.*` |

## Examples

Table presents an overall status of porting code available under **examples** directory of Bubble Tea codebase. The idea
is to rewrite all the code samples and enhance TUI4J logic with missing parts found during implementation.

Each entry below represents a single example code fragment and the status of its migration into Java equivalent which is
represented by one of the labels:

`⚪ TODO` No effort have been put into this one yet

`🟡 In Progress` It's currently under development, and it might be some notable changes will be applied to TUI4J logic to
conform it

`🟢 Done` It's done like DONE, the example works 100% the same as original code does.

`🔴 Won't do` It won't be done because of some technical limitations or was done some other way with explanation in **Notes**.

| Example           | Status        | Notes                                                              |
|-------------------|---------------|--------------------------------------------------------------------|
| altscreen-toggle  | `🟢 Done`     |                                                                    |
| autocomplete      | `🟢 Done`     | Uses **help**, **key** and **textinput** bubbles.                  |
| cellbuffer        | `🟢 Done`     | Uses **harmonica** spring physics.                                 |
| chat              | `🟢 Done`     | Uses **textarea** and **viewport** bubbles.                        |
| composable-views  | `🟢 Done`     | Uses **timer** bubble.                                             |
| credit-card-form  | `🟢 Done`     | Uses **textinput** with validation.                                |
| debounce          | `🟢 Done`     | Demonstrates rate limiting pattern.                                |
| exec              | `🟢 Done`     | Uses terminal suspend/resume for external commands.                |
| eyes              | `🟢 Done`     | Blinking eyes animation.                                           |
| file-picker       | `🟢 Done`     | Uses **filepicker** bubble.                                        |
| focus-blur        | `🟢 Done`     |                                                                    |
| fullscreen        | `🟢 Done`     |                                                                    |
| glamour           | `🟢 Done`     | Glamour not ported; renders raw markdown in a viewport.            |
| help              | `🟢 Done`     | Uses **help** and **key** bubbles.                                 |
| http              | `🟢 Done`     |                                                                    |
| list-default      | `🟢 Done`     |                                                                    |
| list-fancy        | `🟢 Done`     |                                                                    |
| list-simple       | `🟢 Done`     |                                                                    |
| mouse             | `🟢 Done`     |                                                                    |
| package-manager   | `🟢 Done`     | Uses **progress** bubble.                                          |
| pager             | `🟢 Done`     | Uses **viewport** bubble.                                          |
| paginator         | `🟢 Done`     |                                                                    |
| pipe              | `🟢 Done`     | Demonstrates piped input handling.                                 |
| prevent-quit      | `🟢 Done`     | Uses **help**, **key** and **textarea** bubbles.                   |
| progress-animated | `🟢 Done`     | Uses **progress** bubble with tick-based animation.                |
| progress-download | `🟢 Done`     | Uses **progress** bubble.                                          |
| progress-static   | `🟢 Done`     | Uses **progress** bubble.                                          |
| realtime          | `🟢 Done`     | Uses background thread and Program.send() for message injection.   |
| result            | `🟢 Done`     | Demonstrates Program.runWithFinalModel().                          |
| send-msg          | `🟢 Done`     | Demonstrates Program.send() for external message injection.        |
| sequence          | `🟢 Done`     | Nested sequences and batches not yet supported, needs revisiting.  |
| set-window-title  | `🟢 Done`     |                                                                    |
| simple            | `🟢 Done`     | Basic counter example.                                             |
| spinner           | `🟢 Done`     |                                                                    |
| spinners          | `🟢 Done`     | Multiple spinner styles demo.                                      |
| split-editors     | `🟢 Done`     | Uses **textarea** bubble.                                          |
| stopwatch         | `🟢 Done`     | Uses **help**, **key** and **stopwatch** bubbles.                  |
| suspend           | `🟢 Done`     | Uses JLine pause/resume for terminal state management.             |
| table             | `🟢 Done`     | Uses **table** bubble.                                             |
| table-resize      | `🟢 Done`     | Uses **table** bubble with dynamic sizing.                         |
| tabs              | `🟢 Done`     | Tab navigation with lipgloss styling.                              |
| textarea          | `🟢 Done`     | Standalone textarea demo.                                          |
| textinput         | `🟢 Done`     |                                                                    |
| textinputs        | `🟢 Done`     |                                                                    |
| timer             | `🟢 Done`     | Uses **timer** bubble.                                             |
| tui-daemon-combo  | `🟢 Done`     | CLI mode switching demo.                                           |
| views             | `🟢 Done`     | Multi-view navigation demo.                                        |
| window-size       | `🟢 Done`     |                                                                    |

## Bubbles

This table covers all the Bubble's ported so far. The same status labels apply.

| Bubble     | Status    | Notes                                                     |
|------------|-----------|-----------------------------------------------------------|
| cursor     | `🟢 Done` |                                                           |
| filepicker | `🟢 Done` | Directory browser with filtering and symlink support.     |
| help       | `🟢 Done` |                                                           |
| key        | `🟢 Done` |                                                           |
| list       | `🟢 Done` |                                                           |
| paginator  | `🟢 Done` |                                                           |
| progress   | `🟢 Done` | Spring animation, gradient colors, percentage display.    |
| runeutil   | `🟢 Done` |                                                           |
| spinner    | `🟢 Done` |                                                           |
| stopwatch  | `🟢 Done` | Go-style duration formatting, Elm Architecture compliant. |
| table      | `🟢 Done` | Columns, rows, keyboard navigation.                       |
| textarea   | `🟢 Done` | Multi-line editor with cursor movement and word ops.      |
| textinput  | `🟢 Done` |                                                           |
| timer      | `🟢 Done` | Countdown timer with start/stop/reset.                    |
| viewport   | `🟢 Done` | Scrollable content with keyboard nav and line wrapping.   |

## Lipgloss

This table represents porting status of each part of Lipgloss that can be anyway measured.

| What                         | Status    | Notes                                                     |
|------------------------------|-----------|-----------------------------------------------------------|
| Colors and color profiles    | `🟢 Done` | ANSI, ANSI256, RGB, HSL, adaptive colors.                 |
| Borders                      | `🟢 Done` | Normal, rounded, thick, double, hidden, etc.              |
| Margins and paddings         | `🟢 Done` |                                                           |
| Width and wrapping           | `🟢 Done` | Uses x/ansi for proper grapheme width calculation.        |
| Alignment                    | `🟢 Done` |                                                           |
| Max width and max height     | `🟢 Done` | Text truncation with ellipsis support.                    |
| Horizontal and vertical join | `🟢 Done` |                                                           |
| Whitespace                   | `🟢 Done` | Background fill with pattern support.                     |
| List component               | `🟢 Done` |                                                           |
| Tree component               | `🟢 Done` |                                                           |
| Table component              | `🟢 Done` |                                                           |

## x (Experimental Packages)

The charmbracelet/x repository contains experimental packages. TUI4J ports selected components.

### x/ansi (Ported)

| What                         | Status    | Notes                                                     |
|------------------------------|-----------|-----------------------------------------------------------|
| StringWidth                  | `🟢 Done` | Grapheme-aware string width using ICU4J.                  |
| Truncate                     | `🟢 Done` | ANSI-aware truncation with left/right variants.           |
| Cut                          | `🟢 Done` | Extracts visible portion of ANSI-styled text.             |
| Strip                        | `🟢 Done` | Removes all ANSI escape sequences.                        |
| GraphemeCluster              | `🟢 Done` | Proper Unicode grapheme cluster handling.                 |
| Parser (State machine)       | `🟢 Done` | State, Action, TransitionTable for ANSI parsing.          |
| Wrap                         | `⚪ TODO` | Word wrapping with ANSI awareness.                        |
| Style (SGR)                  | `⚪ TODO` | SGR (Select Graphic Rendition) utilities.                 |
| Hyperlink                    | `⚪ TODO` | OSC 8 hyperlink support.                                  |
| Cursor                       | `⚪ TODO` | Cursor movement sequences.                                |
| Mouse                        | `⚪ TODO` | Mouse input parsing and sequences.                        |
| Clipboard                    | `⚪ TODO` | OSC 52 clipboard access.                                  |
| Notification                 | `⚪ TODO` | Terminal notifications.                                   |
| Graphics (Sixel)             | `⚪ TODO` | Sixel graphics support.                                   |
| Kitty extensions             | `⚪ TODO` | Kitty terminal protocol extensions.                       |
| iTerm2 extensions            | `⚪ TODO` | iTerm2 protocol extensions.                               |

### x/cellbuf (Not Ported)

| What                         | Status    | Notes                                                     |
|------------------------------|-----------|-----------------------------------------------------------|
| Buffer                       | `⚪ TODO` | Cell buffer for efficient terminal rendering.             |
| Screen                       | `⚪ TODO` | Screen abstraction over cell buffer.                      |
| Writer                       | `⚪ TODO` | Buffered writer with diff-based updates.                  |

### x/colors (Not Ported)

| What                         | Status    | Notes                                                     |
|------------------------------|-----------|-----------------------------------------------------------|
| Colors                       | `⚪ TODO` | Color conversion and manipulation utilities.              |

### x/editor (Not Ported)

| What                         | Status    | Notes                                                     |
|------------------------------|-----------|-----------------------------------------------------------|
| Editor                       | `⚪ TODO` | Launch external text editor.                              |

### x/conpty (Not Ported)

| What                         | Status    | Notes                                                     |
|------------------------------|-----------|-----------------------------------------------------------|
| Conpty                       | `⚪ TODO` | Windows ConPTY integration.                               |

### x/errors (Not Ported)

| What                         | Status    | Notes                                                     |
|------------------------------|-----------|-----------------------------------------------------------|
| Errors                       | `⚪ TODO` | Error helpers (join).                                     |

### x/etag (Not Ported)

| What                         | Status    | Notes                                                     |
|------------------------------|-----------|-----------------------------------------------------------|
| Etag                         | `⚪ TODO` | ETag helpers.                                             |

## Harmonica

This table represents porting status of charmbracelet/harmonica spring physics.

| What                         | Status    | Notes                                                     |
|------------------------------|-----------|-----------------------------------------------------------|
| Spring                       | `🟢 Done` | Spring-based physics animation for smooth UI transitions. |

## Source File Mappings

### Bubble Tea

#### Root
| Go File                  | Status    | Java Mapping                         |
|--------------------------|-----------|--------------------------------------|
| `commands.go`            | `🟢 Done` | `Command.java`                       |
|                          |           | `BatchMessage.java`                  |
|                          |           | `SequenceMessage.java`               |
|                          |           | `SetWindowTitleMessage.java`         |
|                          |           | `OpenUrlMessage.java`                |
|                          |           | `CopyToClipboardMessage.java`        |
|                          |           | `ReadClipboardMessage.java`          |
| `commands_test.go`       | `🟢 Done` | `CommandsParityTest.java`            |
| `exec.go`                | `🟢 Done` | `ExecProcessMessage.java`            |
|                          |           | `ExecCompletedMessage.java`          |
| `exec_test.go`           | `🟢 Done` | `ExecTest.java`                      |
| `focus.go`               | `🟢 Done` | `FocusMessage.java`                  |
|                          |           | `BlurMessage.java`                   |
| `inputreader_other.go`   | `🟢 Done` | `input/InputHandler.java`            |
|                          |           | `input/NewInputHandler.java`         |
|                          |           | `input/OldLaggyInputHandler.java`    |
|                          |           | `input/NoopInputHandler.java`        |
| `inputreader_windows.go` | `🟢 Done` | `input/WindowsInputHandler.java`     |
| `key.go`                 | `🟢 Done` | `input/key/Key.java`                 |
|                          |           | `input/key/KeyType.java`             |
|                          |           | `input/key/KeyNames.java`            |
|                          |           | `input/key/KeyAliases.java`          |
|                          |           | `KeyPressMessage.java`               |
|                          |           | `PasteMessage.java`                  |
|                          |           | `UnknownInputByteMessage.java`       |
|                          |           | `UnknownSequenceMessage.java`        |
| `key_other.go`           | `🟢 Done` | `input/NewInputHandler.java`         |
| `key_sequences.go`       | `🟢 Done` | `input/key/Sequences.java`           |
|                          |           | `input/key/ExtendedSequences.java`   |
| `key_test.go`            | `🟢 Done` | `input/key/KeyTest.java`             |
| `key_windows.go`         | `🟢 Done` | `input/WindowsInputHandler.java`     |
| `logging.go`             | `🟢 Done` | `Logging.java`                       |
| `logging_test.go`        | `🟢 Done` | `LoggingTest.java`                   |
| `mouse.go`               | `🟢 Done` | `input/MouseMessage.java`            |
|                          |           | `input/MouseButton.java`             |
|                          |           | `input/MouseAction.java`             |
| `mouse_test.go`          | `🟢 Done` | `input/MouseTest.java`               |
| `nil_renderer.go`        | `🟢 Done` | `render/NilRenderer.java`            |
| `nil_renderer_test.go`   | `🟢 Done` | `render/NilRendererTest.java`        |
| `options.go`             | `🟢 Done` | `ProgramOption.java`                 |
| `options_test.go`        | `🟢 Done` | `ProgramOptionsTest.java`            |
| `renderer.go`            | `🟢 Done` | `render/Renderer.java`               |
| `screen.go`              | `🟢 Done` | `WindowSizeMessage.java`             |
|                          |           | `ClearScreenMessage.java`            |
|                          |           | `EnterAltScreenMessage.java`         |
|                          |           | `ExitAltScreenMessage.java`          |
|                          |           | `EnableBracketedPasteMessage.java`   |
|                          |           | `DisableBracketedPasteMessage.java`  |
|                          |           | `EnableMouseCellMotionMessage.java`  |
|                          |           | `EnableMouseAllMotionMessage.java`   |
|                          |           | `DisableMouseMessage.java`           |
|                          |           | `CheckWindowSizeMessage.java`        |
|                          |           | `ResetMouseCursorMessage.java`       |
|                          |           | `SetMouseCursorPointerMessage.java`  |
|                          |           | `SetMouseCursorTextMessage.java`     |
| `screen_test.go`         | `🟢 Done` | `ScreenTest.java`                    |
| `signals_unix.go`        | `🟢 Done` | `Program.java`                       |
| `signals_windows.go`     | `🟢 Done` | `Program.java`                       |
| `standard_renderer.go`   | `🟢 Done` | `render/StandardRenderer.java`       |
|                          |           | `PrintLineMessage.java`              |
| `tea.go`                 | `🟢 Done` | `Program.java`                       |
|                          |           | `Model.java`                         |
|                          |           | `Message.java`                       |
|                          |           | `MessageShim.java`                   |
|                          |           | `UpdateResult.java`                  |
|                          |           | `QuitMessage.java`                   |
|                          |           | `SuspendMessage.java`                |
|                          |           | `ResumeMessage.java`                 |
|                          |           | `ProgramException.java`              |
|                          |           | `ErrorMessage.java`                  |
| `tea_init.go`            | `🟢 Done` | `Program.java`                       |
| `tea_test.go`            | `🟢 Done` | `TeaTest.java`                       |
| `tty.go`                 | `🟢 Done` | `Program.java`                       |
| `tty_unix.go`            | `🟢 Done` | `Program.java`                       |
| `tty_windows.go`         | `🟢 Done` | `Program.java`                       |

#### Tutorials
| Go File                      | Status    | Java Mapping                                                              |
|------------------------------|-----------|---------------------------------------------------------------------------|
| `tutorials/basics/main.go`   | `🟢 Done` | `examples/compat/bubbletea/tutorials/basics/BasicsTutorialExample.java`   |
| `tutorials/commands/main.go` | `🟢 Done` | `examples/compat/bubbletea/tutorials/commands/CommandsTutorialExample.java` |

#### Examples
| Go File                                | Status    | Java Mapping                                                                     |
|----------------------------------------|-----------|----------------------------------------------------------------------------------|
| `examples/altscreen-toggle/main.go`    | `🟢 Done` | `examples/compat/bubbletea/altscreentoggle/AltScreenToggleExample.java`          |
| `examples/autocomplete/main.go`        | `🟢 Done` | `examples/compat/bubbletea/autocomplete/AutocompleteExample.java`                |
| `examples/cellbuffer/main.go`          | `🟢 Done` | `examples/compat/bubbletea/cellbuffer/CellExample.java`                          |
| `examples/chat/main.go`                | `🟢 Done` | `examples/compat/bubbletea/chat/ChatExample.java`                                |
| `examples/composable-views/main.go`    | `🟢 Done` | `examples/compat/bubbletea/composableviews/ComposableViewsExample.java`          |
| `examples/credit-card-form/main.go`    | `🟢 Done` | `examples/compat/bubbletea/creditcardform/CreditCardFormExample.java`            |
| `examples/debounce/main.go`            | `🟢 Done` | `examples/compat/bubbletea/debounce/DebounceExample.java`                        |
| `examples/exec/main.go`                | `🟢 Done` | `examples/compat/bubbletea/exec/ExecExample.java`                                |
| `examples/eyes/main.go`                | `🟢 Done` | `examples/compat/bubbletea/eyes/EyesExample.java`                                |
| `examples/file-picker/main.go`         | `🟢 Done` | `examples/compat/bubbletea/filepicker/FilePickerExample.java`                    |
| `examples/focus-blur/main.go`          | `🟢 Done` | `examples/compat/bubbletea/focusblur/FocusBlurExample.java`                      |
| `examples/fullscreen/main.go`          | `🟢 Done` | `examples/compat/bubbletea/fullscreen/FullscreenExample.java`                    |
| `examples/glamour/main.go`             | `🟢 Done` | `examples/compat/bubbletea/glamour/GlamourExample.java`                          |
| `examples/help/main.go`                | `🟢 Done` | `examples/compat/bubbletea/help/HelpExample.java`                                |
| `examples/http/main.go`                | `🟢 Done` | `examples/compat/bubbletea/http/HttpExample.java`                                |
| `examples/list-default/main.go`        | `🟢 Done` | `examples/compat/bubbletea/listdefault/ListDefaultExample.java`                  |
| `examples/list-fancy/delegate.go`      | `🟢 Done` | `examples/compat/bubbletea/listfancy/Delegate.java`                              |
| `examples/list-fancy/main.go`          | `🟢 Done` | `examples/compat/bubbletea/listfancy/ListFancyExample.java`                      |
|                                        |           | `examples/compat/bubbletea/listfancy/FancyItem.java`                             |
|                                        |           | `examples/compat/bubbletea/listfancy/Keys.java`                                  |
|                                        |           | `examples/compat/bubbletea/listfancy/Styles.java`                                |
| `examples/list-fancy/randomitems.go`   | `🟢 Done` | `examples/compat/bubbletea/listfancy/RandomItemGenerator.java`                   |
| `examples/list-simple/main.go`         | `🟢 Done` | `examples/compat/bubbletea/listsimple/ListSimpleExample.java`                    |
| `examples/mouse/main.go`               | `🟢 Done` | `examples/compat/bubbletea/mouse/MouseExample.java`                              |
| `examples/package-manager/main.go`     | `🟢 Done` | `examples/compat/bubbletea/progress/packagemanager/PackageManagerExample.java`   |
| `examples/package-manager/packages.go` | `🟢 Done` | `examples/compat/bubbletea/progress/packagemanager/PackageManagerExample.java`   |
| `examples/pager/main.go`               | `🟢 Done` | `examples/compat/bubbletea/pager/PagerExample.java`                              |
| `examples/paginator/main.go`           | `🟢 Done` | `examples/compat/bubbletea/paginator/PaginatorExample.java`                      |
| `examples/pipe/main.go`                | `🟢 Done` | `examples/compat/bubbletea/pipe/PipeExample.java`                                |
| `examples/prevent-quit/main.go`        | `🟢 Done` | `examples/compat/bubbletea/preventquit/PreventQuitExample.java`                  |
| `examples/progress-animated/main.go`   | `🟢 Done` | `examples/compat/bubbletea/progress/animated/ProgressAnimatedExample.java`       |
| `examples/progress-download/main.go`   | `🟢 Done` | `examples/compat/bubbletea/progress/download/ProgressDownloadExample.java`       |
| `examples/progress-download/tui.go`    | `🟢 Done` | `examples/compat/bubbletea/progress/download/ProgressDownloadExample.java`       |
| `examples/progress-static/main.go`     | `🟢 Done` | `examples/compat/bubbletea/progress/staticview/ProgressStaticExample.java`       |
| `examples/realtime/main.go`            | `🟢 Done` | `examples/compat/bubbletea/realtime/RealtimeExample.java`                        |
|                                        |           | `examples/compat/bubbletea/realtime/RealtimeMessage.java`                        |
| `examples/result/main.go`              | `🟢 Done` | `examples/compat/bubbletea/result/ResultExample.java`                            |
| `examples/send-msg/main.go`            | `🟢 Done` | `examples/compat/bubbletea/sendmsg/SendMessageExample.java`                      |
|                                        |           | `examples/compat/bubbletea/sendmsg/SendMessage.java`                             |
| `examples/sequence/main.go`            | `🟢 Done` | `examples/compat/bubbletea/sequence/SequenceExample.java`                        |
| `examples/set-window-title/main.go`    | `🟢 Done` | `examples/compat/bubbletea/setwindowtitle/SetWindowTitleExample.java`            |
| `examples/simple/main.go`              | `🟢 Done` | `examples/compat/bubbletea/simple/SimpleExample.java`                            |
|                                        |           | `examples/compat/bubbletea/simple/TickMessage.java`                              |
| `examples/simple/main_test.go`         | `🟢 Done` | `test/java/.../compat/bubbletea/examples/SimpleExampleTest.java`                 |
| `examples/spinner/main.go`             | `🟢 Done` | `examples/compat/bubbletea/spinner/SpinnerExample.java`                          |
| `examples/spinners/main.go`            | `🟢 Done` | `examples/compat/bubbletea/spinners/SpinnersExample.java`                        |
| `examples/split-editors/main.go`       | `🟢 Done` | `examples/compat/bubbletea/split/SplitEditorsExample.java`                       |
| `examples/stopwatch/main.go`           | `🟢 Done` | `examples/compat/bubbletea/stopwatch/StopwatchExample.java`                      |
| `examples/suspend/main.go`             | `🟢 Done` | `examples/compat/bubbletea/suspend/SuspendExample.java`                          |
| `examples/table-resize/main.go`        | `🟢 Done` | `examples/compat/bubbletea/tableresize/TableResizeExample.java`                  |
| `examples/table/main.go`               | `🟢 Done` | `examples/compat/bubbletea/table/TableExample.java`                              |
| `examples/tabs/main.go`                | `🟢 Done` | `examples/compat/bubbletea/tabs/TabsExample.java`                                |
| `examples/textarea/main.go`            | `🟢 Done` | `examples/compat/bubbletea/textarea/TextareaExample.java`                        |
| `examples/textinput/main.go`           | `🟢 Done` | `examples/compat/bubbletea/textinput/TextInputExample.java`                      |
| `examples/textinputs/main.go`          | `🟢 Done` | `examples/compat/bubbletea/textinputs/TextInputsExample.java`                    |
| `examples/timer/main.go`               | `🟢 Done` | `examples/compat/bubbletea/timer/TimerExample.java`                              |
| `examples/tui-daemon-combo/main.go`    | `🟢 Done` | `examples/compat/bubbletea/tuidemoncombo/DaemonComboExample.java`                |
| `examples/views/main.go`               | `🟢 Done` | `examples/compat/bubbletea/views/ViewsExample.java`                              |
| `examples/window-size/main.go`         | `🟢 Done` | `examples/compat/bubbletea/windowsize/WindowSizeExample.java`                    |

### Bubbles
| Go File                                    | Status    | Java Mapping                           |
|--------------------------------------------|-----------|----------------------------------------|
| `bubbles.go`                               | `⚪ TODO` | `<unmapped>`                           |
| `cursor/cursor.go`                         | `🟢 Done` | `cursor/Cursor.java`                   |
|                                            |           | `cursor/CursorMode.java`               |
| `cursor/cursor_test.go`                    | `🟢 Done` | `cursor/CursorTest.java`               |
| `filepicker/filepicker.go`                 | `🟢 Done` | `filepicker/FilePicker.java`           |
|                                            |           | `filepicker/KeyMap.java`               |
|                                            |           | `filepicker/Styles.java`               |
|                                            |           | `filepicker/DidSelectFileMessage.java` |
|                                            |           | `filepicker/DidSelectDirectoryMessage.java` |
| `filepicker/hidden_unix.go`                | `⚪ TODO` | `<unmapped>`                           |
| `filepicker/hidden_windows.go`             | `⚪ TODO` | `<unmapped>`                           |
| `help/help.go`                             | `🟢 Done` | `help/Help.java`                       |
|                                            |           | `help/KeyMap.java`                     |
|                                            |           | `help/Styles.java`                     |
| `help/help_test.go`                        | `🟢 Done` | `help/HelpTest.java`                   |
| `key/key.go`                               | `🟢 Done` | `key/Binding.java`                     |
|                                            |           | `key/Help.java`                        |
| `key/key_test.go`                          | `🟢 Done` | `key/BindingTest.java`                 |
| `list/defaultitem.go`                      | `🟢 Done` | `list/DefaultItem.java`                |
|                                            |           | `list/DefaultItemStyles.java`          |
|                                            |           | `list/DefaultDelegate.java`            |
| `list/keys.go`                             | `🟢 Done` | `list/KeyMap.java`                     |
| `list/list.go`                             | `🟢 Done` | `list/List.java`                       |
|                                            |           | `list/Item.java`                       |
|                                            |           | `list/ItemDelegate.java`               |
|                                            |           | `list/FilterState.java`                |
|                                            |           | `list/FilterFunction.java`             |
|                                            |           | `list/FilteredItem.java`               |
|                                            |           | `list/FetchedItems.java`               |
|                                            |           | `list/FetchedCurrentPageItems.java`    |
|                                            |           | `list/ListDataSource.java`             |
|                                            |           | `list/DefaultDataSource.java`          |
|                                            |           | `list/Rank.java`                       |
|                                            |           | `list/StatusMessageTimeoutMessage.java`|
|                                            |           | `list/fuzzy/FuzzyFilter.java`          |
|                                            |           | `list/fuzzy/Match.java`                |
| `list/list_test.go`                        | `🟢 Done` | `list/ListTest.java`                   |
| `list/style.go`                            | `🟢 Done` | `list/Styles.java`                     |
| `paginator/paginator.go`                   | `🟢 Done` | `paginator/Paginator.java`             |
|                                            |           | `paginator/KeyMap.java`                |
|                                            |           | `paginator/Bounds.java`                |
|                                            |           | `paginator/Type.java`                  |
| `paginator/paginator_test.go`              | `🟢 Done` | `paginator/PaginatorTest.java`         |
| `progress/progress.go`                     | `🟢 Done` | `progress/Progress.java`               |
|                                            |           | `progress/FrameMessage.java`           |
|                                            |           | `progress/SetPercentMessage.java`      |
|                                            |           | `progress/Spring.java`                 |
| `progress/progress_test.go`                | `🟢 Done` | `progress/ProgressTest.java`           |
| `runeutil/runeutil.go`                     | `🟢 Done` | `runeutil/Sanitizer.java`              |
| `runeutil/runeutil_test.go`                | `🟢 Done` | `runeutil/SanitizerTest.java`          |
| `spinner/spinner.go`                       | `🟢 Done` | `spinner/Spinner.java`                 |
|                                            |           | `spinner/SpinnerType.java`             |
|                                            |           | `spinner/TickMessage.java`             |
| `spinner/spinner_test.go`                  | `🟢 Done` | `spinner/SpinnerTest.java`             |
| `stopwatch/stopwatch.go`                   | `🟢 Done` | `stopwatch/Stopwatch.java`             |
|                                            |           | `stopwatch/StartStopMessage.java`      |
|                                            |           | `stopwatch/TickMessage.java`           |
|                                            |           | `stopwatch/ResetMessage.java`          |
| `table/table.go`                           | `🟢 Done` | `table/Table.java`                     |
|                                            |           | `table/Column.java`                    |
|                                            |           | `table/Row.java`                       |
|                                            |           | `table/Keys.java`                      |
|                                            |           | `table/Styles.java`                    |
| `table/table_test.go`                      | `🟢 Done` | `table/TableTest.java`                 |
| `textarea/memoization/memoization.go`      | `⚪ TODO` | `<unmapped>`                           |
| `textarea/memoization/memoization_test.go` | `⚪ TODO` | `<unmapped>`                           |
| `textarea/textarea.go`                     | `🟢 Done` | `textarea/Textarea.java`               |
| `textarea/textarea_test.go`                | `🟢 Done` | `textarea/TextareaTest.java`           |
| `textinput/textinput.go`                   | `🟢 Done` | `textinput/TextInput.java`             |
|                                            |           | `textinput/EchoMode.java`              |
|                                            |           | `textinput/Keys.java`                  |
| `textinput/textinput_test.go`              | `🟢 Done` | `textinput/TextInputTest.java`         |
| `timer/timer.go`                           | `🟢 Done` | `timer/Timer.java`                     |
|                                            |           | `timer/StartStopMessage.java`          |
|                                            |           | `timer/TickMessage.java`               |
|                                            |           | `timer/TimeoutMessage.java`            |
| `viewport/keymap.go`                       | `🟢 Done` | `viewport/KeyMap.java`                 |
| `viewport/viewport.go`                     | `🟢 Done` | `viewport/Viewport.java`               |
| `viewport/viewport_test.go`                | `🟢 Done` | `viewport/ViewportTest.java`           |

### Lipgloss

#### Root
| Go File          | Status    | Java Mapping                     |
|------------------|-----------|----------------------------------|
| `align.go`       | `🟢 Done` | `Alignment.java`                 |
|                  |           | `align/AlignmentDecorator.java`  |
| `align_test.go`  | `🟢 Done` | `align/AlignmentDecoratorTest.java` |
| `ansi_unix.go`   | `⚪ TODO` | `<unmapped>`                     |
| `ansi_windows.go`| `⚪ TODO` | `<unmapped>`                     |
| `borders.go`     | `🟢 Done` | `border/Border.java`             |
|                  |           | `border/StandardBorder.java`     |
|                  |           | `Borders.java`                   |
| `borders_test.go`| `🟢 Done` | `BordersTest.java`               |
| `color.go`       | `🟢 Done` | `color/Color.java`               |
|                  |           | `color/RGB.java`                 |
|                  |           | `color/HSL.java`                 |
|                  |           | `color/ANSIColor.java`           |
|                  |           | `color/ANSI256Color.java`        |
|                  |           | `color/AdaptiveColor.java`       |
|                  |           | `color/ColorProfile.java`        |
|                  |           | `color/TerminalColor.java`       |
|                  |           | `color/ANSIColors.java`          |
|                  |           | `color/ColorApplyStrategy.java`  |
|                  |           | `color/NoColor.java`             |
|                  |           | `color/RGBColor.java`            |
|                  |           | `color/RGBSupplier.java`         |
| `color_test.go`  | `🟢 Done` | `color/RGBTest.java`             |
|                  |           | `color/HSLTest.java`             |
| `get.go`         | `🟢 Done` | `Style.java`                     |
| `join.go`        | `🟢 Done` | `Join.java`                      |
|                  |           | `join/HorizontalJoinDecorator.java` |
|                  |           | `join/VerticalJoinDecorator.java`|
| `join_test.go`   | `🟢 Done` | `join/HorizontalJoinDecoratorTest.java` |
|                  |           | `join/VerticalJoinDecoratorTest.java` |
| `lipgloss.go`    | `🟢 Done` | `Style.java`                     |
|                  |           | `Renderer.java`                  |
| `position.go`    | `🟢 Done` | `Position.java`                  |
| `ranges.go`      | `🟢 Done` | `TextLines.java`                 |
| `ranges_test.go` | `🟢 Done` | `TextLinesTest.java`             |
| `renderer.go`    | `🟢 Done` | `Renderer.java`                  |
|                  |           | `Output.java`                    |
| `renderer_test.go`| `🟢 Done`| `RendererTest.java`              |
| `runes.go`       | `🟢 Done` | `Runes.java`                     |
| `runes_test.go`  | `🟢 Done` | `RunesTest.java`                 |
| `set.go`         | `🟢 Done` | `Style.java`                     |
| `size.go`        | `🟢 Done` | `Size.java`                      |
|                  |           | `Dimensions.java`                |
| `style.go`       | `🟢 Done` | `Style.java`                     |
|                  |           | `MarginDecorator.java`           |
|                  |           | `PaddingDecorator.java`          |
|                  |           | `PlacementDecorator.java`        |
| `style_test.go`  | `🟢 Done` | `StyleTest.java`                 |
| `unset.go`       | `🟢 Done` | `Style.java`                     |
| `whitespace.go`  | `🟢 Done` | `Whitespace.java`                |

#### List
| Go File              | Status    | Java Mapping          |
|----------------------|-----------|-----------------------|
| `list/enumerator.go` | `🟢 Done` | `ListEnumerator.java` |
| `list/list.go`       | `🟢 Done` | `List.java`           |
| `list/list_test.go`  | `🟢 Done` | `list/ListTest.java`  |

#### Table
| Go File               | Status    | Java Mapping              |
|-----------------------|-----------|---------------------------|
| `table/resizing.go`   | `🟢 Done` | `table/Table.java`        |
| `table/rows.go`       | `🟢 Done` | `table/Table.java`        |
|                       |           | `table/Data.java`         |
|                       |           | `table/StringData.java`   |
|                       |           | `table/Filter.java`       |
| `table/table.go`      | `🟢 Done` | `table/Table.java`        |
|                       |           | `table/Data.java`         |
|                       |           | `table/StringData.java`   |
|                       |           | `table/StyleFunc.java`    |
| `table/table_test.go` | `🟢 Done` | `table/TableTest.java`    |
| `table/util.go`       | `🟢 Done` | `table/Table.java`        |

#### Tree
| Go File                | Status    | Java Mapping                |
|------------------------|-----------|-----------------------------|
| `tree/children.go`     | `🟢 Done` | `tree/Children.java`        |
|                        |           | `tree/NodeChildren.java`    |
|                        |           | `tree/Filter.java`          |
| `tree/enumerator.go`   | `🟢 Done` | `tree/TreeEnumerator.java`  |
|                        |           | `tree/TreeIndenter.java`    |
| `tree/example_test.go` | `🟢 Done` | `tree/TreeExampleTest.java` |
| `tree/renderer.go`     | `🟢 Done` | `tree/Renderer.java`        |
|                        |           | `tree/StyleFunction.java`   |
| `tree/tree.go`         | `🟢 Done` | `tree/Tree.java`            |
|                        |           | `tree/TreeStyle.java`       |
|                        |           | `tree/Node.java`            |
|                        |           | `tree/Leaf.java`            |
| `tree/tree_test.go`    | `🟢 Done` | `tree/TreeTest.java`        |

#### Examples
| Go File                               | Status    | Java Mapping                                                             |
|---------------------------------------|-----------|--------------------------------------------------------------------------|
| `examples/layout/main.go`             | `🟢 Done` | `examples/compat/lipgloss/layout/StatusBarExample.java`                  |
|                                       |           | `examples/compat/lipgloss/layout/TabsExample.java`                       |
| `examples/list/duckduckgoose/main.go` | `⚪ TODO` | `<unmapped>`                                                             |
| `examples/list/glow/main.go`          | `🟢 Done` | `examples/compat/lipgloss/list/ListGlowExample.java`                     |
| `examples/list/grocery/main.go`       | `🟢 Done` | `examples/compat/lipgloss/list/ListGroceryExample.java`                  |
| `examples/list/roman/main.go`         | `🟢 Done` | `examples/compat/lipgloss/list/ListRomanExample.java`                    |
| `examples/list/simple/main.go`        | `🟢 Done` | `examples/compat/lipgloss/list/ListSimpleExample.java`                   |
| `examples/list/sublist/main.go`       | `🟢 Done` | `examples/compat/lipgloss/list/ListSublistExample.java`                  |
| `examples/ssh/main.go`                | `⚪ TODO` | `<unmapped>`                                                             |
| `examples/table/ansi/main.go`         | `🟢 Done` | `examples/compat/lipgloss/table/TableAnsiExample.java`                   |
| `examples/table/chess/main.go`        | `🟢 Done` | `examples/compat/lipgloss/table/TableChessExample.java`                  |
| `examples/table/languages/main.go`    | `🟢 Done` | `examples/compat/lipgloss/table/TableLanguagesExample.java`              |
| `examples/table/mindy/main.go`        | `🟢 Done` | `examples/compat/lipgloss/table/TableMindyExample.java`                  |
| `examples/table/pokemon/main.go`      | `🟢 Done` | `examples/compat/lipgloss/table/TablePokemonExample.java`                |
| `examples/tree/background/main.go`    | `🟢 Done` | `examples/compat/lipgloss/tree/TreeBackgroundExample.java`               |
| `examples/tree/files/main.go`         | `🟢 Done` | `examples/compat/lipgloss/tree/TreeFilesExample.java`                    |
| `examples/tree/makeup/main.go`        | `🟢 Done` | `examples/compat/lipgloss/tree/TreeMakeupExample.java`                   |
| `examples/tree/rounded/main.go`       | `🟢 Done` | `examples/compat/lipgloss/tree/TreeRoundedExample.java`                  |
| `examples/tree/simple/main.go`        | `🟢 Done` | `examples/compat/lipgloss/tree/TreeSimpleExample.java`                   |
| `examples/tree/styles/main.go`        | `🟢 Done` | `examples/compat/lipgloss/tree/TreeStylesExample.java`                   |
| `examples/tree/toggle/main.go`        | `🟢 Done` | `examples/compat/lipgloss/tree/TreeToggleExample.java`                   |

### Harmonica
| Go File             | Status    | Java Mapping         |
|---------------------|-----------|----------------------|
| `harmonica.go`      | `🟢 Done` | `package-info.java`  |
| `projectile.go`     | `🟢 Done` | `Projectile.java`    |
|                     |           | `Point.java`         |
|                     |           | `Vector.java`        |
| `projectile_test.go`| `🟢 Done` | `ProjectileTest.java`|
| `spring.go`         | `🟢 Done` | `Spring.java`        |

#### Examples
| Go File                          | Status    | Java Mapping |
|----------------------------------|-----------|--------------|
| `examples/particle/main.go`      | `⚪ TODO` | `<unmapped>` |
| `examples/spring/opengl/main.go` | `⚪ TODO` | `<unmapped>` |
| `examples/spring/tui/main.go`    | `⚪ TODO` | `<unmapped>` |

### x

#### Ansi Package / Root Files
| Go File                      | Status            | Java Mapping               |
|------------------------------|-------------------|----------------------------|
| `ansi/ansi.go`               | `⚪ TODO`         | `<unmapped>`               |
| `ansi/ascii.go`              | `🟢 Done`         | `ansi/Ansi.java`           |
| `ansi/background.go`         | `⚪ TODO`         | `<unmapped>`               |
| `ansi/background_test.go`    | `⚪ TODO`         | `<unmapped>`               |
| `ansi/c0.go`                 | `🟢 Done`         | `ansi/Ansi.java`           |
| `ansi/c1.go`                 | `🟢 Done`         | `ansi/Ansi.java`           |
| `ansi/charset.go`            | `⚪ TODO`         | `<unmapped>`               |
| `ansi/clipboard.go`          | `🟡 In Progress`  | `ansi/Code.java`           |
| `ansi/clipboard_test.go`     | `⚪ TODO`         | `<unmapped>`               |
| `ansi/color.go`              | `⚪ TODO`         | `<unmapped>`               |
| `ansi/color_test.go`         | `⚪ TODO`         | `<unmapped>`               |
| `ansi/ctrl.go`               | `⚪ TODO`         | `<unmapped>`               |
| `ansi/cursor.go`             | `⚪ TODO`         | `<unmapped>`               |
| `ansi/cwd.go`                | `⚪ TODO`         | `<unmapped>`               |
| `ansi/cwd_test.go`           | `⚪ TODO`         | `<unmapped>`               |
| `ansi/doc.go`                | `⚪ TODO`         | `<unmapped>`               |
| `ansi/finalterm.go`          | `⚪ TODO`         | `<unmapped>`               |
| `ansi/focus.go`              | `⚪ TODO`         | `<unmapped>`               |
| `ansi/gen.go`                | `⚪ TODO`         | `<unmapped>`               |
| `ansi/graphics.go`           | `⚪ TODO`         | `<unmapped>`               |
| `ansi/graphics_test.go`      | `⚪ TODO`         | `<unmapped>`               |
| `ansi/hyperlink.go`          | `⚪ TODO`         | `<unmapped>`               |
| `ansi/hyperlink_test.go`     | `⚪ TODO`         | `<unmapped>`               |
| `ansi/inband.go`             | `⚪ TODO`         | `<unmapped>`               |
| `ansi/iterm2.go`             | `⚪ TODO`         | `<unmapped>`               |
| `ansi/keypad.go`             | `⚪ TODO`         | `<unmapped>`               |
| `ansi/kitty.go`              | `⚪ TODO`         | `<unmapped>`               |
| `ansi/method.go`             | `🟢 Done`         | `ansi/Method.java`         |
| `ansi/mode.go`               | `⚪ TODO`         | `<unmapped>`               |
| `ansi/mode_deprecated.go`    | `⚪ TODO`         | `<unmapped>`               |
| `ansi/mode_test.go`          | `⚪ TODO`         | `<unmapped>`               |
| `ansi/modes.go`              | `⚪ TODO`         | `<unmapped>`               |
| `ansi/mouse.go`              | `⚪ TODO`         | `<unmapped>`               |
| `ansi/mouse_test.go`         | `⚪ TODO`         | `<unmapped>`               |
| `ansi/notification.go`       | `⚪ TODO`         | `<unmapped>`               |
| `ansi/notification_test.go`  | `⚪ TODO`         | `<unmapped>`               |
| `ansi/palette.go`            | `⚪ TODO`         | `<unmapped>`               |
| `ansi/palette_test.go`       | `⚪ TODO`         | `<unmapped>`               |
| `ansi/parser.go`             | `⚪ TODO`         | `<unmapped>`               |
| `ansi/parser_apc_test.go`    | `⚪ TODO`         | `<unmapped>`               |
| `ansi/parser_csi_test.go`    | `⚪ TODO`         | `<unmapped>`               |
| `ansi/parser_dcs_test.go`    | `⚪ TODO`         | `<unmapped>`               |
| `ansi/parser_decode.go`      | `⚪ TODO`         | `<unmapped>`               |
| `ansi/parser_decode_test.go` | `⚪ TODO`         | `<unmapped>`               |
| `ansi/parser_esc_test.go`    | `⚪ TODO`         | `<unmapped>`               |
| `ansi/parser_handler.go`     | `⚪ TODO`         | `<unmapped>`               |
| `ansi/parser_osc_test.go`    | `⚪ TODO`         | `<unmapped>`               |
| `ansi/parser_sync.go`        | `⚪ TODO`         | `<unmapped>`               |
| `ansi/parser_test.go`        | `⚪ TODO`         | `<unmapped>`               |
| `ansi/passthrough.go`        | `⚪ TODO`         | `<unmapped>`               |
| `ansi/passthrough_test.go`   | `⚪ TODO`         | `<unmapped>`               |
| `ansi/paste.go`              | `🟡 In Progress`  | `ansi/Code.java`           |
| `ansi/progress.go`           | `⚪ TODO`         | `<unmapped>`               |
| `ansi/progress_test.go`      | `⚪ TODO`         | `<unmapped>`               |
| `ansi/reset.go`              | `⚪ TODO`         | `<unmapped>`               |
| `ansi/screen.go`             | `⚪ TODO`         | `<unmapped>`               |
| `ansi/sgr.go`                | `⚪ TODO`         | `<unmapped>`               |
| `ansi/sgr_test.go`           | `⚪ TODO`         | `<unmapped>`               |
| `ansi/status.go`             | `⚪ TODO`         | `<unmapped>`               |
| `ansi/style.go`              | `⚪ TODO`         | `<unmapped>`               |
| `ansi/style_test.go`         | `⚪ TODO`         | `<unmapped>`               |
| `ansi/termcap.go`            | `⚪ TODO`         | `<unmapped>`               |
| `ansi/title.go`              | `⚪ TODO`         | `<unmapped>`               |
| `ansi/title_test.go`         | `⚪ TODO`         | `<unmapped>`               |
| `ansi/truncate.go`           | `🟢 Done`         | `ansi/Truncate.java`       |
|                              |                   | `ansi/Cut.java`            |
| `ansi/truncate_test.go`      | `🟢 Done`         | `ansi/TruncateTest.java`   |
|                              |                   | `ansi/CutTest.java`        |
| `ansi/urxvt.go`              | `⚪ TODO`         | `<unmapped>`               |
| `ansi/urxvt_test.go`         | `⚪ TODO`         | `<unmapped>`               |
| `ansi/util.go`               | `⚪ TODO`         | `<unmapped>`               |
| `ansi/width.go`              | `🟢 Done`         | `ansi/StringWidth.java`    |
|                              |                   | `ansi/GraphemeCluster.java`|
|                              |                   | `ansi/Strip.java`          |
| `ansi/width_test.go`         | `🟢 Done`         | `ansi/StringWidthTest.java`|
| `ansi/winop.go`              | `⚪ TODO`         | `<unmapped>`               |
| `ansi/wrap.go`               | `⚪ TODO`         | `<unmapped>`               |
| `ansi/wrap_test.go`          | `⚪ TODO`         | `<unmapped>`               |
| `ansi/xterm.go`              | `⚪ TODO`         | `<unmapped>`               |

#### Ansi Package / iterm2
| Go File                      | Status    | Java Mapping |
|------------------------------|-----------|--------------|
| `ansi/iterm2/file.go`        | `⚪ TODO` | `<unmapped>` |
| `ansi/iterm2/file_test.go`   | `⚪ TODO` | `<unmapped>` |
| `ansi/iterm2/iterm2_test.go` | `⚪ TODO` | `<unmapped>` |

#### Ansi Package / kitty
| Go File                      | Status    | Java Mapping |
|------------------------------|-----------|--------------|
| `ansi/kitty/decoder.go`      | `⚪ TODO` | `<unmapped>` |
| `ansi/kitty/decoder_test.go` | `⚪ TODO` | `<unmapped>` |
| `ansi/kitty/encoder.go`      | `⚪ TODO` | `<unmapped>` |
| `ansi/kitty/encoder_test.go` | `⚪ TODO` | `<unmapped>` |
| `ansi/kitty/graphics.go`     | `⚪ TODO` | `<unmapped>` |
| `ansi/kitty/options.go`      | `⚪ TODO` | `<unmapped>` |
| `ansi/kitty/options_test.go` | `⚪ TODO` | `<unmapped>` |
| `ansi/kitty/writer.go`       | `⚪ TODO` | `<unmapped>` |
| `ansi/kitty/writer_test.go`  | `⚪ TODO` | `<unmapped>` |

#### Ansi Package / parser
| Go File                           | Status    | Java Mapping                      |
|-----------------------------------|-----------|-----------------------------------|
| `ansi/parser/const.go`            | `🟢 Done` | `ansi/parser/State.java`          |
|                                   |           | `ansi/parser/Action.java`         |
| `ansi/parser/seq.go`              | `🟢 Done` | `ansi/parser/Action.java`         |
| `ansi/parser/transition_table.go` | `🟢 Done` | `ansi/parser/TransitionTable.java`|

#### Ansi Package / sixel
| Go File                          | Status    | Java Mapping |
|----------------------------------|-----------|--------------|
| `ansi/sixel/color.go`            | `⚪ TODO` | `<unmapped>` |
| `ansi/sixel/color_test.go`       | `⚪ TODO` | `<unmapped>` |
| `ansi/sixel/decoder.go`          | `⚪ TODO` | `<unmapped>` |
| `ansi/sixel/encoder.go`          | `⚪ TODO` | `<unmapped>` |
| `ansi/sixel/palette.go`          | `⚪ TODO` | `<unmapped>` |
| `ansi/sixel/palette_sort.go`     | `⚪ TODO` | `<unmapped>` |
| `ansi/sixel/palette_test.go`     | `⚪ TODO` | `<unmapped>` |
| `ansi/sixel/raster.go`           | `⚪ TODO` | `<unmapped>` |
| `ansi/sixel/raster_test.go`      | `⚪ TODO` | `<unmapped>` |
| `ansi/sixel/repeat.go`           | `⚪ TODO` | `<unmapped>` |
| `ansi/sixel/repeat_test.go`      | `⚪ TODO` | `<unmapped>` |
| `ansi/sixel/sixel_bench_test.go` | `⚪ TODO` | `<unmapped>` |
| `ansi/sixel/sixel_test.go`       | `⚪ TODO` | `<unmapped>` |

#### Cellbuf Package
| Go File                    | Status    | Java Mapping |
|----------------------------|-----------|--------------|
| `cellbuf/buffer.go`        | `⚪ TODO` | `<unmapped>` |
| `cellbuf/buffer_test.go`   | `⚪ TODO` | `<unmapped>` |
| `cellbuf/cell.go`          | `⚪ TODO` | `<unmapped>` |
| `cellbuf/errors.go`        | `⚪ TODO` | `<unmapped>` |
| `cellbuf/geom.go`          | `⚪ TODO` | `<unmapped>` |
| `cellbuf/hardscroll.go`    | `⚪ TODO` | `<unmapped>` |
| `cellbuf/hashmap.go`       | `⚪ TODO` | `<unmapped>` |
| `cellbuf/link.go`          | `⚪ TODO` | `<unmapped>` |
| `cellbuf/pen.go`           | `⚪ TODO` | `<unmapped>` |
| `cellbuf/screen.go`        | `⚪ TODO` | `<unmapped>` |
| `cellbuf/sequence.go`      | `⚪ TODO` | `<unmapped>` |
| `cellbuf/sequence_test.go` | `⚪ TODO` | `<unmapped>` |
| `cellbuf/style.go`         | `⚪ TODO` | `<unmapped>` |
| `cellbuf/tabstop.go`       | `⚪ TODO` | `<unmapped>` |
| `cellbuf/tabstop_test.go`  | `⚪ TODO` | `<unmapped>` |
| `cellbuf/utils.go`         | `⚪ TODO` | `<unmapped>` |
| `cellbuf/wrap.go`          | `⚪ TODO` | `<unmapped>` |
| `cellbuf/wrap_test.go`     | `⚪ TODO` | `<unmapped>` |
| `cellbuf/writer.go`        | `⚪ TODO` | `<unmapped>` |

#### Colors Package
| Go File            | Status    | Java Mapping |
|--------------------|-----------|--------------|
| `colors/colors.go` | `⚪ TODO` | `<unmapped>` |

#### Conpty Package
| Go File                    | Status    | Java Mapping |
|----------------------------|-----------|--------------|
| `conpty/conpty.go`         | `⚪ TODO` | `<unmapped>` |
| `conpty/conpty_other.go`   | `⚪ TODO` | `<unmapped>` |
| `conpty/conpty_windows.go` | `⚪ TODO` | `<unmapped>` |
| `conpty/doc.go`            | `⚪ TODO` | `<unmapped>` |
| `conpty/exec_windows.go`   | `⚪ TODO` | `<unmapped>` |

#### Editor Package
| Go File                 | Status    | Java Mapping |
|-------------------------|-----------|--------------|
| `editor/editor.go`      | `⚪ TODO` | `<unmapped>` |
| `editor/editor_test.go` | `⚪ TODO` | `<unmapped>` |

#### Errors Package
| Go File              | Status    | Java Mapping |
|----------------------|-----------|--------------|
| `errors/join.go`     | `⚪ TODO` | `<unmapped>` |
| `errors/join_test.go`| `⚪ TODO` | `<unmapped>` |

#### Etag Package
| Go File            | Status    | Java Mapping |
|--------------------|-----------|--------------|
| `etag/etag.go`     | `⚪ TODO` | `<unmapped>` |
| `etag/etag_test.go`| `⚪ TODO` | `<unmapped>` |

### Bubble Tea - TUI4J Examples (With No Upstream Equivalent)
| Java File                                                       | Status    | Notes |
|-----------------------------------------------------------------|-----------|-------|
| `examples/compat/bubbletea/BorderTest.java`                     | `🟢 Done` |       |
| `examples/compat/bubbletea/ExamplesRunner.java`                 | `🟢 Done` |       |
| `examples/compat/bubbletea/conway/Conway.java`                  | `🟢 Done` |       |
| `examples/compat/bubbletea/conway/ConwayGame.java`              | `🟢 Done` |       |
| `examples/compat/bubbletea/counter/CounterExample.java`         | `🟢 Done` |       |
| `examples/compat/bubbletea/counter/CounterMessage.java`         | `🟢 Done` |       |
| `examples/compat/bubbletea/cursor/CursorExample.java`           | `🟢 Done` |       |
| `examples/compat/bubbletea/demo/Demo.java`                      | `🟢 Done` |       |
| `examples/compat/bubbletea/error/ErrorExample.java`             | `🟢 Done` |       |
| `examples/compat/bubbletea/fireworks/Fireworks.java`            | `🟢 Done` |       |
| `examples/compat/bubbletea/progress/staticbar/ProgressStaticExample.java` | `🟢 Done` |       |
| `examples/compat/bubbletea/terminfo/TermInfoExample.java`       | `🟢 Done` |       |
| `examples/compat/bubbletea/tetris/Block.java`                   | `🟢 Done` |       |
| `examples/compat/bubbletea/tetris/GameOverMessage.java`         | `🟢 Done` |       |
| `examples/compat/bubbletea/tetris/Grid.java`                    | `🟢 Done` |       |
| `examples/compat/bubbletea/tetris/Position.java`                | `🟢 Done` |       |
| `examples/compat/bubbletea/tetris/TetrisGame.java`              | `🟢 Done` |       |
| `examples/compat/bubbletea/tetris/Tetromino.java`               | `🟢 Done` |       |
| `examples/compat/bubbletea/tetris/TetrominoInstance.java`       | `🟢 Done` |       |
| `examples/compat/bubbletea/width/WidthExample.java`             | `🟢 Done` |       |

### Lipgloss - TUI4J Extensions (No Upstream Equivalent)
| Java File                                      | Status    | Notes |
|------------------------------------------------|-----------|-------|
| `compat/lipgloss/SizeTest.java`                | `🟢 Done` |       |
