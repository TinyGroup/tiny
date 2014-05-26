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
package org.tinygroup.ini;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoguo on 14-3-28.
 */
public class Section {
    private String name;
    private String comment;
    private List<ValuePair> valuePairList;

    public Section() {

    }

    public Section(String name) {
        this.name = name;
    }

    public Section(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ValuePair> getValuePairList() {
        if (valuePairList == null) {
            valuePairList = new ArrayList<ValuePair>();
        }
        return valuePairList;
    }

    public void setValuePairList(List<ValuePair> valuePairList) {
        this.valuePairList = valuePairList;
    }

    public ValuePair getValuePair(String key) {
        if (valuePairList != null) {
            for (ValuePair valuePair : valuePairList) {
                if (valuePair.getKey().equals(key)) {
                    return valuePair;
                }
            }
        }
        return null;
    }

    public <T> T getValue(Class<T> tClass, String key) {
        if (valuePairList != null) {
            for (ValuePair valuePair : valuePairList) {
                if (valuePair.getKey().equals(key)) {
                    return (T) valuePair.getValue(tClass);
                }
            }
        }
        return null;
    }

    public String getValue(String key) {
        if (valuePairList != null) {
            for (ValuePair valuePair : valuePairList) {
                if (valuePair.getKey().equals(key)) {
                    return valuePair.getValue();
                }
            }
        }
        return null;
    }

    public List<ValuePair> getValuePairList(String key) {
        if (valuePairList != null) {
            List<ValuePair> list = new ArrayList<ValuePair>();
            for (ValuePair valuePair : valuePairList) {
                if (valuePair.getKey().equals(key)) {
                    list.add(valuePair);
                }
            }
            return list;
        }
        return null;
    }

    public void add(ValuePair valuePair) {
        if (valuePairList == null) {
            valuePairList = new ArrayList<ValuePair>();
        }
        valuePairList.add(valuePair);
    }

    public void add(String key, String value) {
        ValuePair valuePair = new ValuePair(key, value);
        add(valuePair);
    }

    public <T> void set(String key, T value) {
        ValuePair valuePair = getValuePair(key);
        if (valuePair == null) {
            valuePair = new ValuePair(key, value.toString());
            add(valuePair);
        } else {
            valuePair.setValue(value.toString());
        }
    }
}
