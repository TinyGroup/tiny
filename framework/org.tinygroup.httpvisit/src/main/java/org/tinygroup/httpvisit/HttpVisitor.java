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

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;

import java.util.List;
import java.util.Map;

/**
 * HTTP访问接口，访问不同的网站，请构建不同的实例
 *
 * @author luoguo
 */
public interface HttpVisitor {
    /**
     * 初始化，在访问url之前，必须调用init方法
     */
    void init();

    /**
     * 设置代理
     *
     * @param proxyHost 代理服务器主机域名或IP
     * @param proxyPort 代理端口
     * @param userName  用户名
     * @param passwrod  密码
     */
    void setProxy(String proxyHost, int proxyPort, String userName, String passwrod);

    /**
     * 设置基本安全校验
     *
     * @param host     服务器主机域名或IP
     * @param port     端口
     * @param realm    领域，可以为空，表示任何realm，由被访问端提供
     * @param schema   校验模式，可以为空，表示任何schema，可选值有：Basic,Digest,NTLM，当然也可以自行扩展
     * @param username 用户名
     * @param password 密码
     */
    void setBasicAuth(String host, int port, String realm, String schema, String username, String password);

    /**
     * 设置基本安全校验,realm和schema全部适应全部<br>
     * 比设定了realm和schema的性能差一些
     *
     * @param host     服务器主机域名或IP
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     */
    void setBasicAuth(String host, int port, String username, String password);

    /**
     * 设置自动检测安全校验<br>
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @param schemaList 支持的Schame列表
     */
    void setAlternateAuth(String host, int port, String username, String password, List<String> schemaList);

    /**
     * 获取Http状态
     *
     * @return Http状态
     */
    HttpState getHttpState();

    /**
     * 设置响应编码，如果不设置，则默认是UTF-8
     *
     * @param charset
     */
    void setResponseCharset(String charset);

    /**
     * 需要在请求之前设置<br>
     * 设置请求编码，如果不设置，则默认是ISO-8859-1<br>
     * <b>注意：修改此参数要慎重，确认服务器的解码格式是修改的编码，否则会导致乱码出现</b>
     *
     * @param requestCharset
     */
    void setRequestCharset(String requestCharset);

    /**
     * 设置超时时间，必须在init之前调用
     *
     * @param timeout
     */

    void setTimeout(int timeout);


    /**
     * 添加Cookie
     *
     * @param cookie
     */
    void addCookie(Cookie cookie);

    /**
     * 添加一组Cookie
     *
     * @param cookies
     */
    void addCookie(Cookie[] cookies);

    /**
     * 返回Cookie，在请求完成之后获取，得到是的从浏览器从传入的
     *
     * @return 返回所有Cookie
     */
    Cookie[] getCookies();

    /**
     * 用get方式访问URL
     *
     * @param url       要访问的URL
     * @param parameter 要访问的参数
     * @return 请求结果
     */
    String getUrl(String url, Map<String, ?> parameter);

    /**
     * 用post方式访问URL
     *
     * @param url       要访问的URL
     * @param parameter 要访问的参数
     * @return 请求结果
     */
    String postUrl(String url, Map<String, ?> parameter);

    /**
     * 获取HttpClient对象
     *
     * @return 返回HttpClient对象
     */
    HttpClient getHttpClient();

    /**
     * Post SOAP内容
     *
     * @param url
     * @param soapAction
     * @param xmlEntiry
     * @return 请求结果
     */
    String postSoap(String url, String soapAction, String xmlEntiry);

    /**
     * Post SOAP内容
     *
     * @param url
     * @param xmlEntiry
     * @return 请求结果
     */
    String postSoap(String url, String xmlEntiry);

    /**
     * Post Xml内容
     *
     * @param url
     * @param xmlEntiry
     * @return 请求结果
     */
    String postXml(String url, String xmlEntiry);

    /**
     * 设置Header
     *
     * @param header
     */
    void setHeaderMap(Map<String, String> header);

    /**
     * 设置Header值
     *
     * @param key
     * @param value
     */
    HttpVisitor setHeader(String key, String value);
}
