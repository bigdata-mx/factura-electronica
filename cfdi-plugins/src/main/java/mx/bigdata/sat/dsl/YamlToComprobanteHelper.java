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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public final class YamlToComprobanteHelper {
  
  private final Object factory;
  
  private final Map<String, String> transform;
  
  public YamlToComprobanteHelper(Object factory, Map<String, String> transform) {
    this.factory = factory;
    this.transform = transform;
  }
  
  public void invoke(Object object, Map<String, Object> data) 
    throws Exception {
    invoke(object, data, false);
  }

  public void invoke(Object object, Map<String, Object> data, boolean isList) 
    throws Exception {
    Class<?> c = object.getClass();
    for (Map.Entry<String, Object> me : data.entrySet()) {
      String type = me.getKey();
      String qname = Character.toUpperCase(type.charAt(0))
        + type.substring(1);
      String tx = transform.get(qname);
      StringBuilder enclosing = new StringBuilder();
      if (tx == null) {
        getEnclosing(c, enclosing);
        tx =  c.getSimpleName() + qname;
      }
      Object value = castValue(tx, enclosing.toString(), me.getValue());
      if (!isList) {
        Method method = c.getMethod("set" + qname, value.getClass());
        method.invoke(object, value);
      } else {
        Method method = c.getMethod("get" + qname);
        List list = (List) method.invoke(object);
        list.add(value);
      }
    }
  }
  
  private Object castValue(String type, String enclosing, Object o)
    throws Exception {
    if (o instanceof Integer) {
      return new BigInteger(o.toString());
    }
    if (o instanceof Double) {
      return new BigDecimal(o.toString());
    } 
    if (o instanceof Map) {
      Class<?> c = factory.getClass(); 
      Method method = c.getMethod("create" + enclosing + type);
      Object object = method.invoke(factory);
      invoke(object, (Map<String, Object>) o);
      return object;
    }
    if (o instanceof List) {
      Class<?> c = factory.getClass(); 
      Method method = c.getMethod("create" + enclosing + type);
      Object object = method.invoke(factory);
      List<Map> list =  (List<Map>) o;
      for (Map<String, Object> elem : list) {
        invoke(object, elem, true);
      }     
      return object;
    }
    return o;
  }

  private String transform(String type, String qname) {
    String tx = transform.get(qname);
    return (tx != null) ? tx : type + qname;
  }
  
  private void getEnclosing(Class c, StringBuilder list) {
    Class enclosing = c.getEnclosingClass(); 
    if (enclosing != null) {
      list.insert(0, enclosing.getSimpleName());
      getEnclosing(enclosing, list);
    }
  }
}
