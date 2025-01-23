package com.softylines.kmpwizard.template

fun buildGradleFileTemplate(): String {
    return """
        plugins {
            alias(libs.plugins.kotlinMultiplatform)
        }
        
        kotlin {
            jvm()
            
            sourceSets.commonMain.dependencies {
                // Dependencies
            }
        }
    """.trimIndent()
}