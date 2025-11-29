package com.app.myfitnesslab.interfaces

import androidx.room.*
import com.app.myfitnesslab.model.Calc

@Dao
interface CalcDao {

    @Insert
    fun insert(calc: Calc)

    @Update
    fun update(calc: Calc)

    @Query("SELECT * FROM Calc WHERE type = :type")
    fun getByType(type: String): List<Calc>

    @Query("SELECT * FROM Calc WHERE id = :id")
    fun getById(id: Int): Calc

    @Query("DELETE FROM Calc WHERE id = :id")
    fun deleteById(id: Int)
}