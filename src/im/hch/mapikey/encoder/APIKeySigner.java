package im.hch.mapikey.encoder;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import im.hch.mapikey.common.APIKey;
import im.hch.mapikey.common.Constants;
import org.apache.log4j.Logger;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;

public class APIKeySigner {

    private Logger logger = Logger.getLogger(APIKeySigner.class);

    private Signature signature = null;

    /**
     * Init API key signer.
     * @param keystoreFile keystore file
     * @param keyStorePassword keystore password
     * @param certificateAlias alias of the private key certificate
     */
    public void init(String keystoreFile, String keyStorePassword, String certificateAlias) {
        KeyPair keyPair = RSAKeyReader.getKeyPairFromKeyStore(keystoreFile, keyStorePassword, certificateAlias);
        if (keyPair == null) {
            logger.error("Failed to init APIKeySigner.");
            return;
        }

        try {
            Signature signature = Signature.getInstance(Constants.APIKEY_ALGORITHM);
            signature.initSign(keyPair.getPrivate(), new SecureRandom());
            this.signature = signature;
        } catch (NoSuchAlgorithmException ne) {
            logger.error(String.format("Algorithm (%s) not found, failed to init APIKeySigner.", Constants.APIKEY_ALGORITHM));
        } catch (InvalidKeyException ie) {
            logger.error("Invalid private key, failed to init APIKeySigner.");
        }
    }

    /**
     * Generate the API key signature and set it to the apiKey object.
     * @param apiKey
     * @return true if successfully generated a signature
     */
    public boolean sign(APIKey apiKey) {
        if (apiKey == null || signature == null) {
            return false;
        }

        apiKey.setAlgorithm(Constants.APIKEY_ALGORITHM);
        String signature = getSignature(apiKey.toJson());
        if (signature == null) {
            return false;
        }

        apiKey.setApiKeySignature(signature);
        return true;
    }

    /**
     * Get the signature of the string.
     * @param str
     * @return the signature of the string.
     */
    private String getSignature(String str) {
        try {
            signature.update(str.getBytes());
            return Base64.encode(signature.sign());
        } catch (SignatureException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
