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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import org.w3c.dom.Element;
import org.xml.sax.helpers.DefaultHandler;

import mx.bigdata.cfdi.CFDv3;
import mx.bigdata.cfdi.TFDv1;
import mx.bigdata.cfdi.CFDv3Debugger;
import mx.bigdata.cfdi.security.KeyLoader;

public final class CLI {

  public static void main(String[] args) throws Exception {
    String cmd = args[0];
    if (cmd.equals("valida")) {
      CFDv3 cfd = new CFDv3(new FileInputStream(args[1]));
      cfd.validate(new DefaultHandler());
    } else if (cmd.equals("verifica")) { 
      CFDv3 cfd = new CFDv3(new FileInputStream(args[1]));
      cfd.verify();
    } else if (cmd.equals("dump")) { 
      CFDv3Debugger cfd = 
        new CFDv3Debugger(new CFDv3(new FileInputStream(args[1])));
      cfd.dumpDigests();
    } else if (cmd.equals("firma")) { 
      CFDv3 cfd = new CFDv3(new FileInputStream(args[1]));
      PrivateKey key = KeyLoader
        .loadPKCS8PrivateKey(new FileInputStream(args[2]), args[3]);
      Certificate cert = KeyLoader
        .loadX509Certificate(new FileInputStream(args[4]));
      cfd.sign(key, cert);
      OutputStream out = new FileOutputStream(args[5]);
      cfd.marshal(out);
    } else if (cmd.equals("valida-timbrado")) {
      CFDv3 cfd = new CFDv3(new FileInputStream(args[1]));
      TFDv1 tfd = new TFDv1(cfd);
      tfd.validate(new DefaultHandler());
    } else if (cmd.equals("verifica-timbrado")) { 
      CFDv3 cfd = new CFDv3(new FileInputStream(args[1]));
      TFDv1 tfd = new TFDv1(cfd);
      Certificate cert = KeyLoader
        .loadX509Certificate(new FileInputStream(args[2]));
      int code = tfd.verify(cert);
      if (code != 600) {
        throw new Exception("Timbrado invalido: " +  code);
      }
    } else if (cmd.equals("timbrado")) { 
      CFDv3 cfd = new CFDv3(new FileInputStream(args[1]));
      TFDv1 tfd = new TFDv1(cfd);
      PrivateKey key = KeyLoader
        .loadPKCS8PrivateKey(new FileInputStream(args[2]), args[3]);
      int code = tfd.stamp(key);
      OutputStream out = new FileOutputStream(args[4]);
      tfd.marshal(out);
    } else {
      System.err.println("No existe ese comando");
    }
  }
}