package createshorturl.generator;

import java.math.BigInteger;

interface Encoder {
    public String encode(BigInteger value);
}
