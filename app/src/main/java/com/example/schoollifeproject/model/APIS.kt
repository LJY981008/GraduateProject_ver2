package com.example.schoollifeproject.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * 서버 DB 연결 Interface
 * 작성자 : 이준영, 박동훈
 * */
interface APIS {


    //로그인 정보 호출
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    //로그인 정보 호출
    @FormUrlEncoded
    @POST(MyApp.login_url)
    fun login_users(
        @Field("type") type: Int,
        @Field("ID") userID: String
    ): Call<PostModel>

    //회원가입 정보 호출
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.Register_url)
    fun register_users(
        @Field("createID") createID: String,
        @Field("createPassword") createPassword: String,
        @Field("createName") createName: String,
        @Field("createEmail") createEmail: String
    ): Call<PostModel>

    //공지사항 호출
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.notice_load_url)
    fun notice_load(
        @Field("dum") dum: Int
    ): Call<List<NoticeListModel>>

    //게시글 호출
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.bbs_load_url)
    fun bbs_load(
        @Field("dum") dum: Int
    ): Call<List<FreeListModel>>

    //공부게시판 호출
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.info_load_url)
    fun info_load(
        @Field("dum") dum: Int
    ): Call<List<InfoListModel>>

    //글작성
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.note_write_url)
    fun notice_save(
        @Field("type") type: Int,
        @Field("noticeTitle") noticeTitle: String,
        @Field("userID") userID: String,
        @Field("date") date: String,
        @Field("noticeContents") noticeContents: String
    ): Call<PostModel>

    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.item_save_url)
    fun item_save(
        @Field("itemID") itemID: String,
        @Field("targetItemID") targetItemID: String,
        @Field("parentItemID") parentItemID: String,
        @Field("itemTop") itemTop: String,
        @Field("itemLeft") itemLeft: String,
        @Field("userID") userID: String,
        @Field("itemContent") itemContent: String,
        @Field("itemCount") itemCount: Int,
        @Field("itemWidth") itemWidth: String,
        @Field("itemHeight") itemHeight: String,
        @Field("noteContent") noteContent: String?,
        @Field("mode") mode: String
    ): Call<PostModel>

    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.item_load_url)
    fun item_load(
        @Field("userID") userID: String
    ): Call<List<ItemModel>>

    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.map_public_url)
    fun map_public(
        @Field("userID") userID: String
    ): Call<PostModel>

    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.map_update_url)
    fun map_update(
        @Field("userID") userID: String,
        @Field("public") public: Int,
        @Field("password") password: String
    ): Call<PostModel>

    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.map_popular_url)
    fun map_popular(
        @Field("userID") userID: String
    ): Call<PostModel>

    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.map_like_url)
    fun map_like(
        @Field("userID") userID: String,
        @Field("mapID") mapID: String
    ): Call<PostModel>

    @Multipart // @Multipart 사용 시 @Part로 보내줘야 한다.
    @POST(MyApp.item_file_save_url)
    fun item_file_save(
        @Part file: MultipartBody.Part?,
        @Part("userID") userID: String,
        @Part("itemID") itemID: String
    ): Call<String>

    @FormUrlEncoded
    @POST(MyApp.item_file_load_url)
    fun item_file_load(
        @Field("userID") userID: String,
        @Field("itemID") itemID: String
    ): Call<List<FileModel>>

    @FormUrlEncoded
    @POST(MyApp.item_file_del_url)
    fun item_file_del(
        @Field("userID") userID: String,
        @Field("itemID") itemID: String,
        @Field("fileRealName") fileRealName: String
    ): Call<PostModel>

    @GET
    fun item_file_down(
        @Url filePath: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST(MyApp.note_delete_url)
    fun note_delete(
        @Field("type") type: Int,
        @Field("mapID") mapID: Int
    ): Call<PostModel>

    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.logout_url)
    fun logout(
        @Field("userID") userID: String
    ): Call<PostModel>

    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.map_list_url)
    fun map_list(
        @Field("dum") dum: Int
    ): Call<List<MapListModel>>

    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.delete_info_uri)
    fun delete_info(
        @Field("userID") userID: String
    ): Call<PostModel>

    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    @FormUrlEncoded
    @POST(MyApp.note_update_url)
    fun notice_update(
        @Field("type") type: Int,
        @Field("key") key: Int,
        @Field("title") title: String,
        @Field("contents") contents: String
    ): Call<PostModel>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.

        private const val BASE_URL = "https://hjk709914.cafe24.com"
        fun create(): APIS {
            val gson: Gson = GsonBuilder().setLenient().create();
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIS::class.java)
        }
    }

}
