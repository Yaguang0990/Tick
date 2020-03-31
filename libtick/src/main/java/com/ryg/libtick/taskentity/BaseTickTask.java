package com.ryg.libtick.taskentity;

import java.util.Objects;

/**
 * @author ruanyaguang
 * @data 2019/4/16.
 */
public class BaseTickTask {

    //需要被取消的tickID
    public static final int START_VISM_DOWN_TIME = 100;
    public static final int ACTIVE_VISM_DOWN_TIME = 101;
    public static final int START_VISM_AGAIN = 102;
    public static final int STOP_VISM_DOWN_TIME = 103;
    public static final int NORMAL = 124;
    public static final int TEST = 125;

    /**
     * 定时回调次数:一直调用
     */
    public static final int REPEAT_MODEL_CYCLE = -1;

    /**
     * 指数避退方案
     */
    public static final int REPEAT_MODEL_BACKOFF = -2;

    /**
     * 倒计时模式:分钟
     */
    public static final int TICK_MODEL_Mill = 1;

    /**
     * 倒计时模式:秒
     */
    public static final int TICK_MODEL_SEC = 2;

    /**
     * 定时任务ID
     */
    private int tickID;

    /**
     * 倒计时模式
     * <p>
     * 模式一:以秒为倒计时的单位的模式
     * 模式二:以系统一分钟广播的倒计时模式
     */
    private int tickModel = TICK_MODEL_SEC;

    /**
     * 定时回调次数,默认一直调用
     * <p>
     * REPEAT_MODEL_CYCLE  一直调用
     */
    private int repeatCount = REPEAT_MODEL_CYCLE;

    /**
     * 定时任务间隔时间
     * <p>
     * 默认一秒回调一次
     */
    private int intervalsTime = 1;


    /**
     * 当前倒计时周期内的当前时间
     */
    private int curTickTime;

    /**
     * 任务成功一次之后,下一次任务开始时间
     * <p>
     * 默认无缝执行
     */
    private int nextStartTime = 0;

    private int tickSuccessCount = 0;


    public int getTickID() {
        return tickID;
    }

    public void setTickID(int tickID) {
        this.tickID = tickID;
    }

    public int getTickModel() {
        return tickModel;
    }

    public void setTickModel(int tickModel) {
        this.tickModel = tickModel;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public int getIntervalsTime() {
        return intervalsTime;
    }

    public void setIntervalsTime(int intervalsTime) {
        this.intervalsTime = intervalsTime;
        this.curTickTime = intervalsTime;
    }

    public int getNextStartTime() {
        return nextStartTime;
    }

    public void setNextStartTime(int nextStartTime) {
        this.nextStartTime = nextStartTime;
    }

    public int getCurTickTime() {
        return curTickTime;
    }

    public void resetCurTickTime() {
        curTickTime = intervalsTime;
    }

    public int getTickSuccessCount() {
        return tickSuccessCount;
    }

    public void setTickSuccessCount() {
        this.tickSuccessCount = this.tickSuccessCount + 1;
    }

    /**
     * 开始刷新倒计时
     *
     * @param curTime
     */
    public void setCurTickTime(int curTime) {
        this.curTickTime = curTime;
    }


    /**
     * 倒计时轮询次数,默认无限
     *
     * @param repeatCount
     */
    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTickTask tickTask = (BaseTickTask) o;
        return tickID == tickTask.tickID &&
                tickModel == tickTask.tickModel &&
                repeatCount == tickTask.repeatCount &&
                intervalsTime == tickTask.intervalsTime &&
                curTickTime == tickTask.curTickTime &&
                nextStartTime == tickTask.nextStartTime;
    }

    @Override
    public String toString() {
        return "BaseTickTask{" +
                "tickID=" + tickID +
                ", tickModel=" + tickModel +
                ", repeatCount=" + repeatCount +
                ", intervalsTime=" + intervalsTime +
                ", curTickTime=" + curTickTime +
                ", nextStartTime=" + nextStartTime +
                ", tickSuccessCount=" + tickSuccessCount +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickID, tickModel, repeatCount, intervalsTime, curTickTime, nextStartTime);
    }
}
