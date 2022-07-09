package com.example.ingredient.data

import androidx.room.*

@Dao
interface ExpirationDateDao {
    @Insert
    fun insert(expirationDateData: ExpirationDateData)

    @Update
    fun update(expirationDateData: ExpirationDateData)

    @Delete
    fun delete(expirationDateData: ExpirationDateData)

    @Query("SELECT * FROM expiration_date")
    fun getAll(): List<ExpirationDateData>

}