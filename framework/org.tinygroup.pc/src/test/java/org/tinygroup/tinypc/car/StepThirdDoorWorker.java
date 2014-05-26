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
package org.tinygroup.tinypc.car;

import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;

import java.rmi.RemoteException;

/**
 * Created by luoguo on 14-1-28.
 */
public class StepThirdDoorWorker extends StepThirdWorker {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7295028639938346454L;
	public static final String DOOR = "door";

    public StepThirdDoorWorker() throws RemoteException {
        super(DOOR);
    }


    public boolean acceptWork(Work work) {
        return acceptWork(work, DOOR);
    }


    protected Warehouse doWork(Work work) throws RemoteException {
        return super.doWork(work, DOOR);
    }
}
