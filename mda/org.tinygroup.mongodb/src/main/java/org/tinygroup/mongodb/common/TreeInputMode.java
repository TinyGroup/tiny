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
package org.tinygroup.mongodb.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


/**
 * 
 * 功能说明:树结构输入模式 

 * 开发人员: renhui <br>
 * 开发时间: 2013-10-18 <br>
 * <br>
 */
@XStreamAlias("tree-input-mode")
public class TreeInputMode extends InputMode {
	@XStreamAsAttribute
	@XStreamAlias("tree-id")
	private String treeId;
    @XStreamAsAttribute
	private String url;
    @XStreamAsAttribute
	@XStreamAlias("param-info")
    private String paramInfo;
    @XStreamAsAttribute
	@XStreamAlias("popup-info")
    private String popupInfo;
    @XStreamAsAttribute
	@XStreamAlias("tree-info")
    private String treeInfo;

    

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getTreeId() {
		return treeId;
	}
	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}
	public String getParamInfo() {
		return paramInfo;
	}
	public void setParamInfo(String paramInfo) {
		this.paramInfo = paramInfo;
	}
	public String getPopupInfo() {
		return popupInfo;
	}
	public void setPopupInfo(String popupInfo) {
		this.popupInfo = popupInfo;
	}
	public String getTreeInfo() {
		return treeInfo;
	}
	public void setTreeInfo(String treeInfo) {
		this.treeInfo = treeInfo;
	}
	
	
}
