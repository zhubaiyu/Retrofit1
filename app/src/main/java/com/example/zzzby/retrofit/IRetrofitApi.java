package com.example.zzzby.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 *
 * Created by zby on 2016/10/17.
 */
public interface IRetrofitApi {
    //下载文件的接口
    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url() String url);
}
