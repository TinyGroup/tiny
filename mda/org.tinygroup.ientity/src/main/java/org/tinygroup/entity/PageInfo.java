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
package org.tinygroup.entity;

import org.tinygroup.tinydb.Bean;


/**
 * 
 * 功能说明:分页查询返回对象的封装对象 

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-6 <br>
 * <br>
 */
public class PageInfo {

	/**
	 * 当页记录集
	 */
	private Bean[] beans;
	/**
	 * 总记录数
	 */
	private int totalSize;
	/**
	 * 每页记录数
	 */
	private int pageSize;
	/**
	 * 当前页
	 */
	private int pageNumber;
	/**
	 * 总页数
	 */
	private int totalPages;
	
	public PageInfo(Bean[] beans, int totalSize) {
		super();
		this.beans = beans;
		this.totalSize = totalSize;
	}
	public PageInfo() {
	}
	public Bean[] getBeans() {
		return beans;
	}
	public void setBeans(Bean[] beans) {
		this.beans = beans;
	}
	public int getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	
	
}
