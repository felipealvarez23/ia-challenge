package co.com.bancolombia.usecase.processcustomerstats.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {
    public static String generateMd5(String data) {
        try {
            // 1. Obtener una instancia de MessageDigest para el algoritmo MD5.
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 2. Calcular el hash. El método digest() devuelve un array de bytes.
            byte[] messageDigest = md.digest(data.getBytes(StandardCharsets.UTF_8));

            // 3. Convertir el array de bytes a una representación de signo positivo.
            BigInteger no = new BigInteger(1, messageDigest);

            // 4. Convertir el BigInteger a una cadena hexadecimal.
            String hashtext = no.toString(16);

            // 5. Rellenar con ceros a la izquierda hasta que tenga 32 caracteres.
            //    Un hash MD5 siempre tiene 32 caracteres hexadecimales.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            // Esta excepción no debería ocurrir nunca, ya que MD5 es un algoritmo estándar
            // soportado por todas las JVM. Si ocurre, es un error irrecuperable del entorno.
            throw new RuntimeException("No se pudo encontrar el algoritmo MD5", e);
        }
    }
}
