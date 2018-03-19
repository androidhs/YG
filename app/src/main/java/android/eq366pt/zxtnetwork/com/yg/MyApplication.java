package android.eq366pt.zxtnetwork.com.yg;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.zxy.recovery.callback.RecoveryCallback;
import com.zxy.recovery.core.Recovery;

/**
 * Created by lbk on 2018/3/18.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Recovery.getInstance()
                .debug(true)
                .recoverInBackground(false)
                .recoverStack(true)
                .mainPage(MainActivity.class)
                .recoverEnabled(true)
                .callback(new RecoveryCallback() {
                    @Override
                    public void stackTrace(String stackTrace) {

                    }

                    @Override
                    public void cause(String cause) {

                    }

                    @Override
                    public void exception(String throwExceptionType, String throwClassName, String throwMethodName, int throwLineNumber) {

                    }

                    @Override
                    public void throwable(Throwable throwable) {

                    }
                })
                .silent(false, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
                //                .skip(TestActivity.class)
                .init(this);
        // 如果返回值为 null，则全部使用默认参数。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }


    }
}
