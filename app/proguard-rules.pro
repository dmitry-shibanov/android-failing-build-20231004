# Newspaper data
-keep class nl.fd.newspaperreader.model.* { *; }

# ReadMore request
-keep class nl.fd.data.entity.article.ReadMoreRequest { *; }

# Retrofit library
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-keep class com.shockwave.**

# GSon
-keep class com.google.gson.Gson
-keep class com.google.gson.TypeAdapter
-keepattributes Signature
-keepattributes Exceptions
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep our own classes that we deserialize using GSON
-keepclassmembers class ** {
    @com.google.gson.annotations.Expose <fields>;
}
# And never obfuscate enum names. Especially the ones we use in GSON
-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# RxJava2 library
-dontwarn sun.misc.**

-keepclassmembers class io.reactivexinternal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class io.reactivexinternal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    io.reactivexinternal.util.atomic.LinkedQueueNode producerNode;
    io.reactivexinternal.util.atomic.LinkedQueueNode consumerNode;
}

# Parcel library
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }

# WebView JS callbacks
-keep class nl.fd.data.js.**

# FCM
-dontwarn com.google.android.gms.measurement.**

-keep class com.comscore.** { *; }
-dontwarn com.comscore.**

-keep class org.joda.time.** { *; }
-dontwarn org.joda.time.**

-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder

# Google Ads (Banner)
-keep public class com.google.android.gms.ads.**{
   public *;
}
-keep public class com.google.ads.**{
   public *;
}
-keep class * extends java.util.ListResourceBundle {
   protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
   public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
   @com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
   public static final ** CREATOR;
}


-keep class * extends android.content.ContentProvider { *; }

# Crashlytics
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

# Firebase
# Keep custom model classes
-keep class com.google.firebase.example.fireeats.model.** { *; }

# https://github.com/firebase/FirebaseUI-Android/issues/1175
-dontwarn okio.**
-dontwarn retrofit2.Call
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-keep class android.support.v7.widget.RecyclerView { *; }
-keep class androidx.recyclerview.widget.RecyclerView { *; }
-keep class com.google.firebase.provider.** { *; }

# Sourcepoint (CMP)
-keep interface com.sourcepoint.** { *; }
-keep class com.sourcepoint.** { *; }

# kotlinx
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations
# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.liveramp.mobilesdk.**$$serializer { *; }
-keepclassmembers class com.liveramp.mobilesdk.** {
    *** Companion;
}
-keepclasseswithmembers class com.liveramp.mobilesdk.** {
    kotlinx.serialization.KSerializer serializer(...);
}

#Twitter
#Okio
-dontwarn java.nio.file.**
-dontwarn org.codehaus.mojo.animal_sniffer.**

#Retrofit 2
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain class members for annotations
-keepclasseswithmembers class * {
  @retrofit2.http.* <methods>;
}
-keep class com.twitter.sdk.android.core.models.VideoInfo { *; }

# JWT Tokens
-keep class io.jsonwebtoken.** { *; }
-keepnames class io.jsonwebtoken.* { *; }
-keepnames interface io.jsonwebtoken.* { *; }

-keep class org.bouncycastle.** { *; }
-keepnames class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**
