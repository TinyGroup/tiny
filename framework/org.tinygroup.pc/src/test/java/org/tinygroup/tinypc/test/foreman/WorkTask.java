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
package org.tinygroup.tinypc.test.foreman;

import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.WorkStatus;

public class WorkTask implements Work {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9073481341113299557L;
	private String type;
	private String id;
	private Work nextWork;
	private boolean needSerialize;
	private Warehouse w;
	private WorkStatus workStatus;
	private String foremanType;
	public WorkTask(String type, String id,String foremanType) {
		this.type = type;
		this.id = id;
		this.foremanType = foremanType;
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public Work getNextWork() {
		return nextWork;
	}

	public Work setNextWork(Work nextWork) {
		this.nextWork = nextWork;
		return this.nextWork;
	}

	public boolean isNeedSerialize() {
		return needSerialize;
	}

	public void setNeedSerialize(boolean needSerialize) {
		this.needSerialize = needSerialize;
	}

	public Warehouse getInputWarehouse() {
		return w;
	}

	public void setInputWarehouse(Warehouse inputWarehouse) {
		this.w = inputWarehouse;
	}

	public void setWorkStatus(WorkStatus workStatus) {
		this.workStatus = workStatus;
	}

	public WorkStatus getWorkStatus() {
		return workStatus;
	}

	public String getForemanType() {
		return foremanType;
	}

}
