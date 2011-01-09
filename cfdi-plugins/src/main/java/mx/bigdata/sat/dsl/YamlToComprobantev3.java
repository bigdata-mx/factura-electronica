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
package mx.bigdata.sat.dsl;

import java.util.HashMap;
import java.util.Map;

import mx.bigdata.sat.cfdi.schema.Comprobante;
import mx.bigdata.sat.cfdi.schema.ObjectFactory;

public final class YamlToComprobantev3 {
  
  private final Map<String, String> tx = new HashMap();
  
  {
    tx.put("DomicilioFiscal", "TUbicacionFiscal");
    tx.put("Domicilio", "TUbicacion");
  }
  
  public Comprobante parse(Map<String, Object> data) throws Exception {
    ObjectFactory of = new ObjectFactory();
    Comprobante comp = of.createComprobante();
    YamlToComprobanteHelper helper = new YamlToComprobanteHelper(of, tx);
    helper.invoke(comp, data);
    return comp;
  }
}
