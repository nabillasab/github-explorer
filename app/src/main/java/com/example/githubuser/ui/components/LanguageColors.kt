package com.example.githubuser.ui.components

import androidx.compose.ui.graphics.Color

object LanguageColors {

    val Ruby = Color(0xFFCC342D)
    val Python = Color(0xFF3776AB)
    val JavaScript = Color(0xFFD4A017)
    val TypeScript = Color(0xFF3178C6)
    val Java = Color(0xFFB07219)
    val Kotlin = Color(0xFFA97BFF)
    val Go = Color(0xFF00ADD8)
    val Rust = Color(0xFFDEA584)
    val Swift = Color(0xFFF05138)
    val PHP = Color(0xFF4F5D95)
    val CSharp = Color(0xFF178600)
    val CPlusPlus = Color(0xFFF34B7D)
    val Shell = Color(0xFF89E051)
    val HTML = Color(0xFFE34C26)
    val CSS = Color(0xFF563D7C)
    val Unknown = Color(0xFF8B949E)

    fun getColors(language: String): Color {
        return when (language.lowercase()) {
            "ruby" -> Ruby
            "python" -> Python
            "javascript", "js" -> JavaScript
            "typescript", "ts" -> TypeScript
            "java" -> Java
            "kotlin" -> Kotlin
            "go", "golang" -> Go
            "rust" -> Rust
            "swift" -> Swift
            "php" -> PHP
            "c#", "csharp" -> CSharp
            "c++", "cpp" -> CPlusPlus
            "shell", "bash" -> Shell
            "html" -> HTML
            "css" -> CSS
            else -> Unknown
        }
    }
}