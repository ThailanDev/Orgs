package br.com.alura.orgs.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.util.*

class Converters {

    // o banco irá nos enviar um valor bigDecimal ao qual salvou como double
    @TypeConverter
    fun deDouble(valor: Double?): BigDecimal {
        return valor?.let { BigDecimal(valor.toString()) } ?: BigDecimal.ZERO
    }

    // iremos enviar um bigDecimal em formato double para o banco
    @TypeConverter
    fun bigDecimalParaDouble(valor: BigDecimal?): Double? {
        return valor?.let { valor.toDouble() }
    }
}