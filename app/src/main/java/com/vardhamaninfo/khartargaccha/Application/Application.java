package com.vardhamaninfo.khartargaccha.Application;

import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;
import com.vardhamaninfo.khartargaccha.Retrofit.RestInterface;


/**
 * Created by Android on 6/29/2015.
 */
public class Application extends android.app.Application {

    static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        application = this;
        Fresco.initialize(this);

        // register to be informed of activities starting up
       /* registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {

                // new activity created; force its orientation to portrait
                activity.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }


        });*/

    }


    public static Application getAppInstance() {
        return application;
    }

    /**
     * get ApiHelper Component for this application,
     */
    private RestAdapter apiHelper;

    private synchronized RestAdapter getServerBackend() {
        if (apiHelper != null)
            return apiHelper;
        try {
            return (apiHelper = new RestAdapter());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("oopp", "Invalid Api Definitions");
            throw new RuntimeException("Invalid Api Definitions");
        }
    }


    public RestInterface getServerBackend(RestAdapter.AuthType type) {
        if (type.equals(RestAdapter.AuthType.AUTHARIZED))
            return getServerBackend().getAuthRestInterface();
            //   else (type.equals(RestAdapter.AuthType.UNAUTHARIZED))
        else
            return getServerBackend().getRestInterface();
    }

    public RestInterface getServerBackendYouTube(RestAdapter.AuthType type) {
        if (type.equals(RestAdapter.AuthType.AUTHARIZED))
            return getServerBackend().getAuthRestInterface();
            //   else (type.equals(RestAdapter.AuthType.UNAUTHARIZED))
        else
            return getServerBackend().getRestInterfaceYouTube();
    }


}
