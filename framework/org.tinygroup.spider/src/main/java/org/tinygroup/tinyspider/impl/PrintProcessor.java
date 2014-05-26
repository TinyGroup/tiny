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
package org.tinygroup.tinyspider.impl;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinyspider.Processor;

public class PrintProcessor implements Processor {
    private static Logger logger = LoggerFactory.getLogger(PrintProcessor.class);

    public void process(String url, HtmlNode node) {
        logger.logMessage(LogLevel.INFO, "url:{}", url);
        logger.logMessage(LogLevel.INFO, "content:\n{}", node.toString());
    }

}
