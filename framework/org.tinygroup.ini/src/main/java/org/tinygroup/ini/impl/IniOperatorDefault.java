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
package org.tinygroup.ini.impl;

import org.tinygroup.ini.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 默认实现
 * Created by luoguo on 14-3-29.
 */
public class IniOperatorDefault implements IniOperator {
    private Sections sections = null;
    private String commentChar = ";";
    private static final Pattern SECTION_PATTERN = Pattern.compile("([\\[])(.*)([\\]])");
    private static final Pattern VALUE_PAIR_PATTERN = Pattern.compile("(.*)=((([^\\\\;](\\\\;)?)*))");

    public IniOperatorDefault() {
    }

    public IniOperatorDefault(Sections sections) {
        this.sections=sections;
    }

    public void setSections(Sections sections) {
        this.sections = sections;

    }

    public Sections getSections() {
        return sections;
    }

    public void read(InputStream inputStream, String charset) throws IOException, IniException {
        sections = new Sections();
        InputStreamReader reader = new InputStreamReader(inputStream, charset);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String string = bufferedReader.readLine();
        String sectionName = null;
        while (string != null) {
            string = string.trim();
            if (string.length() == 0 || string.startsWith(commentChar)) {
                addComment(sectionName, string);
            } else if (string.startsWith("[")) { //如果是Section
                sectionName = addSection(string);
            } else {
                addValuePair(string, sectionName);
            }
            string = bufferedReader.readLine();
        }
        reader.close();
    }

    private void addValuePair(String string, String sectionName) throws IniException {
        Matcher matcher = VALUE_PAIR_PATTERN.matcher(string);
        if (matcher.find()) {
            String comment = string.substring(matcher.end()).trim();
            if (comment.length() > 0) {
                add(sectionName, new ValuePair(matcher.group(1).trim(), Utils.decode(matcher.group(2).trim()), comment.substring(1)));
            } else {
                add(sectionName, new ValuePair(matcher.group(1).trim(), Utils.decode(matcher.group(2).trim())));
            }
        } else {
            throw new IniException("不符全规范的内容：" + string);
        }
    }

    private String addSection(String string) throws IniException {
        Matcher matcher = SECTION_PATTERN.matcher(string);
        String sectionName = null;
        if (matcher.find()) {
            sectionName = Utils.decode(matcher.group(2).trim());
            String comment = string.substring(matcher.end()).trim();

            Section section;
            if (comment.startsWith(";")) {//如果有备注
                section = new Section(sectionName, comment.substring(1));
            } else if (comment.length() == 0) {//如果没有备注
                section = new Section(sectionName);
            } else {
                throw new IniException("不符全规范的内容：" + string);
            }
            sections.addSection(section);
        }
        return sectionName;
    }

    private void addComment(String sectionName, String string) {
        ValuePair valuePair = new ValuePair(string.substring(1));
        add(sectionName, valuePair);
    }


    public void write(OutputStream outputStream, String charset) throws IOException {
        if (sections != null) {
            for (Section section : sections.getSectionList()) {
                if (section.getName() != null) {
                    if (section.getComment() != null) {
                        outputStream.write(String.format("[%s];%s%n", Utils.encode(section.getName()), Utils.encode(section.getComment())).getBytes(charset));
                    } else {
                        outputStream.write(String.format("[%s]%n", Utils.encode(section.getName())).getBytes(charset));
                    }
                }
                for (ValuePair valuePair : section.getValuePairList()) {
                    if (valuePair.getKey() != null) {
                        if (valuePair.getComment() != null && valuePair.getComment().length() > 0) {
                            outputStream.write(String.format("%s=%s;%s%n", valuePair.getKey(), Utils.encode(valuePair.getValue()), valuePair.getComment()).getBytes(charset));
                        } else {
                            outputStream.write(String.format("%s=%s%n", valuePair.getKey(), Utils.encode(valuePair.getValue())).getBytes(charset));
                        }
                    } else {
                        outputStream.write(String.format(";%s%n", valuePair.getComment()).getBytes(charset));
                    }
                }
            }
        }
    }

    public Section getSection(String sectionName) {
        if (sections != null) {
            return sections.getSection(sectionName);
        }
        return null;
    }

    public void setCommentChar(char commentChar) {
        this.commentChar = commentChar + "";
    }

    public <T> void put(String sectionName, String key, T value) {
        Section section = checkSection(sectionName);
        section.set(key, value);
    }

    private Section checkSection(String sectionName) {
        if (sections == null) {
            sections = new Sections();
        }
        Section section = sections.getSection(sectionName);
        if (section == null) {
            section = new Section(sectionName);
            sections.addSection(section);
        }
        return section;
    }

    public <T> void add(String sectionName, String key, T value) {
        add(sectionName, new ValuePair(key, value.toString()));
    }

    public <T> T get(Class<T> tClass, String sectionName, String key, T defaultValue) {
        return null;
    }

    public String get(String sectionName, String key, String defaultValue) {
        String value = get(sectionName, key);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    public String get(String sectionName, String key) {
        Section section = sections.getSection(sectionName);
        if (section != null) {
            return section.getValue(key);
        }
        return null;
    }

    public <T> List<T> getList(Class<T> tClass, String sectionName, String key) {
        List<T> list = new ArrayList<T>();
        Section section = getSection(sectionName);
        if (section != null) {
            for (ValuePair valuePair : section.getValuePairList()) {
                if (valuePair.getKey().equals(key)) {
                    list.add(valuePair.getValue(tClass));
                }
            }
        }
        return list;
    }


    public <T> T get(Class<T> tClass, String sectionName, String key) {
        Section section = sections.getSection(sectionName);
        if (section != null) {
            T value = section.getValue(tClass, key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public void add(String sectionName, ValuePair valuePair) {
        Section section = checkSection(sectionName);
        section.add(valuePair);
    }

    public void set(String sectionName, ValuePair valuePair) {

    }

    public void add(String sectionName, List<ValuePair> valuePairList) {
        Section section = checkSection(sectionName);
        section.getValuePairList().addAll(valuePairList);
    }

    public List<ValuePair> getValuePairList(String sectionName, String key) {
        Section section = sections.getSection(sectionName);
        if (section != null) {
            return section.getValuePairList(key);
        }
        return null;
    }

    public ValuePair getValuePair(String sectionName, String key) {
        Section section = sections.getSection(sectionName);
        if (section != null) {
            return section.getValuePair(key);
        }
        return null;
    }
}
