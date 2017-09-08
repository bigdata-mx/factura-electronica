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
package mx.bigdata.sat.common;

import java.util.List;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import org.w3c.dom.Element;

public interface ComprobanteBase33 {

    boolean hasComplemento();

    List<Comprobante.Complemento> getComplementoGetAny();

    String getSello();

    void setComplemento(Element e);

    Object getComprobante();
}
