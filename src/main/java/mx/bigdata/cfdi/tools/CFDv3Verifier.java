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

package mx.bigdata.cfdi.tools;

import java.io.*;
import java.security.*;
import java.security.cert.*;

import javax.crypto.*;
import javax.xml.bind.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import mx.bigdata.cfdi.schema.Comprobante;

public final class CFDv3Verifier {

  private final String origin;
  
  private final String xslt;

  public CFDv3Verifier(String origin, String xslt) {
    this.origin = origin;
    this.xslt = xslt;
  }
  
  public byte[] digest() throws Exception {
    StreamSource in = new StreamSource(new File(origin));
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Result out = new StreamResult(baos);
    TransformerFactory factory = TransformerFactory.newInstance();
    Transformer transformer = factory
      .newTransformer(new StreamSource(new File(xslt)));
    transformer.transform(in, out);
    byte[] bytes = baos.toByteArray();
    byte[] digest = DigestUtils.sha(bytes);
    return digest;
  }
   
  public boolean verifySignature(byte[] digest) throws Exception {
    JAXBContext jc = JAXBContext.newInstance( "mx.bigdata.cfdi.schema" );
    Unmarshaller u = jc.createUnmarshaller();
    Comprobante comp = (Comprobante) u.unmarshal(new File(origin));
    String certs = comp.getCertificado();
    Base64 b64 = new Base64();
    byte[] cbs = b64.decode(certs);
    X509Certificate cert = loadCertificate(cbs);  
    String sig = comp.getSello();
    byte[] signature = b64.decode(sig);    
    Cipher dec = Cipher.getInstance("RSA");
    dec.init(Cipher.DECRYPT_MODE, cert);
    byte[] res = dec.doFinal(signature);
    return verify(digest, res);
  }

  private X509Certificate loadCertificate(byte[] bytes) throws Exception {
    CertificateFactory  cf = CertificateFactory.getInstance("X.509");
    X509Certificate cert = (X509Certificate)
      cf.generateCertificate(new ByteArrayInputStream(bytes));
    return cert;
  }

  private boolean verify(byte[] bytes, byte[] sig) {
    if (bytes.length != sig.length) {
      return false;
    }
    for (int i = 0; i < bytes.length; i++) {
      if (bytes[i] != sig[i]) {
        return false;
      }
    }
    return true;
  }

  public static void main(String[] args) throws Exception {
    CFDv3Verifier verifier = new CFDv3Verifier(args[0], args[1]);
    byte[] digest = verifier.digest();
    verifier.verifySignature(digest);
  }
}