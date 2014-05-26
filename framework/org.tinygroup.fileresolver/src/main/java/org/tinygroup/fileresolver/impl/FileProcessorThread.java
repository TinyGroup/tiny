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
package org.tinygroup.fileresolver.impl;

import java.util.List;

import org.tinygroup.fileresolver.ProcessorCallBack;
import org.tinygroup.threadgroup.AbstractProcessor;
import org.tinygroup.vfs.FileObject;

public  class FileProcessorThread extends AbstractProcessor{

	private List<FileObject> fileObjects;
	
	private ProcessorCallBack callBack;

	public FileProcessorThread(String name) {
		super(name);
	}

	public FileProcessorThread(String name, List<FileObject> fileObjects) {
		super(name);
		this.fileObjects = fileObjects;
	}

	
	protected void action() throws Exception {
		if (fileObjects != null) {

			for (FileObject fileObject : fileObjects) {
				if(callBack!=null){
				    callBack.callBack(fileObject);	
				}
			}
		}

	}

	public void setCallBack(ProcessorCallBack callBack) {
		this.callBack = callBack;
	}
	

}
