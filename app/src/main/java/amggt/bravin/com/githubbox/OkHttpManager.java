package amggt.bravin.com.githubbox;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by shijg on 2017/9/25.
 */

public class OkHttpManager {

    public static String TAG = OkHttpManager.class.getName();

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpManager instance;


    public static OkHttpManager getInstance() {
        if (instance == null) {
            instance = new OkHttpManager();
        }
        return instance;
    }

    public OkHttpClient mOkHttpClient; // okhttp client 请求类
    public CookieManager cookieManager;// cookies 管理类

    private OkHttpManager() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public void initCookie(Context context, URI uri, List<HttpCookie> httpCookies) {
//        PersistenceCookieStore persistenceCookieStore = new PersistenceCookieStore(context);
//        if (!httpCookies.isEmpty()) {
//            for (HttpCookie httpCookie : httpCookies) {
//                persistenceCookieStore.add(uri, httpCookie);
//            }
//        }
//        // 创建 cookies 管理类
//        cookieManager = new CookieManager(persistenceCookieStore, CookiePolicy.ACCEPT_ALL);
//        // client 设置cookies 管理类
//        mOkHttpClient.setCookieHandler(cookieManager);
    }

    /**
     * 获取 cookies 管理类
     *
     * @return 返回cookies 管理类
     */
    public CookieManager getCookieManager() {
        return cookieManager;
    }

    /**
     * 初始化请求拦截器
     *
     * @param context 上下文
     */
    public void initInterceptor(Context context) {
        // 添加User-Agent拦截器
//        mOkHttpClient.interceptors().add(new UserAgentInterceptor(context));
    }

    /**
     * 获取HttpGet 请求方式的 Request
     *
     * @param requestUrl 接口地址
     * @return Request
     */
    public Request getHttpGetRequest(String requestUrl, String requestString) {
        if (requestUrl == null) return null;
        if (requestString != null && !requestString.trim().equals("")) {
            requestUrl = requestUrl.concat(requestString);
        }
        Request.Builder builder = assembleFormRequestHeader()
                .url(requestUrl);

        return builder.build();
    }

    // TODO

    /**
     * 获取HttpDelete 请求方式的 Request
     *
     * @param requestUrl 接口地址
     * @return Request
     */
    public Request getHttpDeleteRequest(String requestUrl, String requestString) {
        if (requestUrl == null) return null;
        if (requestString != null && !requestString.trim().equals("")) {
            requestUrl = requestUrl.concat(requestString);
        }
        Request.Builder builder = assembleFormRequestHeader()
                .url(requestUrl);

        return builder.build();
    }

    /**
     * 获取HttpPost 请求方式的 Request
     *
     * @param requestUrl 接口地址
     * @return Request
     */
    public Request getHttpPostRequest(String requestUrl, Map<String, Object> requestData) {
        if (requestUrl == null) return null;
        Request.Builder builder = null;
        if (requestData != null && !requestData.isEmpty()) {
            FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
            Set<String> keySets = requestData.keySet();
            for (String key : keySets) {
                formEncodingBuilder.add(key, requestData.get(key).toString());
            }
            builder = assembleFormRequestHeader().url(requestUrl).post(formEncodingBuilder.build());
        } else {
            builder = assembleFormRequestHeader().url(requestUrl);
        }

        return builder.build();
    }

    /**
     * 获取HttpPost 请求方式的 Request
     *
     * @param requestUrl 接口地址
     * @param jsonData   请求数据
     * @return Request
     */
    public Request getHttpPostRequest(String requestUrl, String jsonData) {
        if (requestUrl == null) return null;
        Request.Builder builder = null;
        if (jsonData != null) {
            RequestBody body = RequestBody.create(JSON, jsonData);
            builder = assembleJsonRequestHeader().url(requestUrl).post(body);
        } else {
            builder = assembleFormRequestHeader().url(requestUrl);
        }
        return builder.build();
    }


    /**
     * 获取提交文件的 请求方式的 Request
     *
     * @param requestUrl 接口地址
     * @return Request
     */
    public Request getFileRequest(String requestUrl, File file, Map<String, Object> requestData) {
        if (requestUrl == null) return null;
        RequestBody body = new MultipartBuilder().type(MultipartBuilder.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file)).build();
        Request.Builder builder = assembleFormRequestHeader()
                .url(requestUrl)
                .post(body);
        // 请求参数
        if (requestData != null && !requestData.isEmpty()) {
            FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
            Set<String> keySets = requestData.keySet();
            for (String key : keySets) {
                formEncodingBuilder.add(key, requestData.get(key).toString());
            }
            builder.post(formEncodingBuilder.build());
        }

        return builder.build();
    }


    /**
     * 设置request的头部
     *
     * @return Request.Builder
     */
    private Request.Builder assembleFormRequestHeader() {
        return new Request.Builder()
                .addHeader("terminal_type", "DemanderApp")
                .addHeader("device_system_no", "Android" + Build.VERSION.RELEASE)
                .addHeader("X-Mobile-With", "MobileHttpRequest")
//                .addHeader("operator_longitude", BaseConstant.longtitude + "")
//                .addHeader("operator_latitude", BaseConstant.latitude + "")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json;charset=utf-8")
                .addHeader("Connection", "close");
    }

    /**
     * 设置request的头部
     *
     * @return Request.Builder
     */
    private Request.Builder assembleJsonRequestHeader() {
        return new Request.Builder()
                .addHeader("terminal_type", "DemanderApp")
                .addHeader("device_system_no", "Android" + Build.VERSION.RELEASE)
                .addHeader("X-Mobile-With", "MobileHttpRequest")
//                .addHeader("operator_longitude", BaseConstant.longtitude + "")
//                .addHeader("operator_latitude", BaseConstant.latitude + "")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json;charset=utf-8")
                .addHeader("Connection", "close");
    }

//    /**
//     * 该不会开启异步线程。
//     *
//     * @param context 上下文
//     * @param request 请求实体
//     * @return 请求结果
//     * @throws IOException IO异常
//     */
//    public Response execute(Context context, Request request) throws IOException {
//        return mOkHttpClient.newCall(request).execute();
//    }


    /**
     * 提交post请求
     *
     * @param requestUrl  接口请求地址
     * @param requestData post请求参数map
     * @param callback    client请求结果回调
     */
    public void post(String requestUrl, Map<String, Object> requestData, Callback callback) {
        // 获取HttpPost请求request
        Request httpPostRequest = getHttpPostRequest(requestUrl, requestData);
        // 执行异步请求
        mOkHttpClient.newCall(httpPostRequest).enqueue(callback);
    }

    /**
     * 提交post请求
     *
     * @param requestUrl  接口请求地址
     * @param requestData json 请求数据
     * @param callback    client请求结果回调
     */
    public void post(String requestUrl, String requestData, Callback callback) {
        // 获取HttpPost请求request
        Request httpPostRequest = getHttpPostRequest(requestUrl, requestData);
        // 执行异步请求
        mOkHttpClient.newCall(httpPostRequest).enqueue(callback);
    }


    /**
     * 提交get请求
     *
     * @param requestUrl        接口请求地址
     * @param requestDataString Get请求参数String
     * @param callback          client请求结果回调
     */
    public void get(String requestUrl, String requestDataString, Callback callback) {
        // 获取HttpGet请求request
        Request httpGetRequest = getHttpGetRequest(requestUrl, requestDataString);
        // 执行异步请求
        mOkHttpClient.newCall(httpGetRequest).enqueue(callback);
    }

    public void cancelRequest(Object tag) {
    }
}
