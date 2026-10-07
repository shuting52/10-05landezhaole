package com.example.ui.components;

import androidx.compose.foundation.BorderStrokeKt;
import androidx.compose.foundation.ClickableKt;
import androidx.compose.foundation.interaction.MutableInteractionSource;
import androidx.compose.foundation.layout.PaddingKt;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.shape.RoundedCornerShape;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.foundation.text.KeyboardActions;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.AutoAwesomeKt;
import androidx.compose.material.icons.filled.DownloadKt;
import androidx.compose.material.icons.filled.LayersKt;
import androidx.compose.material.icons.filled.VerifiedKt;
import androidx.compose.material3.IconButtonKt;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.OutlinedTextFieldDefaults;
import androidx.compose.material3.OutlinedTextFieldKt;
import androidx.compose.material3.SurfaceKt;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.ScopeUpdateScope;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.Shape;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.platform.TestTagKt;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.VisualTransformation;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.compose.ui.unit.Dp;
import androidx.core.app.NotificationCompat;
import androidx.profileinstaller.ProfileVerifier;
import com.example.model.AdminScreen;
import com.example.model.ButtonType;
import com.example.model.CardStatus;
import com.example.model.LogActionType;
import com.example.model.StatIcon;
import com.example.model.StatItem;
import com.example.model.StatTone;
import com.example.ui.theme.ColorKt;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CommonComponents.kt */
@Metadata(d1 = {"\u0000h\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u001aI\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062 \b\u0002\u0010\u0007\u001a\u001a\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u0001\u0018\u00010\b¢\u0006\u0002\b\n¢\u0006\u0002\b\u000bH\u0007¢\u0006\u0002\u0010\f\u001a3\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007¢\u0006\u0002\u0010\u0012\u001a\u001f\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007¢\u0006\u0002\u0010\u0016\u001a\u001f\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007¢\u0006\u0002\u0010\u001a\u001a\u001f\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007¢\u0006\u0002\u0010\u001e\u001a\u001f\u0010\u001f\u001a\u00020\u00012\u0006\u0010 \u001a\u00020!2\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007¢\u0006\u0002\u0010\"\u001aE\u0010#\u001a\u00020\u00012\u0006\u0010$\u001a\u00020\u00032\u0012\u0010%\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\b2\u0006\u0010&\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010'\u001a\u00020\u0003H\u0007¢\u0006\u0002\u0010(\u001aC\u0010)\u001a\u00020\u00012\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020+2\u0006\u0010-\u001a\u00020+2\u0012\u0010.\u001a\u000e\u0012\u0004\u0012\u00020+\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007¢\u0006\u0002\u0010/\u001a>\u00100\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\u0015\b\u0002\u0010\u001c\u001a\u000f\u0012\u0004\u0012\u00020\u0001\u0018\u000101¢\u0006\u0002\b\nH\u0007¢\u0006\u0002\u00102¨\u00063"}, d2 = {"PageHeader", "", "title", "", "description", "modifier", "Landroidx/compose/ui/Modifier;", "actions", "Lkotlin/Function1;", "Landroidx/compose/foundation/layout/RowScope;", "Landroidx/compose/runtime/Composable;", "Lkotlin/ExtensionFunctionType;", "(Ljava/lang/String;Ljava/lang/String;Landroidx/compose/ui/Modifier;Lkotlin/jvm/functions/Function3;Landroidx/compose/runtime/Composer;II)V", "StatCard", "item", "Lcom/example/model/StatItem;", "onNavigate", "Lcom/example/model/AdminScreen;", "(Lcom/example/model/StatItem;Lkotlin/jvm/functions/Function1;Landroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;II)V", "CardStatusBadge", NotificationCompat.CATEGORY_STATUS, "Lcom/example/model/CardStatus;", "(Lcom/example/model/CardStatus;Landroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;II)V", "ButtonTypeBadge", "type", "Lcom/example/model/ButtonType;", "(Lcom/example/model/ButtonType;Landroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;II)V", "LogActionBadge", "action", "Lcom/example/model/LogActionType;", "(Lcom/example/model/LogActionType;Landroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;II)V", "EnabledBadge", "enabled", "", "(ZLandroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;II)V", "SearchBarField", "value", "onValueChange", "placeholder", "testTag", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;Ljava/lang/String;Landroidx/compose/ui/Modifier;Ljava/lang/String;Landroidx/compose/runtime/Composer;II)V", "PaginationBar", "page", "", "pageSize", "total", "onPageChange", "(IIILkotlin/jvm/functions/Function1;Landroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;II)V", "EmptyStateView", "Lkotlin/Function0;", "(Ljava/lang/String;Ljava/lang/String;Landroidx/compose/ui/Modifier;Lkotlin/jvm/functions/Function2;Landroidx/compose/runtime/Composer;II)V", "app"}, k = 2, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class CommonComponentsKt {

    /* compiled from: CommonComponents.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    /* loaded from: classes6.dex */
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;
        public static final /* synthetic */ int[] $EnumSwitchMapping$2;
        public static final /* synthetic */ int[] $EnumSwitchMapping$3;
        public static final /* synthetic */ int[] $EnumSwitchMapping$4;

        static {
            int[] iArr = new int[StatTone.values().length];
            try {
                iArr[StatTone.CINNABAR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[StatTone.GOLD.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[StatTone.INK.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[StatTone.ORANGE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $EnumSwitchMapping$0 = iArr;
            int[] iArr2 = new int[StatIcon.values().length];
            try {
                iArr2[StatIcon.LAYERS.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr2[StatIcon.DOWNLOAD.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr2[StatIcon.USERS.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr2[StatIcon.CLIPBOARD.ordinal()] = 4;
            } catch (NoSuchFieldError e8) {
            }
            $EnumSwitchMapping$1 = iArr2;
            int[] iArr3 = new int[CardStatus.values().length];
            try {
                iArr3[CardStatus.PUBLISHED.ordinal()] = 1;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr3[CardStatus.REVIEWING.ordinal()] = 2;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr3[CardStatus.DRAFT.ordinal()] = 3;
            } catch (NoSuchFieldError e11) {
            }
            $EnumSwitchMapping$2 = iArr3;
            int[] iArr4 = new int[ButtonType.values().length];
            try {
                iArr4[ButtonType.DOWNLOAD.ordinal()] = 1;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr4[ButtonType.LINK.ordinal()] = 2;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr4[ButtonType.COPY.ordinal()] = 3;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr4[ButtonType.CONTACT.ordinal()] = 4;
            } catch (NoSuchFieldError e15) {
            }
            $EnumSwitchMapping$3 = iArr4;
            int[] iArr5 = new int[LogActionType.values().length];
            try {
                iArr5[LogActionType.CREATE.ordinal()] = 1;
            } catch (NoSuchFieldError e16) {
            }
            try {
                iArr5[LogActionType.UPDATE.ordinal()] = 2;
            } catch (NoSuchFieldError e17) {
            }
            try {
                iArr5[LogActionType.PUBLISH.ordinal()] = 3;
            } catch (NoSuchFieldError e18) {
            }
            try {
                iArr5[LogActionType.DELETE.ordinal()] = 4;
            } catch (NoSuchFieldError e19) {
            }
            try {
                iArr5[LogActionType.REVIEW.ordinal()] = 5;
            } catch (NoSuchFieldError e20) {
            }
            $EnumSwitchMapping$4 = iArr5;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonTypeBadge$lambda$18(ButtonType buttonType, Modifier modifier, int i, int i2, Composer composer, int i3) {
        ButtonTypeBadge(buttonType, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardStatusBadge$lambda$16(CardStatus cardStatus, Modifier modifier, int i, int i2, Composer composer, int i3) {
        CardStatusBadge(cardStatus, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit EmptyStateView$lambda$38(String str, String str2, Modifier modifier, Function2 function2, int i, int i2, Composer composer, int i3) {
        EmptyStateView(str, str2, modifier, function2, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit EnabledBadge$lambda$22(boolean z, Modifier modifier, int i, int i2, Composer composer, int i3) {
        EnabledBadge(z, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LogActionBadge$lambda$20(LogActionType logActionType, Modifier modifier, int i, int i2, Composer composer, int i3) {
        LogActionBadge(logActionType, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PageHeader$lambda$3(String str, String str2, Modifier modifier, Function3 function3, int i, int i2, Composer composer, int i3) {
        PageHeader(str, str2, modifier, function3, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PaginationBar$lambda$35(int i, int i2, int i3, Function1 function1, Modifier modifier, int i4, int i5, Composer composer, int i6) {
        PaginationBar(i, i2, i3, function1, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i4 | 1), i5);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SearchBarField$lambda$27(String str, Function1 function1, String str2, Modifier modifier, String str3, int i, int i2, Composer composer, int i3) {
        SearchBarField(str, function1, str2, modifier, str3, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit StatCard$lambda$14(StatItem statItem, Function1 function1, Modifier modifier, int i, int i2, Composer composer, int i3) {
        StatCard(statItem, function1, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:114:0x0647  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0671  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0378  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0384  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x038a  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x03bd  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x03d3  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0512  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void PageHeader(final java.lang.String r104, final java.lang.String r105, androidx.compose.ui.Modifier r106, kotlin.jvm.functions.Function3<? super androidx.compose.foundation.layout.RowScope, ? super androidx.compose.runtime.Composer, ? super java.lang.Integer, kotlin.Unit> r107, androidx.compose.runtime.Composer r108, final int r109, final int r110) {
        /*
            Method dump skipped, instructions count: 1678
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.CommonComponentsKt.PageHeader(java.lang.String, java.lang.String, androidx.compose.ui.Modifier, kotlin.jvm.functions.Function3, androidx.compose.runtime.Composer, int, int):void");
    }

    public static final void StatCard(final StatItem item, final Function1<? super AdminScreen, Unit> onNavigate, Modifier modifier, Composer $composer, final int $changed, final int i) {
        Object obj;
        Modifier modifier2;
        Triple triple;
        final ImageVector iconVec;
        long bgStart;
        Modifier.Companion companion;
        Composer $composer2;
        final Modifier modifier3;
        Function0 function0;
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(onNavigate, "onNavigate");
        Composer $composer3 = $composer.startRestartGroup(-1984921583);
        ComposerKt.sourceInformation($composer3, "C(StatCard)P(!1,2)107@3882L4038,94@3419L4501:CommonComponents.kt#qonjpd");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(item) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changedInstance(onNavigate) ? 32 : 16;
        }
        int i2 = i & 4;
        if (i2 != 0) {
            $dirty |= 384;
            obj = modifier;
        } else if (($changed & 384) == 0) {
            obj = modifier;
            $dirty |= $composer3.changed(obj) ? 256 : 128;
        } else {
            obj = modifier;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 147) == 146 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            modifier3 = obj;
            $composer2 = $composer3;
        } else {
            if (i2 != 0) {
                modifier2 = Modifier.Companion;
            } else {
                modifier2 = obj;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1984921583, $dirty2, -1, "com.example.ui.components.StatCard (CommonComponents.kt:79)");
            }
            switch (WhenMappings.$EnumSwitchMapping$0[item.getTone().ordinal()]) {
                case 1:
                    triple = new Triple(Color.m4149boximpl(ColorKt.getStatCinnabarStart()), Color.m4149boximpl(ColorKt.getStatCinnabarBorder()), Color.m4149boximpl(ColorKt.getCinnabar()));
                    break;
                case 2:
                    triple = new Triple(Color.m4149boximpl(ColorKt.getStatGoldStart()), Color.m4149boximpl(ColorKt.getStatGoldBorder()), Color.m4149boximpl(ColorKt.getStatGoldText()));
                    break;
                case 3:
                    triple = new Triple(Color.m4149boximpl(ColorKt.getStatInkStart()), Color.m4149boximpl(ColorKt.getStatInkBorder()), Color.m4149boximpl(ColorKt.getInk()));
                    break;
                case 4:
                    triple = new Triple(Color.m4149boximpl(ColorKt.getStatOrangeStart()), Color.m4149boximpl(ColorKt.getStatOrangeBorder()), Color.m4149boximpl(ColorKt.getStatOrangeText()));
                    break;
                default:
                    throw new NoWhenBranchMatchedException();
            }
            long bgStart2 = ((Color) triple.component1()).m4169unboximpl();
            long borderColor = ((Color) triple.component2()).m4169unboximpl();
            final long accentColor = ((Color) triple.component3()).m4169unboximpl();
            switch (WhenMappings.$EnumSwitchMapping$1[item.getIcon().ordinal()]) {
                case 1:
                    iconVec = LayersKt.getLayers(Icons.INSTANCE.getDefault());
                    break;
                case 2:
                    iconVec = DownloadKt.getDownload(Icons.INSTANCE.getDefault());
                    break;
                case 3:
                    iconVec = AutoAwesomeKt.getAutoAwesome(Icons.INSTANCE.getDefault());
                    break;
                case 4:
                    iconVec = VerifiedKt.getVerified(Icons.INSTANCE.getDefault());
                    break;
                default:
                    throw new NoWhenBranchMatchedException();
            }
            Modifier fillMaxWidth$default = SizeKt.fillMaxWidth$default(modifier2, 0.0f, 1, null);
            if (item.getActionScreen() != null) {
                $composer3.startReplaceGroup(311738901);
                ComposerKt.sourceInformation($composer3, "99@3591L33");
                Modifier.Companion companion2 = Modifier.Companion;
                ComposerKt.sourceInformationMarkerStart($composer3, -544131982, "CC(remember):CommonComponents.kt#9igjgp");
                bgStart = bgStart2;
                boolean z = (($dirty2 & 112) == 32) | (($dirty2 & 14) == 4);
                Object rememberedValue = $composer3.rememberedValue();
                if (!z && rememberedValue != Composer.Companion.getEmpty()) {
                    function0 = rememberedValue;
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    companion = ClickableKt.m258clickableXHw0xAI$default(companion2, false, null, null, function0, 7, null);
                    $composer3.endReplaceGroup();
                }
                function0 = new Function0() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return CommonComponentsKt.StatCard$lambda$5$lambda$4(Function1.this, item);
                    }
                };
                $composer3.updateRememberedValue(function0);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                companion = ClickableKt.m258clickableXHw0xAI$default(companion2, false, null, null, function0, 7, null);
                $composer3.endReplaceGroup();
            } else {
                bgStart = bgStart2;
                $composer3.startReplaceGroup(-544130183);
                $composer3.endReplaceGroup();
                companion = Modifier.Companion;
            }
            Modifier testTag = TestTagKt.testTag(fillMaxWidth$default.then(companion), "stat_card_" + item.getId());
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(16));
            RoundedCornerShape roundedCornerShape = m953RoundedCornerShape0680j_4;
            final long bgStart3 = bgStart;
            Modifier modifier4 = modifier2;
            $composer2 = $composer3;
            SurfaceKt.m2543SurfaceT9BRK9s(testTag, roundedCornerShape, Color.Companion.m4194getTransparent0d7_KjU(), 0L, 0.0f, Dp.m6622constructorimpl(2), BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), borderColor), ComposableLambdaKt.rememberComposableLambda(349014038, true, new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda11
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return CommonComponentsKt.StatCard$lambda$13(bgStart3, accentColor, item, iconVec, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer3, 54), $composer2, 12779904, 24);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            modifier3 = modifier4;
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda13
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return CommonComponentsKt.StatCard$lambda$14(StatItem.this, onNavigate, modifier3, $changed, i, (Composer) obj2, ((Integer) obj3).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit StatCard$lambda$5$lambda$4(Function1 $onNavigate, StatItem $item) {
        $onNavigate.invoke($item.getActionScreen());
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0966  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0972  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0978  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x09a9  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x09bf  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0a17  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0a22  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0aa4  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0b19  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0b6f  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01ea  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01fc  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0310  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x031c  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0322  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0355  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x036b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0463  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x046f  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0475  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x04a8  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x04be A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x060f  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x061b  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0621  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0654  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x066a  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x078d  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0799  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x079f  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x07d2  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x07e8 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit StatCard$lambda$13(long r127, long r129, com.example.model.StatItem r131, androidx.compose.ui.graphics.vector.ImageVector r132, androidx.compose.runtime.Composer r133, int r134) {
        /*
            Method dump skipped, instructions count: 2933
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.CommonComponentsKt.StatCard$lambda$13(long, long, com.example.model.StatItem, androidx.compose.ui.graphics.vector.ImageVector, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    public static final void CardStatusBadge(final CardStatus status, Modifier modifier, Composer $composer, final int $changed, final int i) {
        Object obj;
        long m4157copywmQWz5c;
        Pair pair;
        long m4157copywmQWz5c2;
        final Modifier modifier2;
        long m4157copywmQWz5c3;
        Intrinsics.checkNotNullParameter(status, "status");
        Composer $composer2 = $composer.startRestartGroup(-774412796);
        ComposerKt.sourceInformation($composer2, "C(CardStatusBadge)P(1)218@8355L268,214@8263L360:CommonComponents.kt#qonjpd");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changed(status.ordinal()) ? 4 : 2;
        }
        int i2 = i & 2;
        if (i2 != 0) {
            $dirty |= 48;
            obj = modifier;
        } else if (($changed & 48) == 0) {
            obj = modifier;
            $dirty |= $composer2.changed(obj) ? 32 : 16;
        } else {
            obj = modifier;
        }
        if (($dirty & 19) == 18 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
            modifier2 = obj;
        } else {
            Modifier modifier3 = i2 != 0 ? Modifier.Companion : obj;
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-774412796, $dirty, -1, "com.example.ui.components.CardStatusBadge (CommonComponents.kt:208)");
            }
            switch (WhenMappings.$EnumSwitchMapping$2[status.ordinal()]) {
                case 1:
                    m4157copywmQWz5c = Color.m4157copywmQWz5c(r6, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r6) : 0.12f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r6) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r6) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getJadeGreen()) : 0.0f);
                    pair = TuplesKt.to(Color.m4149boximpl(m4157copywmQWz5c), Color.m4149boximpl(ColorKt.getJadeGreen()));
                    break;
                case 2:
                    m4157copywmQWz5c2 = Color.m4157copywmQWz5c(r6, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r6) : 0.16f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r6) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r6) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getGold()) : 0.0f);
                    pair = TuplesKt.to(Color.m4149boximpl(m4157copywmQWz5c2), Color.m4149boximpl(ColorKt.getGoldDark()));
                    break;
                case 3:
                    Color m4149boximpl = Color.m4149boximpl(ColorKt.getMist());
                    m4157copywmQWz5c3 = Color.m4157copywmQWz5c(r6, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r6) : 0.65f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r6) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r6) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
                    pair = TuplesKt.to(m4149boximpl, Color.m4149boximpl(m4157copywmQWz5c3));
                    break;
                default:
                    throw new NoWhenBranchMatchedException();
            }
            long bg = ((Color) pair.component1()).m4169unboximpl();
            final long fg = ((Color) pair.component2()).m4169unboximpl();
            modifier2 = modifier3;
            SurfaceKt.m2543SurfaceT9BRK9s(modifier2, RoundedCornerShapeKt.getCircleShape(), bg, 0L, 0.0f, 0.0f, null, ComposableLambdaKt.rememberComposableLambda(547801439, true, new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda19
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return CommonComponentsKt.CardStatusBadge$lambda$15(CardStatus.this, fg, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer2, 54), $composer2, (($dirty >> 3) & 14) | 12582912, 120);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda20
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return CommonComponentsKt.CardStatusBadge$lambda$16(CardStatus.this, modifier2, $changed, i, (Composer) obj2, ((Integer) obj3).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardStatusBadge$lambda$15(CardStatus $status, long $fg, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C222@8462L10,219@8365L252:CommonComponents.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(547801439, $changed, -1, "com.example.ui.components.CardStatusBadge.<anonymous> (CommonComponents.kt:219)");
            }
            String label = $status.getLabel();
            TextStyle labelSmall = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall();
            TextKt.m2693Text4IGK_g(label, PaddingKt.m671paddingVpY3zN4(Modifier.Companion, Dp.m6622constructorimpl(10), Dp.m6622constructorimpl(4)), $fg, 0L, (FontStyle) null, FontWeight.Companion.getSemiBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, labelSmall, $composer, 196656, 0, 65496);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    public static final void ButtonTypeBadge(final ButtonType type, Modifier modifier, Composer $composer, final int $changed, final int i) {
        Object obj;
        long m4157copywmQWz5c;
        Pair pair;
        long m4157copywmQWz5c2;
        long m4157copywmQWz5c3;
        final Modifier modifier2;
        long m4157copywmQWz5c4;
        Intrinsics.checkNotNullParameter(type, "type");
        Composer $composer2 = $composer.startRestartGroup(442978736);
        ComposerKt.sourceInformation($composer2, "C(ButtonTypeBadge)P(1)242@9208L263,237@9018L453:CommonComponents.kt#qonjpd");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changed(type.ordinal()) ? 4 : 2;
        }
        int i2 = i & 2;
        if (i2 != 0) {
            $dirty |= 48;
            obj = modifier;
        } else if (($changed & 48) == 0) {
            obj = modifier;
            $dirty |= $composer2.changed(obj) ? 32 : 16;
        } else {
            obj = modifier;
        }
        if (($dirty & 19) == 18 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
            modifier2 = obj;
        } else {
            Modifier modifier3 = i2 != 0 ? Modifier.Companion : obj;
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(442978736, $dirty, -1, "com.example.ui.components.ButtonTypeBadge (CommonComponents.kt:230)");
            }
            switch (WhenMappings.$EnumSwitchMapping$3[type.ordinal()]) {
                case 1:
                    m4157copywmQWz5c = Color.m4157copywmQWz5c(r6, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r6) : 0.12f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r6) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r6) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getCinnabar()) : 0.0f);
                    pair = TuplesKt.to(Color.m4149boximpl(m4157copywmQWz5c), Color.m4149boximpl(ColorKt.getCinnabar()));
                    break;
                case 2:
                    m4157copywmQWz5c2 = Color.m4157copywmQWz5c(r6, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r6) : 0.12f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r6) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r6) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInk()) : 0.0f);
                    pair = TuplesKt.to(Color.m4149boximpl(m4157copywmQWz5c2), Color.m4149boximpl(ColorKt.getInk()));
                    break;
                case 3:
                    m4157copywmQWz5c3 = Color.m4157copywmQWz5c(r6, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r6) : 0.16f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r6) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r6) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getGold()) : 0.0f);
                    pair = TuplesKt.to(Color.m4149boximpl(m4157copywmQWz5c3), Color.m4149boximpl(ColorKt.getGoldDark()));
                    break;
                case 4:
                    Color m4149boximpl = Color.m4149boximpl(ColorKt.getMistSoft());
                    m4157copywmQWz5c4 = Color.m4157copywmQWz5c(r6, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r6) : 0.75f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r6) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r6) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
                    pair = TuplesKt.to(m4149boximpl, Color.m4149boximpl(m4157copywmQWz5c4));
                    break;
                default:
                    throw new NoWhenBranchMatchedException();
            }
            long bg = ((Color) pair.component1()).m4169unboximpl();
            final long fg = ((Color) pair.component2()).m4169unboximpl();
            modifier2 = modifier3;
            SurfaceKt.m2543SurfaceT9BRK9s(modifier2, RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(8)), bg, 0L, 0.0f, 0.0f, type == ButtonType.CONTACT ? BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist()) : null, ComposableLambdaKt.rememberComposableLambda(1765192971, true, new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return CommonComponentsKt.ButtonTypeBadge$lambda$17(ButtonType.this, fg, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer2, 54), $composer2, (($dirty >> 3) & 14) | 12582912, 56);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return CommonComponentsKt.ButtonTypeBadge$lambda$18(ButtonType.this, modifier2, $changed, i, (Composer) obj2, ((Integer) obj3).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonTypeBadge$lambda$17(ButtonType $type, long $fg, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C246@9313L10,243@9218L247:CommonComponents.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1765192971, $changed, -1, "com.example.ui.components.ButtonTypeBadge.<anonymous> (CommonComponents.kt:243)");
            }
            String label = $type.getLabel();
            TextStyle labelSmall = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall();
            TextKt.m2693Text4IGK_g(label, PaddingKt.m671paddingVpY3zN4(Modifier.Companion, Dp.m6622constructorimpl(8), Dp.m6622constructorimpl(3)), $fg, 0L, (FontStyle) null, FontWeight.Companion.getMedium(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, labelSmall, $composer, 196656, 0, 65496);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    public static final void LogActionBadge(final LogActionType action, Modifier modifier, Composer $composer, final int $changed, final int i) {
        Object obj;
        long m4157copywmQWz5c;
        Pair pair;
        long m4157copywmQWz5c2;
        long m4157copywmQWz5c3;
        long m4157copywmQWz5c4;
        final Modifier modifier2;
        long m4157copywmQWz5c5;
        Intrinsics.checkNotNullParameter(action, "action");
        Composer $composer2 = $composer.startRestartGroup(1862948632);
        ComposerKt.sourceInformation($composer2, "C(LogActionBadge)266@10066L267,262@9961L372:CommonComponents.kt#qonjpd");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changed(action.ordinal()) ? 4 : 2;
        }
        int i2 = i & 2;
        if (i2 != 0) {
            $dirty |= 48;
            obj = modifier;
        } else if (($changed & 48) == 0) {
            obj = modifier;
            $dirty |= $composer2.changed(obj) ? 32 : 16;
        } else {
            obj = modifier;
        }
        if (($dirty & 19) == 18 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
            modifier2 = obj;
        } else {
            Modifier modifier3 = i2 != 0 ? Modifier.Companion : obj;
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1862948632, $dirty, -1, "com.example.ui.components.LogActionBadge (CommonComponents.kt:254)");
            }
            switch (WhenMappings.$EnumSwitchMapping$4[action.ordinal()]) {
                case 1:
                    m4157copywmQWz5c = Color.m4157copywmQWz5c(r6, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r6) : 0.12f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r6) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r6) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getJadeGreen()) : 0.0f);
                    pair = TuplesKt.to(Color.m4149boximpl(m4157copywmQWz5c), Color.m4149boximpl(ColorKt.getJadeGreen()));
                    break;
                case 2:
                    m4157copywmQWz5c2 = Color.m4157copywmQWz5c(r6, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r6) : 0.16f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r6) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r6) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getGold()) : 0.0f);
                    pair = TuplesKt.to(Color.m4149boximpl(m4157copywmQWz5c2), Color.m4149boximpl(ColorKt.getGoldDark()));
                    break;
                case 3:
                    m4157copywmQWz5c3 = Color.m4157copywmQWz5c(r6, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r6) : 0.12f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r6) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r6) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getCinnabar()) : 0.0f);
                    pair = TuplesKt.to(Color.m4149boximpl(m4157copywmQWz5c3), Color.m4149boximpl(ColorKt.getCinnabar()));
                    break;
                case 4:
                    m4157copywmQWz5c4 = Color.m4157copywmQWz5c(r6, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r6) : 0.18f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r6) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r6) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getCinnabar()) : 0.0f);
                    pair = TuplesKt.to(Color.m4149boximpl(m4157copywmQWz5c4), Color.m4149boximpl(ColorKt.getCinnabar()));
                    break;
                case 5:
                    m4157copywmQWz5c5 = Color.m4157copywmQWz5c(r6, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r6) : 0.12f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r6) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r6) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInk()) : 0.0f);
                    pair = TuplesKt.to(Color.m4149boximpl(m4157copywmQWz5c5), Color.m4149boximpl(ColorKt.getInk()));
                    break;
                default:
                    throw new NoWhenBranchMatchedException();
            }
            long bg = ((Color) pair.component1()).m4169unboximpl();
            final long fg = ((Color) pair.component2()).m4169unboximpl();
            modifier2 = modifier3;
            SurfaceKt.m2543SurfaceT9BRK9s(modifier2, RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(8)), bg, 0L, 0.0f, 0.0f, null, ComposableLambdaKt.rememberComposableLambda(1200508851, true, new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda14
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return CommonComponentsKt.LogActionBadge$lambda$19(LogActionType.this, fg, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer2, 54), $composer2, (($dirty >> 3) & 14) | 12582912, 120);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda15
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return CommonComponentsKt.LogActionBadge$lambda$20(LogActionType.this, modifier2, $changed, i, (Composer) obj2, ((Integer) obj3).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LogActionBadge$lambda$19(LogActionType $action, long $fg, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C270@10173L10,267@10076L251:CommonComponents.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1200508851, $changed, -1, "com.example.ui.components.LogActionBadge.<anonymous> (CommonComponents.kt:267)");
            }
            String label = $action.getLabel();
            TextStyle labelSmall = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall();
            TextKt.m2693Text4IGK_g(label, PaddingKt.m671paddingVpY3zN4(Modifier.Companion, Dp.m6622constructorimpl(8), Dp.m6622constructorimpl(3)), $fg, 0L, (FontStyle) null, FontWeight.Companion.getSemiBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, labelSmall, $composer, 196656, 0, 65496);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    public static final void EnabledBadge(final boolean enabled, Modifier modifier, Composer $composer, final int $changed, final int i) {
        Object obj;
        long bg;
        final long fg;
        final Modifier modifier2;
        Composer $composer2 = $composer.startRestartGroup(2042160779);
        ComposerKt.sourceInformation($composer2, "C(EnabledBadge)285@10648L283,281@10556L375:CommonComponents.kt#qonjpd");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changed(enabled) ? 4 : 2;
        }
        int i2 = i & 2;
        if (i2 != 0) {
            $dirty |= 48;
            obj = modifier;
        } else if (($changed & 48) == 0) {
            obj = modifier;
            $dirty |= $composer2.changed(obj) ? 32 : 16;
        } else {
            obj = modifier;
        }
        if (($dirty & 19) == 18 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
            modifier2 = obj;
        } else {
            Modifier modifier3 = i2 != 0 ? Modifier.Companion : obj;
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(2042160779, $dirty, -1, "com.example.ui.components.EnabledBadge (CommonComponents.kt:278)");
            }
            if (enabled) {
                bg = Color.m4157copywmQWz5c(r15, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r15) : 0.12f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r15) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r15) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getJadeGreen()) : 0.0f);
            } else {
                bg = ColorKt.getMist();
            }
            if (enabled) {
                fg = ColorKt.getJadeGreen();
            } else {
                fg = Color.m4157copywmQWz5c(r15, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r15) : 0.6f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r15) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r15) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
            }
            modifier2 = modifier3;
            SurfaceKt.m2543SurfaceT9BRK9s(modifier2, RoundedCornerShapeKt.getCircleShape(), bg, 0L, 0.0f, 0.0f, null, ComposableLambdaKt.rememberComposableLambda(-482301402, true, new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda17
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return CommonComponentsKt.EnabledBadge$lambda$21(enabled, fg, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer2, 54), $composer2, (($dirty >> 3) & 14) | 12582912, 120);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda18
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return CommonComponentsKt.EnabledBadge$lambda$22(enabled, modifier2, $changed, i, (Composer) obj2, ((Integer) obj3).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit EnabledBadge$lambda$21(boolean $enabled, long $fg, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C289@10770L10,286@10658L267:CommonComponents.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-482301402, $changed, -1, "com.example.ui.components.EnabledBadge.<anonymous> (CommonComponents.kt:286)");
            }
            String str = $enabled ? "启用" : "停用";
            String str2 = str;
            TextKt.m2693Text4IGK_g(str2, PaddingKt.m671paddingVpY3zN4(Modifier.Companion, Dp.m6622constructorimpl(10), Dp.m6622constructorimpl(4)), $fg, 0L, (FontStyle) null, FontWeight.Companion.getSemiBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall(), $composer, 196656, 0, 65496);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    public static final void SearchBarField(final String value, final Function1<? super String, Unit> onValueChange, final String placeholder, Modifier modifier, String testTag, Composer $composer, final int $changed, final int i) {
        Modifier modifier2;
        Object testTag2;
        Composer $composer2;
        final String testTag3;
        final Modifier modifier3;
        Intrinsics.checkNotNullParameter(value, "value");
        Intrinsics.checkNotNullParameter(onValueChange, "onValueChange");
        Intrinsics.checkNotNullParameter(placeholder, "placeholder");
        Composer $composer3 = $composer.startRestartGroup(-588080698);
        ComposerKt.sourceInformation($composer3, "C(SearchBarField)P(4,1,2)336@12222L196,307@11231L273,323@11744L363,304@11128L1383:CommonComponents.kt#qonjpd");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(value) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changedInstance(onValueChange) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            $dirty |= $composer3.changed(placeholder) ? 256 : 128;
        }
        int i2 = i & 8;
        if (i2 != 0) {
            $dirty |= 3072;
            modifier2 = modifier;
        } else if (($changed & 3072) == 0) {
            modifier2 = modifier;
            $dirty |= $composer3.changed(modifier2) ? 2048 : 1024;
        } else {
            modifier2 = modifier;
        }
        int i3 = i & 16;
        if (i3 != 0) {
            $dirty |= 24576;
            testTag2 = testTag;
        } else if (($changed & 24576) == 0) {
            testTag2 = testTag;
            $dirty |= $composer3.changed(testTag2) ? 16384 : 8192;
        } else {
            testTag2 = testTag;
        }
        if (($dirty & 9363) == 9362 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
            modifier3 = modifier2;
            testTag3 = testTag2;
        } else {
            if (i2 != 0) {
                modifier2 = Modifier.Companion;
            }
            if (i3 != 0) {
                testTag2 = "search_input";
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-588080698, $dirty, -1, "com.example.ui.components.SearchBarField (CommonComponents.kt:303)");
            }
            $composer2 = $composer3;
            int $dirty2 = $dirty;
            Modifier modifier4 = modifier2;
            String testTag4 = testTag2;
            OutlinedTextFieldKt.OutlinedTextField(value, onValueChange, TestTagKt.testTag(SizeKt.fillMaxWidth$default(modifier4, 0.0f, 1, null), testTag4), false, false, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableLambdaKt.rememberComposableLambda(-1408735187, true, new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda9
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return CommonComponentsKt.SearchBarField$lambda$23(placeholder, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer2, 54), (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$CommonComponentsKt.INSTANCE.getLambda$952025710$app(), (Function2<? super Composer, ? super Integer, Unit>) ComposableLambdaKt.rememberComposableLambda(-982180689, true, new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda10
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return CommonComponentsKt.SearchBarField$lambda$26(value, onValueChange, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer2, 54), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, true, 0, 0, (MutableInteractionSource) null, (Shape) RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12)), OutlinedTextFieldDefaults.INSTANCE.m2343colors0hiis_0(0L, 0L, 0L, 0L, ColorKt.getPaperSoft(), ColorKt.getPaperSoft(), 0L, 0L, 0L, 0L, null, ColorKt.getCinnabar(), ColorKt.getMist(), 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, $composer2, 221184, 432, 0, 0, 3072, 2147477455, 4095), $composer2, ($dirty2 & 14) | 918552576 | ($dirty2 & 112), 12582912, 0, 1965176);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            testTag3 = testTag4;
            modifier3 = modifier4;
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda12
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return CommonComponentsKt.SearchBarField$lambda$27(value, onValueChange, placeholder, modifier3, testTag3, $changed, i, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SearchBarField$lambda$23(String $placeholder, Composer $composer, int $changed) {
        long m4157copywmQWz5c;
        ComposerKt.sourceInformation($composer, "C310@11325L10,308@11245L249:CommonComponents.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1408735187, $changed, -1, "com.example.ui.components.SearchBarField.<anonymous> (CommonComponents.kt:308)");
            }
            TextStyle bodyMedium = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getBodyMedium();
            m4157copywmQWz5c = Color.m4157copywmQWz5c(r2, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r2) : 0.45f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r2) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r2) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
            TextKt.m2693Text4IGK_g($placeholder, (Modifier) null, m4157copywmQWz5c, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, TextOverflow.Companion.m6539getEllipsisgIe3tQ8(), false, 1, 0, (Function1<? super TextLayoutResult, Unit>) null, bodyMedium, $composer, 384, 3120, 55290);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SearchBarField$lambda$26(String $value, final Function1 $onValueChange, Composer $composer, int $changed) {
        Function0 function0;
        ComposerKt.sourceInformation($composer, "C:CommonComponents.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-982180689, $changed, -1, "com.example.ui.components.SearchBarField.<anonymous> (CommonComponents.kt:324)");
            }
            if ($value.length() > 0) {
                $composer.startReplaceGroup(1467329112);
                ComposerKt.sourceInformation($composer, "325@11821L21,325@11800L283");
                ComposerKt.sourceInformationMarkerStart($composer, 1017165476, "CC(remember):CommonComponents.kt#9igjgp");
                boolean changed = $composer.changed($onValueChange);
                Object rememberedValue = $composer.rememberedValue();
                if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                    function0 = new Function0() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda8
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return CommonComponentsKt.SearchBarField$lambda$26$lambda$25$lambda$24(Function1.this);
                        }
                    };
                    $composer.updateRememberedValue(function0);
                } else {
                    function0 = rememberedValue;
                }
                ComposerKt.sourceInformationMarkerEnd($composer);
                IconButtonKt.IconButton(function0, null, false, null, null, ComposableSingletons$CommonComponentsKt.INSTANCE.m6935getLambda$605614937$app(), $composer, ProfileVerifier.CompilationStatus.RESULT_CODE_ERROR_CANT_WRITE_PROFILE_VERIFICATION_RESULT_CACHE_FILE, 30);
            } else {
                $composer.startReplaceGroup(1455630611);
            }
            $composer.endReplaceGroup();
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SearchBarField$lambda$26$lambda$25$lambda$24(Function1 $onValueChange) {
        $onValueChange.invoke("");
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x03ec  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x03fa  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0478  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x047b  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x04bc  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x04be  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x04ca  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x04cd  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x04da  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x04e7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:128:0x054d  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x02dd  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02e9  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x02ef  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0320  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0336  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0386  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0389  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x03d3  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x03d5  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x03dc  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x03de  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void PaginationBar(final int r81, final int r82, final int r83, final kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> r84, androidx.compose.ui.Modifier r85, androidx.compose.runtime.Composer r86, final int r87, final int r88) {
        /*
            Method dump skipped, instructions count: 1385
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.CommonComponentsKt.PaginationBar(int, int, int, kotlin.jvm.functions.Function1, androidx.compose.ui.Modifier, androidx.compose.runtime.Composer, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PaginationBar$lambda$34$lambda$33$lambda$29$lambda$28(int $page, Function1 $onPageChange) {
        if ($page > 1) {
            $onPageChange.invoke(Integer.valueOf($page - 1));
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PaginationBar$lambda$34$lambda$33$lambda$30(int $page, int $totalPages, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C398@14239L10,396@14141L312:CommonComponents.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1072314675, $changed, -1, "com.example.ui.components.PaginationBar.<anonymous>.<anonymous>.<anonymous> (CommonComponents.kt:396)");
            }
            TextKt.m2693Text4IGK_g($page + " / " + $totalPages, PaddingKt.m671paddingVpY3zN4(Modifier.Companion, Dp.m6622constructorimpl(12), Dp.m6622constructorimpl(8)), ColorKt.getCinnabar(), 0L, (FontStyle) null, FontWeight.Companion.getBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelMedium(), $composer, 197040, 0, 65496);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PaginationBar$lambda$34$lambda$33$lambda$32$lambda$31(int $page, int $totalPages, Function1 $onPageChange) {
        if ($page < $totalPages) {
            $onPageChange.invoke(Integer.valueOf($page + 1));
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:69:0x025e  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x026a  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0270  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02a1  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x02b7  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x03f8  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0422  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0448  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void EmptyStateView(final java.lang.String r65, final java.lang.String r66, androidx.compose.ui.Modifier r67, kotlin.jvm.functions.Function2<? super androidx.compose.runtime.Composer, ? super java.lang.Integer, kotlin.Unit> r68, androidx.compose.runtime.Composer r69, final int r70, final int r71) {
        /*
            Method dump skipped, instructions count: 1125
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.CommonComponentsKt.EmptyStateView(java.lang.String, java.lang.String, androidx.compose.ui.Modifier, kotlin.jvm.functions.Function2, androidx.compose.runtime.Composer, int, int):void");
    }
}
