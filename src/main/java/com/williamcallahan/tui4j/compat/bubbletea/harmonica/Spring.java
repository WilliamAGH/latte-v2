package com.williamcallahan.tui4j.compat.bubbletea.harmonica;

/**
 * Physics-based spring animation primitive.
 * <p>
 * Port of {@code charmbracelet/harmonica}.
 * Simulates a damped spring system for natural animation curves.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.harmonica.Spring} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0", forRemoval = true)
public class Spring {

    private final com.williamcallahan.tui4j.compat.harmonica.Spring delegate;

    private Spring(com.williamcallahan.tui4j.compat.harmonica.Spring delegate) {
        this.delegate = delegate;
    }

    /**
     * Calculates the time delta for a given frame rate.
     *
     * @param n frames per second
     * @return time delta in seconds
     */
    @Deprecated(since = "0.3.0", forRemoval = true)
    public static double fps(int n) {
        return com.williamcallahan.tui4j.compat.harmonica.Spring.fps(n);
    }

    /**
     * Creates a new spring with the specified parameters.
     *
     * @param deltaTime time step for the simulation
     * @param angularFrequency angular frequency of the spring
     * @param dampingRatio damping ratio (1.0 = critically damped)
     * @return a new Spring instance
     */
    @Deprecated(since = "0.3.0", forRemoval = true)
    public static Spring newSpring(double deltaTime, double angularFrequency, double dampingRatio) {
        return new Spring(
            com.williamcallahan.tui4j.compat.harmonica.Spring.newSpring(
                deltaTime, angularFrequency, dampingRatio
            )
        );
    }

    /**
     * Advances the spring simulation by one step.
     * Calculates the new position and velocity based on the pre-computed coefficients.
     *
     * @param pos current position
     * @param vel current velocity
     * @param equilibriumPos equilibrium position
     * @return array containing new position and velocity
     */
    @Deprecated(since = "0.3.0", forRemoval = true)
    public double[] update(double pos, double vel, double equilibriumPos) {
        return delegate.update(pos, vel, equilibriumPos);
    }
}
