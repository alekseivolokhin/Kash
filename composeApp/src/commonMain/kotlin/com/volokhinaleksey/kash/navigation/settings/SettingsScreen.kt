package com.volokhinaleksey.kash.navigation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.components.CurrencyPickerSheet
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.button.KashButton
import com.volokhinaleksey.kash.designsystem.button.KashButtonVariant
import com.volokhinaleksey.kash.designsystem.card.KashCard
import com.volokhinaleksey.kash.designsystem.chip.KashSegmentedControl
import com.volokhinaleksey.kash.designsystem.chip.KashSegmentItem
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import com.volokhinaleksey.kash.designsystem.topbar.KashLogoBadge
import com.volokhinaleksey.kash.designsystem.topbar.KashSectionTopBar
import com.volokhinaleksey.kash.presentation.settings.SettingsEvent
import com.volokhinaleksey.kash.presentation.settings.SettingsUiState
import com.volokhinaleksey.kash.presentation.settings.ThemeMode
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.nav_settings
import kash.composeapp.generated.resources.settings_accounts
import kash.composeapp.generated.resources.settings_app_version
import kash.composeapp.generated.resources.settings_appearance
import kash.composeapp.generated.resources.settings_appearance_dark
import kash.composeapp.generated.resources.settings_appearance_light
import kash.composeapp.generated.resources.settings_appearance_system
import kash.composeapp.generated.resources.settings_base_currency
import kash.composeapp.generated.resources.settings_exchange_rates
import kash.composeapp.generated.resources.settings_export_csv
import kash.composeapp.generated.resources.settings_import_transactions
import kash.composeapp.generated.resources.settings_personal_account
import kash.composeapp.generated.resources.settings_premium
import kash.composeapp.generated.resources.settings_section_about
import kash.composeapp.generated.resources.settings_section_data
import kash.composeapp.generated.resources.settings_section_finance
import kash.composeapp.generated.resources.settings_section_general
import kash.composeapp.generated.resources.settings_sign_out
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    component: SettingsComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    var showCurrencySheet by rememberSaveable { mutableStateOf(false) }

    SettingsContent(
        state = state,
        onEvent = { event ->
            if (event is SettingsEvent.BaseCurrencyClicked) {
                showCurrencySheet = true
            } else {
                component.onEvent(event)
            }
        },
        contentPadding = contentPadding,
    )

    if (showCurrencySheet) {
        CurrencyPickerSheet(
            selectedCode = state.baseCurrencyCode,
            onDismiss = { showCurrencySheet = false },
            onConfirm = { showCurrencySheet = false },
        )
    }
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit,
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
                .padding(bottom = contentPadding.calculateBottomPadding() + 24.dp),
        ) {
            KashSectionTopBar(title = stringResource(Res.string.nav_settings))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = KashDimens.ScreenHorizontalPadding, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp),
            ) {
                ProfileCard(state = state)

                SettingsSection(label = stringResource(Res.string.settings_section_finance)) {
                    NavigationRow(
                        icon = Icons.Outlined.Wallet,
                        label = stringResource(Res.string.settings_accounts),
                        trailingText = state.accountsCount.toString(),
                        onClick = { onEvent(SettingsEvent.AccountsClicked) },
                    )
                    HorizontalDivider(thickness = 1.dp, color = Kash.colors.line)
                    BaseCurrencyRow(
                        currencyCode = state.baseCurrencyCode,
                        currencySymbol = state.baseCurrencySymbol,
                        onClick = { onEvent(SettingsEvent.BaseCurrencyClicked) },
                    )
                    HorizontalDivider(thickness = 1.dp, color = Kash.colors.line)
                    NavigationRow(
                        icon = Icons.Outlined.BarChart,
                        label = stringResource(Res.string.settings_exchange_rates),
                        trailingText = state.sampleRate,
                        useMonoTrailing = true,
                        onClick = { onEvent(SettingsEvent.ExchangeRatesClicked) },
                    )
                }

                SettingsSection(label = stringResource(Res.string.settings_section_general)) {
                    AppearanceRow(
                        themeMode = state.themeMode,
                        onThemeChange = { onEvent(SettingsEvent.ThemeChanged(it)) },
                    )
                }

                SettingsSection(label = stringResource(Res.string.settings_section_data)) {
                    NavigationRow(
                        icon = Icons.Outlined.FileUpload,
                        label = stringResource(Res.string.settings_import_transactions),
                        onClick = { onEvent(SettingsEvent.ImportTransactionsClicked) },
                    )
                    HorizontalDivider(thickness = 1.dp, color = Kash.colors.line)
                    NavigationRow(
                        icon = Icons.Outlined.FileDownload,
                        label = stringResource(Res.string.settings_export_csv),
                        onClick = { onEvent(SettingsEvent.ExportCsvClicked) },
                    )
                }

                SettingsSection(label = stringResource(Res.string.settings_section_about)) {
                    AppVersionRow(version = state.appVersion)
                }

                KashButton(
                    text = stringResource(Res.string.settings_sign_out),
                    onClick = { onEvent(SettingsEvent.SignOutClicked) },
                    variant = KashButtonVariant.Secondary,
                )
            }
        }
    }
}

@Composable
private fun ProfileCard(state: SettingsUiState) {
    KashCard {
        Row(
            modifier = Modifier.padding(
                horizontal = KashDimens.RowHorizontalPadding,
                vertical = KashDimens.RowVerticalPadding,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            KashLogoBadge(size = 44.dp)
            Column(modifier = Modifier.weight(1f)) {
                KashSectionLabel(text = stringResource(Res.string.settings_personal_account))
                Spacer(Modifier.height(2.dp))
                Text(
                    text = state.userName,
                    color = Kash.colors.text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.3).sp,
                )
            }
            if (state.isPremium) {
                PremiumBadge()
            }
        }
    }
}

@Composable
private fun PremiumBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Kash.colors.accentSoft)
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        Text(
            text = stringResource(Res.string.settings_premium),
            color = Kash.colors.accentSoftInk,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.6.sp,
        )
    }
}

@Composable
private fun SettingsSection(
    label: String,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        KashSectionLabel(
            text = label,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
        )
        KashCard { content() }
    }
}

@Composable
private fun NavigationRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    trailingText: String? = null,
    useMonoTrailing: Boolean = false,
) {
    SettingsRowContainer(onClick = onClick) {
        RowIcon(icon = icon, contentDescription = label)
        RowLabel(text = label, modifier = Modifier.weight(1f))
        if (!trailingText.isNullOrEmpty()) {
            Text(
                text = trailingText,
                color = Kash.colors.fade,
                fontSize = if (useMonoTrailing) 12.sp else 13.sp,
                fontFamily = if (useMonoTrailing) com.volokhinaleksey.kash.theme.JetBrainsMonoFontFamily() else null,
            )
            Spacer(Modifier.width(8.dp))
        }
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Kash.colors.fade,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun BaseCurrencyRow(
    currencyCode: String,
    currencySymbol: String,
    onClick: () -> Unit,
) {
    SettingsRowContainer(onClick = onClick) {
        RowIcon(
            icon = Icons.Outlined.AccountBalanceWallet,
            contentDescription = stringResource(Res.string.settings_base_currency),
        )
        RowLabel(
            text = stringResource(Res.string.settings_base_currency),
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "$currencyCode $currencySymbol",
            color = Kash.colors.sub,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Kash.colors.fade,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun AppVersionRow(version: String) {
    SettingsRowContainer(onClick = null) {
        RowIcon(
            icon = Icons.Outlined.Info,
            contentDescription = stringResource(Res.string.settings_app_version),
        )
        RowLabel(
            text = stringResource(Res.string.settings_app_version),
            modifier = Modifier.weight(1f),
        )
        Text(
            text = version,
            color = Kash.colors.fade,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Composable
private fun AppearanceRow(
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = KashDimens.RowHorizontalPadding,
                vertical = KashDimens.RowVerticalPadding,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            RowIcon(
                icon = Icons.Outlined.Palette,
                contentDescription = stringResource(Res.string.settings_appearance),
            )
            RowLabel(text = stringResource(Res.string.settings_appearance))
        }
        Spacer(Modifier.height(12.dp))
        KashSegmentedControl(
            items = listOf(
                KashSegmentItem(ThemeMode.LIGHT, stringResource(Res.string.settings_appearance_light)),
                KashSegmentItem(ThemeMode.DARK, stringResource(Res.string.settings_appearance_dark)),
                KashSegmentItem(ThemeMode.SYSTEM, stringResource(Res.string.settings_appearance_system)),
            ),
            selected = themeMode,
            onSelected = onThemeChange,
            height = 40.dp,
            cornerRadius = 11.dp,
            itemRadius = 9.dp,
        )
    }
}

@Composable
private fun SettingsRowContainer(
    onClick: (() -> Unit)?,
    content: @Composable RowScope.() -> Unit,
) {
    val base = Modifier
        .fillMaxWidth()
        .let { if (onClick != null) it.clickable(onClick = onClick) else it }
        .padding(
            horizontal = KashDimens.RowHorizontalPadding,
            vertical = KashDimens.RowVerticalPadding,
        )
    Row(
        modifier = base,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

@Composable
private fun RowIcon(icon: ImageVector, contentDescription: String?) {
    val isDark = Kash.colors.isDark
    val bg = if (isDark) Kash.colors.cardAlt else Kash.colors.chipBg
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(bg),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Kash.colors.sub,
            modifier = Modifier.size(17.dp),
        )
    }
}

@Composable
private fun RowLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = Kash.colors.text,
        fontSize = 14.5.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun SettingsContentPreview() {
    KashTheme {
        SettingsContent(
            state = SettingsUiState(
                userName = "Alex Mercer",
                userInitials = "AM",
                isPremium = true,
                baseCurrencyCode = "KZT",
                baseCurrencySymbol = "₸",
                themeMode = ThemeMode.SYSTEM,
                appVersion = "1.0.0",
                accountsCount = 5,
                sampleRate = "USD 478.5",
            ),
            onEvent = {},
        )
    }
}
