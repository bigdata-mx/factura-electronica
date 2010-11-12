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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import org.w3c.dom.Element;
import org.xml.sax.helpers.DefaultHandler;

import mx.bigdata.sat.cfdi.CFDv3;
import mx.bigdata.sat.cfdi.TFDv1;
import mx.bigdata.sat.security.KeyLoader;

public final class CLI {

  public static void main(String[] args) throws Exception {
    String cmd = args[0];
    if (cmd.equals("validar")) {
      CFDv3 cfd = new CFDv3(new FileInputStream(args[1]));
      cfd.validar(new DefaultHandler());
    } else if (cmd.equals("verificar")) { 
      CFDv3 cfd = new CFDv3(new FileInputStream(args[1]));
      cfd.verificar();
    } else if (cmd.equals("sellar")) { 
      CFDv3 cfd = new CFDv3(new FileInputStream(args[1]));
      PrivateKey key = KeyLoader
        .loadPKCS8PrivateKey(new FileInputStream(args[2]), args[3]);
      X509Certificate cert = KeyLoader
        .loadX509Certificate(new FileInputStream(args[4]));
      cfd.sellar(key, cert);
      OutputStream out = new FileOutputStream(args[5]);
      cfd.guardar(out);
    } else if (cmd.equals("validar-timbrado")) {
      CFDv3 cfd = new CFDv3(new FileInputStream(args[1]));
      X509Certificate cert = KeyLoader
        .loadX509Certificate(new FileInputStream(args[2]));
      TFDv1 tfd = new TFDv1(cfd, cert);
      tfd.validar(new DefaultHandler());
    } else if (cmd.equals("verificar-timbrado")) { 
      CFDv3 cfd = new CFDv3(new FileInputStream(args[1]));
      X509Certificate cert = KeyLoader
        .loadX509Certificate(new FileInputStream(args[2]));
      TFDv1 tfd = new TFDv1(cfd, cert);
      int code = tfd.verificar();
      if (code != 600) {
        throw new Exception("Timbrado invalido: " +  code);
      }
    } else if (cmd.equals("timbrar")) { 
      CFDv3 cfd = new CFDv3(new FileInputStream(args[1]));
      PrivateKey key = KeyLoader
        .loadPKCS8PrivateKey(new FileInputStream(args[2]), args[3]);
      X509Certificate cert = KeyLoader
        .loadX509Certificate(new FileInputStream(args[4]));
      TFDv1 tfd = new TFDv1(cfd, cert);
      int code = tfd.timbrar(key);
      OutputStream out = new FileOutputStream(args[5]);
      tfd.guardar(out);
    } else {
      System.err.println("No existe ese comando");
      System.exit(1);
    }
  }
}