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
      .loadPKCS8PrivateKey(new FileInputStream("resources/certs/emisor.key"),
                           "a0123456789");
    cert = KeyLoader
      .loadX509Certificate(new FileInputStream("resources/certs/emisor.cer"));
  }
  
  @Test public void testOriginalString() throws Exception {
    CFDv3 cfd = new CFDv3(ExampleCFDFactory.createComprobante());
    String cadena = "||3.0|2010-03-06T20:38:12|ingreso|PAGO EN UNA SOLA EXHIBICION|488.50|488.50|PPL961114GZ1|PHARMA PLUS SA DE CV|AV. RIO MIXCOAC|No. 140|ACACIAS|BENITO JUAREZ|MEXICO, D.F.|Mexico|03240|AV. UNIVERSIDAD|1858|OXTOPULCO|DISTRITO FEDERAL|Mexico|03910|PEPJ8001019Q8|JUAN PEREZ PEREZ|AV UNIVERSIDAD|16 EDF 3|DPTO 101|COPILCO UNIVERSIDAD|COYOACAN|DISTRITO FEDERAL|Mexico|04360|1.0|CAPSULAS|VIBRAMICINA 100MG 10|244.00|244.00|1.0|BOTELLA|CLORUTO 500M|137.93|137.93|1.0|TABLETAS|SEDEPRON 250MG 10|84.50|84.50|IVA|0.00|0.00|IVA|16.00|22.07||";
    assertEquals(cadena, cfd.getCadenaOriginal());
  }
    
  @Test public void testSign() throws Exception {
    CFDv3 cfd = new CFDv3(ExampleCFDFactory.createComprobante());
    cfd.sellar(key, cert);
    String signature = "QepNuYG/YJNY12CSs0J2FeNLZCf43wqxxWSSdB0BQZXLK99hpkCLyxRXaHgxBaxNRTinHuDeR83ArT1+YpUbNxtkzBkoVJ/6JvY1HgpbvBsoncDvT/8NaJTsYQYIvygrLPFnabF2uPkASrOsMmKN30cRF5/sHOjOjfUBuYN5mno=";
    assertEquals(signature, cfd.getComprobante().getSello());
    String certificate = "MIIE/TCCA+WgAwIBAgIUMzAwMDEwMDAwMDAxMDAwMDA4MDAwDQYJKoZIhvcNAQEFBQAwggFvMRgwFgYDVQQDDA9BLkMuIGRlIHBydWViYXMxLzAtBgNVBAoMJlNlcnZpY2lvIGRlIEFkbWluaXN0cmFjacOzbiBUcmlidXRhcmlhMTgwNgYDVQQLDC9BZG1pbmlzdHJhY2nDs24gZGUgU2VndXJpZGFkIGRlIGxhIEluZm9ybWFjacOzbjEpMCcGCSqGSIb3DQEJARYaYXNpc25ldEBwcnVlYmFzLnNhdC5nb2IubXgxJjAkBgNVBAkMHUF2LiBIaWRhbGdvIDc3LCBDb2wuIEd1ZXJyZXJvMQ4wDAYDVQQRDAUwNjMwMDELMAkGA1UEBhMCTVgxGTAXBgNVBAgMEERpc3RyaXRvIEZlZGVyYWwxEjAQBgNVBAcMCUNveW9hY8OhbjEVMBMGA1UELRMMU0FUOTcwNzAxTk4zMTIwMAYJKoZIhvcNAQkCDCNSZXNwb25zYWJsZTogSMOpY3RvciBPcm5lbGFzIEFyY2lnYTAeFw0xMDA3MzAxNjU4NDBaFw0xMjA3MjkxNjU4NDBaMIGWMRIwEAYDVQQDDAlNYXRyaXogU0ExEjAQBgNVBCkMCU1hdHJpeiBTQTESMBAGA1UECgwJTWF0cml6IFNBMSUwIwYDVQQtExxBQUEwMTAxMDFBQUEgLyBBQUFBMDEwMTAxQUFBMR4wHAYDVQQFExUgLyBBQUFBMDEwMTAxSERGUlhYMDExETAPBgNVBAsMCFVuaWRhZCAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDD0ltQNthUNUfzq0t1GpIyapjzOn1W5fGM5G/pQyMluCzP9YlVAgBjGgzwYp9Z0J9gadg3y2ZrYDwvv8b72goyRnhnv3bkjVRKlus6LDc00K7Jl23UYzNGlXn5+i0HxxuWonc2GYKFGsN4rFWKVy3Fnpv8Z2D7dNqsVyT5HapEqwIDAQABo4HqMIHnMAwGA1UdEwEB/wQCMAAwCwYDVR0PBAQDAgbAMB0GA1UdDgQWBBSYodSwRczzj5H7mcO3+mAyXz+y0DAuBgNVHR8EJzAlMCOgIaAfhh1odHRwOi8vcGtpLnNhdC5nb2IubXgvc2F0LmNybDAzBggrBgEFBQcBAQQnMCUwIwYIKwYBBQUHMAGGF2h0dHA6Ly9vY3NwLnNhdC5nb2IubXgvMB8GA1UdIwQYMBaAFOtZfQQimlONnnEaoFiWKfU54KDFMBAGA1UdIAQJMAcwBQYDKgMEMBMGA1UdJQQMMAoGCCsGAQUFBwMCMA0GCSqGSIb3DQEBBQUAA4IBAQArHQEorApwqumSn5EqDOAjbezi8fLco1cYES/PD+LQRM1Vb1g7VLE3hR4S5NNBv0bMwwWAr0WfL9lRRj0PMKLorO8y4TJjRU8MiYXfzSuKYL5Z16kW8zlVHw7CtmjhfjoIMwjQo3prifWxFv7VpfIBstKKShU0qB6KzUUNwg2Ola4t4gg2JJcBmyIAIInHSGoeinR2V1tQ10aRqJdXkGin4WZ75yMbQH4L0NfotqY6bpF2CqIY3aogQyJGhUJji4gYnS2DvHcyoICwgawshjSaX8Y0Xlwnuh6EusqhqlhTgwPNAPrKIXCmOWtqjlDhho/lhkHJMzuTn8AoVapbBUnj";
    assertEquals(certificate, cfd.getComprobante().getCertificado());
    BigInteger bi = cert.getSerialNumber();
    String certificateNum = new String(bi.toByteArray());
    assertEquals(certificateNum, cfd.getComprobante().getNoCertificado());
  }
  
  @Test public void testValidateVerify() throws Exception {
    CFDv3 cfd = new CFDv3(ExampleCFDFactory.createComprobante());
    cfd.sellar(key, cert);
    cfd.validar();
    cfd.verificar();
  }

  @Test public void testValidateVerifyWithFile() throws Exception {
    CFDv3 cfd = new CFDv3(new FileInputStream("resources/xml/cfdv3.xml"));
    cfd.sellar(key, cert);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    cfd.guardar(baos);
    CFDv3 cfd2 = new CFDv3(new ByteArrayInputStream(baos.toByteArray()));
    cfd2.validar();
    cfd2.verificar();
  }

  @Test public void testValidateVerifyExternal() throws Exception {
    CFDv3 cfd = 
      new CFDv3(new FileInputStream("resources/xml/cfdv3.externo.xml"));
    cfd.validar();
    cfd.verificar();
  }

  @Test public void testLoad() throws Exception {
    Comprobante c = CFDv3
      .newComprobante(new FileInputStream("resources/xml/cfdv3.externo.xml"));
    CFDv3 cfd = new CFDv3(c);
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
