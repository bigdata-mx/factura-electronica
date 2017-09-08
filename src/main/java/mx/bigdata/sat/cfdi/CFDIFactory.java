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
package mx.bigdata.sat.cfdi;

import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import mx.bigdata.sat.common.CFDFactory;

public final class CFDIFactory extends CFDFactory {

    public static CFDI load(File file) throws Exception {
        try (InputStream in = new FileInputStream(file)) {
            return load(in);
        }
    }

    public static CFDI load(InputStream in) throws Exception {
        return getCFDI(in);
    }

    private static CFDI getCFDI(InputStream in) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteStreams.copy(in, baos);
        byte[] data = baos.toByteArray();
        if (getVersion(data).equals("3.2")) {
            return new CFDv32(new ByteArrayInputStream(data));
        } else {
            return new CFDv3(new ByteArrayInputStream(data));
        }
    }

    public static CFDI33 load33(File file) throws Exception {
        try (InputStream in = new FileInputStream(file)) {
            return load33(in);
        }
    }

    public static CFDI33 load33(InputStream in) throws Exception {
        return getCFDI33(in);
    }

    private static CFDI33 getCFDI33(InputStream in) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteStreams.copy(in, baos);
        byte[] data = baos.toByteArray();
        return new CFDv33(new ByteArrayInputStream(data));
    }

}
