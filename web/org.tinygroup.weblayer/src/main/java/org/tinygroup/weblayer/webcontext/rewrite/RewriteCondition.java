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
package org.tinygroup.weblayer.webcontext.rewrite;

import static org.tinygroup.commons.tools.Assert.assertNotNull;
import static org.tinygroup.commons.tools.BasicConstant.EMPTY_STRING_ARRAY;
import static org.tinygroup.commons.tools.StringUtil.trimToNull;
import static org.tinygroup.weblayer.webcontext.rewrite.RewriteUtil.getSubstitutedTestString;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.tinygroup.commons.tools.MatchResultSubstitution;
import org.tinygroup.commons.tools.StringEscapeUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.commons.tools.ToStringBuilder;
import org.tinygroup.commons.tools.ToStringBuilder.MapBuilder;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class RewriteCondition implements InitializingBean {
	private static final String SPLIT_CHAR = ",";
	private static final Logger logger = LoggerFactory
			.getLogger(RewriteCondition.class);
	private String testString;
	private String patternString;
	private Pattern pattern;
	private boolean negative;
	private String flags;
	private ConditionFlags conditionFlags;

	public void setTest(String testString) {
		this.testString = trimToNull(testString);
	}

	public void setPattern(String patternString) throws PatternSyntaxException {
		this.patternString = trimToNull(patternString);
	}

	public void setFlags(String flags) {
		this.flags = flags;
		String[] flagArray = null;
		if (StringUtil.isEmpty(flags)) {
			flagArray = EMPTY_STRING_ARRAY;
		} else {
			flagArray = flags.split(SPLIT_CHAR);
		}
		this.conditionFlags = new ConditionFlags(flagArray);
	}

	public String getFlags() {
		return flags;
	}

	public ConditionFlags getConditionFlags() {
		return conditionFlags;
	}

	public void afterPropertiesSet() throws Exception {
		// test
		assertNotNull(testString, "missing test attribute for condition");

		// pattern
		if (patternString == null || "!".equals(patternString)) {
			throw new PatternSyntaxException("empty pattern", patternString, -1);
		}

		String realPattern;

		if (patternString.startsWith("!")) {
			this.negative = true;
			realPattern = patternString.substring(1);
		} else {
			realPattern = patternString;
		}

		this.pattern = Pattern.compile(realPattern);

		// flags
		if (conditionFlags == null) {
			conditionFlags = new ConditionFlags();
		}
	}

	public MatchResult match(MatchResult ruleMatchResult,
			MatchResult conditionMatchResult, HttpServletRequest request) {
		logger.logMessage(LogLevel.TRACE,
				"Testing condition: testString=\"{}\", pattern=\"{}\"",
				StringEscapeUtil.escapeJava(testString),
				StringEscapeUtil.escapeJava(patternString));

		String subsTestString = getSubstitutedTestString(testString,
				ruleMatchResult, conditionMatchResult, request);

		logger.logMessage(LogLevel.TRACE,
				"Expanded testString: original=\"{}\", expanded=\"{}\"",
				StringEscapeUtil.escapeJava(testString),
				StringEscapeUtil.escapeJava(subsTestString));

		Matcher matcher = pattern.matcher(subsTestString);
		boolean matched = matcher.find();

		if (!negative && matched) {
			logger.logMessage(LogLevel.DEBUG,
					"Testing \"{}\" with condition pattern: \"{}\", MATCHED",
					StringEscapeUtil.escapeJava(subsTestString),
					StringEscapeUtil.escapeJava(patternString));

			return matcher.toMatchResult();
		}

		if (negative && !matched) {
			logger.logMessage(LogLevel.DEBUG,
					"Testing \"{}\" with condition pattern: \"{}\", MATCHED",
					StringEscapeUtil.escapeJava(subsTestString),
					StringEscapeUtil.escapeJava(patternString));

			return MatchResultSubstitution.EMPTY_MATCH_RESULT;
		}

		logger.logMessage(LogLevel.TRACE,
				"Testing \"{}\" with condition pattern: \"{}\", MISMATCHED",
				StringEscapeUtil.escapeJava(subsTestString),
				StringEscapeUtil.escapeJava(patternString));

		return null;
	}

	
	public String toString() {
		MapBuilder mb = new MapBuilder();

		mb.append("test", testString);
		mb.append("pattern", patternString);

		if (!conditionFlags.isEmpty()) {
			mb.append("flags", conditionFlags);
		}

		return new ToStringBuilder().append("Condition").append(mb).toString();
	}

	/** 代表condition的标志位。 */
	public static class ConditionFlags extends Flags {
		public ConditionFlags() {
			super();
		}

		public ConditionFlags(String... flags) {
			super(flags);
		}

		/** 标志位：和下一个condition呈“或”的关系。 */
		public boolean hasOR() {
			return hasFlags("OR", "ornext");
		}
	}
}
