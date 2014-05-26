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

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinypc.*;

import java.rmi.RemoteException;

/**
 * 本地包工头
 * Created by luoguo on 14-1-8.
 */
public abstract class AbstractForeman implements Foreman {
    /**
     *
     */
    private static final long serialVersionUID = -8812858260392860166L;
    private WorkQueue workQueue;
    private final String type;
    private String id;
    private WorkStatus workStatus = WorkStatus.WAITING;
    protected volatile boolean cancel = false;
    protected transient static Logger logger = LoggerFactory.getLogger(AbstractForeman.class);
    private WorkCombiner workCombiner;
    private WorkSplitter workSplitter;

    public WorkCombiner getWorkCombiner() {
        return workCombiner;
    }

    public void setWorkCombiner(WorkCombiner workCombiner) {
        this.workCombiner = workCombiner;
    }

    public WorkSplitter getWorkSplitter() {
        return workSplitter;
    }

    public void setWorkSplitter(WorkSplitter workSplitter) {
        this.workSplitter = workSplitter;
    }

    public WorkQueue getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(WorkQueue workQueue) {
        this.workQueue = workQueue;
    }


    public AbstractForeman(String type) throws RemoteException {
        this.id = Util.getUuid();
        this.type = type;
    }

    public String getType() {
        return type;
    }


    public WorkStatus getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(WorkStatus workStatus) {
        this.workStatus = workStatus;
    }

    public String getId() {
        return id;
    }
}

