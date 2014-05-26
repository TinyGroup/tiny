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
package org.tinygroup.weblayer.webcontext;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.weblayer.TinyFilterHandler;
import org.tinygroup.weblayer.WebContext;


/**
 * 保存commit的状态，用来防止重复commit。
 *
 * @author renhui
 */
public class CommitMonitor extends AbstractWebContextWrapper implements HeaderCommitter {
    private final TinyFilterHandler handler;
    private       boolean                       headersCommitted;
    private       boolean                       committed;
    private WebContext topWebContext;
    public CommitMonitor(WebContext context,TinyFilterHandler handler) {
    	super(context);
        this.handler = Assert.assertNotNull(handler, "handler");
    }
    
    public boolean isHeadersCommitted() {
        return headersCommitted || committed;
    }

    public void setHeadersCommitted(boolean headersCommitted) {
        this.headersCommitted = headersCommitted;
    }

    public boolean isCommitted() {
        return committed;
    }

    public void setCommitted(boolean committed) {
        this.committed = committed;
    }
    
    public WebContext getTopWebContext() {
 			return topWebContext;
 		}

 		public void setTopWebContext(WebContext topWebContext) {
 			this.topWebContext = topWebContext;
 		}


    /** 实现内部接口：<code>HeaderCommitter</code>。 */
    public void commitHeaders() {
        if (isHeadersCommitted()) {
            return;
        }

        handler.commitHeaders(topWebContext);
    }
}
