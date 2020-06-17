package com.hanix.myapplication.task.firebase;

import android.content.Context;


import com.hanix.myapplication.common.beans.UserBean;
import com.hanix.myapplication.common.constants.URLApi;
import com.hanix.myapplication.common.interfaces.TaskCallbackInterface;
import com.hanix.myapplication.common.utils.PrefUtil;
import com.hanix.myapplication.task.CommonRestTask;
import com.hanix.myapplication.task.retrofit2.ApiService;
import com.hanix.myapplication.task.retrofit2.MyRetrofit2;

import java.util.Map;

public class UpdateTokenTask extends CommonRestTask {

    ApiService service;

    public UpdateTokenTask(Context context, TaskCallbackInterface taskCallbackInterface) {
        super(context, taskCallbackInterface);
        service = MyRetrofit2.getRetrofit().create(ApiService.class);

        UserBean userBean = PrefUtil.getInstance(context).getUserBean();

        if(userBean== null) {              // TODO : 나중에 변경해야 함
            mIsTaskStop = true;
            return;
        }

        String tokenId = PrefUtil.getInstance(context).getFcmTokenId();

        Map<String, String> paramMap = URLApi.updateAppToken(userBean);
        mReqUrl = paramMap.get(URLApi.KEY_URL);
        mReqUrlQuery = paramMap.get(URLApi.KEY_QUERY);
    }

    @Override
    public void onPostProc(String respStr) {

    }
}
