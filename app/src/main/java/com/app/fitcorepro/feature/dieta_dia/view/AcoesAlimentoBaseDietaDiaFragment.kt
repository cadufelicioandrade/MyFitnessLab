package com.app.fitcorepro.feature.dieta_dia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import com.app.fitcorepro.R
import com.app.fitcorepro.feature.dieta_dia.contract.BaseDietaDiaContract
import com.app.fitcorepro.feature.dieta_dia.model.AlimentoDietaDia
import com.app.fitcorepro.feature.dieta_dia.presentation.AlimentoBaseDietDiaPresenter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AcoesAlimentoBaseDietaDiaFragment : Fragment(), BaseDietaDiaContract {


    private lateinit var headerAlimento: TextView
    private lateinit var titleNovoAlimento: TextView
    private lateinit var layoutNovoGramaAlimento: TextInputLayout
    private lateinit var editNovoGramaAlimento: TextInputEditText
    private lateinit var btnAcaoAlimentoBase: Button
    private lateinit var toggleButton: ImageButton
    private lateinit var valorKcal: TextView
    private lateinit var valorCarb: TextView
    private lateinit var valorProt: TextView
    private lateinit var valorGord: TextView
    private lateinit var valorFib: TextView
    private lateinit var groupVisibility: Group
    private var visibilityToggleTable = true
    private lateinit var alimentoId: String
    private lateinit var nomeAlimento: String
    private var quantidadeGramas = 0
    private lateinit var kcal: String
    private lateinit var proteina: String
    private lateinit var carboidrato: String
    private lateinit var fibra: String
    private lateinit var gordura: String
    private lateinit var refeicaoId: String
    private lateinit var refeicaoTitulo: String
    private lateinit var dietaDiaId: String
    private lateinit var alimentoPresenter: AlimentoBaseDietDiaPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alimentoPresenter = AlimentoBaseDietDiaPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_acoes_alimento_base_dieta_dia, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GetElementsByView(view)
        GetArgs()
        setupUI()

        btnAcaoAlimentoBase.setOnClickListener {
            var gramas = editNovoGramaAlimento.text.toString()

            if (gramas.isBlank()) {
                layoutNovoGramaAlimento.error = "Obrigatório"
                return@setOnClickListener
            }
            layoutNovoGramaAlimento.error = null


            val fator = gramas.toInt() / 100.0

            if (alimentoId.isBlank() || alimentoId.isEmpty()) {
                val alimentoBase = AlimentoDietaDia(
                    "",
                    nomeAlimento,
                    refeicaoId,
                    gramas.toInt(),
                    (kcal.toDouble() * fator),
                    (carboidrato.toDouble() * fator),
                    (proteina.toDouble() * fator),
                    (gordura.toDouble() * fator),
                    (fibra.toDouble() * fator)
                )

                AddAlimentoBase(alimentoBase)
            } else {
                val alimentoBase = AlimentoDietaDia(
                    alimentoId,
                    nomeAlimento,
                    refeicaoId,
                    gramas.toInt(),
                    (kcal.toDouble() * fator),
                    (carboidrato.toDouble() * fator),
                    (proteina.toDouble() * fator),
                    (gordura.toDouble() * fator),
                    (fibra.toDouble() * fator)
                )

                EditAlimentoBase(alimentoBase)
            }

        }

        toggleButton.setOnClickListener {
            visibilityToggleTable = !visibilityToggleTable
            visibilityTable(visibilityToggleTable)
        }


    }

    private fun GetElementsByView(view: View) {
        headerAlimento = view.findViewById(R.id.header_title)
        titleNovoAlimento = view.findViewById(R.id.title_novo_alimento)
        layoutNovoGramaAlimento = view.findViewById(R.id.layout_novo_grama_alimento)
        editNovoGramaAlimento = view.findViewById(R.id.edit_novo_grama_alimento)
        btnAcaoAlimentoBase = view.findViewById(R.id.btn_acao_alimento_refeicao_ao_dia)
        toggleButton = view.findViewById(R.id.btn_toggle_visibility_table)
        valorKcal = view.findViewById(R.id.valor_kcal)
        valorCarb = view.findViewById(R.id.valor_carb)
        valorProt = view.findViewById(R.id.valor_prot)
        valorGord = view.findViewById(R.id.valor_gord)
        valorFib = view.findViewById(R.id.valor_fib)
        toggleButton = view.findViewById(R.id.btn_toggle_visibility_table)
        groupVisibility = view.findViewById(R.id.group_tabela_conteudo)

    }

    private fun setupUI() {
        if (alimentoId.isBlank() || alimentoId.isEmpty()) {
            headerAlimento.text = " Adicione seu Alimento"
            btnAcaoAlimentoBase.text = "Adicionar Alimento ao dia"
        } else {
            headerAlimento.text = "Editar seu Alimento"
            btnAcaoAlimentoBase.text = "Salvar Alimento"
            editNovoGramaAlimento.setText(quantidadeGramas.toString())
        }
    }

    private fun GetArgs() {
        val args = AcoesAlimentoBaseDietaDiaFragmentArgs.fromBundle(requireArguments())
        alimentoId = args.alimentoId
        nomeAlimento = args.alimentoNome
        quantidadeGramas = args.quantidadeGramas
        kcal = args.alimentoCalorias.toString()
        carboidrato = args.alimentoCarboidratos.toString()
        proteina = args.alimentoProteinas.toString()
        gordura = args.alimentoGorduras.toString()
        fibra = args.alimentoFibras.toString()
        refeicaoId = args.refeicaoId
        refeicaoTitulo = args.refeicaoTitulo
        dietaDiaId = args.dietaDiaId



        titleNovoAlimento.text = nomeAlimento
        valorKcal.text = "${kcal} kcal"
        valorProt.text = "${proteina} g"
        valorCarb.text = "${carboidrato} g"
        valorGord.text = "${gordura} g"
        valorFib.text = "${fibra} g"
    }

    private fun visibilityTable(visibilityTable: Boolean) {
        if (visibilityTable) groupVisibility.visibility = View.VISIBLE
        else groupVisibility.visibility = View.GONE
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun onMessageSuccess(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    fun AddAlimentoBase(alimentoDietaDia: AlimentoDietaDia) {

        alimentoPresenter.addAlimento(alimentoDietaDia)
    }

    fun EditAlimentoBase(alimentoDietaDia: AlimentoDietaDia) {
        alimentoPresenter.editAlimento(alimentoDietaDia)
    }

    override fun onError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}