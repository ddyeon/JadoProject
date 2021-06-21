package com.example.jadoproject.data

import android.graphics.Paint
import javax.security.auth.Subject

data class User(
    var dayeon : Studys
)

data class Studys (
    var dates : ArrayList<StudyDate>,
    var UserInfo : Info
)

data class StudyDate(
    var subjects : subjects,
    var behavior_count : Int,
    var behavior_time : String
)

data class subjects(
    var English : String,
    var Korean : String,
    var Math : String
)

data class Info(
    var email : String,
    var name : String,
    var password : String,
    var phoneNumber : String
)