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
package mx.bigdata.sat.schema.binder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateTimeCustomBinder {
  public static Date parseDateTime(String s) {
    try {
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      return formatter.parse(s);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static String printDateTime(Date dt) {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    return (dt == null) ? null : formatter.format(dt);
  }
}
