package tic_tac_toe;

import java.util.ArrayList;

/**
 * This class represents the AI player of the game.
 */
public class AI {
    private boolean winnable;

    /**
     * evaluate the weight of a cell (a potential placement)
     *
     * @param placement The placement array
     * @param i         row number
     * @param j         column number
     * @param who       Indicate whether 1 or -1 is the AI
     * @return weight
     */
    public int evaluate(int[][] placement, int i, int j, int who) {
        int[] sums = new int[4];
        for (int num : placement[i])
            sums[0] += num;
        for (int x = 0; x < 3; x++)
            sums[1] += placement[x][j];

        if (i == j)
            for (int x = 0; x < 3; x++)
                sums[2] += placement[x][x];
        if (i == (2 - j))
            for (int x = 0; x < 3; x++)
                sums[3] += placement[x][2 - x];
        int weight = 0;
        for (int num : sums) {
            if (num == 2 * who) weight += 10000;
            else if (num == -2 * who) weight += 1000;
            else if (num == who) weight += 10;
            else if (num == -who) weight += 6;
            else if (num == 0) weight += 4;
        }
        if (i == 1 && j == 1)
            weight += winnable ? 2 : 4;
        else if (i == j || i == (2 - j))
            weight += 2;
        else
            weight += winnable ? 2 : 1;
        return weight;
    }

    /**
     * @param placement The placement array
     * @param who       Indicate whether 1 or -1 is the AI
     * @return {rowNumber, columnNumber}, the next placement calculated by the AI
     */
    public int[] place(int[][] placement, int who) {
        int[][] emptyCells = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (placement[i][j] == 0) {
                    emptyCells[i][j] = evaluate(placement, i, j, who);
                }
            }
        }
        ArrayList<int[]> maxWeights = new ArrayList<>();
        int max = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                if (emptyCells[i][j] > 0) {
                    if (emptyCells[i][j] > max) {
                        max = emptyCells[i][j];
                        maxWeights = new ArrayList<>();
                        maxWeights.add(new int[]{i, j});
                    } else if (emptyCells[i][j] == max) {
                        max = emptyCells[i][j];
                        maxWeights.add(new int[]{i, j});
                    }
                }
            }
        return maxWeights.get((int) (Math.random() * maxWeights.size()));

    }

    public boolean isWinnable() {
        return winnable;
    }

    public void setWinnable(boolean winnable) {
        this.winnable = winnable;
    }
}
