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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import mx.bigdata.sat.cfdi.examples.ExampleCFDv32Factory;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;

public final class CFDv32Test {
  
  private static PrivateKey key;

  private static X509Certificate cert;
  
  @BeforeClass public static void loadKeys() throws Exception {

    key = KeyLoaderFactory.createInstance(
            KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
            new FileInputStream("resources/certs/CSD01_AAA010101AAA.key"),
            "12345678a"
    ).getKey();

    cert = KeyLoaderFactory.createInstance(
            KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
            new FileInputStream("resources/certs/CSD01_AAA010101AAA.cer")
    ).getKey();

  }
  
  @Test public void testOriginalString() throws Exception {
    CFDv32 cfd = new CFDv32(ExampleCFDv32Factory.createComprobante(), 
                          "mx.bigdata.sat.cfdi.examples");
    String cadena = "||3.2|2012-02-06T20:38:12|ingreso|PAGO EN UNA SOLA EXHIBICION|466.43|488.50|efectivo|Mexico|PPL961114GZ1|PHARMA PLUS SA DE CV|AV. RIO MIXCOAC|No. 140|ACACIAS|BENITO JUAREZ|DISTRITO FEDERAL|Mexico|03240|AV. UNIVERSIDAD|1858|OXTOPULCO|DISTRITO FEDERAL|Mexico|03910|simplificado|PEPJ8001019Q8|JUAN PEREZ PEREZ|AV UNIVERSIDAD|16 EDF 3|DPTO 101|COPILCO UNIVERSIDAD|COYOACAN|DISTRITO FEDERAL|Mexico|04360|1.0|CAPSULAS|VIBRAMICINA 100MG 10|244.00|244.00|1.0|BOTELLA|CLORUTO 500M|137.93|137.93|1.0|TABLETAS|SEDEPRON 250MG 10|84.50|84.50|IVA|0.00|0.00|IVA|16.00|22.07||";
    assertEquals(cadena, cfd.getCadenaOriginal());
  }
    
  @Test public void testSign() throws Exception {
    CFDv32 cfd = new CFDv32(ExampleCFDv32Factory.createComprobante(), 
                          "mx.bigdata.sat.cfdi.examples");
    cfd.sellar(key, cert);
    String signature = "RgcO+YOP97X9un4x+TDsJNoQPkfNg2/iyywDGpdkhN+n6grEJ4J6+eLSgQYK4MakykrlN1QB6CSf5H1M0NN4w1vQ5uFqPiopTRzgqg/e44cD/m6WoXANbi/3w1xG31BTNTgTXOsBkey7OzGF7c24rd7soDWZngkqrU6eEjE0DBw=";
    assertEquals(signature, cfd.getComprobante().getSello());
    String certificate = "MIIEYTCCA0mgAwIBAgIUMjAwMDEwMDAwMDAyMDAwMDE0MjgwDQYJKoZIhvcNAQEFBQAwggFcMRowGAYDVQQDDBFBLkMuIDIgZGUgcHJ1ZWJhczEvMC0GA1UECgwmU2VydmljaW8gZGUgQWRtaW5pc3RyYWNpw7NuIFRyaWJ1dGFyaWExODA2BgNVBAsML0FkbWluaXN0cmFjacOzbiBkZSBTZWd1cmlkYWQgZGUgbGEgSW5mb3JtYWNpw7NuMSkwJwYJKoZIhvcNAQkBFhphc2lzbmV0QHBydWViYXMuc2F0LmdvYi5teDEmMCQGA1UECQwdQXYuIEhpZGFsZ28gNzcsIENvbC4gR3VlcnJlcm8xDjAMBgNVBBEMBTA2MzAwMQswCQYDVQQGEwJNWDEZMBcGA1UECAwQRGlzdHJpdG8gRmVkZXJhbDESMBAGA1UEBwwJQ295b2Fjw6FuMTQwMgYJKoZIhvcNAQkCDCVSZXNwb25zYWJsZTogQXJhY2VsaSBHYW5kYXJhIEJhdXRpc3RhMB4XDTEzMDUwNzE2MDEyOVoXDTE3MDUwNzE2MDEyOVowgdsxKTAnBgNVBAMTIEFDQ0VNIFNFUlZJQ0lPUyBFTVBSRVNBUklBTEVTIFNDMSkwJwYDVQQpEyBBQ0NFTSBTRVJWSUNJT1MgRU1QUkVTQVJJQUxFUyBTQzEpMCcGA1UEChMgQUNDRU0gU0VSVklDSU9TIEVNUFJFU0FSSUFMRVMgU0MxJTAjBgNVBC0THEFBQTAxMDEwMUFBQSAvIEhFR1Q3NjEwMDM0UzIxHjAcBgNVBAUTFSAvIEhFR1Q3NjEwMDNNREZOU1IwODERMA8GA1UECxMIcHJvZHVjdG8wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAKS/beUVy6E3aODaNuLd2S3PXaQre0tGxmYTeUxa55x2t/7919ttgOpKF6hPF5KvlYh4ztqQqP4yEV+HjH7yy/2d/+e7t+J61jTrbdLqT3WD0+s5fCL6JOrF4hqy//EGdfvYftdGRNrZH+dAjWWml2S/hrN9aUxraS5qqO1b7btlAgMBAAGjHTAbMAwGA1UdEwEB/wQCMAAwCwYDVR0PBAQDAgbAMA0GCSqGSIb3DQEBBQUAA4IBAQACPXAWZX2DuKiZVv35RS1WFKgT2ubUO9C+byfZapV6ZzYNOiA4KmpkqHU/bkZHqKjR+R59hoYhVdn+ClUIliZf2ChHh8s0a0vBRNJ3IHfA1akWdzocYZLXjz3m0Er31BY+uS3qWUtPsONGVDyZL6IUBBUlFoecQhP9AO39er8zIbeU2b0MMBJxCt4vbDKFvT9i3V0Puoo+kmmkf15D2rBGR+drd8H8Yg8TDGFKf2zKmRsgT7nIeou6WpfYp570WIvLJQY+fsMp334D05Up5ykYSAxUGa30RdUzA4rxN5hT+W9whWVGD88TD33Nw55uNRUcRO3ZUVHmdWRG+GjhlfsD";
    assertEquals(certificate, cfd.doGetComprobante().getCertificado());
    BigInteger bi = cert.getSerialNumber();
    String certificateNum = new String(bi.toByteArray());
    assertEquals(certificateNum, cfd.doGetComprobante().getNoCertificado());
    
  }
  
  @Test public void testValidateVerify() throws Exception {
    CFDv32 cfd = new CFDv32(ExampleCFDv32Factory.createComprobante(), 
                          "mx.bigdata.sat.cfdi.examples");
    cfd.sellar(key, cert);
    cfd.validar();
    cfd.verificar();
  }

  @Test public void testValidateVerifyWithFile() throws Exception {
    CFDI cfd = CFDIFactory.load(new File("resources/xml/cfdv32.xml"));
    cfd.sellar(key, cert);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    cfd.guardar(baos);
    CFDI cfd2 = CFDIFactory.load(new ByteArrayInputStream(baos.toByteArray()));
    cfd2.validar();
    cfd2.verificar();
  }

//  @Test public void testValidateVerifyExternal() throws Exception {
//    CFDv32 cfd = 
//      new CFDv32(new FileInputStream("resources/xml/cfdv32.externo.xml"));
//    cfd.validar();
//    cfd.verificar();
//  }
//
//  @Test public void testLoad() throws Exception {
//    Comprobante c = CFDv32
//      .newComprobante(new FileInputStream("resources/xml/cfdv32.externo.xml"));
//    CFDv32 cfd = new CFDv32(c);
//    cfd.validar();
//    cfd.verificar();
//  }

  @Test public void testSellarComprobante() throws Exception {
    Comprobante c = CFDv32
      .newComprobante(new FileInputStream("resources/xml/cfdv3.xml"));
    CFDv32 cfd = new CFDv32(c);
    Comprobante sellado = cfd.sellarComprobante(key, cert);
    assertNotNull(sellado.getSello());
    assertNotNull(sellado.getNoCertificado());
    assertNotNull(sellado.getCertificado());
  }
}
