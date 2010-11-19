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

import java.io.InputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.xml.sax.ErrorHandler;

import mx.bigdata.sat.cfdi.schema.Comprobante;

/**
 * @deprecated Reemplazado por {@link mx.bigdata.sat.cfdi.CFDv3}
 * a partir de la version 0.1.3
 */
@Deprecated public final class CFDv3 {
  
  private final mx.bigdata.sat.cfdi.CFDv3 cfd;

  public CFDv3(InputStream in) throws Exception {
    this.cfd = new mx.bigdata.sat.cfdi.CFDv3(in);
  }

  public CFDv3(Comprobante comprobante) throws Exception {
    this.cfd = new mx.bigdata.sat.cfdi.CFDv3(comprobante);
  }

  /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfdi.CFDv3#sellar(PrivateKey, X509Certificate)}
   * a partir de la version 0.1.3
   */
  public void sign(PrivateKey key, X509Certificate cert) throws Exception {
    cfd.sellar(key, cert);
  }

  /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfdi.CFDv3#validar()}
   * a partir de la version 0.1.3
   */
  @Deprecated public void validate() throws Exception {
    cfd.validar(null);
  }

  /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfdi.CFDv3#validar(ErrorHandler)}
   * a partir de la version 0.1.3
   */
  @Deprecated public void validate(ErrorHandler handler) throws Exception {
    cfd.validar(handler);
  }

  /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfdi.CFDv3#verificar()}
   * a partir de la version 0.1.3
   */
  @Deprecated public void verify() throws Exception {
    cfd.verificar();
  }

  /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfdi.CFDv3#guardar(OutputStream)}
   * a partir de la version 0.1.3
   */
  @Deprecated public void marshal(OutputStream out) throws Exception {
    cfd.guardar(out);
  }
}