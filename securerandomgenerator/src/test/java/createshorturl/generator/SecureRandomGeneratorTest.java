package createshorturl.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import lombok.val;

public class SecureRandomGeneratorTest {


    @Test
    public void test1()throws NoSuchAlgorithmException{
        Integer randomLength=1;
        val alphabet="a";
        val algorithm="SHA1PRNG";
        val rand =new SecureRandomGenerator(randomLength, alphabet, algorithm);
        val res =rand.generateUniqueID();
        assertTrue(randomLength==res.length());
        assertEquals("a",res);
    }


    @Test
    public void test2()throws NoSuchAlgorithmException{
        Integer randomLength=100;
        val alphabet="a";
        val algorithm="SHA1PRNG";
        val rand =new SecureRandomGenerator(randomLength, alphabet, algorithm);
        val res =rand.generateUniqueID();
        assertTrue(randomLength==res.length());
        val str = generateLongString("a",randomLength);
        assertEquals(str,res);
    }

    @Test
    public void test3()throws NoSuchAlgorithmException{
        Integer randomLength=100;
        val alphabet="a";
        val algorithm="SHA1PRNG";
        val rand =new SecureRandomGenerator(randomLength, alphabet, algorithm);
        val res =rand.generateUniqueID();
        assertTrue(randomLength==res.length());
        for (int i=0;i<randomLength;i++){
            assertTrue(alphabet.contains(res.charAt(i)+""));   
        }
    }

    private String generateLongString(String character,Integer nro){
        StringBuffer sb= new StringBuffer();
        for (int i=0;i<nro;i++){
            sb.append(character);
        }
        return sb.toString();
    } 
}
