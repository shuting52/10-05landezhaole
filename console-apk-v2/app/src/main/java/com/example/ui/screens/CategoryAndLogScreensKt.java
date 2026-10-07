package com.example.ui.screens;

import androidx.autofill.HintConstants;
import androidx.compose.foundation.BorderStroke;
import androidx.compose.foundation.BorderStrokeKt;
import androidx.compose.foundation.layout.PaddingKt;
import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.layout.SpacerKt;
import androidx.compose.foundation.shape.RoundedCornerShape;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.ArrowDownwardKt;
import androidx.compose.material.icons.filled.ArrowUpwardKt;
import androidx.compose.material3.ButtonColors;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.IconKt;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.MutableIntState;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.platform.TestTagKt;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.unit.Dp;
import com.example.model.CategoryItem;
import com.example.model.SubCategoryItem;
import com.example.ui.theme.ColorKt;
import com.example.viewmodel.AdminUiState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
/* compiled from: CategoryAndLogScreens.kt */
@Metadata(d1 = {"\u0000T\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\u001a\u0082\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000326\u0010\u0004\u001a2\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\r2}\u0010\u000e\u001ay\u0012\u0015\u0012\u0013\u0018\u00010\u0010¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0011\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\b\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0012\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0013\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020\u00150\u0014¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u000f2\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00010\u00182\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u0018H\u0007¢\u0006\u0002\u0010\u001a\u001a)\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u0018H\u0007¢\u0006\u0002\u0010\u001c¨\u0006\u001d²\u0006\n\u0010\u001e\u001a\u00020\u001fX\u008a\u008e\u0002²\u0006\f\u0010 \u001a\u0004\u0018\u00010\u0010X\u008a\u008e\u0002²\u0006\n\u0010!\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010\"\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010#\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010$\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\f\u0010%\u001a\u0004\u0018\u00010\u0010X\u008a\u008e\u0002²\u0006\n\u0010&\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010'\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010(\u001a\u00020\nX\u008a\u008e\u0002"}, d2 = {"CategoryManagementScreen", "", "uiState", "Lcom/example/viewmodel/AdminUiState;", "onMoveCategory", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", HintConstants.AUTOFILL_HINT_NAME, "id", "", "dir", "onSaveOrder", "Lkotlin/Function0;", "onSaveCategory", "Lkotlin/Function5;", "Lcom/example/model/CategoryItem;", "existing", "iconKey", "desc", "", "Lcom/example/model/SubCategoryItem;", "subcategories", "onDeleteCategory", "Lkotlin/Function1;", "onShowToast", "(Lcom/example/viewmodel/AdminUiState;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function5;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "OperationLogsScreen", "(Lcom/example/viewmodel/AdminUiState;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "app", "dialogOpen", "", "editingCat", "catNameInput", "catIconInput", "catDescInput", "catSubcatsText", "deletingCat", "keyword", "selectedAction", "page"}, k = 2, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class CategoryAndLogScreensKt {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$80(AdminUiState adminUiState, Function2 function2, Function0 function0, Function5 function5, Function1 function1, Function1 function12, int i, Composer composer, int i2) {
        CategoryManagementScreen(adminUiState, function2, function0, function5, function1, function12, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit OperationLogsScreen$lambda$118(AdminUiState adminUiState, Function1 function1, int i, Composer composer, int i2) {
        OperationLogsScreen(adminUiState, function1, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:109:0x04c2  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0588  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x059c  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x059e  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x05cc  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x05da  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x05f3  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x05f6  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x063f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void CategoryManagementScreen(final com.example.viewmodel.AdminUiState r61, final kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.Integer, kotlin.Unit> r62, final kotlin.jvm.functions.Function0<kotlin.Unit> r63, final kotlin.jvm.functions.Function5<? super com.example.model.CategoryItem, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.util.List<com.example.model.SubCategoryItem>, kotlin.Unit> r64, kotlin.jvm.functions.Function1<? super com.example.model.CategoryItem, kotlin.Unit> r65, final kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> r66, androidx.compose.runtime.Composer r67, final int r68) {
        /*
            Method dump skipped, instructions count: 1630
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.CategoryAndLogScreensKt.CategoryManagementScreen(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function2, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function5, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function1, androidx.compose.runtime.Composer, int):void");
    }

    private static final boolean CategoryManagementScreen$lambda$1(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void CategoryManagementScreen$lambda$2(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final CategoryItem CategoryManagementScreen$lambda$4(MutableState<CategoryItem> mutableState) {
        return mutableState.getValue();
    }

    private static final String CategoryManagementScreen$lambda$7(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CategoryManagementScreen$lambda$10(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CategoryManagementScreen$lambda$13(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CategoryManagementScreen$lambda$16(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final CategoryItem CategoryManagementScreen$lambda$19(MutableState<CategoryItem> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$52$lambda$26(final MutableState $editingCat$delegate, final MutableState $catNameInput$delegate, final MutableState $catIconInput$delegate, final MutableState $catDescInput$delegate, final MutableState $catSubcatsText$delegate, final MutableState $dialogOpen$delegate, RowScope PageHeader, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter(PageHeader, "$this$PageHeader");
        ComposerKt.sourceInformation($composer, "C69@2609L38,79@3037L61,71@2702L289,70@2664L796:CategoryAndLogScreens.kt#2thlc2");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer.changed(PageHeader) ? 4 : 2;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1615257976, $dirty2, -1, "com.example.ui.screens.CategoryManagementScreen.<anonymous>.<anonymous> (CategoryAndLogScreens.kt:69)");
            }
            SpacerKt.Spacer(RowScope.weight$default(PageHeader, Modifier.Companion, 1.0f, false, 2, null), $composer, 0);
            ButtonColors m1809buttonColorsro_MJ88 = ButtonDefaults.INSTANCE.m1809buttonColorsro_MJ88(ColorKt.getCinnabar(), ColorKt.getPaper(), 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 54, 12);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            Modifier testTag = TestTagKt.testTag(Modifier.Companion, "create_category_btn");
            ComposerKt.sourceInformationMarkerStart($composer, -1779717239, "CC(remember):CategoryAndLogScreens.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.CategoryAndLogScreensKt$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return CategoryAndLogScreensKt.CategoryManagementScreen$lambda$52$lambda$26$lambda$25$lambda$24(MutableState.this, $catNameInput$delegate, $catIconInput$delegate, $catDescInput$delegate, $catSubcatsText$delegate, $dialogOpen$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button((Function0) obj, testTag, false, m953RoundedCornerShape0680j_4, m1809buttonColorsro_MJ88, null, null, null, null, ComposableSingletons$CategoryAndLogScreensKt.INSTANCE.m6996getLambda$983481192$app(), $composer, 805306422, 484);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$52$lambda$26$lambda$25$lambda$24(MutableState $editingCat$delegate, MutableState $catNameInput$delegate, MutableState $catIconInput$delegate, MutableState $catDescInput$delegate, MutableState $catSubcatsText$delegate, MutableState $dialogOpen$delegate) {
        $editingCat$delegate.setValue(null);
        $catNameInput$delegate.setValue("");
        $catIconInput$delegate.setValue("folder");
        $catDescInput$delegate.setValue("");
        $catSubcatsText$delegate.setValue("all:全部");
        CategoryManagementScreen$lambda$2($dialogOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01cb  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01d7  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01dd  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x02fa  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0306  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x030c  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x033f  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0355 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x04ca  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x04ed  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0644  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0749  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit CategoryManagementScreen$lambda$52$lambda$51(final java.util.List r107, kotlin.jvm.functions.Function0 r108, final java.text.NumberFormat r109, final kotlin.jvm.functions.Function2 r110, final androidx.compose.runtime.MutableState r111, final androidx.compose.runtime.MutableState r112, final androidx.compose.runtime.MutableState r113, final androidx.compose.runtime.MutableState r114, final androidx.compose.runtime.MutableState r115, final androidx.compose.runtime.MutableState r116, final androidx.compose.runtime.MutableState r117, androidx.compose.runtime.Composer r118, int r119) {
        /*
            Method dump skipped, instructions count: 1871
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.CategoryAndLogScreensKt.CategoryManagementScreen$lambda$52$lambda$51(java.util.List, kotlin.jvm.functions.Function0, java.text.NumberFormat, kotlin.jvm.functions.Function2, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x07a3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:105:0x07f7  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0871  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x08ee  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01fa  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0200  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0322  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x032e  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0334  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0367  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x037d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x048f  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x04f1  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0533  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0540  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x064f  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x065b  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x065f  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x068d  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x06a3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x06eb  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x06ee  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0719  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0726  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0769  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x076c  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0796  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit CategoryManagementScreen$lambda$52$lambda$51$lambda$50$lambda$49$lambda$48$lambda$47(final com.example.model.CategoryItem r115, java.text.NumberFormat r116, final int r117, final kotlin.jvm.functions.Function2 r118, final java.util.List r119, final androidx.compose.runtime.MutableState r120, final androidx.compose.runtime.MutableState r121, final androidx.compose.runtime.MutableState r122, final androidx.compose.runtime.MutableState r123, final androidx.compose.runtime.MutableState r124, final androidx.compose.runtime.MutableState r125, final androidx.compose.runtime.MutableState r126, androidx.compose.runtime.Composer r127, int r128) {
        /*
            Method dump skipped, instructions count: 2292
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.CategoryAndLogScreensKt.CategoryManagementScreen$lambda$52$lambda$51$lambda$50$lambda$49$lambda$48$lambda$47(com.example.model.CategoryItem, java.text.NumberFormat, int, kotlin.jvm.functions.Function2, java.util.List, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$52$lambda$51$lambda$50$lambda$49$lambda$48$lambda$47$lambda$46$lambda$33$lambda$30$lambda$29(CategoryItem $cat, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C173@7596L10,171@7440L491:CategoryAndLogScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-630063689, $changed, -1, "com.example.ui.screens.CategoryManagementScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (CategoryAndLogScreens.kt:171)");
            }
            TextKt.m2693Text4IGK_g("#" + $cat.getOrder(), PaddingKt.m671paddingVpY3zN4(Modifier.Companion, Dp.m6622constructorimpl(6), Dp.m6622constructorimpl(2)), ColorKt.getInk(), 0L, (FontStyle) null, FontWeight.Companion.getBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall(), $composer, 197040, 0, 65496);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final CharSequence CategoryManagementScreen$lambda$52$lambda$51$lambda$50$lambda$49$lambda$48$lambda$47$lambda$46$lambda$33$lambda$32$lambda$31(SubCategoryItem it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$52$lambda$51$lambda$50$lambda$49$lambda$48$lambda$47$lambda$46$lambda$45$lambda$35$lambda$34(Function2 $onMoveCategory, CategoryItem $cat) {
        $onMoveCategory.invoke($cat.getId(), -1);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$52$lambda$51$lambda$50$lambda$49$lambda$48$lambda$47$lambda$46$lambda$45$lambda$36(int $idx, Composer $composer, int $changed) {
        long m4157copywmQWz5c;
        ComposerKt.sourceInformation($composer, "C216@10463L407:CategoryAndLogScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1150807171, $changed, -1, "com.example.ui.screens.CategoryManagementScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (CategoryAndLogScreens.kt:216)");
            }
            ImageVector arrowUpward = ArrowUpwardKt.getArrowUpward(Icons.INSTANCE.getDefault());
            if ($idx > 0) {
                m4157copywmQWz5c = ColorKt.getInkBlack();
            } else {
                m4157copywmQWz5c = Color.m4157copywmQWz5c(r4, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r4) : 0.25f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r4) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r4) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
            }
            IconKt.m2150Iconww6aTOc(arrowUpward, "上移", SizeKt.m715size3ABfNKs(Modifier.Companion, Dp.m6622constructorimpl(18)), m4157copywmQWz5c, $composer, 432, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$52$lambda$51$lambda$50$lambda$49$lambda$48$lambda$47$lambda$46$lambda$45$lambda$38$lambda$37(Function2 $onMoveCategory, CategoryItem $cat) {
        $onMoveCategory.invoke($cat.getId(), 1);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$52$lambda$51$lambda$50$lambda$49$lambda$48$lambda$47$lambda$46$lambda$45$lambda$39(int $idx, List $sortedCats, Composer $composer, int $changed) {
        long m4157copywmQWz5c;
        ComposerKt.sourceInformation($composer, "C228@11296L428:CategoryAndLogScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1798225350, $changed, -1, "com.example.ui.screens.CategoryManagementScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (CategoryAndLogScreens.kt:228)");
            }
            ImageVector arrowDownward = ArrowDownwardKt.getArrowDownward(Icons.INSTANCE.getDefault());
            if ($idx < CollectionsKt.getLastIndex($sortedCats)) {
                m4157copywmQWz5c = ColorKt.getInkBlack();
            } else {
                m4157copywmQWz5c = Color.m4157copywmQWz5c(r4, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r4) : 0.25f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r4) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r4) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
            }
            IconKt.m2150Iconww6aTOc(arrowDownward, "下移", SizeKt.m715size3ABfNKs(Modifier.Companion, Dp.m6622constructorimpl(18)), m4157copywmQWz5c, $composer, 432, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$52$lambda$51$lambda$50$lambda$49$lambda$48$lambda$47$lambda$46$lambda$45$lambda$42$lambda$41(CategoryItem $cat, MutableState $editingCat$delegate, MutableState $catNameInput$delegate, MutableState $catIconInput$delegate, MutableState $catDescInput$delegate, MutableState $catSubcatsText$delegate, MutableState $dialogOpen$delegate) {
        $editingCat$delegate.setValue($cat);
        $catNameInput$delegate.setValue($cat.getName());
        $catIconInput$delegate.setValue($cat.getIconKey());
        $catDescInput$delegate.setValue($cat.getDesc());
        $catSubcatsText$delegate.setValue(CollectionsKt.joinToString$default($cat.getSubcategories(), "\n", null, null, 0, null, new Function1() { // from class: com.example.ui.screens.CategoryAndLogScreensKt$$ExternalSyntheticLambda15
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CategoryAndLogScreensKt.CategoryManagementScreen$lambda$52$lambda$51$lambda$50$lambda$49$lambda$48$lambda$47$lambda$46$lambda$45$lambda$42$lambda$41$lambda$40((SubCategoryItem) obj);
            }
        }, 30, null));
        CategoryManagementScreen$lambda$2($dialogOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final CharSequence CategoryManagementScreen$lambda$52$lambda$51$lambda$50$lambda$49$lambda$48$lambda$47$lambda$46$lambda$45$lambda$42$lambda$41$lambda$40(SubCategoryItem it) {
        Intrinsics.checkNotNullParameter(it, "it");
        String id = it.getId();
        return id + ":" + it.getName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$52$lambda$51$lambda$50$lambda$49$lambda$48$lambda$47$lambda$46$lambda$45$lambda$44$lambda$43(CategoryItem $cat, MutableState $deletingCat$delegate) {
        $deletingCat$delegate.setValue($cat);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$54$lambda$53(MutableState $dialogOpen$delegate) {
        CategoryManagementScreen$lambda$2($dialogOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0139  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01de  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit CategoryManagementScreen$lambda$64(androidx.compose.runtime.MutableState r51, androidx.compose.runtime.Composer r52, int r53) {
        /*
            Method dump skipped, instructions count: 484
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.CategoryAndLogScreensKt.CategoryManagementScreen$lambda$64(androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0195  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x022d  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x023f  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x02c4  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x02d6  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x035b  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x036d  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x03d9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit CategoryManagementScreen$lambda$74(final androidx.compose.runtime.MutableState r58, final androidx.compose.runtime.MutableState r59, final androidx.compose.runtime.MutableState r60, final androidx.compose.runtime.MutableState r61, androidx.compose.runtime.Composer r62, int r63) {
        /*
            Method dump skipped, instructions count: 991
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.CategoryAndLogScreensKt.CategoryManagementScreen$lambda$74(androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$74$lambda$73$lambda$66$lambda$65(MutableState $catNameInput$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $catNameInput$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$74$lambda$73$lambda$68$lambda$67(MutableState $catIconInput$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $catIconInput$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$74$lambda$73$lambda$70$lambda$69(MutableState $catDescInput$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $catDescInput$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$74$lambda$73$lambda$72$lambda$71(MutableState $catSubcatsText$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $catSubcatsText$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$59(final Function1 $onShowToast, final Function5 $onSaveCategory, final MutableState $catNameInput$delegate, final MutableState $catSubcatsText$delegate, final MutableState $editingCat$delegate, final MutableState $catIconInput$delegate, final MutableState $catDescInput$delegate, final MutableState $dialogOpen$delegate, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C368@18161L61,344@16984L1131,343@16946L1400:CategoryAndLogScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1951185265, $changed, -1, "com.example.ui.screens.CategoryManagementScreen.<anonymous> (CategoryAndLogScreens.kt:343)");
            }
            ButtonColors m1809buttonColorsro_MJ88 = ButtonDefaults.INSTANCE.m1809buttonColorsro_MJ88(ColorKt.getCinnabar(), ColorKt.getPaper(), 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 54, 12);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            ComposerKt.sourceInformationMarkerStart($composer, -1811665764, "CC(remember):CategoryAndLogScreens.kt#9igjgp");
            boolean changed = $composer.changed($onShowToast) | $composer.changed($onSaveCategory);
            Object rememberedValue = $composer.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.CategoryAndLogScreensKt$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return CategoryAndLogScreensKt.CategoryManagementScreen$lambda$59$lambda$58$lambda$57(Function1.this, $onSaveCategory, $catNameInput$delegate, $catSubcatsText$delegate, $editingCat$delegate, $catIconInput$delegate, $catDescInput$delegate, $dialogOpen$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button((Function0) obj, null, false, m953RoundedCornerShape0680j_4, m1809buttonColorsro_MJ88, null, null, null, null, ComposableSingletons$CategoryAndLogScreensKt.INSTANCE.m6989getLambda$1295416447$app(), $composer, 805306368, 486);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v7, types: [java.util.List] */
    public static final Unit CategoryManagementScreen$lambda$59$lambda$58$lambda$57(Function1 $onShowToast, Function5 $onSaveCategory, MutableState $catNameInput$delegate, MutableState $catSubcatsText$delegate, MutableState $editingCat$delegate, MutableState $catIconInput$delegate, MutableState $catDescInput$delegate, MutableState $dialogOpen$delegate) {
        Iterable iterable;
        boolean z;
        SubCategoryItem subCategoryItem;
        int i = 1;
        if (StringsKt.trim((CharSequence) CategoryManagementScreen$lambda$7($catNameInput$delegate)).toString().length() == 0) {
            $onShowToast.invoke("请填写分类名称");
            return Unit.INSTANCE;
        }
        Iterable<String> lines = StringsKt.lines(CategoryManagementScreen$lambda$16($catSubcatsText$delegate));
        boolean z2 = false;
        Collection arrayList = new ArrayList();
        for (String str : lines) {
            String obj = StringsKt.trim((CharSequence) str).toString();
            if ((obj.length() == 0 ? i : 0) != 0) {
                z = z2;
                subCategoryItem = null;
                iterable = lines;
            } else {
                iterable = lines;
                String[] strArr = new String[i];
                strArr[0] = ":";
                List split$default = StringsKt.split$default((CharSequence) obj, strArr, false, 2, 2, (Object) null);
                if (split$default.size() == 2) {
                    z = z2;
                    subCategoryItem = new SubCategoryItem(StringsKt.trim((CharSequence) ((String) split$default.get(0))).toString(), StringsKt.trim((CharSequence) ((String) split$default.get(1))).toString());
                } else {
                    z = z2;
                    subCategoryItem = new SubCategoryItem(obj, obj);
                }
            }
            if (subCategoryItem != null) {
                arrayList.add(subCategoryItem);
            }
            lines = iterable;
            z2 = z;
            i = 1;
        }
        List parsedSubs = (List) arrayList;
        List list = parsedSubs;
        $onSaveCategory.invoke(CategoryManagementScreen$lambda$4($editingCat$delegate), CategoryManagementScreen$lambda$7($catNameInput$delegate), CategoryManagementScreen$lambda$10($catIconInput$delegate), CategoryManagementScreen$lambda$13($catDescInput$delegate), list.isEmpty() ? CollectionsKt.listOf(new SubCategoryItem("all", "全部")) : list);
        CategoryManagementScreen$lambda$2($dialogOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$62(final MutableState $dialogOpen$delegate, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C376@18454L22,375@18408L265:CategoryAndLogScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(603051695, $changed, -1, "com.example.ui.screens.CategoryManagementScreen.<anonymous> (CategoryAndLogScreens.kt:375)");
            }
            BorderStroke m252BorderStrokecXLIe8U = BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist());
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            ComposerKt.sourceInformationMarkerStart($composer, -287903995, "CC(remember):CategoryAndLogScreens.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.CategoryAndLogScreensKt$$ExternalSyntheticLambda14
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return CategoryAndLogScreensKt.CategoryManagementScreen$lambda$62$lambda$61$lambda$60(MutableState.this);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.OutlinedButton((Function0) obj, null, false, m953RoundedCornerShape0680j_4, null, null, m252BorderStrokecXLIe8U, null, null, ComposableSingletons$CategoryAndLogScreensKt.INSTANCE.getLambda$2018760445$app(), $composer, 806879238, 438);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$62$lambda$61$lambda$60(MutableState $dialogOpen$delegate) {
        CategoryManagementScreen$lambda$2($dialogOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$76$lambda$75(MutableState $deletingCat$delegate) {
        $deletingCat$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CategoryManagementScreen$lambda$79$lambda$78(MutableState $deletingCat$delegate, Function1 $onDeleteCategory) {
        CategoryItem CategoryManagementScreen$lambda$19 = CategoryManagementScreen$lambda$19($deletingCat$delegate);
        if (CategoryManagementScreen$lambda$19 != null) {
            $onDeleteCategory.invoke(CategoryManagementScreen$lambda$19);
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:108:0x04e6  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0226 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0223  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void OperationLogsScreen(final com.example.viewmodel.AdminUiState r51, final kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> r52, androidx.compose.runtime.Composer r53, final int r54) {
        /*
            Method dump skipped, instructions count: 1277
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.CategoryAndLogScreensKt.OperationLogsScreen(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function1, androidx.compose.runtime.Composer, int):void");
    }

    private static final String OperationLogsScreen$lambda$82(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String OperationLogsScreen$lambda$85(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final int OperationLogsScreen$lambda$88(MutableIntState $page$delegate) {
        return $page$delegate.getIntValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit OperationLogsScreen$lambda$117$lambda$96(final Function1 $onShowToast, RowScope PageHeader, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter(PageHeader, "$this$PageHeader");
        ComposerKt.sourceInformation($composer, "C439@20524L38,441@20625L31,440@20579L662:CategoryAndLogScreens.kt#2thlc2");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer.changed(PageHeader) ? 4 : 2;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1761744163, $dirty2, -1, "com.example.ui.screens.OperationLogsScreen.<anonymous>.<anonymous> (CategoryAndLogScreens.kt:439)");
            }
            SpacerKt.Spacer(RowScope.weight$default(PageHeader, Modifier.Companion, 1.0f, false, 2, null), $composer, 0);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            BorderStroke m252BorderStrokecXLIe8U = BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist());
            Modifier testTag = TestTagKt.testTag(Modifier.Companion, "export_logs_btn");
            ComposerKt.sourceInformationMarkerStart($composer, 819029858, "CC(remember):CategoryAndLogScreens.kt#9igjgp");
            boolean changed = $composer.changed($onShowToast);
            Object rememberedValue = $composer.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.CategoryAndLogScreensKt$$ExternalSyntheticLambda11
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return CategoryAndLogScreensKt.OperationLogsScreen$lambda$117$lambda$96$lambda$95$lambda$94(Function1.this);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.OutlinedButton((Function0) obj, testTag, false, m953RoundedCornerShape0680j_4, null, null, m252BorderStrokecXLIe8U, null, null, ComposableSingletons$CategoryAndLogScreensKt.INSTANCE.getLambda$117052821$app(), $composer, 806879280, 436);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit OperationLogsScreen$lambda$117$lambda$96$lambda$95$lambda$94(Function1 $onShowToast) {
        $onShowToast.invoke("已导出操作日志（CSV）");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0901  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01d6  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01e2  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01e8  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0327  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0339  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x03f4  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0400  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0406  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0439  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x044f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x050e  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0671  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0696  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x07ef A[LOOP:1: B:91:0x07e9->B:93:0x07ef, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x08b8  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x08c6  */
    /* JADX WARN: Type inference failed for: r7v37 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit OperationLogsScreen$lambda$117$lambda$116(java.util.List r119, int r120, java.util.List r121, final androidx.compose.runtime.MutableState r122, final androidx.compose.runtime.MutableState r123, final androidx.compose.runtime.MutableIntState r124, androidx.compose.runtime.Composer r125, int r126) {
        /*
            Method dump skipped, instructions count: 2311
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.CategoryAndLogScreensKt.OperationLogsScreen$lambda$117$lambda$116(java.util.List, int, java.util.List, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableIntState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit OperationLogsScreen$lambda$117$lambda$116$lambda$115$lambda$104$lambda$98$lambda$97(MutableState $keyword$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $keyword$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit OperationLogsScreen$lambda$117$lambda$116$lambda$115$lambda$104$lambda$103$lambda$102$lambda$100$lambda$99(String $key, MutableState $selectedAction$delegate) {
        $selectedAction$delegate.setValue($key);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit OperationLogsScreen$lambda$117$lambda$116$lambda$115$lambda$104$lambda$103$lambda$102$lambda$101(String $label, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C507@23443L10,507@23409L57:CategoryAndLogScreens.kt#2thlc2");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1879773938, $changed, -1, "com.example.ui.screens.OperationLogsScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (CategoryAndLogScreens.kt:507)");
            }
            TextKt.m2693Text4IGK_g($label, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelMedium(), $composer, 0, 0, 65534);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01ec  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0304  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0310  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0316  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0349  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x035f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x043c  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0448  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x044e  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0481  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0497 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0680  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x068c  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0692  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x06c3  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x06d9  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x07e6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit OperationLogsScreen$lambda$117$lambda$116$lambda$115$lambda$112$lambda$111$lambda$110(com.example.model.ActivityLog r133, androidx.compose.runtime.Composer r134, int r135) {
        /*
            Method dump skipped, instructions count: 2028
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.CategoryAndLogScreensKt.OperationLogsScreen$lambda$117$lambda$116$lambda$115$lambda$112$lambda$111$lambda$110(com.example.model.ActivityLog, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit OperationLogsScreen$lambda$117$lambda$116$lambda$115$lambda$114$lambda$113(MutableIntState $page$delegate, int it) {
        $page$delegate.setIntValue(it);
        return Unit.INSTANCE;
    }
}
