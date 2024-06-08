package com.example.mangapet

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class Repository @Inject constructor(private val db: Database) {

    fun getDatabaseInstance(): FirebaseDatabase {
        return db.getDatabaseInstance()
    }

    fun getDataBasePathForUsers():DatabaseReference {
        return db.getDatabaseInstance().getReference("users")
    }

    fun getDatabaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    fun getFirebaseStorageReference(): StorageReference{
        return FirebaseStorage.getInstance().reference
    }
}