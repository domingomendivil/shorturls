package shorturls.random;

/**
 * Interface used for generating random integers
 * This class is used when generating distributed counters in DynamoDb
 * where integers are added as suffixes to primary keys,
 * so this allows to scale horizontally, by having multiple
 * partitions, avoiding throughput problems on each node
 * and hot partitions.
 *
 */
public interface Randomizer {

    public Integer getRandomInt();
    
}
