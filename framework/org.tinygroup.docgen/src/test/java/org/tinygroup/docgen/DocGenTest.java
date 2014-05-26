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
package org.tinygroup.docgen;

import junit.framework.TestCase;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.docgen.util.ImageUtil;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinytestutil.AbstractTestUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class DocGenTest extends TestCase {

	private static DocumentGeneraterManager manager = null;

	protected void setUp() throws Exception {
		super.setUp();
		AbstractTestUtil.init(null, true);
		if (manager == null) {
			manager = SpringUtil
					.getBean(DocumentGeneraterManager.MANAGER_BEAN_NAME);
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 整体测试
	 * 
	 * @throws Exception
	 */
	public void testDocGenerate() throws Exception {
		File file = new File("test.xml");
		FileOutputStream outputStream = new FileOutputStream(file);
		Context context = new ContextImpl();
		String picData = ImageUtil.fileToBase64("src/test/resources/pic.jpg");
		context.put("picData", picData);
		manager.getFileGenerater("doc").generate("/test.docpage", context,
				new OutputStreamWriter(outputStream));
		outputStream.close();
		file.delete();
	}

	/**
	 * 书签，链接
	 * 
	 * @throws Exception
	 */
	public void testCommon() throws Exception {
		File file = new File("常用.xml");
		FileOutputStream outputStream = new FileOutputStream(file);
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		manager.getFileGenerater("doc").generate("/common.docpage",
				new ContextImpl(), writer);
		outputStream.close();
		file.delete();
	}

	/**
	 * 段落
	 * 
	 * @throws Exception
	 */
	public void testParagraph() throws Exception {
		File file = new File("段落.xml");
		FileOutputStream outputStream = new FileOutputStream(file);
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		manager.getFileGenerater("doc").generate("/paragraph.docpage",
				new ContextImpl(), writer);
		outputStream.close();
		file.delete();
	}

	/**
	 * 目录
	 * 
	 * @throws Exception
	 */
	public void testCatalogue() throws Exception {
		File file = new File("目录.xml");
		FileOutputStream outputStream = new FileOutputStream(file);
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		manager.getFileGenerater("doc").generate("/catalogue.docpage",
				new ContextImpl(), writer);
		outputStream.close();
		file.delete();
	}

	/**
	 * 图片
	 * 
	 * @throws Exception
	 */
	public void testPicture() throws Exception {
		File file = new File("图片.xml");
		FileOutputStream outputStream = new FileOutputStream(file);
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		Context context = new ContextImpl();
		context.put("imageUtil", ImageUtil.class);
		manager.getFileGenerater("doc").generate("/picture.docpage", context,
				writer);
		outputStream.close();
		file.delete();
	}

	/**
	 * 表格
	 * 
	 * @throws Exception
	 */
	public void testTable() throws Exception {
		File file = new File("表格.xml");
		FileOutputStream outputStream = new FileOutputStream(file);
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		manager.getFileGenerater("doc").generate("/table.docpage",
				new ContextImpl(), writer);
		outputStream.close();
		file.delete();
	}

	/**
	 * 项目标号
	 * 
	 * @throws Exception
	 */
	public void testBullets() throws Exception {
		File file = new File("项目标号.xml");
		FileOutputStream outputStream = new FileOutputStream(file);
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		manager.getFileGenerater("doc").generate("/bullets.docpage",
				new ContextImpl(), writer);
		outputStream.close();
		file.delete();
	}

	/**
	 * 页眉，页脚，页码
	 * 
	 * @throws Exception
	 */
	public void testPageHeader() throws Exception {
		File file = new File("页眉页脚.xml");
		FileOutputStream outputStream = new FileOutputStream(file);
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		manager.getFileGenerater("doc").generate("/pageHeaderTail.docpage",
				new ContextImpl(), writer);
		outputStream.close();
		file.delete();
	}

}
