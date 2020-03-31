package com.ryg.libtick.taskentity;


/**
 * 以秒为单位倒计时任务栈
 * <p>
 * 算法参考链接
 * https://www.yiibai.com/java/lang/math_scalb_float.html
 *
 * @author ruanyaguang
 * @data 2019/4/16.
 */
public class BackOffTickTask extends BaseTickTask {


    public static final int MIN_BACKOFF_SECOND = 1;

    /**
     * 默认最大避退数字是一小时(秒为单位)
     */
    public int max_backoff_second = 1 * 60 * 60;

    /**
     * 错误次数
     */
    private int numFailures;

    /**
     * 初始指数基数
     */
    private int initialBackoffMillis = MIN_BACKOFF_SECOND;

    /**
     * 避退次数
     */
    private int backoffAttempts = numFailures + 1;

    /**
     * 指数增长基数
     */
    private int backoff = initialBackoffMillis;


    public BackOffTickTask() {

        setRepeatCount(BackOffTickTask.REPEAT_MODEL_BACKOFF);
    }

    /**
     * 算法
     * <p>
     * backoff  *  ( 2的 (backoffAttempts - 1)次方)
     */
    public boolean refreshCurTickTime() {

        numFailures += 1;
        backoffAttempts = numFailures + 1;


        if (backoff < MIN_BACKOFF_SECOND) {
            backoff = MIN_BACKOFF_SECOND;
        }

        System.out.print("backoffAttempts : " + backoffAttempts + "\n");


        if ((int) Math.scalb(backoff, backoffAttempts - 1) >= max_backoff_second) {

            /**
             *达到最大指数时间,不再增长
             */
            return false;
        }

        setCurTickTime((int) Math.scalb(backoff, backoffAttempts - 1));

        return true;
    }


    public int getMax_backoff_second() {
        return max_backoff_second;
    }

    public void setMax_backoff_second(int max_backoff_second) {
        this.max_backoff_second = max_backoff_second;
    }

    public int getNumFailures() {
        return numFailures;
    }


    public void setNumFailures(int numFailures) {
        this.numFailures = numFailures;
    }

    public int getInitialBackoffMillis() {
        return initialBackoffMillis;
    }

    public void setInitialBackoffMillis(int initialBackoffMillis) {
        this.initialBackoffMillis = initialBackoffMillis;
    }

    public int getBackoffAttempts() {
        return backoffAttempts;
    }

    public void setBackoffAttempts(int backoffAttempts) {
        this.backoffAttempts = backoffAttempts;
    }

    public int getBackoff() {
        return backoff;
    }

    public void setBackoff(int backoff) {
        this.backoff = backoff;
    }

    @Override
    public String toString() {
        return "BackOffTickTask{" +
                "max_backoff_second=" + max_backoff_second +
                ", numFailures=" + numFailures +
                ", initialBackoffMillis=" + initialBackoffMillis +
                ", backoffAttempts=" + backoffAttempts +
                ", backoff=" + backoff +
                '}';
    }
}
