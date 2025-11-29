package com.app.myfitnesslab.interfaces

import androidx.room.*
import com.app.myfitnesslab.model.Aluno

@Dao
interface AlunoDao {

    @Insert
    fun insert(aluno: Aluno)

    @Query("SELECT * FROM alunos WHERE id = :id")
    fun getById(id: Int): Aluno?

    @Query("SELECT * FROM alunos")
    fun getAll(): List<Aluno>

    @Update
    fun update(aluno: Aluno)

    @Delete
    fun delete(aluno: Aluno)

}