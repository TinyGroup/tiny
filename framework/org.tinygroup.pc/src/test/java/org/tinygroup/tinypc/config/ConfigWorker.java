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
package org.tinygroup.tinypc.config;

import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.AbstractWorker;

import java.rmi.RemoteException;

/**
 * 信息处理工人
 * Created by luoguo on 14-1-14.
 */
public class ConfigWorker extends AbstractWorker {
    /**
	 * 
	 */
	private static final long serialVersionUID = -232147883081281240L;

	public ConfigWorker() throws RemoteException {
        super("config");
    }

    public Warehouse doWork(Work work) throws RemoteException {
        String key = work.getInputWarehouse().get("key");
        String value = work.getInputWarehouse().get("value");
        System.out.println(String.format("config key:%s,value:%s", key, value));
        return null;
    }
}
