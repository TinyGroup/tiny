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
package org.tinygroup.httpvisit;

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.httpvisit.impl.HttpVisitorImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ResourceGetter {
    static HttpVisitor visitor = new HttpVisitorImpl();

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Accept", "text/html, application/xhtml+xml, */*");
        header.put("Accept-Language", "zh-CN,en-US;q=0.5");
        header.put("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
        header.put("Accept-Encoding", "gzip, deflate");
        header.put("Host", "localhost:9999");
        header.put("Connection", "Keep-Alive");
        visitor.setHeaderMap(header);

        String httpBase = "http://envirra.com/";
        String writeBase = "D:\\git\\TinySample\\org.tinygroup.tinysite\\src\\main\\webapp\\presso\\";
        writeUrls(httpBase, writeBase,"themes/presso/wp-content/plugins/contact-form-7/includes/css/styles.css,themes/presso/wp-content/plugins/wp-flexible-map/styles.css,themes/presso/wp-content/plugins/woocommerce/assets/css/woocommerce.css,themes/presso/wp-content/themes/presso/framework/flexslider/flexslider-custom.css,themes/presso/wp-content/themes/presso/framework/font-icons/social-icons/css/zocial.css,themes/presso/wp-content/themes/presso/framework/font-icons/entypo/css/entypo.css,themes/presso/wp-content/themes/presso/framework/font-icons/symbol/css/symbol.css,themes/presso/wp-content/themes/presso/framework/swipebox/swipebox.css,themes/presso/wp-content/themes/presso/framework/bootstrap/css/bootstrap.css,themes/presso/wp-content/themes/presso/css/theme.css");
        writeUrls(httpBase, writeBase,"themes/presso/wp-includes/js/jquery/jquery.js,themes/presso/wp-includes/js/jquery/jquery-migrate.min.js,themes/presso/wp-includes/js/comment-reply.min.js,themes/presso/wp-content/themes/presso/framework/instant-search/instant-search.js");
        writeUrls(httpBase, writeBase,"themes/presso/wp-content/plugins/presso-style-switcher/jquery.cookie.js,themes/presso/wp-content/plugins/contact-form-7/includes/js/jquery.form.min.js,themes/presso/wp-content/plugins/contact-form-7/includes/js/scripts.js,themes/presso/wp-content/plugins/woocommerce/assets/js/frontend/add-to-cart.min.js,themes/presso/wp-content/plugins/woocommerce/assets/js/jquery-blockui/jquery.blockUI.min.js,themes/presso/wp-content/plugins/woocommerce/assets/js/jquery-placeholder/jquery.placeholder.min.js,themes/presso/wp-content/plugins/woocommerce/assets/js/frontend/woocommerce.min.js,themes/presso/wp-content/plugins/woocommerce/assets/js/jquery-cookie/jquery.cookie.min.js,themes/presso/wp-content/plugins/woocommerce/assets/js/frontend/cart-fragments.min.js,themes/presso/wp-includes/js/jquery/ui/jquery.ui.effect.min.js,themes/presso/wp-includes/js/jquery/ui/jquery.ui.effect-fade.min.js,themes/presso/wp-includes/js/jquery/ui/jquery.ui.core.min.js,themes/presso/wp-includes/js/jquery/ui/jquery.ui.widget.min.js,themes/presso/wp-includes/js/jquery/ui/jquery.ui.accordion.min.js,themes/presso/wp-includes/js/jquery/ui/jquery.ui.tabs.min.js,themes/presso/wp-content/plugins/presso-shortcodes/js/presso-shortcodes.js,themes/presso/wp-content/themes/presso/js/jquery.fitvids.js,themes/presso/wp-content/themes/presso/js/jquery.isotope.min.js,themes/presso/wp-content/themes/presso/framework/flexslider/jquery.flexslider.js,themes/presso/wp-content/themes/presso/framework/swipebox/jquery.swipebox.min.js");
        writeUrls(httpBase, writeBase,"themes/presso/wp-content/themes/presso/js/asset.js,themes/presso/wp-content/themes/presso/js/main.js");
    }

    private static void writeUrls(String httpBase, String writeBase,String urlArray) throws Exception {
        String urls[] = urlArray.split(",");
        for (String url : urls) {
            writeFile(url, httpBase, writeBase);
        }
    }

    private static void writeFile(String url, String httpBase, String writeBase) throws Exception {
        String fileName = writeBase + url;
        File file = new File(fileName);
        File parent = new File(file.getParent());
        if (!parent.exists()) {
            parent.mkdirs();
        }
        IOUtils.writeToOutputStream(new FileOutputStream(fileName), visitor.getUrl(httpBase + url, null), "utf-8");
        System.out.println("write to:"+fileName);
    }

}
