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
package org.tinygroup.tinydb.relation;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * 表间关系定义
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("relation-config")
public class Relation {
	public static final String MORE_TO_ONE = "n:1";
	public static final String ONE_TO_MORE = "1:n";
	public static final String LIST_MODE = "List";
	public static final String SET_MODE = "Set";
	public static final String ARRAY_MODE = "Array";

	@XStreamAsAttribute
    // 关系标识
	private String id;
	@XStreamAsAttribute
    // 类型
    private String type;
	@XStreamAsAttribute
	@XStreamAlias("collection-mode")
    // array,list,set
    private String collectionMode;
	//keyName是当前表中的外键字段
	@XStreamAsAttribute
	@XStreamAlias("key-name")
    // 外键属性，从表的关联名称，不一定与主表的主键名称相同
    private String keyName;
	//若父bean与当前bean关系是ontToMore，则此属性作为当前bean存在于父bean的key值
	@XStreamAlias("relation-key-name")
	@XStreamAsAttribute
    private String relationKeyName;
	//mainName是parent表中的主键字段
	@XStreamAsAttribute
    //主表关联名称，不一定是主表的主键名称
	@XStreamAlias("main-name")
    private String mainName;
	@XStreamAsAttribute
    private String mode;// 1..n,n..1
	@XStreamAlias("relations")
    // 子关系
    private List<Relation> relations;
    // 父亲
    private transient Relation parent;
	
	
	public Relation() {
		
	}

	public Relation(String id, String type, String collectionMode,
			String keyName, String mode) {
		super();
		this.id = id;
		this.type = type;
		this.collectionMode = collectionMode;
		this.keyName = keyName;
		this.mode = mode;
	}

	
	public Relation(String id, String type, String collectionMode,
			String keyName, String mode, List<Relation> relations) {
		super();
		this.id = id;
		this.type = type;
		this.collectionMode = collectionMode;
		this.keyName = keyName;
		this.mode = mode;
		addRelations(relations);
	}
	
	public Relation(String id, String type, String collectionMode,
			String keyName, String mode, Relation relation) {
		super();
		this.id = id;
		this.type = type;
		this.collectionMode = collectionMode;
		this.keyName = keyName;
		this.mode = mode;
		addRelation(relation);
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCollectionMode() {
		if (collectionMode == null) {
			collectionMode = LIST_MODE;
		}
		return collectionMode;
	}

	public void setCollectionMode(String collectionMode) {
		this.collectionMode = collectionMode;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<Relation> getRelations() {
		if(relations==null){
			relations=new ArrayList<Relation>();
		}
		return relations;
	}

	public void setRelations(List<Relation> relations) {
		addRelations(relations);
	}

	private void addRelations(List<Relation> relations) {
		for (Relation relation : relations) {
			addRelation(relation);
		}
	}

	private void addRelation(Relation relation) {
		if (relations == null) {
			relations = new ArrayList<Relation>();
		}
		relations.add(relation);
		relation.setParent(this);
	}

	public Relation getParent() {
		return parent;
	}

	public void setParent(Relation parent) {
		this.parent = parent;
	}

	public String getMainName() {
		return mainName;
	}

	public void setMainName(String mainName) {
		this.mainName = mainName;
	}

	public String getRelationKeyName() {
		return relationKeyName;
	}

	public void setRelationKeyName(String relationKeyName) {
		this.relationKeyName = relationKeyName;
	}
	
	
}
