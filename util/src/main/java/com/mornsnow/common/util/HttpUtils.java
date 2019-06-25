package com.mornsnow.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class HttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static SSLConnectionSocketFactory sslsf = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static SSLContextBuilder builder = null;

    static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);//max connection
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    /**
     * 执行GET请求
     *
     * @param url
     * @param headerMap 头信息
     * @param paramMap  参数列表
     * @return
     */
    public static String doGet(String url, Map<String, String> headerMap, Map<String, Object> paramMap) {

        LOG.warn("doGet,url:{}, header:{}, params:{}", url,
                MapUtils.isEmpty(headerMap) ? "no headers" : JSON.toJSONString(headerMap),
                MapUtils.isEmpty(paramMap) ? "[empty]" : JSON.toJSONString(paramMap));

        String paramStr = "";

        if (MapUtils.isNotEmpty(paramMap)) {
            paramStr = buildParamStr(paramMap);
        }
        HttpGet httpGet = new HttpGet(url + paramStr);

        return execute(httpGet, headerMap);
    }

    /**
     * 执行GET请求（无头信息）
     *
     * @param url
     * @param paramMap
     * @return
     */
    public static String doGet(String url, Map<String, Object> paramMap) {
        return doGet(url, null, paramMap);
    }

    /**
     * 执行GET请求（无头信息，无参数）
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, null, null);
    }

    /**
     * 执行POST请求
     *
     * @param url
     * @param headerMap
     * @param requestBody
     * @return
     */
    public static String doPost(String url, Map<String, String> headerMap, Object requestBody) {

        LOG.warn("doPost,url:{}, header:{}, requestBody:{}", url, JSON.toJSONString(headerMap), JSON.toJSONString(requestBody));

        HttpPost httpPost = new HttpPost(url);

        if (null != requestBody) {
            String jsonParam = JSONObject.toJSONString(requestBody);
            //解决中文乱码问题
            StringEntity entity = new StringEntity(jsonParam, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
        }

        return execute(httpPost, headerMap);
    }

    /**
     * 执行POST请求（无头信息）
     *
     * @param url
     * @param requestBody
     * @return
     */
    public static String doPost(String url, Object requestBody) {
        return doPost(url, null, requestBody);
    }

    /**
     * 执行POST请求，(无头信息，无body体)
     *
     * @param url
     * @return
     */
    public static String doPost(String url) {
        return doPost(url, null, null);
    }

    /**
     * https
     *
     * @return
     * @throws Exception
     */
    public static CloseableHttpClient getHttpClient() throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .setConnectionManagerShared(true)
                .build();
        return httpClient;
    }

    public static String readHttpResponse(HttpResponse httpResponse)
            throws ParseException, IOException {
        StringBuilder builder = new StringBuilder();
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        builder.append("status:" + httpResponse.getStatusLine());
        builder.append("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            builder.append("\t" + iterator.next());
        }
        // 判断响应实体是否为空
        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            builder.append("response length:" + responseString.length());
            builder.append("response content:" + responseString.replace("\r\n", ""));
        }
        return builder.toString();
    }


    /**
     * https请求
     *
     * @param url
     * @param header
     * @param paramJson
     * @return
     */
    public static String httpsPost(String urlStr, Map<String, String> header, String paramJson) {
        StringBuffer buffer = null;
        try {
            //创建SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new MyX509TrustManager()};
            //初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Content-Type", "application/json");

            if (MapUtils.isNotEmpty(header)) {
                for (String key : header.keySet()) {
                    conn.addRequestProperty(key, header.get(key));
                }
            }

            //设置当前实例使用的SSLSoctetFactory
            conn.setSSLSocketFactory(ssf);
            conn.connect();
            //往服务器端写内容
            if (StringUtils.isNotEmpty(paramJson)) {
                OutputStream os = conn.getOutputStream();
                os.write(paramJson.getBytes("utf-8"));
                os.close();
            }

            //读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            LOG.error("httpsPost faild", e);
        }
        return buffer.toString();
    }

    private static String execute(HttpRequestBase httpRequest, Map<String, String> headerMap) {
        CloseableHttpClient httpClient;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        httpClient = HttpClients.createDefault();

        if (MapUtils.isNotEmpty(headerMap)) {
            for (String key : headerMap.keySet()) {
                httpRequest.setHeader(key, headerMap.get(key));
            }
        }

        try {
            // httpClient对象执行请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpRequest);
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (Exception e) {
            LOG.error("http execute error, url:{}", e);
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    LOG.error("close http response error", e);
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    LOG.error("close http client error", e);
                }
            }
        }
        return result;
    }

    private static String buildParamStr(Map<String, Object> params) {
        if (MapUtils.isEmpty(params)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        if (MapUtils.isNotEmpty(params)) {
            for (String key : params.keySet()) {
                sb.append("&");
                sb.append(key);
                sb.append("=");
                sb.append(params.get(key));
            }
        }

        sb.replace(0, 1, "?");
        return sb.toString();
    }

}
