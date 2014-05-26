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
package org.tinygroup.weblayer.webcontext.rewrite.impl;

import org.tinygroup.weblayer.webcontext.parser.ParserWebContext;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteSubstitutionContext;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteSubstitutionHandler;

/**
 * 替换处理程序，可用来规格化URL，使后续的rewrite配置更统一。
 *
 * @author renhui
 */
public class UrlNormalizer implements RewriteSubstitutionHandler {
    public void postSubstitution(RewriteSubstitutionContext context) {
        ParserWebContext parser = context.getParserWebContext();
        String path = context.getPath();
        String normalizedPath = parser.convertCase(path);

        context.setPath(normalizedPath);
    }
}
