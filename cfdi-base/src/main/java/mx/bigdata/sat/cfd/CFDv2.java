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

package mx.bigdata.sat.cfd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import mx.bigdata.sat.cfd.schema.Comprobante;
import mx.bigdata.sat.common.CFD;
import mx.bigdata.sat.common.URIResolverImpl;
import mx.bigdata.sat.security.KeyLoader;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;

public final class CFDv2 implements CFD {

  private static final String XSLT = "/xslt/cadenaoriginal_2_0.xslt";
  
  private static final String XSD = "/xsd/v2/cfdv2.xsd";
      
  private static final JAXBContext CONTEXT = createContext();
  
  private static final JAXBContext createContext() {
    try {
      return JAXBContext.newInstance("mx.bigdata.sat.cfd.schema");
    } catch (Exception e) {
      throw new Error(e);
    } 
  }

  final Comprobante document;

  public CFDv2(InputStream in) throws Exception {
    this.document = load(in);
  }

  public CFDv2(Comprobante comprobante) throws Exception {
    this.document = copy(comprobante);
  }

  private TransformerFactory tf;

  public void setTransformerFactory(TransformerFactory tf) {
    this.tf = tf;   
    tf.setURIResolver(new URIResolverImpl()); 
  }


  public void sellar(PrivateKey key, X509Certificate cert) throws Exception {
    cert.checkValidity(); 
    String signature = getSignature(key);
    document.setSello(signature);
    byte[] bytes = cert.getEncoded();
    Base64 b64 = new Base64(-1);
    String certStr = b64.encodeToString(bytes);
    document.setCertificado(certStr);
    BigInteger bi = cert.getSerialNumber();
    document.setNoCertificado(new String(bi.toByteArray()));
  }
  
  public Comprobante sellarComprobante(PrivateKey key, X509Certificate cert) 
    throws Exception {
    sellar(key, cert);
    return getComprobante();
  }

  public void validar() throws Exception {
    validar(null);
  }

  public void validar(ErrorHandler handler) throws Exception {
    SchemaFactory sf =
      SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = sf.newSchema(getClass().getResource(XSD));
    Validator validator = schema.newValidator();
    if (handler != null) {
      validator.setErrorHandler(handler);
    }
    validator.validate(new JAXBSource(CONTEXT, document));
  }

  public void verificar() throws Exception {
    String certStr = document.getCertificado();
    Base64 b64 = new Base64();
    byte[] cbs = b64.decode(certStr);
    X509Certificate cert = KeyLoader
      .loadX509Certificate(new ByteArrayInputStream(cbs)); 
    verificar(cert);
  }

  public void verificar(Certificate cert) throws Exception {
    String sigStr = document.getSello();
    Base64 b64 = new Base64();
    byte[] signature = b64.decode(sigStr); 
    byte[] bytes = getOriginalBytes();
    Signature sig = Signature.getInstance("SHA1withRSA");
    sig.initVerify(cert);
    sig.update(bytes);
    boolean bool = sig.verify(signature);
    if (!bool) {
      throw new Exception("Invalid signature");
    }
  }

  public void guardar(OutputStream out) throws Exception {
    Marshaller m = CONTEXT.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, 
                  "http://www.sat.gob.mx/cfd "
                  + "http://www.sat.gob.mx/sitio_internet/cfd/2/cfdv2.xsd");
    m.marshal(document, out);
  }
  
  public String getCadenaOriginal() throws Exception {
    byte[] bytes = getOriginalBytes();
    return new String(bytes, "UTF8");
  }

  public static Comprobante newComprobante(InputStream in) throws Exception {
    return load(in);
  }

  byte[] getOriginalBytes() throws Exception {
    JAXBSource in = new JAXBSource(CONTEXT, document);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Result out = new StreamResult(baos);
    TransformerFactory factory = tf;
    if (factory == null) {
      factory = TransformerFactory.newInstance();
      factory.setURIResolver(new URIResolverImpl());
    }     
    Transformer transformer = factory
      .newTransformer(new StreamSource(getClass().getResourceAsStream(XSLT)));
    transformer.transform(in, out);
    return baos.toByteArray();
  }
  
  String getSignature(PrivateKey key) throws Exception {
    byte[] bytes = getOriginalBytes();
    Signature sig = Signature.getInstance("SHA1withRSA");
    sig.initSign(key);
    sig.update(bytes);
    byte[] signed = sig.sign();
    Base64 b64 = new Base64(-1);
    return b64.encodeToString(signed);
  }

  Comprobante getComprobante() throws Exception {
    return copy(document);
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

  private static Comprobante load(InputStream in) throws Exception {
    try {
      Unmarshaller u = CONTEXT.createUnmarshaller();
      return (Comprobante) u.unmarshal(in);
    } finally {
      in.close();
    }
  }
}