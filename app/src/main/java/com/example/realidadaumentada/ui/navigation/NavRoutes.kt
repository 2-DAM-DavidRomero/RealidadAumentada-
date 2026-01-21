package com.example.realidadaumentada.ui.navigation

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
object HomeScreen

@Serializable
data class ARScreen(val model: String)

@Serializable
object AlphabetScreen

@Serializable
object QuizScreen