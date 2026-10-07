package com.example.ui.screens;

import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.layout.SpacerKt;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.AddKt;
import androidx.compose.material3.IconKt;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.unit.Dp;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DashboardAndCardScreens.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class ComposableSingletons$DashboardAndCardScreensKt {
    public static final ComposableSingletons$DashboardAndCardScreensKt INSTANCE = new ComposableSingletons$DashboardAndCardScreensKt();

    /* renamed from: lambda$-1910081630  reason: not valid java name */
    private static Function3<RowScope, Composer, Integer, Unit> f163lambda$1910081630 = ComposableLambdaKt.composableLambdaInstance(-1910081630, false, new Function3() { // from class: com.example.ui.screens.ComposableSingletons$DashboardAndCardScreensKt$$ExternalSyntheticLambda0
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return ComposableSingletons$DashboardAndCardScreensKt.lambda__1910081630$lambda$0((RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
        }
    });

    /* renamed from: getLambda$-1910081630$app  reason: not valid java name */
    public final Function3<RowScope, Composer, Integer, Unit> m6997getLambda$1910081630$app() {
        return f163lambda$1910081630;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit lambda__1910081630$lambda$0(RowScope Button, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(Button, "$this$Button");
        ComposerKt.sourceInformation($composer, "C115@4552L191,120@4764L39,121@4859L10,121@4824L57:DashboardAndCardScreens.kt#2thlc2");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1910081630, $changed, -1, "com.example.ui.screens.ComposableSingletons$DashboardAndCardScreensKt.lambda$-1910081630.<anonymous> (DashboardAndCardScreens.kt:115)");
            }
            IconKt.m2150Iconww6aTOc(AddKt.getAdd(Icons.INSTANCE.getDefault()), (String) null, SizeKt.m715size3ABfNKs(Modifier.Companion, Dp.m6622constructorimpl(16)), 0L, $composer, 432, 8);
            SpacerKt.Spacer(SizeKt.m720width3ABfNKs(Modifier.Companion, Dp.m6622constructorimpl(4)), $composer, 6);
            TextKt.m2693Text4IGK_g("新建卡片", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelLarge(), $composer, 6, 0, 65534);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }
}
