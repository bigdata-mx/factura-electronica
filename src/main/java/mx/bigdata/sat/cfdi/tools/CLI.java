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
package mx.bigdata.sat.cfdi.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

import mx.bigdata.sat.cfdi.*;
import mx.bigdata.sat.common.ValidationErrorHandler;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;
import org.xml.sax.SAXParseException;

public final class CLI {

    public static void main(String[] args) throws Exception {
        String cmd = args[0];
        switch (cmd) {
            case "validar": {
                String file = args[1];
                CFDI33 cfd = CFDIFactory.load33(new File(file));
                ValidationErrorHandler handler = new ValidationErrorHandler();
                cfd.validar(handler);
                List<SAXParseException> errors = handler.getErrors();
                if (errors.size() > 0) {
                    for (SAXParseException e : errors) {
                        System.err.printf("%s %s\n", file, e.getMessage());
                    }
                    System.exit(1);
                }
                break;
            }
            case "verificar": {
                String file = args[1];
                CFDI33 cfd = CFDIFactory.load33(new File(file));
                cfd.verificar();
                break;
            }
            case "sellar": {
                String file = args[1];
                CFDI33 cfd = CFDIFactory.load33(new File(file));

                PrivateKey key = KeyLoaderFactory.createInstance(
                        KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
                        new FileInputStream(args[2]),
                        args[3]
                ).getKey();

                X509Certificate cert = KeyLoaderFactory.createInstance(
                        KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                        new FileInputStream(args[4])
                ).getKey();

                cfd.sellar(key, cert);
                OutputStream out = new FileOutputStream(args[5]);
                cfd.guardar(out);
                break;
            }
            case "validar-timbrado": {
                String file = args[1];
                CFDI33 cfd = CFDIFactory.load33(new File(file));

                X509Certificate cert = KeyLoaderFactory.createInstance(
                        KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                        new FileInputStream(args[2])
                ).getKey();

                TFDv11c33 tfd = new TFDv11c33(cfd, cert, "", "");
                ValidationErrorHandler handler = new ValidationErrorHandler();
                tfd.validar(handler);
                List<SAXParseException> errors = handler.getErrors();
                if (errors.size() > 0) {
                    for (SAXParseException e : errors) {
                        System.err.printf("%s %s", file, e.getMessage());
                    }
                    System.exit(1);
                }
                break;
            }
            case "verificar-timbrado": {
                String file = args[1];
                CFDI33 cfd = CFDIFactory.load33(new File(file));

                X509Certificate cert = KeyLoaderFactory.createInstance(
                        KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                        new FileInputStream(args[2])
                ).getKey();

                TFDv11c33 tfd = new TFDv11c33(cfd, cert, "", "");
                int code = tfd.verificar();
                if (code != 600) {
                    throw new Exception("Timbrado invalido: " + code);
                }
                break;
            }
            case "timbrar": {
                String file = args[1];
                CFDI33 cfd = CFDIFactory.load33(new File(file));

                PrivateKey key = KeyLoaderFactory.createInstance(
                        KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
                        new FileInputStream(args[2]),
                        args[3]
                ).getKey();

                X509Certificate cert = KeyLoaderFactory.createInstance(
                        KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                        new FileInputStream(args[4])
                ).getKey();

                TFDv11c33 tfd = new TFDv11c33(cfd, cert, "", "");
                tfd.timbrar(key);
                OutputStream out = new FileOutputStream(args[5]);
                tfd.guardar(out);
                break;
            }
            default:
                System.err.println("No existe ese comando");
                System.exit(1);
        }
    }

}
