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
package org.tinygroup.xmlparser;

import junit.framework.TestCase;
import org.tinygroup.xmlparser.XmlDocument;
import org.tinygroup.xmlparser.parser.XmlStringParser;

/**
 * Created by luoguo on 13-12-20.
 */
public class TestEncodingOrder extends TestCase {
    public void testXmlEncoding() {
        XmlDocument document = new XmlStringParser().parse(
                "<?xml version=\"1.0\" " + "encoding=\"gb2312\"?><root />");
        assertEquals("<?xml version=\"1.0\" encoding=\"gb2312\"?><root></root>",document.toString());
    }
    public void testXmlAttributeOrder() {
        XmlDocument document = new XmlStringParser().parse("<root a='1' b='2' c='3' 'd=4' " +
                "a1='7'/>");
        assertEquals("<root a=\"1\" b=\"2\" c=\"3\" d=\"4\" a1=\"7\"></root>",document.toString());
    }
}
