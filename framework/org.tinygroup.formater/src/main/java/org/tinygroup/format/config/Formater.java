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
package org.tinygroup.format.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 格式化器配置参数
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("formater")
public class Formater {
	@XStreamAlias("class-name")
	@XStreamAsAttribute
	private String className;// 格式化器实现类
	private FormatPatternDefine formatPatternDefine;// 格式化正则表达式定义
	@XStreamAlias("format-providers")
	private List<FormatProvider> formatProviders;// 格式化提供者

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public FormatPatternDefine getFormatPatternDefine() {
		return formatPatternDefine;
	}

	public void setFormatPatternDefine(FormatPatternDefine formatPatternDefine) {
		this.formatPatternDefine = formatPatternDefine;
	}

	public List<FormatProvider> getFormatProviders() {
		return formatProviders;
	}

	public void setFormatProviders(List<FormatProvider> formatProviders) {
		this.formatProviders = formatProviders;
	}

}
