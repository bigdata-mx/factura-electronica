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
 *
 */
package mx.bigdata.sat.cfdi;

import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import mx.bigdata.sat.cfdi.CFDv3;
import mx.bigdata.sat.cfdi.TFDv1;
import mx.bigdata.sat.cfdi.examples.ExampleCFDFactory;
import mx.bigdata.sat.cfdi.schema.Comprobante;
import mx.bigdata.sat.security.KeyLoader;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public final class TFDv1Test {
  
  private static PrivateKey key;

  private static X509Certificate cert;
  
  private static PrivateKey pacKey;

  private static X509Certificate pacCert;

  private TFDv1 tfd;
  
  @BeforeClass public static void loadKeys() throws Exception {
    key = KeyLoader
      .loadPKCS8PrivateKey(new FileInputStream("resources/certs/emisor.key"),
                           "a0123456789");
    cert = KeyLoader
      .loadX509Certificate(new FileInputStream("resources/certs/emisor.cer"));
    pacKey = KeyLoader
      .loadPKCS8PrivateKey(new FileInputStream("resources/certs/pac.key"),
                           "a0123456789");
    pacCert = KeyLoader
      .loadX509Certificate(new FileInputStream("resources/certs/pac.cer"));
  }

  @Before public void setupTFD() throws Exception {
    CFDv3 cfd = new CFDv3(ExampleCFDFactory.createComprobante());
    cfd.sellar(key, cert);
    Date date = new GregorianCalendar(2010, 10, 12, 8, 51, 00).getTime();
    UUID uuid = UUID.fromString("843a05d7-207d-4adc-91e8-bda7175bcda3");
    tfd = new TFDv1(cfd, pacCert, uuid, date);  
  }
  
  @Test public void testOriginalString() throws Exception {
    String cadena = "||1.0|843a05d7-207d-4adc-91e8-bda7175bcda3|2010-11-12T08:51:00|uo+1Boc1MWbT4so1/Rp1lG1xjVLJBMAMmV2Bkh4psNCRvGLQqi/22cD45XqTV9MWwmCU8nBSE7PJJp0xMSi0JML7HAXZollIBUAdzzSFhSP64+EGokW0bc+a8JVbNnkRFaxtTiScw+vOjDUzoA4ZUdV8qk9hBj3sLTPNosKxEpk=|30001000000100000801||";
    assertEquals(cadena, tfd.getCadenaOriginal());
  }
    
  @Test public void testStamp() throws Exception {
    tfd.timbrar(pacKey);
    String signature = "CHDar0kb7HhNvh9u1ldhzfCvrbaLQEMsY4LWL49WE5pqrVkp/vKpH07GKCBBDrLS2FtuCd3VUdFLEEqAZPdGByL5CNRFtneVMW8uwLkq3wey1hFkFqeNVizc3iFPV4BL6kwWMXkmAV3rsCM7QL5ZCy7ceepfjxy87rp0xgFUrr8=";
    assertEquals(signature, tfd.getTimbre().getSelloSAT());
    String certificateNum = "30001000000100000801";
    assertEquals(certificateNum, tfd.getTimbre().getNoCertificadoSAT());
  }
  
  @Test public void testValidateVerify() throws Exception {
    tfd.timbrar(pacKey);
    tfd.validar();
    tfd.verificar(pacCert);
  }

  @Test public void testValidateVerifyWithFile() throws Exception {
    CFDv3 cfd = new CFDv3(new FileInputStream("resources/xml/cfdv3_tfd.xml"));
    TFDv1 tfd = new TFDv1(cfd, pacCert);  
    tfd.timbrar(pacKey);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    tfd.guardar(baos);
    CFDv3 cfd2 = new CFDv3(new ByteArrayInputStream(baos.toByteArray()));
    TFDv1 tfd2 = new TFDv1(cfd2, pacCert);
    tfd2.validar();
    tfd2.verificar(pacCert);
  }
}
