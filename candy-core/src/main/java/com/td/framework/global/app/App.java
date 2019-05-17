package com.td.framework.global.app;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.td.framework.utils.L;

/**
 * Created by jc on 2016/12/23 0023.
 * <p>Gihub  </p>
 * <p>应用程序</p>
 */

public class App extends MultiDexApplication {
    protected AppManager mAppManager;
    private static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        initNetConfig();
    }

    /**
     * 初始化网络配置
     * <p>在子类中进行重写，配置网络相关</p>
     * <p>可以在其他地方重写调用方法重新进行网络相关的配置</p>
     */
    public void initNetConfig() {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        mAppManager = AppManager.getAppManager();
        //CrashHandler.getInstance().init(this);
        mContext = this;
        //友盟统计
//        MobclickAgent.setCatchUncaughtExceptions(!L.isDebug);
//        MobclickAgent.setDebugMode(L.isDebug);
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        if (L.isDebug) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

    public static Context newInstance() {

        return mContext;
    }
}
