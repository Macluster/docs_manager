package com.example.docs_manager.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.docs_manager.Models.FileModel
import com.example.docs_manager.Models.FolderModel
import com.example.docs_manager.R
import com.example.docs_manager.ViewModels.MyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloudStoragePage(viewModel: MyViewModel,navController: NavController)
{

    fun onFileCardClick(modal:FileModel)
    {
        viewModel.currentCloudFile.value=modal;

        if(modal.file_ext.split(".")[1]=="mp4"||modal.file_ext.split(".")[1]=="mkv")
        {
            navController.navigate("showNetworkVideo")
        }
        else
        {
            navController.navigate("showNetwrokImage")
        }
    }


    BackHandler {
        // Set orientation to portrait

        // Pop the back stack
        if(viewModel.cloudRoot.size>1) {


            viewModel.cloudRoot.removeAt(viewModel.cloudRoot.size - 1)
            viewModel.currentCloudDir.value = viewModel.cloudRoot[viewModel.cloudRoot.size - 1]
            viewModel.getFolders(viewModel.currentCloudDir.value)
            viewModel.getFiles(viewModel.currentCloudDir.value)
        }
        else
        {

        }
    }




    Scaffold( topBar = { TopAppBar(title = { Text(text = "Cloud Storage")}, actions = {}) }) {pad->
        Column(modifier = Modifier
            .padding(pad)
            .padding(20.dp)) {
            var cloudFolders=viewModel.cloudStorageFolders;
            var cloudFiles=viewModel.cloudStorageFiles;
            LazyColumn() {
                items(cloudFolders.size)
                { index->

                    FolderCard(model = cloudFolders[index], onclick = {
                        println(cloudFolders[index]._id)
                        viewModel.cloudRoot.add(cloudFolders[index]._id)
                        viewModel.currentCloudDir.value=cloudFolders[index]._id;
                        viewModel.getFolders(viewModel.currentCloudDir.value);viewModel.getFiles(viewModel.currentCloudDir.value)})


                }

            }

            LazyColumn() {
                items(cloudFiles.size)
                { index->
                    FileCard(model = cloudFiles[index], onclick = {onFileCardClick(cloudFiles[index])})



                }

            }



        }
    }

}


@Composable
fun FileCard( model:FileModel,onclick:()->Unit)
{
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .clickable { onclick() }) {
            Image(painter = painterResource(id = R.drawable.image), contentDescription ="", modifier = Modifier
                .height(40.dp)
                .width(40.dp) )
            Spacer(modifier = Modifier.width(10.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())) {
                Text(text = model.file_name)
            }

        }
    }

}

@Composable
fun FolderCard( model:FolderModel,onclick:()->Unit)
{
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .clickable { onclick() }) {
            Image(painter = painterResource(id = R.drawable.folder), contentDescription ="", modifier = Modifier
                .height(40.dp)
                .width(40.dp) )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = model.folder_name)
        }
    }
   
}