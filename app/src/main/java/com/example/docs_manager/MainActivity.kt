package com.example.docs_manager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.drawable.Icon
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.docs_manager.Screens.BackupPage
import com.example.docs_manager.Screens.CloudStoragePage
import com.example.docs_manager.Screens.ShowImage
import com.example.docs_manager.Screens.ShowNetorkVideo
import com.example.docs_manager.Screens.ShowNetworkImage
import com.example.docs_manager.Screens.ShowVideo
import com.example.docs_manager.ViewModels.MyViewModel
import com.example.docs_manager.ViewModels.NetworkViewModel
import com.example.docs_manager.ui.theme.Docs_managerTheme
import java.io.File


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Docs_managerTheme {
                var navController= rememberNavController()

                val viewModel = viewModel<MyViewModel>()
//                val networkViewModel = viewModel<NetworkViewModel>(LocalContext.current)
//                val isConnected by networkViewModel.isConnected


                LaunchedEffect(key1 = 1) {
                    viewModel.getFolders("0");
                    viewModel.getFiles("0");
                }




                var currentScreen by remember {
                    mutableStateOf(0)
                }


                Scaffold(bottomBar = {
                    BottomAppBar {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 50.dp, end = 50.dp, top = 10.dp, bottom = 10.dp)) {

                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier
                                .clip(
                                    RoundedCornerShape(20.dp)
                                )
                                .width(40.dp)
                                .height(40.dp)
                                .background(if (currentScreen == 0) Color(0xFF6EACDA) else Color.Transparent)
                                .clickable { currentScreen = 0;navController.navigate("homePage") }) {
                                Image(painter = painterResource(id = R.drawable.folderblack), contentDescription ="" , modifier = Modifier
                                    .height(25.dp)
                                    .width(25.dp))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier
                                .clip(
                                    RoundedCornerShape(20.dp)
                                )
                                .width(40.dp)
                                .height(40.dp)
                                .background(if (currentScreen == 1) Color(0xFF6EACDA) else Color.Transparent)
                                .clickable {
                                    currentScreen = 1;navController.navigate("cloudStorage")
                                }) {
                                Image(painter = painterResource(id = R.drawable.cloudblackk), contentDescription ="" , modifier = Modifier
                                    .height(25.dp)
                                    .width(25.dp))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier
                                .clip(
                                    RoundedCornerShape(20.dp)
                                )
                                .width(40.dp)
                                .height(40.dp)
                                .background(if (currentScreen == 2) Color(0xFF6EACDA) else Color.Transparent)
                                .clickable {
                                    currentScreen =
                                        2;navController.navigate("backupPage");viewModel.addToFilesToBeUploaded()
                                }) {
                                Image(painter = painterResource(id = R.drawable.backupcylinder), contentDescription ="" , modifier = Modifier
                                    .height(30.dp)
                                    .width(30.dp))
                            }

                        }
                    }
                }) { pad->
                    NavHost(navController = navController, startDestination = "homePage", modifier = Modifier.padding(pad))
                    {
                        composable("homePage") { FileManagerScreen(navController,viewModel)}
                        composable("cloudStorage") { CloudStoragePage(viewModel,navController) }
                        composable("showImage") { ShowImage(viewModel.currentFile.value) }
                        composable("showNetwrokImage") { ShowNetworkImage(viewModel.currentCloudFile.value) }
                        composable("showVideo") { ShowVideo(viewModel.currentFile.value, LocalContext.current) }
                        composable("showNetworkVideo") { ShowNetorkVideo(viewModel.currentCloudFile.value, LocalContext.current) }
                        composable("backupPage") { BackupPage(viewModel,navController) }
                    }
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileManagerScreen(navController:NavController,viewModel: MyViewModel) {



    BackHandler {
        // Set orientation to portrait

        // Pop the back stack
        navController.popBackStack()
        viewModel.root.removeAt(viewModel.root.size-1)
        viewModel.currentDir.value=viewModel.root[viewModel.root.size-1]
    }
    val files = viewModel.root[viewModel.root.size-1].listFiles() ?: arrayOf()
    Scaffold(

        topBar = {
            TopAppBar(

                title = { Text(if(viewModel.currentDir.value.name=="0")"MyDocs" else viewModel.currentDir.value.name, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp)) },

            )
        },
        modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(5.dp)) {
//            Text(text = " ${currentDir.absolutePath}", style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
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

            Spacer(modifier = Modifier.height(8.dp))

//            Button(onClick = {
//                // Handle file creation or other actions
//            }) {
//                Text("Create File")
//            }
        }
    }

}


@Composable
fun FileItem(file: File, onClick: () -> Unit,viewModel: MyViewModel) {
    Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)
        .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            if(file.isDirectory)
                Image(painterResource(id = R.drawable.folder), contentDescription ="", modifier = Modifier
                    .height(40.dp)
                    .width(40.dp) )
            else  if(file.extension=="jpg"||file.extension=="png"||file.extension=="jpeg"||file.extension=="HEIC")
                Image(painterResource(id = R.drawable.image), contentDescription ="", modifier = Modifier
                    .height(40.dp)
                    .width(40.dp) )
            else  if(file.extension=="mp4"||file.extension=="mkv")
                Image(painterResource(id = R.drawable.youtube), contentDescription ="", modifier = Modifier
                    .height(40.dp)
                    .width(40.dp) )
            else  if(file.extension=="pdf")
                Image(painterResource(id = R.drawable.pdf), contentDescription ="", modifier = Modifier
                    .height(40.dp)
                    .width(40.dp) )
            else
                Image(painterResource(id = R.drawable.file), contentDescription ="", modifier = Modifier
                    .height(40.dp)
                    .width(40.dp) )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = file.name, style = MaterialTheme.typography.bodyLarge)
        }

        if(viewModel.selectedFiles.contains(file))
        Image(painterResource(id = R.drawable.accept), contentDescription ="", modifier = Modifier
            .height(30.dp)
            .width(30.dp)
            .clickable { viewModel.selectedFiles.remove(file) } )
        else
        Image(painterResource(id = R.drawable.circle), contentDescription ="", modifier = Modifier
            .height(30.dp)
            .width(30.dp)
            .clickable { viewModel.selectedFiles.add(file) } )

    }
}


