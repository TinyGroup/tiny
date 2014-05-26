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
package org.tinygroup.convert.xsdjava;

import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;

import java.util.List;

public class SchemaToClass implements Converter<List<String>, Void> {
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVER = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    private String baseFolder;
	private String packageName;
	private String xjbFolder;

	public SchemaToClass(String baseFolder, String xjbFolder, String packageName) {
		this.baseFolder = baseFolder;
		this.packageName = packageName;
		this.xjbFolder = xjbFolder;
	}

	public Void convert(List<String> xsdFileNames) throws ConvertException {
		String[] args = new String[NINE];
		args[ZERO] = "-d";
		args[ONE] = baseFolder;
		args[TWO] = "-p";
		args[THREE] = packageName;
		args[FOUR] = "-verbose";
		args[FIVE] = "-b";
		args[SIX] = xjbFolder;
		args[SEVER] = "-extension";
		for (int i = 0; i < xsdFileNames.size(); i++) {
			args[EIGHT] = xsdFileNames.get(i);
			try {
				XJCFacade.execute(args);
			} catch (Exception e) {
				throw new ConvertException(e);
			}
		}
		return null;
	}

}
