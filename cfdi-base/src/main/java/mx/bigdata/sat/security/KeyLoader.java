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

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.ssl.PKCS8Key;

import com.google.common.io.ByteStreams;

public final class KeyLoader {

  public static PrivateKey loadPKCS8PrivateKey(InputStream in, String passwd) 
    throws Exception {
    byte[] decrypted = (passwd != null) 
      ? getBytes(in, passwd.toCharArray())
      : getBytes(in);
    PKCS8EncodedKeySpec keysp = new PKCS8EncodedKeySpec(decrypted);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePrivate(keysp);
  }

  public static X509Certificate loadX509Certificate(InputStream in) 
    throws Exception {
    CertificateFactory factory = CertificateFactory.getInstance("X.509");
    return (X509Certificate) factory.generateCertificate(in);
  }

  private static byte[] getBytes(InputStream in) throws Exception {
    try {
      return ByteStreams.toByteArray(in);
    } finally {
      in.close();
    }
  }

  private static byte[] getBytes(InputStream in, char[] passwd) 
    throws Exception {
    try {
      PKCS8Key pkcs8 = new PKCS8Key(in, passwd);
      return pkcs8.getDecryptedBytes();
    } finally {
      in.close();
    }
  }

}