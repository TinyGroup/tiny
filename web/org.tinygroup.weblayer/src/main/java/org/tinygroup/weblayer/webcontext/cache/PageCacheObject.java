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
package org.tinygroup.weblayer.webcontext.cache;

import java.io.Serializable;

/**
 * 
 * 功能说明: 页面缓存对象

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-23 <br>
 * <br>
 */
public class PageCacheObject implements Serializable {

	/**
	 * 页面内容
	 */
	private String content;
	/**
	 * 页面缓存失效时间
	 */
	private Long timeToLived;
	
	
	public PageCacheObject(String content, Long timeToLived) {
		super();
		this.content = content;
		this.timeToLived = timeToLived;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getTimeToLived() {
		return timeToLived;
	}
	public void setTimeToLived(Long timeToLived) {
		this.timeToLived = timeToLived;
	}
	
	
	
}
