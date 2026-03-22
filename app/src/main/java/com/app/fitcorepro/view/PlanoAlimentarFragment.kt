package com.app.fitcorepro.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.fitcorepro.R

class RefeicaoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_refeicao, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnDietaSemana = view.findViewById<ImageButton>(R.id.btn_dieta_semana)
        val btnDietaDia = view.findViewById<ImageButton>(R.id.btn_dieta_dia)

        btnDietaSemana.setOnClickListener {
            findNavController().navigate(R.id.action_nav_diet_to_nav_diet_week)
        }

        btnDietaDia.setOnClickListener {
            findNavController().navigate(R.id.action_nav_diet_to_nav_diet_dia)
        }
    }
}