package com.example.dejoyafinalassessment.data.model

// these fields are for my specific topic ("sports"), confirmed via Postman -
// not the generic placeholder fields from the assignment brief
data class Entity(
    val sportName: String,
    val playerCount: Int,
    val fieldType: String,
    val olympicSport: Boolean,
    val description: String
)
