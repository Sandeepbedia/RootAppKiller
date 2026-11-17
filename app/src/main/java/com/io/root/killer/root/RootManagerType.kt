package com.io.root.killer.root

/**
 * Which root solution granted (or could grant) access on this device.
 * The app behaves identically once root is granted — this is only used
 * to label the status row and to tell [RootManager] which package name
 * is the root manager itself, so it never ends up in a kill list.
 */
enum class RootManagerType(val label: String, val managerPackage: String?) {
    MAGISK("Magisk", "com.topjohnwu.magisk"),
    KERNEL_SU("KernelSU", "me.weishu.kernelsu"),
    APATCH("APatch", "me.bmax.apatch"),
    UNKNOWN_ROOT("Root (unknown manager)", null),
    NONE("Not rooted", null)
}

/** Result of a root-access probe. */
data class RootState(
    val granted: Boolean,
    val manager: RootManagerType,
    val message: String
)
