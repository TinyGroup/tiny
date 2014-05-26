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
package org.tinygroup.velocity.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.InternalContextAdapter;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.threadgroup.AbstractProcessor;
import org.tinygroup.threadgroup.MultiThreadProcessor;
import org.tinygroup.threadgroup.Processor;
import org.tinygroup.velocity.TinyVelocityContext;
import org.tinygroup.velocity.VelocityHelper;
import org.tinygroup.velocity.config.Bean;
import org.tinygroup.velocity.config.StaticClass;
import org.tinygroup.velocity.config.VelocityContextConfig;
import org.tinygroup.vfs.FileObject;

public final class VelocityHelperImpl implements VelocityHelper {
	private static final String ENCODING = "UTF-8";
	private static final String BASE_CONTEXT = "BASE_CONTEXT";
	private static final String PATH_CONTENT = "pageContent";
	private static Logger logger = LoggerFactory
			.getLogger(VelocityHelperImpl.class);
	private VelocityEngine velocity = new VelocityEngine();
	private List<String> macroList = new ArrayList<String>();
	private static final String DEFAULT_FILENAME = "default";// 默认文件名
	private static final String VIEW_EXT_FILENAME = "page";// 视图扩展名
	private static final String LAYOUT_EXT_FILENAME = "layout";// 布局扩展名

	private String defaultFileName = DEFAULT_FILENAME;
	private String viewExtFileName = VIEW_EXT_FILENAME;
	private String layoutExtFileName = LAYOUT_EXT_FILENAME;
	private ThreadLocal<List<Processor>> local = new ThreadLocal<List<Processor>>();
	private FullContextFileRepository fullContextFileRepository;
	private VelocityContextConfig velocityContextConfig;
	private Context initContext;

	public String getDefaultFileName() {
		return defaultFileName;
	}

	public void setDefaultFileName(String defaultFileName) {
		this.defaultFileName = defaultFileName;
	}

	public String getViewExtFileName() {
		return viewExtFileName;
	}

	public void setViewExtFileName(String viewExtFileName) {
		this.viewExtFileName = viewExtFileName;
	}

	public String getLayoutExtFileName() {
		return layoutExtFileName;
	}

	public void setLayoutExtFileName(String layoutExtFileName) {
		this.layoutExtFileName = layoutExtFileName;
	}

	public VelocityHelperImpl() {
		Properties properties = new Properties();

		try {
			logger.logMessage(LogLevel.INFO, "Velocity配置文件：{}",
					VelocityHelperImpl.class
							.getResource("/velocity.properties").toString());
			properties.load(VelocityHelperImpl.class
					.getResourceAsStream("/velocity.properties"));
			for (Object key : properties.keySet()) {
				String value = properties.getProperty(key.toString());
				if (value != null) {
					value = value.trim();
				}
				properties.setProperty(key.toString(), value);
			}
			velocity.init(properties);
		} catch (IOException e) {
			logger.errorMessage(e.getMessage(), e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.tinygroup.velocity.VelocityHelpe#addMacroFile(org.apache
	 * .commons.vfs2.FileObject)
	 */
	public void addMacroFile(FileObject macroFile) {
		macroList.add(macroFile.getPath());
		fullContextFileRepository.addFileObject(macroFile.getPath(), macroFile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.tinygroup.velocity.VelocityHelpe#removeMacroFile(org.apache
	 * .commons.vfs2.FileObject)
	 */
	public void removeMacroFile(FileObject macroFile) {
		macroList.remove(macroFile.getPath());
		fullContextFileRepository.removeFileObject(macroFile.getPath());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.tinygroup.velocity.VelocityHelpe#getFullContextFileRepository ()
	 */
	public FullContextFileRepository getFullContextFileRepository() {
		return fullContextFileRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.tinygroup.velocity.VelocityHelpe#setFullContextFileRepository
	 * (org.tinygroup.application.FullContextFileRepository)
	 */
	public void setFullContextFileRepository(
			FullContextFileRepository fullContextFileRepository) {
		this.fullContextFileRepository = fullContextFileRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.tinygroup.velocity.VelocityHelpe#processTempleate(org.apache
	 * .velocity.VelocityContext, java.io.Writer, java.lang.String)
	 */
	public void processTempleateWithLayout(Context context, Writer writer,
			String path) throws Exception {

		TinyVelocityContext tinyVelocityContext = new TinyVelocityContext(
				context);
		if (initContext != null) {
			context.putSubContext(BASE_CONTEXT, initContext);
		}
		VelocityContext velocityContext = new VelocityContext(
				tinyVelocityContext);
		String templatePath = findTemplatePath(path);
		Template template = velocity.getTemplate(templatePath, ENCODING);
		StringWriter stringWriter = new StringWriter();
		template.merge(velocityContext, stringWriter, macroList);

		List<String> layoutPathList = new ArrayList<String>();
		int lastIndexOf = path.lastIndexOf('/');
		String pagePath = path.substring(0, lastIndexOf);
		String pageName = path.substring(lastIndexOf);
		getLayoutPaths(layoutPathList, pagePath, pageName);
		for (String layoutPath : layoutPathList) {
			context.put(PATH_CONTENT, stringWriter.toString());
			stringWriter.close();
			Template layoutTemplate = velocity
					.getTemplate(layoutPath, ENCODING);
			StringWriter layoutWriter = new StringWriter();
			layoutTemplate.merge(velocityContext, layoutWriter, macroList);
			stringWriter = layoutWriter;
		}
		writer.append(stringWriter.getBuffer());
		processPagelet(writer);
		writer.flush();
	}

	// 处理内嵌pagelet
	public void processTempleate(InternalContextAdapter context, Writer writer,
			String path) throws Exception {
		String templatePath = findTemplatePath(path);
		Template template = velocity.getTemplate(templatePath, ENCODING);
		template.merge(context, writer, macroList);
		writer.flush();
	}

	// 同一个文件写出
	public void processBigpipeTempleate(InternalContextAdapter context,
			Writer writer, String path) throws Exception {
		String id = getUUID().replaceAll("-", "");
		writer.write("<div id=\"");
		writer.write(id);
		writer.write("\" ></div>");
		List<Processor> pageletProcessorList = local.get();
		if (pageletProcessorList == null) {
			pageletProcessorList = new ArrayList<Processor>();
			local.set(pageletProcessorList);
		}
		pageletProcessorList.add(new VelocityProcessAction("VelocityPagelet",
				context, path, id));
		writer.flush();
	}

	class VelocityProcessAction extends AbstractProcessor {

		private org.apache.velocity.context.Context context;
		private Writer writer;
		private String path;
		private String id;

		public VelocityProcessAction(String name,
				org.apache.velocity.context.Context context, String path,
				String id) {
			super(name);
			this.context = context;
			this.path = path;
			this.id = id;
		}

		
		protected void action() throws Exception {
			StringWriter stringWriter = new StringWriter();
			stringWriter.write("$('#");
			stringWriter.write(id);
			stringWriter.write("').html(\"");

			FileObject templateFileObject = fullContextFileRepository
					.getFileObjectDetectLocale(path);
			Template template = velocity.getTemplate(
					templateFileObject.getPath(), ENCODING);
			HtmlStringWriter tempWriter = new HtmlStringWriter();
			template.merge(context, tempWriter, macroList);
			stringWriter.write(tempWriter.toString());
			stringWriter.write("\");\n");
			synchronized (writer) {
				writer.write(stringWriter.toString());
			}
		}
	}

	private void processPagelet(Writer writer) throws IOException {
		List<Processor> pageletProcessorList = local.get();
		if (pageletProcessorList != null && pageletProcessorList.size() > 0) {
			writer.write("<script>\n$(function(){\n");
			MultiThreadProcessor multiThread = new MultiThreadProcessor(
					"Velocity");
			for (Processor processor : pageletProcessorList) {
				VelocityProcessAction vpa = (VelocityProcessAction) processor;
				vpa.writer = writer;
			}
			multiThread.addProcessor(pageletProcessorList);
			multiThread.start();
			writer.write(" });\n</script>\n");
			pageletProcessorList.clear();
		}
	}

	private String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 处理外部pagelet
	 */
	public void processTempleate(Context context, Writer writer, String path)
			throws Exception {
		TinyVelocityContext tinyVelocityContext = new TinyVelocityContext(
				context);
		if (initContext != null) {
			context.putSubContext(BASE_CONTEXT, initContext);
		}
		VelocityContext velocityContext = new VelocityContext(
				tinyVelocityContext);
		String templatePath = findTemplatePath(path);
		Template template = velocity.getTemplate(templatePath, ENCODING);
		template.merge(velocityContext, writer, macroList);
		writer.flush();
	}

	/**
	 * 处理独立页面
	 * 
	 * @param layoutPathList
	 * @param viewFile
	 */
	private void getLayoutPaths(List<String> layoutPathList, String viewPath,
			String viewFile) {
		FileObject object = null;
		if (viewFile.endsWith(VIEW_EXT_FILENAME)) {
			String privateLayout = viewPath
					+ viewFile.substring(0, viewFile.lastIndexOf('.') + 1)
					+ LAYOUT_EXT_FILENAME;
			object = fullContextFileRepository
					.getFileObjectDetectLocale(privateLayout);
			if (object!=null&&object.isExist()) {
				layoutPathList.add(object.getPath());
			}

		}
		if (object == null || !object.isExist()) {
			String findPath = String.format("%s/%s.%s", viewPath,
					defaultFileName, layoutExtFileName);
			FileObject fileObject = fullContextFileRepository
					.getFileObjectDetectLocale(findPath);
			if (fileObject!=null&&fileObject.isExist()) {
				layoutPathList.add(fileObject.getPath());
			}
		}
		int nextIndexSlash = viewPath.lastIndexOf('/');
		if (nextIndexSlash != -1) {
			getLayoutPaths(layoutPathList,
					viewPath.substring(0, nextIndexSlash), viewFile);
		}
	}

	private String findTemplatePath(String path) throws FileNotFoundException {
		FileObject fileObject = fullContextFileRepository
				.getFileObjectDetectLocale(path);
		if (fileObject != null && fileObject.isExist()) {
			return fileObject.getPath();
		} else {
			int lastIndexSlash = path.lastIndexOf('/');
			return getDefaultTemplatePath(path.substring(0, lastIndexSlash));
		}
	}

	private String getDefaultTemplatePath(String path)
			throws FileNotFoundException {
		String findPath = String.format("%s/%s.%s", path, defaultFileName,
				viewExtFileName);
		logger.logMessage(LogLevel.INFO, "由于文件{}找不到，因此查找默认文件",path,findPath);
		FileObject fileObject = fullContextFileRepository
				.getFileObjectDetectLocale(findPath);
		if (fileObject != null&&fileObject.isExist()) {
			return fileObject.getPath();
		} else {
			int lastIndexSlash = path.lastIndexOf('/');
			if (lastIndexSlash == -1) {
				throw new FileNotFoundException(path);
			} else {
				return getDefaultTemplatePath(path.substring(0, lastIndexSlash));
			}
		}
	}

	public void setVelocityContextConfig(
			VelocityContextConfig velocityContextConfig) {
		this.velocityContextConfig = velocityContextConfig;
		initContext();
	}

	private void initContext() {
		if (velocityContextConfig != null && initContext == null) {
			initContext = new ContextImpl();
			if (velocityContextConfig.getStaticClasses() != null) {
				for (StaticClass staticClass : velocityContextConfig
						.getStaticClasses()) {
					try {
						initContext.put(staticClass.getName(),
								Class.forName(staticClass.getClassName()));
					} catch (ClassNotFoundException e) {
						logger.errorMessage("bean:{},类{}添加到环境失败！", e,
								staticClass.getName(),
								staticClass.getClassName());
					}
				}
			}
			if (velocityContextConfig.getPlatformBeans() != null) {
				for (Bean bean : velocityContextConfig.getPlatformBeans()) {
					initVelocityBean(bean);
				}
			}
			if (velocityContextConfig.getSpringBeans() != null) {
				for (Bean bean : velocityContextConfig.getSpringBeans()) {
					initSpringVelocityBean(bean);
				}
			}
		}
	}

	private void initSpringVelocityBean(Bean bean) {
		try {
			if (bean.getType() != null) {
				initContext.put(bean.getName(),
						SpringUtil.getBean(Class.forName(bean.getType())));
			} else {
				initContext.put(bean.getName(),
						SpringUtil.getBean(bean.getName()));
			}
		} catch (ClassNotFoundException e) {
			if (bean.getType() != null) {
				logger.errorMessage("springbean:{}、类型:{},添加到环境失败！", e,
						bean.getName(), bean.getType());
			} else {
				logger.errorMessage("springbean:{},添加到环境失败！", e, bean.getName());
			}
		}
	}

	private void initVelocityBean(Bean bean) {
		try {
			if (bean.getType() != null) {
				initContext.put(bean.getName(),
						SpringUtil.getBean(Class.forName(bean.getType())));
			} else {
				initContext.put(bean.getName(),
						SpringUtil.getBean(bean.getName()));
			}
		} catch (ClassNotFoundException e) {
			if (bean.getType() != null) {
				logger.errorMessage("框架bean:{}、类型:{},添加到环境失败！", e,
						bean.getName(), bean.getType());
			} else {
				logger.errorMessage("框架bean:{},添加到环境失败！", e, bean.getName());
			}
		}
	}

	public void evaluteString(Context context, Writer writer, String string) {
        TinyVelocityContext tinyVelocityContext = new TinyVelocityContext(
                context);
        VelocityContext velocityContext = new VelocityContext(
                tinyVelocityContext);

        velocity.evaluate(velocityContext, writer, "mystring", string);
	}

}
