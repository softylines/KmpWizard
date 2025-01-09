package com.softylines.kmpwizard.parser.libs

data class LibsFile(
    val versionsBlock: LibsBlock.Versions? = null,
    val librariesBlock: LibsBlock.Libraries? = null,
    val pluginsBlock: LibsBlock.Plugins? = null,
    val bundlesBlock: LibsBlock.Bundles? = null,
)