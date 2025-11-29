package com.app.myfitnesslab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.myfitnesslab.data.App
import com.app.myfitnesslab.model.Aluno
import com.app.myfitnesslab.model.Calc
import kotlin.concurrent.thread


class ImcActivity : AppCompatActivity() {

    private lateinit var editWeight: EditText
    private lateinit var editHeight: EditText
    private lateinit var btnCalc: Button
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private var listaAlunosDoBanco = listOf<Aluno>()
    private var alunoId = 0
    private var updateId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_imc)

        autoCompleteTextView = findViewById(R.id.ls_alunos_imc)
        editWeight = findViewById(R.id.edit_imc_weight)
        editHeight = findViewById(R.id.edit_imc_height)
        btnCalc = findViewById(R.id.btn_imc)

        updateId = intent.getIntExtra("updateId", 0)

        if (updateId > 0) {
            btnCalc.setText(R.string.atualizar)
        }

        thread {
            val app = application as App
            val dao = app.db.alunoDao()
            listaAlunosDoBanco = dao.getAll()

            var calculoSalvo: Calc? = null
            if (updateId > 0) {
                calculoSalvo = app.db.calcDao().getById(updateId)
            }

            runOnUiThread {

                if (listaAlunosDoBanco.isEmpty()) {
                    Toast.makeText(this, R.string.aluno_not_found, Toast.LENGTH_LONG).show()
                    return@runOnUiThread
                }
                val nomeAlunos = listaAlunosDoBanco.map { it.nome }
                var adapter =
                    ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nomeAlunos)
                autoCompleteTextView.setAdapter(adapter)

                if (updateId > 0 && calculoSalvo != null) {
                    val alunoEncontrado = listaAlunosDoBanco.find { it.id == calculoSalvo.alunoId }
                    if (alunoEncontrado != null) {
                        autoCompleteTextView.setText(alunoEncontrado.nome, false)
                        alunoId = alunoEncontrado.id
                    }
                }
            }
        }

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            //A posição visual bate com a posição da lista em memória
            val alunoSelecionado = listaAlunosDoBanco[position]
            //pega o id o aluno selecionado
            alunoId = alunoSelecionado.id
            //limpar erros se houver
            autoCompleteTextView.error = null
        }

        btnCalc.setOnClickListener {
            if (!validarCampos()) {
                Toast.makeText(this, R.string.fields_messages, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val weight = editWeight.text.toString().toInt()
            val height = editHeight.text.toString().toInt()

            val result = CalcularImc(weight, height)
            val responseTextImc = imcResponse(result)

            var service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            service.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.imc_response, result))
                .setMessage(responseTextImc)
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setNegativeButton(R.string.save) { _, _ ->
                    thread {
                        val app = application as App
                        val dao = app.db.calcDao()

                        if (updateId > 0) {
                            dao.update(
                                Calc(
                                    id = updateId,
                                    type = "imc",
                                    resp = result,
                                    alunoId = alunoId
                                )
                            )
                        } else {
                            dao.insert(
                                Calc(
                                    type = "imc",
                                    resp = result,
                                    alunoId = alunoId
                                )
                            )
                        }

                        runOnUiThread {
                            Toast.makeText(this, R.string.msg_cadastro, Toast.LENGTH_SHORT).show()
                            goToListActivity()
                        }

                        //depois que tiver a activity de listagem redirecinar para a mesma
                    }
                }.create()
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_search) {
            finish()
            goToListActivity()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun goToListActivity() {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("type", "imc")
        startActivity(intent)
    }

    private fun validarCampos(): Boolean {

        val pesoValido = editWeight.text.toString().isNotEmpty() && !editWeight.text.toString()
            .startsWith("0")
        val alturaValida =
            editHeight.text.toString().isNotEmpty() && !editHeight.text.toString()
                .startsWith("0")

        // Verifica se o usuário realmente clicou em alguém (ID > 0)
        val alunoValido = alunoId > 0

        if (!alunoValido) {
            autoCompleteTextView.error = "Selecione um aluno válido da lista"
        }

        return pesoValido && alturaValida && alunoValido
    }

    private fun CalcularImc(weight: Int, height: Int): Double {
        val height = height.toDouble() / 100
        return weight / (height * height)
    }

    @StringRes
    private fun imcResponse(imc: Double): Int {
        return when {
            imc < 15.0 -> R.string.imc_severely_low_weight
            imc < 16.0 -> R.string.imc_very_low_weight
            imc < 18.5 -> R.string.imc_low_weight
            imc < 25.0 -> R.string.normal
            imc < 30.0 -> R.string.imc_high_weight
            imc < 35.0 -> R.string.imc_so_high_weight
            imc < 40.0 -> R.string.imc_severely_high_weight
            else -> R.string.imc_extreme_weight
        }
    }

}