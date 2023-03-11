package com.example.instagram

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import com.example.instagram.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.sql.Timestamp
import java.util.UUID

private lateinit var binding: ActivityUploadBinding
private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
private lateinit var permissionLauncher: ActivityResultLauncher<String>
var selectedPicture: Uri?=null
private lateinit var auth: FirebaseAuth
private lateinit var storage: FirebaseStorage
private lateinit var firestore: FirebaseFirestore

class UploadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerLauncher()
        auth=Firebase.auth
        storage=Firebase.storage
        firestore=Firebase.firestore



    }

    fun uploadImage(view: View){
        //Universal Unique ID
        val uuid=UUID.randomUUID()
        val imageName="$uuid.jpg"
        val reference= storage.reference
        val referenceImage=reference.child("images").child(imageName)
        if (selectedPicture!=null){
            referenceImage.putFile(selectedPicture!!).addOnSuccessListener {
            //Download URL
                val uploadedPictureRef= storage.reference.child("images").child(imageName)
                uploadedPictureRef.downloadUrl.addOnSuccessListener {
                 val downloadURL=it.toString()
                    if (auth.currentUser !=null){
                        val postMap= hashMapOf<String,Any>()
                        postMap.put("downloadURL",downloadURL)
                        postMap.put("useremail", auth.currentUser!!.email!!)
                        postMap.put("comment",binding.commentText.text.toString())
                        postMap.put("date",com.google.firebase.Timestamp.now())

                        firestore.collection("Posts").add(postMap).addOnSuccessListener {

                            finish()

                        }.addOnFailureListener(){
                            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }
                    }



                }
            }.addOnFailureListener(){
                Toast.makeText(this, it.localizedMessage!!.toString(),Toast.LENGTH_LONG).show()
            }
        }



    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun selectImage(view: View){

        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission need for Galery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                }.show()
            }
            else
            {
                permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
        else
        {
            val intentToGalery= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGalery)

        }
    }

    private fun registerLauncher(){
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if (result.resultCode == RESULT_OK){
                val intentFromResult=result.data
                if (intentFromResult!=null){
                    selectedPicture=intentFromResult.data
                    selectedPicture?.let {
                        binding.imageView.setImageURI(it)
                    }
                }
            }
        }

        permissionLauncher= registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
            if (result){
                val intentToGalery= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGalery)
            }
            else
            {
                Toast.makeText(this@UploadActivity,"Permission Needed",Toast.LENGTH_LONG).show()
            }
        }

    }


}