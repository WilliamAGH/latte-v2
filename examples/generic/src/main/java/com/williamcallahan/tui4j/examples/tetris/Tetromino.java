package com.williamcallahan.tui4j.examples.tetris;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Example program for tetromino.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/tetris/Tetromino.java
 */
public enum Tetromino {

    I(new String[]{
            """
                \s
        ########
        
        
        """,

            """
            ##
            ##
            ##
            ##
        """
    }),
    O(new String[]{
            """
          ####
          ####
        """
    }),
    L(new String[]{
            """
              \s
        ######
            ##
        """,

            """
          ##
          ##
        ####
        """,

            """
              \s
        ##
        ######
        """,

            """
          ####
          ##
          ##
        """
    }),
    J(new String[]{
            """
              \s
        ######
        ##
        """,

            """
        ####
          ##
          ##
        """,

            """
              \s
            ##
        ######
        """,

            """
          ##
          ##
          ####
        """
    }),
    S(new String[]{
            """
              \s
          ####
        ####
        """,

            """
        ##
        ####
          ##
        """
    }),
    T(new String[]{
            """
              \s
        ######
          ##
        """,

            """
          ##
        ####
          ##
        """,

            """
              \s
          ##
        ######
        """,

            """
          ##
          ####
          ##
        """
    }),
    Z(new String[]{
            """
              \s
        ####
          ####
        """,

            """
            ##
          ####
          ##
        """
    });

    private final String[] rotations;

    Tetromino(String[] rotations) {
        this.rotations = rotations;
    }

    public TetrominoInstance newInstance() {
        return new TetrominoInstance(this);
    }

    public Block[] buildBlocks(Color color) {
        return buildBlocks(0, color);
    }

    public static Tetromino random() {
        return values()[(int) (Math.random() * values().length)];
    }

    public int getRotationStatesCount() {
        return rotations.length;
    }

    public Block[] buildBlocks(int rotation, Color color) {
        Position[] positions = toPositions(rotation);

        Block[] blocks = new Block[positions.length];
        for (int i = 0; i < positions.length; i++) {
            Position pos = positions[i];
            blocks[i] = new Block(pos, color);
        }
        return blocks;
    }

    public Position[] toPositions(int rotationIndex) {
        String[] lines = rotations[rotationIndex].split("\\n");
        List<Position> positions = new ArrayList<>();

        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                char charAt = line.charAt(x);
                if (charAt == '#') {
                    positions.add(new Position(x, y));
                }
            }
        }

        return positions.toArray(new Position[0]);
    }
}