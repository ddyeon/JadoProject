package com.example.jadoproject.data

data class ID (
    var Info : Info,
    var Study : Study
        )


data class Info (
    var name : String,
    var password : String,
    var phoneNumber : String,
    var email : String
    )

data class Study (
    var date : DateInfo
        )
data class DateInfo (
    var behavior : Int,
    var behavior_time : Int,
        )