package com.williamcallahan.tui4j.compat.bubbles.filepicker;

import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.ErrorMessage;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File selection bubble.
 * <p>
 * Port of github.com/charmbracelet/bubbles/filepicker/filepicker.go.
 * Allows navigating the filesystem and selecting files or directories.
 */
public class FilePicker implements Model {

    private static final int MARGIN_BOTTOM = 5;
    private static final String DEFAULT_CURSOR = ">";
    private static final String EMPTY_MSG = "Bummer. No Files Found.";
    private static final Logger logger = Logger.getLogger(
        FilePicker.class.getName()
    );

    private final int id;
    private String path;
    private String currentDirectory;
    private List<String> allowedTypes;
    private KeyMap keyMap;
    private List<DirEntry> files;
    private boolean showPermissions;
    private boolean showSize;
    private boolean showHidden;
    private boolean dirAllowed;
    private boolean fileAllowed;
    private boolean autoHeight;
    private int height;
    private int selected;
    private int min;
    private int max;
    private Stack selectedStack;
    private Stack minStack;
    private Stack maxStack;
    private Styles styles;
    private String cursorChar;
    private static final AtomicInteger nextId = new AtomicInteger(1);
    private List<String> readErrors;

    /**
     * Creates a file picker with default configuration.
     */
    public FilePicker() {
        this.id = generateId();
        this.currentDirectory = ".";
        this.cursorChar = DEFAULT_CURSOR;
        this.allowedTypes = new ArrayList<>();
        this.selected = 0;
        this.showPermissions = true;
        this.showSize = true;
        this.showHidden = false;
        this.dirAllowed = false;
        this.fileAllowed = true;
        this.autoHeight = true;
        this.height = 0;
        this.max = 0;
        this.min = 0;
        this.selectedStack = new Stack();
        this.minStack = new Stack();
        this.maxStack = new Stack();
        this.keyMap = new KeyMap();
        this.styles = Styles.defaultStyles();
        this.files = new ArrayList<>();
        this.readErrors = new ArrayList<>();
    }

    /**
     * Returns a unique instance identifier.
     *
     * @return unique id for this picker
     */
    private int generateId() {
        return nextId.getAndIncrement();
    }

    /**
     * Computes the max index clamped to non-negative values.
     * Prevents -1 when height is zero.
     *
     * @return non-negative max index
     */
    private int computeMaxIndex() {
        return Math.max(0, this.height - 1);
    }

    /**
     * Returns a command to populate the file list from the current directory.
     * Respects showHidden rules.
     *
     * @return command that reads the current directory
     */
    @Override
    public Command init() {
        return readDir(this.currentDirectory, this.showHidden);
    }

    /**
     * Builds a command that reads the directory contents.
     *
     * @param directory directory to read
     * @param showHidden whether to include hidden entries
     * @return command that emits a read directory message
     */
    private Command readDir(String directory, boolean showHidden) {
        return () -> {
            try {
                Path dirPath = Paths.get(directory);
                List<DirEntry> entries = new ArrayList<>();
                List<String> errors = new ArrayList<>();
                try (var stream = Files.list(dirPath)) {
                    stream
                        .sorted(
                            Comparator.comparing(
                                FilePicker::isNotDir
                            ).thenComparing(Path::getFileName)
                        )
                        .forEach(p -> {
                            try {
                                boolean isDir = Files.isDirectory(p);
                                boolean isSymlink = Files.isSymbolicLink(p);
                                boolean isHidden = p
                                    .getFileName()
                                    .toString()
                                    .startsWith(".");
                                String name = p.getFileName().toString();

                                if (
                                    !showHidden &&
                                    isHidden &&
                                    !name.equals(".") &&
                                    !name.equals("..")
                                ) {
                                    return;
                                }

                                long size = Files.size(p);
                                String permissions = getPermissions(p);
                                entries.add(
                                    new DirEntry(
                                        name,
                                        isDir,
                                        isSymlink,
                                        size,
                                        permissions
                                    )
                                );
                            } catch (IOException | SecurityException e) {
                                String errorMsg =
                                    "Failed to read: " +
                                    p.getFileName() +
                                    " (" +
                                    e.getMessage() +
                                    ")";
                                errors.add(errorMsg);
                                logger.log(
                                    Level.WARNING,
                                    "Failed to read entry " + p,
                                    e
                                );
                            }
                        });
                }
                return new ReadDirMessage(this.id, entries, errors);
            } catch (IOException e) {
                return new ErrorMessage(e);
            }
        };
    }

    /**
     * Returns true when the path is not a directory.
     *
     * @param p path to test
     * @return true when the path is not a directory
     */
    private static boolean isNotDir(Path p) {
        try {
            return !Files.isDirectory(p);
        } catch (SecurityException e) {
            // Can't propagate inside stream comparator; fallback to treating as file
            logger.log(
                Level.WARNING,
                "Security exception checking directory status for " + p,
                e
            );
            return true;
        }
    }

    /**
     * Returns a permissions string for the path.
     *
     * @param p path to inspect
     * @return permissions string
     * @throws IOException when permissions cannot be read
     */
    private static String getPermissions(Path p) throws IOException {
        try {
            return PosixFilePermissions.toString(
                Files.getPosixFilePermissions(p)
            );
        } catch (UnsupportedOperationException e) {
            // Non-POSIX filesystem (Windows)
            return (
                (Files.isReadable(p) ? "r" : "-") +
                (Files.isWritable(p) ? "w" : "-") +
                (Files.isExecutable(p) ? "x" : "-")
            );
        }
    }

    /**
     * Sets the maximum visible rows for the picker.
     *
     * @param height height in rows
     */
    public void setHeight(int height) {
        this.height = Math.max(0, height);
        if (this.max > this.height - 1) {
            int updatedMax = Math.max(this.min, this.min + this.height - 1);
            this.max = Math.max(0, updatedMax);
        }
    }

    /**
     * Sets terminal size for layout calculations.
     * Clamps selection indices to prevent desync after resize.
     *
     * @param width terminal width in columns
     * @param height terminal height in rows
     */
    public void setTerminalSize(int width, int height) {
        if (this.autoHeight) {
            this.height = Math.max(0, height - MARGIN_BOTTOM);
        }
        int lastIndex = Math.max(0, this.files.size() - 1);
        this.selected = Math.min(this.selected, lastIndex);
        this.min = Math.min(this.min, this.selected);
        int updatedMax = Math.max(this.min, this.min + this.height - 1);
        this.max = Math.min(lastIndex, Math.max(0, updatedMax));
    }

    /**
     * Updates the picker with a message.
     *
     * @param msg message to handle
     * @return updated model and optional command
     */
    @Override
    public UpdateResult<FilePicker> update(Message msg) {
        if (msg instanceof KeyPressMessage keyMsg) {
            return handleKeyPress(keyMsg);
        } else if (msg instanceof ReadDirMessage readDirMsg) {
            if (readDirMsg.id() != this.id) {
                return UpdateResult.from(this);
            }
            this.files = readDirMsg.entries();
            this.readErrors = readDirMsg.errors();
            // Clamp selection indices to prevent out-of-bounds access when directory shrinks
            int lastIndex = Math.max(0, this.files.size() - 1);
            this.selected = Math.min(this.selected, lastIndex);
            this.min = Math.min(this.min, this.selected);
            this.max = Math.min(
                lastIndex,
                Math.max(0, this.min + this.height - 1)
            );
            return UpdateResult.from(this);
        } else if (msg instanceof ErrorMessage errorMsg) {
            logger.log(
                Level.WARNING,
                "File picker failed to read directory",
                errorMsg.error()
            );
            return UpdateResult.from(this);
        }

        return UpdateResult.from(this);
    }

    /**
     * Handles key press messages.
     *
     * @param keyMsg key press message
     * @return updated model and optional command
     */
    private UpdateResult<FilePicker> handleKeyPress(KeyPressMessage keyMsg) {
        if (handleNavigation(keyMsg)) {
            return UpdateResult.from(this);
        } else if (Binding.matches(keyMsg, keyMap.back())) {
            Path current = Path.of(this.currentDirectory);
            Path parent = current.getParent();
            if (parent != null) {
                this.currentDirectory = parent.toString();
            } else if (current.isAbsolute() && current.getRoot() != null) {
                // At filesystem root; stay put (matches Go filepath.Dir behavior)
                this.currentDirectory = current.getRoot().toString();
            }
            // Relative path with no parent stays unchanged
            if (this.selectedStack.length() > 0) {
                this.selected = this.selectedStack.pop();
                this.min = this.minStack.pop();
                this.max = this.maxStack.pop();
            } else {
                this.selected = 0;
                this.min = 0;
                this.max = computeMaxIndex();
            }
            return UpdateResult.from(
                this,
                readDir(this.currentDirectory, this.showHidden)
            );
        } else if (Binding.matches(keyMsg, keyMap.open())) {
            return handleOpen(keyMsg);
        } else if (
            Binding.matches(keyMsg, keyMap.select()) &&
            !Binding.matches(keyMsg, keyMap.open())
        ) {
            return handleSelect(keyMsg);
        }
        return UpdateResult.from(this);
    }

    /**
     * Handles navigation key bindings.
     *
     * @param keyMsg key press message
     * @return true when navigation changed state
     */
    private boolean handleNavigation(KeyPressMessage keyMsg) {
        if (Binding.matches(keyMsg, keyMap.goToTop())) {
            this.selected = 0;
            this.min = 0;
            this.max = computeMaxIndex();
            return true;
        } else if (Binding.matches(keyMsg, keyMap.goToLast())) {
            int lastIndex = Math.max(0, this.files.size() - 1);
            this.selected = lastIndex;
            this.max = lastIndex;
            this.min = Math.min(Math.max(0, this.files.size() - this.height), this.max);
            return true;
        } else if (Binding.matches(keyMsg, keyMap.down())) {
            if (this.files.isEmpty()) {
                return true;
            }
            this.selected++;
            if (this.selected >= this.files.size()) {
                this.selected = this.files.size() - 1;
            }
            if (this.selected > this.max) {
                this.min++;
                this.max++;
            }
            return true;
        } else if (Binding.matches(keyMsg, keyMap.up())) {
            this.selected--;
            if (this.selected < 0) {
                this.selected = 0;
            }
            if (this.selected < this.min) {
                this.min--;
                this.max--;
            }
            return true;
        } else if (Binding.matches(keyMsg, keyMap.pageDown())) {
            if (this.files.isEmpty()) {
                return true;
            }
            this.selected += this.height;
            if (this.selected >= this.files.size()) {
                this.selected = Math.max(0, this.files.size() - 1);
            }
            this.min += this.height;
            this.max += this.height;

            if (this.max >= this.files.size()) {
                this.max = Math.max(0, this.files.size() - 1);
                this.min = Math.min(Math.max(0, this.max - this.height + 1), this.max);
            }
            return true;
        } else if (Binding.matches(keyMsg, keyMap.pageUp())) {
            if (this.files.isEmpty()) {
                return true;
            }
            this.selected -= this.height;
            if (this.selected < 0) {
                this.selected = 0;
            }
            this.min -= this.height;
            this.max -= this.height;

            if (this.min < 0) {
                this.min = 0;
                this.max = Math.min(
                    this.files.size() - 1,
                    this.min + this.height - 1
                );
            }
            return true;
        }
        return false;
    }

    /**
     * Handles open/select behavior for the current entry.
     *
     * @param keyMsg key press message
     * @return updated model and optional command
     */
    private UpdateResult<FilePicker> handleOpen(KeyPressMessage keyMsg) {
        if (this.files.isEmpty()) {
            return UpdateResult.from(this);
        }

        DirEntry f = this.files.get(this.selected);
        boolean isDir = f.isDir();
        boolean isSymlink = f.isSymlink();

        if (isSymlink) {
            try {
                Path linkPath = Paths.get(this.currentDirectory, f.name());
                Path symlinkTarget = Files.readSymbolicLink(linkPath);
                Path resolved = linkPath
                    .getParent()
                    .resolve(symlinkTarget)
                    .normalize();
                isDir = Files.isDirectory(resolved);
            } catch (IOException e) {
                return UpdateResult.from(this, () -> new ErrorMessage(e));
            }
        }

        if ((!isDir && this.fileAllowed) || (isDir && this.dirAllowed)) {
            if (Binding.matches(keyMsg, keyMap.select())) {
                this.path = Paths.get(
                    this.currentDirectory,
                    f.name()
                ).toString();
            }
        }

        if (!isDir) {
            return UpdateResult.from(this);
        }

        this.currentDirectory = Paths.get(
            this.currentDirectory,
            f.name()
        ).toString();
        this.selectedStack.push(this.selected);
        this.minStack.push(this.min);
        this.maxStack.push(this.max);
        this.selected = 0;
        this.min = 0;
        this.max = computeMaxIndex();
        return UpdateResult.from(
            this,
            readDir(this.currentDirectory, this.showHidden)
        );
    }

    /**
     * Handles select behavior for the current entry.
     *
     * @param keyMsg key press message
     * @return updated model
     */
    private UpdateResult<FilePicker> handleSelect(KeyPressMessage keyMsg) {
        didSelectFile(keyMsg);
        return UpdateResult.from(this);
    }

    /**
     * Renders the current view.
     *
     * @return rendered file picker view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();

        if (this.files.isEmpty()) {
            sb.append(
                this.styles.emptyDirectory()
                    .height(this.height)
                    .render(EMPTY_MSG)
            );
            return sb.toString();
        }

        for (int i = 0; i < this.files.size(); i++) {
            if (i < this.min || i > this.max) {
                continue;
            }

            DirEntry f = this.files.get(i);
            boolean disabled = !canSelect(f.name()) && !f.isDir();

            sb.append(renderRow(i, f, disabled));
        }

        int currentHeight = sb.toString().split("\n", -1).length;
        for (int i = currentHeight; i <= this.height; i++) {
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Renders a single row for the given entry.
     *
     * @param i row index
     * @param f directory entry
     * @param disabled whether selection is disabled
     * @return rendered row
     */
    private String renderRow(int i, DirEntry f, boolean disabled) {
        StringBuilder sb = new StringBuilder();
        if (this.selected == i) {
            StringBuilder selectedBuilder = new StringBuilder();
            if (this.showPermissions) {
                selectedBuilder.append(" ").append(f.permissions());
            }
            if (this.showSize) {
                selectedBuilder.append(formatSize(f.size()));
            }
            selectedBuilder.append(" ").append(f.name());

            if (f.isSymlink()) {
                try {
                    Path symlinkPath = Files.readSymbolicLink(
                        Paths.get(this.currentDirectory, f.name())
                    );
                    selectedBuilder.append(" → ").append(symlinkPath);
                } catch (IOException e) {
                    logger.log(
                        Level.WARNING,
                        "Failed to resolve symlink for " + f.name(),
                        e
                    );
                }
            }

            if (disabled) {
                sb.append(this.styles.disabledCursor().render(this.cursorChar));
                sb.append(
                    this.styles.disabledSelected().render(
                        selectedBuilder.toString()
                    )
                );
            } else {
                sb.append(this.styles.cursor().render(this.cursorChar));
                sb.append(
                    this.styles.selected().render(selectedBuilder.toString())
                );
            }
            sb.append("\n");
            return sb.toString();
        }

        Style style = this.styles.file();
        if (f.isDir()) {
            style = this.styles.directory();
        } else if (f.isSymlink()) {
            style = this.styles.symlink();
        } else if (disabled) {
            style = this.styles.disabledFile();
        }

        sb.append(this.styles.cursor().render(" "));

        String fileName = style.render(f.name());
        if (f.isSymlink()) {
            try {
                Path symlinkPath = Files.readSymbolicLink(
                    Paths.get(this.currentDirectory, f.name())
                );
                fileName += " → " + symlinkPath;
            } catch (IOException e) {
                logger.log(
                    Level.WARNING,
                    "Failed to resolve symlink for " + f.name(),
                    e
                );
            }
        }

        if (this.showPermissions) {
            sb
                .append(" ")
                .append(this.styles.permission().render(f.permissions()));
        }
        if (this.showSize) {
            sb.append(this.styles.fileSize().render(formatSize(f.size())));
        }
        sb.append(" ").append(fileName);
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Formats bytes into a human-readable size label.
     *
     * @param bytes size in bytes
     * @return formatted size string
     */
    private String formatSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }

    /**
     * Returns true when the file name matches allowed extensions.
     *
     * @param file file name to test
     * @return true if selection is allowed
     */
    private boolean canSelect(String file) {
        if (this.allowedTypes.isEmpty()) {
            return true;
        }

        for (String ext : this.allowedTypes) {
            if (file.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the last selected path.
     *
     * @return selected path or {@code null} if none selected
     */
    public String selectedPath() {
        return this.path;
    }

    /**
     * Returns true when the message corresponds to selecting a file.
     *
     * @param msg message to inspect
     * @return true when a file was selected
     */
    public boolean didSelectFile(Message msg) {
        if (msg instanceof KeyPressMessage keyMsg) {
            if (!Binding.matches(keyMsg, keyMap.select())) {
                return false;
            }

            if (this.files.isEmpty()) {
                return false;
            }

            DirEntry f = this.files.get(this.selected);
            boolean isDir = f.isDir();
            boolean isSymlink = f.isSymlink();

            if (isSymlink) {
                try {
                    Path linkPath = Paths.get(this.currentDirectory, f.name());
                    Path symlinkTarget = Files.readSymbolicLink(linkPath);
                    Path resolved = linkPath
                        .getParent()
                        .resolve(symlinkTarget)
                        .normalize();
                    isDir = Files.isDirectory(resolved);
                } catch (IOException e) {
                    logger.log(
                        Level.WARNING,
                        "Failed to resolve symlink for " + f.name(),
                        e
                    );
                    return false;
                }
            }

            if ((!isDir && this.fileAllowed) || (isDir && this.dirAllowed)) {
                this.path = Paths.get(
                    this.currentDirectory,
                    f.name()
                ).toString();
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true when the message corresponds to selecting a directory.
     *
     * @param msg message to inspect
     * @return true when a directory was selected
     */
    public boolean didSelectDirectory(Message msg) {
        if (msg instanceof KeyPressMessage keyMsg) {
            if (!Binding.matches(keyMsg, keyMap.open())) {
                return false;
            }

            if (this.files.isEmpty()) {
                return false;
            }

            DirEntry f = this.files.get(this.selected);
            boolean isDir = f.isDir();

            // Handle symlinks - resolve to check if target is a directory
            if (f.isSymlink()) {
                try {
                    Path linkPath = Paths.get(this.currentDirectory, f.name());
                    Path symlinkTarget = Files.readSymbolicLink(linkPath);
                    Path resolved = linkPath
                        .getParent()
                        .resolve(symlinkTarget)
                        .normalize();
                    isDir = Files.isDirectory(resolved);
                } catch (IOException e) {
                    logger.log(
                        Level.WARNING,
                        "Failed to resolve symlink for " + f.name(),
                        e
                    );
                    return false;
                }
            }

            return isDir;
        }
        return false;
    }

    /**
     * Returns the current directory.
     *
     * @return current directory
     */
    public String currentDirectory() {
        return this.currentDirectory;
    }

    /**
     * Returns any errors encountered while reading the current directory.
     * Errors are cleared when a new directory is read.
     *
     * @return read error messages
     */
    public List<String> readErrors() {
        return List.copyOf(this.readErrors);
    }

    /**
     * Returns true if there were errors reading the current directory.
     *
     * @return true when read errors are present
     */
    public boolean hasReadErrors() {
        return !this.readErrors.isEmpty();
    }

    /**
     * Sets the current directory for the picker.
     *
     * @param directory directory to use
     */
    public void setCurrentDirectory(String directory) {
        this.currentDirectory = directory;
    }

    /**
     * Returns the allowed file extensions list.
     *
     * @return copy of allowed extensions
     */
    public List<String> allowedTypes() {
        return new ArrayList<>(this.allowedTypes);
    }

    /**
     * Sets the allowed file extensions.
     *
     * @param types file extensions to allow
     */
    public void setAllowedTypes(String... types) {
        this.allowedTypes = new ArrayList<>(Arrays.asList(types));
    }

    /**
     * Returns whether hidden files are shown.
     *
     * @return true when hidden files are shown
     */
    public boolean showHidden() {
        return this.showHidden;
    }

    /**
     * Sets whether hidden files are shown.
     *
     * @param showHidden true to show hidden files
     */
    public void setShowHidden(boolean showHidden) {
        this.showHidden = showHidden;
    }

    /**
     * Returns whether directories can be selected.
     *
     * @return true when directory selection is allowed
     */
    public boolean dirAllowed() {
        return this.dirAllowed;
    }

    /**
     * Sets whether directories can be selected.
     *
     * @param dirAllowed true to allow directory selection
     */
    public void setDirAllowed(boolean dirAllowed) {
        this.dirAllowed = dirAllowed;
    }

    /**
     * Returns whether files can be selected.
     *
     * @return true when file selection is allowed
     */
    public boolean fileAllowed() {
        return this.fileAllowed;
    }

    /**
     * Sets whether files can be selected.
     *
     * @param fileAllowed true to allow file selection
     */
    public void setFileAllowed(boolean fileAllowed) {
        this.fileAllowed = fileAllowed;
    }

    /**
     * Returns whether permissions are shown.
     *
     * @return true when permissions are shown
     */
    public boolean showPermissions() {
        return this.showPermissions;
    }

    /**
     * Sets whether permissions are shown.
     *
     * @param showPermissions true to show permissions
     */
    public void setShowPermissions(boolean showPermissions) {
        this.showPermissions = showPermissions;
    }

    /**
     * Returns whether file sizes are shown.
     *
     * @return true when file sizes are shown
     */
    public boolean showSize() {
        return this.showSize;
    }

    /**
     * Sets whether file sizes are shown.
     *
     * @param showSize true to show file sizes
     */
    public void setShowSize(boolean showSize) {
        this.showSize = showSize;
    }

    /**
     * Returns the current picker height.
     *
     * @return height in rows
     */
    public int height() {
        return this.height;
    }

    /**
     * Returns the active styles.
     *
     * @return styles configuration
     */
    public Styles styles() {
        return this.styles;
    }

    /**
     * Sets the styles used for rendering.
     *
     * @param styles styles configuration
     */
    public void setStyles(Styles styles) {
        this.styles = styles;
    }

    /**
     * Returns the current key bindings.
     *
     * @return key map
     */
    public KeyMap keyMap() {
        return this.keyMap;
    }

    /**
     * Sets the key bindings.
     *
     * @param keyMap key map
     */
    public void setKeyMap(KeyMap keyMap) {
        this.keyMap = keyMap;
    }

    /**
     * Returns the cursor character.
     *
     * @return cursor character
     */
    public String cursorChar() {
        return this.cursorChar;
    }

    /**
     * Sets the cursor character.
     *
     * @param cursorChar cursor character
     */
    public void setCursorChar(String cursorChar) {
        this.cursorChar = cursorChar;
    }

    /**
     * Port of the file picker directory entry model.
     * Upstream: github.com/charmbracelet/bubbles/filepicker/filepicker.go (dirEntry)
     *
     * @param name entry name
     * @param isDir whether the entry is a directory
     * @param isSymlink whether the entry is a symlink
     * @param size entry size in bytes
     * @param permissions entry permissions string
     */
    public record DirEntry(
        String name,
        boolean isDir,
        boolean isSymlink,
        long size,
        String permissions
    ) {}

    /**
     * Port of the file picker read directory message.
     * Upstream: github.com/charmbracelet/bubbles/filepicker/filepicker.go (readDirMsg)
     *
     * @param id picker id
     * @param entries directory entries
     * @param errors read errors
     */
    private record ReadDirMessage(
        int id,
        List<DirEntry> entries,
        List<String> errors
    ) implements Message {}

    /**
     * Simple integer stack for selection history.
     */
    private static class Stack {

        private final List<Integer> items = new ArrayList<>();

        /**
         * Pushes an item onto the stack.
         *
         * @param item item to push
         */
        public void push(int item) {
            this.items.add(item);
        }

        /**
         * Pops an item from the stack.
         *
         * @return popped item or {@code 0} when empty
         */
        public int pop() {
            if (this.items.isEmpty()) {
                return 0;
            }
            int result = this.items.get(this.items.size() - 1);
            this.items.remove(this.items.size() - 1);
            return result;
        }

        /**
         * Returns the number of items on the stack.
         *
         * @return stack size
         */
        public int length() {
            return this.items.size();
        }
    }
}
