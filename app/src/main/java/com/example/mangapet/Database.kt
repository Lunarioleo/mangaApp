package com.example.mangapet


import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class Database @Inject constructor() {

    fun getDatabaseInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance("https://chatpetproj-default-rtdb.europe-west1.firebasedatabase.app/")
    }


}