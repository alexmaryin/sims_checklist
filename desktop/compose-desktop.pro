-keepclasseswithmembers public class MainKt {  # <-- Change com.company to yours
    public static void main(java.lang.String[]);
}

-dontwarn kotlinx.coroutines.debug.**

-dontwarn ch.qos.logback.**

-dontwarn io.ktor.**

-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class org.jetbrains.skia.** { *; }
-keep class org.jetbrains.skiko.** { *; }

-assumenosideeffects public class androidx.compose.runtime.ComposerKt {
    void sourceInformation(androidx.compose.runtime.Composer,java.lang.String);
    void sourceInformationMarkerStart(androidx.compose.runtime.Composer,int,java.lang.String);
    void sourceInformationMarkerEnd(androidx.compose.runtime.Composer);
}

# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations
-dontnote kotlinx.serialization.SerializationKt

# Keep Serializers

-keep,includedescriptorclasses class com.company.package.**$$serializer { *; }  # <-- Change com.company.package
-keepclassmembers class com.company.package.** {  # <-- Change com.company.package to yours
    *** Companion;
}
-keepclasseswithmembers class com.company.package.** { # <-- Change com.company.package to yours
    kotlinx.serialization.KSerializer serializer(...);
}

# When kotlinx.serialization.json.JsonObjectSerializer occurs

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt and other security providers are available.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
#################################### SLF4J #####################################
-dontwarn org.slf4j.**

# Prevent runtime crashes from use of class.java.getName()
-dontwarn javax.naming.**

# Ignore warnings and Don't obfuscate for now
-dontobfuscate
-ignorewarnings

## Keep Companion classes and class.Companion member of all classes that can be used in our API to
#  allow calling realmObjectCompanionOrThrow and realmObjectCompanionOrNull on the classes
-keep class io.realm.kotlin.types.ObjectId$Companion
-keepclassmembers class io.realm.kotlin.types.ObjectId {
    io.realm.kotlin.types.ObjectId$Companion Companion;
}
-keep class io.realm.kotlin.types.RealmInstant$Companion
-keepclassmembers class io.realm.kotlin.types.RealmInstant {
    io.realm.kotlin.types.RealmInstant$Companion Companion;
}
-keep class org.mongodb.kbson.BsonObjectId$Companion
-keepclassmembers class org.mongodb.kbson.BsonObjectId {
    org.mongodb.kbson.BsonObjectId$Companion Companion;
}
-keep class io.realm.kotlin.dynamic.DynamicRealmObject$Companion, io.realm.kotlin.dynamic.DynamicMutableRealmObject$Companion
-keepclassmembers class io.realm.kotlin.dynamic.DynamicRealmObject, io.realm.kotlin.dynamic.DynamicMutableRealmObject {
    **$Companion Companion;
}
-keep,allowobfuscation class ** implements io.realm.kotlin.types.BaseRealmObject
-keep class ** implements io.realm.kotlin.internal.RealmObjectCompanion
-keepclassmembers class ** implements io.realm.kotlin.types.BaseRealmObject {
    **$Companion Companion;
}

## Preserve all native method names and the names of their classes.
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

## Preserve all classes that are looked up from native code
# Notification callback
-keep class io.realm.kotlin.internal.interop.NotificationCallback {
    *;
}
# Utils to convert core errors into Kotlin exceptions
-keep class io.realm.kotlin.internal.interop.CoreErrorConverter {
    *;
}
-keep class io.realm.kotlin.internal.interop.JVMScheduler {
    *;
}
# Interop, sync-specific classes
-keep class io.realm.kotlin.internal.interop.sync.NetworkTransport {
    # TODO OPTIMIZE Only keep actually required symbols
    *;
}
-keep class io.realm.kotlin.internal.interop.sync.Response {
    # TODO OPTIMIZE Only keep actually required symbols
    *;
}
-keep class io.realm.kotlin.internal.interop.LongPointerWrapper {
    # TODO OPTIMIZE Only keep actually required symbols
    *;
}
-keep class io.realm.kotlin.internal.interop.sync.AppError {
    # TODO OPTIMIZE Only keep actually required symbols
    *;
}
-keep class io.realm.kotlin.internal.interop.sync.SyncError {
    # TODO OPTIMIZE Only keep actually required symbols
    *;
}
-keep class io.realm.kotlin.internal.interop.SyncLogCallback {
    # TODO OPTIMIZE Only keep actually required symbols
    *;
}
-keep class io.realm.kotlin.internal.interop.SyncErrorCallback {
    # TODO OPTIMIZE Only keep actually required symbols
    *;
}
-keep class io.realm.kotlin.Deleteable {
    *;
}
-keep class io.realm.kotlin.internal.interop.sync.JVMSyncSessionTransferCompletionCallback {
    *;
}
-keep class io.realm.kotlin.internal.interop.sync.ResponseCallback {
    *;
}
-keep class io.realm.kotlin.internal.interop.sync.ResponseCallbackImpl {
    *;
}
-keep class io.realm.kotlin.internal.interop.AppCallback {
    *;
}
-keep class io.realm.kotlin.internal.interop.CompactOnLaunchCallback {
    *;
}
-keep class io.realm.kotlin.internal.interop.MigrationCallback {
    *;
}
-keep class io.realm.kotlin.internal.interop.DataInitializationCallback {
    *;
}
-keep class io.realm.kotlin.internal.interop.SubscriptionSetCallback {
    *;
}
-keep class io.realm.kotlin.internal.interop.SyncBeforeClientResetHandler {
    *;
}
-keep class io.realm.kotlin.internal.interop.SyncAfterClientResetHandler {
    *;
}
-keep class io.realm.kotlin.internal.interop.AsyncOpenCallback {
    *;
}
-keep class io.realm.kotlin.internal.interop.NativePointer {
    *;
}
-keep class io.realm.kotlin.internal.interop.ProgressCallback {
    *;
}
-keep class io.realm.kotlin.internal.interop.sync.ApiKeyWrapper {
    *;
}
-keep class io.realm.kotlin.internal.interop.ConnectionStateChangeCallback {
    *;
}
-keep class io.realm.kotlin.internal.interop.SyncThreadObserver {
    *;
}

# Preserve Function<X> methods as they back various functional interfaces called from JNI
-keep class kotlin.jvm.functions.Function* {
    *;
}
-keep class kotlin.Unit {
    *;
}

# Un-comment for debugging
#-printconfiguration /tmp/full-r8-config.txt
#-keepattributes LineNumberTable,SourceFile
#-printusage /tmp/removed_entries.txt
#-printseeds /tmp/kept_entries.txt