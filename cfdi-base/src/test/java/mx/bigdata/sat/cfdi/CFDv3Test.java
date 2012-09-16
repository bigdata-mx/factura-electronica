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
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import mx.bigdata.sat.cfdi.examples.ExampleCFDFactory;
import mx.bigdata.sat.cfdi.schema.Comprobante;
import mx.bigdata.sat.security.KeyLoader;

import org.junit.BeforeClass;
import org.junit.Test;

public final class CFDv3Test {
  
  private static PrivateKey key;

  private static X509Certificate cert;
  
  @BeforeClass public static void loadKeys() throws Exception {
    key = KeyLoader
      .loadPKCS8PrivateKey(new FileInputStream("resources/certs/aaa010101aaa__csd_01.key"),
                           "12345678a");
    cert = KeyLoader
      .loadX509Certificate(new FileInputStream("resources/certs/aaa010101aaa__csd_01.cer"));
  }
  
  @Test public void testOriginalString() throws Exception {
    CFDv3 cfd = new CFDv3(ExampleCFDFactory.createComprobante(), 
                          "mx.bigdata.sat.cfdi.examples");
    String cadena = "||3.0|2011-02-06T20:38:12|ingreso|PAGO EN UNA SOLA EXHIBICION|466.43|488.50|PPL961114GZ1|PHARMA PLUS SA DE CV|AV. RIO MIXCOAC|No. 140|ACACIAS|BENITO JUAREZ|DISTRITO FEDERAL|Mexico|03240|AV. UNIVERSIDAD|1858|OXTOPULCO|DISTRITO FEDERAL|Mexico|03910|PEPJ8001019Q8|JUAN PEREZ PEREZ|AV UNIVERSIDAD|16 EDF 3|DPTO 101|COPILCO UNIVERSIDAD|COYOACAN|DISTRITO FEDERAL|Mexico|04360|1.0|CAPSULAS|VIBRAMICINA 100MG 10|244.00|244.00|1.0|BOTELLA|CLORUTO 500M|137.93|137.93|1.0|TABLETAS|SEDEPRON 250MG 10|84.50|84.50|IVA|0.00|0.00|IVA|16.00|22.07||";
    assertEquals(cadena, cfd.getCadenaOriginal());
  }
    
  @Test public void testSign() throws Exception {
    CFDv3 cfd = new CFDv3(ExampleCFDFactory.createComprobante(), 
                          "mx.bigdata.sat.cfdi.examples");
    cfd.sellar(key, cert);
    String signature = "OhtxtV+CmBwBvkWl7v68M45rpLhw4gkYgF9VjnnVKVZudeCh5fbFT+REbUO5ZU5xA6WhuKbmdx5OKYppQRmxK7FBOiJQYuaN6AYdxB/ldIbXDIX8ouhNnOSyRxuNP0Bbb8lEmB+39Z8ID3oEqXaKFGlpGS+DJ7IqvIZEnGofJUc=";
    assertEquals(signature, cfd.getComprobante().getSello());
    String certificate = "MIIEdDCCA1ygAwIBAgIUMjAwMDEwMDAwMDAxMDAwMDU4NjcwDQYJKoZIhvcNAQEFBQAwggFvMRgwFgYDVQQDDA9BLkMuIGRlIHBydWViYXMxLzAtBgNVBAoMJlNlcnZpY2lvIGRlIEFkbWluaXN0cmFjacOzbiBUcmlidXRhcmlhMTgwNgYDVQQLDC9BZG1pbmlzdHJhY2nDs24gZGUgU2VndXJpZGFkIGRlIGxhIEluZm9ybWFjacOzbjEpMCcGCSqGSIb3DQEJARYaYXNpc25ldEBwcnVlYmFzLnNhdC5nb2IubXgxJjAkBgNVBAkMHUF2LiBIaWRhbGdvIDc3LCBDb2wuIEd1ZXJyZXJvMQ4wDAYDVQQRDAUwNjMwMDELMAkGA1UEBhMCTVgxGTAXBgNVBAgMEERpc3RyaXRvIEZlZGVyYWwxEjAQBgNVBAcMCUNveW9hY8OhbjEVMBMGA1UELRMMU0FUOTcwNzAxTk4zMTIwMAYJKoZIhvcNAQkCDCNSZXNwb25zYWJsZTogSMOpY3RvciBPcm5lbGFzIEFyY2lnYTAeFw0xMjA3MjcxNzAyMDBaFw0xNjA3MjcxNzAyMDBaMIHbMSkwJwYDVQQDEyBBQ0NFTSBTRVJWSUNJT1MgRU1QUkVTQVJJQUxFUyBTQzEpMCcGA1UEKRMgQUNDRU0gU0VSVklDSU9TIEVNUFJFU0FSSUFMRVMgU0MxKTAnBgNVBAoTIEFDQ0VNIFNFUlZJQ0lPUyBFTVBSRVNBUklBTEVTIFNDMSUwIwYDVQQtExxBQUEwMTAxMDFBQUEgLyBIRUdUNzYxMDAzNFMyMR4wHAYDVQQFExUgLyBIRUdUNzYxMDAzTURGUk5OMDkxETAPBgNVBAsTCFVuaWRhZCAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2TTQSPONBOVxpXv9wLYo8jezBrb34i/tLx8jGdtyy27BcesOav2c1NS/Gdv10u9SkWtwdy34uRAVe7H0a3VMRLHAkvp2qMCHaZc4T8k47Jtb9wrOEh/XFS8LgT4y5OQYo6civfXXdlvxWU/gdM/e6I2lg6FGorP8H4GPAJ/qCNwIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQUFAAOCAQEATxMecTpMbdhSHo6KVUg4QVF4Op2IBhiMaOrtrXBdJgzGotUFcJgdBCMjtTZXSlq1S4DG1jr8p4NzQlzxsdTxaB8nSKJ4KEMgIT7E62xRUj15jI49qFz7f2uMttZLNThipunsN/NF1XtvESMTDwQFvas/Ugig6qwEfSZc0MDxMpKLEkEePmQwtZD+zXFSMVa6hmOu4M+FzGiRXbj4YJXn9Myjd8xbL/c+9UIcrYoZskxDvMxc6/6M3rNNDY3OFhBK+V/sPMzWWGt8S1yjmtPfXgFs1t65AZ2hcTwTAuHrKwDatJ1ZPfa482ZBROAAX1waz7WwXp0gso7sDCm2/yUVww==";
    assertEquals(certificate, cfd.doGetComprobante().getCertificado());
    BigInteger bi = cert.getSerialNumber();
    String certificateNum = new String(bi.toByteArray());
    assertEquals(certificateNum, cfd.doGetComprobante().getNoCertificado());
  }
  
  @Test public void testValidateVerify() throws Exception {
    CFDv3 cfd = new CFDv3(ExampleCFDFactory.createComprobante(), 
                          "mx.bigdata.sat.cfdi.examples");
    cfd.sellar(key, cert);
    cfd.validar();
    cfd.verificar();
  }

  @Test public void testValidateVerifyWithFile() throws Exception {
    CFDI cfd = CFDIFactory.load(new File("resources/xml/cfdv3.xml"));
    cfd.sellar(key, cert);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    cfd.guardar(baos);
    CFDI cfd2 = CFDIFactory.load(new ByteArrayInputStream(baos.toByteArray()));
    cfd2.validar();
    cfd2.verificar();
  }

  @Test public void testValidateVerifyExternal() throws Exception {
    CFDI cfd = CFDIFactory.load(new File("resources/xml/cfdv3.externo.xml"));
    cfd.validar();
    cfd.verificar();
  }

  @Test public void testLoad() throws Exception {
    Comprobante c = CFDv3
      .newComprobante(new FileInputStream("resources/xml/cfdv3.externo.xml"));
    CFDI cfd = new CFDv3(c);
    cfd.validar();
    cfd.verificar();
  }

  @Test public void testSellarComprobante() throws Exception {
    Comprobante c = CFDv3
      .newComprobante(new FileInputStream("resources/xml/cfdv3.xml"));
    CFDv3 cfd = new CFDv3(c);
    Comprobante sellado = cfd.sellarComprobante(key, cert);
    assertNotNull(sellado.getSello());
    assertNotNull(sellado.getNoCertificado());
    assertNotNull(sellado.getCertificado());
  }
}
