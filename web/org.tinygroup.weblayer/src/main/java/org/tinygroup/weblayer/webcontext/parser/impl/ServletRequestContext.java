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
package org.tinygroup.weblayer.webcontext.parser.impl;

import javax.servlet.http.HttpServletRequest;

/**
 * 继承自commons-fileupload-1.2.1的同名类，改进了如下内容：
 * <ul>
 * <li>假如<code>request.getCharacterEncoding()</code>返回<code>null</code> ，那么返回默认值
 * <code>ISO-8859-1</code>。该方法将被用来解析header，其中包括field name，file name等。原始类在
 * <code>request.getCharacterEncoding()</code>返回<code>null</code>
 * 时，将使用操作系统默认编码，这样将返回不确定的结果。经过修改后，就和servlet规范相一致。</li>
 * </ul>
 *
 * @author Michael Zhou
 */
public class ServletRequestContext extends org.apache.commons.fileupload.servlet.ServletRequestContext {
    public static final String DEFAULT_CHARSET = "ISO-8859-1";
    
    private HttpServletRequest request;

    public ServletRequestContext(HttpServletRequest request) {
        super(request);
        this.request=request;
    }

    
    public String getCharacterEncoding() {
        String charset = super.getCharacterEncoding();

        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }

        return charset;
    }
    
    public HttpServletRequest getRequest(){
    	return request;
    }
}
