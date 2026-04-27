package com.volokhinaleksey.kash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.button.KashButton
import com.volokhinaleksey.kash.designsystem.button.KashButtonVariant
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.empty_home_action_add
import kash.composeapp.generated.resources.empty_home_action_import
import kash.composeapp.generated.resources.empty_home_subtitle
import kash.composeapp.generated.resources.empty_home_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmptyHomeContent(
    onAddTransactionClick: () -> Unit,
    onImportStatementClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Kash.colors.bg)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Kash.colors.accentSoft),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.CreditCard,
                contentDescription = null,
                tint = Kash.colors.accentSoftInk,
                modifier = Modifier.size(40.dp),
            )
        }

        Spacer(Modifier.size(22.dp))

        Text(
            text = stringResource(Res.string.empty_home_title),
            color = Kash.colors.text,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.7).sp,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.size(8.dp))

        Text(
            text = stringResource(Res.string.empty_home_subtitle),
            color = Kash.colors.sub,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 280.dp),
        )

        Spacer(Modifier.size(24.dp))

        Column(
            modifier = Modifier.widthIn(max = 280.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            KashButton(
                text = stringResource(Res.string.empty_home_action_add),
                onClick = onAddTransactionClick,
                leadingIcon = Icons.Default.Add,
            )
            KashButton(
                text = stringResource(Res.string.empty_home_action_import),
                onClick = onImportStatementClick,
                variant = KashButtonVariant.Secondary,
                leadingIcon = Icons.Outlined.FileUpload,
            )
        }
    }
}
