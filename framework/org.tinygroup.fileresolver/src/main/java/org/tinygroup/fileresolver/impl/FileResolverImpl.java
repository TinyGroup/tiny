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

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tinygroup.commons.order.OrderUtil;
import org.tinygroup.commons.tools.ClassPathUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.FileProcessor;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.ProcessorCallBack;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.vfs.impl.FileSchemaProvider;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 功能说明: 文件搜索器默认实现
 * <p/>
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2013-2-21 <br>
 * <br>
 */
public class FileResolverImpl implements FileResolver {
    private static final String FILE_RESOLVER_CONFIG = "/application/file-resolver-configuration";
    private static final int DEFAULT_THREAD_NUM = 1;
    ConfigurationManager configurationManager = ConfigurationUtil.getConfigurationManager();
    private static Logger logger = LoggerFactory.getLogger(FileResolverImpl.class);

    // 是否对classPath进行处理，默认为处理
    private int fileProcessorThreadNum = DEFAULT_THREAD_NUM;
    // 需要扫描的额外路径列表
    private Set<String> manualClassPaths = new HashSet<String>();
    // 文件处理器列表，由文件查找器统一管理
    private List<FileProcessor> fileProcessorList = new ArrayList<FileProcessor>();
    // 文件信息 key:path,value:modify-time
    private Map<String, Long> fileDateMap = new HashMap<String, Long>();
    // 文件信息
    private Map<String, FileObject> fileObjectCaches = new HashMap<String, FileObject>();
    private Map<String, Pattern> includePathPatternMap = new HashMap<String, Pattern>();

    private Set<String> allScanningPath = new HashSet<String>();
    private XmlNode componentConfig;
    private XmlNode applicationConfig;

    public List<FileProcessor> getFileProcessorList() {
        return fileProcessorList;
    }

    public void setFileProcessorList(List<FileProcessor> fileProcessorList) {
        this.fileProcessorList = fileProcessorList;
    }

    public FileResolverImpl() {
        String classPath = "[\\/]classes\\b";
        includePathPatternMap.put(classPath, Pattern.compile(classPath));
        classPath = "[\\/]test-classes\\b";
        includePathPatternMap.put(classPath, Pattern.compile(classPath));
    }

    public List<String> getManualClassPaths() {
        List<String> classPaths = new ArrayList<String>();
        classPaths.addAll(manualClassPaths);
        return classPaths;
    }

    public void addManualClassPath(String path) {
        if (!manualClassPaths.contains(path)) {
            manualClassPaths.add(path);
        }
    }

    public void addFileProcessor(FileProcessor fileProcessor) {
        fileProcessorList.add(fileProcessor);
    }

    public void resolve() {
        if (fileProcessorList.size() == 0) {
            return;
        } else {
            for (FileProcessor fileProcessor : fileProcessorList) {
                fileProcessor.setFileResolver(this);
            }
            OrderUtil.order(fileProcessorList);
            cleanProcessor();
            // 移动日志信息，文件搜索器中存在处理器时，才会进行全路径扫描，并打印日志信息
            logger.logMessage(LogLevel.INFO, "正在进行全路径扫描....");
            resolverScanPath();
            for (FileProcessor fileProcessor : fileProcessorList) {
                fileProcessor.process();
            }
            logger.logMessage(LogLevel.INFO, "全路径扫描完成。");
        }
    }

    private void refreshScanPath() {
        Set<FileObject> classPaths = new HashSet<FileObject>();
        for (String file : allScanningPath) {
            FileObject fileObject = VFS.resolveFile(file);
            Long lastModifiedTime = fileDateMap.get(fileObject.getAbsolutePath());
            long modifiedTime = fileObject.getLastModifiedTime();
            if (lastModifiedTime.longValue() != modifiedTime || !fileObject.isInPackage()) {
                fileDateMap.put(fileObject.getAbsolutePath(), modifiedTime);
                if (fileObject.isExist()) {
                    classPaths.add(fileObject);
                }
            }
        }
        resolveClassPaths(classPaths);
    }

    private void resolverScanPath() {
        Set<FileObject> classPaths = new HashSet<FileObject>();
        String debugMode = configurationManager.getApplicationProperty("DEBUG_MODE");
        boolean isDebugMode = false;
        if (debugMode == null || debugMode.equalsIgnoreCase("true")) {
            isDebugMode = true;
        }
        if (isDebugMode) {
            classPaths.addAll(getClassPath());
        }
        classPaths.addAll(resolveManualClassPath());
        try {
            addWebClasses(classPaths);
        } catch (Exception e) {
            logger.errorMessage("查找WEB-INF/classes路径失败！", e);
        }
        try {
            if (isDebugMode) {
                getWebLibJars(classPaths);
            }
        } catch (Exception e) {
            logger.errorMessage("查找Web工程中的jar文件列表失败！", e);
        }
        for (FileObject fileObject : classPaths) {
            long modifiedTime = fileObject.getLastModifiedTime();
            fileDateMap.put(fileObject.getAbsolutePath(), modifiedTime);
        }
        resolveClassPaths(classPaths);
        // resolveDeletedFile();
    }

    void addWebClasses(Set<FileObject> classPaths) {
        logger.logMessage(LogLevel.INFO, "查找WEB-INF/classes路径开始...");
        URL url = FileResolverImpl.class.getResource("/");
        String path = url.toString();
        logger.logMessage(LogLevel.INFO, "WEB-INF/classes路径是:{}", path);
        if (path.indexOf("!") < 0) {// 如果在目录中
            FileObject fileObject = VFS.resolveFile(path);
            classPaths.add(fileObject);
            allScanningPath.add(fileObject.getAbsolutePath());
            String libPath = path.replaceAll("/classes", "/lib");
            logger.logMessage(LogLevel.INFO, "WEB-INF/lib路径是:{}", libPath);
            FileObject libFileObject = VFS.resolveFile(libPath);
            classPaths.add(libFileObject);
            allScanningPath.add(libFileObject.getAbsolutePath());
            int index = path.indexOf("/classes");
            if (index > 0) {
                String webInfPath = path.substring(0, index);
                if (webInfPath.endsWith("WEB-INF")) {
                    logger.logMessage(LogLevel.INFO, "WEB-INF路径是:{}", webInfPath);
                    FileObject webInfoFileObject = VFS.resolveFile(webInfPath);
                    classPaths.add(webInfoFileObject);
                    allScanningPath.add(webInfoFileObject.getAbsolutePath());
                }
            }

        } else {// 如果在jar包中
            path = url.getFile().split("!")[0];
            FileObject fileObject = VFS.resolveFile(path);
            classPaths.add(fileObject);
            allScanningPath.add(path);
            String libPath = path.substring(0, path.lastIndexOf('/'));
            logger.logMessage(LogLevel.INFO, "WEB-INF/lib路径是:{}", libPath);
            FileObject libFileObject = VFS.resolveFile(libPath);
            classPaths.add(libFileObject);
            allScanningPath.add(libFileObject.getAbsolutePath());
        }
        logger.logMessage(LogLevel.INFO, "查找WEB-INF/classes路径完成。");

        String webinfPath = configurationManager.getApplicationProperty("TINY_WEBROOT");
        if (webinfPath == null || webinfPath.length() == 0) {
            logger.logMessage(LogLevel.WARN, "WEBROOT变量找不到");
            return;
        }
        FileObject fileObject = VFS.resolveFile(webinfPath);
        classPaths.add(fileObject);
        allScanningPath.add(fileObject.getAbsolutePath());
    }

    void getWebLibJars(Set<FileObject> classPaths) throws Exception {
        logger.logMessage(LogLevel.INFO, "查找Web工程中的jar文件列表开始...");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urls = loader.getResources("META-INF/MANIFEST.MF");
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String path = url.toString();
            path = path.replaceAll("/./", "/");// weblogic中存在这种情况
            if (path.indexOf("!") > 0) {
                path = path.split("!")[0];
            } else {// 专门为JBOSS vfs开头的处理
                path = path.substring(0, path.length() - "META-INF/MANIFEST.MF".length() - 1);
                path = path.substring(path.indexOf(':') + 1);
            }
            FileObject fileObject = VFS.resolveFile(path);
            if (includePathPatternMap != null && includePathPatternMap.size() > 0) {
                if (isInclude(fileObject)) {
                    addJarFile(classPaths, path, fileObject);
                    continue;
                }
            }
            Manifest mf = new Manifest(url.openStream());
            Attributes attributes = mf.getMainAttributes();
            String isTinyProject = attributes.getValue("IsTinyProject");
            if("true".equals(isTinyProject)){
            	logger.logMessage(LogLevel.INFO, "文件<{}>由于在MANIFEST.MF文件中声明了IsTinyProject: true而被扫描。", fileObject);
            	addJarFile(classPaths, path, fileObject);
            }
//            String mfContent = IOUtils.readFromInputStream(url.openStream(), "UTF-8");
//            String[] lines = mfContent.split("\n");
//            for (String line : lines) {
//                String[] pair = line.split(":");
//                if (pair.length == 2) {
//                    if (pair[0].trim().equals("IsTinyProject") && pair[1].trim().equals("true")) {
//                        logger.logMessage(LogLevel.INFO, "文件<{}>由于在MANIFEST.MF文件中声明了IsTinyProject: true而被扫描。", fileObject);
//                        addJarFile(classPaths, path, fileObject);
//                        break;
//                    }
//                }
//            }
        }
        logger.logMessage(LogLevel.INFO, "查找Web工程中的jar文件列表完成。");
    }

    private void addJarFile(Set<FileObject> classPaths, String path, FileObject fileObject) {
        logger.logMessage(LogLevel.INFO, "扫描到jar文件<{}>。", path);
        classPaths.add(fileObject);
        allScanningPath.add(path);
    }

    /**
     * 文件搜索器重新搜索时，需要清空上次各个文件处理器中处理的文件列表,并清除记录的扫描路径。
     */
    private void cleanProcessor() {
        for (FileProcessor fileProcessor : fileProcessorList) {
            fileProcessor.clean();
        }
        allScanningPath.clear();
    }

    boolean isInclude(FileObject fileObject) {
        if (fileObject.getSchemaProvider() instanceof FileSchemaProvider) {
            return true;
        }
        for (String patternString : includePathPatternMap.keySet()) {
            Pattern pattern = includePathPatternMap.get(patternString);
            Matcher matcher = pattern.matcher(fileObject.getFileName());
            if (matcher.find()) {
                logger.logMessage(LogLevel.INFO, "文件<{}>由于匹配了包含正则表达式<{}>而被扫描。", fileObject, patternString);
                return true;
            }
        }
        return false;
    }

    /**
     * 把系统变量中定义的classpath路径，转化为FileObject对象，并返回FileObject列表
     *
     * @return
     */
    private List<FileObject> getClassPath() {
        List<FileObject> classPathFileObjects = new ArrayList<FileObject>();
        String classPathProperty = System.getProperty("java.class.path").toString();
        String operateSys=System.getProperty("os.name").toLowerCase();
        String[] classPaths=null;
        if(operateSys.indexOf("windows")>=0){
        	classPaths = classPathProperty.split(";");
        }else{
        	classPaths=classPathProperty.split(":");
        }
        if(classPaths!=null){
        	 for (String classPath : classPaths) {
                 if (classPath.length() > 0) {
                     FileObject fileObject = VFS.resolveFile(classPath);
                     if (isInclude(fileObject)) {
                         classPathFileObjects.add(fileObject);
                         allScanningPath.add(classPath);
                     }
                 }
             }
        }
        return classPathFileObjects;
    }

    /**
     * 把额外定义的路径，转化为FileObject
     *
     * @return
     */
    private List<FileObject> resolveManualClassPath() {
        List<FileObject> classPathFileObjects = new ArrayList<FileObject>();
        for (String manualClassPath : manualClassPaths) {
            FileObject fileObject = VFS.resolveFile(manualClassPath);
            classPathFileObjects.add(fileObject);
            allScanningPath.add(manualClassPath);
        }
        return classPathFileObjects;
    }

    /**
     * 文件搜索器根据路径列表进行搜索
     *
     * @param classPaths
     */
    private void resolveClassPaths(Set<FileObject> classPaths) {
        List<FileObject> scanPathsList = new ArrayList<FileObject>();
        scanPathsList.addAll(classPaths);
        MultiThreadFileProcessor.mutiProcessor(fileProcessorThreadNum, "file-resolver-threads", scanPathsList, new ProcessorCallBack() {
            public void callBack(FileObject fileObject) {

                logger.logMessage(LogLevel.INFO, "正在扫描路径[{0}]...", fileObject.getAbsolutePath());
                resolveFileObject(fileObject);
                logger.logMessage(LogLevel.INFO, "路径[{0}]扫描完成。", fileObject.getAbsolutePath());
            }
        });
    }

    private void resolveFileObject(FileObject fileObject) {

        logger.logMessage(LogLevel.DEBUG, "找到文件：{}", fileObject.getAbsolutePath().toString());
        processFile(fileObject);
        if (fileObject.isFolder() && fileObject.getChildren() != null) {
            for (FileObject f : fileObject.getChildren()) {
                if (!allScanningPath.contains(f.getAbsolutePath())) {
                    resolveFileObject(f);
                } else {
                    logger.logMessage(LogLevel.INFO, "文件:[{}]在扫描根路径列表中存在，将作为根路径进行扫描", f.getAbsolutePath());
                }
            }
            return;
        }
    }

    /**
     * 移除已删除文件
     */
    private void resolveDeletedFile() {
        // 处理删除的文件
        Map<String, FileObject> tempMap = new HashMap<String, FileObject>();
        for (String path : fileObjectCaches.keySet()) {
            FileObject fileObject = fileObjectCaches.get(path);
            if (!fileObject.isExist()) {
                // 文件已经被删除
                for (FileProcessor fileProcessor : fileProcessorList) {
                    if (fileProcessor.isMatch(fileObject)) {//匹配后才能删除
                        fileProcessor.delete(fileObject);
                    }
                }
            } else {
                tempMap.put(path, fileObject);
            }
        }
        fileObjectCaches = tempMap;
    }

    /**
     * 文件存在并且不在忽略处理列表中，再交给各个文件处理器进行处理。
     *
     * @param fileObject
     */
    private synchronized void processFile(FileObject fileObject) {
        if (fileObject.isExist()) {
            for (FileProcessor fileProcessor : fileProcessorList) {
                if (fileProcessor.isMatch(fileObject)) {
                    String absolutePath = fileObject.getAbsolutePath();
                    Long lastModifiedTime = fileDateMap.get(absolutePath);
                    long modifiedTime = fileObject.getLastModifiedTime();
                    if (lastModifiedTime == null) {// 说明是第一次发现
                        addFile(fileObject, fileProcessor);
                        fileDateMap.put(absolutePath, modifiedTime);
                        fileObjectCaches.put(absolutePath, fileObject);
                    } else if (lastModifiedTime.longValue() != modifiedTime) {
                        changeFile(absolutePath, fileObject, fileProcessor);
                        fileDateMap.put(absolutePath, modifiedTime);
                        fileObjectCaches.put(absolutePath, fileObject);
                    } else {
                        noChangeFile(fileObject, fileProcessor);
                    }
                    break;// 已经找到文件处理器，就退出
                }
            }
        }
    }

    private void noChangeFile(FileObject fileObject, FileProcessor fileProcessor) {
        fileProcessor.noChange(fileObject);

    }

    private void changeFile(String path, FileObject fileObject, FileProcessor fileProcessor) {
        fileProcessor.delete(fileObjectCaches.get(path));
        fileObjectCaches.remove(path);
        fileProcessor.modify(fileObject);// 先删除之前的，再增加新的
    }

    private void addFile(FileObject fileObject, FileProcessor fileProcessor) {
        fileProcessor.add(fileObject);
    }

    public void addIncludePathPattern(String pattern) {
        includePathPatternMap.put(pattern, Pattern.compile(pattern));
    }

    public List<String> getAllScanningPath() {
        List<String> scanList = new ArrayList<String>();
        scanList.addAll(allScanningPath);
        return scanList;
    }

    public FileObject getClassesPath() {
        return VFS.resolveFile(ClassPathUtil.getClassRootPath());
    }

    public int getFileProcessorThreadNum() {
        return fileProcessorThreadNum;
    }

    public void setFileProcessorThreadNum(int threadNum) {
        this.fileProcessorThreadNum = threadNum;

    }

    /**
     * 刷新工作
     */
    public void refresh() {
        if (fileProcessorList.size() == 0) {
            return;
        } else {
            logger.logMessage(LogLevel.INFO, "正在进行全路径刷新....");
            for (FileProcessor fileProcessor : fileProcessorList) {
                fileProcessor.clean();// 清空文件列表
            }
            resolveDeletedFile();// 删除不存在的文件
            refreshScanPath();// 重新扫描
            for (FileProcessor fileProcessor : fileProcessorList) {
                if (fileProcessor.supportRefresh()) {
                    fileProcessor.process();
                }
            }
            logger.logMessage(LogLevel.INFO, "全路径刷新结束....");
        }

    }

    public String getApplicationNodePath() {
        return FILE_RESOLVER_CONFIG;
    }

    public String getComponentConfigPath() {
        return "/fileresolver.config.xml";
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        this.applicationConfig = applicationConfig;
        this.componentConfig = componentConfig;
        initConfig();
    }

    public XmlNode getComponentConfig() {
        return componentConfig;
    }

    public XmlNode getApplicationConfig() {
        return applicationConfig;
    }

    private void initConfig() {
        String threadCount = ConfigurationUtil.getPropertyName(applicationConfig, componentConfig, "thread-count");
        this.setFileProcessorThreadNum(getThreadNum(threadCount));
        List<XmlNode> classPaths = ConfigurationUtil.combineFindNodeList("class-path", applicationConfig, componentConfig);
        for (XmlNode classPath : classPaths) {
            String path = classPath.getAttribute("path");
            if (path != null && path.length() > 0) {
                logger.logMessage(LogLevel.INFO, "添加手工配置classpath: [{0}]...", path);
                this.addManualClassPath(path);
            }
        }
        List<XmlNode> includePatterns = ConfigurationUtil.combineFindNodeList("include-pattern", applicationConfig, componentConfig);
        for (XmlNode includePatter : includePatterns) {
            String pattern = includePatter.getAttribute("pattern");
            if (pattern != null && pattern.length() > 0) {
                logger.logMessage(LogLevel.INFO, "添加包含文件正则表达式: [{0}]...", pattern);
                this.addIncludePathPattern(pattern);
            }
        }
    }

    private int getThreadNum(String threadNum) {
        int fileProcessorThreadNum = 1;
        if (threadNum != null) {
            try {
                fileProcessorThreadNum = Integer.parseInt(threadNum);
                if (fileProcessorThreadNum <= 0) {
                    fileProcessorThreadNum = 1;
                }
            } catch (Exception e) {
                // 传入非int值
                fileProcessorThreadNum = 1;
            }

        }
        return fileProcessorThreadNum;

    }

}
