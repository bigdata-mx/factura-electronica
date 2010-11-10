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

package mx.bigdata.sat.cfdi.examples;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import mx.bigdata.sat.cfdi.CFDv3;
import mx.bigdata.sat.cfdi.TFDv1;
import mx.bigdata.sat.cfdi.schema.ObjectFactory;
import mx.bigdata.sat.security.KeyLoader;

public final class Main {
    
  public static void main(String[] args) throws Exception {
    CFDv3 cfd = new CFDv3(ExampleCFDFactory.createComprobante());
    PrivateKey key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(args[0]),
                                            args[1]);
    X509Certificate cert = KeyLoader
      .loadX509Certificate(new FileInputStream(args[2]));
    cfd.sign(key, cert);
    cfd.validate();
    cfd.verify();
    cfd.marshal(System.err);
    TFDv1 tfd = new TFDv1(cfd);
    PrivateKey ckey = KeyLoader
      .loadPKCS8PrivateKey(new FileInputStream(args[3]), args[1]);
    int code = tfd.stamp(ckey);
    Certificate ccert = KeyLoader
      .loadX509Certificate(new FileInputStream(args[4]));
    code = tfd.verify(ccert);
    tfd.marshal(System.err);
  }
}