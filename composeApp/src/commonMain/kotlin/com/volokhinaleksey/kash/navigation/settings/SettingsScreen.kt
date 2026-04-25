package com.volokhinaleksey.kash.navigation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
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
import com.volokhinaleksey.kash.components.CurrencyPickerSheet
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.presentation.settings.SettingsEvent
import com.volokhinaleksey.kash.presentation.settings.SettingsUiState
import com.volokhinaleksey.kash.presentation.settings.ThemeMode
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.app_name
import kash.composeapp.generated.resources.nav_settings
import kash.composeapp.generated.resources.notifications
import kash.composeapp.generated.resources.settings_app_version
import kash.composeapp.generated.resources.settings_appearance
import kash.composeapp.generated.resources.settings_appearance_dark
import kash.composeapp.generated.resources.settings_appearance_light
import kash.composeapp.generated.resources.settings_appearance_system
import kash.composeapp.generated.resources.settings_base_currency
import kash.composeapp.generated.resources.settings_export_csv
import kash.composeapp.generated.resources.settings_import_transactions
import kash.composeapp.generated.resources.settings_personal_account
import kash.composeapp.generated.resources.settings_premium
import kash.composeapp.generated.resources.settings_section_about
import kash.composeapp.generated.resources.settings_section_data
import kash.composeapp.generated.resources.settings_section_general
import kash.composeapp.generated.resources.settings_sign_out
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    component: SettingsComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    var showCurrencySheet by rememberSaveable { mutableStateOf(false) }

    SettingsScreenContent(
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
private fun SettingsScreenContent(
    state: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit,
    contentPadding: PaddingValues,
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
            SettingsTopBar(title = stringResource(Res.string.nav_settings))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp),
            ) {
                ProfileCard(state = state)

                SettingsSection(label = stringResource(Res.string.settings_section_general)) {
                    BaseCurrencyRow(
                        currencyCode = state.baseCurrencyCode,
                        currencySymbol = state.baseCurrencySymbol,
                        onClick = { onEvent(SettingsEvent.BaseCurrencyClicked) },
                    )
                    RowDivider()
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
                    RowDivider()
                    NavigationRow(
                        icon = Icons.Outlined.FileDownload,
                        label = stringResource(Res.string.settings_export_csv),
                        onClick = { onEvent(SettingsEvent.ExportCsvClicked) },
                    )
                }

                SettingsSection(label = stringResource(Res.string.settings_section_about)) {
                    AppVersionRow(version = state.appVersion)
                }

                SignOutButton(onClick = { onEvent(SettingsEvent.SignOutClicked) })
            }
        }
    }
}

@Composable
private fun SettingsTopBar(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            KashLogo()
            NotificationButton()
        }
        Text(
            text = title,
            color = Kash.colors.text,
            fontSize = 32.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-1.2).sp,
            modifier = Modifier.padding(top = 14.dp),
        )
    }
}

@Composable
private fun KashLogo() {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Kash.colors.accent),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.app_name).take(1),
            color = Kash.colors.accentInk,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp,
        )
    }
}

@Composable
private fun NotificationButton() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Kash.colors.line, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = stringResource(Res.string.notifications),
            tint = Kash.colors.sub,
            modifier = Modifier.size(18.dp),
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 9.dp, end = 10.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(Kash.colors.accent),
        )
    }
}

@Composable
private fun ProfileCard(state: SettingsUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Avatar(initials = state.userInitials)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.settings_personal_account).uppercase(),
                color = Kash.colors.sub,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.4.sp,
            )
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

@Composable
private fun Avatar(initials: String) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Kash.colors.accent),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            color = Kash.colors.accentInk,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.5).sp,
        )
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
        Text(
            text = label.uppercase(),
            color = Kash.colors.sub,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.4.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Kash.colors.card)
                .border(1.dp, Kash.colors.line, RoundedCornerShape(16.dp)),
        ) {
            content()
        }
    }
}

@Composable
private fun RowDivider() {
    HorizontalDivider(thickness = 1.dp, color = Kash.colors.line)
}

@Composable
private fun NavigationRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    SettingsRowContainer(onClick = onClick) {
        RowIcon(icon = icon, contentDescription = label)
        RowLabel(text = label, modifier = Modifier.weight(1f))
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
            .padding(horizontal = 16.dp, vertical = 14.dp),
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
        ThemeSegmentedControl(
            themeMode = themeMode,
            onThemeChange = onThemeChange,
        )
    }
}

@Composable
private fun ThemeSegmentedControl(
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
) {
    val isDark = Kash.colors.isDark
    val trackColor = if (isDark) Kash.colors.cardAlt else Kash.colors.chipBg

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(11.dp))
            .background(trackColor)
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        ThemeOption(
            label = stringResource(Res.string.settings_appearance_light),
            selected = themeMode == ThemeMode.LIGHT,
            modifier = Modifier.weight(1f),
            onClick = { onThemeChange(ThemeMode.LIGHT) },
        )
        ThemeOption(
            label = stringResource(Res.string.settings_appearance_dark),
            selected = themeMode == ThemeMode.DARK,
            modifier = Modifier.weight(1f),
            onClick = { onThemeChange(ThemeMode.DARK) },
        )
        ThemeOption(
            label = stringResource(Res.string.settings_appearance_system),
            selected = themeMode == ThemeMode.SYSTEM,
            modifier = Modifier.weight(1f),
            onClick = { onThemeChange(ThemeMode.SYSTEM) },
        )
    }
}

@Composable
private fun ThemeOption(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(9.dp)
    val selectedBg = if (Kash.colors.isDark) Kash.colors.card else Kash.colors.bg
    Box(
        modifier = modifier
            .clip(shape)
            .background(if (selected) selectedBg else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (selected) Kash.colors.text else Kash.colors.sub,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
        )
    }
}

@Composable
private fun SettingsRowContainer(
    onClick: (() -> Unit)?,
    content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit,
) {
    val base = Modifier
        .fillMaxWidth()
        .let { if (onClick != null) it.clickable(onClick = onClick) else it }
        .padding(horizontal = 16.dp, vertical = 14.dp)
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

@Composable
private fun SignOutButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Kash.colors.line, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.settings_sign_out),
            color = Kash.colors.neg,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
    }
}
