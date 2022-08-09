package createshorturl.generator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import lombok.Getter;

public class SecureRandomGenerator implements IDGenerator {

	private final Integer randomLength;
	private final String alphabet;
	private final SecureRandom secureRandom;
	@Getter(lazy = true)
	private static final SecureRandomGenerator instance = init();

	public SecureRandomGenerator(Integer randomLength, String alphabet, String algorithm)
			throws NoSuchAlgorithmException {
		this.randomLength = randomLength;
		this.alphabet = alphabet;
		secureRandom = new SecureRandom();
	}

	public SecureRandomGenerator(Integer randomLength, String alphabet, String algorithm, String seed)
			throws NoSuchAlgorithmException {
		this.randomLength = randomLength;
		this.alphabet = alphabet;
		byte[] bytesSeed = seed.getBytes();
		secureRandom = new SecureRandom(bytesSeed);
	}

	public String generateUniqueID() {
		String customTag = secureRandom.ints(randomLength, 0, alphabet.length()).mapToObj(i -> alphabet.charAt(i))
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
		return customTag;
	}

	public static final SecureRandomGenerator init()  {
		Integer randomLength = Integer.parseInt(System.getenv("RANDOM_LENGTH"));
		String alphabet = System.getenv("RANDOM_ALPHABET");
		String algorithm = System.getenv("RANDOM_ALGORITHM");
		String seed = System.getenv("RANDOM_SEED");
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
