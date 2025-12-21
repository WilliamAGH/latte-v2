package com.williamcallahan.tui4j.examples.fireworks;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor;
import com.williamcallahan.tui4j.compat.bubbletea.message.WindowSizeMessage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Example program for fireworks.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/fireworks/Fireworks.java
 */
public class Fireworks implements Model {

    record TickMessage(LocalDateTime localDateTime) implements Message{}

    private int width;
    private int height;
    private final List<Firework> fireworks;
    private final Random random;

    public Fireworks(int width, int height) {
        this.width = width;
        this.height = height;
        this.fireworks = new ArrayList<>();
        this.random = new Random();
    }

    @Override
    public Command init() {
        return Command.sequence(
                Command.checkWindowSize(),
                Command.tick(Duration.ofMillis(16), TickMessage::new) // ~60 FPS
        );
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof TickMessage) {
            updateFireworks();
            addNewFireworkWithChance(0.05);
            return UpdateResult.from(this, Command.tick(Duration.ofMillis(16), TickMessage::new));
        } else if (msg instanceof WindowSizeMessage windowSizeMessage) {
            this.width = windowSizeMessage.width();
            this.height = windowSizeMessage.height();
        }
        return UpdateResult.from(this);
    }

    private void updateFireworks() {
        // Update existing fireworks
        fireworks.forEach(Firework::update);
        // Remove dead fireworks
        fireworks.removeIf(Firework::isDead);
    }

    private void addNewFireworkWithChance(double chance) {
        if (random.nextDouble() < chance) {
            int x = random.nextInt(width);
            int y = height - 1; // Start at the bottom of the screen
            TerminalColor effectBaseColor = Color.color(random.nextInt(256) + "");
            fireworks.add(new Firework(x, y, effectBaseColor));
        }
    }

    @Override
    public String view() {
        String[][] grid = new String[height][width];
        for (String[] row : grid) {
            java.util.Arrays.fill(row, " ");
        }

        // Render fireworks onto the grid
        for (Firework firework : fireworks) {
            firework.render(grid);
        }

        return java.util.Arrays.stream(grid)
                .map(strings -> String.join("", strings))
                .collect(Collectors.joining("\n"));
    }

    private static class Firework {
        private Particle rocket;
        private final List<Particle> effect;
        private final TerminalColor effectBaseColor;
        private final Random random;

        public Firework(int x, int y, TerminalColor effectBaseColor) {
            this.rocket = new Particle(x, y, effectBaseColor)
                    .withSpeed(0, -1.0 - Math.random() * 0.2)
                    .withAcceleration(0, 0.05);
            this.effect = new ArrayList<>();
            this.effectBaseColor = effectBaseColor;
            this.random = new Random();
        }

        public void update() {
            if (rocket != null) {
                rocket.update();

                if (rocket.getSpeedY() >= -0.1) { // Lower threshold for explosion
                    // Rocket explodes
                    for (int i = 0; i < 25; i++) {
                        double speedX = (random.nextDouble() - 0.5) * 1.5; // Slightly less spread
                        double speedY = (random.nextDouble() - 0.5) * 1.5; // Keep explosion contained

                        effect.add(new Particle(
                                (int) rocket.getX(),
                                (int) rocket.getY(),
                                effectBaseColor
                        ).withSpeed(speedX, speedY).withAcceleration(0, 0.01).withFading(0.02)); // Slower fade
                    }
                    rocket = null;
                }
            }

            for (Particle particle : effect) {
                particle.update();
            }
        }


        public void render(String[][] grid) {
            if (rocket != null) {
                rocket.render(grid);
            }
            for (Particle particle : effect) {
                particle.render(grid);
            }
        }

        public boolean isDead() {
            return rocket == null && effect.stream().allMatch(Particle::isDead);
        }
    }

    private static class Particle {
        private final Style style;
        private double x;
        private double y;
        private double speedX;
        private double speedY;
        private double accelerationX;
        private double accelerationY;
        private double lifetime;
        private double fading;

        public Particle(int x, int y, TerminalColor color) {
            this.style = Style.newStyle().foreground(color);
            this.x = x;
            this.y = y;
            this.speedX = 0;
            this.speedY = 0;
            this.accelerationX = 0;
            this.accelerationY = 0;
            this.lifetime = 1.0;
            this.fading = 0.0;
        }

        public Particle withSpeed(double speedX, double speedY) {
            this.speedX = speedX;
            this.speedY = speedY;
            return this;
        }

        public Particle withAcceleration(double accelerationX, double accelerationY) {
            this.accelerationX = accelerationX;
            this.accelerationY = accelerationY;
            return this;
        }

        public Particle withFading(double fading) {
            this.fading = fading;
            return this;
        }

        public void update() {
            if (lifetime <= 0) {
                return;
            }

            speedX += accelerationX;
            speedY += accelerationY;

            x += speedX;
            y += speedY;

            lifetime -= fading;
        }

        public void render(String[][] grid) {
            if (lifetime <= 0) {
                return;
            }

            int posX = (int) Math.round(x);
            int posY = (int) Math.round(y);

            if (posX >= 0 && posX < grid[0].length && posY >= 0 && posY < grid.length) {
                grid[posY][posX] = style.render("▪");
            }
        }

        public boolean isDead() {
            return lifetime <= 0;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getSpeedY() {
            return speedY;
        }
    }

    public static void main(String[] args) {
        new Program(new Fireworks(80, 80))
                .withAltScreen()
                .run();
    }
}
