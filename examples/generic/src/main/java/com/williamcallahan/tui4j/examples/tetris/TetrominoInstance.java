package com.williamcallahan.tui4j.examples.tetris;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Example program for tetromino instance.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/tetris/TetrominoInstance.java
 */
public class TetrominoInstance {

    private Block[] blocks;
    private final Color color;
    private final Tetromino type;
    private int rotationState = 0;

    private int originX;
    private int originY;

    public TetrominoInstance(Tetromino tetromino) {
        this.type = tetromino;

        int colorCode = new Random().nextInt(200) + 10;
        this.color = Color.color("" + colorCode);
        this.blocks = tetromino.buildBlocks(color);
    }

    public void moveTo(int dx, int dy) {
        originX += dx;
        originY += dy;

        for (Block block : blocks) {
            block.move(dx, dy);
        }
    }

    public Block[] blocks() {
        return blocks;
    }

    public void rotate(int gridWidth, int gridHeight) {
        // Simulate the next rotation
        int nextRotationState = (rotationState + 1) % type.getRotationStatesCount();
        Block[] newBlocks = type.buildBlocks(nextRotationState, color);

        // Offset the new blocks to align with the current tetromino's origin
        for (Block block : newBlocks) {
            block.move(originX, originY);

            // Check if the block is out of bounds
            if (block.position().x() < 0 || block.position().x() >= gridWidth ||
                    block.position().y() < 0 || block.position().y() >= gridHeight) {
                return; // Cancel rotation if it would result in an invalid state
            }
        }

        // If valid, update the rotation state and replace the blocks
        rotationState = nextRotationState;
        blocks = newBlocks;
    }


    public String preview(int width, int height) {
        char[][] grid = new char[height][width];
        for (char[] row : grid) Arrays.fill(row, ' ');

        for (Block block : blocks) {
            Position pos = block.position();
            pos = new Position(pos.x() - originX, pos.y() - originY);
            if (pos.y() < height && pos.x() < width) {
                grid[pos.y()][pos.x()] = '█';
            }
        }

        return Arrays.stream(grid)
                .map(String::new)
                .collect(Collectors.joining("\n")) + "\n";
    }
}
