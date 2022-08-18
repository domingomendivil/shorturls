package shorturls.random;

import java.util.Random;

public class RandomizerImpl implements Randomizer{
    
    private final Random random = new Random();

	private final Integer randomRange;

	public RandomizerImpl(Integer randomRange) {
		this.randomRange=randomRange;
	}

	/*
	 * This suffix is added to the primary key to avoid "Hot partitions" 
	 * Counters for each of the suffixes must be added together to get the total count
	 * Randomization assures that tha primary keys are uniformly distributed across
	 * all the available DynamoDB partitions.
	 */
	public Integer getRandomInt() {
		return Integer.valueOf(random.nextInt(randomRange));
	}
}
