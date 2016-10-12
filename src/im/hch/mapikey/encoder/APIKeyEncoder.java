package im.hch.mapikey.encoder;

import im.hch.mapikey.common.APIKey;
import im.hch.mapikey.common.AndroidAPIKey;
import im.hch.mapikey.common.IOSAPIKey;

import java.util.Base64;

public class APIKeyEncoder {

    public static final APIKey.Version CURRENT_APIKEY_VERSION = APIKey.Version.V1;
    private APIKeySigner signer;

    public APIKeyEncoder(String keystore, String password, String certAlias) {
        signer = new APIKeySigner();
        signer.init(keystore, password, certAlias);
    }

    public String generateAndroidAPIKey(String packageName, String signature) {
        AndroidAPIKey apiKey = new AndroidAPIKey();
        apiKey.setVersion(CURRENT_APIKEY_VERSION);
        apiKey.setPackageName(packageName);
        apiKey.setSignature(signature);
        return encode(apiKey);
    }

    public String generateIOSAPIKey(String bundleId) {
        IOSAPIKey apiKey = new IOSAPIKey();
        apiKey.setVersion(CURRENT_APIKEY_VERSION);
        apiKey.setBundleId(bundleId);
        return encode(apiKey);
    }

    /**
     * Encode the api key in the following format:
     *
     * Base64(jsonStr) + "." + Signature
     *
     * jsonStr is the json string of the API Key.
     *
     * @param apiKey
     * @return
     */
    private String encode(APIKey apiKey) {
        if (!signer.sign(apiKey)) {
            return null;
        }

        String json = apiKey.toJson();
        String base64 = new String(Base64.getEncoder().encode(json.getBytes()));
        return String.format("%s.%s", base64, apiKey.getApiKeySignature());
    }
}
