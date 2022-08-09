package shorturl.idgenerator;

import java.math.BigInteger;

public interface Base62Encoder {
    public String encode(BigInteger value);
}
