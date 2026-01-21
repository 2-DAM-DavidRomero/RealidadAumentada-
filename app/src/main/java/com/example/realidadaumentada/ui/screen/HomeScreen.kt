package com.example.realidadaumentada.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.navigation.NavController
import com.example.realidadaumentada.ui.navigation.AlphabetScreen
import com.example.realidadaumentada.ui.navigation.HomeScreen
import com.example.realidadaumentada.ui.navigation.QuizScreen

@Composable
fun HomeScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {navController.navigate(AlphabetScreen) }
        ){
            Text(text = "Alphabets")
        }
        Button(
            onClick = { navController.navigate(QuizScreen) }
        ){
            Text(text = "Quiz")
        }
    }

}