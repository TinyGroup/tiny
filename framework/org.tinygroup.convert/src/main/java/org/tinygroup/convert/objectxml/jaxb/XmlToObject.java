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
package org.tinygroup.convert.objectxml.jaxb;

import org.tinygroup.commons.io.ByteArrayInputStream;
import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class XmlToObject<T> implements Converter<String, T> {
    private String encode = "UTF-8";
    private JAXBContext context;
    private Unmarshaller unmarshaller;

    public XmlToObject(String className, String encode) throws ConvertException {
        this(className);
        this.encode = encode;
    }

    public XmlToObject(String className) throws ConvertException {
        try {
            context = JAXBContext.newInstance(className);
            unmarshaller = context.createUnmarshaller();
        } catch (Exception e) {
            throw new ConvertException(e);
        }
    }

    public XmlToObject(Class<T> clazz) throws ConvertException {
        try {
            context = JAXBContext.newInstance(clazz);
            unmarshaller = context.createUnmarshaller();
        } catch (Exception e) {
            throw new ConvertException(e);
        }
    }


    @SuppressWarnings("unchecked")
    public T convert(String inputData) throws ConvertException {

        try {
            return (T) unmarshaller.unmarshal(new ByteArrayInputStream(inputData.getBytes(encode)));
        } catch (Exception e) {
            throw new ConvertException(e);
        }

    }
}
