package com.example.myapplication

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.io.File

class MainActivityViewModel(context: Context) : ViewModel() {

    private val contentResolver = context.contentResolver
    private val filesDir = context.filesDir

    val fileListState = mutableStateListOf<String>()

    init {
        filesDir?.listFiles { _, name -> fileListState.add(name) }
    }

    fun onContentReceived(contentUri: Uri) {
        val newContentFile = File(filesDir, contentUri.toString().substringAfterLast('/'))
        if (!newContentFile.exists()) {
            val inputStream = contentResolver.openInputStream(contentUri)
            val outputStream = newContentFile.outputStream()

            if (inputStream != null) {
                inputStream.copyTo(outputStream)
                fileListState.add(newContentFile.name)
            }
            inputStream?.close()
        }
    }

    fun onContentDeleted(fileName: String) {
        val fileToDelete = File(filesDir, fileName)
        fileToDelete.delete()
        fileListState.remove(fileName)
    }
}