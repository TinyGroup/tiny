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

import org.apache.commons.net.ftp.*;
import org.tinygroup.exception.TinySysRuntimeException;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.SchemaProvider;
import org.tinygroup.vfs.VFSRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by luoguo on 14-3-31.
 */
public class FtpFileObject extends URLFileObject {

    public static final int BUF_SIZE = 102400;
    private FTPFile ftpFile;
    private FTPClient ftpClient;

    private FtpFileObject(SchemaProvider schemaProvider) {
        super(schemaProvider);
    }

    public FtpFileObject(SchemaProvider schemaProvider, String resource) {
        super(schemaProvider, resource);
        connectFtpServer();
        initFTPFile();
    }

    private void connectFtpServer() {
        try {
            ftpClient = new FTPClient();
            FTPClientConfig ftpClientConfig = new FTPClientConfig();
            ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
            ftpClient.configure(ftpClientConfig);
            URL url = getURL();
            if (url.getPort() <= 0) {
                ftpClient.connect(url.getHost());
            } else {
                ftpClient.connect(url.getHost(), url.getPort());
            }
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                throw new VFSRuntimeException("连接失败！");
            }
            if (url.getUserInfo() != null) {
                String userInfo[] = url.getUserInfo().split(":");
                String userName = null;
                String password = null;
                if (userInfo.length >= 1) {
                    userName = userInfo[0];
                }
                if (userInfo.length >= 2) {
                    password = userInfo[1];
                }
                if (!ftpClient.login(userName, password)) {
                    throw new VFSRuntimeException("登录失败：" + url.toString());
                }
                if (!ftpClient.setFileType(FTP.BINARY_FILE_TYPE)) {
                    throw new VFSRuntimeException("设置二进制类型失败");
                }
                ftpClient.setBufferSize(BUF_SIZE);
                ftpClient.setControlEncoding("utf-8");
            }
        } catch (Exception e) {
            throw new VFSRuntimeException(e);
        }
    }

    private void initFTPFile() {
        try {
            String path = getURL().getPath();
            // 如果且以"/"结尾，去掉"/"
            if (path.endsWith("/")) {
                path = path.substring(0, path.lastIndexOf('/'));
            }
            // 资源在服务器中所属目录
            String checkPath = path.substring(0, path.lastIndexOf('/'));
            // 如果所属目录为根目录
            if (checkPath.length() == 0) {
                checkPath = "/";
            }

            String fileName = path.substring(path.lastIndexOf('/'));
            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            ftpClient.enterLocalPassiveMode();
            // 从上级目录的子目录中过滤出当前资源
            FTPFile[] files = ftpClient.listFiles(recode(checkPath), new FtpFileFilterByName(fileName));
            if (files != null && files.length == 1) {
                ftpFile = files[0];
            } else {
                throw new TinySysRuntimeException("查找资源失败，url=" + getURL());
            }
        } catch (Exception e) {
            throw new VFSRuntimeException(e);
        }
    }

    public String getAbsolutePath() {
        // 如果是根结点
        if (getURL() != null) {
            return getURL().getPath();
        }
        String parentAbsolutePath = getParent().getAbsolutePath();
        if (parentAbsolutePath.endsWith("/")) {
            return parentAbsolutePath + ftpFile.getName();
        } else {
            return parentAbsolutePath + "/" + ftpFile.getName();
        }
    }

    public String getPath() {
        // 如果没有计算过
        if (path == null) {
            // 如果有父亲
            if (getParent() != null) {
                super.setPath(getParent().getPath() + "/" + getFileName());
            } else {
                if (ftpFile.isDirectory()) {
                    return "";
                } else {
                    return "/" + ftpFile.getName();
                }
            }
        }
        return getPath();
    }

    public String getFileName() {
        if (fileName == null) {
            fileName = ftpFile.getName();
        }
        return fileName;
    }

    public boolean isFolder() {
        return !ftpFile.isFile();
    }

    public long getLastModifiedTime() {
        return ftpFile.getTimestamp().getTimeInMillis();
    }

    public long getSize() {
        if (getFileSize() > 0) {
            return getFileSize();
        } else if (isFolder()) {
            return 0;
        }

        setFileSize(ftpFile.getSize());
        return getFileSize();
    }

    public InputStream getInputStream() {
        if (isFolder()) {
            return null;
        }

        InputStream is = null;
        try {
            String remote = getAbsolutePath();
            remote = recode(remote);
            is = ftpClient.retrieveFileStream(remote);
        } catch (IOException e) {
            throw new VFSRuntimeException("获取输入流异常", e);
        }

        return is;
    }

    public OutputStream getOutputStream() {
        if (isFolder()) {
            return null;
        }

        OutputStream os = null;
        try {
            String remote = getAbsolutePath();
            remote = recode(remote);
            os = ftpClient.storeFileStream(remote);
        } catch (IOException e) {
            throw new VFSRuntimeException(e);
        }

        return os;
    }

    public List<FileObject> getChildren() {
        if (!isFolder()) {
            return null;
        }

        if (getChildren() == null) {
            try {
                String pathname = getAbsolutePath();
                FTPFile[] files = ftpClient.listFiles(recode(pathname));
                List<FileObject> fileObjects = new ArrayList<FileObject>();
                for (FTPFile file : files) {
                    FtpFileObject fileObject = new FtpFileObject(getSchemaProvider());
                    fileObject.setParent(this);
                    fileObject.ftpFile = file;
                    fileObject.ftpClient = this.ftpClient;
                    fileObjects.add(fileObject);
                }
                return fileObjects;
            } catch (IOException e) {
                throw new VFSRuntimeException(e);
            }
        }

        return getChildren();
    }

    private String recode(String str) {
        String recode = str;
        try {
            recode = new String(str.getBytes("UTF-8"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            // 转码失败,忽略之
        }
        return recode;
    }

}
