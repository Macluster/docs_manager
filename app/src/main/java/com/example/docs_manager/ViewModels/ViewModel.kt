package com.example.docs_manager.ViewModels

import android.os.Environment
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docs_manager.Models.FileModel
import com.example.docs_manager.Models.FolderModel
import com.example.docs_manager.Retrofit.ApiService
import com.example.docs_manager.Retrofit.MyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import java.io.File

class MyViewModel:ViewModel() {

    var currentFile= mutableStateOf<File>(File(""));
    var currentDir = mutableStateOf(File(Environment.getExternalStorageDirectory().path))
    var root= mutableStateListOf(File(Environment.getExternalStorageDirectory().path))
    val selectedFiles = mutableStateListOf<File>()
    var currentCloudDir = mutableStateOf("0")
    var currentCloudFile = mutableStateOf<FileModel>(FileModel("","","","","","","",""))
    var cloudRoot= mutableStateListOf("0")

    var filesToUpload= mutableStateListOf<File>()

    var uploadStatus= MutableStateFlow(0);
    val progress: StateFlow<Int> = uploadStatus

    val cloudStorageFiles= mutableStateListOf<FileModel>()
    val cloudStorageFolders= mutableStateListOf<FolderModel>()

    fun checkFolders(dir:File)
    {
        for(file in dir.listFiles())
        {
            if(file.isDirectory)
            {
                checkFolders(file);
            }
            else if(file.isFile)
            {

                uploadStatus.value++;
                filesToUpload.add(file)
            }
        }
    }

    fun  addToFilesToBeUploaded()
    {
        filesToUpload.clear()
        for( sfile in selectedFiles)
        {
            if(sfile.isFile)
            {
                uploadStatus.value++;
                filesToUpload.add(sfile);
            }
            else if(sfile.isDirectory)
            {
                checkFolders(sfile)
            }
        }
    }


    suspend fun uploadFile()
    {


        val service=MyRepository("http://192.168.1.41:3000/api/");

        var res:ResponseBody
        for( file in filesToUpload)
        {
            res= service.uploadFile(file)
            viewModelScope.launch {
                uploadStatus.value -= 1;
            }


        }




    }
    fun  getFiles(id:String)
    {
        System.out.println(id)
        cloudStorageFiles.clear()
        viewModelScope.launch {
            val service=MyRepository("http://192.168.1.41:3000/api/");
            val res=service.getFiles(id)
            if (res != null) {
               cloudStorageFiles.addAll(res)
            }
        }



    }
   fun getFolders(id:String)
    {
        cloudStorageFolders.clear()
        viewModelScope.launch {
            val service=MyRepository("http://192.168.1.41:3000/api/");
            val res=service.getFolder(id)
            if (res != null) {
                cloudStorageFolders.addAll(res)
            }
        }

    }


}