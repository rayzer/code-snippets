
# Usage


* Use properties

```bash
java -Djavax.net.debug=ssl -Djavax.net.ssl.keyStore=/path/to/keystore.jks -Djavax.net.ssl.keyStorePassword=<key store password> -Djavax.net.ssl.trustStore=/path/to/server/ca/keystore SSLPoke <Address> <port>
```

* Use code to construct SSL

```bash
java -Djavax.net.debug=ssl SSLPoke <Address> <port>
```