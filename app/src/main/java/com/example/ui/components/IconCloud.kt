package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.IntegrationInstructions
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Web
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 2026-10-23：3D 旋转图标云 —— 翻译 React 版 magicui/icon-cloud 为 Compose。
 * 原版 = 30 个技术图标在 3D 球面上旋转（cdn.simpleicons.org 图标）。
 * Compose 实现：Fibonacci 球面均匀分布 + 无限旋转动画 + 3D 投影（深度缩放/透明度）。
 */
@Composable
fun IconCloud(
    modifier: Modifier = Modifier,
    icons: List<ImageVector> = defaultIconCloudIcons(),
    iconTint: Color = Color(0xFF64748B),
    durationMillis: Int = 22000
) {
    // Fibonacci 球面均匀分布
    val points = remember(icons.size) { fibonacciSphere(icons.size) }
    val transition = rememberInfiniteTransition(label = "iconCloud")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "iconCloudRotation"
    )

    BoxWithConstraints(modifier = modifier) {
        val cx = constraints.maxWidth / 2f
        val cy = constraints.maxHeight / 2f
        val radius = minOf(cx, cy) * 0.82f
        val rad = Math.toRadians(rotation.toDouble())
        val cosR = cos(rad).toFloat()
        val sinR = sin(rad).toFloat()

        icons.forEachIndexed { i, icon ->
            val (x, y, z) = points[i]
            // 绕 Y 轴旋转
            val x1 = x * cosR - z * sinR
            val z1 = x * sinR + z * cosR
            // 深度 → 缩放与透明度（越近越大越清晰）
            val depth = (z1 + 1f) / 2f
            val scale = 0.55f + 0.45f * depth
            val alpha = 0.30f + 0.70f * depth
            // 轻微 X 轴倾斜增加立体感
            val y1 = y * 0.92f
            val px = cx + x1 * radius
            val py = cy + y1 * radius

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .offset { IntOffset(px.roundToInt() - 12, py.roundToInt() - 12) }
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
            )
        }
    }
}

/** Fibonacci 球面均匀分布，返回单位球面上的 (x, y, z) */
private fun fibonacciSphere(n: Int): List<Triple<Float, Float, Float>> {
    if (n <= 0) return emptyList()
    if (n == 1) return listOf(Triple(0f, 1f, 0f))
    val goldenAngle = Math.PI * (3.0 - sqrt(5.0))
    return List(n) { i ->
        val y = 1f - (i / (n - 1f)) * 2f
        val r = sqrt((1f - y * y).coerceAtLeast(0f))
        val theta = goldenAngle * i
        Triple(
            (cos(theta) * r).toFloat(),
            y,
            (sin(theta) * r).toFloat()
        )
    }
}

/** 默认图标集：对照原版 slugs（typescript/javascript/dart/java/react/flutter/android/html5/css3/node/express/next/prisma/aws/postgres/firebase/nginx/vercel/jest/docker/git/github/gitlab/vscode/androidstudio/figma）的 Material 近似 */
fun defaultIconCloudIcons(): List<ImageVector> = listOf(
    Icons.Filled.Code,                    // typescript
    Icons.Filled.Terminal,                // javascript
    Icons.Filled.DataObject,              // dart
    Icons.Filled.Coffee,                  // java
    Icons.Filled.IntegrationInstructions, // react
    Icons.Filled.Widgets,                 // flutter
    Icons.Filled.Android,                 // android
    Icons.Filled.Web,                     // html5
    Icons.Filled.Extension,               // css3
    Icons.Filled.Storage,                 // nodedotjs
    Icons.Filled.Terminal,                // express
    Icons.Filled.Cloud,                   // nextdotjs
    Icons.Filled.Dns,                     // prisma
    Icons.Filled.Cloud,                   // amazonaws
    Icons.Filled.Storage,                 // postgresql
    Icons.Filled.Cloud,                   // firebase
    Icons.Filled.Settings,                // nginx
    Icons.Filled.Api,                     // vercel
    Icons.Filled.Build,                   // testinglibrary
    Icons.Filled.Memory,                  // jest
    Icons.Filled.PlayArrow,               // cypress
    Icons.Filled.Widgets,                 // docker
    Icons.Filled.Share,                   // git
    Icons.Filled.Home,                    // jira
    Icons.Filled.Code,                    // github
    Icons.Filled.Star,                    // gitlab
    Icons.Filled.Terminal,                // visualstudiocode
    Icons.Filled.Android,                 // androidstudio
    Icons.Filled.Favorite,                // sonarqube
    Icons.Filled.Extension                // figma
)
