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
package org.tinygroup.xmlparser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xmlparser.formatter.XmlFormater;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

public class ProcessPom {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		File root = new File("E:/SVN/tinyorg-code/trunk/Sources");
		processFolder(root);
	}

	private static void processFolder(File file) throws Exception {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				if (f.getName().equals("pom.xml")) {
					processPom(f);
				}
				if (f.isDirectory()) {
					processFolder(f);
				}
			}
		}

	}

	private static void processPom(File file) throws Exception {
		// System.out.println("begin process file:" + file.getAbsolutePath());
		// System.out.println(file.getAbsolutePath());
		XmlStringParser parser = new XmlStringParser();
		FileObject fileObject = VFS.resolveFile("file:" + file.getAbsolutePath());
		XmlDocument doc = parser.parse(IOUtils.readFromInputStream(fileObject.getInputStream(), "UTF-8"));
		XmlNode root = doc.getRoot();
		root.removeNode("version");
		root.removeNode("groupId");
		XmlNode parent = root.getSubNode("parent");
		if (parent != null) {
			parent.getSubNode("version").getSubNodes().get(0)
					.setContent("0.0.5-SNAPSHOT");
		}
		XmlNode dependencies = root.getSubNode("dependencies");
		if (dependencies != null) {
			List<XmlNode> subNodes = dependencies.getSubNodes("dependency");
			if (subNodes != null) {
				for (XmlNode dependency : subNodes) {
					if (dependency.getSubNode("groupId").getContent().trim()
							.equals("org.tinygroup")) {// 如果是tiny框架中的依赖
						dependency.getSubNode("version").getSubNodes().get(0)
								.setContent("${tinyVersion}");
					}
				}
			}
		}
		XmlFormater formater = new XmlFormater();
		// System.out.println(formater.format(root));
		FileOutputStream outputStream = new FileOutputStream(file);
		IOUtils.writeToOutputStream(outputStream, formater.format(root),
				"UTF-8");
		outputStream.close();
	}

}
