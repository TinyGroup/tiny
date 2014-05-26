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
package org.tinygroup.uiengine;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.uiengine.config.Macro;
import org.tinygroup.uiengine.config.MacroParameter;
import org.tinygroup.uiengine.config.Macros;
import org.tinygroup.uiengine.config.SubMacro;
import org.tinygroup.uiengine.config.UIComponent;
import org.tinygroup.uiengine.config.UIComponents;

import com.thoughtworks.xstream.XStream;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UIComponents components = new UIComponents();
		UIComponent component = new UIComponent();
		component.setName("Input");
		component.setCategory("基本");
		component.setDescription("输入组件描述信息");
		component.setHelp("http://localhost/input.page");
		component.setIcon("/abc/input.ico");
		component.setTitle("输入");
		component.setCssResource("input.css");
		component.setJsResource("input.js");
		component.setJsCodelet("alert(1);");
		component.setCssCodelet("#div{color: red }");
		components.setComponents(new ArrayList<UIComponent>());
		components.getComponents().add(component);
		Macros macros=new Macros();
		component.setMacros(macros);
		List<Macro> macroList=new ArrayList<Macro>();
		macros.setMacroList(macroList);
		Macro macro=new Macro();
		macro.setHasBody(true);
		macro.setIcon("/input/text.ico");
		macro.setName("text");
		macro.setTitle("文本输入框");
		List<SubMacro> submacros=new ArrayList<SubMacro>();
		List<MacroParameter> macroParameterList=new ArrayList<MacroParameter>();
		MacroParameter macroParameter=new MacroParameter();
		macroParameter.setName("label");
		macroParameter.setTitle("标签");
		macroParameter.setType("String");
		macroParameter.setDescription("用于在输入框前面用作提示");
		macroParameterList.add(macroParameter);
		macro.setMacroParameterList(macroParameterList);
		macro.setSubmacros(submacros);
		SubMacro subMacro=new SubMacro();
		subMacro.setRegex("aa");
		submacros.add(subMacro);
		macro.setShortDescription("text长描述");
		macro.setLongDescription("text长描述");
		macroList.add(macro);
		XStream xStream = new XStream();
		xStream.processAnnotations(UIComponents.class);

		System.out.println(xStream.toXML(components));
	}

}
