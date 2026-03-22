package com.app.fitcorepro.feature.plano_semanal.contract

import com.app.fitcorepro.feature.plano_semanal.model.PlanoSemanal

interface PlanoSemanalContract : BaseContrac {
    fun showPlanoSemanal(planoSemanal: PlanoSemanal)
}