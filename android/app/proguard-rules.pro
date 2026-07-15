-keepclassmembers class com.indiacloud.panel.** {
    @android.webkit.JavascriptInterface <methods>;
}

-keep public class android.webkit.**
-keep public class android.net.http.**
-keep public class com.android.internal.http.HttpDateTime
-keep public class android.webkit.HttpAuthHandler

-keep class com.indiacloud.panel.** { *; }
-keep class androidx.** { *; }

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}