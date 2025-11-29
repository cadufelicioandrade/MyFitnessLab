package com.app.myfitnesslab

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.myfitnesslab.data.App
import com.app.myfitnesslab.model.Aluno
import com.app.myfitnesslab.model.Calc
import kotlin.concurrent.thread


class ListActivity : AppCompatActivity() {

    private lateinit var rvList: RecyclerView
    private lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list)

        val type =
            intent?.extras?.getString("type") ?: throw IllegalStateException("type not found")

        rvList = findViewById(R.id.rv_list_items)
        rvList.layoutManager = LinearLayoutManager(this)

        listAdapter = ListAdapter(listOf(), listOf())
        rvList.adapter = listAdapter

        thread {

            val app = application as App
            val calcs = app.db.calcDao().getByType(type)
            val alunos = app.db.alunoDao().getAll()

            if (type == R.string.type_aluno.toString()) {
                runOnUiThread {
                    if (alunos.isEmpty()) {
                        Toast.makeText(this, R.string.register_not_found, Toast.LENGTH_LONG).show()
                    } else {
                        listAdapter.updateAlunoList(alunos)
                    }
                }
            } else {
                runOnUiThread {
                    if (calcs.isEmpty()) {
                        Toast.makeText(this, R.string.register_not_found, Toast.LENGTH_LONG).show()
                    } else {

                        val aluno_dictionary = alunos.associate { it.id to it.nome }

                        calcs.forEach { calc ->
                            val nomeAluno = aluno_dictionary[calc.alunoId]
                            calc.nomeAluno = nomeAluno ?: ""
                        }

                        listAdapter.updateCalcList(calcs)
                    }
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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    private inner class ListAdapter(
        private var listCalc: List<Calc>, private var listAlunos: List<Aluno>
    ) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            val view = layoutInflater.inflate(R.layout.list_item, parent, false)
            return ListViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            if (listCalc.size > 0) {
                val currentItem = listCalc[position]
                holder.bindCalc(currentItem)
            } else {
                val currentItem = listAlunos[position]
                holder.bindAluno(currentItem)
            }
        }

        override fun getItemCount(): Int {
            val size = if (listCalc.isNotEmpty()) listCalc.size else listAlunos.size
            return size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun updateCalcList(newList: List<Calc>) {
            listCalc = newList
            notifyDataSetChanged()
        }

        @SuppressLint("NotifyDataSetChanged")
        fun updateAlunoList(newList: List<Aluno>) {
            listAlunos = newList
            notifyDataSetChanged()
        }

        private inner class ListViewHolder(listCalcView: View) :
            RecyclerView.ViewHolder(listCalcView) {

            @SuppressLint("SetTextI18n")
            fun bindCalc(item: Calc) {
                val txtItem = itemView.findViewById<TextView>(R.id.list_item_text)
                val resp = String.format("%.2f", item.resp)
                txtItem.setText("${item.nomeAluno}: ${resp} - ${item.type}")

                txtItem.setOnLongClickListener {
                    showOptionsDialog(
                        itemView.context,
                        onEdit = {
                            val intent = when (item.type) {
                                "imc" -> Intent(this@ListActivity, ImcActivity::class.java)
                                "tmb" -> Intent(this@ListActivity, TmbActivity::class.java)
                                else -> null
                            }
                            intent?.let {
                                it.putExtra("updateId", item.id)
                                startActivity(it)
                            }
                        },
                        onDelete = {
                            deleteCalc(item)
                        }
                    )
                    true
                }
            }

            @SuppressLint("SetTextI18n")
            fun bindAluno(item: Aluno) {
                val txtItem = itemView.findViewById<TextView>(R.id.list_item_text)
                txtItem.setText("Aluno: ${item.nome}")
                txtItem.setOnLongClickListener {
                    showOptionsDialog(
                        itemView.context,
                        onEdit = {
                            val intent = Intent(this@ListActivity, AlunoActivity::class.java)
                            intent.putExtra("updateId", item.id)
                            startActivity(intent)
                        },
                        onDelete = {
                            deleteAluno(item)
                        }
                    )

                    true
                }
            }
        }
    }

    private fun showOptionsDialog(
        context: android.content.Context,
        onEdit: () -> Unit,
        onDelete: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(R.string.app_name)
            .setItems(R.array.options_operations) { dialog, which ->
                when (which) {
                    0 -> onEdit()
                    1 -> onDelete()
                }
            }.create()
            .show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteCalc(calc: Calc) {

        thread {
            val app = application as App
            val dao = app.db.calcDao()
            dao.deleteById(calc.id)
            val list = app.db.calcDao().getByType(calc.type)

            val alunoDao = app.db.alunoDao()
            val listAlunos = alunoDao.getAll()

            list.forEach { c ->
                val aluno = listAlunos.find{ it.id == c.alunoId }

                if(aluno != null){
                    c.nomeAluno = aluno.nome
                }
            }

            runOnUiThread {
                Toast.makeText(this, R.string.msgDeleted, Toast.LENGTH_SHORT).show()
                listAdapter.updateCalcList(list)
            }
        }
    }

    private fun deleteAluno(aluno: Aluno) {

        thread {
            val app = application as App
            val dao = app.db.alunoDao()
            dao.delete(aluno)
            val alunos = app.db.alunoDao().getAll()

            runOnUiThread {
                Toast.makeText(this, R.string.msgDeleted, Toast.LENGTH_SHORT).show()
                listAdapter.updateAlunoList(alunos)
            }
        }
    }
}