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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public final class TFDv1Test {

    private static PrivateKey key;

    private static X509Certificate cert;

    private static PrivateKey pacKey;

    private static X509Certificate pacCert;

    private TFDv1 tfd;

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
        CFDv3 cfd = new CFDv3(ExampleCFDFactory.createComprobante(),
                "mx.bigdata.sat.cfdi.examples");
        cfd.sellar(key, cert);
        Date date = new GregorianCalendar(2011, 1, 7, 8, 51, 0).getTime();
        UUID uuid = UUID.fromString("843a05d7-207d-4adc-91e8-bda7175bcda3");
        tfd = new TFDv1(cfd, pacCert, uuid, date);
    }

    @Test
    public void testOriginalString() throws Exception {
        String cadena = "||1.0|843a05d7-207d-4adc-91e8-bda7175bcda3|2011-02-07T08:51:00|U0yTrfIKAllJtmASZQlgdzMeJLStjN+7nXyFHJ9Zk02LhT6BV4PD3qN2NZkuQQ27QFh8eluAxZ7BKY8qROyiIu6bi3h0ClZxgXdrHNxicDQVfQdo7EmqSVqj/teQcJdbZhjEmk4DABxTjPzk3vrktsj29DK/Fg1G/qYqCb5T+xY=|20001000000200001429||";
        assertEquals(cadena, tfd.getCadenaOriginal());
    }

    @Test
    public void testStamp() throws Exception {
        tfd.timbrar(pacKey);
        String signature = "gRWA9WrwQlxo7gRL+ROBL0R+Q5YKyGnG9bcd5Fjgwokh/bbDr5e6IWoFEw3SAwtUef9ePnxmEHxx3CVu16IL7r38f1qmIfa2dIGgeRNsGeux/IvFXkTJ+LPdQlClybnso1Dv3GqiLZDiRu1heATPP5TZgF0xLK7DeHkU6RJBIYQ=";
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
