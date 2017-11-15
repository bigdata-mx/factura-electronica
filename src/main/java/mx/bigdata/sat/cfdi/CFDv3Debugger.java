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
import java.io.FileInputStream;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;

final class CFDv3Debugger {

    private final CFDv33 cfd;

    private CFDv3Debugger(CFDv33 cfd) throws Exception {
        this.cfd = cfd;
    }

    private void dumpDigests() throws Exception {
        System.err.println(cfd.getCadenaOriginal());
        String certStr = cfd.document.getCertificado();
        Base64 b64 = new Base64();
        byte[] cbs = b64.decode(certStr);
        X509Certificate cert = (X509Certificate) KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                new ByteArrayInputStream(cbs)).getKey();
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

    public static void main(String[] args) throws Exception {
        CFDv3Debugger cfd = new CFDv3Debugger(new CFDv33(new FileInputStream(args[0])));
        cfd.dumpDigests();
    }

}
