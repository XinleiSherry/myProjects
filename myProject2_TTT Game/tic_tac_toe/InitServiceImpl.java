package tic_tac_toe;

import java.util.ArrayList;

/**
 * This is the class implements the evaluation and placement functions
 */
public class InitServiceImpl implements InitService {
    /**
     * This method is to evaluate a specific square.
     * @param placement     square to be evaluated
     * @param i             row
     * @param j             column
     * @param who           player
     * @param winnable
     * @return
     */
    @Override
    public int evaluate(int[][] placement, int i, int j, int who,  boolean winnable) {
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
     * This method is to determine where to go for the next move.
     * @param placement
     * @param who
     * @param winnable
     * @return
     */
    @Override
    public int[] place(int[][] placement, int who, boolean winnable) {
        int[][] emptyCells = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (placement[i][j] == 0) {
                    emptyCells[i][j] = evaluate(placement, i, j, who, winnable);
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

}
