package com.volokhinaleksey.kash.platform

import com.volokhinaleksey.kash.BuildConfig

actual fun getAppVersion(): String = BuildConfig.VERSION_NAME
