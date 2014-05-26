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

import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.tinypc.Warehouse;

/**
 * Created by luoguo on 14-1-8.
 */
public class WarehouseDefault extends ContextImpl implements Warehouse {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3551570264489045287L;

	public void putSubWarehouse(Warehouse warehouse) {
        this.putSubContext(Util.getUuid(), warehouse);
    }
}
