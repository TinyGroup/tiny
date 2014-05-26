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
package org.tinygroup.pageflowbasiccomponent;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tinygroup.context.Context;
import org.tinygroup.convert.objectjson.jackson.ObjectToJson;
import org.tinygroup.weblayer.WebContext;

public class WriteJsonComponent extends AbstractWriteComponent  {
	
	private ObjectToJson<Object> objectToJson = new ObjectToJson<Object>(
			JsonSerialize.Inclusion.NON_NULL);

	public void execute(Context context) {
		WebContext webContext=(WebContext)context;
		try {
			webContext.getResponse().getWriter().write(objectToJson.convert(context.get(resultKey)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
