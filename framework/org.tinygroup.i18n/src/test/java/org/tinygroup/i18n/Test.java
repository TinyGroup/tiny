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
package org.tinygroup.i18n;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.format.config.FormatPatternDefine;
import org.tinygroup.format.config.FormatProvider;
import org.tinygroup.format.config.Formater;
import org.tinygroup.i18n.config.I18nConfiguration;
import org.tinygroup.i18n.config.I18nMessage;

import com.thoughtworks.xstream.XStream;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		I18nConfiguration configuration = new I18nConfiguration();
		XStream stream = new XStream();
		stream.autodetectAnnotations(true);

		List<I18nMessage> i18nMessageContexts = new ArrayList<I18nMessage>();
		I18nMessage aa = new I18nMessage();
		aa.setClassName("org.tinygroup.i18n.I18nMessageContextImpl");
		i18nMessageContexts.add(aa);
		configuration.setI18nMessageContexts(i18nMessageContexts);
		List<I18nMessage> i18nMessageStandards = new ArrayList<I18nMessage>();
		I18nMessage bb = new I18nMessage();
		bb.setClassName("org.tinygroup.i18n.impl.I18nMessageImpl");
		i18nMessageStandards.add(bb);
		configuration.setI18nMessageStandards(i18nMessageStandards);
		Formater formater = new Formater();
		FormatPatternDefine formatPatternDefine = new FormatPatternDefine();
		formatPatternDefine.setPrefixPatternString("${");
		formatPatternDefine.setPostfixPatternString("}");
		formatPatternDefine
				.setPatternString("([$]+[{]+[a-zA-Z0-9[.[_[:[/[#]]]]]]+[}])");
		formater.setClassName("org.tinygroup.format.impl.FormaterImpl");
		formater.setFormatPatternDefine(formatPatternDefine);
		List<FormatProvider> formatProviders = new ArrayList<FormatProvider>();
		FormatProvider formaterP1 = new FormatProvider();
		formaterP1.setArea("");
		formaterP1.setClassName("org.tinygroup.format.impl.ContextFormater");
		formatProviders.add(formaterP1);
		FormatProvider formaterP2 = new FormatProvider();
		formaterP2.setArea("context");
		formaterP2.setClassName("org.tinygroup.format.impl.ContextFormater");
		formatProviders.add(formaterP2);
		formater.setFormatProviders(formatProviders);
		configuration.setFormater(formater);
		System.out.println(stream.toXML(configuration));
	}

}
