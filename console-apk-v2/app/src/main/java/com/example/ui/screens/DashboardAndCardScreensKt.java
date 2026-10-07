package com.example.ui.screens;

import androidx.autofill.HintConstants;
import androidx.compose.foundation.BorderStrokeKt;
import androidx.compose.foundation.layout.PaddingKt;
import androidx.compose.foundation.layout.PaddingValues;
import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.SpacerKt;
import androidx.compose.foundation.shape.RoundedCornerShape;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.material3.ButtonColors;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.SurfaceKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.platform.TestTagKt;
import androidx.compose.ui.unit.Dp;
import androidx.core.app.NotificationCompat;
import com.example.model.ActivityLog;
import com.example.model.ButtonType;
import com.example.model.CardStatus;
import com.example.ui.theme.ColorKt;
import com.example.viewmodel.AdminUiState;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DashboardAndCardScreens.kt */
@Metadata(d1 = {"\u0000H\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\u001a\u0088\u0003\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u00052 \u0002\u0010\t\u001a\u009b\u0002\u0012\u0015\u0012\u0013\u0018\u00010\u000b¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\r\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0012\u0012\u0013\u0012\u00110\u0013¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0014\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0016\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0017\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0019\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u001a\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u0005H\u0007¢\u0006\u0002\u0010\u001e\u001a\u0015\u0010\u001f\u001a\u00020\u00012\u0006\u0010 \u001a\u00020!H\u0007¢\u0006\u0002\u0010\"\u001aà\u0002\u0010#\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032 \u0002\u0010\t\u001a\u009b\u0002\u0012\u0015\u0012\u0013\u0018\u00010\u000b¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\r\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0012\u0012\u0013\u0012\u00110\u0013¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0014\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0016\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0017\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0019\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u001a\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u0005H\u0007¢\u0006\u0002\u0010$¨\u0006%²\u0006\n\u0010&\u001a\u00020'X\u008a\u008e\u0002"}, d2 = {"DashboardScreen", "", "uiState", "Lcom/example/viewmodel/AdminUiState;", "onDateRangeChange", "Lkotlin/Function1;", "", "onNavigate", "Lcom/example/model/AdminScreen;", "onSaveCard", "Lkotlin/Function13;", "Lcom/example/model/ResourceCard;", "Lkotlin/ParameterName;", HintConstants.AUTOFILL_HINT_NAME, "existing", "desc", "url", "Lcom/example/model/ButtonType;", "btnType", "Lcom/example/model/CardStatus;", NotificationCompat.CATEGORY_STATUS, "category", "subcatId", "icon", "fallbackText", "badge", "badgeType", "highlights", "onDeleteCard", "onShowToast", "(Lcom/example/viewmodel/AdminUiState;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function13;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "ActivityLogRow", "log", "Lcom/example/model/ActivityLog;", "(Lcom/example/model/ActivityLog;Landroidx/compose/runtime/Composer;I)V", "CardManagementScreen", "(Lcom/example/viewmodel/AdminUiState;Lkotlin/jvm/functions/Function13;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "app", "createOpen", ""}, k = 2, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class DashboardAndCardScreensKt {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ActivityLogRow$lambda$35(ActivityLog activityLog, int i, Composer composer, int i2) {
        ActivityLogRow(activityLog, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardManagementScreen$lambda$40(AdminUiState adminUiState, Function13 function13, Function1 function1, Function1 function12, int i, Composer composer, int i2) {
        CardManagementScreen(adminUiState, function13, function1, function12, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit DashboardScreen$lambda$28(AdminUiState adminUiState, Function1 function1, Function1 function12, Function13 function13, Function1 function14, Function1 function15, int i, Composer composer, int i2) {
        DashboardScreen(adminUiState, function1, function12, function13, function14, function15, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x0451  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x059b A[LOOP:1: B:119:0x0595->B:121:0x059b, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0614  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0635  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0697  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x07ac  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x07ba  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x07d3  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x07d5  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x080f  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0308  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void DashboardScreen(final com.example.viewmodel.AdminUiState r107, final kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> r108, final kotlin.jvm.functions.Function1<? super com.example.model.AdminScreen, kotlin.Unit> r109, kotlin.jvm.functions.Function13<? super com.example.model.ResourceCard, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super com.example.model.ButtonType, ? super com.example.model.CardStatus, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> r110, final kotlin.jvm.functions.Function1<? super com.example.model.ResourceCard, kotlin.Unit> r111, final kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> r112, androidx.compose.runtime.Composer r113, final int r114) {
        /*
            Method dump skipped, instructions count: 2092
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.DashboardAndCardScreensKt.DashboardScreen(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function13, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function1, androidx.compose.runtime.Composer, int):void");
    }

    private static final boolean DashboardScreen$lambda$1(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void DashboardScreen$lambda$2(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit DashboardScreen$lambda$23$lambda$13(final List $dateRanges, final AdminUiState $uiState, final Function1 $onDateRangeChange, final MutableState $createOpen$delegate, RowScope PageHeader, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter(PageHeader, "$this$PageHeader");
        ComposerKt.sourceInformation($composer, "C76@2617L1361,71@2376L1602,103@3996L38,107@4157L131,106@4090L21,105@4052L847:DashboardAndCardScreens.kt#2thlc2");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer.changed(PageHeader) ? 4 : 2;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1775991406, $dirty2, -1, "com.example.ui.screens.DashboardScreen.<anonymous>.<anonymous> (DashboardAndCardScreens.kt:71)");
            }
            SurfaceKt.m2543SurfaceT9BRK9s(PageHeader.weight(Modifier.Companion, 1.0f, false), RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12)), ColorKt.getPaperSoft(), 0L, 0.0f, 0.0f, BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist()), ComposableLambdaKt.rememberComposableLambda(851820183, true, new Function2() { // from class: com.example.ui.screens.DashboardAndCardScreensKt$$ExternalSyntheticLambda7
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return DashboardAndCardScreensKt.DashboardScreen$lambda$23$lambda$13$lambda$10($dateRanges, $uiState, $onDateRangeChange, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer, 54), $composer, 14156160, 56);
            SpacerKt.Spacer(RowScope.weight$default(PageHeader, Modifier.Companion, 1.0f, false, 2, null), $composer, 0);
            ButtonColors m1809buttonColorsro_MJ88 = ButtonDefaults.INSTANCE.m1809buttonColorsro_MJ88(ColorKt.getCinnabar(), ColorKt.getPaper(), 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 54, 12);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            PaddingValues m664PaddingValuesYgX7TsA = PaddingKt.m664PaddingValuesYgX7TsA(Dp.m6622constructorimpl(14), Dp.m6622constructorimpl(10));
            Modifier testTag = TestTagKt.testTag(Modifier.Companion, "dashboard_create_card_btn");
            ComposerKt.sourceInformationMarkerStart($composer, -1625669209, "CC(remember):DashboardAndCardScreens.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.DashboardAndCardScreensKt$$ExternalSyntheticLambda8
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DashboardAndCardScreensKt.DashboardScreen$lambda$23$lambda$13$lambda$12$lambda$11(MutableState.this);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button((Function0) obj, testTag, false, m953RoundedCornerShape0680j_4, m1809buttonColorsro_MJ88, null, null, m664PaddingValuesYgX7TsA, null, ComposableSingletons$DashboardAndCardScreensKt.INSTANCE.m6997getLambda$1910081630$app(), $composer, 817889334, 356);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:29:0x016e  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0296  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x02a2  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x02a8  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x034f  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0354  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x036f  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0374  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x03f8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit DashboardScreen$lambda$23$lambda$13$lambda$10(java.util.List r79, com.example.viewmodel.AdminUiState r80, kotlin.jvm.functions.Function1 r81, androidx.compose.runtime.Composer r82, int r83) {
        /*
            Method dump skipped, instructions count: 1022
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.DashboardAndCardScreensKt.DashboardScreen$lambda$23$lambda$13$lambda$10(java.util.List, com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function1, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit DashboardScreen$lambda$23$lambda$13$lambda$10$lambda$9$lambda$8$lambda$6$lambda$5(Function1 $onDateRangeChange, String $r) {
        $onDateRangeChange.invoke($r);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit DashboardScreen$lambda$23$lambda$13$lambda$12$lambda$11(MutableState $createOpen$delegate) {
        DashboardScreen$lambda$2($createOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01d6  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x039b  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x03a7  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x03ad  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x03de  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x03f4  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0464 A[LOOP:0: B:57:0x045e->B:59:0x0464, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x04b7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit DashboardScreen$lambda$23$lambda$22(com.example.viewmodel.AdminUiState r84, androidx.compose.runtime.Composer r85, int r86) {
        /*
            Method dump skipped, instructions count: 1213
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.DashboardAndCardScreensKt.DashboardScreen$lambda$23$lambda$22(com.example.viewmodel.AdminUiState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit DashboardScreen$lambda$25$lambda$24(MutableState $createOpen$delegate) {
        DashboardScreen$lambda$2($createOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit DashboardScreen$lambda$27$lambda$26(Function13 $onSaveCard, String name, String desc, String url, ButtonType btnType, CardStatus status, String cat, String subcatId, String icon, String fallbackText, String badge, String badgeType, String highlights) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(desc, "desc");
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(btnType, "btnType");
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(cat, "cat");
        Intrinsics.checkNotNullParameter(subcatId, "subcatId");
        Intrinsics.checkNotNullParameter(icon, "icon");
        Intrinsics.checkNotNullParameter(fallbackText, "fallbackText");
        Intrinsics.checkNotNullParameter(badge, "badge");
        Intrinsics.checkNotNullParameter(badgeType, "badgeType");
        Intrinsics.checkNotNullParameter(highlights, "highlights");
        $onSaveCard.invoke(null, name, desc, url, btnType, status, cat, subcatId, icon, fallbackText, badge, badgeType, highlights);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0225  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0231  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0237  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x03b1  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x03bd  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x03c3  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x03f4  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x040a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x05eb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void ActivityLogRow(final com.example.model.ActivityLog r103, androidx.compose.runtime.Composer r104, final int r105) {
        /*
            Method dump skipped, instructions count: 1551
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.DashboardAndCardScreensKt.ActivityLogRow(com.example.model.ActivityLog, androidx.compose.runtime.Composer, int):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:74:0x033a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void CardManagementScreen(final com.example.viewmodel.AdminUiState r41, final kotlin.jvm.functions.Function13<? super com.example.model.ResourceCard, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super com.example.model.ButtonType, ? super com.example.model.CardStatus, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> r42, final kotlin.jvm.functions.Function1<? super com.example.model.ResourceCard, kotlin.Unit> r43, final kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> r44, androidx.compose.runtime.Composer r45, final int r46) {
        /*
            Method dump skipped, instructions count: 854
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.DashboardAndCardScreensKt.CardManagementScreen(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function13, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function1, androidx.compose.runtime.Composer, int):void");
    }
}
