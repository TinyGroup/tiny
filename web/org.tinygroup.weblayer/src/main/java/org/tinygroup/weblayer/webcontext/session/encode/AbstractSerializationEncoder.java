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
package org.tinygroup.weblayer.webcontext.session.encode;

import static org.tinygroup.commons.tools.ArrayUtil.isEmptyArray;
import static org.tinygroup.commons.tools.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.commons.codec.binary.Base64;
import org.tinygroup.commons.io.ByteArrayOutputStream;
import org.tinygroup.support.BeanSupport;
import org.tinygroup.weblayer.webcontext.session.SessionStore.StoreContext;
import org.tinygroup.weblayer.webcontext.session.encrypter.Encrypter;
import org.tinygroup.weblayer.webcontext.session.exception.SessionEncoderException;
import org.tinygroup.weblayer.webcontext.session.serializer.Serializer;
import org.tinygroup.weblayer.webcontext.session.serializer.impl.JsonConvertSerializer;

/**
 * 通过<code>Serializer</code>提供的序列化机制来编码对象，以及解码字符串。
 * <p>
 * 编码步骤为：
 * </p>
 * <ul>
 * <li>用<code>Serializer</code>序列化，默认使用<code>HessianSerializer</code>。</li>
 * <li>压缩。</li>
 * <li>如果<code>Encrypter</code>存在，用它加密，否则，不加密。</li>
 * <li>Base64编码。</li>
 * <li>URL encoding，以确保所有字符都符合HTTP header的要求。</li>
 * </ul>
 * <p>
 * 解码步骤相反。
 * </p>
 *
 * @author renhui
 */
public abstract class AbstractSerializationEncoder extends BeanSupport implements SessionEncoder {
    protected Serializer serializer;
    protected Encrypter  encrypter;

    
    protected void init() throws Exception {
        if (serializer == null) {
            serializer = new JsonConvertSerializer();
        }
    }

    /** 编码。 */
    public String encode(Map<String, Object> attrs, StoreContext storeContext) throws SessionEncoderException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 1. 序列化
        // 2. 压缩
        Deflater def = new Deflater(Deflater.BEST_COMPRESSION, false);
        DeflaterOutputStream dos = new DeflaterOutputStream(baos, def);

        try {
            serializer.serialize(assertNotNull(attrs, "objectToEncode is null"), dos);
        } catch (Exception e) {
            throw new SessionEncoderException("Failed to encode session state", e);
        } finally {
            try {
                dos.close();
            } catch (IOException e) {
            }

            def.end();
        }

        byte[] plaintext = baos.toByteArray().toByteArray();

        // 3. 加密
        byte[] cryptotext = encrypt(plaintext);

        // 4. base64编码
        try {
            String encodedValue = new String(Base64.encodeBase64(cryptotext, false), "ISO-8859-1");

            return URLEncoder.encode(encodedValue, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new SessionEncoderException("Failed to encode session state", e);
        }
    }

    /** 加密。 */
    private byte[] encrypt(byte[] plaintext) throws SessionEncoderException {
        if (encrypter != null) {
            return encrypter.encrypt(plaintext);
        }

        return plaintext;
    }

    /** 解码。 */
    public Map<String, Object> decode(String encodedValue, StoreContext storeContext) throws SessionEncoderException {
        // 1. base64解码
        byte[] cryptotext = null;

        try {
            encodedValue = URLDecoder.decode(assertNotNull(encodedValue, "encodedValue is null"), "ISO-8859-1");
            cryptotext = Base64.decodeBase64(encodedValue.getBytes("ISO-8859-1"));

            if (isEmptyArray(cryptotext)) {
                throw new SessionEncoderException("Session state is empty: " + encodedValue);
            }
        } catch (Exception e) {
            throw new SessionEncoderException("Failed to decode session state: ", e);
        }

        // 2. 解密
        byte[] plaintext = decrypt(cryptotext);

        if (isEmptyArray(plaintext)) {
            throw new SessionEncoderException("Decrypted session state is empty: " + encodedValue);
        }

        // 3. 解压缩
        ByteArrayInputStream bais = new ByteArrayInputStream(plaintext);
        Inflater inf = new Inflater(false);
        InflaterInputStream iis = new InflaterInputStream(bais, inf);

        // 4. 反序列化
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> attrs = (Map<String, Object>) serializer.deserialize(iis);
            return attrs;
        } catch (Exception e) {
            throw new SessionEncoderException("Failed to parse session state", e);
        } finally {
            try {
                iis.close();
            } catch (IOException e) {
            }

            inf.end();
        }
    }

    /** 解密。 */
    private byte[] decrypt(byte[] cryptotext) throws SessionEncoderException {
        if (encrypter != null) {
            return encrypter.decrypt(cryptotext);
        }

        return cryptotext;
    }

    
    public String toString() {
        return getClass().getSimpleName() + "[" + serializer + ", " + (encrypter == null ? "no encrypter" : encrypter)
               + "]";
    }
}
