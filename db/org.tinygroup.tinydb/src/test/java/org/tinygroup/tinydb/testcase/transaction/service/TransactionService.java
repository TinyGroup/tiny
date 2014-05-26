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
package org.tinygroup.tinydb.testcase.transaction.service;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.testcase.transaction.logic.AnimalLogic;
import org.tinygroup.tinydb.testcase.transaction.logic.BranchLogic;

/**
 * 
 * 功能说明: 事务测试服务

 * 开发人员: renhui <br>
 * 开发时间: 2013-7-31 <br>
 * <br>
 */
public class TransactionService {
	
	private AnimalLogic animalLogic;
	
	private BranchLogic branchLogic;
	
	public AnimalLogic getAnimalLogic() {
		return animalLogic;
	}
	public void setAnimalLogic(AnimalLogic animalLogic) {
		this.animalLogic = animalLogic;
	}
	public BranchLogic getBranchLogic() {
		return branchLogic;
	}
	public void setBranchLogic(BranchLogic branchLogic) {
		this.branchLogic = branchLogic;
	}
	/**
	 * 
	 * 事务成功测试服务
	 * @param animals
	 * @param branchs
	 */
	public void transactionSuccess(Bean[] animals,Bean[] branchs){
		  animalLogic.insertBeanSuccess(animals);
		  branchLogic.insertBeanSuccess(branchs);
	}
	/**
	 * 
	 * 独立事务测试服务，场景：logic1开启独立事务，新增一条记录，logic不开启独立事务，新增失败
	 * @param animals
	 * @param branchs
	 */
	public void independentTransaction(Bean[] animals,Bean[] branchs){
		  animalLogic.insertBeanWithRequiresNew(animals);
		  branchLogic.insertBeanFaiure(branchs);
	}
	/**
	 * 
	 * 场景：logic、logic都不开启独立事务，logic2操作失败，记录都回滚
	 * @param animals
	 * @param branchs
	 */
	public void transactionFailure(Bean[] animals,Bean[] branchs){
		  animalLogic.insertBeanSuccess(animals);
		  branchLogic.insertBeanFaiure(branchs);
	}
	

	public void deleteBean(Bean[] beans1, Bean[] beans2) {
		 animalLogic.deleteBean(beans1);
		 branchLogic.deleteBean(beans2);
	}
	
}
