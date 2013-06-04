package mx.bigdata.sat.security;

import lombok.Getter;
import mx.bigdata.sat.exceptions.KeyException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created with IntelliJ IDEA.
 * User: Gerardo Aquino
 * Date: 3/06/13
 */
public class PublicKeyLoader implements KeyLoader {

    @Getter
    X509Certificate key;


    public PublicKeyLoader(String certificateLocation) {
        try {
            this.setX509Certificate(new FileInputStream(certificateLocation));
        } catch (FileNotFoundException fnfe) {
            throw new KeyException("La ubicación del archivo de la llave pública es incorrecta", fnfe.getCause());
        }
    }



    public PublicKeyLoader(InputStream crtInputStream) {
        this.setX509Certificate(crtInputStream);
    }



    public void setX509Certificate(InputStream crtInputStream) {
        try {
            this.key = (X509Certificate)
                    CertificateFactory.getInstance("X.509").generateCertificate(crtInputStream);
        } catch (CertificateException e) {
            throw new KeyException("Error al obtener el certificado x.509. La codificación puede ser incorrecta.", e.getCause());
        }
    }

}
