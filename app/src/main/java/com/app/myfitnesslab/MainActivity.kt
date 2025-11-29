package com.app.myfitnesslab

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.myfitnesslab.model.MainItem

class MainActivity : AppCompatActivity() {

    private lateinit var rvMain: RecyclerView
    private lateinit var btnAluno: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        btnAluno = findViewById(R.id.cad_aluno)

        btnAluno.setOnClickListener{
            val intent = Intent(this, AlunoActivity::class.java)
            startActivity(intent)
        }


        val mainItems = mutableListOf<MainItem>()

        mainItems.add(
            MainItem(id=1, drawableId = R.drawable.outline_monitor_weight_24,textStringId = R.string.IMC,color = R.color.laranja))

        mainItems.add(
            MainItem(id=2, drawableId = R.drawable.outline_bolt_24,textStringId = R.string.TMB,color = R.color.amber))

        val main_adapter = MainAdapter(mainItems,{id ->
            when(id) {
                1 -> {
                    val intent = Intent(this@MainActivity, ImcActivity::class.java)
                    startActivity(intent)
                }
                2 -> {
                    val intent = Intent(this@MainActivity, TmbActivity::class.java)
                    startActivity(intent)
                }
            }
        })

        rvMain = findViewById(R.id.rc_main)
        rvMain.adapter = main_adapter
        rvMain.layoutManager = GridLayoutManager(this, 2)
    }

    private inner class MainAdapter(
        private val items: List<MainItem>,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val item = items[position]
            holder.bind(item)
        }

        override fun getItemCount(): Int {
            return items.size
        }


        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(item: MainItem) {
                val img: ImageView = itemView.findViewById(R.id.item_image)
                val name: TextView = itemView.findViewById(R.id.item_text_name)
                val container: LinearLayout = itemView.findViewById(R.id.item_container)

                img.setImageResource(item.drawableId)
                name.setText(item.textStringId)

                // Use ContextCompat para pegar a cor real a partir do ID
                val colorValue = androidx.core.content.ContextCompat.getColor(itemView.context, item.color)
                container.setBackgroundColor(colorValue)

                container.setOnClickListener{
                    onItemClick.invoke(item.id)
                }
            }
        }
    }

}