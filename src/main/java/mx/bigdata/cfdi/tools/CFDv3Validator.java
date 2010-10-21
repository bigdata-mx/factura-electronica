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

package mx.bigdata.cfdi.tools;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.bind.util.ValidationEventCollector;

public final class CFDv3Validator {

  private final String origin;
  
  private final String xsd;

  public CFDv3Validator(String origin, String xsd) {
    this.origin = origin;
    this.xsd = xsd;
  }
  
  public void validate() throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance( "mx.bigdata.cfdi.schema" );
    Unmarshaller u = jc.createUnmarshaller();
    SchemaFactory sf =
      SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = sf.newSchema(new File(xsd));
    u.setSchema(schema);
    ValidationEventCollector vec = new ValidationEventCollector();
    u.setEventHandler(vec);
    u.unmarshal(new File(origin));
    if (vec.hasEvents()) {
      for (ValidationEvent ve : vec.getEvents()) {
        String msg = ve.getMessage();
        ValidationEventLocator vel = ve.getLocator();
        int line = vel.getLineNumber();
        int column = vel.getColumnNumber();
        System.err.println(origin + ": " + line + "." + column + ": " + msg); 
      }
    }
  }

  public static void main(String[] args) throws Exception {
    CFDv3Validator validator = new CFDv3Validator(args[0], args[1]);
    validator.validate();
  }

}