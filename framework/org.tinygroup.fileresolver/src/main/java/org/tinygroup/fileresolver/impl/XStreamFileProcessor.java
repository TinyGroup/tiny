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

import java.lang.reflect.Array;
import java.util.Collection;

import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;
import org.tinygroup.xstream.config.XStreamAnnotationClass;
import org.tinygroup.xstream.config.XStreamClassAlias;
import org.tinygroup.xstream.config.XStreamClassAliases;
import org.tinygroup.xstream.config.XStreamConfiguration;
import org.tinygroup.xstream.config.XStreamPropertyAlias;
import org.tinygroup.xstream.config.XStreamPropertyImplicit;
import org.tinygroup.xstream.config.XStreamPropertyOmit;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * 功能说明:xstream文件处理器.优先级别低于i18n,但是高于其他处理器 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-20 <br>
 * <br>
 */
public class XStreamFileProcessor extends AbstractFileProcessor {

	private static final String XSTREAM_FILE_EXTENSION = ".xstream.xml";
	private String xstreamFileExtension = XSTREAM_FILE_EXTENSION;

	public String getXstreamFileExtension() {
		return xstreamFileExtension;
	}

	public void setXstreamFileExtension(String xstreamFileExtension) {
		this.xstreamFileExtension = xstreamFileExtension;
	}

	public boolean isMatch(FileObject fileObject) {
		if (fileObject.getFileName().endsWith(XSTREAM_FILE_EXTENSION)) {
			return true;
		}
		return false;
	}

	private void process(FileObject fileObject) {
		try {
			logger.logMessage(LogLevel.INFO, "找到XStream配置文件[{0}]，并开始加载...",
					fileObject.getAbsolutePath());
			XStream loadXStream = XStreamFactory.getXStream();
			XStreamConfiguration xstreamConfiguration = (XStreamConfiguration) loadXStream
					.fromXML(fileObject.getInputStream());
			XStream xStream = XStreamFactory.getXStream(xstreamConfiguration
					.getPackageName());

			loadAnnotationClass(xStream, xstreamConfiguration);
			if (xstreamConfiguration.getxStreamClassAliases() != null) {
				processClassAliases(xStream,
						xstreamConfiguration.getxStreamClassAliases());
			}
			logger.logMessage(LogLevel.INFO, "XStream配置文件[{0}]，加载完毕。",
					fileObject.getAbsolutePath());
		} catch (Exception e) {
			logger.errorMessage(
					String.format("processing file <%s>",
							fileObject.getAbsolutePath()), e);
		}
	}

	private void processClassAliases(XStream xStream,
			XStreamClassAliases classAliases) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		if (classAliases.getClassAliases() != null) {
			for (XStreamClassAlias classAlias : classAliases.getClassAliases()) {
				Class<?> clazz = Class.forName(classAlias.getType());
				xStream.alias(classAlias.getAliasName(), clazz);
				processClassAlias(xStream, classAlias, clazz);
			}
		}
	}

	private void processClassAlias(XStream xStream,
			XStreamClassAlias classAlias, Class<?> clazz)
			throws InstantiationException, IllegalAccessException {
		if (classAlias.getProperAliases() != null) {
			processPropertyAlias(xStream, classAlias, clazz);
		}
		if (classAlias.getPropertyImplicits() != null) {
			processPropertyImplicit(xStream, classAlias, clazz);
		}
		if (classAlias.getPropertyOmits() != null) {
			processPropertyOmit(xStream, classAlias, clazz);
		}
	}

	private void processPropertyOmit(XStream xStream,
			XStreamClassAlias classAlias, Class<?> clazz) {
		for (XStreamPropertyOmit propertyOmit : classAlias.getPropertyOmits()) {
			xStream.omitField(clazz, propertyOmit.getAttributeName());
		}
	}

	private void processPropertyImplicit(XStream xStream,
			XStreamClassAlias classAlias, Class<?> clazz)
			throws InstantiationException, IllegalAccessException {
		for (XStreamPropertyImplicit propertyImplicit : classAlias
				.getPropertyImplicits()) {
			String name = propertyImplicit.getAttributeName();
			Object newInstance = clazz.newInstance();
			if (newInstance instanceof Array) {
				xStream.addImplicitArray(clazz, name);
			} else if (newInstance instanceof Collection) {
				xStream.addImplicitCollection(clazz, name);
			}
		}
	}

	private void processPropertyAlias(XStream xStream,
			XStreamClassAlias classAlias, Class<?> clazz) {
		for (XStreamPropertyAlias propertyAlias : classAlias.getProperAliases()) {
			xStream.aliasAttribute(clazz, propertyAlias.getAttributeName(),
					propertyAlias.getAliasName());
		}
	}

	private void loadAnnotationClass(XStream xStream,
			XStreamConfiguration xstreamConfiguration)
			throws ClassNotFoundException {
		if (xstreamConfiguration.getxStreamAnnotationClasses() != null) {
			for (XStreamAnnotationClass annotationClass : xstreamConfiguration
					.getxStreamAnnotationClasses()) {
				xStream.processAnnotations(Class.forName(annotationClass
						.getClassName()));
			}
		}
	}

	public void process() {
		for (FileObject fileObject : fileObjects) {
			process(fileObject);
		}

	}

	public void setFileResolver(FileResolver fileResolver) {

	}

	
	public int getOrder() {
		return HIGHEST_PRECEDENCE+20;
	}



}
