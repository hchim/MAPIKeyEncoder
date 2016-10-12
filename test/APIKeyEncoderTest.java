import im.hch.mapikey.encoder.APIKeyEncoder;
import org.junit.Assert;
import org.junit.Test;

public class APIKeyEncoderTest {

    @Test
    public void testGenerateAndroidAPIKey() {
        APIKeyEncoder encoder = new APIKeyEncoder("KeyStore.jks", "mapikey", "mapikey");
        String apiKey = encoder.generateAndroidAPIKey("com.test.testapk",
                "0F:E4:FF:85:C2:15:91:83:96:DA:DC:7C:D8:CE:69:63:33:9A:F3:3D:37:75:1A:56:E5:4C:72:06:B6:3A:3C:7C");
        System.out.println(apiKey);
        Assert.assertNotNull(apiKey);
    }

    @Test
    public void testGenerateIOSAPIKey() {
        APIKeyEncoder encoder = new APIKeyEncoder("KeyStore.jks", "mapikey", "mapikey");
        String apiKey = encoder.generateIOSAPIKey("com.test-mapikey");
        System.out.println(apiKey);
        Assert.assertNotNull(apiKey);
    }
}
