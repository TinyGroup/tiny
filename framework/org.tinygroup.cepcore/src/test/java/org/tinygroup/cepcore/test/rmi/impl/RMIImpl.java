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
package org.tinygroup.cepcore.test.rmi.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.tinygroup.cepcore.test.rmi.RMIInterface;
import org.tinygroup.event.Event;

public class RMIImpl extends UnicastRemoteObject implements RMIInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1542017064746686457L;
	public RMIImpl() throws RemoteException {
		super();
	}
	public Event deal(Event context) throws RemoteException {
		return context;
		//System.out.println(context.getServiceRequest().getContext().get("node"));
		
	}

}
