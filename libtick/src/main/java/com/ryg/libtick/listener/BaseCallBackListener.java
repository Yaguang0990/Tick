package com.ryg.libtick.listener;

/**
 * 回调到其他线程
 *
 * @author ruanyaguang
 * @data 2019/4/16.
 */
public interface BaseCallBackListener {
    /**
     * 时间到
     */
    void timeUp(int curRepeatTime);

}
