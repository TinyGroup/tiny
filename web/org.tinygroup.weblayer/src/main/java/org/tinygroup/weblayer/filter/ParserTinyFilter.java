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
package org.tinygroup.weblayer.filter;

import org.springframework.beans.PropertyEditorRegistrar;
import org.tinygroup.commons.tools.ArrayUtil;
import org.tinygroup.commons.tools.ObjectUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.util.ParserXmlNodeUtil;
import org.tinygroup.weblayer.webcontext.parser.ParserWebContext;
import org.tinygroup.weblayer.webcontext.parser.impl.ParserWebContextImpl;
import org.tinygroup.weblayer.webcontext.parser.impl.PropertyEditorRegistrarsSupport;
import org.tinygroup.weblayer.webcontext.parser.upload.ParameterParserFilter;
import org.tinygroup.weblayer.webcontext.parser.upload.UploadService;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 解析用户提交的参数，无论是普通的请求，还是multipart/form-data这样的用于上传文件的 请求。
 * 
 * @author renhui
 * 
 */
public class ParserTinyFilter extends AbstractTinyFilter {

	private static final String PROPERTY = "property";
	private static final boolean CONVERTER_QUIET_DEFAULT = true;
	private static final String CONVERTER_QUIET_PARAM = "converterQuietParam";
	private static final String URL_CASE_FOLDING_DEFAULT = ParserWebContext.URL_CASE_FOLDING_LOWER_WITH_UNDERSCORES;
	private static final String CASE_FOLDING = "caseFolding";
	private static final boolean AUTO_UPLOAD_DEFAULT = true;
	private static final String AUTO_UPLOAD = "autoUpload";
	private static final boolean UNESCAPE_PARAMETERS_DEFAULT = true;
	private static final String UNESCAPE_PARAMETERS = "unescapeParameters";
	private static final boolean USE_SERVLET_ENGINE_PARSER_DEFAULT = false;
	private static final String USE_SERVLET_ENGINE_PARSER = "useServletEngineParser";
	private static final boolean USE_BODY_ENCODING_FOR_URI_DEFAULT = true;
	private static final String USE_BODY_ENCODING_FOR_URI = "useBodyEncodingForUri";
	private static final String URI_ENCODING_DEFAULT = "UTF-8";
	private static final String URI_ENCODING = "uriEncoding";
	private static final boolean TRIMMING_DEFAULT = true;
	private static final String TRIMMING = "trimming";
	private static final String HTML_FIELD_SUFFIX_DEFAULT = ".~html";
	private static final String HTML_FIELD_SUFFIX = "htmlFieldSuffix";
	private static final String PARSER_CONFIG = "parser";
	private static final String PROPERTY_EDITOR = "property-editor";
	private static final String PARAM_PARSER_FILTER = "param-parser-filter";
	private static final String UPLOAD_SERVICE = "upload-service";

	private PropertyEditorRegistrarsSupport propertyEditorRegistrars = new PropertyEditorRegistrarsSupport();
	/**
	 * 类型转换出错时，是否不报错，而是返回默认值。
	 */
	private Boolean converterQuiet;
	/**
	 * 参数和cookies名称的大小写转换选项。默认值为: lower_with_underscores 配置文件属性可选项: none:
	 * 不对parameters和cookies的名称进行大小写转换。 lower: 将parameters和cookies的名称转换成小写。
	 * lower_with_underscores:将parameters和cookies的名称转换成小写加下划线。 upper:
	 * 将parameters和cookies的名称转换成大写。
	 * upper_with_underscores:将parameters和cookies的名称转换成大写加下划线。
	 */
	private String caseFolding;
	/**
	 * 是否自动处理上传文件。默认值为true。
	 */
	private Boolean autoUpload;
	/**
	 * 是否对参数进行HTML entities解码，默认为true
	 */
	private Boolean unescapeParameters;
	/**
	 * 是否让servlet engine来解析GET参数。默认值为false
	 */
	private Boolean useServletEngineParser;
	/**
	 * 是否以request.setCharacterEncoding所指定的编码来解析query。默认值为true
	 */
	private Boolean useBodyEncodingForURI;
	/**
	 * 如果不以request.setCharacterEncoding所指定的编码来解析query，那么就用这个。默认值为“UTF-8”
	 */
	private String uriEncoding;
	/**
	 * 是否对参数值进行trimming，默认值为true
	 */
	private Boolean trimming;
	/**
	 * 解析参数的过滤器
	 */
	private ParameterParserFilter[] filters;
	/**
	 * HTML类型的字段名后缀，默认值为：".~html"。
	 */
	private String htmlFieldSuffix;
	/**
	 * 文件上传服务
	 */
	private UploadService uploadService;

	public void setPropertyEditorRegistrars(PropertyEditorRegistrar[] registrars) {
		propertyEditorRegistrars.setPropertyEditorRegistrars(registrars);
	}

	public void setConverterQuiet(boolean converterQuiet) {
		this.converterQuiet = converterQuiet;
	}

	public void setCaseFolding(String caseFolding) {
		this.caseFolding = caseFolding;
	}

	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}

	public void setUnescapeParameters(boolean unescapeParameters) {
		this.unescapeParameters = unescapeParameters;
	}

	public void setUseServletEngineParser(boolean useServletEngineParser) {
		this.useServletEngineParser = useServletEngineParser;
	}

	public void setUseBodyEncodingForURI(boolean useBodyEncodingForURI) {
		this.useBodyEncodingForURI = useBodyEncodingForURI;
	}

	public void setURIEncoding(String uriEncoding) {
		this.uriEncoding = uriEncoding;
	}

	public void setTrimming(boolean trimming) {
		this.trimming = trimming;
	}

	public void setParameterParserFilters(ParameterParserFilter[] filters) {
		this.filters = filters;
	}

	public void setHtmlFieldSuffix(String htmlFieldSuffix) {
		this.htmlFieldSuffix = htmlFieldSuffix;
	}

	public void setUploadService(UploadService uploadService) {
		this.uploadService = uploadService;
	}

	
	public void initTinyFilter() {
		super.initTinyFilter();
		init();
	}

	private void init() {

		if (converterQuiet == null) {
			// 类型转换出错时，是否不报错，而是返回默认值。
			converterQuiet = ObjectUtil.defaultIfNull(
					Boolean.parseBoolean(get(CONVERTER_QUIET_PARAM)),
					CONVERTER_QUIET_DEFAULT);
		}
		if (caseFolding == null) {
			// 参数和cookies名称的大小写转换选项
			caseFolding = StringUtil.defaultIfEmpty(get(CASE_FOLDING),
					URL_CASE_FOLDING_DEFAULT).toLowerCase();
		}

		if (autoUpload == null) {
			// 是否自动处理上传文件
			autoUpload = ObjectUtil
					.defaultIfNull(Boolean.parseBoolean(get(AUTO_UPLOAD)),
							AUTO_UPLOAD_DEFAULT);
		}

		if (unescapeParameters == null) {
			// 是否对参数进行HTML entities解码，默认为true
			unescapeParameters = ObjectUtil.defaultIfNull(
					Boolean.parseBoolean(get(UNESCAPE_PARAMETERS)),
					UNESCAPE_PARAMETERS_DEFAULT);
		}

		if (useServletEngineParser == null) {
			// 是否让servlet engine来解析GET参数
			useServletEngineParser = ObjectUtil.defaultIfNull(
					Boolean.parseBoolean(get(USE_SERVLET_ENGINE_PARSER)),
					USE_SERVLET_ENGINE_PARSER_DEFAULT);
		}

		if (useBodyEncodingForURI == null) {
			// 是否以request.setCharacterEncoding所指定的编码来解析query
			useBodyEncodingForURI = ObjectUtil.defaultIfNull(
					Boolean.parseBoolean(get(USE_BODY_ENCODING_FOR_URI)),
					USE_BODY_ENCODING_FOR_URI_DEFAULT);
		}

		if (uriEncoding == null) {
			// 如果不以request.setCharacterEncoding所指定的编码来解析query，那么就用这个
			uriEncoding = ObjectUtil.defaultIfNull(get(URI_ENCODING),
					URI_ENCODING_DEFAULT);
		}

		if (trimming == null) {
			// 是否对参数值进行trimming
			trimming = ObjectUtil.defaultIfNull(
					Boolean.parseBoolean(get(TRIMMING)), TRIMMING_DEFAULT);
		}

		if (htmlFieldSuffix == null) {
			// HTML类型的字段名后缀
			htmlFieldSuffix = StringUtil.defaultIfEmpty(get(HTML_FIELD_SUFFIX),
					HTML_FIELD_SUFFIX_DEFAULT);
		}

		ConfigurationManager appConfigManager = ConfigurationUtil.getConfigurationManager();
		XmlNode parserNode = appConfigManager.getApplicationConfig().getSubNode(
				PARSER_CONFIG);
		parserExtraConfig(parserNode);

	}

	
	protected void parserExtraConfig(XmlNode parserNode) {

		if (propertyEditorRegistrars.size() == 0) {
			PropertyEditorRegistrar[] strars = ParserXmlNodeUtil
					.parseConfigToArray(PROPERTY_EDITOR, PROPERTY, parserNode,
							PropertyEditorRegistrar.class);
			propertyEditorRegistrars.setPropertyEditorRegistrars(strars);
		}
		if (filters == null) {
			setParameterParserFilters(ParserXmlNodeUtil.parseConfigToArray(
					PARAM_PARSER_FILTER, parserNode,
					ParameterParserFilter.class));
		}
		setUploadService(ParserXmlNodeUtil.parseConfigToObject(UPLOAD_SERVICE,
				PROPERTY, parserNode, UploadService.class));

	}

	
	public void preProcess(WebContext context) {

	}

	
	public void postProcess(WebContext context) {

	}

	
	public WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
		ParserWebContextImpl parserWebContext = new ParserWebContextImpl(
				wrappedContext);
		parserWebContext.setPropertyEditorRegistrar(propertyEditorRegistrars);
		parserWebContext.setConverterQuiet(converterQuiet);
		parserWebContext.setAutoUpload(autoUpload);
		parserWebContext.setCaseFolding(caseFolding);
		parserWebContext.setUnescapeParameters(unescapeParameters);
		parserWebContext.setUseServletEngineParser(useServletEngineParser);
		parserWebContext.setUseBodyEncodingForURI(useBodyEncodingForURI);
		parserWebContext.setURIEncoding(uriEncoding);
		parserWebContext.setTrimming(trimming);

		if (autoUpload) {
			parserWebContext.setUploadService(uploadService);
		}

		if (!ArrayUtil.isEmptyArray(filters)) {
			parserWebContext.setParameterParserFilters(filters);
		}

		parserWebContext.setHtmlFieldSuffix(htmlFieldSuffix);

		return parserWebContext;
	}

	
	public int getOrder() {
		return PARSER_FILTER_PRECEDENCE;
	}

}
