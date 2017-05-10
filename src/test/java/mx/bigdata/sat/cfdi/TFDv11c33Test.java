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

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import mx.bigdata.sat.cfdi.examples.ExampleCFDv33Factory;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public final class TFDv11c33Test {

    private static PrivateKey key;

    private static X509Certificate cert;

    private static PrivateKey pacKey;

    private static X509Certificate pacCert;

    private TFDv11c33 tfd;

    @BeforeClass
    public static void loadKeys() throws Exception {
        key = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
                new FileInputStream("resources/certs/CSD01_AAA010101AAA.key"),
                "12345678a"
        ).getKey();

        cert = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                new FileInputStream("resources/certs/CSD01_AAA010101AAA.cer")
        ).getKey();

        pacKey = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
                new FileInputStream("resources/certs/CSD02_AAA010101AAA.key"),
                "12345678a"
        ).getKey();

        pacCert = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                new FileInputStream("resources/certs/CSD02_AAA010101AAA.cer")
        ).getKey();
    }

    @Before
    public void setupTFD() throws Exception {
        CFDv33 cfd = new CFDv33(ExampleCFDv33Factory.createComprobante());
        cfd.sellar(key, cert);
        Date date = new GregorianCalendar(2017, 6, 7, 8, 51, 0).getTime();
        UUID uuid = UUID.fromString("843a05d7-207d-4adc-91e8-bda7175bcda3");
        String PAC = "FLI081010EK2";
        String leyenda = "Leyenda opciones del Pac 01";
        tfd = new TFDv11c33(cfd, pacCert, uuid, date, PAC, leyenda);
    }

    @Test
    public void testOriginalString() throws Exception {
        String cadena = "||1.1|843a05d7-207d-4adc-91e8-bda7175bcda3|2017-06-07T08:51:00|FLI081010EK2|Leyenda opciones del Pac 01|lv5UVJIRZEW3pmyFxjo4S01iyoE72OrjP6/SThdOK8/Gkfla/QRdtEPa3JCK/HDh7dhg466Hu58L4WsAng0q3Q/4vfl2rdUWAN3uqQMQvYEycgrFCgLF3pR0+eYGKZmI7GDeGGNcrFAbkkVA8xR/ZTk9O1vsFHP2aubur1dQo4M=|20001000000200001429||";
        assertEquals(cadena, tfd.getCadenaOriginal());
    }

    @Test
    public void testStamp() throws Exception {
        tfd.timbrar(pacKey);
        String signature = "CeytnQFeFvAeChPYHC4ny/LKgSed4aYSrWMmQWl2nIrBgDw3qCcyhap4eE1I5jdVx6r52UYioE2Wd0+CUotaRpKBInf6AOEoLP46+DLFJmK+6gKxU/1yOLvL+2OTr9pYdGgACJW6FiCswOzK1Z9HmDRSZn9hnSa2n6PjDiIwTxI=";
        assertEquals(signature, tfd.getTimbre().getSelloSAT());
        BigInteger bi = pacCert.getSerialNumber();
        String certificateNum = new String(bi.toByteArray());
        assertEquals(certificateNum, tfd.getTimbre().getNoCertificadoSAT());
    }

    @Test
    public void testValidateVerify() throws Exception {
        tfd.timbrar(pacKey);
        tfd.validar();
        tfd.verificar();
    }

    @Test
    public void testValidateVerifyWithFile() throws Exception {
        // CFDv32 cfd2 = new CFDv32(new FileInputStream("resources/xml/CFDI1.xml"));
        // TFDv1c32 tfd2 = new TFDv1c32(cfd2, pacCert);
        // tfd2.validar();
        // tfd2.verificar();
    }
}
