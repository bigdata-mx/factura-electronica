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

import java.io.OutputStream;
import java.security.PrivateKey;

import java.security.cert.X509Certificate;

import org.xml.sax.ErrorHandler;

import mx.bigdata.sat.cfdi.CFDv3;

/**
 * @deprecated Reemplazado por {@link mx.bigdata.sat.cfdi.TFDv1}
 * a partir de la version 0.1.3
 */
@Deprecated public final class TFDv1 {

  private final mx.bigdata.sat.cfdi.TFDv1 tfd;

  public TFDv1(CFDv3 cfd, X509Certificate cert) throws Exception {
    this.tfd = new mx.bigdata.sat.cfdi.TFDv1(cfd, cert);
  }

  /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfdi.TFDv1#timbrar(PrivateKey)}
   * a partir de la version 0.1.3
   */
  @Deprecated public int stamp(PrivateKey key) throws Exception {
    return tfd.timbrar(key);
  }


 /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfdi.TFDv1#validar()}
   * a partir de la version 0.1.3
   */
  @Deprecated public void validate() throws Exception {
    tfd.validar();
  }
  
 /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfdi.TFDv1#validar(ErrorHandler)}
   * a partir de la version 0.1.3
   */
  @Deprecated public void validate(ErrorHandler handler) throws Exception {
    tfd.validar(handler);
  }
  
 /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfdi.TFDv1#verificar()}
   * a partir de la version 0.1.3
   */
  @Deprecated public int verify() throws Exception {
    return tfd.verificar();
  }

  /**
   * @deprecated Reemplazado por {@link mx.bigdata.sat.cfdi.TFDv1#guardar(OutputStream)}
   * a partir de la version 0.1.3
   */
  @Deprecated public void marshal(OutputStream out) throws Exception {
    tfd.guardar(out);
  }

}