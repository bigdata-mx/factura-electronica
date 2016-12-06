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
package mx.bigdata.sat.cfd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import mx.bigdata.sat.cfd.examples.ExampleCFDFactory;
import mx.bigdata.sat.cfd.schema.Comprobante;

import mx.bigdata.sat.security.KeyLoader;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public final class CFDv2Test {
  
  private static PrivateKey key;

  private static X509Certificate cert;

    @BeforeClass
    public static void loadKeys() throws Exception {

        key = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
                "resources/certs/CSD01_AAA010101AAA.key",
                "12345678a"
        ).getKey();

        cert = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                "resources/certs/CSD01_AAA010101AAA.cer"
        ).getKey();
    }

  @Test public void testOriginalString() throws Exception {
    CFDv2 cfd = new CFDv2(ExampleCFDFactory.createComprobante());
    String cadena = "||2.0|ABCD|2|2010-05-03T14:11:36|49|2008|ingreso|UNA SOLA EXHIBICI\u00D3N|2000.00|0.00|2320.00|PAMC660606ER9|CONTRIBUYENTE PRUEBASEIS PATERNOSEIS MATERNOSEIS|PRUEBA SEIS|6|6|PUEBLA CENTRO|PUEBLA|PUEBLA|PUEBLA|M\u00C9XICO|72000|CAUR390312S87|ROSA MAR\u00CDA CALDER\u00D3N UIRIEGAS|TOPOCHICO|52|JARDINES DEL VALLE|NUEVO LEON|M\u00E9xico|95465|1.00|Servicio|01|Asesoria Fiscal y administrativa|2000.00|2000.00|IVA|16.00|320.00|320.00||";
    assertEquals(cadena, cfd.getCadenaOriginal());
  }
    
  @Test public void testSign() throws Exception {
    CFDv2 cfd = new CFDv2(ExampleCFDFactory.createComprobante());
    Comprobante sellado = cfd.sellarComprobante(key, cert);
    String signature = "acof1JHhLdpvSFkR8PiISu24VoNkMWGl2tj4jN/1eO8TW91btuqCz38IEPu2IwzCSvVKQIf47ZTk4zv9W/NCVVHj9EhwFALy1hS2Dxyf6yccf0r0ecIIdF2XgKvFvZwPkXouJJN2nFipSjy8MdaBk2y1tfTHgIaGmg1aqzwoqAM=";
    assertEquals(signature, sellado.getSello());
    String certificate = "MIIEYTCCA0mgAwIBAgIUMjAwMDEwMDAwMDAyMDAwMDE0MjgwDQYJKoZIhvcNAQEFBQAwggFcMRowGAYDVQQDDBFBLkMuIDIgZGUgcHJ1ZWJhczEvMC0GA1UECgwmU2VydmljaW8gZGUgQWRtaW5pc3RyYWNpw7NuIFRyaWJ1dGFyaWExODA2BgNVBAsML0FkbWluaXN0cmFjacOzbiBkZSBTZWd1cmlkYWQgZGUgbGEgSW5mb3JtYWNpw7NuMSkwJwYJKoZIhvcNAQkBFhphc2lzbmV0QHBydWViYXMuc2F0LmdvYi5teDEmMCQGA1UECQwdQXYuIEhpZGFsZ28gNzcsIENvbC4gR3VlcnJlcm8xDjAMBgNVBBEMBTA2MzAwMQswCQYDVQQGEwJNWDEZMBcGA1UECAwQRGlzdHJpdG8gRmVkZXJhbDESMBAGA1UEBwwJQ295b2Fjw6FuMTQwMgYJKoZIhvcNAQkCDCVSZXNwb25zYWJsZTogQXJhY2VsaSBHYW5kYXJhIEJhdXRpc3RhMB4XDTEzMDUwNzE2MDEyOVoXDTE3MDUwNzE2MDEyOVowgdsxKTAnBgNVBAMTIEFDQ0VNIFNFUlZJQ0lPUyBFTVBSRVNBUklBTEVTIFNDMSkwJwYDVQQpEyBBQ0NFTSBTRVJWSUNJT1MgRU1QUkVTQVJJQUxFUyBTQzEpMCcGA1UEChMgQUNDRU0gU0VSVklDSU9TIEVNUFJFU0FSSUFMRVMgU0MxJTAjBgNVBC0THEFBQTAxMDEwMUFBQSAvIEhFR1Q3NjEwMDM0UzIxHjAcBgNVBAUTFSAvIEhFR1Q3NjEwMDNNREZOU1IwODERMA8GA1UECxMIcHJvZHVjdG8wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAKS/beUVy6E3aODaNuLd2S3PXaQre0tGxmYTeUxa55x2t/7919ttgOpKF6hPF5KvlYh4ztqQqP4yEV+HjH7yy/2d/+e7t+J61jTrbdLqT3WD0+s5fCL6JOrF4hqy//EGdfvYftdGRNrZH+dAjWWml2S/hrN9aUxraS5qqO1b7btlAgMBAAGjHTAbMAwGA1UdEwEB/wQCMAAwCwYDVR0PBAQDAgbAMA0GCSqGSIb3DQEBBQUAA4IBAQACPXAWZX2DuKiZVv35RS1WFKgT2ubUO9C+byfZapV6ZzYNOiA4KmpkqHU/bkZHqKjR+R59hoYhVdn+ClUIliZf2ChHh8s0a0vBRNJ3IHfA1akWdzocYZLXjz3m0Er31BY+uS3qWUtPsONGVDyZL6IUBBUlFoecQhP9AO39er8zIbeU2b0MMBJxCt4vbDKFvT9i3V0Puoo+kmmkf15D2rBGR+drd8H8Yg8TDGFKf2zKmRsgT7nIeou6WpfYp570WIvLJQY+fsMp334D05Up5ykYSAxUGa30RdUzA4rxN5hT+W9whWVGD88TD33Nw55uNRUcRO3ZUVHmdWRG+GjhlfsD";
    assertEquals(certificate, sellado.getCertificado());
    String certificateNum = "20001000000200001428";
    assertEquals(certificateNum, sellado.getNoCertificado());
  }
  
  @Test public void testValidateVerify() throws Exception {
    CFDv2 cfd = new CFDv2(ExampleCFDFactory.createComprobante(2011));
    cfd.sellar(key, cert);
    cfd.validar();
    cfd.verificar();
  }

  @Test public void testValidateVerifyPrevoious() throws Exception {
    CFDv2 cfd = new CFDv2(ExampleCFDFactory.createComprobante(2010));
    cfd.sellar(key, cert);
    cfd.validar();
    cfd.verificar();
  }

  @Test public void testValidateVerifyWithFile() throws Exception {
    CFD2 cfd = CFD2Factory.load(new File("resources/xml/cfdv2.xml"));
    cfd.sellar(key, cert);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    cfd.guardar(baos);
    CFD2 cfd2 = new CFDv2(new ByteArrayInputStream(baos.toByteArray()));
    cfd2.validar();
    cfd2.verificar();
  }

  @Test public void testValidateVerifyWithExternalFile() throws Exception {
    CFD2 cfd = CFD2Factory.load(new File("resources/xml/cfdv2.externo.xml"));
    cfd.validar();
    cfd.verificar();
  }

  @Test public void testLoad() throws Exception {
    Comprobante c = CFDv2
      .newComprobante(new FileInputStream("resources/xml/cfdv2.externo.xml"));
    CFDv2 cfd = new CFDv2(c);
    cfd.validar();
    cfd.verificar();
  }

  @Test public void testSellarComprobante() throws Exception {
    Comprobante c = CFDv2
      .newComprobante(new FileInputStream("resources/xml/cfdv2.xml"));
    CFDv2 cfd = new CFDv2(c);
    Comprobante sellado = cfd.sellarComprobante(key, cert);
    assertNotNull(sellado.getSello());
    assertNotNull(sellado.getNoCertificado());
    assertNotNull(sellado.getCertificado());
  }
}
