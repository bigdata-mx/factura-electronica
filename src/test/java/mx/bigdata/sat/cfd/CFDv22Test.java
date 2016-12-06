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

import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public final class CFDv22Test {
  
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
    CFDv22 cfd = new CFDv22(ExampleCFDv22Factory.createComprobante());
    String cadena = "||2.2|ABCD|2|2012-05-03T14:11:36|49|2008|ingreso|UNA SOLA EXHIBICI\u00D3N|2000.00|0.00|2320.00|efectivo|Mexico|PAMC660606ER9|CONTRIBUYENTE PRUEBASEIS PATERNOSEIS MATERNOSEIS|PRUEBA SEIS|6|6|PUEBLA CENTRO|PUEBLA|PUEBLA|PUEBLA|M\u00C9XICO|72000|simplificado|CAUR390312S87|ROSA MAR\u00CDA CALDER\u00D3N UIRIEGAS|TOPOCHICO|52|JARDINES DEL VALLE|NUEVO LEON|M\u00E9xico|95465|1.00|Servicio|01|Asesoria Fiscal y administrativa|2000.00|2000.00|IVA|16.00|320.00|320.00||";
    assertEquals(cadena, cfd.getCadenaOriginal());
  }
    
  @Test public void testSign() throws Exception {
    CFDv22 cfd = new CFDv22(ExampleCFDv22Factory.createComprobante());
    Comprobante sellado = cfd.sellarComprobante(key, cert);
    String signature = "NNdxON09nIBCj/rOzofILlbBgaru7g9xx0Bag//vlF3akKJROPggCefI49F2PCA+FZ5uM8dE+qrSHDmivKkQhHHliJvZ2KOdHVQpOlZMHB2otNdpSKLpY6S+xhVl8kZ1DSqz5Bi040RxxQ4OjblL6/wGF9F95tD8mYs+sAUgUb0=";
    assertEquals(signature, sellado.getSello());
    String certificate = "MIIEYTCCA0mgAwIBAgIUMjAwMDEwMDAwMDAyMDAwMDE0MjgwDQYJKoZIhvcNAQEFBQAwggFcMRowGAYDVQQDDBFBLkMuIDIgZGUgcHJ1ZWJhczEvMC0GA1UECgwmU2VydmljaW8gZGUgQWRtaW5pc3RyYWNpw7NuIFRyaWJ1dGFyaWExODA2BgNVBAsML0FkbWluaXN0cmFjacOzbiBkZSBTZWd1cmlkYWQgZGUgbGEgSW5mb3JtYWNpw7NuMSkwJwYJKoZIhvcNAQkBFhphc2lzbmV0QHBydWViYXMuc2F0LmdvYi5teDEmMCQGA1UECQwdQXYuIEhpZGFsZ28gNzcsIENvbC4gR3VlcnJlcm8xDjAMBgNVBBEMBTA2MzAwMQswCQYDVQQGEwJNWDEZMBcGA1UECAwQRGlzdHJpdG8gRmVkZXJhbDESMBAGA1UEBwwJQ295b2Fjw6FuMTQwMgYJKoZIhvcNAQkCDCVSZXNwb25zYWJsZTogQXJhY2VsaSBHYW5kYXJhIEJhdXRpc3RhMB4XDTEzMDUwNzE2MDEyOVoXDTE3MDUwNzE2MDEyOVowgdsxKTAnBgNVBAMTIEFDQ0VNIFNFUlZJQ0lPUyBFTVBSRVNBUklBTEVTIFNDMSkwJwYDVQQpEyBBQ0NFTSBTRVJWSUNJT1MgRU1QUkVTQVJJQUxFUyBTQzEpMCcGA1UEChMgQUNDRU0gU0VSVklDSU9TIEVNUFJFU0FSSUFMRVMgU0MxJTAjBgNVBC0THEFBQTAxMDEwMUFBQSAvIEhFR1Q3NjEwMDM0UzIxHjAcBgNVBAUTFSAvIEhFR1Q3NjEwMDNNREZOU1IwODERMA8GA1UECxMIcHJvZHVjdG8wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAKS/beUVy6E3aODaNuLd2S3PXaQre0tGxmYTeUxa55x2t/7919ttgOpKF6hPF5KvlYh4ztqQqP4yEV+HjH7yy/2d/+e7t+J61jTrbdLqT3WD0+s5fCL6JOrF4hqy//EGdfvYftdGRNrZH+dAjWWml2S/hrN9aUxraS5qqO1b7btlAgMBAAGjHTAbMAwGA1UdEwEB/wQCMAAwCwYDVR0PBAQDAgbAMA0GCSqGSIb3DQEBBQUAA4IBAQACPXAWZX2DuKiZVv35RS1WFKgT2ubUO9C+byfZapV6ZzYNOiA4KmpkqHU/bkZHqKjR+R59hoYhVdn+ClUIliZf2ChHh8s0a0vBRNJ3IHfA1akWdzocYZLXjz3m0Er31BY+uS3qWUtPsONGVDyZL6IUBBUlFoecQhP9AO39er8zIbeU2b0MMBJxCt4vbDKFvT9i3V0Puoo+kmmkf15D2rBGR+drd8H8Yg8TDGFKf2zKmRsgT7nIeou6WpfYp570WIvLJQY+fsMp334D05Up5ykYSAxUGa30RdUzA4rxN5hT+W9whWVGD88TD33Nw55uNRUcRO3ZUVHmdWRG+GjhlfsD";
    assertEquals(certificate, sellado.getCertificado());
    String certificateNum = "20001000000200001428";
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
