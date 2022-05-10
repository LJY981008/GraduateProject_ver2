package com.example.schoollifeproject.model

/**
 * 서버연결을 위한 PHP path object
 * 작성자 : 이준영, 박동훈
 */
object MyApp {
    const val TAG: String = "로그"

    // * 서버에 있는 php 파일 위치
    //  ! 서버 IP 제외
    // ex) /파일명.php
    const val Select_url: String = "/php/login_select.php"
    const val Register_url: String = "/php/register_insert.php"
    const val notice_key_search_url: String = "/php/notice_key_search.php"
    const val notice_save_url: String = "/php/notice_insert.php"
    const val item_save_url: String = "/php/item_save.php"
    const val item_load_url: String = "/php/item_load.php"
    const val map_public_url: String = "/php/map_select.php"
    const val map_update_url: String = "/php/map_update.php"
    const val map_popular_url: String = "/php/map_popular.php"
    const val map_like_url: String = "/php/map_like.php"
    const val item_file_save_url: String = "/php/item_file_save.php"
    const val item_file_load_url: String = "/php/item_file_load.php"
    const val item_file_del_url: String = "/php/item_file_del.php"
    const val map_list_url: String = "/php/map_list.php"
}