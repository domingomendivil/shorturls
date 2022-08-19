package shorturls.random;

import java.util.Random;

/**
 * Randomizer implementation. 
 * Constructor receives a maximum random integer
 * 
 *
 */
public class RandomizerImpl implements Randomizer{
    
	/**
	 * Java Random class to generate numbers
	 */
    private final Random random = new Random();

    /**
     * the maximum integer to be generated
     */
	private final Integer randomRange;

	/**
	 * Constructor accepts a maximum integer, such that
	 * integers from 0 to randomRange will be generated
	 * @param randomRange the maximum integer to be generated
	 */
	public RandomizerImpl(Integer randomRange) {
		this.randomRange=randomRange;
	}

	/**
	 * This suffix is added to the primary key to avoid "Hot partitions" 
	 * Counters for each of the suffixes must be added together to get the total count
	 * Randomization assures that tha primary keys are uniformly distributed across
	 * all the available DynamoDB partitions.
	 */
	public Integer getRandomInt() {
		return Integer.valueOf(random.nextInt(randomRange));
	}
}
