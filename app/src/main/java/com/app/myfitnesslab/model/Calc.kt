package com.app.myfitnesslab.model

import androidx.room.*
import java.util.Date

@Entity(
    tableName = "calc",
    foreignKeys = [
        ForeignKey(
            entity = Aluno::class,
            parentColumns = ["id"],
            childColumns = ["alunoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("alunoId")]
)
data class Calc(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "resp") var resp: Double,
    @ColumnInfo(name = "created_date") var createdDate: Date = Date(),
    @ColumnInfo(name = "alunoId") var alunoId: Int = 0
){
    @Ignore var nomeAluno: String = ""
}
