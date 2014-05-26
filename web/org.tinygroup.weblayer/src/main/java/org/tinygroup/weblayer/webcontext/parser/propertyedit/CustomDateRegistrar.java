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
package org.tinygroup.weblayer.webcontext.parser.propertyedit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.tinygroup.commons.i18n.LocaleUtil;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;

public class CustomDateRegistrar implements PropertyEditorRegistrar {
    private String   format;
    private Locale   locale;
    private TimeZone timeZone;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setLocale(String locale) {
        this.locale = LocaleUtil.parseLocale(locale);
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = TimeZone.getTimeZone(timeZone);
    }
    

    public void setLocaleObject(Locale locale) {
		this.locale = locale;
	}

	public void setTimeZoneObject(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public void registerCustomEditors(PropertyEditorRegistry registry) {
        if (format == null) {
            format = "yyyy-MM-dd";
        }

        if (locale == null) {
            locale = LocaleUtil.getContext().getLocale();
        }

        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        sdf.setTimeZone(timeZone);

        registry.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }
}
