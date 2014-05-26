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
package org.tinygroup.fileresolver;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.tinygroup.fileresolver.impl.FullContextFileFinder;
import org.tinygroup.fileresolver.impl.FullContextFileRepositoryImpl;
import org.tinygroup.parser.Document;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlParser;
import org.tinygroup.xmlparser.parser.XmlStringParser;

/**
 * 
 * 功能说明:  FullContentFileResposity接口测试用例

 * 开发人员: renhui <br>
 * 开发时间: 2013-2-22 <br>
 * <br>
 */
public class FullContentFileResposityTest extends TestCase {
	
	private static final String FILE_PATH = "/org/tinygroup/fileresolver/FileResolver.class";

	private static FullContextFileRepository repository;
	
	 private static boolean hasInited;
	 
	 
	  void init(){
		 repository=new  FullContextFileRepositoryImpl();
		  FileResolver fileResolver=FileResolverFactory.getFileResolver();
		  FullContextFileFinder finder=new FullContextFileFinder();
		  XmlParser<String> parse=new XmlStringParser();
		  Document<XmlNode> document=parse.parse("<full-context-file-finder>" +
		  		"<file ext-name=\"class\" content-type=\"application/test\" />" +
		  		"</full-context-file-finder>");
		  finder.config(document.getRoot(),null);
		  finder.setFullContextFileRepository(repository);
		  fileResolver.addFileProcessor(finder);
		  fileResolver.resolve();
	  }
	
	
	
	
	protected void setUp() throws Exception {
		if(!hasInited){
			init();
			hasInited=true;
		}
		
	}

	
	protected void tearDown() throws Exception {

	}


	public void testAddSearchPath() throws IOException {
		FileObject fileObject=repository.getFileObject("test.xml");
		assertTrue(fileObject==null);
		File director=new File("c:/test");
		if(!director.exists()){
			director.mkdirs();
		}
		File testFile=new File("c:/test/test.xml");
		if(!testFile.exists()){
			testFile.createNewFile();
		}
		repository.addSearchPath("c:/test");
		
		fileObject=repository.getFileObject("/test.xml");
		assertTrue(fileObject!=null);
		
		
	}

	public void testGetFileObject() {
	    FileObject fileObject= repository.getFileObject(FILE_PATH);
	    assertEquals("FileResolver.class", fileObject.getFileName());
	}

	public void testRemoveFileObject() {
	    FileObject beforeRemove= repository.getFileObject(FILE_PATH);
	    assertTrue(beforeRemove!=null);
		repository.removeFileObject(FILE_PATH);
		 FileObject afterRemove= repository.getFileObject(FILE_PATH);
		 repository.addFileObject(FILE_PATH, beforeRemove);
		 assertTrue(afterRemove!=null);
	}


	public void testGetFileContentType() {
		 assertEquals("application/test",repository.getFileContentType("class"));
	}

	public void testGetRootFileObject() {
		FileObject fileObject= repository.getRootFileObject(FILE_PATH);
		assertTrue(fileObject.getAbsolutePath().endsWith("classes"));
	}



}
