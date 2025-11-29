package com.app.myfitnesslab.data

import android.content.Context
import androidx.room.*
import com.app.myfitnesslab.interfaces.AlunoDao
import com.app.myfitnesslab.interfaces.CalcDao
import com.app.myfitnesslab.model.Aluno
import com.app.myfitnesslab.model.Calc
import com.app.myfitnesslab.utils.DateConverter

@Database(entities = [Aluno::class, Calc::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun alunoDao(): AlunoDao
    abstract fun calcDao(): CalcDao

    companion object{

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "my_fitness_lab"
                ).build()
                INSTANCE = instance
                instance
            }
        }


    }

}