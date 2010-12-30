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

import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.xml.transform.TransformerFactory;

import org.xml.sax.ErrorHandler;

public interface CFD {
  void setTransformerFactory(TransformerFactory tf); 
  void sellar(PrivateKey key, X509Certificate cert) throws Exception;
  void validar() throws Exception;
  void validar(ErrorHandler handler) throws Exception;
  void verificar() throws Exception;
  void guardar(OutputStream out) throws Exception;
  String getCadenaOriginal() throws Exception;
}