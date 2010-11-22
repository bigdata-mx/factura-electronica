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

import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList;

public final class ValidationErrorHandler extends DefaultHandler {

  private List<SAXParseException> errors = Lists.newArrayList();

  public void error(SAXParseException e)  {
    errors.add(e);
  }
  
  public List<SAXParseException> getErrors() {
    return ImmutableList.copyOf(errors);
  }
}