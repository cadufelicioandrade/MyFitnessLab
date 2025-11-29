package com.app.myfitnesslab

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.myfitnesslab.data.App
import com.app.myfitnesslab.model.Aluno
import kotlin.concurrent.thread

class AlunoActivity : AppCompatActivity() {

    private lateinit var btnCadastro: Button
    private lateinit var editNomeAluno: EditText
    private var updateId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_aluno)

        editNomeAluno = findViewById(R.id.edit_nome_aluno)
        btnCadastro = findViewById(R.id.btn_salvar_aluno)
        updateId = intent.getIntExtra("updateId", 0)

        if (updateId > 0) {
            btnCadastro.setText(R.string.atualizar)
            thread {
                val app = application as App
                val dao = app.db.alunoDao()
                val aluno = dao.getById(updateId)
                runOnUiThread { editNomeAluno.setText(aluno?.nome) }
            }
        }

        btnCadastro.setOnClickListener {

            if (editNomeAluno.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.campo_vazio, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            thread {
                val app = application as App
                val dao = app.db.alunoDao()

                if(updateId > 0){
                    val aluno = Aluno(id = updateId, nome = editNomeAluno.text.toString())
                    dao.update(aluno)
                }else{
                    val aluno = Aluno(nome = editNomeAluno.text.toString())
                    dao.insert(aluno)
                }

                runOnUiThread {
                    Toast.makeText(this, R.string.msg_cadastro, Toast.LENGTH_SHORT).show()
                    goToList()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_search) {
            finish()
            goToList()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun goToList() {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("type", R.string.type_aluno.toString())
        startActivity(intent)
    }
}