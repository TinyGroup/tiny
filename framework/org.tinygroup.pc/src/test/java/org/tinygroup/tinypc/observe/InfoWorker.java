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
package org.tinygroup.tinypc.observe;

import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.AbstractWorker;

import java.rmi.RemoteException;

/**
 * 信息处理工人
 * Created by luoguo on 14-1-14.
 */
public class InfoWorker extends AbstractWorker {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6369978871587731479L;

	public InfoWorker(String type) throws RemoteException {
        super(type);
    }

    public Warehouse doWork(Work work) throws RemoteException {
        String title = work.getInputWarehouse().get("title");
        String content = work.getInputWarehouse().get("content");
        System.out.println(String.format("Title:%s,Content:%s", title, content));
        return null;
    }
}
