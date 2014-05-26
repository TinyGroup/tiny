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
package org.tinygroup.weblayer.webcontext.session.encode.impl;

import org.tinygroup.weblayer.webcontext.session.encode.AbstractSerializationEncoder;
import org.tinygroup.weblayer.webcontext.session.encrypter.Encrypter;
import org.tinygroup.weblayer.webcontext.session.serializer.Serializer;

/**
 * 通过<code>Serializer</code>提供的序列化机制来编码对象，以及解码字符串。
 * <p>
 * 可设置<code>Serializer</code>和<code>Encrypter</code>， <code>Serializer</code>
 * 的默认值为<code>JavaSerializer</code>。
 * </p>
 *
 * @author renhui
 */
public class SerializationEncoder extends AbstractSerializationEncoder {
    public Serializer getSerializer() {
        return serializer;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public Encrypter getEncrypter() {
        return encrypter;
    }

    public void setEncrypter(Encrypter encrypter) {
        this.encrypter = encrypter;
    }

}
