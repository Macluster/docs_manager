package com.example.docs_manager.Screens

import android.content.Context
import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.docs_manager.Models.FileModel


import java.io.File



@Composable
fun ShowVideo(currentFile: File,context: Context)
{
    val videoViewer = remember { VideoView(context).apply {
        setVideoURI(Uri.parse(currentFile.path))

    } }


    videoViewer.setOnPreparedListener {
        it.start()
    }

    Scaffold() {padding->
        Column(modifier = Modifier.padding(padding)) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    ,
                factory = { context ->

                    videoViewer

                }
            )
        }
    }
}



@Composable
fun ShowNetorkVideo(video: FileModel,context: Context)
{
    val videoViewer = remember { VideoView(context).apply {
        setVideoURI(Uri.parse(video.file_url))

    } }


    videoViewer.setOnPreparedListener {
        it.start()
    }

    Scaffold() {padding->
        Column(modifier = Modifier.padding(padding)) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                ,
                factory = { context ->

                    videoViewer

                }
            )
        }
    }
}