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
package org.tinygroup.weblayer.webcontext.cache;

import java.util.regex.Pattern;

import org.tinygroup.weblayer.WebContext;


/**
 * 
 * 功能说明:缓存映射信息对象 

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-23 <br>
 * <br>
 */
public class CacheMapping {

	private String patternStr;
	
	private String paramNames;
	
	private Long timeToLived;
	
	private Pattern pattern;
	
	private String[] names;

	public CacheMapping(String patternStr, String paramNames, Long timeToLived) {
		super();
		this.patternStr = patternStr;
		this.paramNames = paramNames;
		this.timeToLived = timeToLived;
		pattern=Pattern.compile(patternStr);
		if(paramNames!=null){
			names=paramNames.split(",");
		}
	}
	
	public String getCacheKey(String accessPath,WebContext webContext){
		StringBuffer cacheKey=new StringBuffer(accessPath);
		if(names!=null){
			if(names.length>0){
				cacheKey.append("?");
				for (int i = 0; i < names.length; i++) {
					String name = names[i];
					Object value= webContext.getRequest().getParameter(name);
					String paramValue="";
					if(value!=null){
						paramValue=String.valueOf(value);
					}
					cacheKey.append(name).append("=").append(paramValue);
					if(i!=names.length-1){
						cacheKey.append("&");
					}
				}
			}
			
		}
		return cacheKey.toString();
		
	}
	
	public boolean matches(String accessPath){
	      return pattern.matcher(accessPath).matches();
	}
	

	public String getPatternStr() {
		return patternStr;
	}

	public void setPatternStr(String patternStr) {
		this.patternStr = patternStr;
	}

	public String getParamNames() {
		return paramNames;
	}

	public void setParamNames(String paramNames) {
		this.paramNames = paramNames;
	}

	public Long getTimeToLived() {
		return timeToLived;
	}

	public void setTimeToLived(Long timeToLived) {
		this.timeToLived = timeToLived;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	
	
}
