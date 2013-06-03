/*
 *  Copyright 2010 BigData.mx
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package mx.bigdata.sat.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

import mx.bigdata.sat.exceptions.KeyException;
import org.apache.commons.ssl.PKCS8Key;

import com.google.common.io.ByteStreams;

public final class KeyLoader {


    /**
     * @param crtInputStream    Flujo de entrada del certificado del cual se obtiene la llave privada
     * @param passwd            Contraseña con la cual se puede obtener la información de la llave
     *                          privada
     *
     * @return  Llave privada encapsulada en el objeto {@link PrivateKey}
     *
     * @throws KeyException Lanzada si existe un problema con la lectura de la llave privada. La
     *                      excepción es lanzada si alguno de estos casos se presenta:
     *                      <ul>
     *                          <li>
     *                              Error de lectura del flujo de entrada del documento.
     *                          </li>
     *                          <li>
     *                              Error en la obtencón de la información de la llave privada debido
     *                              a que la contraseña no es correcta.
     *                          </li>
     *                          <li>
     *                              Error en la obtención de la llave privada debido a que el algoritmo
     *                              de cifrado no es el adecuado para el certificado.
     *                          </li>
     *                      </ul>
     */
    public static PrivateKey loadPKCS8PrivateKey(InputStream crtInputStream, String passwd) throws KeyException {
        byte[] decrypted = null;
        PrivateKey privateKey = null;

        try {
            decrypted = (passwd != null)
                    ? getCertBytes(crtInputStream, passwd.toCharArray())
                    : getBytes(crtInputStream);
        }catch (IOException ioe) {
            throw new KeyException("Error de E/S al leer la información del certificado", ioe.getCause());
        }

        PKCS8EncodedKeySpec keysp = new PKCS8EncodedKeySpec(decrypted);

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privateKey = kf.generatePrivate(keysp);
        }catch (GeneralSecurityException gse) {
            throw new KeyException("Error al obtener la información del certificado debido a su codificación", gse.getCause());
        }

        return privateKey;
    }


    /**
     * @param crtInputStream    Flujo de entrada del archivo que representa la llave pública codificada
     *                          en x509
     *
     * @return  Información de la llave pública encapsulada en el objeto {@link X509Certificate}
     *
     * @throws KeyException Lanzada si la codificación del certificado no es correcta.
     */
    public static X509Certificate loadX509Certificate(InputStream crtInputStream) throws KeyException {
        CertificateFactory factory = null;
        X509Certificate x509Crt = null;
        try {
            factory = CertificateFactory.getInstance("X.509");
            x509Crt =  (X509Certificate) factory.generateCertificate(crtInputStream);
        } catch (CertificateException e) {
            throw new KeyException("Error al obtener el certificado x.509. La codificación puede ser incorrecta.", e.getCause());
        }

        return x509Crt;
    }

    private static byte[] getBytes(InputStream in) throws IOException {
        try {
            return ByteStreams.toByteArray(in);
        } finally {
            in.close();
        }
    }

    private static byte[] getCertBytes(InputStream in, char[] passwd) throws KeyException, IOException {
        byte[] bytes = null;
        try {
            PKCS8Key pkcs8 = new PKCS8Key(in, passwd);
            bytes = pkcs8.getDecryptedBytes();
        } catch (GeneralSecurityException e) {
            throw new KeyException("La contraseña del certificado no es correcta", e.getCause());
        } finally {
            in.close();
        }

        return bytes;
    }

}