package test;

import java.util.Random;

/**
 * a class to give a predictable random sequence.
 */
public class PredictableRandom extends Random {
  private int sequentialNum = 0;
  private final int[] randomSequence;

  /**
   * the random sequence of the generator.
   *
   * @param randomSequence the random sequence.
   */
  public PredictableRandom(int[] randomSequence) {
    super();
    this.randomSequence = randomSequence;
  }

  /**
   * the random generator.
   *
   * @param bound the bound integer.
   * @return the random number.
   */
  public int nextInt(int bound) {
    int res = randomSequence[sequentialNum] - 1;
    sequentialNum++;
    return res;
  }
}
