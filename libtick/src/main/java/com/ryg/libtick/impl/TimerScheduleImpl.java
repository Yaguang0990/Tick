package com.ryg.libtick.impl;


import com.ryg.libtick.listener.BaseCallBackListener;
import com.ryg.libtick.taskentity.BaseTickTask;

/**
 * @author ruanyaguang
 * @data 2019/4/16.
 */
public interface TimerScheduleImpl {

    void addTickTask(BaseTickTask task, BaseCallBackListener listener);


    void removeTask(int tickID);

    void removeAllTask();
}
