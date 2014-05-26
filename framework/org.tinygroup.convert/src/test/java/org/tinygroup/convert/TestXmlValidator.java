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
package org.tinygroup.convert;

import org.tinygroup.convert.objectxml.jaxb.ObjectToXml;
import org.tinygroup.convert.validate.schemafile.XmlValidator;

import java.io.File;
import java.net.URISyntaxException;

/**
 * 功能说明: 验证xml格式
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-27 <br>
 * <br>
 */
public class TestXmlValidator extends AbstractConvertTestCase {

    public void testXmlValidator() throws ConvertException {

        try {
            File schemaFile = new File(getClass().getResource("/validator/schema1.xsd").toURI());
            XmlValidator xmlValidator = new XmlValidator(schemaFile);
            String xmlString = getXmlString();
            assertTrue(xmlValidator.isValidate(xmlString));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

    private String getXmlString() throws ConvertException {
        Classes classes = createClasses();
        ObjectToXml<Classes> objectToXml = new ObjectToXml<Classes>(Classes.class, true);
        return objectToXml.convert(classes);
    }

}
