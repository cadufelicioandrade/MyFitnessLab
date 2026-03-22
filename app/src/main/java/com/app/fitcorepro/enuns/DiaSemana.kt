package com.app.fitcorepro.enuns

enum class DiaSemana(val codigo: Int, val descricao: String) {

    SEGUNDA(1, "Segunda"),
    TERCA(2, "Terça"),
    QUARTA(3, "Quarta"),
    QUINTA(4, "Quinta"),
    SEXTA(5, "Sexta"),
    SABADO(6, "Sábado"),
    DOMINGO(7, "Domingo");

    companion object {

        fun fromDescricao(descricao: String): DiaSemana? {
            return entries.find {
                it.descricao.equals(descricao, ignoreCase = true)
            }
        }

        fun fromCodigo(codigo: Int): DiaSemana? {
            return entries.find { it.codigo == codigo }
        }
    }

}