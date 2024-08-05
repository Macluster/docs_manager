package com.example.docs_manager.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.docs_manager.FileItem
import com.example.docs_manager.R
import com.example.docs_manager.ViewModels.MyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupPage(viewModel: MyViewModel,navController: NavController)
{
    var files=viewModel.selectedFiles
    var isUploading by remember {
        mutableStateOf(false)
    }
    val progress by viewModel.progress.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text(text = "BackUp")}, actions = {}) }) { pad->
        
        Column(modifier = Modifier
            .padding(pad)
            .padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Files to be backed up are listed below", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp))
                Spacer(modifier = Modifier.width(5.dp))
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .widthIn(min = 30.dp, max = 130.dp)
                    .height(30.dp)
                    .background(Color.Red)) {
                    Text(text = progress.toString(), style = TextStyle(color = Color.White))
                    
                }
                Text(text = "Upload", style = TextStyle(Color.White))
//                Image(
//                    painterResource(id = R.drawable.backupblue), contentDescription ="", modifier = Modifier
//                        .height(40.dp)
//                        .width(40.dp)
//                        .clickable {
//                            CoroutineScope(Dispatchers.IO).launch {
//                                viewModel.uploadFile()
//                                // Process the response
//                            }
//                        } )
            }


      

            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(files.size) { file ->
                    FileItem(file = files[file], onClick = {
                        if (files[file].isDirectory) {

                            viewModel.currentDir.value = files[file]
                            viewModel.root.add(files[file])
                            navController.navigate("homePage")
                        }
                        else if(files[file].extension=="png"||files[file].extension=="jpg"||files[file].extension=="jpeg")
                        {
                            viewModel.currentFile.value=files[file]
                            navController.navigate("showImage")
                        }
                        else if(files[file].extension=="mp4"||files[file].extension=="mkv")
                        {
                            viewModel.currentFile.value=files[file]
                            navController.navigate("showVideo")
                        }
                    },viewModel)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .height(60.dp)
                .fillMaxWidth()
                .background(if (isUploading) Color.Blue else Color.DarkGray)
                .clickable {
                    isUploading = true;
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.uploadFile()
                        // Process the response
                    }
                }) {
                if(isUploading)
                    Text(text = "Uploaded ${viewModel.filesToUpload.size-progress}/${viewModel.filesToUpload.size}", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold))
                else
                     Text(text = "Upload", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp))
            }
        }

    }
}