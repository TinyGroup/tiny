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
package org.tinygroup.xmlparser.formatter;

import junit.framework.TestCase;
import org.tinygroup.xmlparser.XmlDocument;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.nio.charset.Charset;

public class XmlFormatterTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFormatDocumentSelfHtmlDocument() {
        try {
            XmlDocument doc;
            doc = new XmlStringParser().parse("<html 中='文'><!--abc--><head><title>aaa</title></head></html>");
            XmlFormater f = new XmlFormater();
            System.out.println(f.format(doc));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testFormat() {
        try {
            XmlDocument xmlDocument = new XmlStringParser().parse("<title><a>a</a></title>");
            XmlFormater formater = new XmlFormater();
            System.out.println(formater.format(xmlDocument));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testWriteFormat() {
        try {
            XmlDocument doc;
            doc = new XmlStringParser().parse("<title><a>a</a></title>");
            XmlFormater f = new XmlFormater();
            f.format(doc, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testError() {
        try {
            XmlDocument document = new XmlStringParser().parse("<Html><html></html></html>");
            XmlFormater formater = new XmlFormater();
            formater.format(document, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testFormatDocumentSelfHtmlDocumentOutputStream() {
        try {
            XmlDocument document = new XmlStringParser().parse("<html 中='文'><head><title>aaa</title><中>中信</中></head></html>");
            document.write(System.out);
            System.out.println("\n================================\n");
            XmlFormater formater = new XmlFormater();
            formater.setEncode(Charset.defaultCharset().displayName());
            formater.format(document, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
