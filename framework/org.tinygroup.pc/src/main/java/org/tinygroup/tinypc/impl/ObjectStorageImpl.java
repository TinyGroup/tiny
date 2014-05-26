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
package org.tinygroup.tinypc.impl;

import org.tinygroup.tinypc.ObjectStorage;
import org.tinygroup.tinypc.PCRuntimeException;

import java.io.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by luoguo on 14-1-14.
 */
public class ObjectStorageImpl implements ObjectStorage {
    private String rootFolder;
    private Map<Serializable, String> fileObjectMapping = new HashMap<Serializable, String>();
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    public ObjectStorageImpl() {

    }

    public ObjectStorageImpl(String rootFolder) {
        setRootFolder(rootFolder);
    }


    public void setRootFolder(String rootFolder) {
        if (!rootFolder.endsWith(File.separator)) {
            this.rootFolder = rootFolder + File.separator;
        } else {
            this.rootFolder = rootFolder;
        }
    }

    public String getRootFolder() {
        return rootFolder;
    }

    public List<Serializable> loadObjects() throws IOException, ClassNotFoundException {
        return loadObjects(null);
    }

    public <T> List<T> loadObjects(String typeName) throws IOException, ClassNotFoundException {
        List<Serializable> objectList = new ArrayList<Serializable>();
        checkFolder();
        File root = new File(rootFolder);
        loadObjects(root, objectList, typeName);
        return (List<T>) objectList;
    }

    private void loadObjects(File root, List<Serializable> objectList, String typeName)
            throws IOException, ClassNotFoundException {
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                loadObjects(file, objectList, typeName);
            } else {
                if (file.getName().endsWith(getEndofFileName(typeName))) {
                    objectList.add(loadObject(file));
                }
            }
        }
    }

    private Serializable loadObject(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
        Serializable object = (Serializable) inputStream.readObject();
        inputStream.close();
        return object;
    }

    private void checkFolder() {
        if (rootFolder == null) {
            throw new PCRuntimeException("存储位置没有设置！");
        }
        File file = new File(rootFolder);

        if (!file.exists()) {
            //如果目录不存在，则创建目录
            file.mkdirs();
        }
        if (!file.isDirectory()) {
            //如果不是文件夹
            throw new PCRuntimeException("存储位置" + rootFolder + "不是目录，是文件！");
        }
    }

    public void saveObject(Serializable object) throws IOException {
        saveObject(object, null);
    }

    public void saveObject(Serializable object, String typeName) throws IOException {
        String fileName = getFileName(typeName);
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName));
        outputStream.writeObject(object);
        outputStream.close();
        fileObjectMapping.put(object, fileName);

    }

    private String getFileName(String typeName) throws RemoteException {
        String currentDate = format.format(new Date());
        File folder = new File(rootFolder + currentDate);
        if (!folder.exists()) {
            //检查路径是否存在，如果不存在，则创建之
            folder.mkdirs();
        }
        return rootFolder + currentDate + File.separatorChar + Util.getUuid()
                + getEndofFileName(typeName);
    }

    private String getEndofFileName(String typeName) {
        return "_" + (typeName == null ? "" : typeName) + "" +
                ".object";
    }

    public void clearObject(Serializable object) {
        String fileName = fileObjectMapping.get(object);
        if (fileName != null) {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public void clearObjects() throws IOException {
        clearObjects(null);
    }

    public void clearObjects(String typeName) throws IOException {
        checkFolder();
        File root = new File(rootFolder);
        File[] files = root.listFiles();
        for (File file : files) {
            removeFile(file, typeName);
        }
    }

    private void removeFile(File file, String typeName) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                removeFile(f, typeName);
            }
        }
        if (file.isDirectory() && file.list().length == 0) {
            file.delete();
        } else if (typeName == null || isWithExtFileName(file, typeName)) {
            file.delete();
        }
    }

    private boolean isWithExtFileName(File file, String typeName) {
        return file.isFile() && file.getName().endsWith("_" + typeName + "" +
                ".object");
    }
}
