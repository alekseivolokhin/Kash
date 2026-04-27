package com.volokhinaleksey.kash.navigation.importexport

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.card.KashCard
import com.volokhinaleksey.kash.designsystem.feedback.KashHintBanner
import com.volokhinaleksey.kash.designsystem.feedback.KashScreenSubtitle
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import com.volokhinaleksey.kash.designsystem.topbar.KashBackTopBar
import com.volokhinaleksey.kash.presentation.importexport.BankTint
import com.volokhinaleksey.kash.presentation.importexport.ImportPickEvent
import com.volokhinaleksey.kash.presentation.importexport.ImportPickUiState
import com.volokhinaleksey.kash.presentation.importexport.SupportedBank
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.import_choose_file
import kash.composeapp.generated.resources.import_drop_hint
import kash.composeapp.generated.resources.import_other_bank_hint
import kash.composeapp.generated.resources.import_screen_subtitle
import kash.composeapp.generated.resources.import_screen_title
import kash.composeapp.generated.resources.import_supported_banks
import kash.composeapp.generated.resources.import_top_bar_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ImportPickScreen(
    component: ImportPickComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    ImportPickContent(
        state = state,
        onEvent = component::onEvent,
        onBackClick = component::onBackClicked,
        contentPadding = contentPadding,
    )
}

@Composable
private fun ImportPickContent(
    state: ImportPickUiState,
    onEvent: (ImportPickEvent) -> Unit,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = contentPadding.calculateTopPadding()),
        ) {
            KashBackTopBar(
                title = stringResource(Res.string.import_top_bar_title),
                onBackClick = onBackClick,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = KashDimens.ScreenHorizontalPadding)
                    .padding(top = 16.dp, bottom = 30.dp + contentPadding.calculateBottomPadding()),
            ) {
                Text(
                    text = stringResource(Res.string.import_screen_title),
                    color = Kash.colors.text,
                    fontSize = 28.sp,
                    lineHeight = 31.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-1).sp,
                )
                Spacer(Modifier.height(8.dp))
                KashScreenSubtitle(text = stringResource(Res.string.import_screen_subtitle))

                Spacer(Modifier.height(22.dp))
                DropArea(onClick = { onEvent(ImportPickEvent.PickFileClicked) })

                Spacer(Modifier.height(22.dp))
                KashSectionLabel(text = stringResource(Res.string.import_supported_banks))
                Spacer(Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.supportedBanks.forEach { bank ->
                        BankRow(
                            bank = bank,
                            onClick = { onEvent(ImportPickEvent.BankClicked(bank.code)) },
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))
                KashHintBanner(text = stringResource(Res.string.import_other_bank_hint))
            }
        }
    }
}

@Composable
private fun DropArea(onClick: () -> Unit) {
    val isDark = Kash.colors.isDark
    val baseTint = if (isDark) Color(0x05F1ECE0) else Color(0x051B1F1A)
    val dashColor = Kash.colors.lineStrong

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(baseTint)
            .dashedBorder(color = dashColor, strokeWidth = 1.5.dp, cornerRadius = 18.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Kash.colors.accentSoft),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.FileUpload,
                contentDescription = null,
                tint = Kash.colors.accentSoftInk,
                modifier = Modifier.size(22.dp),
            )
        }
        Text(
            text = stringResource(Res.string.import_choose_file),
            color = Kash.colors.text,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = stringResource(Res.string.import_drop_hint),
            color = Kash.colors.fade,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun BankRow(bank: SupportedBank, onClick: () -> Unit) {
    val tint = bankTintColors(bank.tint)
    KashCard(
        onClick = onClick,
        radius = 14.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(tint.bg),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = bank.code,
                    color = tint.fg,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bank.name,
                    color = Kash.colors.text,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(1.dp))
                Text(
                    text = bank.sourceLabel,
                    color = Kash.colors.sub,
                    fontSize = 12.sp,
                )
            }
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = Kash.colors.fade,
                modifier = Modifier.size(15.dp),
            )
        }
    }
}

private data class BankTintColors(val bg: Color, val fg: Color)

@Composable
private fun bankTintColors(tint: BankTint): BankTintColors {
    val isDark = Kash.colors.isDark
    return when (tint) {
        BankTint.Kaspi ->
            if (isDark) BankTintColors(Color(0x29C46C60), Color(0xFFD89E96))
            else BankTintColors(Color(0xFFF4DFDC), Color(0xFF7A3A30))
    }
}

private fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp,
    cornerRadius: Dp,
    dashOn: Dp = 6.dp,
    dashOff: Dp = 4.dp,
): Modifier = this.drawBehind {
    val strokePx = strokeWidth.toPx()
    val cornerPx = cornerRadius.toPx()
    val effect = PathEffect.dashPathEffect(floatArrayOf(dashOn.toPx(), dashOff.toPx()), 0f)
    val inset = strokePx / 2f
    drawRoundRect(
        color = color,
        topLeft = Offset(inset, inset),
        size = Size(size.width - strokePx, size.height - strokePx),
        cornerRadius = CornerRadius(cornerPx - inset, cornerPx - inset),
        style = Stroke(width = strokePx, pathEffect = effect),
    )
}

@Preview
@Composable
private fun ImportPickContentPreview() {
    KashTheme {
        ImportPickContent(
            state = ImportPickUiState(
                supportedBanks = listOf(
                    SupportedBank("KSP", "Kaspi", "CSV · auto-detect", BankTint.Kaspi),
                ),
            ),
            onEvent = {},
            onBackClick = {},
        )
    }
}
