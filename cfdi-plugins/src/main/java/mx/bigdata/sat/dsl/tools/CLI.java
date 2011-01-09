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

package mx.bigdata.sat.dsl.tools;

import java.io.*;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import mx.bigdata.sat.dsl.YamlToComprobantev2;
import mx.bigdata.sat.dsl.YamlToComprobantev3;
import mx.bigdata.sat.common.CFD;
import mx.bigdata.sat.cfd.CFDv2;
import mx.bigdata.sat.cfdi.CFDv3;
import mx.bigdata.sat.security.KeyLoader;

import org.yaml.snakeyaml.Yaml;

public final class CLI {
  
  public static CFD parse(String file) 
    throws Exception {
    Map<String, Object> map = loadMap(new File(file));
    String version = (String) map.get("version");
    if (version.equals("2.0")) {
      YamlToComprobantev2 ytc = new YamlToComprobantev2();
      mx.bigdata.sat.cfd.schema.Comprobante c = ytc.parse(map);
      return new CFDv2(c);
    } else if (version.equals("3.0")) {
      YamlToComprobantev3 ytc = new YamlToComprobantev3();
      mx.bigdata.sat.cfdi.schema.Comprobante c = ytc.parse(map);
      return new CFDv3(c);
    }
    return null;
  }

  private static Map loadMap(File file) throws Exception {
    Yaml yaml = new Yaml();
    FileInputStream in = new FileInputStream(file);
    try {
      return (Map) yaml.load(in);
    } finally {
      in.close();    
    }
  }

  public static void main(String[] args) throws Exception {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    System.err.print("Password: ");
    String passwd = in.readLine();
    PrivateKey key = KeyLoader
      .loadPKCS8PrivateKey(new FileInputStream(args[1]), passwd);
    X509Certificate cert = KeyLoader
      .loadX509Certificate(new FileInputStream(args[2]));
    OutputStream out = new FileOutputStream(args[3]);
    CFD cfd = parse(args[0]);
    cfd.sellar(key, cert);
    cfd.guardar(out);
  }
}