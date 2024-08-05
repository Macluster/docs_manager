package com.example.docs_manager.Retrofit

import com.example.docs_manager.Models.FileModel
import com.example.docs_manager.Models.FolderModel
import com.example.docs_manager.Models.UploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call



import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.io.File

data class Rbody(val folder_id:String)
interface  ApiService
{
    @POST("uploadData")
    @Multipart
    suspend fun uploadFiles(@Part file: MultipartBody.Part,@Part("path") path:RequestBody): ResponseBody

    @POST("getFiles")
     suspend fun getFiles(@Body body:Rbody):Response<List<FileModel>>

    @POST("getFolder")
     suspend fun getFolders(@Body body:Rbody):Response<List<FolderModel>>

}




class MyRepository( api:String)
{





    private  val retrofit= Retrofit.Builder().baseUrl(api).addConverterFactory(GsonConverterFactory.create()).build()
    private  val apiService=retrofit.create(ApiService::class.java)

    suspend fun uploadFile(file:File): ResponseBody{


            val requestFile = file.asRequestBody("*/*".toMediaTypeOrNull())

            val filePathBody = file.path.toRequestBody("text/plain".toMediaTypeOrNull())
              val body=MultipartBody.Part.createFormData("file", file.path, requestFile)



            return apiService.uploadFiles(body,filePathBody);
    }

    suspend fun getFiles(id:String): List<FileModel>?
    {
        val response = apiService.getFiles(Rbody(id))
        if (response.isSuccessful) {
            return response.body() // This will be of type List<FolderModel> or null
        } else {
            println("Error: ${response.code()} ${response.message()}")
            return null
        }
    }
    suspend fun getFolder(id:String): List<FolderModel>?
    {
        val response = apiService.getFolders(Rbody(id))
        if (response.isSuccessful) {
            return response.body() // This will be of type List<FolderModel> or null
        } else {
            println("Error: ${response.code()} ${response.message()}")
            return null
        }
    }


}