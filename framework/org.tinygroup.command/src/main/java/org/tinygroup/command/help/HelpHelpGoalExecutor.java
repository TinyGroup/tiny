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
package org.tinygroup.command.help;

import org.tinygroup.command.CommandGoalExecutor;
import org.tinygroup.command.CommandSystem;
import org.tinygroup.command.config.Command;
import org.tinygroup.command.config.CommandGoal;
import org.tinygroup.command.config.Parameter;
import org.tinygroup.context.Context;

/**
 * 帮助命令的帮助指令
 * 
 * @author luoguo
 * 
 */
public class HelpHelpGoalExecutor implements CommandGoalExecutor {
	private String goal;
	private String command;
	private String parameter;
	private boolean showDetail = false;
	private boolean showGoal = false;
	private boolean showParameter = false;

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public boolean isShowParameter() {
		return showParameter;
	}

	public void setShowParameter(boolean showParameter) {
		this.showParameter = showParameter;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isShowDetail() {
		return showDetail;
	}

	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	public boolean isShowGoal() {
		return showGoal;
	}

	public void setShowGoal(boolean showGoal) {
		this.showGoal = showGoal;
	}

	public void execute(CommandSystem commandSystem, Context context) {
		commandSystem.println("用法: [命令:]Goal [[参数名=参数值]...]");
		commandSystem.print("命令");
		commandSystem.indentPrint("Goal", 1);
		commandSystem.indentPrint("参数", 1);
		commandSystem.println();
		for (String key : commandSystem.getCommands().keySet()) {
			commandSystem.println();
			if (command == null || command.equals(key)) {
				printCommand(commandSystem, key);
			}
		}
	}

	/**
	 * 输出命令
	 * 
	 * @param commandSystem
	 * @param key
	 */
	private void printCommand(CommandSystem commandSystem, String key) {
		commandSystem.indentPrint(key, 0);
		Command cmd = commandSystem.getCommands().get(key);
		commandSystem.println();
		if (!showDetail) {
			commandSystem.indentPrint(cmd.getShortDescription(), COMMANDDESPOS);
		} else {
			commandSystem.indentPrint(cmd.getDescription(), COMMANDDESPOS);
		}
		commandSystem.println();
		printCommandGoals(cmd, commandSystem);
	}

	/**
	 * 输出子命令
	 * 
	 * @param commandGoal
	 * @param commandSystem
	 */
	public void printCommandGoals(Command cmd, CommandSystem commandSystem) {

		if (!showGoal) {
			return;
		}
		for (CommandGoal commandGoal : cmd.getCommandGoals()) {
			if (goal == null || goal.equals(commandGoal.getName())) {

				commandSystem
						.indentPrint(commandGoal.getName(), COMMANDGOALPOS);
				commandSystem.println();
				if (!showDetail) {
					commandSystem.indentPrint(
							commandGoal.getShortDescription(),
							COMMANDGOALDESPOS);
				} else {
					commandSystem.indentPrint(commandGoal.getDescription(),
							COMMANDGOALDESPOS);
				}
				commandSystem.println();
				printCommandGoalParams(commandGoal, commandSystem);
			}
		}

	}

	public void printCommandGoalParams(CommandGoal commandGoal,
			CommandSystem commandSystem) {
		if (!showParameter) {
			return;
		}
		if (commandGoal.getParameters() == null) {
			return;
		}
		for (Parameter para : commandGoal.getParameters()) {
			if (parameter == null || parameter.equals(para.getName())) {
				commandSystem.indentPrint(para.getName(), COMMANDGOALPARAMPOS);
				commandSystem.println();
				if (!showDetail) {
					commandSystem.indentPrint(para.getShortDescription(),
							COMMANDGOALPARAMDESPOS);
				} else {
					commandSystem.indentPrint(para.getDescription(),
							COMMANDGOALPARAMDESPOS);
				}
				commandSystem.println();
			}
		}

	}

}
