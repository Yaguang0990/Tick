

   ##开启普通倒计时
     SecTickTask secTickTask = new SecTickTask();
     secTickTask.setTickID(124 + i);//设置ID
     secTickTask.setIntervalsTime(2 + i);
     secTickTask.setRepeatCount(5);//设置重复次数 REPEAT_MODEL_CYCLE 为永远

     /**
     * 添加定时任务
     * ThreadCallBackListener为异步回调
     **/
     TimeSchedule.getInstance().addTickTask(secTickTask, new ThreadCallBackListener() {
         @Override
         public void timeUp() {

           LogUtil.d("MainThreadCallBackListener", secTickTask.toString());

         }
      });


   /**
      * 添加定时任务
      * MainThreadCallBackListener为主线程回调
      **/
       TimeSchedule.getInstance().addTickTask(secTickTask, new MainThreadCallBackListener() {
               @Override
               public void timeUp() {

                 LogUtil.d("MainThreadCallBackListener", secTickTask.toString());

              }
      });



      final BackOffTickTask backOffTickTask = new BackOffTickTask();
                  backOffTickTask.setTickID(124 + i);
                  backOffTickTask.setIntervalsTime(2 + i);//第一次倒计时延迟时间
                  backOffTickTask.setMax_backoff_second(30);//设置最大时间间隔,超过这个间隔,任务停止


                  TimeSchedule.getInstance().addTickTask(backOffTickTask, new ThreadCallBackListener() {
                      @Override
                      public void timeUp() {

                          TimeSchedule.getInstance().continueTask(backOffTickTask.getTickID());//任务失败要调用这个,继续倒计时
                          // TimeSchedule.getInstance().removeTask(backOffTickTask.getTickID());//任务成功要调用这个,结束倒计时
                          LogUtil.d("MainThreadCallBackListener", backOffTickTask.toString());

                      }
                  });