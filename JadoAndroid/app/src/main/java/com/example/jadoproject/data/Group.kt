package com.example.jadoproject.data

import android.graphics.Paint
import com.google.gson.annotations.SerializedName
import javax.security.auth.Subject

data class Group(
   var groups : ArrayList<Group>
)

data class Groups(
    var groupname : String = "",
    var goal : String = "",
    //var friend : List<Member>
)

data class Member(
    var id : String,
    var image: String
)

data class Friend(
    var id : String
)
