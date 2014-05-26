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
package org.tinygroup.vfs.impl;

import junit.framework.TestCase;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.vfs.VFSRuntimeException;

import java.io.*;
import java.util.List;

public class FtpFileObjectTest extends TestCase {

	private static String rootDir; // ftp服务器根路径
	private static FtpServer ftpServer; // ftp服务器
	private static boolean inited = false; // 是否已初始化

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if (ftpServer != null) {
			ftpServer.stop(); // 停止服务器
			System.out.println("停止服务器成功");
		}
		deleteFile(rootDir); // 清理文件，文件夹
	}

	public static void main(String[] args) throws Exception {
		init(); // 初始化ftp服务器

		FtpFileObjectTest ftpFileObjectTest = new FtpFileObjectTest();
		ftpFileObjectTest.fileTest();
		ftpFileObjectTest.folderTest();
		ftpFileObjectTest.folderTest2();
		ftpFileObjectTest.folderTest3();

		System.out.println("ftp服务器对应目录删除成功");
		deleteFile(rootDir); // 清理文件，文件夹
	}

	private static void init() throws Exception {
		if (inited) {
			return;
		}

		File file = new File("ftpServer");
		if (!file.exists()) {
			file.mkdirs();
		}

		String absolutePath = file.getAbsolutePath();
		absolutePath = absolutePath.replaceAll("\\\\", "/");
		if (!absolutePath.endsWith("/")) {
			absolutePath = absolutePath + "/";
		}

		rootDir = absolutePath;

		try {
			ftpServer = getFTPServer(rootDir);
			ftpServer.start(); // 启动服务器
			System.out.println("ftp服务器启动成功,服务器根路径：" + rootDir);
		} catch (FtpException e) {
			deleteFile(rootDir); // 清理文件，文件夹
			throw new VFSRuntimeException("ftp服务器启动失败", e);
		}
	}

	public void test() throws Exception {
		// init(); // 初始化ftp服务器

		// fileTest();
		// folderTest();
		// folderTest2();
		// folderTest3();
	}

	private void fileTest() throws Exception {
		String fileName = "文件  11 aa.txt";
		String pathname = rootDir + fileName;
		createNewFile(pathname); // 新建文件
		write(pathname, "内容测试 中文 空格 英文      sdfsfd   数字   1231 "); // 写入内容
		String resource = "ftp://anonymous:@127.0.0.1:21/" + fileName;
		FileObject fileObject = VFS.resolveFile(resource);

		assertNotNull(fileObject.getSchemaProvider());
		assertEquals(resource, fileObject.getURL().toString());
		assertEquals("/文件  11 aa.txt", fileObject.getAbsolutePath());
		assertEquals("/文件  11 aa.txt", fileObject.getPath());
		assertEquals("文件  11 aa.txt", fileObject.getFileName());
		assertEquals("txt", fileObject.getExtName());
		assertTrue(fileObject.isExist());
		assertTrue(!fileObject.isFolder());
		assertTrue(!fileObject.isInPackage());
		assertTrue(fileObject.getLastModifiedTime() > 0);
		assertTrue(fileObject.getSize() > 0);
		InputStream is = fileObject.getInputStream();
		OutputStream os = fileObject.getOutputStream();
		assertTrue(is != null || os != null);
		if (is != null) {
			is.close();
		}
		if (os != null) {
			os.close();
		}
		assertNull(fileObject.getParent());
		assertNull(fileObject.getChildren());

		deleteFile(rootDir + fileName);
	}

	public void folderTest() throws Exception {
		String dirName = "目录 11 aaa";
		mkdirs(rootDir + dirName); // 新建文件夹
		String resource = "ftp://anonymous:@127.0.0.1:21/" + dirName;
		FileObject fileObject = VFS.resolveFile(resource);

		assertNotNull(fileObject.getSchemaProvider());
		assertEquals(resource, fileObject.getURL().toString());
		assertEquals("/目录 11 aaa", fileObject.getAbsolutePath());
		assertEquals("", fileObject.getPath());
		assertEquals("目录 11 aaa", fileObject.getFileName());
		assertEquals("", fileObject.getExtName());
		assertTrue(fileObject.isExist());
		assertTrue(fileObject.isFolder());
		assertTrue(!fileObject.isInPackage());
		assertTrue(fileObject.getLastModifiedTime() > 0);
		assertTrue(fileObject.getSize() == 0);
		assertNull(fileObject.getInputStream());
		assertNull(fileObject.getOutputStream());
		assertNull(fileObject.getParent());
		assertNotNull(fileObject.getChildren());
		assertEquals(0, fileObject.getChildren().size());
		assertNull(fileObject.getChild(""));

		deleteFile(rootDir + dirName);
	}

	public void folderTest2() throws Exception {
		String dirName = "目录 22 aaa/目录 12 bbb/目录 123 ccc";
		mkdirs(rootDir + dirName); // 新建文件夹
		String resource = "ftp://anonymous:@127.0.0.1:21/" + dirName;
		FileObject fileObject = VFS.resolveFile(resource);

		assertNotNull(fileObject.getSchemaProvider());
		assertEquals(resource, fileObject.getURL().toString());
		assertEquals("/目录 22 aaa/目录 12 bbb/目录 123 ccc",
				fileObject.getAbsolutePath());
		assertEquals("", fileObject.getPath());
		assertEquals("目录 123 ccc", fileObject.getFileName());
		assertEquals("", fileObject.getExtName());
		assertTrue(fileObject.isExist());
		assertTrue(fileObject.isFolder());
		assertTrue(!fileObject.isInPackage());
		assertTrue(fileObject.getLastModifiedTime() > 0);
		assertTrue(fileObject.getSize() == 0);
		assertNull(fileObject.getInputStream());
		assertNull(fileObject.getOutputStream());
		assertNull(fileObject.getParent());
		assertNotNull(fileObject.getChildren());
		assertEquals(0, fileObject.getChildren().size());
		assertNull(fileObject.getChild(""));

		deleteFile(rootDir + dirName);
	}

	public void folderTest3() throws Exception {
		String dirName = "目录 33 aaa/目录 erw 123/目录 wer1 2sd";
		mkdirs(rootDir + dirName + "/目录 123 abc"); // 文件夹
		String pathname = rootDir + dirName + "/子文件 sd 12sdf.txt";
		createNewFile(pathname); // 新建子文件
		write(pathname, "内容测试 中文 空格 英文      sdfsfd   数字   1231 "); // 写入内容

		String resource = "ftp://anonymous:@127.0.0.1:21/" + dirName;
		FileObject fileObject = VFS.resolveFile(resource);

		assertNotNull(fileObject.getSchemaProvider());
		assertEquals(resource, fileObject.getURL().toString());
		assertEquals("/目录 33 aaa/目录 erw 123/目录 wer1 2sd",
				fileObject.getAbsolutePath());
		assertEquals("", fileObject.getPath());
		assertEquals("目录 wer1 2sd", fileObject.getFileName());
		assertEquals("", fileObject.getExtName());
		assertTrue(fileObject.isExist());
		assertTrue(fileObject.isFolder());
		assertTrue(!fileObject.isInPackage());
		assertTrue(fileObject.getLastModifiedTime() > 0);
		assertTrue(fileObject.getSize() == 0);
		assertNull(fileObject.getInputStream());
		assertNull(fileObject.getOutputStream());
		assertNull(fileObject.getParent());
		assertNotNull(fileObject.getChildren());
		assertEquals(2, fileObject.getChildren().size());
		assertNotNull(fileObject.getChild("目录 123 abc"));
		assertNotNull(fileObject.getChild("子文件 sd 12sdf.txt"));

		List<FileObject> childen = fileObject.getChildren();
		for (FileObject fileObject2 : childen) {
			if (fileObject2.isFolder()) {
				assertNotNull(fileObject2.getSchemaProvider());
				assertNull(fileObject2.getURL());
				assertEquals("/目录 33 aaa/目录 erw 123/目录 wer1 2sd/目录 123 abc",
						fileObject2.getAbsolutePath());
				assertEquals("/目录 123 abc", fileObject2.getPath());
				assertEquals("目录 123 abc", fileObject2.getFileName());
				assertEquals("", fileObject2.getExtName());
				assertTrue(fileObject2.isExist());
				assertTrue(fileObject2.isFolder());
				assertTrue(!fileObject2.isInPackage());
				assertTrue(fileObject2.getLastModifiedTime() > 0);
				assertTrue(fileObject2.getSize() == 0);
				assertNull(fileObject2.getInputStream());
				assertNull(fileObject2.getOutputStream());
				assertNotNull(fileObject2.getParent());
				assertNotNull(fileObject2.getChildren());
				assertEquals(0, fileObject2.getChildren().size());
				assertNull(fileObject2.getChild(""));
			} else {
				assertNull(fileObject2.getURL());
				assertNotNull(fileObject2.getSchemaProvider());
				assertEquals(
						"/目录 33 aaa/目录 erw 123/目录 wer1 2sd/子文件 sd 12sdf.txt",
						fileObject2.getAbsolutePath());
				assertEquals("/子文件 sd 12sdf.txt", fileObject2.getPath());
				assertEquals("子文件 sd 12sdf.txt", fileObject2.getFileName());
				assertEquals("txt", fileObject2.getExtName());
				assertTrue(fileObject2.isExist());
				assertTrue(!fileObject2.isFolder());
				assertTrue(!fileObject2.isInPackage());
				assertTrue(fileObject2.getLastModifiedTime() > 0);
				assertTrue(fileObject2.getSize() > 0);
				InputStream is = fileObject2.getInputStream();
				OutputStream os = fileObject2.getOutputStream();
				assertTrue(is != null || os != null);
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
				assertNotNull(fileObject2.getParent());
				assertNull(fileObject2.getChildren());
			}
		}

		deleteFile(rootDir + dirName);
	}

	private static void mkdirs(String absolutePath) {
		File file = new File(absolutePath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	private static void createNewFile(String pathname) throws IOException {
		File file = new File(pathname);
		if (!file.exists()) {
			file.createNewFile();
		}
	}

	private static void write(String pathname, String content)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(new File(pathname));
		PrintWriter pw = new PrintWriter(fos);
		pw.write(content.toString().toCharArray());
		pw.flush();
		if (fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
			}
		}
		if (pw != null) {
			pw.close();
		}
	}

	private static FtpServer getFTPServer(String ftpRootPath)
			throws FtpException, IOException {
		// 新建并配置users.properties文件
		File file = new File(ftpRootPath + "users.properties");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		PrintWriter pw = new PrintWriter(fos);
		StringBuilder content = new StringBuilder();
		content.append("ftpserver.user.anonymous.homedirectory=")
				.append(ftpRootPath).append("\n");
		content.append("ftpserver.user.anonymous.userpassword=\n");
		content.append("ftpserver.user.anonymous.writepermission=true\n");
		pw.write(content.toString().toCharArray());
		pw.flush();
		if (fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
			}
		}
		if (pw != null) {
			pw.close();
		}

		// 创建并配置服务工厂
		FtpServerFactory serverFactory = new FtpServerFactory();
		ListenerFactory factory = new ListenerFactory();
		factory.setPort(21);
		serverFactory.addListener("default", factory.createListener()); // 添加监听
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		userManagerFactory.setFile(file);
		serverFactory.setUserManager(userManagerFactory.createUserManager()); // 设置用户配置信息

		// 创建并返回服务
		return serverFactory.createServer();
	}

	private static void deleteFile(String delPath) {
		if (delPath == null || delPath.trim().length() == 0) {
			return;
		}
		String pathname = delPath.replaceAll("\\\\", "/");
		File file = new File(pathname);
		if (file.isDirectory()) {
			String[] fileList = file.list();
			for (int i = 0; i < fileList.length; i++) {
				deleteFile(pathname + "/" + fileList[i]);
			}
		}
		System.out.println(file.getAbsolutePath() + ",删除成功");
		file.delete();
	}

}
