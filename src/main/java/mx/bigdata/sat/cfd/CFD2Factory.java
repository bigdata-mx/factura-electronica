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
package mx.bigdata.sat.cfd;

import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import mx.bigdata.sat.common.CFDFactory;

public final class CFD2Factory extends CFDFactory {

    public static CFD2 load(File file) throws Exception {
        try (InputStream in = new FileInputStream(file)) {
            return load(in);
        }
    }

    public static CFD2 load(InputStream in) throws Exception {
        return getCFD2(in);
    }

    private static CFD2 getCFD2(InputStream in) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteStreams.copy(in, baos);
        byte[] data = baos.toByteArray();
        if (getVersion(data).equals("2.2")) {
            return new CFDv22(new ByteArrayInputStream(data));
        } else {
            return new CFDv2(new ByteArrayInputStream(data));
        }
    }

}
