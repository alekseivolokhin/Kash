package com.volokhinaleksey.kash.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.theme.Kash

@Composable
fun AmountInputSection(
    label: String,
    amount: String,
    currencySymbol: String,
    onAmountChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.5.sp),
            color = Kash.colors.sub,
        )
        Spacer(Modifier.height(10.dp))

        val numberStyle = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 56.sp,
            lineHeight = 64.sp,
            letterSpacing = (-1).sp,
            color = Kash.colors.text,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.width(((amount.ifEmpty { "0" }).length * 32).dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (amount.isEmpty()) {
                    Text(
                        text = "0",
                        style = numberStyle.copy(color = Kash.colors.fade),
                    )
                }
                BasicTextField(
                    value = amount,
                    onValueChange = onAmountChange,
                    singleLine = true,
                    textStyle = numberStyle,
                    cursorBrush = SolidColor(Kash.colors.text),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = currencySymbol,
                style = numberStyle.copy(color = Kash.colors.accent),
            )
        }
    }
}
