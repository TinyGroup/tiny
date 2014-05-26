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
package org.tinygroup.dict.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.cache.Cache;
import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.context.Context;
import org.tinygroup.dict.Dict;
import org.tinygroup.dict.DictLoader;
import org.tinygroup.dict.DictManager;
import org.tinygroup.support.BeanSupport;

public class DictManagerImpl extends BeanSupport implements DictManager {

	private Map<String, List<DictLoader>> dictLoaderMap = new HashMap<String, List<DictLoader>>();
	private Cache cache;

	public void load(DictLoader dictLoader) {
		dictLoader.load(this);
	}

	public void load() {
		clear();//先清除再去加载
		for (List<DictLoader> dictLoaderList : dictLoaderMap.values()) {
			for (DictLoader tempDictLoader : dictLoaderList) {
				tempDictLoader.load(this);
			}
		}

	}

	public Dict getDict(String dictTypeName, Context context) {
		String lang = LocaleUtil.getContext().getLocale().toString();
		for (DictLoader dictLoader : dictLoaderMap.get(lang)) {
			Dict dict = dictLoader.getDict(dictTypeName, this, context);
			if (dict != null) {
				return dict;
			}
		}
		throw new RuntimeException("没有找到<" + dictTypeName + ">的字典类型");
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public Cache getCache() {
		return cache;
	}

	public void clear() {
		for (List<DictLoader> dictLoaderList : dictLoaderMap.values()) {
			for (DictLoader tempDictLoader : dictLoaderList) {
				tempDictLoader.clear(this);
			}
		}

	}

	public void clear(DictLoader dictLoader) {
		dictLoader.clear(this);
	}

	public void addDictLoader(DictLoader dictLoader) {
		String lang = dictLoader.getLanguage();
		if (lang == null || lang.length() == 0) {
			lang = LocaleUtil.getContext().getLocale().toString();
		}
		List<DictLoader> dictLoaderList = dictLoaderMap.get(lang);
		if (dictLoaderList == null) {
			dictLoaderList = new ArrayList<DictLoader>();
			dictLoaderMap.put(lang, dictLoaderList);
		}
		dictLoaderList.add(dictLoader);
	}
	

}
