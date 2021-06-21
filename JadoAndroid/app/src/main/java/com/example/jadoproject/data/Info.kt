package com.example.jadoproject.data

import javax.security.auth.Subject

data class Jado(
    var Camera : String,
    var User : User
)

data class User(
    var ID : ArrayList<ID>
)

data class ID(
    var UserName : UserName
)

data class UserName(
    var Study : Study,
    var USerInfo : UserInfo
)

data class UserInfo(
   var Email : String,
   var Name : String,
   var Password : String,
   var PhoneNumber : String
)

data class Study(
    var date  : ArrayList<StudyDate>
)

data class StudyDate(
    var Subject :  ArrayList<Subjects>,
    var behaior_count : Int,
    var behavior_time : String
)

data class Subjects(
    var subjectTitle : String
)

