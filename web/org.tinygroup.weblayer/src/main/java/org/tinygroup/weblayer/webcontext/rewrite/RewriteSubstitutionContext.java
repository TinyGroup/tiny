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
package org.tinygroup.weblayer.webcontext.rewrite;

import org.tinygroup.commons.tools.MatchResultSubstitution;
import org.tinygroup.weblayer.webcontext.parser.ParserWebContext;
import org.tinygroup.weblayer.webcontext.parser.valueparser.ParameterParser;


/**
 * 代表一个替换过程的上下文信息。
 * <p>
 * 用户<code>RewriteSubstitutionHandler</code>处理程序可以修改path和参数。
 * </p>
 *
 * @author renhui
 */
public interface RewriteSubstitutionContext extends RewriteContext {
    String getPath();

    void setPath(String path);

    ParserWebContext getParserWebContext();

    ParameterParser getParameters();

    MatchResultSubstitution getMatchResultSubstitution();
}
