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

package mx.bigdata.sat.cfdi;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import mx.bigdata.sat.security.KeyLoader;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;

public final class CFDv3Debugger {

  private final CFDv3 cfd;

  public CFDv3Debugger(CFDv3 cfd) throws Exception {
    this.cfd = cfd;
  }

  public void dumpDigests() throws Exception {
    System.err.println(cfd.getOriginalString());
    byte[] digest = cfd.getDigest();
    CFDv3.dump("Digestion generada", digest, System.err);
    String certStr = cfd.document.getCertificado();
    Base64 b64 = new Base64();
    byte[] cbs = b64.decode(certStr);
    X509Certificate cert = KeyLoader
      .loadX509Certificate(new ByteArrayInputStream(cbs)); 
    cert.checkValidity(); 
    String sigStr = cfd.document.getSello();
    byte[] signature = b64.decode(sigStr); 
    CFDv3.dump("Digestion firmada", signature, System.err);
    Cipher dec = Cipher.getInstance("RSA");
    dec.init(Cipher.DECRYPT_MODE, cert);
    byte[] result = dec.doFinal(signature);
    CFDv3.dump("Digestion decriptada", result, System.err);
    ASN1InputStream aIn = new ASN1InputStream(result);
    ASN1Sequence seq = (ASN1Sequence) aIn.readObject();
    ASN1OctetString sigHash = (ASN1OctetString) seq.getObjectAt(1);
    CFDv3.dump("Sello", sigHash.getOctets(), System.err);
  }
}
