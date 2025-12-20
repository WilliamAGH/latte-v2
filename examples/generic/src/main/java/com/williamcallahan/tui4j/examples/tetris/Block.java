package com.williamcallahan.tui4j.examples.tetris;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;

/**
 * Example program for block.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/tetris/Block.java
 */
public class Block {

    private Position position;
    private final Color color;

    public Block(Position position, Color color) {
        this.position = position;
        this.color = color;
    }

    public Position position() {
        return position;
    }

    public Color color() {
        return color;
    }

    public void move(int dx, int dy) {
        this.position = new Position(
                position().x() + dx,
                position().y() + dy
        );
    }
}
