package cn.jsbintask.memo.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.view.WindowManager;

import cn.jsbintask.memo.MemoApplication;

import static android.content.Context.KEYGUARD_SERVICE;



public class WakeLockUtil {

    public static void wakeUpAndUnlock() {

        PowerManager pm = (PowerManager) MemoApplication.getContext()
                .getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        boolean screenOn = pm.isInteractive();
        if (!screenOn) {

            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, "bright");
            wl.acquire(10000);
            wl.release();
        }

        KeyguardManager keyguardManager = (KeyguardManager) MemoApplication.getContext()
                .getSystemService(KEYGUARD_SERVICE);
        assert keyguardManager != null;
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");

        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard();
    }
}
