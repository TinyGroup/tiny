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
package org.tinygroup.weblayer.webcontext.lazycommit.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.AbstractResponseWrapper;
import org.tinygroup.weblayer.webcontext.AbstractWebContextWrapper;
import org.tinygroup.weblayer.webcontext.lazycommit.LazyCommitWebContext;
import org.tinygroup.weblayer.webcontext.lazycommit.exception.LazyCommitFailedException;


/**
 * 延迟提交response的实现。
 *
 * @author renhui
 */
public class LazyCommitWebContextImpl extends AbstractWebContextWrapper implements LazyCommitWebContext {
    private final static Logger log = LoggerFactory.getLogger(LazyCommitWebContext.class);
    private SendError sendError;
    private String    sendRedirect;
    private boolean   setLocation;
    private boolean   bufferFlushed;
    private int       status;

    /**
     * 包装一个<code>RequestContext</code>对象。
     *
     * @param wrappedContext 被包装的<code>RequestContext</code>
     */
    public LazyCommitWebContextImpl(WebContext wrappedContext) {
        super(wrappedContext);
        setResponse(new ResponseWrapper(wrappedContext.getResponse()));
    }

    /**
     * 判断当前请求是否已出错。
     *
     * @return 如果出错，则返回<code>true</code>
     */
    public boolean isError() {
        return sendError != null;
    }

    /**
     * 如果<code>sendError()</code>方法曾被调用，则该方法返回一个error状态值。
     *
     * @return error状态值，若系统正常，则返回<code>0</code>
     */
    public int getErrorStatus() {
        if (sendError != null) {
            return sendError.status;
        }

        return 0;
    }

    /**
     * 如果<code>sendError()</code>方法曾被调用，则该方法返回一个error信息。
     *
     * @return error信息，若系统正常，则返回<code>null</code>
     */
    public String getErrorMessage() {
        if (sendError != null) {
            return sendError.message;
        }

        return null;
    }

    /**
     * 判断当前请求是否已被重定向。
     *
     * @return 如果重定向，则返回<code>true</code>
     */
    public boolean isRedirected() {
        return setLocation || !StringUtil.isEmpty(sendRedirect);
    }

    /**
     * 取得重定向的URI。
     *
     * @return 重定向的URI，如果没有重定向，则返回<code>null</code>
     */
    public String getRedirectLocation() {
        return sendRedirect;
    }

    /**
     * 取得最近设置的HTTP status。
     *
     * @return HTTP status值
     */
    public int getStatus() {
        return status;
    }

    public void commitHeaders() throws LazyCommitFailedException {
        try {
            ((ResponseWrapper) getResponse()).commitHeaders();
        } catch (IOException e) {
            throw new LazyCommitFailedException(e);
        }
    }

    /**
     * 结束一个请求。
     *
     * @throws LazyCommitFailedException 如果失败
     */
    public void commit() throws LazyCommitFailedException {
        try {
            ((ResponseWrapper) getResponse()).commit();
        } catch (IOException e) {
            throw new LazyCommitFailedException(e);
        }
    }

    /** 包装response。 */
    private class ResponseWrapper extends AbstractResponseWrapper {
        private boolean headersCommitted;

        public ResponseWrapper(HttpServletResponse response) {
            super(LazyCommitWebContextImpl.this, response);
        }

        
        public void sendError(int status) throws IOException {
            sendError(status, null);
        }

        
        public void sendError(int status, String message) throws IOException {
            if (sendError == null && sendRedirect == null) {
                sendError = new SendError(status, message);

                if (headersCommitted) {
                    super.sendError(status, message);
                }
            }
        }

        /**
         * 设置重定向URI。
         *
         * @param location 重定向的URI
         * @throws IOException           输入输出失败
         * @throws IllegalStateException 如果response已经committed
         */
        
        public void sendRedirect(String location) throws IOException {
            if (sendError == null && sendRedirect == null) {
                sendRedirect = location;

                if (headersCommitted) {
                    super.sendRedirect(location);
                }
            }
        }

        
        public void setHeader(String key, String value) {
            if ("location".equalsIgnoreCase(key)) {
                setLocation = true;
            }

            super.setHeader(key, value);
        }

        
        public void flushBuffer() throws IOException {
            bufferFlushed = true;

            if (headersCommitted) {
                super.flushBuffer();
            }
        }

        /**
         * 设置HTTP status。
         *
         * @param sc HTTP status值
         */
        
        public void setStatus(int sc) {
            status = sc;

            if (headersCommitted) {
                super.setStatus(sc);
            }
        }

        
        public void reset() {
            super.reset();
            sendError = null;
            sendRedirect = null;
            setLocation = false;
            bufferFlushed = false;
            status = 0;
        }

        private void commitHeaders() throws IOException {
            headersCommitted = true;

            if (status > 0) {
                log.logMessage(LogLevel.DEBUG,"Set HTTP status to {}" ,status);
                super.setStatus(status);
            }

            if (sendError != null) {
                if (sendError.message == null) {
                	log.logMessage(LogLevel.DEBUG,"Set error page:  {}" ,sendError.status);
                    super.sendError(sendError.status);
                } else {
                	log.logMessage(LogLevel.DEBUG,"Set error page: {} {}" ,sendError.status,sendError.message);
                    super.sendError(sendError.status, sendError.message);
                }
            } else if (sendRedirect != null) {
            	log.logMessage(LogLevel.DEBUG,"Set redirect location to {}" ,sendRedirect);
                // 将location用输出编码转换一下，这样可以确保包含非US_ASCII字符的URL正确输出
                String charset = getCharacterEncoding();

                if (charset != null) {
                    sendRedirect = new String(sendRedirect.getBytes(charset), "8859_1");
                }

                super.sendRedirect(sendRedirect);
            }
        }

        private void commit() throws IOException {
            if (bufferFlushed) {
                super.flushBuffer();
            }
        }
    }

    /** 保存sendError的信息。 */
    private class SendError {
        public final int    status;
        public final String message;

        public SendError(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
