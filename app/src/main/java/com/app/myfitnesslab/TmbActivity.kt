package com.app.myfitnesslab

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.*
import com.app.myfitnesslab.data.App
import com.app.myfitnesslab.model.Aluno
import com.app.myfitnesslab.model.Calc
import kotlin.concurrent.thread

class TmbActivity : AppCompatActivity() {

    private lateinit var btnTmb: Button
    private lateinit var lstAlunosTmb: AutoCompleteTextView
    private lateinit var lstGeneroTmb: AutoCompleteTextView
    private lateinit var lstAtividadeTmb: AutoCompleteTextView
    private lateinit var weightTmb: EditText
    private lateinit var heightTmb: EditText
    private lateinit var ageTmb: EditText
    private var listaAlunosDoBanco = listOf<Aluno>()
    private var alunoId = 0
    private var updateId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tmb)

        btnTmb = findViewById(R.id.btn_tmb)
        lstAlunosTmb = findViewById(R.id.lst_alunos_tmb)
        lstGeneroTmb = findViewById(R.id.genero_tmb)
        lstAtividadeTmb = findViewById(R.id.atividade_tmb)
        weightTmb = findViewById(R.id.weight_tmb)
        heightTmb = findViewById(R.id.height_tmb)
        ageTmb = findViewById(R.id.age_tmb)

        updateId = intent.getIntExtra("updateId", 0)

        if(updateId > 0){
            btnTmb.setText(R.string.atualizar)
        }

        val adapter_genero = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            resources.getStringArray(R.array.gender)
        )
        val adapter_atividade = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            resources.getStringArray(R.array.tmb_lifestyle)
        )

        lstGeneroTmb.setAdapter(adapter_genero)
        lstAtividadeTmb.setAdapter(adapter_atividade)

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
                var adapter_alunos = ArrayAdapter(
                    this,
                    android.R.layout.simple_dropdown_item_1line, nomeAlunos
                )
                lstAlunosTmb.setAdapter(adapter_alunos)

                if (updateId > 0 && calculoSalvo != null) {
                    val alunoEncontrado = listaAlunosDoBanco.find { it.id == calculoSalvo.alunoId }
                    if (alunoEncontrado != null) {
                        lstAlunosTmb.setText(alunoEncontrado.nome, false)
                        alunoId = alunoEncontrado.id
                    }

                }
            }
        }

        lstAlunosTmb.setOnClickListener { lstAlunosTmb.showDropDown() }
        lstGeneroTmb.setOnClickListener { lstGeneroTmb.showDropDown() }
        lstAtividadeTmb.setOnClickListener { lstAtividadeTmb.showDropDown() }

        lstAlunosTmb.setOnItemClickListener { parent, view, position, id ->
            val nomeSelecionado = parent.getItemAtPosition(position).toString()
            val alunoSelecionado = listaAlunosDoBanco.find { it.nome == nomeSelecionado }
            if (alunoSelecionado != null) {
                alunoId = alunoSelecionado.id
                lstAlunosTmb.error = null
            }
        }

        btnTmb.setOnClickListener {
            if (!validarCampos()) {
                Toast.makeText(this, R.string.fields_messages, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val genero = lstGeneroTmb.text.toString()
            val lifestyle = lstAtividadeTmb.text.toString()
            val weight = weightTmb.text.toString().toInt()
            val height = heightTmb.text.toString().toInt()
            val age = ageTmb.text.toString().toInt()

            val tmbBasal = CalcularTmbBasal(weight, height, age, genero)
            val tmbTotal = tmbBasal * getLifestyleValue(lifestyle)

            val message_resp = getString(R.string.tmb_response, tmbTotal)

            AlertDialog.Builder(this)
                .setMessage(message_resp)
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setNegativeButton(R.string.save) { dialog, which ->
                    thread {
                        val app = application as App
                        val dao = app.db.calcDao()

                        if (updateId > 0) {
                            dao.update(
                                Calc(
                                    id = updateId,
                                    type = "tmb",
                                    resp = tmbTotal,
                                    alunoId = alunoId
                                )
                            )
                        } else {
                            dao.insert(
                                Calc(
                                    type = "tmb",
                                    resp = tmbTotal,
                                    alunoId = alunoId
                                )
                            )
                        }
                        runOnUiThread {
                            Toast.makeText(this, R.string.msg_cadastro, Toast.LENGTH_SHORT).show()
                            goListActivities()
                        }

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
            goListActivities()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goListActivities() {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("type", "tmb")
        startActivity(intent)
    }

    private fun CalcularTmbBasal(weight: Int, height: Int, age: Int, gender: String): Double {
        val base = (10 * weight) + (6.25 * height) - (5 * age)
        return if (gender == "Masculino") base + 5 else base - 161
    }

    private fun getLifestyleValue(lifestyle: String): Double {
        val lifestyleArray = resources.getStringArray(R.array.tmb_lifestyle)

        return when (lifestyle) {
            lifestyleArray[0] -> 1.2
            lifestyleArray[1] -> 1.375
            lifestyleArray[2] -> 1.55
            lifestyleArray[3] -> 1.725
            lifestyleArray[4] -> 1.9
            else -> 1.0
        }
    }

    private fun validarCampos(): Boolean {
        return weightTmb.text.toString().isNotEmpty() &&
                heightTmb.text.toString().isNotEmpty() &&
                ageTmb.text.toString().isNotEmpty() &&
                !weightTmb.text.toString().startsWith("0") &&
                !heightTmb.text.toString().startsWith("0") &&
                !ageTmb.text.toString().startsWith("0") &&
                lstAlunosTmb.text.toString().isNotEmpty() &&
                lstGeneroTmb.text.toString().isNotEmpty() &&
                lstAtividadeTmb.text.toString().isNotEmpty()

    }
}