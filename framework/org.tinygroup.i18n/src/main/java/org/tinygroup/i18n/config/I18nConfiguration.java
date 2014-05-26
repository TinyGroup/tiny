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
package org.tinygroup.i18n.config;

import java.util.List;

import org.tinygroup.format.config.Formater;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * i18n配置
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("i18n-configuration")
public class I18nConfiguration {
	@XStreamAlias("i18n-message-standards")
	private List<I18nMessage> i18nMessageStandards;
	@XStreamAlias("i18n-message-contexts")
	private List<I18nMessage> i18nMessageContexts;
	private Formater formater;// 格式化器

	public Formater getFormater() {
		return formater;
	}

	public void setFormater(Formater formater) {
		this.formater = formater;
	}

	public List<I18nMessage> getI18nMessageStandards() {
		return i18nMessageStandards;
	}

	public void setI18nMessageStandards(List<I18nMessage> i18nMessageStandards) {
		this.i18nMessageStandards = i18nMessageStandards;
	}

	public List<I18nMessage> getI18nMessageContexts() {
		return i18nMessageContexts;
	}

	public void setI18nMessageContexts(List<I18nMessage> i18nMessageContexts) {
		this.i18nMessageContexts = i18nMessageContexts;
	}

}
