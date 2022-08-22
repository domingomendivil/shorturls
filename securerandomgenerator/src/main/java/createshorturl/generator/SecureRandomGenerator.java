package createshorturl.generator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import lombok.Getter;
import lombok.val;

/**
 * Class that implements the IDGenerator.
 * It generates a secureRandom based on some input parameters
 * of the constructor. 
 */
public class SecureRandomGenerator implements IDGenerator {

	/*
	 * The length of random string to be generated.
	 * For short URLs:
	 * It's advisable to pass a randomLegth of at least
	 * 6.
	 */
	private final Integer randomLength;

	/*
	 * Alphabet to be used for generating the random string.
	 * For short URLs It's advisable to pass an alphabet 
	 * with latin letters and numbers, and without
	 * special characters.
	 */
	private final String alphabet;

	/**
	 * SecureRandom java class used to generate randoms.
	 */
	private final SecureRandom secureRandom;


	@Getter(lazy = true)
	private static final SecureRandomGenerator instance = init();

	/**
	 * Public constructor of the class. In this case no seed is 
	 * used for generating randoms.
	 * @param randomLength
	 * @param alphabet
	 * @param algorithm
	 * @throws NoSuchAlgorithmException
	 */
	public SecureRandomGenerator(Integer randomLength, String alphabet, String algorithm)
			throws NoSuchAlgorithmException {
		this.randomLength = randomLength;
		this.alphabet = alphabet;
		secureRandom = new SecureRandom();
	}

	/**
	 * Public constructor of the class. In this case a seed is passed
	 * for generating randoms.
	 * @param randomLength
	 * @param alphabet
	 * @param algorithm
	 * @param seed
	 * @throws NoSuchAlgorithmException
	 */
	public SecureRandomGenerator(Integer randomLength, String alphabet, String algorithm, String seed)
			throws NoSuchAlgorithmException {
		this.randomLength = randomLength;
		this.alphabet = alphabet;
		byte[] bytesSeed = seed.getBytes();
		secureRandom = new SecureRandom(bytesSeed);
	}

	/**
	 * Generates a random
	 */
	public String generateUniqueID() {
		val customTag = secureRandom.ints(randomLength, 0, alphabet.length()).mapToObj(i -> alphabet.charAt(i))
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
		return customTag;
	}

	/**
	 * Gets an instance of the class, based on public system environment
	 * configuration.
	 * @return
	 */
	public static final SecureRandomGenerator init()  {
		val randomLength = Integer.parseInt(System.getenv("RANDOM_LENGTH"));
		val alphabet = System.getenv("RANDOM_ALPHABET");
		val algorithm = System.getenv("RANDOM_ALGORITHM");
		val seed = System.getenv("RANDOM_SEED");
		try {
			if (seed == null)
				return new SecureRandomGenerator(randomLength, alphabet, algorithm);
			else
				return new SecureRandomGenerator(randomLength, alphabet, algorithm, seed);			
		}catch(NoSuchAlgorithmException e) {
			throw new ConfigurationException(e);
		}

	}

}
