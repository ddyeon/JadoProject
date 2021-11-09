package com.example.jadoproject.data


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


