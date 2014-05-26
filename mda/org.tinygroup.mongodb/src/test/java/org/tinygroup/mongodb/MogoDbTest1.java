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
package org.tinygroup.mongodb;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.MongoClient;

public class MogoDbTest1 {

	/**
	 * @param args
	 * @throws Throwable
	 */
	public static void main(String[] args) throws Throwable {
		MongoClient connection = new MongoClient();
		DB db = connection.getDB("test");
		DBCollection collection = db.getCollection("p");
		DBObject query = new BasicDBObject();
		query.put("_id", new ObjectId("528c53aa5f4089467c5dd15a"));
		for (DBObject object : collection.find(query).toArray()) {
			System.out.println(object.toString());
			DBRef dbref = new DBRef(db, "p", object.get("p_id"));
			DBObject refObj=dbref.fetch();
			System.out.println(refObj.toString());
		}
		connection.close();

	}

}
