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
 * Mouse selection, hover, and click tracking for {@link Program}.
 * <p>
 * This is a tui4j extension; Bubble Tea does not include mouse selection UX.
 *
 * @see Program
 * @see ProgramConfiguration
 */
final class ProgramMouseHandler {

    private final ProgramConfiguration config;
    private final Consumer<Message> send;

    private final MouseSelectionTracker mouseSelectionTracker =
        new MouseSelectionTracker();
    private final MouseHoverTextDetector mouseHoverTextDetector =
        new MouseHoverTextDetector();
    private final MouseClickTracker mouseClickTracker = new MouseClickTracker();
    private final MouseSelectionAutoScroller mouseSelectionAutoScroller;

    private boolean mouseSelectionCursorActive;
    private boolean hoverTextCursorActive;
    private MouseCursor currentTargetCursor;

    ProgramMouseHandler(
        ProgramConfiguration config,
        IntSupplier terminalHeight,
        Consumer<Message> send
    ) {
        this.config = Objects.requireNonNull(config, "config");
        this.send = Objects.requireNonNull(send, "send");

        this.mouseSelectionAutoScroller = new MouseSelectionAutoScroller(
            terminalHeight,
            mouseSelectionTracker,
            send
        );

        if (config.isSelectionAutoScrollEnabled()) {
            mouseSelectionAutoScroller.configure(
                config.selectionAutoScrollEdgeRows(),
                config.selectionAutoScrollIntervalMs()
            );
        }
    }

    void enableSelectionAutoScroll() {
        mouseSelectionAutoScroller.enable();
    }

    void configureSelectionAutoScroll(int edgeRows, int intervalMs) {
        mouseSelectionAutoScroller.configure(edgeRows, intervalMs);
    }

    void startAutoScroll() {
        mouseSelectionAutoScroller.start();
    }

    void stopAutoScroll() {
        mouseSelectionAutoScroller.stop();
    }

    /**
     * Handles mouse message side-effects (cursor updates and click/selection messages).
     *
     * @param mouseMessage mouse message
     * @param model current model
     * @param renderer renderer used for cursor changes
     */
    void handleMouse(MouseMessage mouseMessage, Model model, Renderer renderer) {
        Objects.requireNonNull(mouseMessage, "mouseMessage");
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(renderer, "renderer");

        mouseSelectionAutoScroller.onMouse(mouseMessage);
        handleMouseClickTracking(mouseMessage, model);
        handleMouseSelectionTracking(mouseMessage, renderer);
        handleMouseHoverCursor(mouseMessage, model, renderer);
        handleMouseTargetCursor(mouseMessage, model, renderer);
    }

    /**
     * Resets cursor state tracked by this handler.
     *
     * @param renderer renderer used for cursor changes
     */
    void cleanupCursors(Renderer renderer) {
        Objects.requireNonNull(renderer, "renderer");

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

        mouseSelectionCursorActive = false;
        hoverTextCursorActive = false;
        currentTargetCursor = null;
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
            send.accept(selectionUpdate.selectionScrollUpdate());
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
        Model model,
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
            model.view(),
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
        Model model,
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

        MouseTarget target = resolveMouseTarget(mouseMessage, model);
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

    private void handleMouseClickTracking(MouseMessage mouseMessage, Model model) {
        if (!config.isMouseClicksEnabled()) {
            return;
        }
        MouseTarget target = resolveMouseTarget(mouseMessage, model);
        MouseClickMessage clickMessage = mouseClickTracker.handle(
            mouseMessage,
            target
        );
        if (clickMessage != null) {
            send.accept(clickMessage);
        }
    }

    private static MouseTarget resolveMouseTarget(
        MouseMessage mouseMessage,
        Model model
    ) {
        if (!(model instanceof MouseTargetProvider provider)) {
            return null;
        }
        return MouseTargets.hitTest(
            provider.mouseTargets(),
            mouseMessage.column(),
            mouseMessage.row()
        );
    }
}

