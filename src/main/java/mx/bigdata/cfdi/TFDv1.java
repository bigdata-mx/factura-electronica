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

import mx.bigdata.sat.cfdi.CFDv3;

/**
 * @deprecated Reemplazado por {@link mx.bigdata.sat.cfdi.TFDv1}
 * a partir de la version 0.1.3
 */
@Deprecated public final class TFDv1 extends mx.bigdata.sat.cfdi.TFDv1 {

  public TFDv1(CFDv3 cfd) throws Exception {
    super(cfd);
  }

}