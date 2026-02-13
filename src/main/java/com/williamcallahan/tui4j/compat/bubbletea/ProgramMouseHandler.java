package com.williamcallahan.tui4j.compat.bubbletea;

import com.williamcallahan.tui4j.compat.bubbletea.input.MouseAction;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseMessage;
import com.williamcallahan.tui4j.compat.bubbletea.render.Renderer;
import com.williamcallahan.tui4j.input.MouseClickMessage;
import com.williamcallahan.tui4j.input.MouseClickTracker;
import com.williamcallahan.tui4j.input.MouseCursor;
import com.williamcallahan.tui4j.input.MouseHoverTextDetector;
import com.williamcallahan.tui4j.input.MouseSelectionAutoScroller;
import com.williamcallahan.tui4j.input.MouseSelectionTracker;
import com.williamcallahan.tui4j.input.MouseSelectionUpdate;
import com.williamcallahan.tui4j.input.MouseTarget;
import com.williamcallahan.tui4j.input.MouseTargetProvider;
import com.williamcallahan.tui4j.input.MouseTargets;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * Handles all mouse-related behavior for a {@link Program}: selection tracking,
 * hover-text cursor, target cursor, click tracking, and auto-scroll.
 * <p>
 * tui4j extension; no Bubble Tea equivalent — Go Bubble Tea has no built-in
 * mouse cursor management beyond raw event delivery.
 *
 * @see Program
 * @see ProgramConfiguration
 */
final class ProgramMouseHandler {

    private final ProgramConfiguration config;
    private final Consumer<Message> sendFn;
    private final MouseSelectionAutoScroller autoScroller;

    private final MouseSelectionTracker mouseSelectionTracker =
        new MouseSelectionTracker();
    private final MouseHoverTextDetector mouseHoverTextDetector =
        new MouseHoverTextDetector();
    private final MouseClickTracker mouseClickTracker = new MouseClickTracker();
    private boolean mouseSelectionCursorActive;
    private boolean hoverTextCursorActive;
    private MouseCursor currentTargetCursor;

    /**
     * Creates a mouse handler bound to the given configuration and program callbacks.
     *
     * @param config program configuration for reading enabled-flags
     * @param terminalHeight supplies the current terminal height for auto-scroll
     * @param sendFn callback to enqueue messages into the program's message queue
     */
    ProgramMouseHandler(
        ProgramConfiguration config,
        IntSupplier terminalHeight,
        Consumer<Message> sendFn
    ) {
        this.config = Objects.requireNonNull(config, "config");
        this.sendFn = Objects.requireNonNull(sendFn, "sendFn");
        this.autoScroller = new MouseSelectionAutoScroller(
            Objects.requireNonNull(terminalHeight, "terminalHeight"),
            mouseSelectionTracker,
            sendFn
        );
        applyAutoScrollConfig();
    }

    /**
     * Enables selection auto-scroll on the scroller instance.
     * Called from {@link Program#withMouseSelectionAutoScroll()}.
     */
    void enableSelectionAutoScroll() {
        autoScroller.enable();
    }

    /**
     * Configures auto-scroll edge rows and interval.
     * Called from {@link Program#withMouseSelectionAutoScroll(int, int)}.
     *
     * @param edgeRows number of rows from the edge that trigger auto-scroll
     * @param intervalMs interval in milliseconds between auto-scroll steps
     */
    void configureSelectionAutoScroll(int edgeRows, int intervalMs) {
        autoScroller.configure(edgeRows, intervalMs);
    }

    /**
     * Starts the auto-scroller polling thread.
     */
    void startAutoScroll() {
        autoScroller.start();
    }

    /**
     * Stops the auto-scroller polling thread.
     */
    void stopAutoScroll() {
        autoScroller.stop();
    }

    /**
     * Dispatches a mouse event to all sub-handlers.
     *
     * @param mouseMessage the mouse event
     * @param currentModel current model (for view text and target resolution)
     * @param renderer renderer for cursor operations
     */
    void handleMouse(MouseMessage mouseMessage, Model currentModel, Renderer renderer) {
        autoScroller.onMouse(mouseMessage);
        handleMouseClickTracking(mouseMessage, currentModel);
        handleMouseSelectionTracking(mouseMessage, renderer);
        handleMouseHoverCursor(mouseMessage, currentModel, renderer);
        handleMouseTargetCursor(mouseMessage, currentModel, renderer);
    }

    /**
     * Resets any active mouse cursors during program cleanup.
     *
     * @param renderer renderer for cursor reset
     */
    void cleanupCursors(Renderer renderer) {
        if (config.isManageMouseSelectionCursor() && mouseSelectionCursorActive) {
            renderer.resetMouseCursor();
        }
        if (config.isHoverTextCursorEnabled() && hoverTextCursorActive) {
            renderer.resetMouseCursor();
        }
        if (
            config.isMouseTargetCursorEnabled() &&
            currentTargetCursor != null &&
            currentTargetCursor != MouseCursor.DEFAULT
        ) {
            renderer.resetMouseCursor();
        }
    }

    private void applyAutoScrollConfig() {
        if (!config.isSelectionAutoScrollEnabled()) {
            return;
        }
        autoScroller.configure(
            config.selectionAutoScrollEdgeRows(),
            config.selectionAutoScrollIntervalMs()
        );
    }

    private void handleMouseSelectionTracking(
        MouseMessage mouseMessage,
        Renderer renderer
    ) {
        MouseSelectionUpdate selectionUpdate = mouseSelectionTracker.update(
            mouseMessage
        );

        if (
            config.isExtendSelectionOnScroll() &&
            selectionUpdate.selectionScrollUpdate() != null
        ) {
            sendFn.accept(selectionUpdate.selectionScrollUpdate());
        }

        if (!config.isManageMouseSelectionCursor()) {
            return;
        }

        if (selectionUpdate.selectionStarted()) {
            setMouseSelectionCursorText(renderer);
            return;
        }

        if (selectionUpdate.selectionEnded()) {
            resetMouseSelectionCursor(renderer);
            return;
        }

        if (selectionUpdate.selectionActive()) {
            if (mouseMessage.isWheel()) {
                resetMouseSelectionCursor(renderer);
                return;
            }

            if (
                mouseMessage.getAction() == MouseAction.MouseActionMotion ||
                mouseMessage.getAction() == MouseAction.MouseActionPress
            ) {
                setMouseSelectionCursorText(renderer);
            }
        }
    }

    private void handleMouseHoverCursor(
        MouseMessage mouseMessage,
        Model currentModel,
        Renderer renderer
    ) {
        if (!config.isHoverTextCursorEnabled()) {
            return;
        }
        if (mouseSelectionTracker.isSelecting() || mouseSelectionCursorActive) {
            return;
        }
        if (mouseMessage.isWheel()) {
            return;
        }
        if (
            mouseMessage.getAction() != MouseAction.MouseActionMotion &&
            mouseMessage.getAction() != MouseAction.MouseActionPress &&
            mouseMessage.getAction() != MouseAction.MouseActionRelease
        ) {
            return;
        }

        boolean overText = mouseHoverTextDetector.isHoveringText(
            currentModel.view(),
            mouseMessage.column(),
            mouseMessage.row()
        );

        if (overText && !hoverTextCursorActive) {
            renderer.setMouseCursorText();
            hoverTextCursorActive = true;
        } else if (!overText && hoverTextCursorActive) {
            renderer.resetMouseCursor();
            hoverTextCursorActive = false;
        }
    }

    private void handleMouseTargetCursor(
        MouseMessage mouseMessage,
        Model currentModel,
        Renderer renderer
    ) {
        if (!config.isMouseTargetCursorEnabled()) {
            return;
        }
        if (mouseSelectionTracker.isSelecting() || mouseSelectionCursorActive) {
            currentTargetCursor = null; // Clear cache so next motion re-evaluates.
            return;
        }
        if (hoverTextCursorActive) {
            currentTargetCursor = null; // Clear cache so next motion re-evaluates.
            return;
        }
        if (mouseMessage.isWheel()) {
            currentTargetCursor = null; // Clear cache so next motion re-evaluates.
            return;
        }
        if (
            mouseMessage.getAction() != MouseAction.MouseActionMotion &&
            mouseMessage.getAction() != MouseAction.MouseActionPress &&
            mouseMessage.getAction() != MouseAction.MouseActionRelease
        ) {
            return;
        }

        MouseTarget target = resolveMouseTarget(mouseMessage, currentModel);
        MouseCursor desiredCursor = (target != null)
            ? target.cursor()
            : MouseCursor.DEFAULT;

        if (desiredCursor == currentTargetCursor) {
            return;
        }

        switch (desiredCursor) {
            case POINTER -> renderer.setMouseCursorPointer();
            case TEXT -> renderer.setMouseCursorText();
            default -> renderer.resetMouseCursor();
        }
        currentTargetCursor = desiredCursor;
    }

    private void setMouseSelectionCursorText(Renderer renderer) {
        if (mouseSelectionCursorActive) {
            return;
        }
        renderer.setMouseCursorText();
        mouseSelectionCursorActive = true;
    }

    private void resetMouseSelectionCursor(Renderer renderer) {
        if (!mouseSelectionCursorActive) {
            return;
        }
        renderer.resetMouseCursor();
        mouseSelectionCursorActive = false;
    }

    private void handleMouseClickTracking(MouseMessage mouseMessage, Model currentModel) {
        if (!config.isMouseClicksEnabled()) {
            return;
        }
        MouseTarget target = resolveMouseTarget(mouseMessage, currentModel);
        MouseClickMessage clickMessage = mouseClickTracker.handle(
            mouseMessage,
            target
        );
        if (clickMessage != null) {
            sendFn.accept(clickMessage);
        }
    }

    private MouseTarget resolveMouseTarget(
        MouseMessage mouseMessage,
        Model currentModel
    ) {
        if (!(currentModel instanceof MouseTargetProvider provider)) {
            return null;
        }
        return MouseTargets.hitTest(
            provider.mouseTargets(),
            mouseMessage.column(),
            mouseMessage.row()
        );
    }
}

