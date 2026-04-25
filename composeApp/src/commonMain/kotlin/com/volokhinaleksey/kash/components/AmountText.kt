package com.volokhinaleksey.kash.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

@Composable
fun AmountText(
    amount: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    currencyColor: Color = Color.Unspecified,
    currencyWeight: FontWeight? = null,
    currencyScale: Float = 0.6f,
) {
    val splitIdx = amount.lastIndexOf(' ')
    val number: String
    val currency: String
    if (splitIdx > 0) {
        number = amount.substring(0, splitIdx)
        currency = amount.substring(splitIdx + 1)
    } else {
        number = amount
        currency = ""
    }

    val resolvedColor = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.onSurface
    val resolvedCurrencyColor = if (currencyColor != Color.Unspecified) currencyColor else resolvedColor

    val baseFontSize = style.fontSize.value.takeIf { !it.isNaN() && it > 0f } ?: 16f
    val currencySize = TextUnit(
        value = baseFontSize * currencyScale,
        type = TextUnitType.Sp,
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            text = number,
            style = style,
            color = resolvedColor,
        )
        if (currency.isNotEmpty()) {
            Text(
                text = " $currency",
                style = style.copy(
                    fontSize = currencySize,
                    fontWeight = currencyWeight ?: style.fontWeight,
                ),
                color = resolvedCurrencyColor,
            )
        }
    }
}
