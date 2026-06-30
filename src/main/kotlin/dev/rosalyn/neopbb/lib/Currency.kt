package dev.rosalyn.neopbb.lib

import java.text.NumberFormat

fun formatCurrency(amount: Float): String {
    return if (amount.isInfinite())
            "$${if (amount < 0) "-" else ""}∞"
        else NumberFormat.getCurrencyInstance().format(amount)
}