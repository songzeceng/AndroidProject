package com.example.songzeceng.studyofretrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.Observable;

/**
 * Created by songzeceng on 2018/2/4.
 */

public interface IRequest {
    @GET("ajax.php")
            //ajax.php?a=fy&f=auto&t=auto&w=hello%20world
    Call<Result> getResult(@Query("a") String a,@Query("f") String from
            ,@Query("t") String to,@Query("w") String w);
    //@Query用于在？之后传参

    //每一个方法都要有http请求方法的注解
    @GET("ajax.php")
    Observable<Result> getResultInRxJava(@Query("a") String a,@Query("f") String from
            ,@Query("t") String to,@Query("w") String w);
    //RxJava的运用
}
