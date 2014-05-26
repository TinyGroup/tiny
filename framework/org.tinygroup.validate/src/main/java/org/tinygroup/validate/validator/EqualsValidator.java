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
package org.tinygroup.validate.validator;

import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.impl.AbstractValidator;

/**
 * equal校验器
 * @author renhui
 *
 */
public class EqualsValidator extends AbstractValidator {
	
	private static final String EQUALS_VALIDATOR_MESSAGE_KEY="equals_validator_message_key";
	
	private static final String DEFAULT_MESSAGE="{0}:{1}的值{2}与校验值{3}不相等。";
	
	
	private String value;


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}





	public <T> void validate(String name, String title, T value,
			ValidateResult validateResult) {
		
		if (value != null) {
			if(value instanceof String){
				String stringValue=value.toString();
				if(this.value!=null&&!this.value.equals(stringValue)){
					validateResult.addError(name, i18nMessages.getMessage(EQUALS_VALIDATOR_MESSAGE_KEY,DEFAULT_MESSAGE,name, title,value,this.value));
				}
			}
			if(value instanceof Number){
				String stringValue=((Number)value).toString();
				if(this.value!=null&&!this.value.equals(stringValue)){
					validateResult.addError(name, i18nMessages.getMessage(EQUALS_VALIDATOR_MESSAGE_KEY,DEFAULT_MESSAGE,name, title,value,this.value));
				}
			}
		}
		
	}

}
