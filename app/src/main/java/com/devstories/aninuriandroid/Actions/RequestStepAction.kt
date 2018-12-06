package com.devstories.aninuriandroid.Actions

import com.devstories.aninuriandroid.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

object RequestStepAction {

    // 스텝 변경
    fun change_step(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/request_step/change_step.json", params, handler)
    }

    //스텝 체크
    fun check_step(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/request_step/check_step.json", params, handler)
    }

    //스텝 삭제
    fun end_step(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/request_step/end_step.json", params, handler)
    }

}