package com.example.health_and_fitness.Medicine_Remainder.Model

data class Reminder(
    var id: Int= 0,
    var title: String?,
    var date: String?,
    var time: String?,
    var repeat: String?,
    var repeatNo: String?,
    var repeatType: String?,
    var active: String?
)