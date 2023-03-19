package com.example.firebasestorage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import firebasestorage.main2

class main1 : AppCompatActivity() {

    private val storageRef = Firebase.storage.reference
    private lateinit var uploadButton: Button
    private lateinit var download_bt: Button

    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uploadButton = findViewById(R.id.upload_button)
        download_bt = findViewById(R.id.download_bt)
        progressBar = findViewById(R.id.progress_bar)
        statusText = findViewById(R.id.status_text)
        download_bt.setOnClickListener {
            val intent = Intent(this, main2::class.java)
            startActivity(intent)
        }
        uploadButton.setOnClickListener {
            selectFile()

        }

    }

    private fun selectFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/pdf"
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun uploadFile(fileUri: Uri) {
        val fileName = fileUri.lastPathSegment!!
        val fileRef = storageRef.child(fileName)

        val uploadTask = fileRef.putFile(fileUri)

        uploadTask.addOnSuccessListener {
            progressBar.visibility = View.GONE
            statusText.text = "Upload successful"
        }.addOnFailureListener {
            progressBar.visibility = View.GONE
            statusText.text = "Upload failed"
        }.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
            progressBar.progress = progress.toInt()
        }
    }

    companion object {
        const val REQUEST_CODE = 101
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { fileUri ->
                progressBar.visibility = View.VISIBLE
                statusText.text = "Uploading..."
                uploadFile(fileUri)
            }
        }
    }

}