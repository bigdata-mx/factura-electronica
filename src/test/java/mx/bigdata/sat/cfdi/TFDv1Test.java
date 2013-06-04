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

import mx.bigdata.sat.cfdi.examples.ExampleCFDFactory;

import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;
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

    key = KeyLoaderFactory.createInstance(
            KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
            new FileInputStream("resources/certs/aaa010101aaa__csd_01.key"),
            "12345678a"
    ).getKey();

    cert = KeyLoaderFactory.createInstance(
            KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
            new FileInputStream("resources/certs/aaa010101aaa__csd_01.cer")
    ).getKey();

    pacKey = KeyLoaderFactory.createInstance(
            KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
            new FileInputStream("resources/certs/aaa010101aaa__csd_02.key"),
            "12345678a"
    ).getKey();

    pacCert = KeyLoaderFactory.createInstance(
            KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
            new FileInputStream("resources/certs/aaa010101aaa__csd_02.cer")
    ).getKey();
  }

  @Before public void setupTFD() throws Exception {
    CFDv3 cfd = new CFDv3(ExampleCFDFactory.createComprobante(), 
                          "mx.bigdata.sat.cfdi.examples");
    cfd.sellar(key, cert);
    Date date = new GregorianCalendar(2011, 1, 7, 8, 51, 0).getTime();
    UUID uuid = UUID.fromString("843a05d7-207d-4adc-91e8-bda7175bcda3");
    tfd = new TFDv1(cfd, pacCert, uuid, date);  
  }
  
  @Test public void testOriginalString() throws Exception {
    String cadena = "||1.0|843a05d7-207d-4adc-91e8-bda7175bcda3|2011-02-07T08:51:00|OhtxtV+CmBwBvkWl7v68M45rpLhw4gkYgF9VjnnVKVZudeCh5fbFT+REbUO5ZU5xA6WhuKbmdx5OKYppQRmxK7FBOiJQYuaN6AYdxB/ldIbXDIX8ouhNnOSyRxuNP0Bbb8lEmB+39Z8ID3oEqXaKFGlpGS+DJ7IqvIZEnGofJUc=|20001000000100005868||";
    assertEquals(cadena, tfd.getCadenaOriginal());
  }
    
  @Test public void testStamp() throws Exception {
    tfd.timbrar(pacKey);
    String signature = "B2K9h9gcAdIoDYM6HwC8lx6+Hy0qmxeBsqT3CelIsrKrq89VGj0M/lR9cqz3y2hmlDq8UK7Sxh8OXngCnDT/QmS6+C0Xp+blQ9jFNfWOhOpUK9L+ddoG6yCDrGpqUWfBwAA5mOB/gwM0qztHorvtnmjPLPVvtdrO1MDkq713t4U=";
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
    CFDv3 cfd = new CFDv3(new FileInputStream("resources/xml/cfdv3_tfd.xml"));
    TFDv1 tfd = new TFDv1(cfd, pacCert);  
    tfd.timbrar(pacKey);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    tfd.guardar(baos);
    CFDv3 cfd2 = new CFDv3(new ByteArrayInputStream(baos.toByteArray()));
    TFDv1 tfd2 = new TFDv1(cfd2, pacCert);
    tfd2.validar();
    tfd2.verificar();
  }
}
