import java.nio.file.*;
import java.security.*;
import java.util.Base64;

public class GenKeys {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        String priv = pem("PRIVATE KEY", kp.getPrivate().getEncoded());   // PKCS#8
        String pub = pem("PUBLIC KEY", kp.getPublic().getEncoded());      // X.509

        Path dir = Paths.get("src", "main", "resources", "certs");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("private_key.pem"), priv);
        Files.writeString(dir.resolve("public_key.pem"), pub);

        System.out.println("Wrote keys to " + dir.toAbsolutePath());
        System.out.println("Private starts: " + priv.lines().skip(1).findFirst().orElse("").substring(0, 12));
        System.out.println("Public starts:  " + pub.lines().skip(1).findFirst().orElse("").substring(0, 12));
    }

    static String pem(String type, byte[] der) {
        String b64 = Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(der);
        return "-----BEGIN " + type + "-----\n" + b64 + "\n-----END " + type + "-----\n";
    }
}

