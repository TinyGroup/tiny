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
package org.tinygroup.vfs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtils {

	/**
	 * list files in the given directory and subdirs (with recursion)
	 * 
	 * @param paths
	 * @return
	 */
	public static List<File> getFiles(String paths) {
		List<File> filesList = new ArrayList<File>();
		for (final String path : paths.split(File.pathSeparator)) {
			final File file = new File(path);
			if (file.isDirectory()) {
				recurse(filesList, file);
			} else {
				filesList.add(file);
			}
		}
		return filesList;
	}

	private static void recurse(List<File> filesList, File f) {
		File list[] = f.listFiles();
		for (File file : list) {
			if (file.isDirectory()) {
				recurse(filesList, file);
			} else {
				filesList.add(file);
			}
		}
	}

	/**
	 * List the content of the given jar
	 * 
	 * @param jarPath
	 * @return
	 * @throws IOException
	 */
	public static List<String> getJarContent(String jarPath) throws IOException {
		List<String> content = new ArrayList<String>();
		JarFile jarFile = new JarFile(jarPath);
		Enumeration<JarEntry> e = jarFile.entries();
		while (e.hasMoreElements()) {
			JarEntry entry = (JarEntry) e.nextElement();
			String name = entry.getName();
			if (name.endsWith("jquery-1.2.6.min.js")) {
				readEntry(jarFile, entry);
			}
			content.add(name);
		}
		return content;
	}

	private static void readEntry(JarFile jarFile, JarEntry entry) {
		try {
			InputStream stream = jarFile.getInputStream(entry);
			BufferedInputStream bis = new BufferedInputStream(stream);
			byte[] buffer = new byte[stream.available()];
			bis.read(buffer);
			bis.close();
			stream.close();
			System.out.println(new String(buffer, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {
		for (String s : getJarContent("E:/SVN/Open/Source/test/tinyuicomponent/target/tinyuicomponent-0.0.1-SNAPSHOT.jar")) {
			System.out.println(s);
		}
	}

	public static void printInfo(FileObject fileObject) {
		System.out.println("schemaProvider=" + fileObject.getSchemaProvider());
		System.out.println("url=" + fileObject.getURL());
		System.out.println("absolutePath=" + fileObject.getAbsolutePath());
		System.out.println("path=" + fileObject.getPath());
		System.out.println("fileName=" + fileObject.getFileName());
		System.out.println("extName=" + fileObject.getExtName());
		System.out.println("isFolder=" + fileObject.isFolder());
		System.out.println("isInPackage=" + fileObject.isInPackage());
		System.out.println("isExist=" + fileObject.isExist());
		System.out.println("size=" + fileObject.getSize());
		System.out.println("lastModifiedTime="
				+ fileObject.getLastModifiedTime());
		System.out.println("parent=" + fileObject.getParent());
		System.out.println("inputStream=" + fileObject.getInputStream());
		System.out.println("outputStream=" + fileObject.getOutputStream());
		System.out.println("---------------------------------------------");
	}

	public static void printFileObject(FileObject fileObject)
			throws IOException {
		printInfo(fileObject);
		if (fileObject.isFolder() && fileObject.getChildren() != null) {
			List<FileObject> children = fileObject.getChildren();
			for (FileObject fo : children) {
				printFileObject(fo);
			}
		}
	}
}