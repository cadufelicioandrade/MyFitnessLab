package com.app.myfitnesslab.model

import androidx.room.*

@Entity(tableName = "alunos")
data class Aluno (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "nome") var nome: String
)