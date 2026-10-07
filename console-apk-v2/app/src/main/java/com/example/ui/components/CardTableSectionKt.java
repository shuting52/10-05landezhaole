package com.example.ui.components;

import androidx.autofill.HintConstants;
import androidx.compose.foundation.BorderStrokeKt;
import androidx.compose.foundation.layout.PaddingKt;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.SurfaceKt;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.MutableIntState;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.ScopeUpdateScope;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.platform.TestTagKt;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.unit.Dp;
import androidx.core.app.NotificationCompat;
import com.example.model.ButtonType;
import com.example.model.CardStatus;
import com.example.model.ResourceCard;
import com.example.ui.theme.ColorKt;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
/* compiled from: CardTableSection.kt */
@Metadata(d1 = {"\u0000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\u001a¼\u0003\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\u00062\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00062\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u000e2 \u0002\u0010\u0010\u001a\u009b\u0002\u0012\u0015\u0012\u0013\u0018\u00010\u0007¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0014\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0013\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0016\u0012\u0013\u0012\u00110\u0017¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\u0019¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u001a\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u001b\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u001c\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u001e\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u001f\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b( \u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(!\u0012\u0004\u0012\u00020\u00010\u00112\u0012\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010#2\u0012\u0010$\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010#2\b\b\u0002\u0010%\u001a\u00020&H\u0007¢\u0006\u0002\u0010'\u001a?\u0010(\u001a\u00020\u00012\u0006\u0010)\u001a\u00020\u00072\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00010+2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00010+2\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00010+H\u0003¢\u0006\u0002\u0010.¨\u0006/²\u0006\n\u00100\u001a\u00020\u0003X\u008a\u008e\u0002²\u0006\n\u00101\u001a\u00020\u0003X\u008a\u008e\u0002²\u0006\n\u00102\u001a\u00020\u0003X\u008a\u008e\u0002²\u0006\n\u00103\u001a\u00020\fX\u008a\u008e\u0002²\u0006\n\u00104\u001a\u00020\u000eX\u008a\u008e\u0002²\u0006\f\u00105\u001a\u0004\u0018\u00010\u0007X\u008a\u008e\u0002²\u0006\f\u00106\u001a\u0004\u0018\u00010\u0007X\u008a\u008e\u0002"}, d2 = {"CardTableSection", "", "title", "", "description", "allCards", "", "Lcom/example/model/ResourceCard;", "categories", "categoryObjects", "Lcom/example/model/CategoryItem;", "pageSize", "", "showFilters", "", "showHeaderActions", "onSaveCard", "Lkotlin/Function13;", "Lkotlin/ParameterName;", HintConstants.AUTOFILL_HINT_NAME, "existing", "desc", "url", "Lcom/example/model/ButtonType;", "btnType", "Lcom/example/model/CardStatus;", NotificationCompat.CATEGORY_STATUS, "category", "subcatId", "icon", "fallbackText", "badge", "badgeType", "highlights", "onDeleteCard", "Lkotlin/Function1;", "onShowToast", "modifier", "Landroidx/compose/ui/Modifier;", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/util/List;IZZLkotlin/jvm/functions/Function13;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Landroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;III)V", "CardItemRow", "card", "onView", "Lkotlin/Function0;", "onEdit", "onDelete", "(Lcom/example/model/ResourceCard;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "app", "keyword", "selectedStatus", "selectedCategory", "page", "formOpen", "editingCard", "deletingCard"}, k = 2, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class CardTableSectionKt {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardItemRow$lambda$82(ResourceCard resourceCard, Function0 function0, Function0 function02, Function0 function03, int i, Composer composer, int i2) {
        CardItemRow(resourceCard, function0, function02, function03, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$71(String str, String str2, List list, List list2, List list3, int i, boolean z, boolean z2, Function13 function13, Function1 function1, Function1 function12, Modifier modifier, int i2, int i3, int i4, Composer composer, int i5) {
        CardTableSection(str, str2, list, list2, list3, i, z, z2, function13, function1, function12, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i2 | 1), RecomposeScopeImplKt.updateChangedFlags(i3), i4);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:206:0x04c5  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x04cb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void CardTableSection(final java.lang.String r43, final java.lang.String r44, final java.util.List<com.example.model.ResourceCard> r45, final java.util.List<java.lang.String> r46, java.util.List<com.example.model.CategoryItem> r47, int r48, boolean r49, boolean r50, kotlin.jvm.functions.Function13<? super com.example.model.ResourceCard, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super com.example.model.ButtonType, ? super com.example.model.CardStatus, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> r51, kotlin.jvm.functions.Function1<? super com.example.model.ResourceCard, kotlin.Unit> r52, final kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> r53, androidx.compose.ui.Modifier r54, androidx.compose.runtime.Composer r55, final int r56, final int r57, final int r58) {
        /*
            Method dump skipped, instructions count: 1957
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.CardTableSectionKt.CardTableSection(java.lang.String, java.lang.String, java.util.List, java.util.List, java.util.List, int, boolean, boolean, kotlin.jvm.functions.Function13, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function1, androidx.compose.ui.Modifier, androidx.compose.runtime.Composer, int, int, int):void");
    }

    private static final String CardTableSection$lambda$1(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CardTableSection$lambda$4(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CardTableSection$lambda$7(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final int CardTableSection$lambda$10(MutableIntState $page$delegate) {
        return $page$delegate.getIntValue();
    }

    private static final boolean CardTableSection$lambda$13(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void CardTableSection$lambda$14(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final ResourceCard CardTableSection$lambda$16(MutableState<ResourceCard> mutableState) {
        return mutableState.getValue();
    }

    private static final ResourceCard CardTableSection$lambda$19(MutableState<ResourceCard> mutableState) {
        return mutableState.getValue();
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: SSATransform
        java.lang.IndexOutOfBoundsException: bitIndex < 0: -114
        	at java.base/java.util.BitSet.get(BitSet.java:626)
        	at jadx.core.dex.visitors.ssa.LiveVarAnalysis.fillBasicBlockInfo(LiveVarAnalysis.java:65)
        	at jadx.core.dex.visitors.ssa.LiveVarAnalysis.runAnalysis(LiveVarAnalysis.java:36)
        	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:55)
        	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:41)
        */
    static final kotlin.Unit CardTableSection$lambda$61(java.util.List r138, int r139, int r140, boolean r141, boolean r142, java.lang.String r143, java.lang.String r144, kotlin.jvm.functions.Function1 r145, androidx.compose.runtime.MutableState r146, androidx.compose.runtime.MutableState r147, androidx.compose.runtime.MutableState r148, java.util.List r149, androidx.compose.runtime.MutableState r150, androidx.compose.runtime.MutableState r151, androidx.compose.runtime.MutableState r152, androidx.compose.runtime.MutableIntState r153, androidx.compose.runtime.Composer r154, int r155) {
        /*
            Method dump skipped, instructions count: 4251
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.CardTableSectionKt.CardTableSection$lambda$61(java.util.List, int, int, boolean, boolean, java.lang.String, java.lang.String, kotlin.jvm.functions.Function1, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, java.util.List, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableIntState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$45$lambda$31$lambda$30$lambda$27$lambda$26(Function1 $onShowToast, int $total) {
        $onShowToast.invoke("已导出卡片列表（CSV，共 " + $total + " 条）");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$45$lambda$31$lambda$30$lambda$29$lambda$28(MutableState $editingCard$delegate, MutableState $formOpen$delegate) {
        $editingCard$delegate.setValue(null);
        CardTableSection$lambda$14($formOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$45$lambda$33$lambda$32(MutableState $keyword$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $keyword$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$45$lambda$44$lambda$37$lambda$35$lambda$34(String $valKey, MutableState $selectedStatus$delegate) {
        $selectedStatus$delegate.setValue($valKey);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$45$lambda$44$lambda$37$lambda$36(String $label, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C192@8333L10,192@8299L57:CardTableSection.kt#qonjpd");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(2043427851, $changed, -1, "com.example.ui.components.CardTableSection.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (CardTableSection.kt:192)");
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
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$45$lambda$44$lambda$39$lambda$38(MutableState $selectedCategory$delegate) {
        $selectedCategory$delegate.setValue("all");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$45$lambda$44$lambda$43$lambda$41$lambda$40(String $catName, MutableState $selectedCategory$delegate) {
        $selectedCategory$delegate.setValue($catName);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$45$lambda$44$lambda$43$lambda$42(String $catName, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C230@10297L10,230@10261L59:CardTableSection.kt#qonjpd");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(231386845, $changed, -1, "com.example.ui.components.CardTableSection.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (CardTableSection.kt:230)");
            }
            TextKt.m2693Text4IGK_g($catName, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelMedium(), $composer, 0, 0, 65534);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$48(final MutableState $editingCard$delegate, final MutableState $formOpen$delegate, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C253@11258L130,252@11204L796:CardTableSection.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-367575502, $changed, -1, "com.example.ui.components.CardTableSection.<anonymous>.<anonymous>.<anonymous> (CardTableSection.kt:252)");
            }
            ComposerKt.sourceInformationMarkerStart($composer, -1052736460, "CC(remember):CardTableSection.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.components.CardTableSectionKt$$ExternalSyntheticLambda8
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return CardTableSectionKt.CardTableSection$lambda$61$lambda$60$lambda$48$lambda$47$lambda$46(MutableState.this, $formOpen$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.OutlinedButton((Function0) obj, null, false, RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(10)), null, null, BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getCinnabar()), null, null, ComposableSingletons$CardTableSectionKt.INSTANCE.m6932getLambda$782452096$app(), $composer, 806879238, 438);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$48$lambda$47$lambda$46(MutableState $editingCard$delegate, MutableState $formOpen$delegate) {
        $editingCard$delegate.setValue(null);
        CardTableSection$lambda$14($formOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$57$lambda$56$lambda$51$lambda$50(Function1 $onShowToast, ResourceCard $card) {
        String name = $card.getName();
        String url = $card.getUrl();
        if (StringsKt.isBlank(url)) {
            url = $card.getCategory();
        }
        $onShowToast.invoke("查看《" + name + "》· " + ((Object) url));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$57$lambda$56$lambda$53$lambda$52(ResourceCard $card, MutableState $editingCard$delegate, MutableState $formOpen$delegate) {
        $editingCard$delegate.setValue($card);
        CardTableSection$lambda$14($formOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$57$lambda$56$lambda$55$lambda$54(ResourceCard $card, MutableState $deletingCard$delegate) {
        $deletingCard$delegate.setValue($card);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$61$lambda$60$lambda$59$lambda$58(MutableIntState $page$delegate, int it) {
        $page$delegate.setIntValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$63$lambda$62(MutableState $formOpen$delegate) {
        CardTableSection$lambda$14($formOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$65$lambda$64(Function13 $onSaveCard, MutableState $editingCard$delegate, String name, String desc, String url, ButtonType btnType, CardStatus status, String cat, String subcatId, String icon, String fallbackText, String badge, String badgeType, String highlights) {
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
        $onSaveCard.invoke(CardTableSection$lambda$16($editingCard$delegate), name, desc, url, btnType, status, cat, subcatId, icon, fallbackText, badge, badgeType, highlights);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$67$lambda$66(MutableState $deletingCard$delegate) {
        $deletingCard$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardTableSection$lambda$70$lambda$69(MutableState $deletingCard$delegate, Function1 $onDeleteCard) {
        ResourceCard CardTableSection$lambda$19 = CardTableSection$lambda$19($deletingCard$delegate);
        if (CardTableSection$lambda$19 != null) {
            $onDeleteCard.invoke(CardTableSection$lambda$19);
        }
        return Unit.INSTANCE;
    }

    private static final void CardItemRow(final ResourceCard card, final Function0<Unit> function0, final Function0<Unit> function02, final Function0<Unit> function03, Composer $composer, final int $changed) {
        Composer $composer2;
        Composer $composer3 = $composer.startRestartGroup(-1446627597);
        ComposerKt.sourceInformation($composer3, "C(CardItemRow)P(!1,3,2)339@14223L5986,332@13998L6211:CardTableSection.kt#qonjpd");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changedInstance(card) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changedInstance(function0) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            $dirty |= $composer3.changedInstance(function02) ? 256 : 128;
        }
        if (($changed & 3072) == 0) {
            $dirty |= $composer3.changedInstance(function03) ? 2048 : 1024;
        }
        if (($dirty & 1171) == 1170 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1446627597, $dirty, -1, "com.example.ui.components.CardItemRow (CardTableSection.kt:331)");
            }
            $composer2 = $composer3;
            SurfaceKt.m2543SurfaceT9BRK9s(TestTagKt.testTag(SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, null), "card_item_" + card.getId()), RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(14)), ColorKt.getPaper(), 0L, 0.0f, 0.0f, BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist()), ComposableLambdaKt.rememberComposableLambda(-553310280, true, new Function2() { // from class: com.example.ui.components.CardTableSectionKt$$ExternalSyntheticLambda6
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return CardTableSectionKt.CardItemRow$lambda$81(ResourceCard.this, function0, function02, function03, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), $composer2, 14156160, 56);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.components.CardTableSectionKt$$ExternalSyntheticLambda7
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return CardTableSectionKt.CardItemRow$lambda$82(ResourceCard.this, function0, function02, function03, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0883  */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0889  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x08bc  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x08d2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0939  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x098a  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x099f  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0a13  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0aa7  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0ab3  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0ab9  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0aea  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0b00  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0c37  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01d9  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01e5  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0303  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x030f  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0315  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0348  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x035e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0438  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0444  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x044a  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x047d  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0493 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x057d  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x060d  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0683  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x074b  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0757  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x075d  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0790  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x07a6  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0877  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit CardItemRow$lambda$81(final com.example.model.ResourceCard r137, kotlin.jvm.functions.Function0 r138, kotlin.jvm.functions.Function0 r139, kotlin.jvm.functions.Function0 r140, androidx.compose.runtime.Composer r141, int r142) {
        /*
            Method dump skipped, instructions count: 3133
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.CardTableSectionKt.CardItemRow$lambda$81(com.example.model.ResourceCard, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardItemRow$lambda$81$lambda$80$lambda$76$lambda$75$lambda$74$lambda$72(ResourceCard $card, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C369@15515L10,367@15401L307:CardTableSection.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(405036359, $changed, -1, "com.example.ui.components.CardItemRow.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (CardTableSection.kt:367)");
            }
            String category = $card.getCategory();
            TextStyle labelSmall = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall();
            TextKt.m2693Text4IGK_g(category, PaddingKt.m671paddingVpY3zN4(Modifier.Companion, Dp.m6622constructorimpl(6), Dp.m6622constructorimpl(2)), ColorKt.getInk(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, labelSmall, $composer, 432, 0, 65528);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardItemRow$lambda$81$lambda$80$lambda$76$lambda$75$lambda$74$lambda$73(ResourceCard $card, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C381@16173L10,379@16051L332:CardTableSection.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(156588194, $changed, -1, "com.example.ui.components.CardItemRow.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (CardTableSection.kt:379)");
            }
            String subcatId = $card.getSubcatId();
            TextStyle labelSmall = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall();
            TextKt.m2693Text4IGK_g(subcatId, PaddingKt.m671paddingVpY3zN4(Modifier.Companion, Dp.m6622constructorimpl(6), Dp.m6622constructorimpl(2)), ColorKt.getGoldDark(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, labelSmall, $composer, 432, 0, 65528);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }
}
