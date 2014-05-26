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
package org.tinygroup.tinypc.impl;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinypc.PCRuntimeException;
import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.Worker;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * 只找一个工人进行工作的包工头 Created by luoguo on 14-1-8.
 */
public class ForemanSelectOneWorker extends AbstractForeman {
	/**
	 * 
	 */
	private static final long serialVersionUID = -848878926324050616L;
	private static transient Logger logger = LoggerFactory
			.getLogger(ForemanSelectOneWorker.class);

	public ForemanSelectOneWorker(String type) throws RemoteException {
		super(type);
	}

	public Warehouse work(Work work, List<Worker> workerList)
			throws RemoteException {
		Worker worker = workerList.get(Util.randomIndex(workerList.size()));
		List<Worker> workersActived = new ArrayList<Worker>();
		workersActived.add(worker);
		workerList.remove(worker);
		return dealWork(work, worker, workerList, workersActived);

	}

	private Warehouse dealWork(Work work, Worker worker,
			List<Worker> workerList, List<Worker> workersActived)
			throws RemoteException {
		try {
			logger.logMessage(LogLevel.DEBUG,"worker:{0}开始执行",worker.getId());
			Warehouse  w = worker.work(work);
			logger.logMessage(LogLevel.DEBUG,"worker:{0}执行完成",worker.getId());
			return w;
		} catch (Exception e) {
			logger.errorMessage("worker:{0}执行时出错", e, work.getId());
			if (workerList.size() == 0) {
				throw new PCRuntimeException(String.format("没有对应于work:%s的工人！",
						work.getType()),e);
			}
			return reAction(work, workerList, workersActived);
		}
	}

	private Warehouse reAction(Work work,List<Worker> workers, List<Worker> workersActived)
			throws RemoteException {
		logger.logMessage(LogLevel.DEBUG, "开始重新查找worker");
		Worker redoWorker = null;
		for (Worker worker : workers) {
			if (worker.acceptWork(work)) {
				logger.logMessage(LogLevel.DEBUG, "查找到worker:{0}",
						worker.getId());
				redoWorker = worker;
				workersActived.add(worker);
			}
		}
		if (redoWorker == null) {
			throw new PCRuntimeException(String.format("没有对应于%s的工人！",
					work.getType()));
		}
		workers.remove(redoWorker);
		return dealWork(work, redoWorker, workers, workersActived);
	}
}
