package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbles.spinner.SpinnerType;
import com.williamcallahan.tui4j.compat.bubbletea.Command;
import java.time.Duration;
import java.util.function.Supplier;

/**
 * Public (non-Model) API surface for {@link List}.
 * <p>
 * Upstream: bubbles/list/list.go (extracted from the original port).
 */
abstract class ListPublicApi extends ListPublicApiSizing {

    /** Sets the spinner type.
     * @param spinnerType spinner type
     */
    public void setSpinnerType(SpinnerType spinnerType) {
        spinner.setType(spinnerType);
    }

    /** Starts the spinner tick loop.
     * @return tick command
     */
    public Command startSpinner() {
        this.showSpinner = true;
        return spinner::tick;
    }

    /** Stops the spinner. */
    public void stopSpinner() {
        this.showSpinner = false;
    }

    /** Disables quit keybindings for this list. */
    public void disableQuitKeybindings() {
        this.disableQuitKeybindings = true;
        keys.quit().setEnabled(false);
        keys.forceQuit().setEnabled(false);
    }

    /** Sets a temporary status message.
     * @param status status message
     * @return command
     */
    public Command newStatusMessage(String status) {
        return ListStatusMessageManager.newStatusMessage(self(), status);
    }

    /** Sets status message lifetime.
     * @param statusMessageLifetime status message lifetime
     */
    public void setStatusMessageLifetime(Duration statusMessageLifetime) {
        this.statusMessageLifetime = statusMessageLifetime;
    }

    /** Returns current list styles.
     * @return current list styles
     */
    public Styles styles() {
        return styles;
    }

    /** Sets additional short help key bindings.
     * @param additionalShortHelpKeyMap bindings supplier
     */
    public void setAdditionalShortHelpKeys(
        Supplier<Binding[]> additionalShortHelpKeyMap
    ) {
        this.additionalShortHelpKeyMap = additionalShortHelpKeyMap;
    }

    /** Sets additional full help key bindings.
     * @param additionalFullHelpKeyMap bindings supplier
     */
    public void setAdditionalFullHelpKeys(
        Supplier<Binding[]> additionalFullHelpKeyMap
    ) {
        this.additionalFullHelpKeyMap = additionalFullHelpKeyMap;
    }
}

