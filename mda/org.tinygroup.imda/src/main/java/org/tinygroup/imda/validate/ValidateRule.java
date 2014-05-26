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
package org.tinygroup.imda.validate;

import org.tinygroup.springutil.SpringUtil;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 校验规则的定义
 * 
 * @author luog
 * 
 */
@XStreamAlias("validate-rule")
public class ValidateRule {
	@XStreamAsAttribute
	@XStreamAlias("rule-name")
	String ruleName;
	@XStreamAsAttribute
	@XStreamAlias("rule-value")
	String ruleValue;
	@XStreamAsAttribute
	String messege;

	public String getMessege() {
		if (messege == null || messege.length() == 0) {
			ValidateManager validateManager = SpringUtil
					.getBean("validateManager");
			messege = validateManager.getDefaultMessage(ruleName);
		}
		if (ruleValue != null && ruleValue.length() > 0) {
			if (ruleValue.indexOf(",") == 0) {
				messege = messege.replaceAll("[{]0[}]", ruleValue);
			} else {
				String trimValue = ruleValue.substring(1,
						ruleValue.length() - 1);
				String[] range = trimValue.split(",");
				for (int i = 0; i < range.length; i++) {
					messege = messege.replaceAll("[{]" + i + "[}]", range[i]);
				}
			}
		}
		return messege;
	}

	public void setMessege(String messege) {
		this.messege = messege;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleValue() {
		return ruleValue;
	}

	public void setRuleValue(String ruleValue) {
		this.ruleValue = ruleValue;
	}

}
