package com.app.fitcorepro.feature.dieta_dia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.app.fitcorepro.R
import com.app.fitcorepro.feature.dieta_dia.viewmodel.DietaDiaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddRefeicaoDietaDiaDialogFragment : BottomSheetDialogFragment() {

    private val sharedViewModel: DietaDiaViewModel by activityViewModels()
    private lateinit var txtNovaRefeicaoNome: TextInputEditText
    private lateinit var layout_nova_refeicao_nome: TextInputLayout
    private lateinit var btnAddRefeicao: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_add_refeicao_dieta_dia, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout_nova_refeicao_nome = view.findViewById(R.id.layout_nova_refeicao_nome)
        txtNovaRefeicaoNome = view.findViewById(R.id.edit_nova_refeicao_nome)
        btnAddRefeicao = view.findViewById(R.id.btn_add_refeicao_ao_dia)

        btnAddRefeicao.setOnClickListener {

            val txtNovaRefeicao = txtNovaRefeicaoNome.text.toString()

            if (txtNovaRefeicao.isNullOrBlank()) {
                layout_nova_refeicao_nome.error = "Obrigatório"
                return@setOnClickListener
            }
            layout_nova_refeicao_nome.error = null
            sharedViewModel.noNovaRefeicaoAdicionada(txtNovaRefeicao)
            dismiss()
        }
    }
}