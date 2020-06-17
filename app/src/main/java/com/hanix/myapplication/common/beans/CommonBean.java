package com.hanix.myapplication.common.beans;

import java.io.Serializable;

public class CommonBean implements Serializable {

    /** 결과상태 **/
    public String result;
    /** 결과 메시지 **/
    public String resultMsg;

    /** 토큰 **/
    public String authToken;
    /** 생성날짜 **/
    public String regDt;
    /** 수정날짜 **/
    public String updDt;

    /** 검색어 */
    public String searchText = "";
    /** DB 시작 로우 번호 */
    public int startRow = 1;
    /** 페이지당 보여줄 레코드수 (변경가능하게 set/get 추가함) */
    public int countPerRecord = 10;
}
