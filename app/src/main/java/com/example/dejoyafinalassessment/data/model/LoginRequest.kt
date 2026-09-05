package com.example.dejoyafinalassessment.data.model

// heads up future me: the brief's field names are placeholders, this is what
// actually works against the API (checked in Postman).
// username = student ID but WITHOUT the leading "s" (e.g. 123456, not s123456)
// password = my first name, and it's case-sensitive
// the professor corrected this after the brief went out so don't trust the PDF here
data class LoginRequest(
    val username: String,
    val password: String
)
