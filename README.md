# CoreLibraryDesugeringCrashOnAndroidKitkat
This is a project which reproduces the crashes with core library desugering and Android devices lower Lollipop.

Every class which will be touched by core library desugering and is accessed inside the `onCreate` method of the `Application` leads to a `NoClassDefFoundError` on Android 4.4.4 devices.

I tried the following:

```
class CoreApplication : MultiDexApplication() {
    override fun attachBaseContext(base: Context?) {
        //adding the new GMS causes to many Methods in APK, therefore configure Application as MultiDex
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        Optional.of("TEst")
    }
}
```

Which will result in 

```
03-22 18:36:09.231 657-657/com.plauzeware.CoreLibraryDesugeringCrashOnAndroidKitkat E/AndroidRuntime: FATAL EXCEPTION: main
    Process: com.plauzeware.CoreLibraryDesugeringCrashOnAndroidKitkat, PID: 657
    java.lang.NoClassDefFoundError: j$.util.Optional
        at com.plauzeware.corelibrarydesugeringcrashonandroidkitkat.CoreApplication.onCreate(Application.kt:17)
        at android.app.Instrumentation.callApplicationOnCreate(Instrumentation.java:1030)
        at android.app.ActivityThread.handleBindApplication(ActivityThread.java:4409)
        at android.app.ActivityThread.access$1500(ActivityThread.java:139)
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1270)
        at android.os.Handler.dispatchMessage(Handler.java:102)
        at android.os.Looper.loop(Looper.java:136)
        at android.app.ActivityThread.main(ActivityThread.java:5086)
        at java.lang.reflect.Method.invokeNative(Native Method)
        at java.lang.reflect.Method.invoke(Method.java:515)
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:785)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:601)
        at dalvik.system.NativeStart.main(Native Method)
```

But when I wrap the call to Optional inside a helper class 

```
class Loader {
    companion object {
        fun load() {
            Optional.of("TEst")
        }
    }
}
```

and call it inside my onCreate function

```
class CoreApplication : MultiDexApplication() {
    override fun attachBaseContext(base: Context?) {
        //adding the new GMS causes to many Methods in APK, therefore configure Application as MultiDex
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        Loader.load()
    }
}
```

it won't crash.
