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

package mx.bigdata.cfd;

import java.io.InputStream;

import mx.bigdata.sat.cfd.schema.Comprobante;
/**
 * @deprecated  As of release 0.1.3, replaced by {@link mx.bigdata.sat.cfd.CFDv2}
 */

@Deprecated
public final class CFDv2 extends mx.bigdata.sat.cfd.CFDv2 {
  public CFDv2(InputStream in) throws Exception {
    super(in);
  }

  public CFDv2(Comprobante comprobante) throws Exception {
    super(comprobante);
  }
}