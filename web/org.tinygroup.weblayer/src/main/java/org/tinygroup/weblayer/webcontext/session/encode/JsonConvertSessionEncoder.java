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

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tinygroup.commons.io.ByteArrayOutputStream;
import org.tinygroup.convert.objectjson.jackson.JsonToObject;
import org.tinygroup.convert.objectjson.jackson.ObjectToJson;
import org.tinygroup.weblayer.webcontext.session.SessionStore.StoreContext;
import org.tinygroup.weblayer.webcontext.session.exception.SessionEncoderException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static org.tinygroup.commons.tools.ArrayUtil.isEmptyArray;
import static org.tinygroup.commons.tools.Assert.assertNotNull;

/**
 * 
 * 功能说明:没有压缩加密的序列化处理 

 * 开发人员: renhui <br>
 * 开发时间: 2013-10-9 <br>
 * <br>
 */
public class JsonConvertSessionEncoder implements SessionEncoder {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String encode(Map<String, Object> attrs, StoreContext storeContext)
			throws SessionEncoderException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectToJson objectToXml=new ObjectToJson(JsonSerialize.Inclusion.NON_NULL);
        try {
            String result= objectToXml.convert(attrs);
            baos.write(result.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new SessionEncoderException("Failed to encode session state", e);
        } finally {
            baos.close();
        }

        byte[] plaintext = baos.toByteArray().toByteArray();

        // 4. base64编码
        try {
            String encodedValue = new String(Base64.encodeBase64(plaintext, false), "ISO-8859-1");

            return URLEncoder.encode(encodedValue, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new SessionEncoderException("Failed to encode session state", e);
        }
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> decode(String encodedValue,
			StoreContext storeContext) throws SessionEncoderException {
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

        if (isEmptyArray(cryptotext)) {
            throw new SessionEncoderException("Decrypted session state is empty: " + encodedValue);
        }

        try {
        	JsonToObject jsonToObject=new JsonToObject(HashMap.class);
            Map<String, Object> attrs = (Map<String, Object>) jsonToObject.convert(new String(cryptotext, "UTF-8"));
            return attrs;
        } catch (Exception e) {
            throw new SessionEncoderException("Failed to parse session state", e);
        } 
	}

}
