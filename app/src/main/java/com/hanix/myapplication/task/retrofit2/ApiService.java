package com.hanix.myapplication.task.retrofit2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 레트로핏 컨트롤러 > 여기서 서버를 통해 was의 컨트롤러를 부른다.
 */
public interface ApiService {

    /** POST 방식이 아니라 GET 방식이어서 Landroid.graphics 에러가 남
     * 웬만하면 GET 보다 POST 쓰자 **/
    @GET("insertUserAn")
    Call<Void> insertUserAn(@Query("adre_user") String adre_user, @Query("adre_location") String adre_location); // 사용자 등록 (사용자 고유id, 국가)

    @GET("checkUserAn")
    Call<String> checkUserAn(@Query("adre_user") String adre_user);                                                                 // 사용자 체크 (있는지 없는지 유무)

    @GET("updateUserAn")
    Call<Void> updateUserAn(@Query("adre_user") String adre_user);                                                                // 결제 시 결제값 업데이트 (결제값 : 1)

    @GET("checkPayAn")
    Call<String> checkPayAn(@Query("adre_user") String adre_user);                                                                   // 결제회원 체크 (결제 시 return 1)
}
