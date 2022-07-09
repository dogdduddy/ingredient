package com.example.ingredient.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expiration_date")
data class ExpirationDateData(
    @PrimaryKey var name: String,
    var state: String,
    var registration: String,
    var expirationDate: String
)