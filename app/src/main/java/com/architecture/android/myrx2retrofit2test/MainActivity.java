package com.architecture.android.myrx2retrofit2test;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.architecture.android.myrx2retrofit2test.bean.Root;
import com.architecture.android.myrx2retrofit2test.bean.Translation;
import com.architecture.android.myrx2retrofit2test.method.SM3Digest;
import com.architecture.android.myrx2retrofit2test.method.SM4Utils;
import com.architecture.android.myrx2retrofit2test.network.GetKeyInterface;
import com.architecture.android.myrx2retrofit2test.network.GetRequestRxInterface;
import com.architecture.android.myrx2retrofit2test.network.GetRequest_Interface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit;

    OkHttpClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SM4Utils.testDec();

        mClient = new OkHttpClient.Builder()
                .addInterceptor(new ReceivedCookiesInterceptor())
                .addInterceptor(new AddCookiesInterceptor())
                .addNetworkInterceptor(loggingInterceptor).build();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


//        startRxRequest();
//        startRequest();
        startKeyRequest();

//        startKeyRequest2();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startKeyRequest3();
//                startKeyRequest2();
            }
        });
    }


    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            Log.i("拦截器", "ApiServiceHelper.createApiService().HttpLoggingInterceptor.log().message -> " + message);
        }
    });

    private void startRxRequest(){
        createRxRetrofit();
        createRxGetRequest();
        callRxResult();
    }

    private void createRxRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/") // 设置 网络请求 Url
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();
    }

    private void createRxGetRequest(){
        request = retrofit.create(GetRequest_Interface.class);
    }




    private void callRxResult(){
//        Observable<Translation> observable = request.getRxCall("hello good");
        request.getRxCall("hello good")
                .subscribeOn(Schedulers.io()) // we want to add a list item at time of subscription
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Translation>() {
            @Override
            public void accept(Translation translation) throws Exception {
                Log.d("Translation", "callRxResult " );
                translation.show();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("Translation", "callRxResult " );
                Log.d("Translation", "error " + throwable.getMessage());
            }
        });
    }


//--------------------------------------------------------------------
    private GetRequest_Interface request;

    private void startRequest(){
        createRetrofit();
        createGetRequest();
        callResult();
    }

    private void createRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();
    }

    private void createGetRequest(){
        request = retrofit.create(GetRequest_Interface.class);
    }

    private void callResult(){
        Call<Translation> call = request.getCall("nice you");

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<Translation>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {
                // 步骤7：处理返回的数据结果
                response.body().show();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<Translation> call, Throwable throwable) {
                System.out.println("连接失败");
            }
        });
    }

    //----------------------------------------------------------------------

    private Root mRoot;

    private String newKey;

    private long timestampVar = 0;

    private void startKeyRequest(){
        createKeyRetrofit();
        createGetKey();
        callKeyResult();
    }

    private void createKeyRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.6.156:30000/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .client(mClient)
                .build();
    }
    private GetKeyInterface keyRequest;

    private void createGetKey(){
        keyRequest = retrofit.create(GetKeyInterface.class);
    }

    private void callKeyResult(){
        Call<Root> call = keyRequest.getCall();

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<Root>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                Log.d("root_log", " onResponse");
                System.out.println("连接成功  " + response.body().getAllMsg());
                mRoot = response.body();
                // 步骤7：处理返回的数据结果
                response.body().show();
                encrypt();

            }

            //请求失败时回调
            @Override
            public void onFailure(Call<Root> call, Throwable throwable) {
                System.out.println("连接失败  " + throwable.toString());

            }
        });
    }

    private void encrypt(){
        SM4Utils sm4Utils = new SM4Utils();
        String oldKey = sm4Utils.getDecStr(mRoot.getResultValue().getKey(), "JeF8U9wHFOMfs2Y8");
        System.out.println("连接成功 oldKey  " + oldKey);
        String tempKey = oldKey.substring(0, 8);
        System.out.println("连接成功 tempKey  " + tempKey);
        newKey = "ow5gneap" + tempKey;
        System.out.println("连接成功 newKey  " + newKey);
        System.out.println("连接成功 currentTimeMillis  " + System.currentTimeMillis());
        System.out.println("连接成功 getTimestamp  " + mRoot.getResultValue().getTimestamp());
        timestampVar = mRoot.getResultValue().getTimestamp() - System.currentTimeMillis();

    }

    private String getCrypto(String value){
        SM4Utils sm4Utils = new SM4Utils();
        return  sm4Utils.getEncStr(value, newKey);
    }

    private String getChecksum(String value){
        SM3Digest sm3 = new SM3Digest();
        return sm3.encrypt(value);
    }


    private String getCrypto(){
        String source = "proximity=118.1214739,24.47387227&autocomplete=true&start=0&limit=10";
        return  getCrypto(source);
    }

    private String getChecksum(){
        String source = "proximity=118.1214739,24.47387227&autocomplete=true&start=0&limit=10";
        return getChecksum(source);
    }


    private String getTimestamp(){
        return String.valueOf(System.currentTimeMillis()+timestampVar);
    }


    //-----------------------------------------------------------------------------


    private void startKeyRequest3(){
        createKeyRetrofit3();
        createGetKey3();
        callKeyResult3();
    }

    private void createKeyRetrofit3(){


        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.6.156:30000/") // 设置 网络请求 Url
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(mClient)
                .build();
    }
//    private GetKeyInterface keyRequest;

    private void createGetKey3(){
        keyRequest = retrofit.create(GetKeyInterface.class);
    }

    private void callKeyResult3(){
        Log.i("拦截器", "getCrypto " + getCrypto());
        Log.i("拦截器", "getCrypto " + getCrypto());
        Log.i("拦截器", "getChecksum " + getChecksum());
        Log.i("拦截器", "getChecksum " + getChecksum());
        Log.i("拦截器", "getTimestamp " + getTimestamp());
        Call<String> call = keyRequest.getCallSearch(getCrypto(), getChecksum(), getTimestamp());

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<String>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("root_log", " onResponse");
                System.out.println("连接成功  " + response.body().toString());

                SM4Utils sm4Utils = new SM4Utils();

                String str = sm4Utils.getDecStr("T9zRSuLP6+li\\/nTPjEFc111Hotu1Q36eF06eOG8Lq\\/IQ4HCW8WIRmkzgqCqTJJ8mbxIv5tYv5fQISipN97OJzEtwP7wQ7nVsKBnHHnXZx5E5XCTJpBk24xzmlN+DF7ff", "ow5gneapacb0543a");
                Log.i("拦截器str ", str);

            }

            //请求失败时回调
            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                System.out.println("连接失败  " + throwable.toString());

            }
        });
    }



//-----------------------------------------------------------------------------------

    private void startKeyRequest2(){
        createKeyRetrofit2();
        createGetKey2();
        callKeyResult2();
    }

    private void createKeyRetrofit2(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.6.156:30000/") // 设置 网络请求 Url
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
//    private GetKeyInterface keyRequest;

    private void createGetKey2(){
        keyRequest = retrofit.create(GetKeyInterface.class);
    }

    private void callKeyResult2(){
        Call<String> call = keyRequest.getCallString();

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<String>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("root_log", " onResponse");
                System.out.println("连接成功  " + response.body().toString());
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                System.out.println("连接失败  " + throwable.toString());

            }
        });
    }

    //----------------------------------------------------------

    List<String> cookieList = new ArrayList<>();

    public class ReceivedCookiesInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            okhttp3.Response originalResponse = chain.proceed(chain.request());
            //这里获取请求返回的cookie
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                final StringBuffer cookieBuffer = new StringBuffer();
                List<String> list = originalResponse.headers("Set-Cookie");
                cookieList = list;
                for (String str : list) {
                    Log.i("拦截器ReceivedCookies", str);
                }
            }
            return originalResponse;
        }
    }

    public class AddCookiesInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            final Request.Builder builder = chain.request().newBuilder();
            if(cookieList != null && cookieList.size() > 0){
                Log.i("拦截器AddCookies", cookieList.get(0));
                builder.addHeader("Cookie", cookieList.get(0));
            }
            return chain.proceed(builder.build());
        }
    }

}
