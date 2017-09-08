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
import mx.bigdata.sat.cfdi.examples.ExampleCFDv33Factory;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;

public final class CFDv33Test {

    private static PrivateKey key;

    private static X509Certificate cert;

    @BeforeClass
    public static void loadKeys() throws Exception {

        key = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
                new FileInputStream("resources/certs/CSD_Prueba_CFDI_LAN8507268IA.key"),
                "12345678a"
        ).getKey();

        cert = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                new FileInputStream("resources/certs/CSD_Prueba_CFDI_LAN8507268IA.cer")
        ).getKey();

    }

    @Test
    public void testOriginalString() throws Exception {
        CFDv33 cfd = new CFDv33(ExampleCFDv33Factory.createComprobante());
        String cadena = "||3.3|F|12345|2017-07-01T00:00:00|02|20001000000200001428|Crédito a 20 días|1550.00|100.00|MXN|1|1798|I|PUE|03240|aB1cD|06|a0452045-89cb-4792-9cc0-153f21faab7f|PPL961114GZ1|PHARMA PLUS SA DE CV|601|PEPJ8001019Q8|JUAN PEREZ PEREZ|MEX|ResidenteExtranjero1|G01|10101501|GEN01|1.0|EA|CAPSULAS|VIBRAMICINA 100MG 10|775.00|775.00|0.16|002|Tasa|0.160000|124.00|67 52 3924 8060097|123456|10101501|GEN02|1.0|EA|BOTELLA|CLORUTO 500M|775.00|775.00|0.16|002|Tasa|0.160000|124.00|67 52 3924 8060097|123456|0|002|Tasa|0.160000|248.00|248.00||";
        assertEquals(cadena, cfd.getCadenaOriginal());
    }

    @Test
    public void testSign() throws Exception {
        CFDv33 cfd = new CFDv33(ExampleCFDv33Factory.createComprobante());
        cfd.sellar(key, cert);
        String signature = "O7geP4zL9Bm3WvwbIsvuhYza+zbm6Sp6eAM3umZfGgYGqQWW8O6YwKZXlxgsGF6jUZ3zQhBJB0oQD5XoycMKToyjJl9Dz4Ao/bQGyuJUHcQ65A+VK1+gNrP5CAda7kz2VGIVhIA35GuBFn/rxiygMVdYluG554B/Fr1NZp/70GG5VDme7eFerHwljrl6KfpUHlF+ZkSAZRWIXvtu3F63FakZTBhfR3odTM2I2g8rk6V3W+Qew8vi/qPkV54gOc2rvkf0U/aIQPHoByBma8WxQNdUi/1Odu1P3aLO1O18JFWYz+ZKxbnuvbOKM3gLiqiadsL+i8wMFk4Jf2YcGV6WqA==";
        assertEquals(signature, cfd.getComprobante().getSello());
        String certificate = "MIIF0TCCA7mgAwIBAgIUMjAwMDEwMDAwMDAzMDAwMjI4MTYwDQYJKoZIhvcNAQELBQAwggFmMSAwHgYDVQQDDBdBLkMuIDIgZGUgcHJ1ZWJhcyg0MDk2KTEvMC0GA1UECgwmU2VydmljaW8gZGUgQWRtaW5pc3RyYWNpw7NuIFRyaWJ1dGFyaWExODA2BgNVBAsML0FkbWluaXN0cmFjacOzbiBkZSBTZWd1cmlkYWQgZGUgbGEgSW5mb3JtYWNpw7NuMSkwJwYJKoZIhvcNAQkBFhphc2lzbmV0QHBydWViYXMuc2F0LmdvYi5teDEmMCQGA1UECQwdQXYuIEhpZGFsZ28gNzcsIENvbC4gR3VlcnJlcm8xDjAMBgNVBBEMBTA2MzAwMQswCQYDVQQGEwJNWDEZMBcGA1UECAwQRGlzdHJpdG8gRmVkZXJhbDESMBAGA1UEBwwJQ295b2Fjw6FuMRUwEwYDVQQtEwxTQVQ5NzA3MDFOTjMxITAfBgkqhkiG9w0BCQIMElJlc3BvbnNhYmxlOiBBQ0RNQTAeFw0xNjEwMjUyMTU0MTlaFw0yMDEwMjUyMTU0MTlaMIG9MR4wHAYDVQQDExVNQiBJREVBUyBESUdJVEFMRVMgU0MxHjAcBgNVBCkTFU1CIElERUFTIERJR0lUQUxFUyBTQzEeMBwGA1UEChMVTUIgSURFQVMgRElHSVRBTEVTIFNDMSUwIwYDVQQtExxMQU44NTA3MjY4SUEgLyBGVUFCNzcwMTE3QlhBMR4wHAYDVQQFExUgLyBGVUFCNzcwMTE3TURGUk5OMDkxFDASBgNVBAsUC1BydWViYV9DRkRJMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjHr4KeoEx3BdkQP93AuN4fKo0rCZQsd9RJGBzQFvhmPJjGaVP81OUORM+lCRllxZxATZCAIFPOT3jl5wYgtolGYWWrt1HoAiuja1LKDGKrYgph0qWYKYeuew10fTyV+AeSbx1jTKz1PAAak06hx4M0rvmdiGO/Kg00/0wKz5/L3ZIMXEj+Hgr0IGh/yUIy8m5aKf+9jwuNttm/xDoeW3A8pxuidPU1Z1vliaZs75n89hC9LNwshhoaF3AvXIsgLDeuh9WoMGSm0HrilP9umFnm3nGUESiJa15Ep7LbG4CIhZrrknSm4fyrPk9KAigqLYMJhRsRwfp2qncAnAA+FuSQIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQsFAAOCAgEAd7t48tgawC9aczrGYt+4GFRcjj1LVKV3NElG+VH2s51KPkKPLj2Sw6OiEOGd+49spxHj1VR5MFvJo/pEJLY3EuLTifC9YZZYC8pHNDiA/eSvKqW5JNzp5/rgs3qAG1GrfdNGuSD3FkqhDdB6tJYqzTc12IC7xEAhKXrWZYCqa+zb9ogtzrUVL3vRRLMpnGEHK2yx8dhvG35qjHEfXyuoBsWILrVmnPpDCFO/CCLQB1OuMti1mlir6voBN0L1EbFK30w2bEuVihAeVLX8vVfMq4ZPI7UTLnblGnN11CCqiZkWhhehYrMdCjb5thMkEA+CMlIaFJYp7pNkLxQd4Y5+r8pTrdxxyvpA51DIWdoxvwaOiz1bzZk6ElVY2rfxwyZaJ17cJ1jmS4Yb5P4h8+5zkmZnPmRqfmaVO3nsApLWP6A38ZBrwwss429PJMSpfeXKGysPsqwF0yP3blsM7Cw53393LSHGKNm2GgG0kcrHnbbku6z6fjBdXMQQ5vjPuMNyw/pe3PzQLVoNOrD5AOoZmSG2TI3DtY4edLdiGmNQjo3MmAMMq4s7lr4AELPWAZRbnOlD1nEWGLdRp1mViteDvXwBL9E98EB4K9xK21DvgJ6rzw/D9rX6epeANfoXazWC0iCYcBNXiPikApcW73a/Jl/WjkEwEdkL/jLj0KCep58=";
        assertEquals(certificate, cfd.doGetComprobante().getCertificado());
        BigInteger bi = cert.getSerialNumber();
        String certificateNum = new String(bi.toByteArray());
        assertEquals(certificateNum, cfd.doGetComprobante().getNoCertificado());

    }

    @Test
    public void testValidateVerify() throws Exception {
        CFDv33 cfd = new CFDv33(ExampleCFDv33Factory.createComprobante());
        cfd.sellar(key, cert);
        cfd.validar();
        cfd.verificar();
    }

    @Test
    public void testValidateVerifyWithFile() throws Exception {
        CFDI33 cfd = CFDIFactory.load33(new File("resources/xml/cfdv33.xml"));
        cfd.sellar(key, cert);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cfd.guardar(baos);
        CFDI33 cfd2 = CFDIFactory.load33(new ByteArrayInputStream(baos.toByteArray()));
        cfd2.validar();
        cfd2.verificar();
    }

    @Test
    public void testSellarComprobante() throws Exception {
        Comprobante c = CFDv33.newComprobante(new FileInputStream("resources/xml/cfdv33.xml"));
        CFDv33 cfd = new CFDv33(c);
        Comprobante sellado = cfd.sellarComprobante(key, cert);
        assertNotNull(sellado.getSello());
        assertNotNull(sellado.getNoCertificado());
        assertNotNull(sellado.getCertificado());
    }

}
