package com.example.mangapet

import android.net.Uri
import android.renderscript.Sampler.Value
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID

import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    private val _uiState = MutableLiveData<UiStates>()
    val uiState: LiveData<UiStates> = _uiState
    private val auth = repo.getDatabaseAuth()
    private val database = repo.getDataBasePathForUsers()

    private fun getCurrentUserTarget(): DatabaseReference? {
        val currentUser = auth.currentUser
        return currentUser?.let {
            database.child(it.uid)
        }
    }

    fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result.user?.let { user ->
                    val currentUser =
                        User(id = user.uid, displayName = user.displayName, email = user.email)
                    getCurrentUserTarget()?.setValue(currentUser.toMap())
                        ?.addOnCompleteListener { registerUser ->
                            if (registerUser.isSuccessful) {
                                _uiState.postValue(
                                    UiStates.UserState(
                                        true,
                                        "User successfully created!"
                                    )
                                )
                            } else {
                                _uiState.postValue(
                                    UiStates.UserState(
                                        false,
                                        "${registerUser.exception?.message}"
                                    )
                                )
                            }
                        }
                }
            } else {
                _uiState.postValue(UiStates.UserState(false, "${task.exception?.message}"))
            }
        }
    }

    fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _uiState.postValue(UiStates.UserState(true, "Successfully signed in!"))
            } else {
                _uiState.postValue(UiStates.UserState(false, "${it.exception}"))
            }
        }
    }

    fun saveImageToStorage(imageUri: Uri) {
        val fileName = "images/${UUID.randomUUID()}.jpg"
        val storageRef = repo.getFirebaseStorageReference()
        val imageRef = storageRef.child(fileName)
        imageRef.putFile(imageUri).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                val target = getCurrentUserTarget()
                target?.child("pfImage")?.setValue(imageUrl)?.addOnCompleteListener { updateTask ->
                    if (updateTask.isSuccessful) {
                        _uiState.postValue(UiStates.UserFullInfo(getCurrentUser().currentUser?.displayName ?: "UnknownUser", imageUrl))
                        getCurrentUserInfo()  // Fetch the updated user info immediately
                    }
                }


            }
        }
    }


    fun getCurrentUser(): FirebaseAuth {
        return repo.getDatabaseAuth()
    }


    fun changeUserName(userName: String) {
        val currentUser = getCurrentUser().currentUser
        if (currentUser != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build()
                currentUser.updateProfile(profileUpdates).addOnCompleteListener {
                if (it.isSuccessful){
                    getCurrentUserTarget()?.child("displayName")?.setValue(userName)
                    getCurrentUserInfo()
                }
            }
        }
    }

    fun getCurrentUserInfo(){
        val target = getCurrentUserTarget()
        target?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val data = snapshot.getValue(User::class.java)
                    _uiState.postValue(UiStates.UserFullInfo(userName = data?.displayName?: "Unknown user", data?.pfImage?: "https://www.shareicon.net/data/512x512/2015/10/16/656968_info_512x512.png"))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getUsersList(){
        val target = database
        val list = ArrayList<User>()
        target.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                    snapshot.children.forEach {
                        val data = it.getValue(User::class.java)
                        list.add(data!!)
                        _uiState.postValue(UiStates.UsersInfoList(list))
                    }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }



    sealed class UiStates {
        class UserState(val isUserRegistered: Boolean, val description: String) : UiStates()

        class UserFullInfo(val userName: String?, val profileAvatar: String?): UiStates()

        class UsersInfoList(val usersList: List<User>): UiStates()
    }
}




