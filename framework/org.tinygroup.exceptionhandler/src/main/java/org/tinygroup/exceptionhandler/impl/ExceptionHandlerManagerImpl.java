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
package org.tinygroup.exceptionhandler.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.event.Event;
import org.tinygroup.exceptionhandler.ExceptionHandler;
import org.tinygroup.exceptionhandler.ExceptionHandlerManager;

public class ExceptionHandlerManagerImpl implements ExceptionHandlerManager {
	private Map<String, ExceptionHandler> handlerNameMap = new HashMap<String, ExceptionHandler>();
	private Map<Class<?>, ExceptionHandler> handlerMap = new HashMap<Class<?>, ExceptionHandler>();
	private List<Class<?>> exceptionList = new ArrayList<Class<?>>();

	public void addHandler(String exception, ExceptionHandler handler) throws ClassNotFoundException {
		if (handlerNameMap.containsKey(exception)) {
			return;
		}
		handlerNameMap.put(exception, handler);
		Class<?> exceptionClass = Class.forName(exception);
		exceptionList.add(exceptionClass);
		handlerMap.put(exceptionClass, handler);

	}

	public boolean handle(Throwable e,Event event) {
		Class<?> exceptionClass = e.getClass();
		int index = exceptionList.indexOf(exceptionClass);
		if(index!=-1){
			handlerMap.get( exceptionList.get(index) ).handle(e,event);
			return true;
		}else{
			for(int i=0;i<exceptionList.size();i++){
				Class<?> clazz = exceptionList.get(i);
				if( implmentInterface(exceptionClass, clazz) ){
					handlerMap.get(clazz).handle(e,event);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean handleWithAllHandler(Throwable e,Event event) {
		Class<?> exceptionClass = e.getClass();
		boolean flag = false;
		for(int i=0;i<exceptionList.size();i++){
			Class<?> clazz = exceptionList.get(i);
			if( implmentInterface(exceptionClass, clazz) ){
				handlerMap.get(clazz).handle(e,event);
				flag =true;
			}
		}
		return flag;
	}
	private boolean implmentInterface(Class<?> clazz, Class<?> interfaceClazz) {
		return interfaceClazz.isAssignableFrom(clazz);
	}
}
