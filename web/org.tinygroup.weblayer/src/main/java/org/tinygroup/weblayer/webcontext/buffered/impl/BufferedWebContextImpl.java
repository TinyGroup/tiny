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
package org.tinygroup.weblayer.webcontext.buffered.impl;

import org.tinygroup.commons.io.ByteArray;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.AbstractWebContextWrapper;
import org.tinygroup.weblayer.webcontext.buffered.BufferedWebContext;
import org.tinygroup.weblayer.webcontext.buffered.response.BufferedResponseImpl;


public class BufferedWebContextImpl extends AbstractWebContextWrapper implements BufferedWebContext {

	public BufferedWebContextImpl(WebContext wrappedContext) {
		super(wrappedContext);
		setResponse(new BufferedResponseImpl(this, wrappedContext.getResponse()));
	}
	
	 /**
     * 设置是否将所有信息保存在内存中。
     *
     * @return 如果是，则返回<code>true</code>
     */
	public boolean isBuffering() {
		return getBufferedResponse().isBuffering();
	}

	 /**
     * 设置buffer模式，如果设置成<code>true</code>，表示将所有信息保存在内存中，否则直接输出到原始response中。
     * <p>
     * 此方法必须在<code>getOutputStream</code>和<code>getWriter</code>方法之前执行，否则将抛出
     * <code>IllegalStateException</code>。
     * </p>
     *
     * @param buffering 是否buffer内容
     * @throws IllegalStateException <code>getOutputStream</code>或
     *                               <code>getWriter</code>方法已经被执行
     */
    public void setBuffering(boolean buffering) {
        getBufferedResponse().setBuffering(buffering);
    }


    /**
     * 创建新的buffer，保存老的buffer。
     *
     * @throws IllegalStateException 如果不在buffer模式，或<code>getWriter</code>及
     *                               <code>getOutputStream</code>方法从未被调用
     */
    public void pushBuffer() {
        getBufferedResponse().pushBuffer();
    }

    /**
     * 弹出最近的buffer，如果堆栈中只有一个buffer，则弹出后再创建一个新的。
     *
     * @return 最近的buffer内容
     * @throws IllegalStateException 如果不在buffer模式，或<code>getWriter</code>
     *                               方法曾被调用，或 <code>getOutputStream</code>方法从未被调用
     */
    public ByteArray popByteBuffer() {
        return getBufferedResponse().popByteBuffer();
    }

    /**
     * 弹出最近的buffer，如果堆栈中只有一个buffer，则弹出后再创建一个新的。
     *
     * @return 最近的buffer内容
     * @throws IllegalStateException 如果不在buffer模式，或<code>getOutputStream</code>
     *                               方法曾被调用，或<code>getWriter</code>方法从未被调用
     */
    public String popCharBuffer() {
        return getBufferedResponse().popCharBuffer();
    }
	
	/**
     * 取得<code>BufferedRunDataResponse</code>实例。
     *
     * @return <code>BufferedRunDataResponse</code>实例
     */
    public BufferedResponseImpl getBufferedResponse() {
        return (BufferedResponseImpl) getResponse();
    }

	public ByteArray peekByteBuffer() {
		return getBufferedResponse().peekByteBuffer();
	}

	public String peekCharBuffer() {
		return getBufferedResponse().peekCharBuffer();
	}

}
