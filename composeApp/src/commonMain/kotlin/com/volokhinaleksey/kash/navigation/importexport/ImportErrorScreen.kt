package com.volokhinaleksey.kash.navigation.importexport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.button.KashButton
import com.volokhinaleksey.kash.designsystem.button.KashButtonVariant
import com.volokhinaleksey.kash.designsystem.card.KashCard
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import com.volokhinaleksey.kash.designsystem.topbar.KashBackTopBar
import com.volokhinaleksey.kash.presentation.importexport.ImportErrorEvent
import com.volokhinaleksey.kash.presentation.importexport.ImportErrorUiState
import com.volokhinaleksey.kash.theme.JetBrainsMonoFontFamily
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.import_error_action_help
import kash.composeapp.generated.resources.import_error_action_retry
import kash.composeapp.generated.resources.import_error_details_format
import kash.composeapp.generated.resources.import_error_details_label
import kash.composeapp.generated.resources.import_error_subtitle
import kash.composeapp.generated.resources.import_error_title
import kash.composeapp.generated.resources.import_top_bar_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ImportErrorScreen(
    component: ImportErrorComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    ImportErrorContent(
        state = state,
        onEvent = component::onEvent,
        onBackClick = component::onBackClicked,
        contentPadding = contentPadding,
    )
}

@Composable
private fun ImportErrorContent(
    state: ImportErrorUiState,
    onEvent: (ImportErrorEvent) -> Unit,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
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
            KashBackTopBar(
                title = stringResource(Res.string.import_top_bar_title),
                onBackClick = onBackClick,
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
    val iconBg = if (Kash.colors.isDark) Color(0x1FD27F73) else Color(0xFFF4DFDC)

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
                tint = Kash.colors.neg,
                modifier = Modifier.size(40.dp),
            )
        }

        Spacer(Modifier.size(22.dp))
        Text(
            text = stringResource(Res.string.import_error_title),
            color = Kash.colors.text,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.7).sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = stringResource(Res.string.import_error_subtitle),
            color = Kash.colors.sub,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 300.dp),
        )

        Spacer(Modifier.size(22.dp))
        DetailsBlock(state = state)

        Spacer(Modifier.size(22.dp))
        Column(
            modifier = Modifier.widthIn(max = 320.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            KashButton(
                text = stringResource(Res.string.import_error_action_retry),
                onClick = onRetry,
            )
            KashButton(
                text = stringResource(Res.string.import_error_action_help),
                onClick = onHelp,
                variant = KashButtonVariant.Tertiary,
            )
        }
    }
}

@Composable
private fun DetailsBlock(state: ImportErrorUiState) {
    KashCard(
        modifier = Modifier.widthIn(max = 320.dp),
        radius = 12.dp,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
        ) {
            KashSectionLabel(text = stringResource(Res.string.import_error_details_label))
            Spacer(Modifier.size(4.dp))
            Text(
                text = stringResource(
                    Res.string.import_error_details_format,
                    state.fileName,
                    state.errorCode,
                    state.pages,
                    state.sizeLabel,
                ),
                color = Kash.colors.sub,
                fontFamily = JetBrainsMonoFontFamily(),
                fontSize = 12.sp,
                lineHeight = 19.sp,
            )
        }
    }
}

@Preview
@Composable
private fun ImportErrorContentPreview() {
    KashTheme {
        ImportErrorContent(
            state = ImportErrorUiState(
                fileName = "operations.csv",
                errorCode = "E_PARSE_FAILED",
                pages = 0,
                sizeLabel = "412 KB",
            ),
            onEvent = {},
            onBackClick = {},
        )
    }
}
