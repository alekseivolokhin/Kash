package com.volokhinaleksey.kash.navigation.transfer

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.bank.Bank
import com.volokhinaleksey.kash.designsystem.bank.KashAccountCardRow
import com.volokhinaleksey.kash.designsystem.button.KashButton
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import com.volokhinaleksey.kash.designsystem.topbar.KashBackTopBar
import com.volokhinaleksey.kash.presentation.accounts.Account
import com.volokhinaleksey.kash.presentation.accounts.AccountType
import com.volokhinaleksey.kash.presentation.accounts.TransferEvent
import com.volokhinaleksey.kash.presentation.accounts.TransferUiState
import com.volokhinaleksey.kash.theme.JetBrainsMonoFontFamily
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.transfer_action_save
import kash.composeapp.generated.resources.transfer_field_comment
import kash.composeapp.generated.resources.transfer_from
import kash.composeapp.generated.resources.transfer_rate_auto_suffix
import kash.composeapp.generated.resources.transfer_rate_label_format
import kash.composeapp.generated.resources.transfer_rate_override
import kash.composeapp.generated.resources.transfer_they_receive
import kash.composeapp.generated.resources.transfer_title
import kash.composeapp.generated.resources.transfer_to
import kash.composeapp.generated.resources.transfer_you_send
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TransferScreen(
    component: TransferComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    TransferContent(
        state = state,
        onBackClick = { component.onEvent(TransferEvent.BackClicked) },
        onFromClick = { component.onEvent(TransferEvent.FromClicked) },
        onToClick = { component.onEvent(TransferEvent.ToClicked) },
        onRateOverrideClick = { component.onEvent(TransferEvent.RateOverrideClicked) },
        onSaveClick = { component.onEvent(TransferEvent.SaveClicked) },
        contentPadding = contentPadding,
    )
}

@Composable
private fun TransferContent(
    state: TransferUiState,
    onBackClick: () -> Unit,
    onFromClick: () -> Unit,
    onToClick: () -> Unit,
    onRateOverrideClick: () -> Unit,
    onSaveClick: () -> Unit,
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
                .padding(bottom = contentPadding.calculateBottomPadding() + 100.dp),
        ) {
            KashBackTopBar(
                title = stringResource(Res.string.transfer_title),
                onBackClick = onBackClick,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = KashDimens.ScreenHorizontalPadding, vertical = 12.dp),
            ) {
                KashSectionLabel(text = stringResource(Res.string.transfer_from))
                Spacer(Modifier.height(8.dp))
                AccountCardWrap(account = state.from, onClick = onFromClick)

                ArrowConnector()

                KashSectionLabel(text = stringResource(Res.string.transfer_to))
                Spacer(Modifier.height(8.dp))
                AccountCardWrap(account = state.to, onClick = onToClick)

                Spacer(Modifier.height(22.dp))
                CenteredAmount(
                    label = stringResource(Res.string.transfer_you_send),
                    amount = state.sendAmount,
                    currencySymbol = state.sendCurrencySymbol,
                    big = true,
                )

                Spacer(Modifier.height(18.dp))
                CenteredAmount(
                    label = stringResource(Res.string.transfer_they_receive),
                    amount = state.receiveAmount,
                    currencySymbol = state.receiveCurrencySymbol,
                    big = false,
                )

                Spacer(Modifier.height(18.dp))
                RateRow(
                    pair = state.ratePair,
                    rate = state.rateValue,
                    isAuto = state.rateAuto,
                    onOverrideClick = onRateOverrideClick,
                )

                Spacer(Modifier.height(10.dp))
                CommentField(comment = state.comment)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(
                    start = KashDimens.ScreenHorizontalPadding,
                    end = KashDimens.ScreenHorizontalPadding,
                    bottom = contentPadding.calculateBottomPadding() + 28.dp,
                ),
        ) {
            KashButton(
                text = stringResource(Res.string.transfer_action_save),
                onClick = onSaveClick,
            )
        }
    }
}

@Composable
private fun AccountCardWrap(account: Account, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(16.dp)),
    ) {
        KashAccountCardRow(account = account, onClick = onClick)
    }
}

@Composable
private fun ArrowConnector() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Kash.colors.card)
                .border(1.dp, Kash.colors.line, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDownward,
                contentDescription = null,
                tint = Kash.colors.sub,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun CenteredAmount(
    label: String,
    amount: String,
    currencySymbol: String,
    big: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label.uppercase(),
            color = Kash.colors.sub,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.4.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        val numberSize = if (big) 44.sp else 30.sp
        val tracking = if (big) (-2).sp else (-1.2).sp
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = amount,
                color = Kash.colors.text,
                fontSize = numberSize,
                lineHeight = numberSize,
                fontWeight = FontWeight.Medium,
                letterSpacing = tracking,
            )
            Spacer(Modifier.size(6.dp))
            Text(
                text = currencySymbol,
                color = if (big) Kash.colors.accent else Kash.colors.fade,
                fontSize = numberSize,
                lineHeight = numberSize,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Composable
private fun RateRow(
    pair: String,
    rate: String,
    isAuto: Boolean,
    onOverrideClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(if (Kash.colors.isDark) Kash.colors.cardAlt else Kash.colors.chipBg),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "=",
                color = Kash.colors.sub,
                style = TextStyle(
                    fontFamily = JetBrainsMonoFontFamily(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.transfer_rate_label_format, pair),
                color = Kash.colors.sub,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.1.sp,
            )
            Spacer(Modifier.height(1.dp))
            Row {
                Text(
                    text = rate,
                    color = Kash.colors.text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
                if (isAuto) {
                    Spacer(Modifier.size(4.dp))
                    Text(
                        text = stringResource(Res.string.transfer_rate_auto_suffix),
                        color = Kash.colors.fade,
                        fontSize = 14.sp,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Kash.colors.accentSoft)
                .clickable(onClick = onOverrideClick)
                .padding(horizontal = 10.dp, vertical = 6.dp),
        ) {
            Text(
                text = stringResource(Res.string.transfer_rate_override),
                color = Kash.colors.accentSoftInk,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun CommentField(comment: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Notes,
            contentDescription = null,
            tint = Kash.colors.sub,
            modifier = Modifier.size(18.dp),
        )
        Column(modifier = Modifier.weight(1f)) {
            KashSectionLabel(text = stringResource(Res.string.transfer_field_comment))
            Spacer(Modifier.height(2.dp))
            Text(
                text = comment,
                color = Kash.colors.text,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Preview
@Composable
private fun TransferContentPreview() {
    KashTheme {
        TransferContent(
            state = TransferUiState(
                from = Account(
                    id = "kaspi-gold",
                    name = "Kaspi Gold",
                    type = AccountType.Debit,
                    currency = "KZT",
                    balance = 425000,
                    bank = Bank.Kaspi,
                ),
                to = Account(
                    id = "halyk-onay",
                    name = "Halyk Onay",
                    type = AccountType.Debit,
                    currency = "USD",
                    balance = 850,
                    bank = Bank.Halyk,
                ),
                sendAmount = "50 000",
                sendCurrencySymbol = "₸",
                receiveAmount = "104.50",
                receiveCurrencySymbol = "$",
                ratePair = "USD / KZT",
                rateValue = "478.50",
                rateAuto = true,
                comment = "Top-up for travel",
            ),
            onBackClick = {},
            onFromClick = {},
            onToClick = {},
            onRateOverrideClick = {},
            onSaveClick = {},
        )
    }
}
