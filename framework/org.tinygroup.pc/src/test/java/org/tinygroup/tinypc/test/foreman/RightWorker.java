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

import java.rmi.RemoteException;

import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.AbstractWorker;

public class RightWorker extends AbstractWorker {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2418099267597610109L;

	public RightWorker(String type) throws RemoteException {
		super(type);
	}

	protected Warehouse doWork(Work work) throws RemoteException {
		System.out.println("execute work:"+work.getId());
		return work.getInputWarehouse();
	}

}
