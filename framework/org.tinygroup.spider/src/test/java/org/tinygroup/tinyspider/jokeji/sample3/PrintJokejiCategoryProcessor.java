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
package org.tinygroup.tinyspider.jokeji.sample3;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.tinyspider.Processor;

public class PrintJokejiCategoryProcessor implements Processor {
    public void process(String url, HtmlNode node) {
        System.out.println("\n===============================================");
        System.out.println(node.getContent());
        System.out.println("===============================================");
        JokejiTest.processUrl("http://www.jokeji.cn"+node.getAttribute("href"));
    }
}
