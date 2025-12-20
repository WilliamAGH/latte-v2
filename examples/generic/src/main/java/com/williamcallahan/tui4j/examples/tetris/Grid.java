package com.williamcallahan.tui4j.examples.tetris;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.williamcallahan.tui4j.Command.tick;

/**
 * Example program for grid.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/tetris/Grid.java
 */
public class Grid implements Model {

    record TickMessage(LocalDateTime localDateTime) implements Message {
    }

    private final int width;
    private final int height;

    private int totalLinesCleared;
    private int score;
    private int level;

    private Block[][] blocks;
    private Style blockStyle = Style.newStyle();
    private TetrominoInstance nextPiece = Tetromino.random().newInstance();
    private TetrominoInstance currentPiece;
    private boolean currentPieceLocked;
    private Duration tickRate;

    private boolean gameOver;

    public Grid(int width, int height) {
        this.level = 1;
        this.width = width;
        this.height = height;
        this.blocks = new Block[height][width];
        this.tickRate = Duration.ofMillis(500);
        spawnNewPiece();
    }

    @Override
    public Command init() {
        return tick(tickRate, TickMessage::new);
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof TickMessage) {
            return handleTick();
        } else if (msg instanceof KeyPressMessage keyPressMessage) {
            return handleKey(keyPressMessage);
        }

        return UpdateResult.from(this);
    }

    private UpdateResult<? extends Model> handleTick() {
        if (canMove(0, 1)) {
            moveBlocks(0, 1);
        } else {
            lockCurrentPiece(true);

            if (isGameOver()) {
                this.gameOver = true;
                return UpdateResult.from(this, GameOverMessage::new);
            }

            spawnNewPiece();
        }
        return UpdateResult.from(this, tick(tickRate, TickMessage::new));
    }

    private UpdateResult<? extends Model> handleKey(KeyPressMessage key) {
        switch (key.key()) {
            case "left" -> {
                if (canMove(-1, 0)) moveBlocks(-2, 0);
            }
            case "right" -> {
                if (canMove(1, 0)) moveBlocks(2, 0);
            }
            case "down" -> {
                if (canMove(0, 1)) {
                    moveBlocks(0, 1);
                } else {
                    lockCurrentPiece(true);
                }
            }
            case "up" -> {
                currentPiece.rotate(width, height);
            }
            case " " -> {
                hardDrop();
            }
        }
        return UpdateResult.from(this);
    }

    private void hardDrop() {
        int rowsDropped = 0;
        while (canMove(0, 1)) {
            moveBlocks(0, 1);
            rowsDropped++;
        }
        lockCurrentPiece(false);
        score += rowsDropped * 2;
    }

    private boolean canMove(int dx, int dy) {
        for (Block block : currentPiece.blocks()) {
            Position position = block.position();
            Position newPos = new Position(position.x() + dx, position.y() + dy);
            if (newPos.x() < 0 || newPos.x() >= width ||
                    newPos.y() < 0 || newPos.y() >= height ||
                    blocks[newPos.y()][newPos.x()] != null) {
                return false;
            }
        }
        return true;
    }

    private void lockCurrentPiece(boolean softDrop) {
        if (currentPieceLocked) {
            return;
        }

        this.currentPieceLocked = true;
        for (Block block : currentPiece.blocks()) {
            Position pos = block.position();
            blocks[pos.y()][pos.x()] = block;
        }

        int removed = removeFullLines();
        if (removed == 0 && softDrop) {
            score += level;
        }
        if (removed > 0) {
            score += calculatePoints(removed);
        }

        if (totalLinesCleared / 10 > level) {
            level++;
            tickRate = tickRate.minusMillis(50);
        }
    }

    private int calculatePoints(int linesCleared) {
        return switch (linesCleared) {
            case 1 -> 40 * (level + 1);
            case 2 -> 100 * (level + 1);
            case 3 -> 300 * (level + 1);
            case 4 -> 1200 * (level + 1); // Tetris!
            default -> 0;
        };
    }

    private int removeFullLines() {
        int linesCleared = 0;

        for (int y = height - 1; y >= 0; y--) {
            if (isLineFull(y)) {
                clearLine(y);
                shiftRowsDown(y);
                linesCleared++;
                y++; // Recheck the same row
            }
        }

        totalLinesCleared += linesCleared;
        return linesCleared;
    }

    private boolean isLineFull(int y) {
        for (int x = 0; x < width; x++) {
            if (blocks[y][x] == null) {
                return false;
            }
        }
        return true;
    }

    private void clearLine(int y) {
        Arrays.fill(blocks[y], null);
    }

    private void shiftRowsDown(int y) {
        for (int aboveY = y - 1; aboveY >= 0; aboveY--) {
            System.arraycopy(blocks[aboveY], 0, blocks[aboveY + 1], 0, width);
            Arrays.fill(blocks[aboveY], null);
        }
    }

    private void moveBlocks(int dx, int dy) {
        currentPiece.moveTo(dx, dy);
    }

    @Override
    public String view() {
        String[][] grid = new String[height][];
        for (int i = 0; i < grid.length; i++) {
            grid[i] = new String[width];
            Arrays.fill(grid[i], "·");
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (blocks[y][x] != null) {
                    grid[y][x] = blockStyle.foreground(blocks[y][x].color()).render("█");
                }
            }
        }

        for (Block block : currentPiece.blocks()) {
            Position pos = block.position();
            if (pos.y() >= 0 && pos.y() < height && pos.x() >= 0 && pos.x() < width) { // Ensure within bounds
                grid[pos.y()][pos.x()] = blockStyle.foreground(block.color()).render("█");
            }
        }

        return Arrays.stream(grid)
                .map(row -> String.join("", row))
                .collect(Collectors.joining("\n")) + "\n";
    }


    private void spawnNewPiece() {
        this.currentPiece = nextPiece;
        this.currentPieceLocked = false;
        currentPiece.moveTo(width / 2 - 4, 0);

        this.nextPiece = Tetromino.random().newInstance();
    }

    public String nextBlockPreview() {
        return nextPiece.preview(8, 4);
    }

    private boolean isGameOver() {
        for (Block block : nextPiece.blocks()) {
            Position pos = block.position();
            int spawnX = pos.x() + width / 2 - 4;
            int spawnY = pos.y();

            if (blocks[spawnY][spawnX] != null) {
                return true;
            }
        }
        return false;
    }

    public boolean gameOver() {
        return gameOver;
    }

    public int score() {
        return score;
    }

    public int level() {
        return level;
    }
}
