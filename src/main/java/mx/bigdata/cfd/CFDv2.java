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
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.xml.transform.TransformerFactory;

import org.xml.sax.ErrorHandler;

import mx.bigdata.sat.cfd.schema.Comprobante;

/**
 * @deprecated Reemplazado por {@link mx.bigdata.sat.cfd.CFDv2}
 * a partir de la version 0.1.3
 */
@Deprecated public final class CFDv2 {
  
  private final mx.bigdata.sat.cfd.CFDv2 cfd;

  public CFDv2(InputStream in) throws Exception {
    this.cfd = new mx.bigdata.sat.cfd.CFDv2(in);
  }

  public CFDv2(Comprobante comprobante) throws Exception {
    this.cfd = new mx.bigdata.sat.cfd.CFDv2(comprobante);
  }

  public void setTransformerFactory(TransformerFactory tf) {
    cfd.setTransformerFactory(tf);
  }
    
  /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfd.CFDv2#sellar(PrivateKey, X509Certificate)}
   * a partir de la version 0.1.3
   */
  @Deprecated public void sign(PrivateKey key, X509Certificate cert) throws Exception {
    cfd.sellar(key, cert);
  }

  /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfd.CFDv2#validar()}
   * a partir de la version 0.1.3
   */
  @Deprecated public void validate() throws Exception {
    cfd.validar(null);
  }

  /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfd.CFDv2#validar(ErrorHandler)}
   * a partir de la version 0.1.3
   */
  @Deprecated public void validate(ErrorHandler handler) throws Exception {
    cfd.validar(handler);
  }

  /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfd.CFDv2#verificar()}
   * a partir de la version 0.1.3
   */
  @Deprecated public void verify() throws Exception {
    cfd.verificar();
  }
   
  /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfd.CFDv2#verificar(Certificate)}
   * a partir de la version 0.1.3
   */
  @Deprecated public void verify(Certificate cert) throws Exception {
    cfd.verificar(cert);
  }

  /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfd.CFDv2#guardar(OutputStream)}
   * a partir de la version 0.1.3
   */
  @Deprecated public void marshal(OutputStream out) throws Exception {
    cfd.guardar(out);
  }

}