package com.architecture.android.myrx2retrofit2test.network;


import com.architecture.android.myrx2retrofit2test.bean.Translation;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yangsimin on 2018/4/26.
 */

public interface GetRequestRxInterface {
    @GET("ajax.php?a=fy&f=en&t=zh")
    Observable<Translation> getRxCall(@Query("w") String w);
}
