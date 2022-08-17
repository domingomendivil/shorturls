package createshorturl.generator;

public class Randomizer {
	
	private final Long randomRange;
	public Randomizer(Long randomRange) {
		this.randomRange=randomRange;
	}

	/*
	 * This suffix is added to the primary key to avoid "Hot partitions" 
	 * Counters for each of the suffixes must be added together to get the total count
	 * Randomization assures that tha primary keys are uniformly distributed across
	 * all the available DynamoDB partitions.
	 */
	public String getRandomSuffix() {
		double l =Math.floor(Math.random() * (randomRange + 1));
		return l+"";
	}
}
