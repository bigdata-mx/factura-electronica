/*
 *  Copyright 2010-2011 BigData.mx
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
package mx.bigdata.sat.cfdi.examples;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import mx.bigdata.sat.cfdi.CFDv33;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;

public final class Main {

    public static void main(String[] args) throws Exception {
        CFDv33 cfd = new CFDv33(ExampleCFDv33Factory.createComprobante(), "mx.bigdata.sat.cfdi.examples");
        cfd.addNamespace("http://www.bigdata.mx/cfdi/example", "example");

        PrivateKey key = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
                new FileInputStream("resources/certs/CSD_Prueba_CFDI_LAN8507268IA.key"),
                "12345678a"
        ).getKey();

        X509Certificate cert = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                new FileInputStream("resources/certs/CSD_Prueba_CFDI_LAN8507268IA.cer")
        ).getKey();

        Comprobante sellado = cfd.sellarComprobante(key, cert);
        System.err.println(sellado.getSello());
        cfd.validar();
        cfd.verificar();
        ByteArrayOutputStream by = new ByteArrayOutputStream();
        cfd.guardar(by);
        cfd.guardar(System.out);
    }

}
