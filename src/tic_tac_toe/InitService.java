package tic_tac_toe;

/**
 * This is the interface that can evaluate a position of the grid and
 * determine the good  choices for the next step
 **/
public interface InitService {

    int evaluate(int[][] placement, int i, int j, int who, boolean winnable);


    int[] place(int[][] placement, int who, boolean winnable);
}
