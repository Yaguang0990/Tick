package com.ryg.libtick.taskentity;


/**
 * 以秒为单位倒计时任务栈
 *
 * @author ruanyaguang
 * @data 2019/4/16.
 */
public class SecTickTask extends BaseTickTask {

    public SecTickTask() {
    }

    public SecTickTask(int tickID, int intervalsTime, int repeatCount) {

        setTickID(tickID);//设置ID
        setIntervalsTime(intervalsTime);
        setRepeatCount(repeatCount);
    }


    public SecTickTask(int tickID, int intervalsTime) {

        setTickID(tickID);//设置ID
        setIntervalsTime(intervalsTime);
    }
}
