# Overview

This package is used to generate API key for IOS and Android platforms.
An API key is a string that in the following format:

<api key information>.<api key signature>

Both the API key information part and the API key signature part are encoded
in the Base64 schema. The API key information is a json string that contains
the following application information:

- Android
    - packageName
    - signature: the SHA256 signature of the public key certificate that was used to sign the apk file
- IOS
    - bundleId
    
The API key signature part generated from the json string with the private
key.

# Usage

## Generate keystore

Use the following command to generate a keystore.

```
keytool -genkey -alias <key alias> -keyalg RSA -keystore KeyStore.jks -keysize 2048
```

## Generate Android API key

```
APIKeyEncoder encoder = new APIKeyEncoder("KeyStore.jks", "mapikey", "mapikey");
String apiKey = encoder.generateAndroidAPIKey("com.test.testapk",
                "0F:E4:FF:85:C2:15:91:83:96:DA:DC:7C:D8:CE:69:63:33:9A:F3:3D:37:75:1A:56:E5:4C:72:06:B6:3A:3C:7C");
```

## Generate IOS API key

```
APIKeyEncoder encoder = new APIKeyEncoder("KeyStore.jks", "mapikey", "mapikey");
String apiKey = encoder.generateIOSAPIKey("com.test-mapikey");
```