package cn.jsbintask.memo.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import cn.jsbintask.memo.MemoApplication;

import java.util.Date;


public class ClockManager {
    private static ClockManager instance = new ClockManager();

    private ClockManager() {
    }

    public static ClockManager getInstance() {
        return instance;
    }


    private static AlarmManager getAlarmManager() {
        return (AlarmManager) MemoApplication.getContext().getSystemService(Context.ALARM_SERVICE);
    }


    public void cancelAlarm(PendingIntent pendingIntent) {
        getAlarmManager().cancel(pendingIntent);
    }


    public void addAlarm(PendingIntent pendingIntent, Date performTime) {
        cancelAlarm(pendingIntent);
        getAlarmManager().set(AlarmManager.RTC_WAKEUP, performTime.getTime(), pendingIntent);
    }
}
