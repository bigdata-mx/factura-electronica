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
 */

package mx.bigdata.cfdi;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.cert.Certificate;

import javax.crypto.Cipher;
import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.bind.util.JAXBSource;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import mx.bigdata.cfdi.schema.Comprobante;
import mx.bigdata.cfdi.security.KeyLoader;

public final class CFDv3 {

  private static final String XSLT = "/xslt/cadenaoriginal_3_0.xslt";
  
  private static final String XSD = "/xsd/cfdv3.xsd";
      
  private static final JAXBContext CONTEXT = createContext();
  
  private static final JAXBContext createContext() {
    try {
      return JAXBContext.newInstance("mx.bigdata.cfdi.schema");
    } catch (Exception e) {
      throw new Error(e);
    } 
  }

  private final Comprobante document;

  public CFDv3(InputStream in) throws Exception {
    this.document = load(in);
  }

  public CFDv3(Comprobante comprobante) throws Exception {
    this.document = copy(comprobante);
  }

  public void validate() throws Exception {
    SchemaFactory sf =
      SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = sf.newSchema(getClass().getResource(XSD));
    Validator validator = schema.newValidator();
    validator.validate(new JAXBSource(CONTEXT, document));
  }

  public void verify() throws Exception {
    byte[] digest = getDigest();
    String certStr = document.getCertificado();
    Base64 b64 = new Base64();
    byte[] cbs = b64.decode(certStr);
    Certificate cert = KeyLoader
      .loadX509Certificate(new ByteArrayInputStream(cbs));  
    String sigStr = document.getSello();
    byte[] signature = b64.decode(sigStr);    
    Cipher dec = Cipher.getInstance("RSA");
    dec.init(Cipher.DECRYPT_MODE, cert);
    byte[] result = dec.doFinal(signature);
    verify(digest, result);
  }

  public byte[] getOriginalBytes() throws Exception {
    JAXBSource in = new JAXBSource(CONTEXT, document);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Result out = new StreamResult(baos);
    TransformerFactory factory = TransformerFactory.newInstance();
    Transformer transformer = factory
      .newTransformer(new StreamSource(getClass().getResourceAsStream(XSLT)));
    transformer.transform(in, out);
    return baos.toByteArray();
  }
  
  public String getOriginalString() throws Exception {
    byte[] bytes = getOriginalBytes();
    return new String(bytes);
  }
  
  public byte[] getDigest() throws Exception {
    byte[] bytes = getOriginalBytes();
    return DigestUtils.sha(bytes);
  }
  
  public String getSignature(Key key) throws Exception {
    byte[] digest = getDigest();
    Cipher enc = Cipher.getInstance("RSA");
    enc.init(Cipher.ENCRYPT_MODE, key);
    Base64 b64 = new Base64(-1);
    byte[] ciphered = enc.doFinal(digest);
    return b64.encodeToString(ciphered);
  }

  public void marshall(OutputStream out) throws Exception {
    Marshaller m = CONTEXT.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, 
                  "http://www.sat.gob.mx/cfd/3 cfdv3.xsd");
    m.marshal(document, out);
  }
  
  private void verify(byte[] bytes, byte[] sig) throws Exception {
    if (bytes.length != sig.length) {
      throw new Exception("Invalid signature");
    }
    for (int i = 0; i < bytes.length; i++) {
      if (bytes[i] != sig[i]) {
        throw new Exception("Invalid signature");
      }
    }
  }
    
  // Defensive deep-copy
  private Comprobante copy(Comprobante comprobante) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db = dbf.newDocumentBuilder(); 
    Document doc = db.newDocument();
    Marshaller m = CONTEXT.createMarshaller();
    m.marshal(comprobante, doc);
    Unmarshaller u = CONTEXT.createUnmarshaller();
    return (Comprobante) u.unmarshal(doc);
  }

  private Comprobante load(InputStream source) throws Exception {
    try {
      Unmarshaller u = CONTEXT.createUnmarshaller();
      return (Comprobante) u.unmarshal(source);
    } finally {
      source.close();
    }
  }

  public static void dump(String title, byte[] bytes, PrintStream out) {
    out.printf("%s : ", title);
    for (byte b : bytes) {
      out.printf("%02x:", b & 0xff);
    }
    out.println();
  }

}