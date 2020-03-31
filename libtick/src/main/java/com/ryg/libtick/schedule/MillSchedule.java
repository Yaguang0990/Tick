package com.ryg.libtick.schedule;


import com.ryg.libtick.impl.TimerScheduleImpl;
import com.ryg.libtick.listener.BaseCallBackListener;
import com.ryg.libtick.taskentity.BaseTickTask;

/**
 * 分钟为单位的倒计时
 *
 * @author ruanyaguang
 * @data 2019/4/16.
 */
public class MillSchedule implements TimerScheduleImpl {

    @Override
    public void addTickTask(BaseTickTask task, BaseCallBackListener listener) {

    }


    @Override
    public void removeTask(int tickID) {

    }

    @Override
    public void removeAllTask() {

    }
}
