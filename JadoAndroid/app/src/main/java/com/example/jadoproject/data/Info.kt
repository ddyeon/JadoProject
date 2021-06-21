package com.example.jadoproject.data

import android.graphics.Paint
import com.google.gson.annotations.SerializedName
import javax.security.auth.Subject

data class User(
    var dayeon : Studys
)

data class Studys (
    var dates : ArrayList<StudyDate>,
    var UserInfo : Info
    )

data class  StudyDate(
    var subjects : subjects,
    var behavior_count : Int,
    var behavior_time : String,
    var total_time : String
)

data class subjects(
    var English : String,
    var Korean : String,
    var Math : String
)

data class Info(
    var Email : String,
    var Name : String,
    var Password : String,
    var PhoneNumber : String
)

data class weekdata(
    @SerializedName("2021-06-14")
    val _0614 : StudyDate,

    @SerializedName("2021-06-15")
    val _0615 : StudyDate,

    @SerializedName("2021-06-16")
    val _0616 : StudyDate,

    @SerializedName("2021-06-17")
    val _0617 : StudyDate,

    @SerializedName("2021-06-18")
    val _0618 : StudyDate,

    @SerializedName("2021-06-19")
    val _0619 : StudyDate,

    @SerializedName("2021-06-20")
    val _0620 :StudyDate

)

