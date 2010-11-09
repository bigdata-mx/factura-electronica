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

package mx.bigdata.sat.cfdi;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class NamespacePrefixMapperImpl extends NamespacePrefixMapper {
  
  public String getPreferredPrefix(String namespaceUri, String suggestion, 
                                   boolean requirePrefix) {
    if ("http://www.w3.org/2001/XMLSchema-instance".equals(namespaceUri)) {
      return "xsi";
    }
    if("http://www.sat.gob.mx/cfd/3".equals(namespaceUri)) {
      return "cfdi";
    }
    if("http://www.sat.gob.mx/TimbreFiscalDigital".equals(namespaceUri)) {
      return "tfd";
    }
    return suggestion;
  }
    
  public String[] getPreDeclaredNamespaceUris() {
    return new String[0];
  }
}