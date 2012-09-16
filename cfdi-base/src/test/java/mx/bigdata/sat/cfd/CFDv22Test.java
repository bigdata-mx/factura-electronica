/*
 *  Copyright 2011 BigData.mx
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
package mx.bigdata.sat.cfd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import mx.bigdata.sat.cfd.examples.ExampleCFDv22Factory;
import mx.bigdata.sat.cfd.v22.schema.Comprobante;
import mx.bigdata.sat.security.KeyLoader;

import org.junit.BeforeClass;
import org.junit.Test;

public final class CFDv22Test {
  
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
    CFDv22 cfd = new CFDv22(ExampleCFDv22Factory.createComprobante());
    String cadena = "||2.2|ABCD|2|2012-05-03T14:11:36|49|2008|ingreso|UNA SOLA EXHIBICI\u00D3N|2000.00|0.00|2320.00|efectivo|Mexico|PAMC660606ER9|CONTRIBUYENTE PRUEBASEIS PATERNOSEIS MATERNOSEIS|PRUEBA SEIS|6|6|PUEBLA CENTRO|PUEBLA|PUEBLA|PUEBLA|M\u00C9XICO|72000|simplificado|CAUR390312S87|ROSA MAR\u00CDA CALDER\u00D3N UIRIEGAS|TOPOCHICO|52|JARDINES DEL VALLE|NUEVO LEON|M\u00E9xico|95465|1.00|Servicio|01|Asesoria Fiscal y administrativa|2000.00|2000.00|IVA|16.00|320.00|320.00||";
    assertEquals(cadena, cfd.getCadenaOriginal());
  }
    
  @Test public void testSign() throws Exception {
    CFDv22 cfd = new CFDv22(ExampleCFDv22Factory.createComprobante());
    Comprobante sellado = cfd.sellarComprobante(key, cert);
    String signature = "hb5J0NP2jFbMtcwt1ImGhuwbl2K48/9KSzfS/wlbSXaU8PAYre0xH74m4GD0gN0jfs8xAJrBFamq/xsEnNtaXYxcCR//U53ouWksXf6dwWuuD1N8h+PjelweS9P7vwDUY9mGCQlX5CHs1Sj4zWsGiCXK+gNumIqBZTeFAImT1jI=";
    assertEquals(signature, sellado.getSello());
    String certificate = "MIIEdDCCA1ygAwIBAgIUMjAwMDEwMDAwMDAxMDAwMDU4NjcwDQYJKoZIhvcNAQEFBQAwggFvMRgwFgYDVQQDDA9BLkMuIGRlIHBydWViYXMxLzAtBgNVBAoMJlNlcnZpY2lvIGRlIEFkbWluaXN0cmFjacOzbiBUcmlidXRhcmlhMTgwNgYDVQQLDC9BZG1pbmlzdHJhY2nDs24gZGUgU2VndXJpZGFkIGRlIGxhIEluZm9ybWFjacOzbjEpMCcGCSqGSIb3DQEJARYaYXNpc25ldEBwcnVlYmFzLnNhdC5nb2IubXgxJjAkBgNVBAkMHUF2LiBIaWRhbGdvIDc3LCBDb2wuIEd1ZXJyZXJvMQ4wDAYDVQQRDAUwNjMwMDELMAkGA1UEBhMCTVgxGTAXBgNVBAgMEERpc3RyaXRvIEZlZGVyYWwxEjAQBgNVBAcMCUNveW9hY8OhbjEVMBMGA1UELRMMU0FUOTcwNzAxTk4zMTIwMAYJKoZIhvcNAQkCDCNSZXNwb25zYWJsZTogSMOpY3RvciBPcm5lbGFzIEFyY2lnYTAeFw0xMjA3MjcxNzAyMDBaFw0xNjA3MjcxNzAyMDBaMIHbMSkwJwYDVQQDEyBBQ0NFTSBTRVJWSUNJT1MgRU1QUkVTQVJJQUxFUyBTQzEpMCcGA1UEKRMgQUNDRU0gU0VSVklDSU9TIEVNUFJFU0FSSUFMRVMgU0MxKTAnBgNVBAoTIEFDQ0VNIFNFUlZJQ0lPUyBFTVBSRVNBUklBTEVTIFNDMSUwIwYDVQQtExxBQUEwMTAxMDFBQUEgLyBIRUdUNzYxMDAzNFMyMR4wHAYDVQQFExUgLyBIRUdUNzYxMDAzTURGUk5OMDkxETAPBgNVBAsTCFVuaWRhZCAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2TTQSPONBOVxpXv9wLYo8jezBrb34i/tLx8jGdtyy27BcesOav2c1NS/Gdv10u9SkWtwdy34uRAVe7H0a3VMRLHAkvp2qMCHaZc4T8k47Jtb9wrOEh/XFS8LgT4y5OQYo6civfXXdlvxWU/gdM/e6I2lg6FGorP8H4GPAJ/qCNwIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQUFAAOCAQEATxMecTpMbdhSHo6KVUg4QVF4Op2IBhiMaOrtrXBdJgzGotUFcJgdBCMjtTZXSlq1S4DG1jr8p4NzQlzxsdTxaB8nSKJ4KEMgIT7E62xRUj15jI49qFz7f2uMttZLNThipunsN/NF1XtvESMTDwQFvas/Ugig6qwEfSZc0MDxMpKLEkEePmQwtZD+zXFSMVa6hmOu4M+FzGiRXbj4YJXn9Myjd8xbL/c+9UIcrYoZskxDvMxc6/6M3rNNDY3OFhBK+V/sPMzWWGt8S1yjmtPfXgFs1t65AZ2hcTwTAuHrKwDatJ1ZPfa482ZBROAAX1waz7WwXp0gso7sDCm2/yUVww==";
    assertEquals(certificate, sellado.getCertificado());
    String certificateNum = "20001000000100005867";
    assertEquals(certificateNum, sellado.getNoCertificado());
  }
  
  @Test public void testValidateVerify() throws Exception {
    CFDv22 cfd = new CFDv22(ExampleCFDv22Factory.createComprobante(2011));
    cfd.sellar(key, cert);
    cfd.validar();
    cfd.verificar();
  }

  @Test public void testValidateVerifyPrevious() throws Exception {
    CFDv22 cfd = new CFDv22(ExampleCFDv22Factory.createComprobante(2010));
    cfd.sellar(key, cert);
    cfd.validar();
    cfd.verificar();
  }

  @Test public void testValidateVerifyWithFile() throws Exception {
    CFD2 cfd = CFD2Factory.load(new File("resources/xml/cfdv22.xml"));
    cfd.sellar(key, cert);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    cfd.guardar(baos);
    CFD2 cfd2 = CFD2Factory.load(new ByteArrayInputStream(baos.toByteArray()));
    cfd2.validar();
    cfd2.verificar();
  }

//  @Test public void testValidateVerifyWithExternalFile() throws Exception {
//    CFDv22 cfd = 
//      new CFDv22(new FileInputStream("resources/xml/cfdv22.externo.xml"));
//    cfd.validar();
//    cfd.verificar();
//  }

//  @Test public void testLoad() throws Exception {
//    Comprobante c = CFDv22
//      .newComprobante(new FileInputStream("resources/xml/cfdv22.externo.xml"));
//    CFDv22 cfd = new CFDv22(c);
//    cfd.validar();
//    cfd.verificar();
//  }

  @Test public void testSellarComprobante() throws Exception {
    Comprobante c = CFDv22
      .newComprobante(new FileInputStream("resources/xml/cfdv2.xml"));
    CFDv22 cfd = new CFDv22(c);
    Comprobante sellado = cfd.sellarComprobante(key, cert);
    assertNotNull(sellado.getSello());
    assertNotNull(sellado.getNoCertificado());
    assertNotNull(sellado.getCertificado());
  }
}
