package com.williamcallahan.tui4j.examples.conway;


import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.UpdateResult;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Example program for conway.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/conway/Conway.java
 */
public class Conway implements Model {

    record TickMessage(LocalDateTime localDateTime) implements Message{}

    private final int width;
    private final int height;
    private Set<Point> liveCells;
    private Set<Point> nextCells;

    public Conway(int width, int height, Set<Point> initialCells) {
        this.width = width;
        this.height = height;
        this.liveCells = new HashSet<>(initialCells);
        this.nextCells = new HashSet<>();
    }

    @Override
    public Command init() {
        return Command.tick(Duration.ofMillis(100), TickMessage::new);
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof TickMessage) {
            computeNextGeneration();
            return UpdateResult.from(this, Command.tick(Duration.ofMillis(100), TickMessage::new));
        }
        return UpdateResult.from(this);
    }

    private void computeNextGeneration() {
        nextCells.clear();
        Set<Point> potentialCells = new HashSet<>();

        for (Point cell : liveCells) {
            int neighbors = countLiveNeighbors(cell);
            if (neighbors == 2 || neighbors == 3) {
                nextCells.add(cell);
            }
            potentialCells.addAll(getNeighbors(cell));
        }

        for (Point cell : potentialCells) {
            if (!liveCells.contains(cell) && countLiveNeighbors(cell) == 3) {
                nextCells.add(cell);
            }
        }

        liveCells = new HashSet<>(nextCells);
    }

    private int countLiveNeighbors(Point point) {
        int count = 0;
        for (Point neighbor : getNeighbors(point)) {
            if (liveCells.contains(neighbor)) {
                count++;
            }
        }
        return count;
    }

    private Set<Point> getNeighbors(Point point) {
        Set<Point> neighbors = new HashSet<>();
        int x = point.x;
        int y = point.y;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) { // Exclude the point itself
                    int nx = (x + dx + width) % width; // Wrap horizontally
                    int ny = (y + dy + height) % height; // Wrap vertically
                    neighbors.add(new Point(nx, ny));
                }
            }
        }

        return neighbors;
    }


    @Override
    public String view() {
        char[][] grid = new char[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = liveCells.contains(new Point(x, y)) ? '█' : '·';
            }
        }

        StringBuilder builder = new StringBuilder();
        for (char[] row : grid) {
            builder.append(new String(row)).append("\n");
        }
        return builder.toString();
    }

    private record Point(int x, int y) {
    }

    public static Conway createDefault(int width, int height) {
        Set<Point> initialCells = new HashSet<>(Set.of(
                // Glider
                new Point(1, 0), new Point(2, 1), new Point(0, 2), new Point(1, 2), new Point(2, 2),
                // Blinker
                new Point(10, 10), new Point(11, 10), new Point(12, 10),
                // Lightweight Spaceship (LWSS)
                new Point(20, 20), new Point(21, 20), new Point(22, 20), new Point(22, 19), new Point(21, 18)
        ));

        return new Conway(width, height, initialCells);
    }

    public static Conway createRandom(int width, int height, double liveProbability) {
        Set<Point> initialCells = new HashSet<>();

        Random random = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (random.nextDouble() < liveProbability) { // e.g., 0.2 for 20% chance
                    initialCells.add(new Point(x, y));
                }
            }
        }

        return new Conway(width, height, initialCells);
    }

}
