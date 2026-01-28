# X Compatibility Map

- **Source Repo**: [charmbracelet/x](https://github.com/charmbracelet/x)
- **Copyright**: Copyright (c) 2023 Charmbracelet, Inc.
- **Java Package**: `com.williamcallahan.tui4j.compat.x`

See AGENTS.md rules DPR5–DPR11 for naming and deprecation policy.

---

## Ansi Package

**Java Package**: `com.williamcallahan.tui4j.compat.x.ansi`

### Root Files

- [ ] **ansi/ansi.go** → `<unmapped>`
- [x] **ansi/ascii.go** → `ansi/Ansi.java`
- [ ] **ansi/background.go** → `<unmapped>`
- [ ] **ansi/background_test.go** → `<unmapped>`
- [x] **ansi/c0.go** → `ansi/Ansi.java`
- [x] **ansi/c1.go** → `ansi/Ansi.java`
- [ ] **ansi/charset.go** → `<unmapped>`
- [x] **ansi/clipboard.go** → `src/main/java/com/williamcallahan/tui4j/ansi/Code.java` *(partial)*
- [ ] **ansi/clipboard_test.go** → `<unmapped>`
- [ ] **ansi/color.go** → `<unmapped>`
- [ ] **ansi/color_test.go** → `<unmapped>`
- [ ] **ansi/ctrl.go** → `<unmapped>`
- [ ] **ansi/cursor.go** → `<unmapped>`
- [ ] **ansi/cwd.go** → `<unmapped>`
- [ ] **ansi/cwd_test.go** → `<unmapped>`
- [ ] **ansi/doc.go** → `<unmapped>`
- [ ] **ansi/finalterm.go** → `<unmapped>`
- [ ] **ansi/focus.go** → `<unmapped>`
- [ ] **ansi/gen.go** → `<unmapped>`
- [ ] **ansi/graphics.go** → `<unmapped>`
- [ ] **ansi/graphics_test.go** → `<unmapped>`
- [ ] **ansi/hyperlink.go** → `<unmapped>`
- [ ] **ansi/hyperlink_test.go** → `<unmapped>`
- [ ] **ansi/inband.go** → `<unmapped>`
- [ ] **ansi/iterm2.go** → `<unmapped>`
- [ ] **ansi/keypad.go** → `<unmapped>`
- [ ] **ansi/kitty.go** → `<unmapped>`
- [x] **ansi/method.go** → `ansi/Method.java`
- [ ] **ansi/mode.go** → `<unmapped>`
- [ ] **ansi/mode_deprecated.go** → `<unmapped>`
- [ ] **ansi/mode_test.go** → `<unmapped>`
- [ ] **ansi/modes.go** → `<unmapped>`
- [ ] **ansi/mouse.go** → `<unmapped>`
- [ ] **ansi/mouse_test.go** → `<unmapped>`
- [ ] **ansi/notification.go** → `<unmapped>`
- [ ] **ansi/notification_test.go** → `<unmapped>`
- [ ] **ansi/palette.go** → `<unmapped>`
- [ ] **ansi/palette_test.go** → `<unmapped>`
- [ ] **ansi/parser.go** → `<unmapped>`
- [ ] **ansi/parser_apc_test.go** → `<unmapped>`
- [ ] **ansi/parser_csi_test.go** → `<unmapped>`
- [ ] **ansi/parser_dcs_test.go** → `<unmapped>`
- [ ] **ansi/parser_decode.go** → `<unmapped>`
- [ ] **ansi/parser_decode_test.go** → `<unmapped>`
- [ ] **ansi/parser_esc_test.go** → `<unmapped>`
- [ ] **ansi/parser_handler.go** → `<unmapped>`
- [ ] **ansi/parser_osc_test.go** → `<unmapped>`
- [ ] **ansi/parser_sync.go** → `<unmapped>`
- [ ] **ansi/parser_test.go** → `<unmapped>`
- [ ] **ansi/passthrough.go** → `<unmapped>`
- [ ] **ansi/passthrough_test.go** → `<unmapped>`
- [x] **ansi/paste.go** → `src/main/java/com/williamcallahan/tui4j/ansi/Code.java` *(partial)*
- [ ] **ansi/progress.go** → `<unmapped>`
- [ ] **ansi/progress_test.go** → `<unmapped>`
- [ ] **ansi/reset.go** → `<unmapped>`
- [ ] **ansi/screen.go** → `<unmapped>`
- [ ] **ansi/sgr.go** → `<unmapped>`
- [ ] **ansi/sgr_test.go** → `<unmapped>`
- [ ] **ansi/status.go** → `<unmapped>`
- [ ] **ansi/style.go** → `<unmapped>`
- [ ] **ansi/style_test.go** → `<unmapped>`
- [ ] **ansi/termcap.go** → `<unmapped>`
- [ ] **ansi/title.go** → `<unmapped>`
- [ ] **ansi/title_test.go** → `<unmapped>`

- [x] **ansi/truncate.go**
  - `ansi/Truncate.java`
  - `ansi/Cut.java`

- [x] **ansi/truncate_test.go**
  - `ansi/TruncateTest.java`
  - `ansi/CutTest.java`
- [ ] **ansi/urxvt.go** → `<unmapped>`
- [ ] **ansi/urxvt_test.go** → `<unmapped>`
- [ ] **ansi/util.go** → `<unmapped>`

- [x] **ansi/width.go**
  - `ansi/StringWidth.java`
  - `ansi/GraphemeCluster.java`
  - `ansi/Strip.java`

- [x] **ansi/width_test.go** → `ansi/StringWidthTest.java`
- [ ] **ansi/winop.go** → `<unmapped>`
- [ ] **ansi/wrap.go** → `<unmapped>`
- [ ] **ansi/wrap_test.go** → `<unmapped>`
- [ ] **ansi/xterm.go** → `<unmapped>`

---

### iterm2

- [ ] **ansi/iterm2/file.go** → `<unmapped>`
- [ ] **ansi/iterm2/file_test.go** → `<unmapped>`
- [ ] **ansi/iterm2/iterm2_test.go** → `<unmapped>`

---

### kitty

- [ ] **ansi/kitty/decoder.go** → `<unmapped>`
- [ ] **ansi/kitty/decoder_test.go** → `<unmapped>`
- [ ] **ansi/kitty/encoder.go** → `<unmapped>`
- [ ] **ansi/kitty/encoder_test.go** → `<unmapped>`
- [ ] **ansi/kitty/graphics.go** → `<unmapped>`
- [ ] **ansi/kitty/options.go** → `<unmapped>`
- [ ] **ansi/kitty/options_test.go** → `<unmapped>`
- [ ] **ansi/kitty/writer.go** → `<unmapped>`
- [ ] **ansi/kitty/writer_test.go** → `<unmapped>`

---

### parser

**Java Package**: `com.williamcallahan.tui4j.compat.x.ansi.parser`

- [x] **ansi/parser/const.go**
  - `ansi/parser/State.java`
  - `ansi/parser/Action.java`

- [x] **ansi/parser/seq.go** → `ansi/parser/Action.java`

- [x] **ansi/parser/transition_table.go** → `ansi/parser/TransitionTable.java`

---

### sixel

- [ ] **ansi/sixel/color.go** → `<unmapped>`
- [ ] **ansi/sixel/color_test.go** → `<unmapped>`
- [ ] **ansi/sixel/decoder.go** → `<unmapped>`
- [ ] **ansi/sixel/encoder.go** → `<unmapped>`
- [ ] **ansi/sixel/palette.go** → `<unmapped>`
- [ ] **ansi/sixel/palette_sort.go** → `<unmapped>`
- [ ] **ansi/sixel/palette_test.go** → `<unmapped>`
- [ ] **ansi/sixel/raster.go** → `<unmapped>`
- [ ] **ansi/sixel/raster_test.go** → `<unmapped>`
- [ ] **ansi/sixel/repeat.go** → `<unmapped>`
- [ ] **ansi/sixel/repeat_test.go** → `<unmapped>`
- [ ] **ansi/sixel/sixel_bench_test.go** → `<unmapped>`
- [ ] **ansi/sixel/sixel_test.go** → `<unmapped>`

---

## Cellbuf Package

- [ ] **cellbuf/buffer.go** → `<unmapped>`
- [ ] **cellbuf/buffer_test.go** → `<unmapped>`
- [ ] **cellbuf/cell.go** → `<unmapped>`
- [ ] **cellbuf/errors.go** → `<unmapped>`
- [ ] **cellbuf/geom.go** → `<unmapped>`
- [ ] **cellbuf/hardscroll.go** → `<unmapped>`
- [ ] **cellbuf/hashmap.go** → `<unmapped>`
- [ ] **cellbuf/link.go** → `<unmapped>`
- [ ] **cellbuf/pen.go** → `<unmapped>`
- [ ] **cellbuf/screen.go** → `<unmapped>`
- [ ] **cellbuf/sequence.go** → `<unmapped>`
- [ ] **cellbuf/sequence_test.go** → `<unmapped>`
- [ ] **cellbuf/style.go** → `<unmapped>`
- [ ] **cellbuf/tabstop.go** → `<unmapped>`
- [ ] **cellbuf/tabstop_test.go** → `<unmapped>`
- [ ] **cellbuf/utils.go** → `<unmapped>`
- [ ] **cellbuf/wrap.go** → `<unmapped>`
- [ ] **cellbuf/wrap_test.go** → `<unmapped>`
- [ ] **cellbuf/writer.go** → `<unmapped>`

---

## Colors Package

- [ ] **colors/colors.go** → `<unmapped>`

---

## Conpty Package

- [ ] **conpty/conpty.go** → `<unmapped>`
- [ ] **conpty/conpty_other.go** → `<unmapped>`
- [ ] **conpty/conpty_windows.go** → `<unmapped>`
- [ ] **conpty/doc.go** → `<unmapped>`
- [ ] **conpty/exec_windows.go** → `<unmapped>`

---

## Editor Package

- [ ] **editor/editor.go** → `<unmapped>`
- [ ] **editor/editor_test.go** → `<unmapped>`

---

## Errors Package

- [ ] **errors/join.go** → `<unmapped>`
- [ ] **errors/join_test.go** → `<unmapped>`

---

## Etag Package

- [ ] **etag/etag.go** → `<unmapped>`
- [ ] **etag/etag_test.go** → `<unmapped>`
