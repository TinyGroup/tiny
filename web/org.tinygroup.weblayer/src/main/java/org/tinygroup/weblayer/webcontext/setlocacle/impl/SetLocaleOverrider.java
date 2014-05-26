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
package org.tinygroup.weblayer.webcontext.setlocacle.impl;

import static org.tinygroup.commons.tools.StringUtil.*;
import static org.tinygroup.commons.tools.PathNameWildcardCompiler.*;

import java.util.regex.Pattern;

/**
 * 根据request uri来设置输入、输出charset。
 *
 * @author Michael Zhou
 */
public class SetLocaleOverrider {
    private Pattern requestUriPattern;
    private String  requestUriPatternName;
    private String  inputCharset;
    private String  outputCharset;
    
    

    public SetLocaleOverrider(String requestUri,
			String inputCharset, String outputCharset) {
		this.requestUriPattern = compilePathName(requestUri);
		this.requestUriPatternName = requestUri;
		this.inputCharset = inputCharset;
		this.outputCharset = outputCharset;
	}

	public Pattern getRequestUriPattern() {
        return requestUriPattern;
    }

    public void setUri(String requestUriPatternName) {
        this.requestUriPattern = compilePathName(requestUriPatternName);
        this.requestUriPatternName = requestUriPatternName;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = trimToNull(inputCharset);
    }

    public String getOutputCharset() {
        return outputCharset;
    }

    public void setOutputCharset(String outputCharset) {
        this.outputCharset = trimToNull(outputCharset);
    }

    
    public String toString() {
        return String.format("Override[uri=%s, inputCharset=%s, outputCharset=%s]", requestUriPatternName, inputCharset, outputCharset);
    }
}
