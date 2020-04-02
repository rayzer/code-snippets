import javax.net.ssl.*;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SSLPoke {
    private static final String KEYSTORE_PATH="/path/to/client.jks";
    private static final String KEYSTORE_PASSWORD="password of keystore";
    private static final String CA_PATH="/path/to/server_ca.pem";

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: "+SSLPoke.class.getName()+" <host> <port>");
            System.exit(1);
        }
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance( KeyManagerFactory.getDefaultAlgorithm() );
            KeyStore ks = KeyStore.getInstance( KeyStore.getDefaultType() );
            ks.load(new FileInputStream( KEYSTORE_PATH ), KEYSTORE_PASSWORD.toCharArray() );
            kmf.init( ks, KEYSTORE_PASSWORD.toCharArray() );

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate caCert = (X509Certificate) cf.generateCertificate(new FileInputStream( CA_PATH ));
            trustStore.setCertificateEntry("caCert", caCert);
            tmf.init(trustStore);

            sc.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
            SSLSocketFactory sslsocketfactory = sc.getSocketFactory();

            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(args[0], Integer.parseInt(args[1]));
 
            InputStream in = sslsocket.getInputStream();
            OutputStream out = sslsocket.getOutputStream();
 
            out.write(1);
 
            while (in.available() > 0) {
                System.out.print(in.read());
            }
            System.out.println("SUCCESS");
 
        } catch (Exception exception) {
            String message = exception.getClass().getName();
            if (message != null && message.length() > 0)
            {
                message += " : " + exception.getMessage();
            }
            System.err.println("FAILURE: " + message);
        }
    }
}