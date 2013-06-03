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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import mx.bigdata.sat.cfdi.examples.ExampleCFDv32Factory;
import mx.bigdata.sat.security.KeyLoader;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public final class TFDv1c32Test {
  
  private static PrivateKey key;

  private static X509Certificate cert;
  
  private static PrivateKey pacKey;

  private static X509Certificate pacCert;

  private TFDv1c32 tfd;
   
  @BeforeClass public static void loadKeys() throws Exception {
    key = KeyLoader
      .loadPKCS8PrivateKey(new FileInputStream("resources/certs/aaa010101aaa__csd_01.key"),
                           "12345678a");
    cert = KeyLoader
      .loadX509Certificate(new FileInputStream("resources/certs/aaa010101aaa__csd_01.cer"));
    pacKey = KeyLoader
      .loadPKCS8PrivateKey(new FileInputStream("resources/certs/aaa010101aaa__csd_02.key"),
                           "12345678a");
    pacCert = KeyLoader
      .loadX509Certificate(new FileInputStream("resources/certs/aaa010101aaa__csd_02.cer"));
  }

  @Before public void setupTFD() throws Exception {
    CFDv32 cfd = new CFDv32(ExampleCFDv32Factory.createComprobante(), 
                          "mx.bigdata.sat.cfdi.examples");
    cfd.sellar(key, cert);
    Date date = new GregorianCalendar(2011, 1, 7, 8, 51, 0).getTime();
    UUID uuid = UUID.fromString("843a05d7-207d-4adc-91e8-bda7175bcda3");
    tfd = new TFDv1c32(cfd, pacCert, uuid, date);  
  }
  
  @Test public void testOriginalString() throws Exception {
    String cadena = "||1.0|843a05d7-207d-4adc-91e8-bda7175bcda3|2011-02-07T08:51:00|AMLMtSJp9zwUNRlYybXquVFS8TMIhHhPv3BhpkMHXL/cuYMlIPO41c4Ac5EOMzALNzUOcuvybuBdzSuWgUIo4EAvJql9CNJyKjl5ktS0bsimFYSquMYDxRNucp2hCBQEVEfKGbgZctaUNpkOlzzxd8B55RNWlL/f96pQWIVtCOc=|20001000000100005868||";
    assertEquals(cadena, tfd.getCadenaOriginal());
  }
    
  @Test public void testStamp() throws Exception {
    tfd.timbrar(pacKey);
    String signature = "F41FXg5+nJDxRh0L8twH4SLhppCqi3zU4Hx6nk8fAHsUKbd+Tjx92SCl2ija1aLCihGrWZKJW3MfGW/fhpX0kaXSB2EIlYROdB+M1JPCVYdrcXYplsPID6udJqrCuTvuYAe1/Sigh/KhPBuTgZw8buXaCuoB09tDybUlLroyjGM=";
    assertEquals(signature, tfd.getTimbre().getSelloSAT());    
    BigInteger bi = pacCert.getSerialNumber();
    String certificateNum = new String(bi.toByteArray());
    assertEquals(certificateNum, tfd.getTimbre().getNoCertificadoSAT());
  }
  
  @Test public void testValidateVerify() throws Exception {
    tfd.timbrar(pacKey);
    tfd.validar();
    tfd.verificar();
  }

  @Test public void testValidateVerifyWithFile() throws Exception {
    // CFDv32 cfd2 = new CFDv32(new FileInputStream("resources/xml/CFDI1.xml"));
    // TFDv1c32 tfd2 = new TFDv1c32(cfd2, pacCert);
    // tfd2.validar();
    // tfd2.verificar();
  }
}
