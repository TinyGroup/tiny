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
package org.tinygroup.convert.validate.schemastring;

import org.tinygroup.commons.io.ByteArrayInputStream;
import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.ObjectValidator;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class XmlValidator implements ObjectValidator<String> {
    private String encode = "UTF-8";
    private Schema schema;

    public XmlValidator(String schemaContent, String encode) throws ConvertException {
        this(schemaContent);
        this.encode = encode;
    }

    public XmlValidator(String schemaContent) throws ConvertException {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Source source = new StreamSource(new ByteArrayInputStream(schemaContent.getBytes(encode)));
            this.schema = schemaFactory.newSchema(source);
        } catch (Exception e) {
            throw new ConvertException(e);
        }
    }

    public boolean isValidate(String xmlString) throws ConvertException {

        try {
            Source xmlSource = new StreamSource(new ByteArrayInputStream(xmlString.getBytes(encode)));
            Validator validator = schema.newValidator();
            validator.validate(xmlSource);
            return true;
        } catch (Exception e) {
            throw new ConvertException(e);
        }
    }

}
