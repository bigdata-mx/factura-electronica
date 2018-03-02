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

import java.io.FileInputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import mx.bigdata.sat.cfdi.CFDv3;
import mx.bigdata.sat.cfdi.schema.Comprobante;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;

public final class Main {

    public static void main(String[] args) throws Exception {
        CFDv3 cfd = new CFDv3(ExampleCFDFactory.createComprobante(), "mx.bigdata.sat.cfdi.examples");
        cfd.addNamespace("http://www.bigdata.mx/cfdi/example", "example");

        PrivateKey key = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
                new FileInputStream(args[0]),
                args[1]
        ).getKey();

        X509Certificate cert = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                new FileInputStream(args[2])
        ).getKey();

        Comprobante sellado = cfd.sellarComprobante(key, cert);
        System.err.println(sellado.getSello());
        cfd.validar();
        cfd.verificar();
        cfd.guardar(System.out);
    }

}
