package com.ryg.tick;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.ryg.libtick.TimeSchedule;
import com.ryg.libtick.listener.MainThreadCallBackListener;
import com.ryg.libtick.listener.ThreadCallBackListener;
import com.ryg.libtick.taskentity.BackOffTickTask;
import com.ryg.libtick.taskentity.SecTickTask;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        openNormalTick();
    }

    private void openNormalTick() {
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
                //，在飞主线程，此时会报错
                //textView.setText(curRepeatTime + "");
            }

        });

        TimeSchedule.getInstance().addTickTask(secTickTask, new MainThreadCallBackListener() {
            @Override
            public void timeUp(int curRepeatTime) {

                textView.setText(curRepeatTime + "");

            }
        });
    }

    /**
     * 添加指数规避定时任务
     * MainThreadCallBackListener为主线程回调
     * ThreadCallBackListener为异步回调
     **/
    private void openBackOffTickTask() {

        final BackOffTickTask backOffTickTask = new BackOffTickTask();
        backOffTickTask.setTickID(124);
        backOffTickTask.setIntervalsTime(2);//第一次倒计时延迟时间
        backOffTickTask.setInitialBackoffMillis(2);//倒计时的指数
        backOffTickTask.setMax_backoff_second(30);//设置最大时间间隔,超过这个间隔,任务停止


        TimeSchedule.getInstance().addTickTask(backOffTickTask, new ThreadCallBackListener() {
            @Override
            public void timeUp(int curRepeatTime) {

                TimeSchedule.getInstance().continueTask(backOffTickTask.getTickID());//增加指数时间，继续倒计时
                // TimeSchedule.getInstance().removeTask(backOffTickTask.getTickID());//任务成功要调用这个,结束倒计时

            }
        });
    }
}
