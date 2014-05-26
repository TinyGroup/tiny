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
package org.tinygroup.weblayer.tinyprocessor;

import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.uiengine.config.UIComponent;
import org.tinygroup.uiengine.manager.UIComponentManager;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.AbstractTinyProcessor;
import org.tinygroup.weblayer.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UI引擎处理css及js文件的合并并输出
 *
 * @author luoguo
 */
public class UiEngineTinyProcessor extends AbstractTinyProcessor {
    static Pattern urlPattern = Pattern.compile("(url[(][\"\']?)(.*?)([\"\']?[)])");
    UIComponentManager uiComponentManager;
    private static final String CACHE_CONTROL = "max-age=315360000";
    private FullContextFileRepository fullContextFileRepository;

    public UIComponentManager getUiComponentManager() {
        return uiComponentManager;
    }

    public void setUiComponentManager(UIComponentManager uiComponentManager) {
        this.uiComponentManager = uiComponentManager;
    }

    public FullContextFileRepository getFullContextFileRepository() {
        return fullContextFileRepository;
    }

    public void setFullContextFileRepository(FullContextFileRepository fullContextFileRepository) {
        this.fullContextFileRepository = fullContextFileRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(FullContextUrlRedirectTinyProcessor.class);


    public void init() {
        super.init();
    }


    public void reallyProcess(String servletPath, WebContext context) {
        logger.logMessage(LogLevel.DEBUG, "{}开始处理...", servletPath);
        HttpServletResponse response = context.getResponse();
        HttpServletRequest request = context.getRequest();
        boolean isDebug = true;
        String isReleaseMode = context.get("TINY_IS_RELEASE_MODE");
        if (isReleaseMode != null && isReleaseMode.length() > 0) {
            isDebug = Boolean.parseBoolean(isReleaseMode);
        }
        String contextPath = context.get("TINY_CONTEXT_PATH");
        try {
            String lastModifiedSign;
            if (servletPath.endsWith("uijs")) {
                lastModifiedSign = new Date(getJsLastModifiedSign(isDebug)).toGMTString();
                response.setContentType("text/javascript");
            } else if (servletPath.endsWith("uicss")) {
                lastModifiedSign = new Date(getCssLastModifiedSign(isDebug)).toGMTString();
                response.setContentType("text/css");
            } else {
                throw new RuntimeException("UiEngineTinyProcessor不能处理请求：" + servletPath);
            }
            String ims = request.getHeader("If-Modified-Since");
            if (ims != null && ims.length() > 0) {
                if (ims.equals(lastModifiedSign)) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Last-modified", lastModifiedSign);
            response.setHeader("Connection", "keep-alive");
            response.setHeader("Cache-Control", CACHE_CONTROL);
            response.setHeader("Date", lastModifiedSign);
            if (servletPath.endsWith("uijs")) {
                writeJs(response, isDebug);
            }
            if (servletPath.endsWith("uicss")) {
                writeCss(contextPath, response, servletPath, isDebug);
            }

            logger.logMessage(LogLevel.DEBUG, "{}处理完成。", servletPath);
        } catch (IOException e) {
            logger.errorMessage("{}写入响应信息出错", e, servletPath);
            throw new RuntimeException(e);
        }

    }

    private void writeJs(HttpServletResponse response, boolean isDebug) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
            String[] paths = uiComponentManager.getComponentJsArray(component, isDebug);
            if (paths != null) {
                for (String path : paths) {
                    logger.logMessage(LogLevel.INFO, "正在处理js文件:<{}>", path);
                    FileObject fileObject = fullContextFileRepository.getFileObject(path);
                    InputStream stream = new BufferedInputStream(fileObject.getInputStream());
                    StreamUtil.io(stream, outputStream, true, false);
                    stream.close();
                    outputStream.flush();
                    outputStream.write('\n');
                    logger.logMessage(LogLevel.INFO, "js文件:<{}>处理完毕", path);
                }
            }
            if (component.getJsCodelet() != null) {
                outputStream.write(component.getJsCodelet().getBytes("UTF-8"));
            }
        }
        outputStream.close();
    }

    private void writeCss(String contextPath, HttpServletResponse response, String servletPath, boolean isDebug)
            throws IOException {
        OutputStream outputStream = response.getOutputStream();
        outputStream.write("@charset \"utf-8\";\n".getBytes());
        for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
            String[] paths = uiComponentManager.getComponentCssArray(component, isDebug);
            if (paths != null) {
                for (String path : paths) {
                    logger.logMessage(LogLevel.INFO, "正在处理css文件:<{}>", path);
                    FileObject fileObject = fullContextFileRepository.getFileObject(path);
                    InputStream stream = new BufferedInputStream(fileObject.getInputStream());
                    byte[] buffer = new byte[stream.available()];
                    stream.read(buffer);
                    stream.close();
                    writeCss(outputStream, contextPath, new String(buffer, "UTF-8"), fileObject.getPath());
                    outputStream.write('\n');
                    logger.logMessage(LogLevel.INFO, "css文件:<{}>处理完毕", path);
                }
            }
            if (component.getCssCodelet() != null) {
                writeCss(outputStream, contextPath, component.getCssCodelet(), servletPath);
            }
        }
        outputStream.close();
    }

    private void writeCss(OutputStream outputStream, String contextPath, String string, String servletPath)
            throws IOException {
        Matcher matcher = urlPattern.matcher(string);
        int curpos = 0;
        while (matcher.find()) {
            outputStream.write(string.substring(curpos, matcher.start()).getBytes("UTF-8"));
            if (matcher.group(2).trim().startsWith("data:")) {
                outputStream.write(matcher.group().getBytes("UTF-8"));
            } else {
                outputStream.write(matcher.group(1).getBytes("UTF-8"));
                outputStream.write(convertUrl(contextPath, matcher.group(2), servletPath).getBytes("UTF-8"));
                outputStream.write(matcher.group(3).getBytes("UTF-8"));
            }
            curpos = matcher.end();
            continue;
        }
        outputStream.write(string.substring(curpos).getBytes("UTF-8"));
    }

    private static String convertUrl(String contextPath, String url, String servletPath) {
        if (contextPath == null) {
            contextPath = "";
        }
        if (url.startsWith("/") || url.startsWith("\\")) {
            return contextPath + url;
        } else if (url.startsWith("../") || url.startsWith("..\\")) {
            String firstThree = url.substring(0, 3);
            int count = 0;
            while (url.startsWith(firstThree)) {
                count++;
                url = url.substring(3);
            }
            String[] paths = servletPath.split("/");
            StringBuffer sb = new StringBuffer(contextPath);
            for (int i = 0; i < paths.length - count - 1; i++) {
                sb.append(paths[i]).append("/");
            }
            sb.append(url);
            return sb.toString();
        }
        return contextPath + servletPath.substring(0, servletPath.lastIndexOf('/') + 1) + url;
    }

    private synchronized long getJsLastModifiedSign(boolean isDebug) {
        long time = 0;
        for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
            String[] paths = uiComponentManager.getComponentJsArray(component, isDebug);
            if (paths != null) {
                for (String path : paths) {
                    FileObject fileObject = fullContextFileRepository.getFileObject(path);
                    if (fileObject != null && fileObject.isExist()) {
                        time += fileObject.getLastModifiedTime();
                        time += path.hashCode();
                    } else {
                        throw new RuntimeException("不能找到资源文件：" + component.getName() + "-" + path);
                    }
                }
            }
        }
        return time;
    }

    private long getCssLastModifiedSign(boolean isDebug) {
        long time = 0;
        for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
            String[] paths = uiComponentManager.getComponentCssArray(component, isDebug);
            if (paths != null) {
                for (String path : paths) {
                    FileObject fileObject = fullContextFileRepository.getFileObject(path);
                    if (fileObject != null && fileObject.isExist()) {
                        time += fileObject.getLastModifiedTime();
                        time += path.hashCode();
                    } else {
                        throw new RuntimeException("不能找到资源文件：" + component.getName() + "-" + path);
                    }
                }
            }
        }
        return time;
    }
}
