package com.volokhinaleksey.kash.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
            EmptyHomePrimaryAction(
                text = stringResource(Res.string.empty_home_action_add),
                onClick = onAddTransactionClick,
            )
            EmptyHomeOutlinedAction(
                text = stringResource(Res.string.empty_home_action_import),
                onClick = onImportStatementClick,
            )
        }
    }
}

@Composable
private fun EmptyHomePrimaryAction(
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(Kash.colors.accent)
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Kash.colors.accentInk,
            modifier = Modifier.size(16.dp),
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = text,
            color = Kash.colors.accentInk,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun EmptyHomeOutlinedAction(
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(13.dp))
            .border(
                border = BorderStroke(1.dp, Kash.colors.lineStrong),
                shape = RoundedCornerShape(13.dp),
            )
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.FileUpload,
            contentDescription = null,
            tint = Kash.colors.text,
            modifier = Modifier.size(16.dp),
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = text,
            color = Kash.colors.text,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
