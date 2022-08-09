package createshorturl.generator;

import java.math.BigInteger;

interface Base62Encoder {
    public String encode(BigInteger value);
}
