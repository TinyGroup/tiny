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
package org.tinygroup.tinydb.testcase.transaction.logic;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.operator.DBOperator;

public class AnimalLogic {
	
	private DBOperator<?> operator;
	
	public AnimalLogic(){
		
	}
    @Transactional
	public void insertBeanSuccess(Bean[] beans1) {
		getOperator().batchInsert(beans1);
	}
	
    @Transactional(propagation=Propagation.REQUIRES_NEW)
  	public void insertBeanWithRequiresNew(Bean[] beans1) {
  		getOperator().batchInsert(beans1);
  	}

	public void deleteBean(Bean[] beans1) {
		getOperator().batchDelete(beans1);
	}
	
	public DBOperator<?> getOperator() {
		return operator;
	}


	public void setOperator(DBOperator<?> operator) {
		this.operator = operator;
	}
	
}
