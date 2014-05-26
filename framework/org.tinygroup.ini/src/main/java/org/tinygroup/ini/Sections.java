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
 * INI的Sections，可以包含多个段
 * Created by luoguo on 14-3-28.
 */
public class Sections {
    private List<Section> sectionList;

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public void addSection(Section section) {
        if (sectionList == null) {
            sectionList = new ArrayList<Section>();
        }
        sectionList.add(section);
    }

    public Section getSection(String sectionName) {
        if (sectionList != null) {
            for (Section section : sectionList) {
                if (section.getName() == null || sectionName == null) {
                    if (section.getName() == sectionName) {
                        return section;
                    }
                } else if (section.getName().equals(sectionName)) {
                    return section;
                }
            }
        }
        return null;
    }
}
