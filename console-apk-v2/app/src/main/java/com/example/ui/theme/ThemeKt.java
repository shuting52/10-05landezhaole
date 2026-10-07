package com.example.ui.theme;

import androidx.compose.material3.ColorScheme;
import androidx.compose.material3.ColorSchemeKt;
import androidx.compose.material3.MaterialThemeKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.ScopeUpdateScope;
import androidx.compose.ui.graphics.Color;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Theme.kt */
@Metadata(d1 = {"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a4\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\u0011\u0010\u0007\u001a\r\u0012\u0004\u0012\u00020\u00030\b¢\u0006\u0002\b\tH\u0007¢\u0006\u0002\u0010\n\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"GuochaoColorScheme", "Landroidx/compose/material3/ColorScheme;", "MyApplicationTheme", "", "darkTheme", "", "dynamicColor", "content", "Lkotlin/Function0;", "Landroidx/compose/runtime/Composable;", "(ZZLkotlin/jvm/functions/Function2;Landroidx/compose/runtime/Composer;II)V", "app"}, k = 2, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes3.dex */
public final class ThemeKt {
    private static final ColorScheme GuochaoColorScheme;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MyApplicationTheme$lambda$0(boolean z, boolean z2, Function2 function2, int i, int i2, Composer composer, int i3) {
        MyApplicationTheme(z, z2, function2, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    static {
        long m4157copywmQWz5c;
        long m4157copywmQWz5c2;
        long cinnabar = ColorKt.getCinnabar();
        long paper = ColorKt.getPaper();
        long statCinnabarStart = ColorKt.getStatCinnabarStart();
        long cinnabar2 = ColorKt.getCinnabar();
        long ink = ColorKt.getInk();
        long paper2 = ColorKt.getPaper();
        long statInkStart = ColorKt.getStatInkStart();
        long ink2 = ColorKt.getInk();
        long gold = ColorKt.getGold();
        long m4196getWhite0d7_KjU = Color.Companion.m4196getWhite0d7_KjU();
        long statGoldStart = ColorKt.getStatGoldStart();
        long goldDark = ColorKt.getGoldDark();
        long paper3 = ColorKt.getPaper();
        long inkBlack = ColorKt.getInkBlack();
        long paperSoft = ColorKt.getPaperSoft();
        long inkBlack2 = ColorKt.getInkBlack();
        long mistSoft = ColorKt.getMistSoft();
        m4157copywmQWz5c = Color.m4157copywmQWz5c(r37, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r37) : 0.65f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r37) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r37) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
        long mist = ColorKt.getMist();
        m4157copywmQWz5c2 = Color.m4157copywmQWz5c(r39, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r39) : 0.7f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r39) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r39) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getMist()) : 0.0f);
        GuochaoColorScheme = ColorSchemeKt.m1936lightColorSchemeCXl9yA$default(cinnabar, paper, statCinnabarStart, cinnabar2, 0L, ink, paper2, statInkStart, ink2, gold, m4196getWhite0d7_KjU, statGoldStart, goldDark, paper3, inkBlack, paperSoft, inkBlack2, mistSoft, m4157copywmQWz5c, 0L, 0L, 0L, ColorKt.getCinnabar(), Color.Companion.m4196getWhite0d7_KjU(), 0L, 0L, mist, m4157copywmQWz5c2, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, -214433776, 15, null);
    }

    public static final void MyApplicationTheme(boolean darkTheme, boolean dynamicColor, final Function2<? super Composer, ? super Integer, Unit> content, Composer $composer, final int $changed, final int i) {
        boolean darkTheme2;
        boolean dynamicColor2;
        Composer $composer2;
        final boolean darkTheme3;
        final boolean dynamicColor3;
        Intrinsics.checkNotNullParameter(content, "content");
        Composer $composer3 = $composer.startRestartGroup(548420513);
        ComposerKt.sourceInformation($composer3, "C(MyApplicationTheme)P(1,2)38@1085L121:Theme.kt#75kw8w");
        int $dirty = $changed;
        if (($changed & 384) == 0) {
            $dirty |= $composer3.changedInstance(content) ? 256 : 128;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 129) == 128 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            darkTheme3 = darkTheme;
            dynamicColor3 = dynamicColor;
            $composer2 = $composer3;
        } else {
            if ((i & 1) != 0) {
                darkTheme2 = false;
            } else {
                darkTheme2 = darkTheme;
            }
            if ((i & 2) == 0) {
                dynamicColor2 = dynamicColor;
            } else {
                dynamicColor2 = false;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(548420513, $dirty2, -1, "com.example.ui.theme.MyApplicationTheme (Theme.kt:37)");
            }
            MaterialThemeKt.MaterialTheme(GuochaoColorScheme, null, TypeKt.getTypography(), content, $composer3, (($dirty2 << 3) & 7168) | 390, 2);
            $composer2 = $composer3;
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            darkTheme3 = darkTheme2;
            dynamicColor3 = dynamicColor2;
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.theme.ThemeKt$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return ThemeKt.MyApplicationTheme$lambda$0(darkTheme3, dynamicColor3, content, $changed, i, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }
}
