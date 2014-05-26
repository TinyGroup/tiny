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
package org.tinygroup.databasebuinstaller;

import org.tinygroup.commons.order.Ordered;

/**
 * 
 * 功能说明:数据库安装处理接口 

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-15 <br>
 * <br>
 */
public interface InstallProcessor extends Ordered {
	String TABLE_INSTALL_PROCESSOR="tableInstallProcessor";
	String INITDATA_INSTALL_PROCESSOR="initDataInstallProcessor";
	String PROCEDURE_INSTALL_PROCESSOR="procedureInstallProcessor";
	String VIEW_INSTALL_PROCESSOR="viewInstallProcessor";
	String DATABASE_INSTALL_PROCESSOR="databaseInstaller";
   /**
    * 
    * 对某种数据库语言进行处理
    */
	void process(String language);

}
