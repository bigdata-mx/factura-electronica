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
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
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
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import mx.bigdata.cfdi.schema.Comprobante;
import mx.bigdata.cfdi.schema.Comprobante.Complemento;
import mx.bigdata.cfdi.schema.ObjectFactory;
import mx.bigdata.cfdi.schema.TimbreFiscalDigital;
import mx.bigdata.cfdi.security.KeyLoader;

public final class TFDv1 {

  private static final String XSLT = "/xslt/cadenaoriginal_TFD_1_0.xslt";
  
  private static final String XSD = "/xsd/TimbreFiscalDigital.xsd";
      
  private static final JAXBContext CONTEXT = createContext();
  
  private static final JAXBContext createContext() {
    try {
      return JAXBContext.newInstance("mx.bigdata.cfdi.schema");
    } catch (Exception e) {
      throw new Error(e);
    } 
  }

  private final Comprobante document;

  private final String cfdSignature;

  private TransformerFactory tf;

  private TimbreFiscalDigital tfd;

  public TFDv1(CFDv3 cfd) throws Exception {
    this.document = cfd.getComprobante();
    this.tfd = getTimbreFiscalDigital(document); 
    this.cfdSignature = document.getSello();
  }

  public void setTransformerFactory(TransformerFactory tf) {
    this.tf = tf;    
  }

  public int stamp(PrivateKey key) throws Exception {
    if (tfd != null) {
      return 304;
    }
    tfd = createStamp();
    String signature = getSignature(key);
    tfd.setSelloSAT(signature);
    stampTFD(); 
    return 300;
  }

  public void validate() throws Exception {
    validate(null);
  }

  public void validate(ErrorHandler handler) throws Exception {
    SchemaFactory sf =
      SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = sf.newSchema(getClass().getResource(XSD));
    Validator validator = schema.newValidator();
    if (handler != null) {
      validator.setErrorHandler(handler);
    }
    validator.validate(new JAXBSource(CONTEXT, tfd));
  }

  public int verify(Certificate cert) throws Exception {
    if (tfd == null) {
      return 601; //No contiene timbrado
    }
    Base64 b64 = new Base64();
    String sigStr = tfd.getSelloSAT();
    byte[] signature = b64.decode(sigStr); 
    byte[] bytes = getOriginalBytes();
    Signature sig = Signature.getInstance("SHA1withRSA");
    sig.initVerify(cert);
    sig.update(bytes);
    boolean verified = sig.verify(signature);   
    return verified ? 600 : 602; //Sello del timbrado no valido
  }

  byte[] getOriginalBytes() throws Exception {
    JAXBSource in = new JAXBSource(CONTEXT, tfd);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Result out = new StreamResult(baos);
    TransformerFactory factory = tf;
    if (factory == null) {
      factory = TransformerFactory.newInstance();
    } 
    Transformer transformer = factory
      .newTransformer(new StreamSource(getClass().getResourceAsStream(XSLT)));
    transformer.transform(in, out);
    return baos.toByteArray();
  }
  
  String getOriginalString() throws Exception {
    byte[] bytes = getOriginalBytes();
    return new String(bytes);
  }
  
  byte[] getDigest() throws Exception {
    byte[] bytes = getOriginalBytes();
    return DigestUtils.sha(bytes);
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

  private void stampTFD() throws Exception {
    Element element = marshalTFD();
    ObjectFactory of = new ObjectFactory();
    Comprobante.Complemento comp = of.createComprobanteComplemento();
    List<Object> list = comp.getAny(); 
    list.add(element);
    document.setComplemento(comp);
  } 

  private Element marshalTFD() throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db = dbf.newDocumentBuilder(); 
    Document doc = db.newDocument();
    Marshaller m = CONTEXT.createMarshaller();
    m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
                  new NamespacePrefixMapperImpl());
    m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
    m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, 
         "http://www.sat.gob.mx/TimbreFiscalDigital TimbreFiscalDigital.xsd");
    m.marshal(tfd, doc);
    return doc.getDocumentElement();
  }

  public void marshal(OutputStream out) throws Exception {
    Marshaller m = CONTEXT.createMarshaller();
    m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
                  new NamespacePrefixMapperImpl());
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, 
                  "http://www.sat.gob.mx/cfd/3 cfdv3.xsd");
    m.marshal(document, out);
  }

  private TimbreFiscalDigital createStamp() {
    ObjectFactory of = new ObjectFactory();
    TimbreFiscalDigital tfd = of.createTimbreFiscalDigital();
    tfd.setVersion("1.0");
    Date date = new Date();
    tfd.setFechaTimbrado(date);
    tfd.setSelloCFD(cfdSignature);
    UUID uuid = UUID.randomUUID(); 
    tfd.setUUID(uuid.toString());
    tfd.setNoCertificadoSAT("30001000000100000801");
    return tfd;
  }

  private TimbreFiscalDigital getTimbreFiscalDigital(Comprobante document) 
    throws Exception {    
    Comprobante.Complemento comp = document.getComplemento();
    if (comp != null) {
      List<Object> list = comp.getAny();
      for (Object o : list) {
        if (o instanceof TimbreFiscalDigital) {
          return (TimbreFiscalDigital) o;
        }
      }
    }
    return null;
  }

}