package net.thesyndicate.games;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by dboolbe on 6/12/14.
 */
public class TeleTiles {

    private int maxRow = 3;
    private int maxColumn = 3;

    private int[][] grid;
    private int[][] gridCorrect;

    /**
     * Creates a game with the default 3x3 grid.
     */
    public TeleTiles() {
        grid = new int[maxRow][maxColumn];
        gridCorrect = new int[maxRow][maxColumn];
        reset();
    }

    /**
     * Creates a games with the provided dimensions for the grid
     *
     * @param maxRow    the number of cells per row
     * @param maxColumn the number of cells per column
     */
    public TeleTiles(int maxRow, int maxColumn) {
        this.maxRow = maxRow;
        this.maxColumn = maxColumn;

        grid = new int[maxRow][maxColumn];
        gridCorrect = new int[maxRow][maxColumn];
        reset();
    }

    /**
     * Resets the game grid and reshuffles the cells
     */
    public void reset() {
        int idx = 0;
        for (int row = 0; row < maxRow; row++)
            for (int column = 0; column < maxColumn; column++) {
                grid[row][column] = idx;
                gridCorrect[row][column] = idx;
                idx++;
            }

        // create an array containing all the possible integers in the grid
        ArrayList<Integer> integers = new ArrayList<Integer>(maxRow * maxColumn);
        for (int index = 0; index < (maxRow * maxColumn); index++)
            integers.add(index);

        // create a random generator
        Random random = new Random(System.currentTimeMillis());
        for (int row = 0; row < maxRow; row++)
            for (int column = 0; column < maxColumn; column++) {
                // randomly select an index to an element in the array
                int index = random.nextInt(integers.size());
                // add the element to the grid and remove it from the array
                grid[row][column] = integers.remove(index);
            }

        // Debug Purposes Only
        printGrid();
    }

    private void printGrid() {
        System.out.println("--- Attempted ---");
        for (int row = 0; row < maxRow; row++) {
            for (int column = 0; column < maxColumn; column++)
                System.out.print(String.format("%S ", grid[row][column]));
            System.out.println();
        }
        printGridCorrect();
    }

    private void printGridCorrect() {
        System.out.println("--- Please don't cheat!!! ---");
        System.out.println("--- Correct ---");
        for (int row = 0; row < maxRow; row++) {
            for (int column = 0; column < maxColumn; column++)
                System.out.print(String.format("%S ", gridCorrect[row][column]));
            System.out.println();
        }
    }

    private void rotateClockwiseRow(int index) {
        // checks to make sure the row index is within bounds
        if ((index >= 0) && (index < maxRow)) {
            // temporary store the last element on the indexed row
            int temp = grid[index][maxColumn - 1];
            // rotate the elements to the end of the row
            for (int column = (maxColumn - 1); column > 0; column--)
                grid[index][column] = grid[index][column - 1];
            // put the temporarily stored element at the beginning of the row
            grid[index][0] = temp;
        }
    }

    private void rotateClockwiseColumn(int index) {
        // checks to make sure the column index is within bounds
        if ((index >= 0) && (index < maxColumn)) {
            // temporary store the last element on the indexed column
            int temp = grid[maxRow - 1][index];
            // rotate the elements to the end of the column
            for (int row = (maxRow - 1); row > 0; row--)
                grid[row][index] = grid[row - 1][index];
            // put the temporarily stored element at the beginning of the column
            grid[0][index] = temp;
        }
    }

    private void rotateCounterclockwiseRow(int index) {
        // checks to make sure the row index is within bounds
        if ((index >= 0) && (index < maxRow)) {
            // temporary store the first element on the indexed row
            int temp = grid[index][0];
            // rotate the elements to the beginning of the row
            for (int column = 0; column < (maxColumn - 1); column++)
                grid[index][column] = grid[index][column + 1];
            // put the temporarily stored element at the beginning of the row
            grid[index][maxColumn - 1] = temp;
        }
    }

    private void rotateCounterclockwiseColumn(int index) {
        // checks to make sure the column index is within bounds
        if ((index >= 0) && (index < maxColumn)) {
            // temporary store the first element on the indexed column
            int temp = grid[0][index];
            // rotate the elements to the beginning of the column
            for (int row = 0; row < (maxRow - 1); row++)
                grid[row][index] = grid[row + 1][index];
            // put the temporarily stored element at the beginning of the column
            grid[maxRow - 1][index] = temp;
        }
    }

    public void rowXleft(int index) {
        rotateCounterclockwiseRow(index);
        printGrid();
    }

    public void rowXright(int index) {
        rotateClockwiseRow(index);
        printGrid();
    }

    public void columnXup(int index) {
        rotateCounterclockwiseColumn(index);
        printGrid();
    }

    public void columnXdown(int index) {
        rotateClockwiseColumn(index);
        printGrid();
    }

    public int getMaxRow() {
        return maxRow;
    }

    public int getMaxColumn() {
        return maxColumn;
    }

    public int getGridElement(int row, int column) {
        return grid[row][column];
    }

    public int getCorrectGridElement(int row, int column) {
        return gridCorrect[row][column];
    }

    public boolean testGridElement(int row, int column) {
        return grid[row][column] == gridCorrect[row][column];
    }

    /**
     * Tests whether the grid is in a completed state.
     *
     * @return true iff the grid is sorted else false
     */
    public boolean isGameOver() {
        boolean gameOver = true;
        for (int row = 0; row < maxRow; row++)
            for (int column = 0; column < maxColumn; column++) {
                if (grid[row][column] != gridCorrect[row][column]) {
                    gameOver = false;
                    break;
                }
            }
        return gameOver;
    }
}
