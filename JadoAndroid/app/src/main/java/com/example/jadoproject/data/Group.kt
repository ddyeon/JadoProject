package com.example.jadoproject.data

import android.graphics.Paint
import com.google.gson.annotations.SerializedName
import javax.security.auth.Subject

data class GroupList(
   var groups : ArrayList<Group>
)

data class Group(
    var title: GName
)

data class GName(
    var friend : ArrayList<Members>,
    var goal : String,
    var groupname : String
)

data class Members(
    var id : String,
    var image : String
)

data class Groups(
    var groupname : String = "",
    var goal : String = "",
    //var friend : List<Member>
)


data class Friend(
    var id : String
)
