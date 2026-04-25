package com.volokhinaleksey.kash.platform

import platform.Foundation.NSBundle

actual fun getAppVersion(): String =
    NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
        ?: ""
