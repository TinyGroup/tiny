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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.tinygroup.command.config.Command;
import org.tinygroup.command.config.CommandGoal;
import org.tinygroup.command.config.Commands;
import org.tinygroup.command.config.Parameter;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 命令执行系统，采用多例实现，因此可以支持多个命令体系
 * 
 * @author luoguo
 * 
 */
public final class CommandSystem {

	public static String COMMANDS_XSTREAM = "commands";
	private Logger logger = LoggerFactory.getLogger(CommandSystem.class);
	private static final int ASCII_MAX = 256;
	private static final int MAX_LINE_LENGTH = 80;

	// 命令系统
	private static Map<String, CommandSystem> commandHelperTable = new HashMap<String, CommandSystem>();

	private Map<String, Command> commands = new HashMap<String, Command>();// 命令集名称对应的命令集
	private Map<String, List<String>> goalInCommand = new HashMap<String, List<String>>();// 命令所在的命令集名称集合
	private Context context = new ContextImpl();
	private static Pattern commandPattern = Pattern.compile("(\\w|[:]|[-])+");
	private static Pattern parameterPattern = Pattern
			.compile("(\\b(\\w|[\u4e00-\u9fa5]|[/]|[.]|[-])+\\s*=\"[^\"]*\")|(\\b(\\w|[\u4e00-\u9fa5]|[/]|[.]|[-])+='[^\']*')|(\\b(\\w|[\u4e00-\u9fa5]|[/]|[.]|[-])+=(\\w|[\u4e00-\u9fa5]|[.]|[*]|[%]|[-]|[\\\\]|[/])+)|(\"[^\"]*\"|'[^\']*'|(\\w|[\u4e00-\u9fa5]|[.]|[*]|[%]|[-]|[\\\\]|[/])+)");
	private OutputStream out;

	/**
	 * 获得输入流
	 * 
	 * @return
	 */
	public OutputStream getOutputStream() {
		return out;
	}

	/**
	 * 获得命令执行系统实例
	 * 
	 * @param type
	 * @param out
	 * @return
	 */
	public static CommandSystem getInstance(String name, Commands commands,
			OutputStream out) {
		CommandSystem commandSystem = commandHelperTable.get(name);
		if (commandSystem == null) {
			commandSystem = new CommandSystem(commands);
			commandHelperTable.put(name, commandSystem);
		} else if (commands != null) {
			for (Command command : commands.getCommandList()) {
				commandSystem.addCommand(command);
			}

		}
		if (out != null) {
			commandSystem.out = out;
		}

		return commandSystem;
	}

	public static CommandSystem getInstance(String name) {
		return commandHelperTable.get(name);
	}

	/**
	 * 构造函数
	 * 
	 * @param type
	 */
	private CommandSystem(Commands commands) {
		for (Command cmd : commands.getCommandList()) {
			addCommand(cmd);
		}
	}

	public Map<String, Command> getCommands() {
		return commands;
	}

	/**
	 * 返回环境
	 * 
	 * @return
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * 添加命令
	 * 
	 * @param set
	 */
	private void addCommand(Command set) {
		// 处理command与loader的映射
		commands.put(set.getName(), set);
		for (CommandGoal commandgoal : set.getCommandGoals()) {
			List<String> cmdSetNames = goalInCommand.get(commandgoal.getName());
			if (cmdSetNames == null) {
				cmdSetNames = new ArrayList<String>();
				goalInCommand.put(commandgoal.getName(), cmdSetNames);
			}
			cmdSetNames.add(set.getName());
		}
	}

	/**
	 * 执行一个命令
	 * 
	 * @param cmd
	 */
	public void execute(String cmd) {
		// 分解命令为命令和参数
		List<String> cmds = parseCmd(cmd);
		if (cmds.size() == 0) {
			println("请输入有效的命令及goal！ 获取帮助为： h|help");
			return;
		}
		execute(cmds);
	}

	private void execute(List<String> cmds) {
		Map<String, String> parameter = new HashMap<String, String>();
		String[] str = cmds.get(0).split(":");
		Command command = null;
		CommandGoal goal = null;
		switch (str.length) {
		case 1:// 只有goal，没有命令
			List<String> setsName = goalInCommand.get(str[0]);
			if (setsName == null) {
				println("info:invalid-commands");// 无效命令
				return;
			} else if (setsName.size() != 1) {
				print("Goal:" + str[0] + "在命令[");
				for (int i = 0; i < setsName.size(); i++) {
					if (i > 0) {
						print(",");
					}
					print(setsName.get(i));
				}
				println("]中出现，请在goal前添加命令。");
				return;
			} else {
				command = commands.get(setsName.get(0));
				for (CommandGoal c : command.getCommandGoals()) {
					if (c.getName().equals(str[0])) {
						goal = c;
						break;
					}
				}
			}
			break;
		case 2:// 有命令集，也有命令
			command = commands.get(str[0]);
			if (command != null) {
				for (CommandGoal c : command.getCommandGoals()) {
					if (c.getName().equals(str[1])) {
						goal = c;
						break;
					}
				}
			}
			break;
		}
		// 输入的命令有效
		if (goal != null && command != null) {
			// 输入参数 统一放到parameter里
			for (int i = 1; i < cmds.size(); i++) {
				String strCmd = cmds.get(i);
				int index = strCmd.indexOf('=');
				if (index > 0) {
					String key = strCmd.substring(0, index);
					if (goal.getParameters() != null) {
						for (Parameter p : goal.getParameters()) {
							if (p.getName().equals(key)) {
								String value = strCmd.substring(index + 1);
								parameter.put(key, value);
								break;
							}
						}
					}
				} else {
					if (goal.getDefaultParameter() != null) {
						if (goal.getDefaultParameter() == null
								|| goal.getDefaultParameter().length() == 0) {
							println("无效的参数：" + strCmd);
							return;
						}
						parameter.put(goal.getDefaultParameter(), strCmd);
					}
				}
			}
			// 把没有指定参数的参数值用默认值
			if (goal.getParameters() != null) {
				for (Parameter p : goal.getParameters()) {
					if (parameter.get(p.getName()) == null) {
						if (p.getDefaultValue() != null
								&& p.getDefaultValue().length() > 0) {
							parameter.put(p.getName(), p.getDefaultValue());
						} else {
							if (p.isNecessary()) {
								// 不能忽略参数
								println("参数" + p.getName() + "不能为空。");
								return;
							}
						}
					}
				}
			}

			CommandGoalExecutor commandGoalExecutor = goal
					.getCommandGoalExecutor();
			for (String property : parameter.keySet()) {
				String v = parameter.get(property);
				try {
					if (v != null && v.length() > 0) {
						BeanUtils.setProperty(commandGoalExecutor, property,
								parameter.get(property));
					}
				} catch (Exception e1) {
					logger.errorMessage("属性设置错误！属性名:{0}，属性值:{1}。", e1,
							property, v);
				}
			}
			commandGoalExecutor.execute(this, context);
		} else {
			println("无效的命令:" + cmds.get(0));
		}
	}

	/**
	 * 分解命令行为一个一个单元
	 * 
	 * @param cmd
	 * @return
	 */
	private List<String> parseCmd(String cmd) {
		List<String> ret = new ArrayList<String>();
		Matcher m = commandPattern.matcher(cmd);
		if (m.find()) {
			ret.add(m.group());
		} else {
			return ret;
		}
		int s = m.end();
		m = parameterPattern.matcher(cmd);
		while (m.find(s)) {
			String str = m.group();
			if (str.startsWith("\"") || str.startsWith("'")) {
				ret.add(str.substring(1, str.length() - 1));
			} else {
				ret.add(str);
			}
			s = m.end();
		}
		return ret;
	}

	public void println(String str) {
		print(str);
		print("\n");
	}

	public void println() {
		print("\n");
	}

	public void print(String str) {
		try {
			if (str != null) {
				out.write(str.getBytes());
			}
		} catch (IOException e) {
			logger.errorMessage("内容({0})输入时出错：。", e, str);
		}
	}

	public int indentPrint(String str, int tab) {

		this.printTab(tab);
		int count = tab;

		for (int i = 0; i < str.length(); i++) {
			String s = str.substring(i, i + 1);
			if (str.charAt(i) >= 0 && str.charAt(i) <= ASCII_MAX) {
				if (count > MAX_LINE_LENGTH) {
					println("");
					this.printTab(tab);
					count = tab;
				}
				print(s);
				count += 1;
			} else {
				if (count > MAX_LINE_LENGTH) {
					println("");
					this.printTab(tab);
					count = tab;
				}
				print(s);
				count += 2;
			}
		}
		return count;
	}

	private void printTab(int tab) {
		for (int i = 0; i < tab; i++) {
			print(String.format(" ", ""));
		}
	}
}
