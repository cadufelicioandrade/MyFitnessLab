package com.app.fitcorepro.feature.plano_semanal.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.fitcorepro.R
import com.app.fitcorepro.feature.plano_semanal.contract.AlimentoContract
import com.app.fitcorepro.feature.plano_semanal.model.AlimentoPlanoSemanal
import com.app.fitcorepro.feature.plano_semanal.presentation.AlimentoPlanoSemPresenter
import com.google.android.material.textfield.TextInputLayout

class EditaAlimentoPlanoSemFragment : Fragment(), AlimentoContract {

    private lateinit var txtNomeAlimento: EditText
    private lateinit var txtGrama: EditText
    private lateinit var btnSalvarAlimento: Button
    private lateinit var presenter: AlimentoPlanoSemPresenter
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AlimentoPlanoSemPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edita_alimento_plano, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val alimentoId = arguments?.getString("alimentoId").toString()
        val alimentoNome = arguments?.getString("alimentoNome").toString()
        val alimentoGrama = arguments?.getInt("alimentoGrama").toString()
        var refeicaoId = arguments?.getString("refeicaoId").toString()

        val layout_novo_alimento_nome =
            view.findViewById<TextInputLayout>(R.id.layout_novo_alimento_nome)
        val layout_novo_grama_alimento =
            view.findViewById<TextInputLayout>(R.id.layout_novo_grama_alimento)

        txtNomeAlimento = view.findViewById(R.id.edit_novo_alimento_nome)
        txtGrama = view.findViewById(R.id.add_novo_grama_alimento)
        btnSalvarAlimento = view.findViewById(R.id.btn_save_alimento)
        progress = view.findViewById(R.id.progress_bar_dieta)

        txtNomeAlimento.setText(alimentoNome)
        txtGrama.setText(alimentoGrama)

        btnSalvarAlimento.setOnClickListener {
            val nomeAlimento = txtNomeAlimento.text.toString()
            val grama = txtGrama.text.toString()

            if (nomeAlimento.isEmpty() || grama.isEmpty()) {
                if (nomeAlimento.isEmpty()) layout_novo_alimento_nome.error = "Obrigatório"
                if (grama.isEmpty()) layout_novo_grama_alimento.error = "Obrigatório"
                return@setOnClickListener
            }

            layout_novo_alimento_nome.error = null
            layout_novo_grama_alimento.error = null

            presenter.editarAlimento(
                AlimentoPlanoSemanal(
                    alimentoId,
                    nomeAlimento,
                    grama.toInt(),
                    refeicaoId
                )
            )

            findNavController().navigate(R.id.action_nav_edit_aliment_to_nav_diet_week)
        }

    }

    override fun showLoading() {
        progress.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress.visibility = View.GONE
    }

    override fun showMessageSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}