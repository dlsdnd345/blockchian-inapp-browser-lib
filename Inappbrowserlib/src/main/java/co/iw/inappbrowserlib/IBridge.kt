package co.iw.inappbrowserlib

import android.webkit.JavascriptInterface


/**
 * 웹 통신 인터페이스
 */
interface IBridge {


    @JavascriptInterface
    fun getNetwork(): Number

    @JavascriptInterface
    fun enable(): String

    @JavascriptInterface
    fun sendAsync(payload: String): String


    /**
     * WebPage -> Native
     * 웹에서 안드로이드로 액션 요청
     * @param id : webRequest 식별하기 위한 고유값
     * @param action : 미리 정의된 액션 (ex SHOW_TEXT_DIALOG , GO_TO_SCREEN)
     * @param params : 액션에 대한 상세 데이터 (jsonString)
     */
    @JavascriptInterface
    fun webRequest(id: String, action: String, params: String)


    /**
     * Native -> WebPage
     * webRequest 에 대한 결과 콜백
     * @param id : webRequest 에서 전달받은 고유값
     * @param action : webRequest 에서 전달받은 액션값
     * @param params : web에 전달해야하는 액션에 대한 결과 데이터 (jsonString)
     */
    fun webRequestCallBack(id: String, action: String, callbackParams: String)


    /**
     * Native -> WebPage
     * 안드로이드에서 웹에 요청
     * @param id : nativeRequest 식별하기 위한 고유값
     * @param action : 미리 정의된 액션 (ex )
     * @param params : 액션에 대한 상세 데이터 (jsonString)
     */
    fun nativeRequest(id: String, action: String, params: String)


    /**
     * WebPage -> Native
     * nativeRequest 에 대한 결과 콜백
     * @param id : nativeRequest 에서 전달받은 고유값
     * @param action : nativeRequest 에서 전달받은 액션값
     * @param params : native에 전달해야하는 액션에 대한 결과 데이터 (jsonString)
     */
    @JavascriptInterface
    fun nativeRequestCallBack(id: String, action: String, callbackParams: String)

}