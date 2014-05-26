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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.commons.namestrategy.impl.CamelCaseStrategy;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.Native2AsciiUtils;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.parser.filter.FastNameFilter;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xmlparser.formatter.XmlFormater;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

/**
 * 按模块导出
 * 
 * @author luoguo
 * 
 */
public class MetadataGeneratePDM {
	static String packageName = "org.tinygroup";
	Map<String, String> stdFieldsMap = new HashMap<String, String>();
	Map<String, String> i18nMap = new HashMap<String, String>();

	static Map<String, String> typeMap = new HashMap<String, String>();
	static Map<String, HashSet<String>> customTypeMap = new HashMap<String, HashSet<String>>();
	
	CamelCaseStrategy camelCaseStrategy=new CamelCaseStrategy();

	static XmlDocument doc = null;
	// static String outputFolder =
	// "E:/SVN/Open/Source/test/bubiz/src/main/resources/luoguo/";
	static String outputFolder = "c:/out/";
	static String basePackagePath = "aa.bb";
	static {
		typeMap.put("SMALLINT", "SmallInt");
		typeMap.put("MEDIUMINT", "MediumInt");
		typeMap.put("VARCHAR", "Varchar");
		typeMap.put("FLOAT", "Float");
		typeMap.put("TINYINT", "TinyInt");
		typeMap.put("TEXT", "Text");
		typeMap.put("ENUM", "TinyText");
		typeMap.put("DATETIME", "Datetime");
		typeMap.put("DATE", "Date");
		typeMap.put("CHAR", "Varchar");
		typeMap.put("INTEGER", "Integer");
		typeMap.put("BOOL", "TinyInt");
		typeMap.put("BIGINT", "BigInt");
		typeMap.put("DECIMAL", "Decimal");
		typeMap.put("TIMESTAMP", "Timestamp");
		typeMap.put("BLOB", "Blob");
		typeMap.put("CLOB", "Clob");
		typeMap.put("INT", "Int");
		typeMap.put("DOUBLE", "Double");
	}

	public static void main(String[] args) throws Exception {
		File file1 = new File(
				"C://Users//renhui.HS//Desktop//demo.pdm");
		System.out.println(file1.getAbsolutePath());
		XmlStringParser parser = new XmlStringParser();
		FileObject file = VFS.resolveFile("file:" + file1.getAbsolutePath());
		doc = parser.parse(IOUtils.readFromInputStream(file.getInputStream(), "UTF-8"));

		MetadataGeneratePDM mg = new MetadataGeneratePDM();
		mg.export("all");
		mg.exportCustomType();
	}

	private void exportCustomType() throws Exception {
		StringBuffer buf = new StringBuffer("<business-types name=\""
				+ basePackagePath + "\" title=\"业务类型\" package-name=\""
				+ basePackagePath + "\">");
		for (String type : customTypeMap.keySet()) {
			HashSet<String> lengthSet = customTypeMap.get(type);
			for (String length : lengthSet) {
				type = type.toUpperCase();
				if ("NUMERIC".equals(type) || "NUMBER".endsWith(type)) {
					if (length.indexOf(",") != -1) {
						String[] lens = length.split(",");
						String len = lens[0];
						String scale = lens[1];
						String type2 = getNumberType(type, len, scale);

						buf.append("<business-type id=\"" + typeMap.get(type2)
								+ len + "\" name=\"" + type2 + "\" title=\""
								+ type2 + "\" standard-type-id=\"type_"
								+ typeMap.get(type2).toLowerCase() + "\">");
						buf.append("<placeholder-value-list>");
						buf.append("<placeholder-value name=\"precision\" value=\""
								+ len + "\" />");
						buf.append("<placeholder-value name=\"scale\" value=\""
								+ scale + "\" />");
						buf.append("</placeholder-value-list>");
						buf.append("</business-type>");
					} else {
						String type2 = getNumberType(type, length, "0");
						buf.append("<business-type id=\"" + typeMap.get(type2)
								+ length + "\" name=\"" + type2 + length
								+ "\" title=\"" + type2 + length
								+ "\" standard-type-id=\"type_"
								+ typeMap.get(type2).toLowerCase() + "\">");
						buf.append("<placeholder-value-list>");
						buf.append("<placeholder-value name=\"length\" value=\""
								+ length + "\" />");
						buf.append("</placeholder-value-list>");
						buf.append("</business-type>");
					}
				} else {
					buf.append("<business-type id=\"" + typeMap.get(type)
							+ length + "\" name=\"" + type + length
							+ "\" title=\"" + type + length
							+ "\" standard-type-id=\"type_"
							+ typeMap.get(type).toLowerCase() + "\">");
					if (!"".equals(length)) { // 如果该业务类型不存在length，则不用输出placeholder标签
						buf.append("<placeholder-value-list>");
						buf.append("<placeholder-value name=\"length\" value=\""
								+ length + "\" />");
						buf.append("</placeholder-value-list>");
					}
					buf.append("</business-type>");
				}
			}
		}
		buf.append("</business-types");
		writeBuf(buf.toString(), outputFolder
				+ "businessdatatype.bizdatatype.xml");
	}

	public void export(String moduleName) throws Exception {
		exportModule(moduleName);
		exportStdField(moduleName);
		exportI18nField(moduleName);
	}

	private void exportI18nField(String module) throws Exception {
		StringBuffer buf = new StringBuffer();
		for (String key : i18nMap.keySet()) {
			String value = i18nMap.get(key);
			key = getLowerCase(key);
			buf.append(String.format("%s=%s\n", key,
					Native2AsciiUtils.native2Ascii(value)));
		}

		writeBuf(buf.toString(), outputFolder + "i18n/" + module
				+ "_zn_CN.properties");
	}

	private void exportStdField(String module) throws Exception {
		StringBuffer buf = new StringBuffer("<standard-fields package-name=\""
				+ packageName + "." + module.toLowerCase() + "\">\n");
		for (String key : stdFieldsMap.keySet()) {
			String[] string = stdFieldsMap.get(key).split(",");
			// System.out.println(string[1]);
			key = getLowerCase(key);
			buf.append(String
					.format("<standard-field id=\"%s\" name=\"%s\" title=\"%s\" business-type-id=\"%s\" />\n",
							key, key, string[0], string[1]));
		}
		buf.append("</standard-fields>\n");

		writeBuf(buf.toString(), outputFolder + module + ".stdfield.xml");
	}

	/**
	 * 导出模块
	 * 
	 * @param moduleName
	 * @throws FileSystemException
	 * @throws Exception
	 */
	private void exportModule(String moduleName) throws Exception {
		FastNameFilter<XmlNode> fast = new FastNameFilter<XmlNode>(
				doc.getRoot());
		List<XmlNode> elementList = fast.findNodeList("o:Model");
		// String moduleId = getModuleId(doc.getRoot(), moduleName);
		for (XmlNode element : elementList) {
			// 获取模块列表
			XmlNode packages = element.getSubNode("c:Packages");
			if(packages!=null){
				exportPackages(packages);
			}else{
				String module_name = element.getSubNode("a:Code").getContent(); // 模块名称
				exportTables(element, module_name);
			}
			
		}
	}
	
	
	private void exportPackages(XmlNode node)throws Exception{
	   if(node!=null){
			List<XmlNode> packages=node.getSubNodes("o:Package");
			if(!CollectionUtil.isEmpty(packages)){
				for (XmlNode packageNode : packages) {
					String module_name = packageNode.getSubNode("a:Code").getContent(); // 模块名称
					XmlNode packagesNode=packageNode.getSubNode("c:Packages");
					if(packagesNode!=null){
						exportPackages(packagesNode);
					}else{
						exportTables(packageNode, module_name);
					}
				}
			}
			
	   }
		
	}

	private void exportTables(XmlNode xmlNode, String module_name)
			throws Exception {
		List<XmlNode> tableNodes = xmlNode.getSubNode("c:Tables")
		.getSubNodes("o:Table"); // 包下的表节点
		if(!CollectionUtil.isEmpty(tableNodes)){
			for (XmlNode table : tableNodes) {
				exportTable(module_name, null, table, outputFolder);
			}
		}
	}

	private static String getModuleId(XmlNode root, String moduleName) {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(root);
		Map<String, String> atts = new HashMap<String, String>();
		atts.put("name", moduleName);
		atts.put("xmi:type", "uml:Package");
		filter.setIncludeAttribute(atts);
		XmlNode node = filter.findNode("element");
		return node.getAttribute("xmi:idref");
	}

	private void exportTable(String moduleName, XmlNode root,
			XmlNode tableNode, String outputFolder) throws Exception {
		StringBuffer buf = new StringBuffer();
		String tableName = tableNode.getSubNode("a:Code").getContent();
		String cname = tableNode.getSubNode("a:Name").getContent() == null ? ""
				: tableNode.getSubNode("a:Name").getContent();
		i18nMap.put(tableName, cname);
		buf.append(String
				.format("<entity-model id=\"%s\" name=\"%s\" title=\"%s\" ignore=\"false\" description=\"%s\" enable-delete=\"true\" enable-modity=\"true\" version=\"2.0\" cache-enabled=\"true\" package-name=\"%s\">",
						camelCaseStrategy.getPropertyName(tableName), tableName, cname, cname, packageName + "."
								+ moduleName.toLowerCase()));
		processAtrribute(root, tableNode, buf);
		printOperations(root, tableNode, buf);
		buf.append("</entity-model>");
		File folder = new File(outputFolder + moduleName);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		String fileName = outputFolder + moduleName + File.separator
				+ tableName + ".entity.xml";
		writeBuf(buf.toString(), fileName);
	}

	private static void writeBuf(String buf, String fileName) throws Exception {
		System.out.println("write to file: " + fileName);
		File file = new File(fileName).getParentFile();
		if (!file.exists()) {
			file.mkdirs();
		}
		FileOutputStream outputStream = new FileOutputStream(fileName);
		if (fileName.endsWith(".xml")) {
			outputStream.write(formatXml(buf.toString()).getBytes("UTF-8"));// utf8的方式保存文件（即文件时UTF-8的，即里面的文字就是UTF-8的）
		} else {
			outputStream.write(buf.toString().getBytes("UNICODE"));// utf8的方式保存文件（即文件时UTF-8的，即里面的文字就是UTF-8的）
		}
		outputStream.close();
	}

	private static String formatXml(String string) {
		XmlFormater f = new XmlFormater();
		XmlDocument doc = new XmlStringParser().parse(string);
		return f.format(doc);

	}

	private void processAtrribute(XmlNode root, XmlNode tableNode,
			StringBuffer buf) {
		buf.append("<groups>");
		buf.append("<group id=\"basicgroup\" name=\"basicgroup\" title=\"基本信息\" description=\"基本信息\" enable-delete=\"true\" enable-modity=\"true\">");

		List<XmlNode> nodeList = tableNode.getSubNode("c:Columns").getSubNodes(
				"o:Column");
		for (XmlNode att : nodeList) {

			String col_id = att.getAttribute("Id");
			String type = att.getSubNode("a:DataType").getContent();
			String length = att.getSubNode("a:Length") == null ? null : att
					.getSubNode("a:Length").getContent();
			String precision = att.getSubNode("a:Precision") == null ? "0"
					: att.getSubNode("a:Precision").getContent();
			if (type != null && type.indexOf("2") != -1) {
				type = type.substring(0, type.indexOf("2"));
			}
			if (type != null && type.indexOf("(") != -1) {
				type = type.substring(0, type.indexOf("("));
			}
			if (length != null && !length.equals("0")) {
				if (precision != null && !"0".equals(precision)) { // 形如number(3,2);
					HashSet<String> set = customTypeMap.get("NUMBER");// DECIMAL
					String len = length + "," + precision;
					if (set == null) {
						set = new HashSet<String>();
						customTypeMap.put(type, set);
					}
					if (!set.contains(len)) {
						set.add(len);
					}
				} else {
					HashSet<String> set = customTypeMap.get(type);
					if (set == null) {
						set = new HashSet<String>();
						customTypeMap.put(type, set);
					}
					if (!set.contains(length)) {
						set.add(length);
					}
				}
			} else {
				length = "";
				HashSet<String> set = customTypeMap.get(type);
				if (set == null) {
					set = new HashSet<String>();
					customTypeMap.put(type, set);
				}
				if (!set.contains(length)) {
					set.add(length);
				}
			}

			// XmlNode style = att.getSubNode("style");
			String cname = att.getSubNode("a:Name").getContent();
			// if (style != null) {
			// cname = style.getAttribute("value");
			// }
			if (cname == null) {
				cname = "";
			}
			// String name = att.getAttribute("name");
			String name = att.getSubNode("a:Code").getContent();
			name = getLowerCase(name);
			if (type == null) {
				System.out.println("Error:" + name);
				continue;
			}
			boolean primary = isPrimary(null, tableNode, col_id);
			// boolean unique = isUnique(null, tableNode, name);
			boolean unique = primary;
			buf.append(String
					.format("<field standard-field-id=\"%s\" primary=\"%s\"  unique=\"%s\" display=\"false\" id=\"%s_uuid\" table-field=\"true\" />",
							name, primary, unique, name));
			i18nMap.put(name, cname);
			if (length.length() == 0) {
				stdFieldsMap.put(name,
						cname + "," + getBizType(type, null, null));
			} else {
				if ("DECIMAL8".equals(getBizType(type, length, precision))) {
					System.out.println("DECIMAL8");
				}
				stdFieldsMap.put(name,
						cname + "," + getBizType(type, length, precision));
			}
		}
		buf.append("</group>");
		buf.append("</groups>");
	}

	private static String getBizType(String type, String length,
			String precision) {
		String string = typeMap.get(type.toUpperCase());
		if ("NUMBER".equals(type.toUpperCase())
				|| "NUMERIC".equals(type.toUpperCase())) {
			string = typeMap.get(getNumberType(type, length, precision));
		}
		if (string == null) {
			throw new RuntimeException("mistake type:" + type);
		}
		if (length == null) {
			return string;
		} else {
			return string + length;
		}
	}

	private static String getNumberType(String type, String length,
			String precision) {
		String string = "";
		if ("0".equals(precision)) {// 整数
		    if(StringUtil.isBlank(length)){
		    	string = "BIGINT";
		    }else{
		    	if (Integer.parseInt(length) <= 3) {
					string = "TINYINT";
				} else if (Integer.parseInt(length) > 3
						&& Integer.parseInt(length) <= 5) {
					string = "SMALLINT";
				} else if (Integer.parseInt(length) > 5
						&& Integer.parseInt(length) <= 8) {
					string = "MEDIUMINT";
				} else {
					string = "BIGINT";
				}
		    }
			
		} else {
			string = "DECIMAL";
		}
		return string;
	}

	private static String getLowerCase(String str) {
		str = str.replaceAll("_ID", "Id");
		str = str.replaceAll("ID", "Id");
		StringBuffer sb = new StringBuffer();
		String[] strArray = str.split("_");
		for (int i = 0; i < strArray.length; i++) {
			if (i > 0) {
				sb.append("_");
			}
			String word = strArray[i];
			if (word.equalsIgnoreCase("id")) {
				sb.append(word.toLowerCase());
			} else {
				for (int j = 0; j < word.length(); j++) {
					char ch = word.charAt(j);
					if (ch >= 'A' && ch <= 'Z') {
						if (j != 0) {
							sb.append("_");
						}
						sb.append((char) (ch - 'A' + 'a'));
					} else {
						sb.append(ch);
					}
				}
			}
		}
		return sb.toString();
	}

	private static boolean isPrimary(XmlNode root, XmlNode tableNode,
			String colId) {
		List<XmlNode> keys = tableNode.getSubNode("c:Keys") == null ? null
				: tableNode.getSubNode("c:Keys").getSubNodes("o:Key");
		List<XmlNode> primaryKeys = tableNode.getSubNode("c:PrimaryKey") == null ? null
				: tableNode.getSubNode("c:PrimaryKey").getSubNodes("o:Key");
		if (keys != null) {
			for (XmlNode key : keys) {
				String keyid = key.getAttribute("Id");
				List<XmlNode> key_cols = key.getSubNode("c:Key.Columns") == null ? null
						: key.getSubNode("c:Key.Columns").getSubNodes(
								"o:Column");
				if (key_cols != null) {
					for (XmlNode key_col : key_cols) {
						String col_id = key_col.getAttribute("Ref");
						if (col_id != null && col_id.equals(colId)) {
							// 该列是一各key，还需要验证是否为primarykey
							if (primaryKeys != null) {
								for (XmlNode pk : primaryKeys) {
									String pk_id = pk.getAttribute("Ref");
									if (pk_id != null && pk_id.equals(keyid)) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	private static boolean isUnique(XmlNode root, XmlNode element, String name) {
		XmlNode operations = element.getSubNode("operations");
		if (operations != null) {
			List<XmlNode> nodeList = operations.getSubNodes("operation");
			for (XmlNode operation : nodeList) {
				XmlNode stereotype = operation.getSubNode("stereotype");
				if (stereotype != null) {
					if (stereotype.getAttribute("stereotype").equals("unique")) {
						NameFilter<XmlNode> filter = new NameFilter<XmlNode>(
								root);
						filter.setIncludeAttribute("xmi:id",
								operation.getAttribute("xmi:idref"));
						XmlNode node = filter.findNode("ownedOperation");
						List<XmlNode> parameterList = node
								.getSubNodes("ownedParameter");
						if (parameterList != null
								&& parameterList.size() == 1
								&& getLowerCase(
										parameterList.get(0).getAttribute(
												"name")).equals(name)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private static void printOperations(XmlNode root, XmlNode element,
			StringBuffer buf) {
		XmlNode operations = element.getSubNode("operations");
		if (operations != null) {
			List<XmlNode> nodeList = operations.getSubNodes("operation");
			for (XmlNode operation : nodeList) {
				XmlNode stereotype = operation.getSubNode("stereotype");
				if (stereotype != null) {
					if (stereotype.getAttribute("stereotype").equals("PK")) {
						printPK(root, element, operation);
					} else if (stereotype.getAttribute("stereotype").equals(
							"unique")) {
						System.out.println(getUnique(root, element, operation));
					} else if (stereotype.getAttribute("stereotype").equals(
							"index")) {
						System.out.println(getIndex(root, element, operation));
					}
				}
			}
		}
	}

	private static void printPK(XmlNode root, XmlNode element, XmlNode operation) {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(root);
		filter.setIncludeAttribute("xmi:id",
				operation.getAttribute("xmi:idref"));
		XmlNode node = filter.findNode("ownedOperation");
		getParameters(node);
	}

	private static String getUnique(XmlNode root, XmlNode element,
			XmlNode operation) {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(root);
		filter.setIncludeAttribute("xmi:id",
				operation.getAttribute("xmi:idref"));
		XmlNode node = filter.findNode("ownedOperation");
		return getParameters(node);
	}

	private static String getIndex(XmlNode root, XmlNode element,
			XmlNode operation) {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(root);
		filter.setIncludeAttribute("xmi:id",
				operation.getAttribute("xmi:idref"));
		XmlNode node = filter.findNode("ownedOperation");
		return getParameters(node);
	}

	private static String getParameters(XmlNode node) {
		StringBuffer sb = new StringBuffer();
		List<XmlNode> parameterList = node.getSubNodes("ownedParameter");
		if (parameterList != null) {
			for (XmlNode para : parameterList) {
				if (sb.length() > 0) {
					sb.append(",");
					sb.append(para.getAttribute("name"));
				}
			}
		}
		return sb.toString();
	}
}
