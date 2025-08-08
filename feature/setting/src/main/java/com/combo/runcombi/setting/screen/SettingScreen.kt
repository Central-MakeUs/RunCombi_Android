package com.combo.runcombi.setting.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.RunCombiAppTopBar
import com.combo.runcombi.core.designsystem.component.RunCombiBottomSheet
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey05
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey09
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.feature.setting.R
import com.combo.runcombi.setting.model.BottomSheetType
import com.combo.runcombi.setting.model.SettingEvent
import com.combo.runcombi.setting.viewmodel.SettingViewModel
import com.combo.runcombi.ui.ext.clickableSingle

fun openUrl(context: android.content.Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}


@Composable
fun SettingScreen(
    goToLogin: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val eventFlow = viewModel.eventFlow
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            when (event) {
                is SettingEvent.LogoutSuccess, is SettingEvent.WithdrawSuccess -> {
                    goToLogin()
                }

                is SettingEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            RunCombiAppTopBar(
                onBack = onBack, title = "설정"
            )
            SettingContent(onLogout = {
                viewModel.tryLogout()
            }, onWithdraw = {
                viewModel.tryWithDraw()
            })
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary01)
            }
        }
    }
}

@Composable
fun SettingContent(
    onLogout: () -> Unit = {},
    onWithdraw: () -> Unit = {},
) {
    val context = LocalContext.current

    var bottomSheetType by remember { mutableStateOf<BottomSheetType?>(null) }

    RunCombiBottomSheet(
        show = bottomSheetType != null,
        onDismiss = { bottomSheetType = null },
        onAccept = {
            when (bottomSheetType) {
                BottomSheetType.Logout -> {
                    bottomSheetType = null
                    onLogout()
                }

                BottomSheetType.Withdraw -> {
                    bottomSheetType = null
                    onWithdraw()
                }

                else -> {
                    bottomSheetType = null
                }
            }
            bottomSheetType = null
        },
        onCancel = { bottomSheetType = null },
        title = when (bottomSheetType) {
            BottomSheetType.Logout -> "로그아웃"
            BottomSheetType.Withdraw -> "회원 탈퇴"
            else -> ""
        },
        subtitle = when (bottomSheetType) {
            BottomSheetType.Logout -> "정말 로그아웃 하시겠습니까?"
            BottomSheetType.Withdraw -> "정말 회원 탈퇴 하시겠습니까"
            else -> ""
        },
        acceptButtonText = when (bottomSheetType) {
            BottomSheetType.Logout -> "로그아웃"
            BottomSheetType.Withdraw -> "탈퇴"
            else -> ""
        },
        cancelButtonText = "취소"
    )

    Column(
        modifier = Modifier
            .background(Grey01)
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(36.dp))
        SectionTitle("약관 및 정책")
        Spacer(Modifier.height(16.dp))
        SettingItem("서비스 이용약관", onClick = {
            openUrl(
                context,
                "https://encouraging-potential-f2d.notion.site/2366951266db80dabc10c701a65875fe?source=copy_link"
            )
        })
        Spacer(Modifier.height(20.dp))
        SettingItem("개인정보처리방침", onClick = {
            openUrl(
                context,
                "https://encouraging-potential-f2d.notion.site/2366951266db8035ad23c11c502fd243?source=copy_link"
            )
        })
        Spacer(Modifier.height(20.dp))
        SettingItem("위치정보 이용약관", onClick = {
            openUrl(
                context,
                "https://encouraging-potential-f2d.notion.site/2366951266db80efabead10fb511a343?source=copy_link"
            )
        })

        Spacer(Modifier.height(40.dp))
        SectionTitle("계정")
        Spacer(Modifier.height(16.dp))
        SettingItem("로그아웃", onClick = { bottomSheetType = BottomSheetType.Logout })
        Spacer(Modifier.height(20.dp))
        SettingItem("회원 탈퇴", onClick = { bottomSheetType = BottomSheetType.Withdraw })

        Spacer(Modifier.height(40.dp))
        SectionTitle("고객")
        Spacer(Modifier.height(16.dp))
        // SettingItem("런콤비 개선 제안", onClick = { /* TODO */ })
        //Spacer(Modifier.height(20.dp))
        AppVersionRow(onUpdateClick = { /* TODO */ })
    }
}


@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = body2,
        color = Grey05,
    )
}

@Composable
fun SettingItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableSingle { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, style = body1, color = Grey09)
        Spacer(modifier = Modifier.weight(1f))
        StableImage(
            drawableResId = R.drawable.ic_arrow_right,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun SettingItemWithIcon(text: String, iconRes: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text)
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun AppVersionRow(onUpdateClick: () -> Unit) {
    val context = LocalContext.current
    val versionName = try {
        "v.${context.packageManager.getPackageInfo(context.packageName, 0).versionName}" ?: ""
    } catch (e: Exception) {
        ""
    }
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Text("앱 버전", color = Grey09, style = body1)
        Spacer(modifier = Modifier.width(8.dp))
        Text(versionName, style = body3, color = Grey06)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingContent() {
    SettingContent()
}
