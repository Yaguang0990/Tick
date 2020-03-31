package com.ryg.libtick.schedule;


import android.os.Handler;
import android.util.Log;

import com.ryg.libtick.impl.TimerScheduleImpl;
import com.ryg.libtick.listener.BaseCallBackListener;
import com.ryg.libtick.listener.MainThreadCallBackListener;
import com.ryg.libtick.listener.ThreadCallBackListener;
import com.ryg.libtick.taskentity.BackOffTickTask;
import com.ryg.libtick.taskentity.BaseTickTask;
import com.ryg.libtick.thread.AsyncCallback;
import com.ryg.libtick.thread.EasyThread;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import static com.ryg.libtick.taskentity.BaseTickTask.REPEAT_MODEL_BACKOFF;
import static com.ryg.libtick.taskentity.BaseTickTask.REPEAT_MODEL_CYCLE;


/**
 * 秒为单位的倒计时
 *
 * @author ruanyaguang
 * @data 2019/4/16.
 */
public class SecondSchedule implements TimerScheduleImpl {


    private static final String TAG = SecondSchedule.class.getName();

    private Map<Integer, Node> tickTaskMap = new LinkedHashMap<>();

    int countdown = 0;
    private EasyThread executor = EasyThread.getInstance();

    private Timer timer = new Timer();
    private TimerTask timerTask;

    Handler heart_handler = new Handler();
    Runnable heart_runnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "countdown : " + countdown);
            checkTask();
            countdown += 1;
            heart_handler.postDelayed(heart_runnable, 1000);
        }
    };


    public SecondSchedule() {
        init();
    }

    private void init() {

//        if (null != timerTask) {
//            timerTask.cancel();
//            timer.schedule(timerTask, 0, 1000);
//            return;
//        }
//
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//
//                Log.d(TAG, "TimeSchedule.getInstance: timerTask " + countdown);
////                LogUtils.d(TAG, "countdown : " + countdown);
//                checkTask();
//                countdown += 1;
//            }
//        };
//
//        timer.schedule(timerTask, 0, 1000);
        if (heart_handler != null && heart_runnable != null) {
            heart_handler.removeCallbacks(heart_runnable);
            heart_handler.postDelayed(heart_runnable, 1000);
        }


    }

    private synchronized void checkTask() {

        if (tickTaskMap.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<Integer, Node>> entries = tickTaskMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Node> entry = entries.next();
//            LogUtils.d(TAG, "Key = " + entry.getKey().toString() + ", Value = " + entry.getValue());

            Node node = entry.getValue();

            BaseTickTask tickTask = node.getBaseTickTask();


            /**
             * 常规倒计时
             */
            if (tickTask.getRepeatCount() == REPEAT_MODEL_CYCLE || tickTask.getRepeatCount() > 0) {

                if (tickTask.getCurTickTime() == 0) {//吉时到
                    doCallBackAction(entry, tickTask.getRepeatCount());

                    tickTask.setTickSuccessCount();

                    if (tickTask.getRepeatCount() == 0) {//不需要再重复倒计时,直接删除任务

                        tickTask.setCurTickTime(-1);//防止没有及时回收
//                        tickTaskMap.remove(entry.getKey());
                        entries.remove();

                    } else if (tickTask.getRepeatCount() > 0) {//还需要继续任务,那就继续,如果是-1,则永远继续

                        tickTask.setRepeatCount(tickTask.getRepeatCount() - 1);
                        tickTask.resetCurTickTime();

                    } else if (tickTask.getRepeatCount() == REPEAT_MODEL_CYCLE) {

                        tickTask.resetCurTickTime();
                    }

                } else if (tickTask.getCurTickTime() > 0) {//吉时未到

                    tickTask.setCurTickTime(tickTask.getCurTickTime() - 1);

                } else {
                    tickTask.setCurTickTime(-1);
                }
            }
            /**
             * 指数避退方案
             */
            else if (tickTask.getRepeatCount() == REPEAT_MODEL_BACKOFF) {

                if (tickTask.getCurTickTime() == 0) {//吉时到

                    doCallBackAction(entry, 0);
                    tickTask.setCurTickTime(-100);//此时是等待任务执行

                } else if (tickTask.getCurTickTime() > 0) {//吉时未到

                    tickTask.setCurTickTime(tickTask.getCurTickTime() - 1);

                } else {
                }

            } else {
                entries.remove();
//                LogUtils.d(TAG, "TASKID : " + tickTask.getTickID() + " TASKREPEAT_TIME : " + tickTask.getRepeatCount() + " 删除任务 剩余任务数量 checkTask : " + tickTaskMap.size());
            }


        }
    }


    /**
     * 吉时到,开始回调
     *
     * @param entry
     */
    private void doCallBackAction(Map.Entry<Integer, Node> entry, final int curTime) {
        final BaseCallBackListener callBackListener = entry.getValue().getBaseCallBackListener();

        if (null == callBackListener) {
            return;
        }

        if (callBackListener instanceof MainThreadCallBackListener) {

            executor.setName("Handle task")
                    .async(new EmptyCallable(), new AsyncCallback<String>() {
                        @Override
                        public void onSuccess(String s) {

                            callBackListener.timeUp(curTime - 1);//此时就是UI线程了
                        }

                        @Override
                        public void onFailed(Throwable t) {
                            //永远不会Failed,因为EmptyCallable什么都没做
                        }
                    });

        } else if (callBackListener instanceof ThreadCallBackListener) {

            executor.setName("Runnable task")
                    .execute(new Runnable() {
                        @Override
                        public void run() {
                            callBackListener.timeUp(curTime - 1);//此时就是普通线程了
                        }
                    });
        }
    }


    /**
     * 继续避退算法
     *
     * @param tickID
     */
    public synchronized void continueBackOff(int tickID) {

        Iterator<Map.Entry<Integer, Node>> entries = tickTaskMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Node> entry = entries.next();

            Node node = entry.getValue();
            BaseTickTask tickTask = node.getBaseTickTask();

            if (tickTask instanceof BackOffTickTask && tickTask.getTickID() == tickID) {

                BackOffTickTask backOffTickTask = (BackOffTickTask) tickTask;


                if (backOffTickTask.refreshCurTickTime()) {
                    //继续倒计时
                    Log.d("continueBackOff", "backOffTickTaskID : " + backOffTickTask.getTickID() + " getCurTickTime : " + backOffTickTask.getCurTickTime() + "");

                } else {

                    Log.d("continueBackOff", "达到最大时间限度");
                    removeTask(tickID);
                }
            }
        }
    }

    public void stopTick() {
//        if (timer != null) {
//            timer.cancel();
//            timer.purge();
//            timer = null;
//        }
//
//        if (timerTask != null) {
//            timerTask.cancel();
//            timerTask = null;
//        }


        if (heart_handler != null && heart_runnable != null) {
            heart_handler.removeCallbacks(heart_runnable);
        }

        removeAllTask();


    }


    @Override
    public synchronized void addTickTask(BaseTickTask task, BaseCallBackListener listener) {

        Node node = new Node(task, listener);
        tickTaskMap.put(task.getTickID(), node);
    }


    @Override
    public synchronized void removeTask(int tickID) {

        Iterator<Map.Entry<Integer, Node>> entries = tickTaskMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Node> entry = entries.next();

            Node node = entry.getValue();

            BaseTickTask tickTask = node.getBaseTickTask();

            if (tickTask.getTickID() == tickID) {

                entries.remove();
//                LogUtils.d(TAG, "TASKID : " + tickTask.getTickID() + " TASKREPEAT_TIME : " + tickTask.getRepeatCount() + " 删除任务 剩余任务数量 removeTask : " + tickTaskMap.size());
                break;
            }
        }


    }

    @Override
    public void removeAllTask() {
        tickTaskMap.clear();
    }


    private class EmptyCallable implements Runnable, Callable<String> {
        @Override
        public void run() {
        }

        @Override
        public String call() throws Exception {
            return null;
        }
    }


    public Map<Integer, Node> getTickTaskMap() {
        return tickTaskMap;
    }


    public class Node {
        BaseTickTask baseTickTask;
        BaseCallBackListener baseCallBackListener;

        public Node(BaseTickTask baseTickTask, BaseCallBackListener baseCallBackListener) {
            this.baseTickTask = baseTickTask;
            this.baseCallBackListener = baseCallBackListener;
        }

        public BaseTickTask getBaseTickTask() {
            return baseTickTask;
        }


        public BaseCallBackListener getBaseCallBackListener() {
            return baseCallBackListener;
        }

    }
}
