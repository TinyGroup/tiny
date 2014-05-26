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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 
 * 功能说明: 文件搜索器接口测试用例
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-2-21 <br>
 * <br>
 */
public class FileResolverTest extends TestCase {

	private FileResolver fileResolver;

	
	protected void setUp() throws Exception {
		fileResolver = FileResolverFactory.getFileResolver();
	}

	
	protected void tearDown() throws Exception {
		fileResolver = null;
	}

	public void testResolve() {
		TestFileProcess process = new TestFileProcess();
		fileResolver.addFileProcessor(process);
		fileResolver.resolve();
		assertEquals(true, process.exist());
		assertEquals(2, process.fileSize());
	}

	public void testAddSkipPathResolver() {

		// fileResolver.addSkipPathPattern(".*/skip/");
		// TestFileProcess process = new TestFileProcess();
		// fileResolver.addFileProcessor(process);
		// fileResolver.resolve();
		// assertEquals(1, process.fileSize());

	}

	class TestFileProcess implements FileProcessor {

		private List<FileObject> files = new ArrayList<FileObject>();

		public void process() {

		}

		public void noChange(FileObject fileObject) {
			files.add(fileObject);
		}

		public void modify(FileObject fileObject) {
			files.add(fileObject);
		}

		public boolean isMatch(FileObject fileObject) {

			return fileObject.getAbsolutePath().indexOf("test-classes") > 0
					&& fileObject.getFileName().equalsIgnoreCase("test.xml");

		}

		public void delete(FileObject fileObject) {
		}

		public void clean() {
			files.clear();
		}

		public void add(FileObject fileObject) {
			files.add(fileObject);
		}

		boolean exist() {
			return files.size() > 0 ? true : false;
		}

		int fileSize() {
			return files.size();
		}

		public void setFileResolver(FileResolver fileResolver) {

		}

		public boolean supportRefresh() {

			return false;
		}

		public String getApplicationNodePath() {
			return null;
		}

		public String getComponentConfigPath() {
			return null;
		}

		public void config(XmlNode applicationConfig, XmlNode componentConfig) {

		}

		public XmlNode getComponentConfig() {
			return null;
		}

		public XmlNode getApplicationConfig() {
			return null;
		}

		public int getOrder() {
			return 0;
		}
	}
}
