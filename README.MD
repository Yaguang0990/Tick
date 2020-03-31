# Tick Android全局计时器，指数避退计时器

**使用步骤：**
1. 创建计时器任务Task
2. 设置计时器参数
3. 开始倒计时

开启倒计时

`‌
SecTickTask secTickTask = new SecTickTask();
        secTickTask.setTickID(124);//设置ID
        secTickTask.setIntervalsTime(2);
        secTickTask.setRepeatCount(5);//设置重复次数REPEAT_MODEL_CYCLE 为永远
  TimeSchedule.getInstance().addTickTask(secTickTask, new MainThreadCallBackListener() {
            @Override
            public void timeUp(int curRepeatTime) {
                textView.setText(curRepeatTime + "");
            }
        });        
`    
