package com.volokhinaleksey.kash.navigation.importexport

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.components.BackTopBar
import com.volokhinaleksey.kash.presentation.importexport.ImportErrorEvent
import com.volokhinaleksey.kash.presentation.importexport.ImportErrorUiState
import com.volokhinaleksey.kash.theme.JetBrainsMonoFontFamily
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.import_error_action_help
import kash.composeapp.generated.resources.import_error_action_retry
import kash.composeapp.generated.resources.import_error_details_format
import kash.composeapp.generated.resources.import_error_details_label
import kash.composeapp.generated.resources.import_error_subtitle
import kash.composeapp.generated.resources.import_error_title
import kash.composeapp.generated.resources.import_top_bar_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ImportErrorScreen(
    component: ImportErrorComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    val onEvent = remember(component) { component::onEvent }
    val onBack = remember(component) { component::onBackClicked }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg)
            .padding(top = contentPadding.calculateTopPadding()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = contentPadding.calculateBottomPadding() + 30.dp),
        ) {
            BackTopBar(
                title = stringResource(Res.string.import_top_bar_title),
                onBackClick = onBack,
                showNotifications = false,
            )

            ErrorBody(
                state = state,
                onRetry = { onEvent(ImportErrorEvent.RetryClicked) },
                onHelp = { onEvent(ImportErrorEvent.HelpClicked) },
            )
        }
    }
}

@Composable
private fun ErrorBody(
    state: ImportErrorUiState,
    onRetry: () -> Unit,
    onHelp: () -> Unit,
) {
    val c = Kash.colors
    val iconBg = if (c.isDark) Color(0x1FD27F73) else Color(0xFFF4DFDC)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.WarningAmber,
                contentDescription = null,
                tint = c.neg,
                modifier = Modifier.size(40.dp),
            )
        }

        Spacer(Modifier.height(22.dp))
        Text(
            text = stringResource(Res.string.import_error_title),
            color = c.text,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.7).sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.import_error_subtitle),
            color = c.sub,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 300.dp),
        )

        Spacer(Modifier.height(22.dp))
        DetailsBlock(state = state)

        Spacer(Modifier.height(22.dp))
        Column(
            modifier = Modifier.widthIn(max = 320.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PrimaryAction(
                text = stringResource(Res.string.import_error_action_retry),
                onClick = onRetry,
            )
            SecondaryAction(
                text = stringResource(Res.string.import_error_action_help),
                onClick = onHelp,
            )
        }
    }
}

@Composable
private fun DetailsBlock(state: ImportErrorUiState) {
    val c = Kash.colors
    val mono = JetBrainsMonoFontFamily()
    Column(
        modifier = Modifier
            .widthIn(max = 320.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(c.card)
            .border(1.dp, c.line, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        Text(
            text = stringResource(Res.string.import_error_details_label).uppercase(),
            color = c.fade,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(
                Res.string.import_error_details_format,
                state.fileName,
                state.errorCode,
                state.pages,
                state.sizeLabel,
            ),
            color = c.sub,
            fontFamily = mono,
            fontSize = 12.sp,
            lineHeight = 19.sp,
        )
    }
}

@Composable
private fun PrimaryAction(text: String, onClick: () -> Unit) {
    val c = Kash.colors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(c.accent)
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = c.accentInk,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun SecondaryAction(text: String, onClick: () -> Unit) {
    val c = Kash.colors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = c.sub,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}
