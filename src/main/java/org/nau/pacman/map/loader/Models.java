package org.nau.pacman.map.loader;

import org.nau.pacman.map.node.*;
import org.nau.pacman.map.PacModel;

import java.util.Arrays;
import java.util.Scanner;

public class Models {
    private static final ClassLoader classLoader = Models.class.getClassLoader();
    private static final char WALL_CHAR = '#';
    private static final char CORN_CHAR = '*';
    private static final char SPACE_CHAR = ' ';
    private static final char GHOST_CHAR = 'g';
    private static final char PAC_CHAR = 'p';

    public static PacModel load(String fileName) throws NullPointerException {
        Scanner scanner = new Scanner(classLoader.getResourceAsStream(fileName));

        Node[][] rows = new Node[PacModel.MAX_HEIGHT][];
        int rowNumber = 0;
        while (scanner.hasNext()) {
            rows[rowNumber++] = rowFromString(scanner.nextLine());
        }
        return new PacModel(Arrays.copyOf(rows, rowNumber));
    }

    private static Node[] rowFromString(String rowStr) {
        Node[] nodes = new Node[rowStr.length()];
        for (int i = 0; i < rowStr.length(); i++) {
            nodes[i] = createNode(rowStr.charAt(i));
        }
        return nodes;
    }

    private static Node createNode(char nodeRepresentation) {
        switch (nodeRepresentation) {
            case PAC_CHAR:
                return new PacManUnit();
            case GHOST_CHAR:
                return new GhostUnit();
            case WALL_CHAR:
                return new Wall();
            case CORN_CHAR:
                return new Corn();
            case SPACE_CHAR:
                return new Space();
            default:
                return null;
        }
    }

}
