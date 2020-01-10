package com.github.lamba92.ktor.features

import java.io.File

fun String.notContains(regex: Regex) = !contains(regex)
fun File.notExists() = !exists()