package com.hanix.myapplication.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.common.constants.AppConstants;
import com.hanix.myapplication.common.interfaces.TaskCallbackInterface;
import com.hanix.myapplication.common.utils.DlgUtil;

import java.io.InterruptedIOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.nio.channels.ClosedChannelException;

import javax.net.ssl.SSLException;

public class CommonRestTask extends AsyncTask<String, Void, String> {

    private Context mContext;

    private boolean mIsException;
    private String mExceptionMsg = "";
    private Exception mException;

    public String mReqUrl = "";
    public String mReqUrlQuery = "";
    public TaskCallbackInterface mTaskCallbackInterFace;
    public boolean mIsDlgShow = false;
    public boolean mIsTaskStop = false;

    //Constructor
    public CommonRestTask(Context context, TaskCallbackInterface taskCallbackInterface) {
        mContext = context;
        mTaskCallbackInterFace = taskCallbackInterface;
    }

    private String getHttpJson(String url) {
        mIsException = false;
        String rawResponse = "";

        try {

            GLog.d("URL : " + url);
            HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();

            HttpContent content = new ByteArrayContent("application/x-www-form-urlencoded", mReqUrlQuery.getBytes());

            HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(url), content);
            request.setConnectTimeout(AppConstants.TIME_OUT);
            request.setReadTimeout(AppConstants.TIME_OUT);
            rawResponse = request.execute().parseAsString();

            GLog.i("RESULT : " + rawResponse);

        } catch (Exception e) {
            mException = e;
            mIsException = true;
            mExceptionMsg = e.getMessage();
            e.printStackTrace();
        }

        return rawResponse;
    }

    public static<T> T getBeanParse (final Context context, String jsonString, Class<T>  classOfT) {
        if(TextUtils.isEmpty(jsonString)) return null;

        try {
            return new Gson().fromJson(jsonString, classOfT);
        } catch (final Exception e)  {
            e.printStackTrace();

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast.makeText(context, "파싱 에러", Toast.LENGTH_LONG).show();
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                }
            });
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        if(mIsDlgShow) {
            DlgUtil.showWaitingDlg(mContext);
        }
    }

    @Override
    protected void onPostExecute(String respStr) {
        if(mIsTaskStop) {
            return;
        }

        if(mIsException) {
            if(mException != null &&
                    (mException instanceof UnknownHostException || mException instanceof BindException ||
                            mException instanceof ConnectException || mException instanceof SocketTimeoutException ||
                            mException instanceof ClosedChannelException || mException instanceof InterruptedIOException ||
                            mException instanceof NoRouteToHostException || mException instanceof PortUnreachableException ||
                            mException instanceof ProtocolException || mException instanceof SocketException ||
                            mException instanceof SSLException || mException instanceof UnknownServiceException ||
                            mException.getCause() instanceof MalformedURLException
                    )
            )
            {
                GLog.d(mExceptionMsg);
                DlgUtil.showMsgDlg(mContext, mExceptionMsg);
                return;
            }
        } else { //에러 없을시 실행
            //1차 호출
            onPostProc(respStr);

            //2차 호출
            if(mTaskCallbackInterFace != null) {
                mTaskCallbackInterFace.onPostProc(respStr, this);
            }
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        if(mIsTaskStop) return "";
        return getHttpJson(mReqUrl);
    }

    /** 통신이후 처리 */
    public void onPostProc(String respStr) {

    }

}
