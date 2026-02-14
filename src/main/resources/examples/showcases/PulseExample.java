package examples.showcases;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.ProgramOption;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.WindowSizeMessage;
import com.williamcallahan.tui4j.compat.bubbles.spinner.Spinner;
import com.williamcallahan.tui4j.compat.bubbles.spinner.SpinnerType;
import com.williamcallahan.tui4j.compat.bubbles.viewport.Viewport;
import com.williamcallahan.tui4j.compat.lipgloss.Position;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;
import com.williamcallahan.tui4j.compat.lipgloss.color.AdaptiveColor;
import com.williamcallahan.tui4j.compat.lipgloss.join.HorizontalJoinDecorator;
import com.williamcallahan.tui4j.compat.lipgloss.join.VerticalJoinDecorator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Random;
import java.util.List;
import java.util.Map;

/**
 * A high-fidelity replica of the Pulse AI coding assistant UI.
 * <p>
 * Features Charm's signature aesthetic: bold purple/pink gradients,
 * split-pane layout, scrollable viewports, command palette, and animations.
 * <p>
 * Keyboard: Tab/Shift+Tab = switch tabs, j/k = scroll, Ctrl+P = palette, q = quit
 */
public class PulseExample implements Model {

    // ═══════════════════════════════════════════════════════════════════════════
    // PULSE COLOR PALETTE - Futuristic, high-energy aesthetic
    // ═══════════════════════════════════════════════════════════════════════════

    // Primary brand colors
    private static final AdaptiveColor PULSE_PINK = new AdaptiveColor("#FF5F87", "#FF5F87");
    private static final AdaptiveColor PULSE_PURPLE = new AdaptiveColor("#AF5FFF", "#AF5FFF");
    private static final AdaptiveColor PULSE_VIOLET = new AdaptiveColor("#875FFF", "#875FFF");

    // UI colors
    private static final AdaptiveColor BG_DARK = new AdaptiveColor("#1A1A2E", "#1A1A2E");
    private static final AdaptiveColor BG_PANEL = new AdaptiveColor("#16213E", "#16213E");
    private static final AdaptiveColor BORDER_DIM = new AdaptiveColor("#4A4A6A", "#4A4A6A");
    private static final AdaptiveColor BORDER_GLOW = new AdaptiveColor("#AF5FFF", "#AF5FFF");
    private static final AdaptiveColor TEXT_PRIMARY = new AdaptiveColor("#E4E4E7", "#E4E4E7");
    private static final AdaptiveColor TEXT_DIM = new AdaptiveColor("#71717A", "#71717A");
    private static final AdaptiveColor TEXT_MUTED = new AdaptiveColor("#52525B", "#52525B");

    // Syntax/Diff colors
    private static final AdaptiveColor DIFF_ADD = new AdaptiveColor("#22C55E", "#22C55E");
    private static final AdaptiveColor DIFF_DEL = new AdaptiveColor("#EF4444", "#EF4444");
    private static final AdaptiveColor SYNTAX_KEYWORD = new AdaptiveColor("#C084FC", "#C084FC");
    private static final AdaptiveColor SYNTAX_STRING = new AdaptiveColor("#FCD34D", "#FCD34D");
    private static final AdaptiveColor SYNTAX_COMMENT = new AdaptiveColor("#6B7280", "#6B7280");

    // File status colors
    private static final AdaptiveColor FILE_MODIFIED = new AdaptiveColor("#FBBF24", "#FBBF24");
    private static final AdaptiveColor FILE_ADDED = new AdaptiveColor("#34D399", "#34D399");
    private static final AdaptiveColor FILE_DELETED = new AdaptiveColor("#F87171", "#F87171");

    // ═══════════════════════════════════════════════════════════════════════════
    // STYLES - Futuristic, high-contrast, terminal-native
    // ═══════════════════════════════════════════════════════════════════════════

    // Logo/Brand
    private static final Style LOGO_STYLE = Style.newStyle()
            .foreground(PULSE_PINK)
            .bold(true);

    // Tabs
    private static final Style TAB_ACTIVE = Style.newStyle()
            .foreground(PULSE_PINK)
            .background(BG_PANEL)
            .bold(true)
            .padding(0, 2);

    private static final Style TAB_INACTIVE = Style.newStyle()
            .foreground(TEXT_DIM)
            .padding(0, 2);

    private static final Style TAB_SEPARATOR = Style.newStyle()
            .foreground(BORDER_DIM);

    // Panels
    private static final Style PANEL_STYLE = Style.newStyle()
            .border(StandardBorder.RoundedBorder)
            .borderForeground(BORDER_DIM)
            .padding(0, 1);

    private static final Style PANEL_FOCUSED = Style.newStyle()
            .border(StandardBorder.RoundedBorder)
            .borderForeground(BORDER_GLOW)
            .padding(0, 1);

    private static final Style PANEL_HEADER = Style.newStyle()
            .foreground(TEXT_DIM)
            .bold(true);

    // Sidebar
    private static final Style SIDEBAR_FILE = Style.newStyle()
            .foreground(TEXT_PRIMARY);

    private static final Style SIDEBAR_DIR = Style.newStyle()
            .foreground(PULSE_VIOLET);

    // Status bar
    private static final Style STATUS_BAR = Style.newStyle()
            .foreground(TEXT_DIM)
            .background(BG_DARK);

    private static final Style STATUS_MODE = Style.newStyle()
            .foreground(BG_DARK)
            .background(PULSE_PINK)
            .bold(true)
            .padding(0, 1);

    private static final Style STATUS_SESSION = Style.newStyle()
            .foreground(PULSE_PURPLE)
            .bold(true);

    private static final Style STATUS_HINT = Style.newStyle()
            .foreground(TEXT_MUTED);

    // Chat
    private static final Style CHAT_USER = Style.newStyle()
            .foreground(PULSE_PINK)
            .bold(true);

    private static final Style CHAT_AI = Style.newStyle()
            .foreground(PULSE_PURPLE)
            .bold(true);

    private static final Style CHAT_MESSAGE = Style.newStyle()
            .foreground(TEXT_PRIMARY);

    // Command Palette
    private static final Style PALETTE_BG = Style.newStyle()
            .border(StandardBorder.DoubleBorder)
            .borderForeground(PULSE_PURPLE)
            .padding(1, 2);

    private static final Style PALETTE_TITLE = Style.newStyle()
            .foreground(PULSE_PINK)
            .bold(true);

    private static final Style PALETTE_ITEM = Style.newStyle()
            .foreground(TEXT_PRIMARY);

    private static final Style PALETTE_SELECTED = Style.newStyle()
            .foreground(BG_DARK)
            .background(PULSE_PURPLE)
            .bold(true);

    private static final Style PALETTE_KEY = Style.newStyle()
            .foreground(TEXT_DIM);

    // ═══════════════════════════════════════════════════════════════════════════
    // LAYOUT CONFIG - Centralized dimension constants
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Layout dimensions for the Pulse UI.
     */
    private record LayoutConfig(
            int sidebarWidth,
            int tabBarHeight,
            int statusBarHeight,
            int minContentHeight,
            int paletteWidth,
            int paletteVerticalPadding
    ) {
        private static final LayoutConfig DEFAULT = new LayoutConfig(28, 1, 1, 10, 40, 6);
    }

    private static final LayoutConfig LAYOUT = LayoutConfig.DEFAULT;

    // ═══════════════════════════════════════════════════════════════════════════
    // STATE
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Tab identifiers with their display labels.
     */
    private enum Tab {
        CODE("󰈮 Code"),
        DIFF("󰦒 Diff"),
        PLAN("󰔚 Plan"),
        CHAT("󰭻 Chat");

        private final String label;

        Tab(String label) {
            this.label = label;
        }

        String label() {
            return label;
        }
    }

    private enum Focus { SIDEBAR, CONTENT }

    /**
     * Bundled state for immutable model transitions.
     */
    private record ViewState(
            Tab activeTab,
            int width,
            int height,
            Model spinner,
            Map<Tab, Viewport> viewports
    ) {}

    private final List<Tab> tabs = List.of(Tab.CODE, Tab.DIFF, Tab.PLAN, Tab.CHAT);
    private Tab activeTab;
    private Focus focus;
    private int width;
    private int height;

    // Sub-models
    private Model spinner;
    private final Map<Tab, Viewport> viewports = new EnumMap<>(Tab.class);

    // Command palette state
    private boolean paletteOpen;
    private int paletteSelection;
    private final List<String> paletteCommands = List.of(
            "New Session",
            "Open File",
            "Run Tests",
            "Git Status",
            "Settings",
            "Help"
    );

    // Mock data
    private static final Random RANDOM = new Random();
    private final String sessionId = "pulse-" + String.format("%04x", RANDOM.nextInt(0xFFFF));
    private int tokenCount = 0;

    // ═══════════════════════════════════════════════════════════════════════════
    // CONSTRUCTORS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Creates a new PulseExample with default state.
     */
    public PulseExample() {
        this.activeTab = Tab.CHAT;
        this.focus = Focus.CONTENT;
        this.paletteOpen = false;
        this.paletteSelection = 0;

        // Initialize spinner with Pulse's signature dots
        this.spinner = new Spinner(SpinnerType.DOT)
                .setStyle(Style.newStyle().foreground(PULSE_PINK));

        // Initialize viewports
        initializeViewports();
    }

    private void initializeViewports() {
        viewports.put(Tab.CODE, new Viewport());
        viewports.put(Tab.DIFF, new Viewport());
        viewports.put(Tab.CHAT, new Viewport());
    }

    private PulseExample(ViewState state) {
        this.activeTab = state.activeTab();
        this.width = state.width();
        this.height = state.height();
        this.spinner = state.spinner();
        this.viewports.putAll(state.viewports());
    }

    @Override
    public Command init() {
        // Defer content generation until after program start (when TerminalInfo is ready)
        viewports.get(Tab.CHAT).setContent(generateChatContent());
        viewports.get(Tab.CODE).setContent(generateCodeContent());
        viewports.get(Tab.DIFF).setContent(generateDiffContent());
        return Command.batch(
            spinner.init(),
            Command.setWindowTitle("Pulse")
        );
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        // Handle window resize
        if (msg instanceof WindowSizeMessage wsm) {
            this.width = wsm.width();
            this.height = wsm.height();
            resizeViewports();
            return UpdateResult.from(this);
        }

        // Handle keypresses
        if (msg instanceof KeyPressMessage key) {
            // Command palette handling
            if (paletteOpen) {
                return handlePaletteInput(key);
            }

            switch (key.key()) {
                case "q", "ctrl+c":
                    return UpdateResult.from(this, QuitMessage::new);

                case "ctrl+p":
                    paletteOpen = true;
                    paletteSelection = 0;
                    return UpdateResult.from(this);

                case "tab", "l":
                    activeTab = nextTab();
                    return UpdateResult.from(new PulseExample(new ViewState(activeTab, width, height, spinner, viewports)));

                case "shift+tab", "h":
                    activeTab = prevTab();
                    return UpdateResult.from(new PulseExample(new ViewState(activeTab, width, height, spinner, viewports)));

                case "1":
                    activeTab = Tab.CODE;
                    return UpdateResult.from(this);
                case "2":
                    activeTab = Tab.DIFF;
                    return UpdateResult.from(this);
                case "3":
                    activeTab = Tab.PLAN;
                    return UpdateResult.from(this);
                case "4":
                    activeTab = Tab.CHAT;
                    return UpdateResult.from(this);

                case "left", "right":
                    focus = (focus == Focus.SIDEBAR) ? Focus.CONTENT : Focus.SIDEBAR;
                    return UpdateResult.from(this);

                case "j", "down":
                    scrollActiveViewport(1);
                    return UpdateResult.from(this);

                case "k", "up":
                    scrollActiveViewport(-1);
                    return UpdateResult.from(this);

                case "g":
                    scrollActiveViewportToTop();
                    return UpdateResult.from(this);

                case "G":
                    scrollActiveViewportToBottom();
                    return UpdateResult.from(this);

                default:
                    break;
            }
        }

        // Update spinner
        UpdateResult<? extends Model> spinnerResult = spinner.update(msg);
        this.spinner = spinnerResult.model();
        tokenCount += RANDOM.nextInt(3);

        return UpdateResult.from(this, spinnerResult.command());
    }

    private UpdateResult<? extends Model> handlePaletteInput(KeyPressMessage key) {
        switch (key.key()) {
            case "esc", "ctrl+p", "enter":
                paletteOpen = false;
                return UpdateResult.from(this);

            case "j", "down":
                paletteSelection = (paletteSelection + 1) % paletteCommands.size();
                return UpdateResult.from(this);

            case "k", "up":
                paletteSelection = (paletteSelection - 1 + paletteCommands.size()) % paletteCommands.size();
                return UpdateResult.from(this);

            default:
                break;
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        if (width == 0 || height == 0) {
            return spinner.view() + " Initializing Pulse...";
        }

        // Build the main layout
        String tabBar = renderTabBar();
        String mainContent = renderMainContent();
        String statusBar = renderStatusBar();

        String layout = VerticalJoinDecorator.joinVertical(
                Position.Left,
                tabBar,
                mainContent,
                statusBar
        );

        // Overlay command palette if open
        if (paletteOpen) {
            layout = overlayPalette(layout);
        }

        return layout;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // RENDER COMPONENTS
    // ═══════════════════════════════════════════════════════════════════════════

    private String renderTabBar() {
        StringBuilder sb = new StringBuilder();

        // Logo
        sb.append(LOGO_STYLE.render(" ⚡ PULSE "));
        sb.append(TAB_SEPARATOR.render("│"));

        // Tabs - labels defined in Tab enum
        for (Tab tab : tabs) {
            Style style = (tab == activeTab) ? TAB_ACTIVE : TAB_INACTIVE;
            sb.append(style.render(tab.label()));
        }

        // Fill remaining width
        int usedWidth = 12 + (tabs.size() * 10); // Approximate
        int remaining = Math.max(0, width - usedWidth - 20);
        sb.append(" ".repeat(remaining));

        // Session indicator
        sb.append(STATUS_SESSION.render(sessionId));

        return sb.toString();
    }

    private String renderMainContent() {
        int contentHeight = Math.max(LAYOUT.minContentHeight(), height - LAYOUT.tabBarHeight() - LAYOUT.statusBarHeight() - 2);

        String sidebar = renderSidebar(contentHeight);
        String content = renderContentPane(contentHeight);

        return HorizontalJoinDecorator.joinHorizontal(Position.Top, sidebar, content);
    }

    private String renderSidebar(int height) {
        StringBuilder sb = new StringBuilder();
        int sidebarWidth = LAYOUT.sidebarWidth();

        // Header
        sb.append(PANEL_HEADER.render("  FILES")).append("\n");
        sb.append(Style.newStyle().foreground(BORDER_DIM).render("─".repeat(sidebarWidth - 4))).append("\n");

        // File tree
        sb.append(renderFileTree());

        // Changes section
        sb.append("\n");
        sb.append(PANEL_HEADER.render("  CHANGES")).append("\n");
        sb.append(Style.newStyle().foreground(BORDER_DIM).render("─".repeat(sidebarWidth - 4))).append("\n");
        sb.append(renderChanges());

        Style sidebarStyle = (focus == Focus.SIDEBAR) ? PANEL_FOCUSED : PANEL_STYLE;
        return sidebarStyle
                .width(sidebarWidth)
                .height(height)
                .render(sb.toString());
    }

    private String renderFileTree() {
        StringBuilder sb = new StringBuilder();
        sb.append(SIDEBAR_DIR.render("  src/")).append("\n");
        sb.append(SIDEBAR_DIR.render("  ├─ main/")).append("\n");
        sb.append(SIDEBAR_DIR.render("  │  ├─ java/")).append("\n");
        sb.append(SIDEBAR_FILE.render("  │  │  └─ App.java")).append("\n");
        sb.append(SIDEBAR_DIR.render("  │  └─ resources/")).append("\n");
        sb.append(SIDEBAR_DIR.render("  └─ test/")).append("\n");
        sb.append(SIDEBAR_FILE.render("  build.gradle")).append("\n");
        sb.append(SIDEBAR_FILE.render("  README.md")).append("\n");
        return sb.toString();
    }

    private String renderChanges() {
        StringBuilder sb = new StringBuilder();
        sb.append(Style.newStyle().foreground(FILE_MODIFIED).render(" M "));
        sb.append(SIDEBAR_FILE.render("PulseExample.java")).append("\n");
        sb.append(Style.newStyle().foreground(FILE_ADDED).render(" A "));
        sb.append(SIDEBAR_FILE.render("NewFeature.java")).append("\n");
        sb.append(Style.newStyle().foreground(FILE_DELETED).render(" D "));
        sb.append(SIDEBAR_FILE.render("OldCode.java")).append("\n");
        return sb.toString();
    }

    private String renderContentPane(int height) {
        int contentWidth = Math.max(20, width - LAYOUT.sidebarWidth() - 4);

        // PLAN tab has custom rendering; others use viewports map
        Viewport viewport = viewports.get(activeTab);
        String content = (viewport != null) ? viewport.view() : renderPlanView();

        Style paneStyle = (focus == Focus.CONTENT) ? PANEL_FOCUSED : PANEL_STYLE;
        return paneStyle
                .width(contentWidth)
                .height(height)
                .render(content);
    }

    private String renderPlanView() {
        StringBuilder sb = new StringBuilder();
        sb.append(Style.newStyle().foreground(PULSE_PINK).bold(true).render("  EXECUTION PLAN")).append("\n\n");

        String[] steps = {
                "Analyze request context",
                "Search codebase for relevant files",
                "Generate implementation plan",
                "Write code changes",
                "Run tests and validate"
        };

        for (int i = 0; i < steps.length; i++) {
            String icon;
            if (i < 2) {
                icon = Style.newStyle().foreground(DIFF_ADD).render("✓");
            } else if (i == 2) {
                icon = spinner.view();
            } else {
                icon = Style.newStyle().foreground(TEXT_DIM).render("○");
            }
            String text = (i <= 2)
                    ? Style.newStyle().foreground(TEXT_PRIMARY).render(steps[i])
                    : Style.newStyle().foreground(TEXT_DIM).render(steps[i]);
            sb.append("  ").append(icon).append(" ").append(text).append("\n");
        }

        return sb.toString();
    }

    private String renderStatusBar() {
        StringBuilder sb = new StringBuilder();

        // Mode indicator
        sb.append(STATUS_MODE.render(" NORMAL "));
        sb.append(" ");

        // Current file/context
        sb.append(Style.newStyle().foreground(TEXT_PRIMARY).render("PulseExample.java"));
        sb.append(" ");

        // Token count
        sb.append(Style.newStyle().foreground(PULSE_PURPLE).render("󰊖 " + tokenCount + " tokens"));

        // Fill
        int usedWidth = 40;
        int remaining = Math.max(0, width - usedWidth - 30);
        sb.append(" ".repeat(remaining));

        // Time
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        sb.append(Style.newStyle().foreground(TEXT_DIM).render(time));
        sb.append(" ");

        // Hints
        sb.append(STATUS_HINT.render("Ctrl+P"));
        sb.append(Style.newStyle().foreground(TEXT_MUTED).render(" palette "));
        sb.append(STATUS_HINT.render("?"));
        sb.append(Style.newStyle().foreground(TEXT_MUTED).render(" help"));

        return STATUS_BAR.width(width).render(sb.toString());
    }

    private String overlayPalette(String background) {
        // Build palette content
        StringBuilder sb = new StringBuilder();
        sb.append(PALETTE_TITLE.render("  Command Palette")).append("\n\n");

        for (int i = 0; i < paletteCommands.size(); i++) {
            String cmd = paletteCommands.get(i);
            Style style = (i == paletteSelection) ? PALETTE_SELECTED : PALETTE_ITEM;
            String prefix = (i == paletteSelection) ? " ▸ " : "   ";
            sb.append(style.render(prefix + cmd)).append("\n");
        }

        sb.append("\n");
        sb.append(PALETTE_KEY.render("  ↑↓ navigate  ⏎ select  esc close"));

        int paletteWidth = LAYOUT.paletteWidth();
        int paletteHeight = paletteCommands.size() + LAYOUT.paletteVerticalPadding();
        String palette = PALETTE_BG
                .width(paletteWidth)
                .height(paletteHeight)
                .render(sb.toString());

        // Center the palette (simple approximation - just prepend spaces)
        int padLeft = Math.max(0, (width - paletteWidth) / 2);
        int padTop = Math.max(0, (height - paletteHeight) / 3);

        StringBuilder result = new StringBuilder();
        String[] bgLines = background.split("\n", -1);
        String[] paletteLines = palette.split("\n", -1);

        for (int i = 0; i < bgLines.length; i++) {
            if (i >= padTop && i < padTop + paletteLines.length) {
                int pIdx = i - padTop;
                // Overlay palette line onto background
                String pLine = " ".repeat(padLeft) + paletteLines[pIdx];
                result.append(pLine);
            } else {
                result.append(bgLines[i]);
            }
            if (i < bgLines.length - 1) {
                result.append("\n");
            }
        }

        return result.toString();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CONTENT GENERATORS
    // ═══════════════════════════════════════════════════════════════════════════

    private String generateChatContent() {
        Style userStyle = CHAT_USER;
        Style aiStyle = CHAT_AI;
        Style msgStyle = CHAT_MESSAGE;

        return userStyle.render("You") + "\n" +
               msgStyle.render("Add a dark mode toggle to the settings page.\n" +
                              "It should persist the preference to localStorage.") + "\n\n" +
               aiStyle.render("Pulse") + " " + spinner.view() + "\n" +
               msgStyle.render("""
                       I'll implement a dark mode toggle for you. Here's my plan:

                       1. Add a toggle switch component to Settings
                       2. Create a theme context for state management
                       3. Persist preference to localStorage
                       4. Apply theme class to document root

                       Let me start by modifying the Settings component...""") + "\n\n" +
               userStyle.render("You") + "\n" +
               msgStyle.render("Looks good! Please proceed.") + "\n\n" +
               aiStyle.render("Pulse") + "\n" +
               msgStyle.render("""
                       Done! I've made the following changes:
                       • Added ThemeContext with useTheme hook
                       • Created DarkModeToggle component
                       • Updated Settings.jsx with toggle
                       • Added CSS variables for both themes

                       The toggle now persists across sessions.""");
    }

    private String generateCodeContent() {
        Style kw = Style.newStyle().foreground(SYNTAX_KEYWORD);
        Style str = Style.newStyle().foreground(SYNTAX_STRING);
        Style cmt = Style.newStyle().foreground(SYNTAX_COMMENT);
        Style txt = Style.newStyle().foreground(TEXT_PRIMARY);

        return cmt.render("// Settings.jsx - Dark mode implementation") + "\n" +
               kw.render("import") + txt.render(" React ") + kw.render("from") + str.render(" 'react'") + txt.render(";") + "\n" +
               kw.render("import") + txt.render(" { useTheme } ") + kw.render("from") + str.render(" './ThemeContext'") + txt.render(";") + "\n\n" +
               kw.render("export function") + txt.render(" Settings() {") + "\n" +
               txt.render("  ") + kw.render("const") + txt.render(" { theme, toggleTheme } = useTheme();") + "\n\n" +
               txt.render("  ") + kw.render("return") + txt.render(" (") + "\n" +
               txt.render("    <div className=") + str.render("\"settings-panel\"") + txt.render(">") + "\n" +
               txt.render("      <h2>Settings</h2>") + "\n" +
               txt.render("      <div className=") + str.render("\"setting-item\"") + txt.render(">") + "\n" +
               txt.render("        <label>Dark Mode</label>") + "\n" +
               txt.render("        <Toggle") + "\n" +
               txt.render("          checked={theme === ") + str.render("'dark'") + txt.render("}") + "\n" +
               txt.render("          onChange={toggleTheme}") + "\n" +
               txt.render("        />") + "\n" +
               txt.render("      </div>") + "\n" +
               txt.render("    </div>") + "\n" +
               txt.render("  );") + "\n" +
               txt.render("}");
    }

    private String generateDiffContent() {
        Style add = Style.newStyle().foreground(DIFF_ADD);
        Style del = Style.newStyle().foreground(DIFF_DEL);
        Style ctx = Style.newStyle().foreground(TEXT_DIM);
        Style hdr = Style.newStyle().foreground(PULSE_PURPLE).bold(true);

        return hdr.render("@@ -12,6 +12,18 @@ Settings.jsx") + "\n\n" +
               ctx.render("   <h2>Settings</h2>") + "\n" +
               ctx.render("   <div className=\"settings-list\">") + "\n" +
               add.render("+ <div className=\"setting-item\">") + "\n" +
               add.render("+   <label htmlFor=\"darkMode\">") + "\n" +
               add.render("+     Dark Mode") + "\n" +
               add.render("+   </label>") + "\n" +
               add.render("+   <Toggle") + "\n" +
               add.render("+     id=\"darkMode\"") + "\n" +
               add.render("+     checked={theme === 'dark'}") + "\n" +
               add.render("+     onChange={toggleTheme}") + "\n" +
               add.render("+   />") + "\n" +
               add.render("+ </div>") + "\n" +
               ctx.render("     <div className=\"setting-item\">") + "\n" +
               del.render("- <span>Notifications</span>") + "\n" +
               add.render("+ <label>Notifications</label>") + "\n" +
               ctx.render("       <Toggle checked={notifications} />") + "\n" +
               ctx.render("     </div>");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════════════════════════

    private Tab nextTab() {
        int idx = tabs.indexOf(activeTab);
        return tabs.get((idx + 1) % tabs.size());
    }

    private Tab prevTab() {
        int idx = tabs.indexOf(activeTab);
        return tabs.get((idx - 1 + tabs.size()) % tabs.size());
    }

    private void resizeViewports() {
        int vpHeight = Math.max(5, height - LAYOUT.tabBarHeight() - LAYOUT.statusBarHeight() - 6);
        int vpWidth = Math.max(20, width - LAYOUT.sidebarWidth() - 8);

        for (Viewport vp : viewports.values()) {
            vp.setWidth(vpWidth);
            vp.setHeight(vpHeight);
        }
    }

    private void scrollActiveViewport(int delta) {
        Viewport vp = getActiveViewport();
        if (vp != null) {
            if (delta > 0) {
                vp.scrollDown(delta);
            } else {
                vp.scrollUp(Math.abs(delta));
            }
        }
    }

    private void scrollActiveViewportToTop() {
        Viewport vp = getActiveViewport();
        if (vp != null) {
            vp.gotoTop();
        }
    }

    private void scrollActiveViewportToBottom() {
        Viewport vp = getActiveViewport();
        if (vp != null) {
            vp.gotoBottom();
        }
    }

    private Viewport getActiveViewport() {
        return viewports.get(activeTab);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Runs the Pulse example.
     *
     * @param args command line arguments (ignored)
     */
    public static void main(String[] args) {
        new Program(new PulseExample(), ProgramOption.withAltScreen(), ProgramOption.withMouseAllMotion())
                .run();
    }
}
