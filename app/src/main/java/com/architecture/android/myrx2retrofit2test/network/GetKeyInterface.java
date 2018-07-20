package com.architecture.android.myrx2retrofit2test.network;

import com.architecture.android.myrx2retrofit2test.bean.Root;
import com.architecture.android.myrx2retrofit2test.bean.Translation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yangsimin on 2018/7/18.
 */

public interface GetKeyInterface {

    @GET("key")
    Call<Root> getCall();

    @GET("key")
    Call<String> getCallString();


    @GET("geocoding/v1/aegis/万达广场.json?")
    Call<String> getCallSearch(@Query("crypto") String crypto, @Query("checksum") String checksum, @Query("t") String timestamp);

}
