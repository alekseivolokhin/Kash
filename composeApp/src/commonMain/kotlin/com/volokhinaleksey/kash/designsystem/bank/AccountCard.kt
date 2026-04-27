package com.volokhinaleksey.kash.designsystem.bank

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.presentation.accounts.Account
import com.volokhinaleksey.kash.presentation.accounts.AccountType
import com.volokhinaleksey.kash.theme.Kash

fun AccountType.toBadgeStyle(): AccountTypeStyle = when (this) {
    AccountType.Cash -> AccountTypeStyle.Cash
    AccountType.Debit -> AccountTypeStyle.Debit
    AccountType.Credit -> AccountTypeStyle.Credit
    AccountType.Deposit -> AccountTypeStyle.Deposit
}

fun currencySymbol(code: String): String = when (code) {
    "KZT" -> "₸"
    "USD" -> "$"
    "EUR" -> "€"
    "RUB" -> "₽"
    "GBP" -> "£"
    else -> code
}

@Composable
fun KashAccountCardCompact(
    account: Account,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val borderColor = if (selected) Kash.colors.accent else Kash.colors.line
    val sym = currencySymbol(account.currency)
    Column(
        modifier = modifier
            .width(168.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Kash.colors.card)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            BankBadge(bank = account.bank, size = 26.dp, cornerRadius = 7.dp)
            AccountTypeBadge(type = account.type.toBadgeStyle())
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = account.name,
            color = Kash.colors.sub,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = formatBalance(account.balance),
                color = Kash.colors.text,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.5).sp,
                maxLines = 1,
            )
            Spacer(Modifier.width(3.dp))
            Text(
                text = sym,
                color = Kash.colors.fade,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Composable
fun KashAccountCardRow(
    account: Account,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val sym = currencySymbol(account.currency)
    val isNegative = account.balance < 0
    val amountColor = if (isNegative) Kash.colors.neg else Kash.colors.text
    val nonKztConv = account.baseConv?.takeIf { account.currency != "KZT" }

    Row(
        modifier = modifier
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        BankBadge(bank = account.bank, size = 38.dp, cornerRadius = 10.dp)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = account.name,
                    color = Kash.colors.text,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.2).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                AccountTypeBadge(type = account.type.toBadgeStyle())
            }
            val subtitle = if (account.type == AccountType.Credit && account.limit != null) {
                val available = account.limit - kotlin.math.abs(account.balance)
                "Available ${formatBalance(available)} $sym · limit ${formatBalance(account.limit)}"
            } else if (account.baseConv != null && account.currency != "KZT") {
                "${account.currency} · ≈ ${formatBalance(account.baseConv)} ₸"
            } else {
                account.currency
            }
            Text(
                text = subtitle,
                color = Kash.colors.sub,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = if (isNegative) "−${formatBalance(-account.balance)}" else formatBalance(account.balance),
                    color = amountColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.3).sp,
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text = sym,
                    color = Kash.colors.fade,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
            if (nonKztConv != null) {
                Text(
                    text = "≈ ${formatBalance(nonKztConv)} ₸",
                    color = Kash.colors.fade,
                    fontSize = 11.sp,
                )
            }
        }
    }
}

@Composable
fun KashAccountCardSelector(
    account: Account,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 11.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            BankBadge(bank = account.bank, size = 28.dp, cornerRadius = 8.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "FROM ACCOUNT",
                    color = Kash.colors.sub,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.1.sp,
                )
                Spacer(Modifier.height(1.dp))
                Text(
                    text = "${account.name} · ${account.currency}",
                    color = Kash.colors.text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

internal fun formatBalance(value: Long): String {
    val abs = kotlin.math.abs(value)
    val s = abs.toString()
    val sb = StringBuilder()
    val len = s.length
    for (i in 0 until len) {
        if (i > 0 && (len - i) % 3 == 0) sb.append(' ')
        sb.append(s[i])
    }
    return sb.toString()
}
