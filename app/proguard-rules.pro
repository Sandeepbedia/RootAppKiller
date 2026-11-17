# Add project specific ProGuard rules here.
# For more details, see
#   https://developer.android.com/studio/build/shrink-code
# libsu ships its own consumer rules, but keep this explicit since this
# app's only job is talking to a root shell through it.
-keep class com.topjohnwu.superuser.** { *; }
