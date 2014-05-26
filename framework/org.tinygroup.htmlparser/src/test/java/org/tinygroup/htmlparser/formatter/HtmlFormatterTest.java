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
package org.tinygroup.htmlparser.formatter;

import junit.framework.TestCase;
import org.tinygroup.htmlparser.HtmlDocument;
import org.tinygroup.htmlparser.parser.HtmlStringParser;

import java.nio.charset.Charset;

public class HtmlFormatterTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFormatDocumentSelfHtmlDocument() {
        HtmlDocument doc;
        try {
            doc = new HtmlStringParser().parse("<html 中='文'><!--abc--><HEAD><title>aaa</title></head></html>");
            doc.write(System.out);
            System.out.println("\n================================\n");
            HtmlFormater f = new HtmlFormater();
            System.out.println(f.format(doc));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testDOCTYPE() {
        HtmlDocument doc;
        try {
            doc = new HtmlStringParser().parse("<html></html><a></a>");
            doc.write(System.out);
            System.out.println("\n================================");
            HtmlFormater f = new HtmlFormater();
            System.out.println(f.format(doc));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testFormatDocumentSelfHtmlDocumentOutputStream() {
        try {
            HtmlDocument doc = new HtmlStringParser().parse("<html 中='文'><head><title>aaa</title><中>中信</中></head></html>");
            doc.write(System.out);
            System.out.println("\n================================\n");
            HtmlFormater f = new HtmlFormater();
            f.setEncode(Charset.defaultCharset().displayName());
            f.format(doc, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
