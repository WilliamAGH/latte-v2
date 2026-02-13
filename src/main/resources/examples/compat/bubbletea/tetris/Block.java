package examples.compat.bubbletea.tetris;

import com.williamcallahan.tui4j.compat.lipgloss.color.Color;

/**
 * Example program for block.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/tetris/Block.java
 */
public class Block {

    private Position position;
    private final Color color;

    /**
     * Creates Block to keep example ready for use.
     *
     * @param position position
     * @param color color
     */
    public Block(Position position, Color color) {
        this.position = position;
        this.color = color;
    }

    /**
     * Handles position for example.
     *
     * @return result
     */
    public Position position() {
        return position;
    }

    /**
     * Handles color for example.
     *
     * @return result
     */
    public Color color() {
        return color;
    }

    /**
     * Handles move for example.
     *
     * @param dx dx
     * @param dy dy
     */
    public void move(int dx, int dy) {
        this.position = new Position(
                position().x() + dx,
                position().y() + dy
        );
    }
}
