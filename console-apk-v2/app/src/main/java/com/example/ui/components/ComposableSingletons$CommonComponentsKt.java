package com.example.ui.components;

import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeftKt;
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRightKt;
import androidx.compose.material.icons.filled.ClearKt;
import androidx.compose.material.icons.outlined.SearchKt;
import androidx.compose.material3.IconKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.vector.ImageVector;
import com.example.ui.theme.ColorKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
/* compiled from: CommonComponents.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class ComposableSingletons$CommonComponentsKt {
    public static final ComposableSingletons$CommonComponentsKt INSTANCE = new ComposableSingletons$CommonComponentsKt();
    private static Function2<Composer, Integer, Unit> lambda$952025710 = ComposableLambdaKt.composableLambdaInstance(952025710, false, new Function2() { // from class: com.example.ui.components.ComposableSingletons$CommonComponentsKt$$ExternalSyntheticLambda0
        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Object obj2) {
            return ComposableSingletons$CommonComponentsKt.lambda_952025710$lambda$0((Composer) obj, ((Integer) obj2).intValue());
        }
    });

    /* renamed from: lambda$-605614937  reason: not valid java name */
    private static Function2<Composer, Integer, Unit> f101lambda$605614937 = ComposableLambdaKt.composableLambdaInstance(-605614937, false, new Function2() { // from class: com.example.ui.components.ComposableSingletons$CommonComponentsKt$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Object obj2) {
            return ComposableSingletons$CommonComponentsKt.lambda__605614937$lambda$1((Composer) obj, ((Integer) obj2).intValue());
        }
    });

    /* renamed from: lambda$-281881043  reason: not valid java name */
    private static Function2<Composer, Integer, Unit> f100lambda$281881043 = ComposableLambdaKt.composableLambdaInstance(-281881043, false, new Function2() { // from class: com.example.ui.components.ComposableSingletons$CommonComponentsKt$$ExternalSyntheticLambda2
        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Object obj2) {
            return ComposableSingletons$CommonComponentsKt.lambda__281881043$lambda$2((Composer) obj, ((Integer) obj2).intValue());
        }
    });
    private static Function2<Composer, Integer, Unit> lambda$417370326 = ComposableLambdaKt.composableLambdaInstance(417370326, false, new Function2() { // from class: com.example.ui.components.ComposableSingletons$CommonComponentsKt$$ExternalSyntheticLambda3
        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Object obj2) {
            return ComposableSingletons$CommonComponentsKt.lambda_417370326$lambda$3((Composer) obj, ((Integer) obj2).intValue());
        }
    });

    /* renamed from: getLambda$-281881043$app  reason: not valid java name */
    public final Function2<Composer, Integer, Unit> m6934getLambda$281881043$app() {
        return f100lambda$281881043;
    }

    /* renamed from: getLambda$-605614937$app  reason: not valid java name */
    public final Function2<Composer, Integer, Unit> m6935getLambda$605614937$app() {
        return f101lambda$605614937;
    }

    public final Function2<Composer, Integer, Unit> getLambda$417370326$app() {
        return lambda$417370326;
    }

    public final Function2<Composer, Integer, Unit> getLambda$952025710$app() {
        return lambda$952025710;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit lambda_952025710$lambda$0(Composer $composer, int $changed) {
        long m4157copywmQWz5c;
        ComposerKt.sourceInformation($composer, "C317@11542L167:CommonComponents.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(952025710, $changed, -1, "com.example.ui.components.ComposableSingletons$CommonComponentsKt.lambda$952025710.<anonymous> (CommonComponents.kt:317)");
            }
            ImageVector search = SearchKt.getSearch(Icons.Outlined.INSTANCE);
            m4157copywmQWz5c = Color.m4157copywmQWz5c(r2, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r2) : 0.45f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r2) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r2) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
            IconKt.m2150Iconww6aTOc(search, "搜索", (Modifier) null, m4157copywmQWz5c, $composer, 3120, 4);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit lambda__605614937$lambda$1(Composer $composer, int $changed) {
        long m4157copywmQWz5c;
        ComposerKt.sourceInformation($composer, "C326@11866L199:CommonComponents.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-605614937, $changed, -1, "com.example.ui.components.ComposableSingletons$CommonComponentsKt.lambda$-605614937.<anonymous> (CommonComponents.kt:326)");
            }
            ImageVector clear = ClearKt.getClear(Icons.INSTANCE.getDefault());
            m4157copywmQWz5c = Color.m4157copywmQWz5c(r2, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r2) : 0.45f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r2) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r2) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
            IconKt.m2150Iconww6aTOc(clear, "清除搜索", (Modifier) null, m4157copywmQWz5c, $composer, 3120, 4);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit lambda__281881043$lambda$2(Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C386@13820L149:CommonComponents.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-281881043, $changed, -1, "com.example.ui.components.ComposableSingletons$CommonComponentsKt.lambda$-281881043.<anonymous> (CommonComponents.kt:386)");
            }
            IconKt.m2150Iconww6aTOc(KeyboardArrowLeftKt.getKeyboardArrowLeft(Icons.AutoMirrored.Filled.INSTANCE), "上一页", (Modifier) null, 0L, $composer, 48, 12);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit lambda_417370326$lambda$3(Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C414@14874L150:CommonComponents.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(417370326, $changed, -1, "com.example.ui.components.ComposableSingletons$CommonComponentsKt.lambda$417370326.<anonymous> (CommonComponents.kt:414)");
            }
            IconKt.m2150Iconww6aTOc(KeyboardArrowRightKt.getKeyboardArrowRight(Icons.AutoMirrored.Filled.INSTANCE), "下一页", (Modifier) null, 0L, $composer, 48, 12);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }
}
