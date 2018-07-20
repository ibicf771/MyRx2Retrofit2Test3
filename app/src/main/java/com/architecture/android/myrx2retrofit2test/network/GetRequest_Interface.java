package com.architecture.android.myrx2retrofit2test.network;


import com.architecture.android.myrx2retrofit2test.bean.Translation;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yangsimin on 2018/4/24.
 */

public interface GetRequest_Interface {

    @GET("ajax.php?a=fy&f=en&t=zh")
    Call<Translation> getCall(@Query("w") String w);

    @GET("ajax.php?a=fy&f=en&t=zh")
    Observable<Translation> getRxCall(@Query("w") String w);

}
