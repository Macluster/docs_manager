package com.example.docs_manager.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.docs_manager.Models.FileModel
import java.io.File


@Composable
fun ShowImage(currentFile: File)
{
    Scaffold(modifier = Modifier.fillMaxSize() .background(Color.Black)) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Black).padding(innerPadding), verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier.padding(10.dp)) {
                Text(text = currentFile.name, style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold))
            }

            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                AsyncImage(model = currentFile.path, contentDescription ="", contentScale = ContentScale.Crop )
            }
        }
    }



}

@Composable
fun ShowNetworkImage(modal:FileModel)
{
    Scaffold(modifier = Modifier.fillMaxSize() .background(Color.Black)) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Black).padding(innerPadding), verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier.padding(10.dp)) {
                Text(text = modal.file_name, style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold))
            }

            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                AsyncImage(model = modal.file_url, contentDescription ="", contentScale = ContentScale.Crop)
            }
        }
    }
}