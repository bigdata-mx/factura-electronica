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

package mx.bigdata.sat.cfd.examples;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import mx.bigdata.sat.cfd.CFDv2;
import mx.bigdata.sat.cfd.schema.ObjectFactory;
import mx.bigdata.sat.cfd.schema.Comprobante;
import mx.bigdata.sat.cfd.schema.Comprobante.Conceptos;
import mx.bigdata.sat.cfd.schema.Comprobante.Conceptos.Concepto;
import mx.bigdata.sat.cfd.schema.Comprobante.Emisor;
import mx.bigdata.sat.cfd.schema.Comprobante.Impuestos;
import mx.bigdata.sat.cfd.schema.Comprobante.Impuestos.Traslados;
import mx.bigdata.sat.cfd.schema.Comprobante.Impuestos.Traslados.Traslado;
import mx.bigdata.sat.cfd.schema.Comprobante.Receptor;
import mx.bigdata.sat.cfd.schema.TUbicacionFiscal;
import mx.bigdata.sat.cfd.schema.TUbicacion;
import mx.bigdata.sat.security.KeyLoader;

public final class Main {
    
  public static void main(String[] args) throws Exception {
    CFDv2 cfd = new CFDv2(ExampleCFDFactory.createComprobante());
    PrivateKey key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(args[0]),
                                            args[1]);
    X509Certificate cert = KeyLoader
      .loadX509Certificate(new FileInputStream(args[2]));
    cfd.sign(key, cert);
    cfd.validate();
    cfd.verify();
    cfd.marshal(System.err);
  }

}