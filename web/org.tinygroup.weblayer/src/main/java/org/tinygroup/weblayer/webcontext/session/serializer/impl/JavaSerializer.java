/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.weblayer.webcontext.session.serializer.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.tinygroup.weblayer.webcontext.session.serializer.Serializer;


/**
 * Java序列化。
 *
 * @author renhui
 */
public class JavaSerializer implements Serializer {
    public void serialize(Object objectToEncode, OutputStream os) throws Exception {
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(os);
            oos.writeObject(objectToEncode);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public Object deserialize(InputStream is) throws Exception {
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(is);
            return ois.readObject();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                }
            }
        }
    }

    
    public String toString() {
        return getClass().getSimpleName();
    }

}
