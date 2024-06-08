package com.example.mangapet


data class User(val id: String? = null, val displayName: String? = null, val email: String? = null, val pfImage:String? = null){

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to displayName,
            "email" to email
        )
    }
}