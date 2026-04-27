package com.volokhinaleksey.kash.navigation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.outlined.Check
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
import com.volokhinaleksey.kash.designsystem.feedback.KashPageDots
import com.volokhinaleksey.kash.designsystem.layout.KashBottomCtaArea
import com.volokhinaleksey.kash.designsystem.topbar.KashLogoBadge
import com.volokhinaleksey.kash.theme.InterTightFontFamily
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.onb_feature_categories_sub
import kash.composeapp.generated.resources.onb_feature_categories_title
import kash.composeapp.generated.resources.onb_feature_imports_sub
import kash.composeapp.generated.resources.onb_feature_imports_title
import kash.composeapp.generated.resources.onb_feature_local_sub
import kash.composeapp.generated.resources.onb_feature_local_title
import kash.composeapp.generated.resources.onb_get_started
import kash.composeapp.generated.resources.onb_login_hint
import kash.composeapp.generated.resources.onb_splash_subtitle
import kash.composeapp.generated.resources.onb_splash_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun SplashContent(
    onGetStartedClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(Modifier.height(24.dp))
            KashLogoBadge(size = 96.dp)
            Spacer(Modifier.height(28.dp))
            Text(
                text = stringResource(Res.string.onb_splash_title),
                color = Kash.colors.text,
                fontFamily = InterTightFontFamily(),
                fontSize = 38.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-1.4).sp,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(Res.string.onb_splash_subtitle),
                color = Kash.colors.sub,
                fontSize = 15.sp,
                lineHeight = 22.5.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 300.dp),
            )

            Spacer(Modifier.height(32.dp))
            Column(
                modifier = Modifier.widthIn(max = 280.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                FeatureRow(
                    title = stringResource(Res.string.onb_feature_imports_title),
                    sub = stringResource(Res.string.onb_feature_imports_sub),
                )
                FeatureRow(
                    title = stringResource(Res.string.onb_feature_categories_title),
                    sub = stringResource(Res.string.onb_feature_categories_sub),
                )
                FeatureRow(
                    title = stringResource(Res.string.onb_feature_local_title),
                    sub = stringResource(Res.string.onb_feature_local_sub),
                )
            }
            Spacer(Modifier.height(24.dp))
        }

        KashBottomCtaArea {
            KashButton(
                text = stringResource(Res.string.onb_get_started),
                onClick = onGetStartedClick,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(Res.string.onb_login_hint),
                color = Kash.colors.sub,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onLoginClick)
                    .padding(vertical = 4.dp),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(18.dp))
            KashPageDots(activeIndex = 0, total = 3)
        }
    }
}

@Preview
@Composable
private fun SplashContentPreview() {
    KashTheme { SplashContent(onGetStartedClick = {}, onLoginClick = {}) }
}

@Composable
private fun FeatureRow(title: String, sub: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(22.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Kash.colors.accentSoft),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = Kash.colors.accentSoftInk,
                modifier = Modifier.size(12.dp),
            )
        }
        Column {
            Text(
                text = title,
                color = Kash.colors.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = sub,
                color = Kash.colors.sub,
                fontSize = 12.5.sp,
            )
        }
    }
}
