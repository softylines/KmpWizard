package com.softylines.kmpwizard.core.libs

data class LibsFile(
    val versionsBlock: LibsBlock.Versions? = null,
    val librariesBlock: LibsBlock.Libraries? = null,
    val pluginsBlock: LibsBlock.Plugins? = null,
    val bundlesBlock: LibsBlock.Bundles? = null,
)