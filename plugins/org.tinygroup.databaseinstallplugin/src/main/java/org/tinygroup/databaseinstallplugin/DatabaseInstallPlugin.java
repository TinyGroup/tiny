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
package org.tinygroup.databaseinstallplugin;

import org.tinygroup.databasebuinstaller.DatabaseInstallerProcessor;
import org.tinygroup.plugin.Plugin;

/**
 * 数据库脚本安装插件
 * 功能说明: 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-18 <br>
 * <br>
 */
public class DatabaseInstallPlugin  implements Plugin {
	
	
	private DatabaseInstallerProcessor installer;
	

	public DatabaseInstallerProcessor getInstaller() {
		return installer;
	}

	public void setInstaller(DatabaseInstallerProcessor installer) {
		this.installer = installer;
	}


	public void start() {
		installer.process();
	}

	public void stop() {

	}

}
