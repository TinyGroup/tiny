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
package org.tinygroup.convert.objecttxt.fixwidth;

import org.apache.commons.beanutils.BeanUtils;
import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;
import org.tinygroup.convert.text.config.Text;
import org.tinygroup.convert.text.config.TextCell;
import org.tinygroup.convert.text.config.TextRow;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectToText<T> implements Converter<List<T>, String> {
    private static Logger logger = LoggerFactory.getLogger(ObjectToText.class);
    private Map<String, String> propertyNames;
    private List<String> properties;
    private String fieldSplit = " ";
    private String lineSplit;

    public ObjectToText(Map<String, String> propertyNames,
                        List<String> properties, String lineSplit) {
        this.propertyNames = propertyNames;
        this.properties = properties;
        this.lineSplit = lineSplit;
    }

    public String convert(List<T> inputDatas) throws ConvertException {
        List<TextRow> rows = initBuffers(inputDatas);
        writeLineRows(inputDatas, rows);
        return buffersToText(rows);
    }

    private List<TextRow> initBuffers(List<T> inputDatas) {
        List<TextRow> rows = new ArrayList<TextRow>();
        for (int i = 0; i < inputDatas.size() + 1; i++) {
            rows.add(new TextRow());
        }
        return rows;

    }

    private void writeLineRows(List<T> inputDatas, List<TextRow> rows) throws ConvertException {
        for (int i = 0; i < properties.size(); i++) {
            String property = properties.get(i);
            int propertyMaxLength = computeColumnLength(inputDatas, property,
                    rows);
            adjustDataLength(propertyMaxLength, rows, i);
        }
    }

    private int computeColumnLength(List<T> inputDatas, String property,
                                    List<TextRow> rows) throws ConvertException {
        String title = property;
        if (propertyNames.containsKey(property)) {
            String propertyName = propertyNames.get(property);
            if (propertyName != null && !"".equals(propertyName.trim())) {
                title = propertyName;
            }
        }
        TextCell cell = new TextCell(title);
        // 将列头加入数据
        rows.get(0).addCell(cell);
// 设置列头数据长度为最大长度
        int maxLength = getLength(title);

        for (int i = 0; i < inputDatas.size(); i++) {
            int length = 0;
            try {
                String value = getAttributeValue(inputDatas.get(i), property);
                length = getLength(value);
                if (length > maxLength) {
                    maxLength = length;
                }
                TextCell dataCell = new TextCell(value);
                // 将第n列的数据加入列表
                rows.get(i + 1).addCell(dataCell);
            } catch (Exception e) {
                logger.errorMessage("读取第{0}列数据的属性{1}时出错", e, i, property);
            }
        }
        return maxLength;
    }

    private void adjustDataLength(int propertyMaxLength,
                                  List<TextRow> rows, int index) {
        for (int i = 0; i < rows.size(); i++) {
            TextRow row = rows.get(i);
            TextCell cell = row.getCells().get(index);
            cell.setLength(propertyMaxLength);
        }
    }

    private String buffersToText(List<TextRow> rows) {
        Text text = new Text();
        text.setRows(rows);
        return text.toString(fieldSplit, lineSplit, true);
    }

    private int getLength(String string) throws ConvertException {
        try {
            return string.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            throw new ConvertException(e);
        }
    }

    private String getAttributeValue(T object, String property) throws ConvertException {
        try {
            Object value = BeanUtils.getProperty(object, property);
            if (value == null) {
                return "";
            }
            return value.toString();
        } catch (Exception e) {
            throw new ConvertException(e);
        }
    }
}
