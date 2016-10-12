package im.hch.mapikey.encoder;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class RSAKeyReader {

    private static final Logger logger = Logger.getLogger(RSAKeyReader.class);
    private static final String ALGORITHM = "RSA";
    private static final String KEYSTORE_TYPE = "jks";

    /**
     * Get the key pair from the keystore file.
     * @param keystoreFile the keystore file
     * @param keyStorePassword the keystore password
     * @param certificateAlias the certificate alias
     * @return a KeyPair object
     */
    public static KeyPair getKeyPairFromKeyStore(String keystoreFile, String keyStorePassword, String certificateAlias) {
        KeyStore keyStore = loadKeyStore(keystoreFile, keyStorePassword);
        if (keyStore == null) {
            return null;
        }

        KeyPair keyPair = null;
        try {
            PrivateKey privateKey =  (PrivateKey) keyStore.getKey(certificateAlias, keyStorePassword.toCharArray());
            Certificate cert = keyStore.getCertificate(certificateAlias);
            PublicKey publicKey = cert.getPublicKey();
            keyPair = new KeyPair(publicKey, privateKey);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }

        return keyPair;
    }

    /**
     * Load Keystore from the keystore file.
     * @param keystoreFile name of the keystore file.
     * @param password keystore password
     * @return a KeyStore object
     */
    public static KeyStore loadKeyStore(String keystoreFile, String password) {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            keyStore.load(new FileInputStream(keystoreFile), password.toCharArray());
        } catch (KeyStoreException e) {
            logger.error("Failed to create an instance of KeyStore for type : " + KEYSTORE_TYPE);
        } catch (NoSuchAlgorithmException e) {
            logger.error(String.format("Algorithm (%s) not found.", ALGORITHM));
        } catch (IOException ie) {
            logger.error(String.format("Failed to load key store file: %s.", keystoreFile));
        } catch (CertificateException ce) {
            logger.error("Failed to load certificate from the keystore file.");
        }

        return keyStore;
    }
}
