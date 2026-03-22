package com.app.fitcorepro.feature.dieta_dia.presentation.data

import com.app.fitcorepro.feature.dieta_dia.model.AlimentoDietaDia
import com.app.fitcorepro.feature.dieta_dia.model.DietaDia
import com.app.fitcorepro.feature.dieta_dia.model.RefeicaoDietaDia
import com.app.fitcorepro.feature.dieta_dia.presentation.BaseDietaDiaCallback
import java.time.LocalDate

class DietaDiaDataSource {

    fun getDietaDiaByData(data: LocalDate, callback: BaseDietaDiaCallback): DietaDia {
        return createMockDietaDia()
    }

    private fun createMockDietaDia(): DietaDia {
        // ... (seu código de mock não precisa mudar)
        val dietaDiaId = "dieta-hoje-123"
        val refeicaoCafeId = "ref-cafe-01"
        val refeicaoAlmocoId = "ref-almoco-02"

        val alimentosCafe = listOf(
            AlimentoDietaDia(
                id = "alimento-1",
                nome = "Ovo Cozido",
                refeicaoDietaDiaId = refeicaoCafeId,
                quantidadeGrama = 100,
                calorias = 155.0,
                carboidratos = 1.1,
                proteinas = 13.0,
                gorduras = 11.0,
                fibras = 0.0
            ),
            AlimentoDietaDia(
                id = "alimento-2",
                nome = "Pão Integral",
                refeicaoDietaDiaId = refeicaoCafeId,
                quantidadeGrama = 50,
                calorias = 130.0,
                carboidratos = 25.0,
                proteinas = 7.0,
                gorduras = 2.0,
                fibras = 4.0
            )
        )

        val alimentosAlmoco = listOf(
            AlimentoDietaDia(
                id = "alimento-3",
                nome = "Filé de Frango Grelhado",
                refeicaoDietaDiaId = refeicaoAlmocoId,
                quantidadeGrama = 150,
                calorias = 165.0,
                carboidratos = 0.0,
                proteinas = 35.0,
                gorduras = 3.6,
                fibras = 0.0
            ),
            AlimentoDietaDia(
                id = "alimento-4",
                nome = "Arroz Branco",
                refeicaoDietaDiaId = refeicaoAlmocoId,
                quantidadeGrama = 100,
                calorias = 130.0,
                carboidratos = 28.0,
                proteinas = 2.7,
                gorduras = 0.3,
                fibras = 0.4
            )
        )

        val cafeDaManha = RefeicaoDietaDia(
            id = refeicaoCafeId,
            titulo = "Café da Manhã",
            ordem = 1,
            dietaDiaId = dietaDiaId,
            alimentos = alimentosCafe
        )
        val almoco = RefeicaoDietaDia(
            id = refeicaoAlmocoId,
            titulo = "Almoço",
            ordem = 2,
            dietaDiaId = dietaDiaId,
            alimentos = alimentosAlmoco
        )

        return DietaDia(
            id = dietaDiaId,
            usuarioId = "user-mock-456",
            dataDieta = LocalDate.now(),
            refeicoes = listOf(cafeDaManha, almoco)
        )
    }
}