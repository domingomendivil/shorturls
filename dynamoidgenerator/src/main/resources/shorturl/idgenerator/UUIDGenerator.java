package shorturl.idgenerator;

import java.util.UUID;

public class UUIDGenerator implements IDGenerator {

	@Override
	public String generateUniqueID() {
		UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString().replace("-","");
		return new Base62EncoderImpl().encodeUUID(uuidStr);
	}

}
