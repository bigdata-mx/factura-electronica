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

import java.io.File;
import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;

import mx.bigdata.cfdi.schema.*;

public final class Main {

  public static void main(String[] args) throws Exception {
    Comprobante comp = createComprobante();
    JAXBContext jc = JAXBContext.newInstance( "mx.bigdata.cfdi.schema" );
    Marshaller m = jc.createMarshaller();
    SchemaFactory sf =
      SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = sf.newSchema(new File("resources/xsd/cfdv3.xsd"));
    m.setSchema(schema);
    m.marshal(comp, System.out);
  }

  private static Comprobante createComprobante() throws Exception {
    ObjectFactory of = new ObjectFactory();
    Comprobante comp = of.createComprobante();
    comp.setVersion("3.0");
    DatatypeFactory dtf = DatatypeFactory.newInstance(); 
    XMLGregorianCalendar cal = dtf
      .newXMLGregorianCalendar(new GregorianCalendar());
    comp.setFecha(cal);
    comp.setSello("");
    comp.setFormaDePago("PAGO EN UNA SOLA EXHIBICION");
    comp.setNoCertificado("30001000000100000800");
    comp.setCertificado("");
    comp.setSubTotal(new BigDecimal("13.25"));
    comp.setTotal(new BigDecimal("14.1"));
    return comp;
  }

}