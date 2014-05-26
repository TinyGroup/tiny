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
package org.tinygroup.command;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.command.config.Command;
import org.tinygroup.command.config.CommandGoal;
import org.tinygroup.command.config.Commands;
import org.tinygroup.command.config.Parameter;

import com.thoughtworks.xstream.XStream;

public class TestGenerate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Commands commands = new Commands();
		XStream stream = new XStream();
		stream.autodetectAnnotations(true);

		List<Command> commandList = new ArrayList<Command>();
		commands.setCommandList(commandList);
		Command command = new Command();
		commandList.add(command);
		command.setName("help");
		command.setDescription("abc");
		command.setShortDescription("aa");
		List<CommandGoal> commandGoals = new ArrayList<CommandGoal>();
		command.setCommandGoals(commandGoals);
		CommandGoal commandGoal = new CommandGoal();
		;
		commandGoals.add(commandGoal);
		commandGoal.setName("aa");
		commandGoal.setDefaultParameter("bb");
		commandGoal.setShortDescription("aa");
		commandGoal.setDescription("cc");
		
		List<Parameter> parameters=new ArrayList<Parameter>();
		commandGoal.setParameters(parameters);
		
		Parameter cmdParameter=new Parameter();
		parameters.add(cmdParameter);
		cmdParameter.setDefaultValue("aa");
		cmdParameter.setDescription("aa");
		cmdParameter.setName("aa");
		cmdParameter.setNecessary(false);
		cmdParameter.setShortDescription("aa");
		
		System.out.println(stream.toXML(commands));
	}
}
