package com.hanix.myapplication.common.interfaces;


import com.hanix.myapplication.task.CommonRestTask;

/**
 * Task 공통 Callback Interface
 */
public interface TaskCallbackInterface {

    public void onPostProc(String respStr, CommonRestTask commonRestTask);

}
