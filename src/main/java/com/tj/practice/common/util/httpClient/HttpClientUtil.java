package com.tj.practice.common.util.httpClient;

import com.alibaba.fastjson.JSONObject;
import com.tj.practice.common.util.ThreadUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.*;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.CodingErrorAction;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基于 httpclient 4.3.1版本的 http工具类
 */

public class HttpClientUtil {
    private static int connectTimeout = 10000, soTimeout = 60000;
    private static PoolingHttpClientConnectionManager connManager = null;
    private static CloseableHttpClient httpclient = null;

    static {
        try {
            //SSLContext sslContext = SSLContexts.custom().useTLS().build();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, null);

            // 域名验证 这种方式不对主机名进行验证，验证功能被关闭
            //SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", sslsf).build();

            connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            // Create socket configuration
            SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            connManager.setDefaultSocketConfig(socketConfig);
            // Create message constraints
            MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000).build();

            // Create connection configuration
            ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
                    .setMessageConstraints(messageConstraints).build();
            connManager.setDefaultConnectionConfig(connectionConfig);
            connManager.setMaxTotal(2000);
            connManager.setDefaultMaxPerRoute(200);
            new Thread() {
                public void run() {
                    while (!Thread.interrupted()) {
                        connManager.closeIdleConnections(3, TimeUnit.MINUTES);
                        connManager.closeExpiredConnections();
                        ThreadUtil.sleep(3, TimeUnit.MINUTES);
                    }
                }
            }.start();

            httpclient = HttpClients.custom().setConnectionManager(connManager).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String doGet(String url, Map<String, String> params, String charset) throws Exception {
        return doGet(url, params, charset, connectTimeout, soTimeout);
    }

    public static String doPost(String url, Map<String, String> params, String charset) throws Exception {
        return doPost(url, params, charset, connectTimeout, soTimeout);
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     * @throws Exception
     */

    public static String doGet(String url, Map<String, String> params, String charset, int connectTimeout, int soTimeout) throws Exception {
        return doGet(url, params, null, charset, connectTimeout, soTimeout);
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     * @throws Exception
     */
    public static String doPost(String url, Map<String, String> params, String charset, int connectTimeout, int soTimeout) throws Exception {
        return doPost(url, params, null, charset, connectTimeout, soTimeout);
    }

    /**
     * 发送post数据流
     *
     * @param url            地址
     * @param b              字节流，从字符串转byte时需要 指定编码转换
     * @param connectTimeout 链接超时时间(毫秒)
     * @param soTimeout      读取超时时间
     *                       (毫秒)
     * @return 接收到的byte 需要指定编码转换
     * @throws Exception
     */
    public static byte[] doPostStream(String url, byte[] b, Map<String, String> headmap, int connectTimeout, int soTimeout) throws Exception {
        // 创建待处理的文件
        // 创建待处理的表单域内容文本
        ByteArrayEntity bae = new ByteArrayEntity(b);
        return doPostInputStream(url, bae, headmap, connectTimeout, soTimeout);
    }

    /**
     * 发送post数据流
     *
     * @param url
     * @param stream
     * @param headmap
     * @param connectTimeout
     * @param soTimeout
     * @return 接收到的byte 需要指定编码转换
     * @throws Exception
     */
    public static byte[] doPostInputStream(String url, InputStream stream, Map<String, String> headmap, int connectTimeout, int soTimeout) throws Exception {
        InputStreamEntity bae = new InputStreamEntity(stream);
        return doPostInputStream(url, bae, headmap, connectTimeout, soTimeout);
    }

    public static byte[] doPostInputStream(String url, HttpEntity entity, Map<String, String> headmap, int connectTimeout, int soTimeout) throws Exception {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(soTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout).build();
        // 请求处理页面
        HttpPost httppost = new HttpPost(url);
        httppost.setConfig(requestConfig);

        if (headmap != null) {
            for (String name : headmap.keySet()) {
                String value = headmap.get(name);
                httppost.setHeader(name, value);
            }
            String CONN_CLOSE = headmap.get(HTTP.CONN_DIRECTIVE);
            //短连接控制  只有head中传入了才做控制    headmap.put(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
            if (HTTP.CONN_CLOSE.equals(CONN_CLOSE)) {
                httppost.setProtocolVersion(HttpVersion.HTTP_1_0);
            }
        }

        // 设置请求
        httppost.setEntity(entity);
        // 执行
        CloseableHttpResponse response = httpclient.execute(httppost);
        HttpEntity entityOut = response.getEntity();
        byte[] result = null;
        if (entityOut != null) {
            result = EntityUtils.toByteArray(entityOut);
        }
        EntityUtils.consume(entityOut);
        response.close();

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httppost.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode + "," + new String(result, "UTF-8"));
        }
        return result;
    }

    /**
     * 发送get请求
     *
     * @param url            请求路径
     * @param params         请求参数
     * @param headmap        头部参数
     * @param charset        发送的字符编发
     * @param connectTimeout 链接超时时间(毫秒)
     * @param soTimeout      读取超时时间(毫秒)
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, String> params, Map<String, String> headmap, String charset, int connectTimeout, int soTimeout) throws Exception {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(soTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout).build();
        List<NameValuePair> pairs = null;
        if (params != null && !params.isEmpty()) {
            pairs = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
        }
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if (headmap != null) {
            for (String name : headmap.keySet()) {
                String value = headmap.get(name);
                httpPost.setHeader(name, value);
            }
            String CONN_CLOSE = headmap.get(HTTP.CONN_DIRECTIVE);
            //短连接控制  只有head中传入了才做控制    headmap.put(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
            if (HTTP.CONN_CLOSE.equals(CONN_CLOSE)) {
                httpPost.setProtocolVersion(HttpVersion.HTTP_1_0);
            }
        }

        if (pairs != null && pairs.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
        }
        CloseableHttpResponse response = httpclient.execute(httpPost);

        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, charset);
        }
        EntityUtils.consume(entity);
        response.close();
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpPost.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode + "," + result);
        }
        return result;
    }

    public static String doPost(String url, JSONObject params, Map<String, String> headmap, String charset, int connectTimeout, int soTimeout) throws Exception {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        StringEntity entity = new StringEntity(params.toJSONString(),"utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(soTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        httpPost.setEntity(entity);
        if (headmap != null) {
            for (String name : headmap.keySet()) {
                String value = headmap.get(name);
                httpPost.setHeader(name, value);
            }
            String CONN_CLOSE = headmap.get(HTTP.CONN_DIRECTIVE);
            //短连接控制  只有head中传入了才做控制    headmap.put(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
            if (HTTP.CONN_CLOSE.equals(CONN_CLOSE)) {
                httpPost.setProtocolVersion(HttpVersion.HTTP_1_0);
            }
        }
        CloseableHttpResponse response = httpclient.execute(httpPost);

        HttpEntity data = response.getEntity();
        String result = null;
        if (data != null) {
            result = EntityUtils.toString(data, charset);
        }
        EntityUtils.consume(data);
        response.close();
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpPost.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode + "," + result);
        }
        return result;
    }

    /**
     * 发送get请求
     *
     * @param url            请求路径
     * @param params         请求参数
     * @param headmap        头部参数
     * @param charset        发送的字符编发
     * @param connectTimeout 链接超时时间(毫秒)
     * @param soTimeout      读取超时时间(毫秒)
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, String> params, Map<String, String> headmap, String charset, int connectTimeout, int soTimeout) throws Exception {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        HttpGet httpGet = new HttpGet(url);
        if (headmap != null) {
            for (String name : headmap.keySet()) {
                String value = headmap.get(name);
                httpGet.setHeader(name, value);
            }
            String CONN_CLOSE = headmap.get(HTTP.CONN_DIRECTIVE);
            //短连接控制  只有head中传入了才做控制    headmap.put(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
            if (HTTP.CONN_CLOSE.equals(CONN_CLOSE)) {
                httpGet.setProtocolVersion(HttpVersion.HTTP_1_0);
            }
        }
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(soTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout).build();
        if (params != null && !params.isEmpty()) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
        }
        httpGet.setConfig(requestConfig);
        httpGet.setURI(URI.create(url));
        CloseableHttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, charset);
        }
        EntityUtils.consume(entity);
        response.close();
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpGet.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode + "," + result);
        }
        return result;
    }

    /**
     * form表单参数
     * @param url 请求地址
     * @param param 请求参数
     * @return
     * @throws Exception
     */
    public static String execute(String url, Map<String,String> param) throws Exception{
        String result = null;
        if (null == param){
            result = Jsoup.connect(url).ignoreContentType(true).timeout(10000).post().text();
        }else {
            Map<String,String> _param = new HashMap<String, String>();
            for (Map.Entry<String, String> entry : param.entrySet()) {
                if (null != entry.getValue()){
                    _param.put(entry.getKey(), entry.getValue());
                }
            }
            result = Jsoup.connect(url).ignoreContentType(true).data(_param).timeout(10000).post().text();
        }
        return result;
    }
}
