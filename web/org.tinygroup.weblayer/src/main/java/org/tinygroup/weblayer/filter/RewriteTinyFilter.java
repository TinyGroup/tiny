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
package org.tinygroup.weblayer.filter;

import java.util.List;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.util.ParserXmlNodeUtil;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteCondition;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteRule;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteSubstitution;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteSubstitution.Parameter;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteSubstitutionHandler;
import org.tinygroup.weblayer.webcontext.rewrite.impl.RewriteWebContextImpl;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 改写URL及参数，类似于Apache HTTPD Server中的rewrite模块。
 * 
 * @author renhui
 * 
 */
public class RewriteTinyFilter extends AbstractTinyFilter {

	private static final String REWRITE_CONFIG = "rewrite";
	private RewriteRule[] rules;

	public void setRules(RewriteRule[] rules) {
		this.rules = rules;
	}

	
	public void initTinyFilter() {
		super.initTinyFilter();
		initRules();
	}

	private void initRules() {

		ConfigurationManager appConfigManager = ConfigurationUtil.getConfigurationManager();
		XmlNode parserNode = appConfigManager.getApplicationConfig().getSubNode(
				REWRITE_CONFIG);
		parserExtraConfig(parserNode);

	}

	
	protected void parserExtraConfig(XmlNode parserNode) {

		if (rules == null) {
			Assert.assertNotNull(parserNode, "rewrite config must not null");
			NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(parserNode);
			List<XmlNode> ruleNodes = nameFilter.findNodeList("rule");
			if (!CollectionUtil.isEmpty(ruleNodes)) {
				rules = new RewriteRule[ruleNodes.size()];
				for (int i = 0; i < ruleNodes.size(); i++) {
					XmlNode ruleNode = ruleNodes.get(i);
					RewriteRule rule = new RewriteRule();
					rule.setPattern(ruleNode.getAttribute("pattern"));
					rule.setConditions(ruleConditions(ruleNode));
					rule.setSubstitution(ruleSubstitution(ruleNode));
					rule.setHandlers(ruleHandlers(ruleNode));
					rules[i] = rule;
					try {
						rule.afterPropertiesSet();
					} catch (Exception e) {
						logger.errorMessage("initializingBean error", e);
						throw new RuntimeException("initializingBean error", e);
					}
				}

			}

		}

	}

	private Object[] ruleHandlers(XmlNode ruleNode) {
		return ParserXmlNodeUtil.parseConfigToArray("rewrite-handler",
				ruleNode, RewriteSubstitutionHandler.class);
	}

	/**
	 * 
	 * 解析配置创建RewriteSubstitution对象
	 * 
	 * @param ruleNode
	 * @return
	 */
	private RewriteSubstitution ruleSubstitution(XmlNode ruleNode) {
		RewriteSubstitution substitution = ParserXmlNodeUtil
				.parseConfigToObject("substitution",null, ruleNode,
						RewriteSubstitution.class, new String[] { "uri",
								"flags" });
		Parameter[] parameters = ParserXmlNodeUtil.parseConfigToArray(
				"parameter", ruleNode, Parameter.class, new String[] { "key",
						"value" });
		substitution.setParameters(parameters);
		return substitution;
	}

	/**
	 * 
	 * 解析配置创建RewriteCondition对象
	 * 
	 * @param ruleNode
	 * @return
	 */
	private RewriteCondition[] ruleConditions(XmlNode ruleNode) {
		return ParserXmlNodeUtil.parseConfigToArray("condition", ruleNode,
				RewriteCondition.class, new String[] { "test", "flags",
						"pattern" });
	}

	
	public void preProcess(WebContext context) {
		RewriteWebContextImpl rewrite = (RewriteWebContextImpl) context;
		rewrite.prepare();
	}

	
	public void postProcess(WebContext context) {

	}

	public WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
		return new RewriteWebContextImpl(wrappedContext, rules);
	}

	
	public int getOrder() {
		return REWRITE_FILTER_PRECEDENCE;
	}
	

}
