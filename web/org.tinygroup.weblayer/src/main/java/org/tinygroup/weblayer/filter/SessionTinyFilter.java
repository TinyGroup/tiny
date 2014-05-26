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

import static org.tinygroup.commons.tools.ArrayUtil.isEmptyArray;
import static org.tinygroup.commons.tools.Assert.assertNotNull;
import static org.tinygroup.commons.tools.CollectionUtil.createArrayList;
import static org.tinygroup.commons.tools.CollectionUtil.createLinkedHashSet;
import static org.tinygroup.commons.tools.ObjectUtil.defaultIfNull;
import static org.tinygroup.commons.tools.StringUtil.defaultIfEmpty;
import static org.tinygroup.commons.tools.StringUtil.trimToNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.ToStringBuilder;
import org.tinygroup.commons.tools.ToStringBuilder.MapBuilder;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.util.ParserXmlNodeUtil;
import org.tinygroup.weblayer.webcontext.session.ExactMatchesOnlySessionStore;
import org.tinygroup.weblayer.webcontext.session.SessionConfig;
import org.tinygroup.weblayer.webcontext.session.SessionConfig.CookieConfig;
import org.tinygroup.weblayer.webcontext.session.SessionConfig.IdConfig;
import org.tinygroup.weblayer.webcontext.session.SessionConfig.StoreMappingsConfig;
import org.tinygroup.weblayer.webcontext.session.SessionConfig.StoresConfig;
import org.tinygroup.weblayer.webcontext.session.SessionConfig.UrlEncodeConfig;
import org.tinygroup.weblayer.webcontext.session.SessionIDGenerator;
import org.tinygroup.weblayer.webcontext.session.SessionInterceptor;
import org.tinygroup.weblayer.webcontext.session.SessionModelEncoder;
import org.tinygroup.weblayer.webcontext.session.SessionStore;
import org.tinygroup.weblayer.webcontext.session.exception.SessionFrameworkException;
import org.tinygroup.weblayer.webcontext.session.impl.SessionModelEncoderImpl;
import org.tinygroup.weblayer.webcontext.session.impl.SessionWebContextImpl;
import org.tinygroup.weblayer.webcontext.session.impl.UUIDGenerator;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 增强的Session框架，可将session中的对象保存到cookie、数据库或其它存储中。
 * 
 * @author renhui
 * 
 */
public class SessionTinyFilter extends AbstractTinyFilter {

	private final static Logger log = LoggerFactory
			.getLogger(SessionTinyFilter.class);

	private final ConfigImpl config = new ConfigImpl();
	private static final String SESSION_CONFIG = "session";

	private static XmlNode sessionNode;

	public SessionConfig getConfig() {
		return config;
	}

	/** 初始化factory。 */
	public void initTinyFilter() {
		super.initTinyFilter();
		ConfigurationManager appConfigManager = ConfigurationUtil.getConfigurationManager();
		sessionNode = appConfigManager.getApplicationConfig().getSubNode(
				SESSION_CONFIG);
		try {
			config.init();
		} catch (Exception e) {
			throw new SessionFrameworkException(e.getMessage(), e);
		}
		String storeName = config.getStoreMappings().getStoreNameForAttribute(
				config.getModelKey());

		if (storeName == null) {
			throw new IllegalArgumentException(
					"No storage configured for session model: key="
							+ config.getModelKey());
		}
	}

	
	public void preProcess(WebContext context) {

	}

	
	public void postProcess(WebContext context) {
		SessionWebContextImpl session = (SessionWebContextImpl) context;
		session.commit();
	}

	public WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
		return new SessionWebContextImpl(wrappedContext, config);
	}

	// 实现SessionConfig。
	@SuppressWarnings("unused")
	private static class ConfigImpl implements SessionConfig {
		private static final String LIFECYCLE_LOGGER = "lifecycle-logger";
		private static final String INTERCEPTOR = "interceptor";
		private static final String MAX_INACTIVE_INTERVAL = "maxInactiveInterval";
		private static final String FORCE_EXPIRATION_PERIOD = "forceExpirationPeriod";
		private static final String MODEL_KEY = "modelKey";
		private static final String KEEP_IN_TOUCH = "keepInTouch";
		private static final String SESSION_MODEL_ENCODER = "session-model-encoder";
		private final IdConfigImpl id = new IdConfigImpl();
		private final StoresConfigImpl stores = new StoresConfigImpl();
		private final StoreMappingsConfigImpl storeMappings = new StoreMappingsConfigImpl();
		private Integer maxInactiveInterval;
		private Long forceExpirationPeriod;
		private String modelKey;
		private Boolean keepInTouch;
		private SessionModelEncoder[] sessionModelEncoders;
		private SessionInterceptor[] sessionInterceptors;

		public int getMaxInactiveInterval() {
			return maxInactiveInterval;
		}

		public void setMaxInactiveInterval(int maxInactiveInterval) {
			this.maxInactiveInterval = maxInactiveInterval;
		}

		public long getForceExpirationPeriod() {
			return forceExpirationPeriod;
		}

		public void setForceExpirationPeriod(long forceExpirationPeriod) {
			this.forceExpirationPeriod = forceExpirationPeriod;
		}

		public String getModelKey() {
			return modelKey;
		}

		public void setModelKey(String modelKey) {
			this.modelKey = modelKey;
		}

		public boolean isKeepInTouch() {
			return keepInTouch;
		}

		public void setKeepInTouch(boolean keepInTouch) {
			this.keepInTouch = keepInTouch;
		}

		public IdConfig getId() {
			return id;
		}

		public StoresConfig getStores() {
			return stores;
		}

		public StoreMappingsConfig getStoreMappings() {
			return storeMappings;
		}

		public SessionModelEncoder[] getSessionModelEncoders() {
			return sessionModelEncoders;
		}

		public void setSessionModelEncoders(
				SessionModelEncoder[] sessionModelEncoders) {
			this.sessionModelEncoders = sessionModelEncoders;
		}

		public SessionInterceptor[] getSessionInterceptors() {
			return sessionInterceptors;
		}

		public void setSessionInterceptors(
				SessionInterceptor[] sessionInterceptors) {
			this.sessionInterceptors = sessionInterceptors;
		}

		private void init() throws Exception {
			if (maxInactiveInterval == null) {
				String interval = ParserXmlNodeUtil.getAttributeValue(
						MAX_INACTIVE_INTERVAL, sessionNode);
				if (interval == null) {
					maxInactiveInterval = MAX_INACTIVE_INTERVAL_DEFAULT;
				} else {
					maxInactiveInterval = Integer.parseInt(interval);
				}

			}
			if (forceExpirationPeriod == null) {
				String period = ParserXmlNodeUtil.getAttributeValue(
						MAX_INACTIVE_INTERVAL, sessionNode);
				if (period == null) {
					forceExpirationPeriod = FORCE_EXPIRATION_PERIOD_DEFAULT;
				} else {
					forceExpirationPeriod = Long.parseLong(period);
				}

			}
			if (modelKey == null) {
				modelKey = defaultIfEmpty(ParserXmlNodeUtil.getAttributeValue(
						MODEL_KEY, sessionNode), MODEL_KEY_DEFAULT);
			}
			if (keepInTouch == null) {
				String touch = ParserXmlNodeUtil.getAttributeValue(
						KEEP_IN_TOUCH, sessionNode);
				if (touch == null) {
					keepInTouch = KEEP_IN_TOUCH_DEFAULT;
				} else {
					keepInTouch = Boolean.parseBoolean(touch);
				}

			}

			id.init();
			stores.init(this);
			storeMappings.init(stores);

			// 对所有的ExactMatchesOnlySessionStore，设置attribute names。
			for (String storeName : stores.getStoreNames()) {
				SessionStore store = stores.getStore(storeName);

				if (store instanceof ExactMatchesOnlySessionStore) {
					String[] exactMatchedAttrNames = storeMappings
							.getExactMatchedAttributeNames(storeName);

					if (exactMatchedAttrNames == null) {
						throw new IllegalArgumentException(
								"Session store "
										+ storeName
										+ " only support exact matches to attribute names");
					}

					((ExactMatchesOnlySessionStore) store)
							.initAttributeNames(exactMatchedAttrNames);
				}
			}

			sessionModelEncoders = ParserXmlNodeUtil.parseConfigToArray(
					SESSION_MODEL_ENCODER, sessionNode,
					SessionModelEncoder.class);
			if (isEmptyArray(sessionModelEncoders)) {
				sessionModelEncoders = new SessionModelEncoder[] { new SessionModelEncoderImpl() };
			}
			sessionInterceptors = ParserXmlNodeUtil.parseConfigToArray(
					INTERCEPTOR, sessionNode, SessionInterceptor.class);
			if (isEmptyArray(sessionInterceptors)) {
				sessionInterceptors = new SessionInterceptor[0];
			}

			for (SessionInterceptor l : sessionInterceptors) {
				l.init(this);
			}
		}

		
		public String toString() {
			MapBuilder mb = new MapBuilder();

			mb.append("maxInactiveInterval", String.format(
					"%,d seconds (%,3.2f hours)", maxInactiveInterval,
					(double) maxInactiveInterval / 3600));
			mb.append("forceExpirationPeriod", String.format(
					"%,d seconds (%,3.2f hours)", forceExpirationPeriod,
					(double) forceExpirationPeriod / 3600));
			mb.append("modelKey", modelKey);
			mb.append("keepInTouch", keepInTouch);
			mb.append("idConfig", id);
			mb.append("stores", stores);
			mb.append("storeMappings", storeMappings);
			mb.append("sessionModelEncoders", sessionModelEncoders);

			return new ToStringBuilder().append("SessionConfig").append(mb)
					.toString();
		}
	}

	@SuppressWarnings("unused")
	private static class IdConfigImpl implements IdConfig {
		private static final String SESSIONID_GENERATOR = "sessionid-generator";
		private static final String COOKIE_ENABLED = "cookieEnabled";
		private static final String ID_CONFIG_NODE_NAME = "id";
		private static final String URL_ENCODE_ENABLED = "urlEncodeEnabled";
		private final CookieConfigImpl cookie = new CookieConfigImpl();
		private final UrlEncodeConfigImpl urlEncode = new UrlEncodeConfigImpl();
		private Boolean cookieEnabled;
		private Boolean urlEncodeEnabled;
		private SessionIDGenerator generator;

		public boolean isCookieEnabled() {
			return cookieEnabled;
		}

		public void setCookieEnabled(boolean cookieEnabled) {
			this.cookieEnabled = cookieEnabled;
		}

		public boolean isUrlEncodeEnabled() {
			return urlEncodeEnabled;
		}

		public void setUrlEncodeEnabled(boolean urlEncodeEnabled) {
			this.urlEncodeEnabled = urlEncodeEnabled;
		}

		public CookieConfig getCookie() {
			return cookie;
		}

		public UrlEncodeConfig getUrlEncode() {
			return urlEncode;
		}

		public SessionIDGenerator getGenerator() {
			return generator;
		}

		public void setGenerator(SessionIDGenerator generator) {
			this.generator = generator;
		}

		private void init() {
			if (cookieEnabled == null) {
				String enabled = ParserXmlNodeUtil
						.getAttributeValueWithSubNode(ID_CONFIG_NODE_NAME,
								COOKIE_ENABLED, sessionNode);
				if (enabled == null) {
					cookieEnabled = COOKIE_ENABLED_DEFAULT;
				} else {
					cookieEnabled = Boolean.parseBoolean(enabled);
				}

			}
			if (urlEncodeEnabled == null) {
				String enabled = ParserXmlNodeUtil
						.getAttributeValueWithSubNode(ID_CONFIG_NODE_NAME,
								URL_ENCODE_ENABLED, sessionNode);
				if (enabled == null) {
					urlEncodeEnabled = URL_ENCODE_ENABLED_DEFAULT;
				} else {
					urlEncodeEnabled = Boolean.parseBoolean(enabled);
				}
			}

			if (generator == null) {
				generator = defaultIfNull(
						ParserXmlNodeUtil.parseConfigToObject(SESSIONID_GENERATOR,
								sessionNode, SessionIDGenerator.class),
						new UUIDGenerator());
			}

			cookie.init();
			urlEncode.init();
		}

		
		public String toString() {
			MapBuilder mb = new MapBuilder();

			mb.append("cookieEnabled", cookieEnabled);
			mb.append("urlEncodeEnabled", urlEncodeEnabled);
			mb.append("cookieConfig", cookie);
			mb.append("urlEncodeConfig", urlEncode);
			mb.append("generator", generator);

			return new ToStringBuilder().append("IdConfig").append(mb)
					.toString();
		}
	}

	@SuppressWarnings("unused")
	private static class CookieConfigImpl implements CookieConfig {
		private static final String COOKIE_CONFIG_NAME = "cookie";
		private static final String NAME = "name";
		private static final String DOMAIN = "domain";
		private static final String PATH = "path";
		private static final String MAX_AGE = "maxAge";
		private static final String HTTP_ONLY = "httpOnly";
		private static final String SECURE = "secure";
		private String name;
		private String domain;
		private String path;
		private Integer maxAge;
		private Boolean httpOnly;
		private Boolean secure;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			// normalize domain
			domain = trimToNull(domain);

			if (domain != null && !domain.startsWith(".")) {
				domain = "." + domain;
			}

			this.domain = domain;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public int getMaxAge() {
			return maxAge;
		}

		public void setMaxAge(int maxAge) {
			this.maxAge = maxAge;
		}

		public boolean isHttpOnly() {
			return httpOnly;
		}

		public void setHttpOnly(boolean httpOnly) {
			this.httpOnly = httpOnly;
		}

		public boolean isSecure() {
			return secure;
		}

		public void setSecure(boolean secure) {
			this.secure = secure;
		}

		private void init() {
			if (name == null) {
				name = defaultIfEmpty(
						ParserXmlNodeUtil.getAttributeValueWithSubNode(
								COOKIE_CONFIG_NAME, NAME, sessionNode),
						COOKIE_NAME_DEFAULT);
			}
			if (domain == null) {
				domain = defaultIfEmpty(
						ParserXmlNodeUtil.getAttributeValueWithSubNode(
								COOKIE_CONFIG_NAME, DOMAIN, sessionNode),
						COOKIE_DOMAIN_DEFAULT);
			}
			if (path == null) {
				path = defaultIfEmpty(
						ParserXmlNodeUtil.getAttributeValueWithSubNode(
								COOKIE_CONFIG_NAME, PATH, sessionNode),
						COOKIE_PATH_DEFAULT);
			}
			if (maxAge == null) {
				String maxAgeString = ParserXmlNodeUtil
						.getAttributeValueWithSubNode(COOKIE_CONFIG_NAME,
								MAX_AGE, sessionNode);
				if (maxAgeString == null) {
					maxAge = COOKIE_MAX_AGE_DEFAULT;
				} else {
					maxAge = Integer.parseInt(maxAgeString);
				}

			}
			if (httpOnly == null) {
				String httpOnlyString = ParserXmlNodeUtil
						.getAttributeValueWithSubNode(COOKIE_CONFIG_NAME,
								HTTP_ONLY, sessionNode);
				if (httpOnlyString == null) {
					httpOnly = COOKIE_HTTP_ONLY_DEFAULT;
				} else {
					httpOnly = Boolean.parseBoolean(httpOnlyString);
				}

			}
			if (secure == null) {
				String secureString = ParserXmlNodeUtil
						.getAttributeValueWithSubNode(COOKIE_CONFIG_NAME,
								SECURE, sessionNode);
				if (secureString == null) {
					secure = COOKIE_SECURE_DEFAULT;
				} else {
					secure = Boolean.parseBoolean(secureString);
				}

			}
		}

		
		public String toString() {
			MapBuilder mb = new MapBuilder();

			mb.append("name", name);
			mb.append("domain", domain);
			mb.append("path", path);
			mb.append("maxAge", String.format("%,d seconds", maxAge));
			mb.append("httpOnly", httpOnly);
			mb.append("secure", secure);

			return new ToStringBuilder().append("CookieConfig").append(mb)
					.toString();
		}
	}

	@SuppressWarnings("unused")
	private static class UrlEncodeConfigImpl implements UrlEncodeConfig {
		private static final String URL_ENCODE_CONFIG_NAME = "url-encode";
		private static final String NAME = "name";
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private void init() {
			if (name == null) {
				name = defaultIfEmpty(
						ParserXmlNodeUtil.getAttributeValueWithSubNode(
								URL_ENCODE_CONFIG_NAME, NAME, sessionNode),
						URL_ENCODE_NAME_DEFAULT);
			}
		}

		
		public String toString() {
			MapBuilder mb = new MapBuilder();

			mb.append("name", name);

			return new ToStringBuilder().append("UrlEncodeConfig").append(mb)
					.toString();
		}
	}

	@SuppressWarnings("unused")
	private static class StoresConfigImpl implements StoresConfig {
		private static final String ATTRIBUTE_NAME = "bean-name";
		private static final String SESSION_STORE_CONFIG_NAME = "session-store";
		private Map<String, SessionStore> stores;

		public void setStores(LinkedHashMap<String, SessionStore> stores) {
			this.stores = stores;
		}

		public SessionStore getStore(String storeName) {
			return stores.get(storeName);
		}

		public String[] getStoreNames() {
			return stores.keySet().toArray(new String[stores.size()]);
		}

		private void init(SessionConfig sessionConfig) throws Exception {
			if (stores == null) {
				stores = defaultIfNull(ParserXmlNodeUtil.parseConfigToMap(
						SESSION_STORE_CONFIG_NAME, ATTRIBUTE_NAME, sessionNode,
						SessionStore.class),
						new LinkedHashMap<String, SessionStore>());
			}

			// 初始化所有stores
			for (Map.Entry<String, SessionStore> entry : stores.entrySet()) {
				entry.getValue().init(entry.getKey(), sessionConfig);
			}
		}

		
		public String toString() {
			return new ToStringBuilder().append("Stores").append(stores)
					.toString();
		}
	}

	@SuppressWarnings("unused")
	private static class StoreMappingsConfigImpl implements StoreMappingsConfig {
		private static final String MATCH_ALL = "*";
		private static final String MATCH_CONFIG_NAME = "match";
		private static final String NAME = "name";
		private static final String STORE = "store";
		private static final String PATTERN = "pattern";
		private static final String MATCH_REGEX_NODE_NAME = "matchRegex";
		private AttributePattern[] patterns;
		private String defaultStore;
		private Map<String, String> attributeMatchCache;

		public void setPatterns(AttributePattern[] patterns) {
			this.patterns = patterns;
		}

		private void init(StoresConfig stores) {
			attributeMatchCache = CollectionUtil.createConcurrentHashMap();
			if (patterns == null) {
				patterns = parserMatchConfig();
				patterns = defaultIfNull(patterns, new AttributePattern[0]);
			}

			for (AttributePattern pattern : patterns) {
				if (pattern.isDefaultPattern()) {
					if (defaultStore != null) {
						throw new IllegalArgumentException(
								"More than one stores mapped to *: "
										+ defaultStore + " and "
										+ pattern.getStoreName());
					}

					defaultStore = pattern.getStoreName();
				}

				if (stores.getStore(pattern.getStoreName()) == null) {
					throw new IllegalArgumentException(
							"Undefined Session Store: " + pattern);
				}
			}
		}

		private AttributePattern[] parserMatchConfig() {
			Assert.assertNotNull(sessionNode, "解析的节点对象不能为空");
			NameFilter<XmlNode> matchFilter = new NameFilter<XmlNode>(
					sessionNode);
			List<XmlNode> matchRegexNodes = matchFilter
					.findNodeList(MATCH_REGEX_NODE_NAME);
			List<AttributePattern> patterns=new ArrayList<AttributePattern>();
			if (!CollectionUtil.isEmpty(matchRegexNodes)) {
				for (int i = 0; i < matchRegexNodes.size(); i++) {
					String patternName = matchRegexNodes.get(i).getAttribute(PATTERN);
					String storeName = matchRegexNodes.get(i).getAttribute(STORE);
					patterns.add(AttributePattern.getRegexPattern(storeName,
							patternName));
				}
			}
			List<XmlNode> matchNodes=matchFilter.findNodeList(MATCH_CONFIG_NAME);
			if(!CollectionUtil.isEmpty(matchNodes)){
				for (XmlNode xmlNode : matchNodes) {
					String name=xmlNode.getAttribute(NAME);
					String storeName=xmlNode.getAttribute(STORE);
					if(MATCH_ALL.equals(name)){
						patterns.add(AttributePattern.getDefaultPattern(storeName));
					}else{
					    patterns.add(AttributePattern.getExactPattern(storeName, name));
					}
				}
			}
			return patterns.toArray(new AttributePattern[0]);
		}

		public String getStoreNameForAttribute(String attrName) {
			attrName = assertNotNull(trimToNull(attrName), "attrName must not null");
			String matchedStoreName = attributeMatchCache.get(attrName);

			if (matchedStoreName != null) {
				return matchedStoreName;
			} else {
				List<AttributeMatch> matches = createArrayList(patterns.length);

				for (AttributePattern pattern : patterns) {
					if (pattern.isDefaultPattern()) {
						matches.add(new AttributeMatch(pattern, 0));
					} else if (pattern.isRegexPattern()) {
						Matcher matcher = pattern.getPattern()
								.matcher(attrName);

						if (matcher.find()) {
							matches.add(new AttributeMatch(pattern, matcher
									.end() - matcher.start()));
						}
					} else {
						if (pattern.patternName.equals(attrName)) {
							matches.add(new AttributeMatch(pattern,
									pattern.patternName.length()));
						}
					}
				}

				// 最长匹配优先
				Collections.sort(matches);
				ToStringBuilder buf = new ToStringBuilder();

				if (matches.isEmpty()) {
					buf.append("does not match any pattern");
				} else {
					buf.append("matches the following CANDIDATED patterns:")
							.append(matches);
				}
				buf.format("Attribute \"%s\" ", attrName);

				log.logMessage(LogLevel.DEBUG, buf.toString());

				if (!matches.isEmpty()) {
					matchedStoreName = matches.get(0).pattern.getStoreName();
				}

				if (matchedStoreName != null) {
					attributeMatchCache.put(attrName, matchedStoreName);
				}
			}

			if (matchedStoreName != null) {
				log.logMessage(LogLevel.DEBUG,
						"Session attribute {} is handled by session store: {}",
						attrName, matchedStoreName);
			}

			return matchedStoreName;
		}

		public String[] getExactMatchedAttributeNames(String storeName) {
			storeName = assertNotNull(trimToNull(storeName), "no storeName");

			Set<String> attrNames = createLinkedHashSet();

			for (AttributePattern pattern : patterns) {
				if (pattern.getStoreName().equals(storeName)) {
					// 如果是非精确匹配，则返回null。
					if (pattern.isDefaultPattern() || pattern.isRegexPattern()) {
						return null;
					}

					attrNames.add(pattern.patternName);
				}
			}

			return attrNames.toArray(new String[attrNames.size()]);
		}

		
		public String toString() {
			return new ToStringBuilder().append("StoreMappings")
					.append(patterns).toString();
		}
	}

	/** 代表一个attribute的匹配。 */
	private static class AttributeMatch implements Comparable<AttributeMatch> {
		private final AttributePattern pattern;
		private final int matchLength;

		public AttributeMatch(AttributePattern pattern, int matchLength) {
			this.pattern = pattern;
			this.matchLength = matchLength;
		}

		/** 先比较匹配长度，较长的优先。其次比较匹配的类型，精确匹配比正则表达式匹配优先。 */
		public int compareTo(AttributeMatch o) {
			int result = o.matchLength - matchLength;

			if (result == 0) {
				int r1 = pattern.isRegexPattern() ? 0 : 1;
				int r2 = o.pattern.isRegexPattern() ? 0 : 1;

				return r2 - r1;
			}

			return result;
		}

		
		public String toString() {
			return new ToStringBuilder().append(pattern)
					.append(", matchLength=").append(matchLength).toString();
		}
	}

	/** 代表一个pattern的信息。 */
	public static class AttributePattern {
		public final String patternName;
		public final String storeName;
		public final Pattern pattern;

		/** 创建默认匹配，匹配所有attribute names。 */
		public static AttributePattern getDefaultPattern(String storeName) {
			return new AttributePattern(storeName, null, null);
		}

		/** 创建精确匹配，匹配名称完全相同的attribute names。 */
		public static AttributePattern getExactPattern(String storeName,
				String attrName) {
			return new AttributePattern(storeName, attrName, null);
		}

		/** 创建正则表达式匹配。 */
		public static AttributePattern getRegexPattern(String storeName,
				String regexName) {
			try {
				return new AttributePattern(storeName, regexName,
						Pattern.compile(regexName));
			} catch (Exception e) {
				throw new IllegalArgumentException(String.format(
						"Invalid regex pattern %s for store %s", regexName,
						storeName));
			}
		}

		private AttributePattern(String storeName, String patternName,
				Pattern pattern) {
			this.storeName = assertNotNull(trimToNull(storeName), "storeName");
			this.patternName = patternName;
			this.pattern = pattern;
		}

		public boolean isDefaultPattern() {
			return patternName == null;
		}

		public boolean isRegexPattern() {
			return pattern != null;
		}

		public String getPatternName() {
			return patternName;
		}

		public String getStoreName() {
			return storeName;
		}

		public Pattern getPattern() {
			return pattern;
		}

		
		public String toString() {
			ToStringBuilder buf = new ToStringBuilder();

			if (isDefaultPattern()) {
				buf.format("match=\"*\", store=\"%s\"", storeName);
			} else if (isRegexPattern()) {
				buf.format("match=~/%s/, store=\"%s\"", patternName, storeName);
			} else {
				buf.format("match=\"%s\", store=\"%s\"", patternName, storeName);
			}

			return buf.toString();
		}
	}

	
	public int getOrder() {
		return SESSION_FILTER_PRECEDENCE;
	}

}
