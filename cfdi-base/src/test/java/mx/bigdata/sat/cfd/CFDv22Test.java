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
      .loadPKCS8PrivateKey(new FileInputStream("resources/certs/emisor.key"),
                           "a0123456789");
    cert = KeyLoader
      .loadX509Certificate(new FileInputStream("resources/certs/emisor.cer"));
  }
  
  @Test public void testOriginalString() throws Exception {
    CFDv22 cfd = new CFDv22(ExampleCFDv22Factory.createComprobante());
    String cadena = "||2.2|ABCD|2|2012-05-03T14:11:36|49|2008|ingreso|UNA SOLA EXHIBICI\u00D3N|2000.00|0.00|2320.00|efectivo|Mexico|PAMC660606ER9|CONTRIBUYENTE PRUEBASEIS PATERNOSEIS MATERNOSEIS|PRUEBA SEIS|6|6|PUEBLA CENTRO|PUEBLA|PUEBLA|PUEBLA|M\u00C9XICO|72000|simplificado|CAUR390312S87|ROSA MAR\u00CDA CALDER\u00D3N UIRIEGAS|TOPOCHICO|52|JARDINES DEL VALLE|NUEVO LEON|M\u00E9xico|95465|1.00|Servicio|01|Asesoria Fiscal y administrativa|2000.00|2000.00|IVA|16.00|320.00|320.00||";
    assertEquals(cadena, cfd.getCadenaOriginal());
  }
    
  @Test public void testSign() throws Exception {
    CFDv22 cfd = new CFDv22(ExampleCFDv22Factory.createComprobante());
    Comprobante sellado = cfd.sellarComprobante(key, cert);
    String signature = "JWl0g/ZBoWjAFGrjZw+Pg+uj3JI3+a1UVFvXzoaH7VR/0xpn4stpFcmJuZk3ZzkUVM9zcsl5BOFTmRPUF57JZEEmTveDIt2ZSVbGEXX0lj+x69V5mT9devD7mTvJdl3Nw1A0le+2ciaThOLG2D9JKP46xsYmfQNUNBEGf4/K5QA=";
    assertEquals(signature, sellado.getSello());
    String certificate = "MIIE/TCCA+WgAwIBAgIUMzAwMDEwMDAwMDAxMDAwMDA4MDAwDQYJKoZIhvcNAQEFBQAwggFvMRgwFgYDVQQDDA9BLkMuIGRlIHBydWViYXMxLzAtBgNVBAoMJlNlcnZpY2lvIGRlIEFkbWluaXN0cmFjacOzbiBUcmlidXRhcmlhMTgwNgYDVQQLDC9BZG1pbmlzdHJhY2nDs24gZGUgU2VndXJpZGFkIGRlIGxhIEluZm9ybWFjacOzbjEpMCcGCSqGSIb3DQEJARYaYXNpc25ldEBwcnVlYmFzLnNhdC5nb2IubXgxJjAkBgNVBAkMHUF2LiBIaWRhbGdvIDc3LCBDb2wuIEd1ZXJyZXJvMQ4wDAYDVQQRDAUwNjMwMDELMAkGA1UEBhMCTVgxGTAXBgNVBAgMEERpc3RyaXRvIEZlZGVyYWwxEjAQBgNVBAcMCUNveW9hY8OhbjEVMBMGA1UELRMMU0FUOTcwNzAxTk4zMTIwMAYJKoZIhvcNAQkCDCNSZXNwb25zYWJsZTogSMOpY3RvciBPcm5lbGFzIEFyY2lnYTAeFw0xMDA3MzAxNjU4NDBaFw0xMjA3MjkxNjU4NDBaMIGWMRIwEAYDVQQDDAlNYXRyaXogU0ExEjAQBgNVBCkMCU1hdHJpeiBTQTESMBAGA1UECgwJTWF0cml6IFNBMSUwIwYDVQQtExxBQUEwMTAxMDFBQUEgLyBBQUFBMDEwMTAxQUFBMR4wHAYDVQQFExUgLyBBQUFBMDEwMTAxSERGUlhYMDExETAPBgNVBAsMCFVuaWRhZCAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDD0ltQNthUNUfzq0t1GpIyapjzOn1W5fGM5G/pQyMluCzP9YlVAgBjGgzwYp9Z0J9gadg3y2ZrYDwvv8b72goyRnhnv3bkjVRKlus6LDc00K7Jl23UYzNGlXn5+i0HxxuWonc2GYKFGsN4rFWKVy3Fnpv8Z2D7dNqsVyT5HapEqwIDAQABo4HqMIHnMAwGA1UdEwEB/wQCMAAwCwYDVR0PBAQDAgbAMB0GA1UdDgQWBBSYodSwRczzj5H7mcO3+mAyXz+y0DAuBgNVHR8EJzAlMCOgIaAfhh1odHRwOi8vcGtpLnNhdC5nb2IubXgvc2F0LmNybDAzBggrBgEFBQcBAQQnMCUwIwYIKwYBBQUHMAGGF2h0dHA6Ly9vY3NwLnNhdC5nb2IubXgvMB8GA1UdIwQYMBaAFOtZfQQimlONnnEaoFiWKfU54KDFMBAGA1UdIAQJMAcwBQYDKgMEMBMGA1UdJQQMMAoGCCsGAQUFBwMCMA0GCSqGSIb3DQEBBQUAA4IBAQArHQEorApwqumSn5EqDOAjbezi8fLco1cYES/PD+LQRM1Vb1g7VLE3hR4S5NNBv0bMwwWAr0WfL9lRRj0PMKLorO8y4TJjRU8MiYXfzSuKYL5Z16kW8zlVHw7CtmjhfjoIMwjQo3prifWxFv7VpfIBstKKShU0qB6KzUUNwg2Ola4t4gg2JJcBmyIAIInHSGoeinR2V1tQ10aRqJdXkGin4WZ75yMbQH4L0NfotqY6bpF2CqIY3aogQyJGhUJji4gYnS2DvHcyoICwgawshjSaX8Y0Xlwnuh6EusqhqlhTgwPNAPrKIXCmOWtqjlDhho/lhkHJMzuTn8AoVapbBUnj";
    assertEquals(certificate, sellado.getCertificado());
    String certificateNum = "30001000000100000800";
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
    CFDv22 cfd = new CFDv22(new FileInputStream("resources/xml/cfdv22.xml"));
    cfd.sellar(key, cert);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    cfd.guardar(baos);
    CFDv22 cfd2 = new CFDv22(new ByteArrayInputStream(baos.toByteArray()));
    cfd2.validar();
    cfd2.verificar();
  }

//  @Test public void testValidateVerifyWithExternalFile() throws Exception {
//    CFDv22 cfd = 
//      new CFDv22(new FileInputStream("resources/xml/cfdv2.externo.xml"));
//    cfd.validar();
//    cfd.verificar();
//  }

//  @Test public void testLoad() throws Exception {
//    Comprobante c = CFDv22
//      .newComprobante(new FileInputStream("resources/xml/cfdv2.externo.xml"));
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
