package com.ryg.libtick;


import com.ryg.libtick.impl.TimerScheduleImpl;
import com.ryg.libtick.listener.BaseCallBackListener;
import com.ryg.libtick.schedule.MillSchedule;
import com.ryg.libtick.schedule.SecondSchedule;
import com.ryg.libtick.taskentity.BackOffTickTask;
import com.ryg.libtick.taskentity.BaseTickTask;
import com.ryg.libtick.taskentity.MillTickTask;
import com.ryg.libtick.taskentity.SecTickTask;

import java.util.Map;

/**
 * @author ruanyaguang
 * @data 2019/4/16.
 */
public class TimeSchedule implements TimerScheduleImpl {

    private volatile static TimeSchedule singleton = null;

    public SecondSchedule secondSchedule = new SecondSchedule();
    public MillSchedule millSchedule = new MillSchedule();

    public static TimeSchedule getInstance() {
        if (singleton == null) {
            synchronized (TimeSchedule.class) {
                if (singleton == null) {
                    singleton = new TimeSchedule();
                }
            }
        }
        return singleton;
    }


    public void continueTask(int tickID) {
        secondSchedule.continueBackOff(tickID);
    }


    /**
     * 在 SilverReceiver 中的(isTimeTick)方法中调用
     * <p>
     * 系统一分钟广播调用时调到这里来参与
     */
    public void millTimeTick() {

    }

    @Override
    public void addTickTask(BaseTickTask task, BaseCallBackListener listener) {

        if (task instanceof SecTickTask) {

            secondSchedule.addTickTask(task, listener);
            return;
        }

        if (task instanceof MillTickTask) {

            millSchedule.addTickTask(task, listener);
            return;
        }

        if (task instanceof BackOffTickTask) {

            secondSchedule.addTickTask(task, listener);
            return;
        }

    }


    public Map<Integer, SecondSchedule.Node> getSecMap() {

        return secondSchedule.getTickTaskMap();
    }

    @Override
    public void removeTask(int tickID) {
        secondSchedule.removeTask(tickID);
    }

    @Override
    public void removeAllTask() {
        secondSchedule.removeAllTask();
    }

    public void clear() {

        if (null != secondSchedule) {
            secondSchedule.stopTick();
        }
    }

}
