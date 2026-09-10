# AdMob / UMP
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.ump.** { *; }
-dontwarn com.google.android.gms.ads.**
-dontwarn com.google.android.ump.**

# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Play In-App Update / Review
-keep class com.google.android.play.core.** { *; }
-dontwarn com.google.android.play.core.**
