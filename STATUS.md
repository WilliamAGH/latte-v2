# Porting status

This page covers the current status of porting Bubble Tea to TUI4J.

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
| autocomplete      | `⚪ TODO`     | Needs **help**, **key** and **textinput** bubbles.                 |
| cellbuffer        | `⚪ TODO`     | Any reasonable port of **harmonica** is needed.                    |
| chat              | `⚪ TODO`     | Needs **textarea** and **viewport** bubbles.                       |
| composable-views  | `⚪ TODO`     | Needs **timer** bubble.                                            |
| credit-card-form  | `⚪ TODO`     |                                                                    |
| debounce          | `⚪ TODO`     |                                                                    |
| exec              | `⚪ TODO`     |                                                                    |
| file-picker       | `⚪ TODO`     | Needs **filepicker** bubble.                                       |
| focus-blur        | `🟢 Done`     |                                                                    |
| fullscreen        | `🟢 Done`     |                                                                    |
| glamour           | `⚪ TODO`     | Needs any reasonable port of **glamour**.                          |
| help              | `⚪ TODO`     | Needs **help** and **key** bubbles.                                |
| http              | `🟢 Done`     |                                                                    |
| list-default      | `🟢 Done`     |                                                                    |
| list-fancy        | `🟢 Done`     |                                                                    |
| list-simple       | `🟢 Done`     |                                                                    |
| mouse             | `🟢 Done`     |                                                                    |
| package-manager   | `⚪ TODO`     | Needs **progress** bubble.                                         |
| pager             | `⚪ TODO`     | Needs **viewport** bubble.                                         |
| paginator         | `🟢 Done`     |                                                                    |
| pipe              | `⚪ TODO`     |                                                                    |
| prevent-quit      | `⚪ TODO`     | Needs **help**, **key** and **textarea** bubbles.                  |
| progress-animated | `⚪ TODO`     | Needs **progress** bubble.                                         |
| progress-download | `⚪ TODO`     | Needs **progress** bubble.                                         |
| progress-static   | `⚪ TODO`     | Needs **progress** bubble.                                         |
| realtime          | `⚪ TODO`     |                                                                    |
| result            | `🔴 Won't do` | Rewritten as **demo** example.                                     |
| send-msg          | `⚪ TODO`     |                                                                    |
| sequence          | `🟢 Done`     | Nested sequences and batches not yet supported, needs revisiting.  |
| set-window-title  | `🟢 Done`     |                                                                    |
| simple            | `⚪ TODO`     |                                                                    |
| spinner           | `🟢 Done`     |                                                                    |
| spinners          | `⚪ TODO`     |                                                                    |
| split-editors     | `⚪ TODO`     |                                                                    |
| stopwatch         | `⚪ TODO`     | Needs **help**, **key** and **stopwatch** bubbles.                 |
| suspend           | `⚪ TODO`     |                                                                    |
| table             | `⚪ TODO`     | Needs **table** bubble.                                            |
| table-resize      | `⚪ TODO`     |                                                                    |
| tabs              | `⚪ TODO`     |                                                                    |
| textarea          | `⚪ TODO`     | Needs **textarea** bubble.                                         |
| textinput         | `🟢 Done`     |                                                                    |
| textinputs        | `🟢 Done`     |                                                                    |
| timer             | `⚪ TODO`     | Needs **help**, **key** and **timer** bubbles.                     |
| tui-daemon-combo  | `⚪ TODO`     |                                                                    |
| views             | `⚪ TODO`     |                                                                    |
| window-size       | `🟢 Done`     |                                                                    |

## Bubbles

This table covers all the Bubble's ported so far. The same status labels apply.

| Bubble     | Status    | Notes                          |
|------------|-----------|--------------------------------|
| cursor     | `🟢 Done` |                                |
| filepicker | `⚪ TODO` |                                |
| help       | `🟢 Done` |                                |
| key        | `🟢 Done` |                                |
| list       | `🟢 Done` |                                |
| paginator  | `🟢 Done` |                                |
| progress   | `⚪ TODO` |                                |
| runeutil   | `🟢 Done` |                                |
| spinner    | `🟢 Done` |                                |
| stopwatch  | `⚪ TODO` |                                |
| table      | `⚪ TODO` | Needs **table** from lipgloss. |
| textarea   | `⚪ TODO` |                                |
| textinput  | `🟢 Done` |                                |
| timer      | `⚪ TODO` |                                |
| viewport   | `⚪ TODO` |                                |

## Lipgloss

This table represents porting status of each part of Lipgloss that can be anyway measured.

| What                         | Status    | Notes                                                     |
|------------------------------|-----------|-----------------------------------------------------------|
| Colors and color profiles    | `🟢 Done` |                                                           |
| Borders                      | `🟢 Done` |                                                           |
| Margins and paddings         | `🟢 Done` |                                                           |
| Width and wrapping           | `🟢 Done` | Some text width tests failing (OSC, CSI).                 |
| Alignment                    | `🟢 Done` |                                                           |
| Max width and max height     | `⚪ TODO` |                                                           |
| Horizontal and vertical join | `🟢 Done` |                                                           |
| List component               | `🟢 Done` |                                                           |
| Tree component               | `🟢 Done` |                                                           |
| Table component              | `⚪ TODO` |                                                           |
