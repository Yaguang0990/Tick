# Tick 全局计时器，指数避退计时器（Android）

**引用方法**

方法一：引用 libtick module 到项目

方法二：直接复制 libtick的代码到项目

**使用步骤：**

1. 创建计时器任务Task
2. 设置计时器参数
3. 开始倒计时

**开启倒计时**

```
SecTickTask secTickTask = new SecTickTask();
secTickTask.setTickID(124);//设置ID
secTickTask.setIntervalsTime(2);
secTickTask.setRepeatCount(5);//设置重复次数 REPEAT_MODEL_CYCLE 为永远

/**
 * 添加定时任务
 * ThreadCallBackListener为异步回调
 **/
TimeSchedule.getInstance().addTickTask(secTickTask, new ThreadCallBackListener() {
    @Override
    public void timeUp(int curRepeatTime) {
        //在非主线程，此时会报错
        //textView.setText(curRepeatTime + "");
    }

});

/**
 * 添加定时任务
 * MainThreadCallBackListener为主线程回调
 **/
TimeSchedule.getInstance().addTickTask(secTickTask, new MainThreadCallBackListener() {
    @Override
    public void timeUp(int curRepeatTime) {

        textView.setText(curRepeatTime + "");

    }
});
```

**指数避退计时器**
```
BackOffTickTask backOffTickTask = new BackOffTickTask();
backOffTickTask.setTickID(124);
backOffTickTask.setIntervalsTime(2);//第一次倒计时延迟时间
backOffTickTask.setInitialBackoffMillis(2);//倒计时的指数
backOffTickTask.setMax_backoff_second(30);//设置最大时间间隔,超过这个间隔,任务停止


TimeSchedule.getInstance().addTickTask(backOffTickTask, new ThreadCallBackListener() {
    @Override
    public void timeUp(int curRepeatTime) {


    }
});

TimeSchedule.getInstance().continueTask(backOffTickTask.getTickID());//增加指数时间，继续倒计时
//TimeSchedule.getInstance().removeTask(backOffTickTask.getTickID());//任务成功要调用这个,结束倒计时

```

**移除倒计时任务**
```
TimeSchedule.getInstance().removeAllTask();//移除所有任务，在退出程序的时候必须调用，

TimeSchedule.getInstance().removeTask(id);//根据TaskID移除任务

```
