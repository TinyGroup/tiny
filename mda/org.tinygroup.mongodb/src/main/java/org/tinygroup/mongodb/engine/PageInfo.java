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
package org.tinygroup.mongodb.engine;

import org.bson.BSONObject;

/**
 * 
 * 功能说明:分页查询返回对象的封装对象
 * <p>
 * 
 * 开发人员: renhui <br>
 * 开发时间: 2013-9-6 <br>
 * <br>
 */
public class PageInfo {

	/**
	 * 当页记录集
	 */
	private BSONObject[] objects;
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

	public PageInfo(BSONObject[] objects, int totalSize) {
		super();
		this.objects = objects;
		this.totalSize = totalSize;
	}

	public PageInfo() {
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

	public int getTotalPages() {
		return totalPages;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public BSONObject[] getObjects() {
		return objects;
	}

	public void setObjects(BSONObject[] objects) {
		this.objects = objects;
	}

	public void pageAttributeSet(int pageSize, int pageNumber, int totalSize) {
		// if (pageSize <= 0 || pageNumber <= 0 || totalSize < 0) {
		// throw new IllegalArgumentException(
		// "参数pageSize和pageNumber必须大于零，totalSize必须大于等于零");
		// }
		if (pageNumber <= 0) {
			pageNumber = 1;
		}
		if (pageSize <= 0) {
			pageSize = 10;
		}

		this.totalSize = totalSize;
		this.pageSize = pageSize;
		this.totalPages = totalSize % pageSize == 0 ? (totalSize / pageSize)
				: (totalSize / pageSize + 1);
		this.pageNumber = pageNumber > totalPages ? (totalPages > 0 ? totalPages
				: 1)
				: pageNumber;
	}

	public int getStart() {
		return (pageNumber - 1) * pageSize;
	}

	/**
	 * 获取数组长度，主要计算出最后一页的实际记录数
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param totalPages
	 * @param totalSize
	 * @return
	 */
	public int getArraySize() {
		int temp = pageSize;
		if (totalSize < pageSize) { // 总数不够一页
			temp = totalSize;
		} else if ((pageNumber == totalPages)) {
			temp = totalSize - pageSize * (totalPages - 1); // 最后一页实际记录数
		}
		return temp;
	}

}
