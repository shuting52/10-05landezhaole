package com.example.ui.components;

import androidx.autofill.HintConstants;
import androidx.compose.foundation.BorderStrokeKt;
import androidx.compose.foundation.interaction.MutableInteractionSource;
import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.shape.RoundedCornerShape;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.foundation.text.KeyboardActions;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material3.AndroidAlertDialog_androidKt;
import androidx.compose.material3.AndroidMenu_androidKt;
import androidx.compose.material3.ButtonColors;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.ExposedDropdownMenuBoxScope;
import androidx.compose.material3.ExposedDropdownMenuDefaults;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.MenuAnchorType;
import androidx.compose.material3.OutlinedTextFieldKt;
import androidx.compose.material3.TextFieldColors;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.ScopeUpdateScope;
import androidx.compose.runtime.internal.ComposableLambda;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Shape;
import androidx.compose.ui.platform.TestTagKt;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.VisualTransformation;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.unit.Dp;
import androidx.core.app.NotificationCompat;
import com.example.model.ButtonType;
import com.example.model.CardStatus;
import com.example.model.ResourceCard;
import com.example.model.SubCategoryItem;
import com.example.ui.theme.ColorKt;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
/* compiled from: Dialogs.kt */
@Metadata(d1 = {"\u0000D\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0015\u001a×\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00072\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u0089\u0002\u0010\r\u001a\u0084\u0002\u0012\u0013\u0012\u00110\b¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\b¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0013\u0012\u00110\b¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0012\u0012\u0013\u0012\u00110\u0013¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0014\u0012\u0013\u0012\u00110\u0015¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0016\u0012\u0013\u0012\u00110\b¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0017\u0012\u0013\u0012\u00110\b¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\b¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0019\u0012\u0013\u0012\u00110\b¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u001a\u0012\u0013\u0012\u00110\b¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u001b\u0012\u0013\u0012\u00110\b¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u001c\u0012\u0013\u0012\u00110\b¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u001d\u0012\u0004\u0012\u00020\u00010\u000eH\u0007¢\u0006\u0002\u0010\u001e\u001aM\u0010\u001f\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010 \u001a\u00020\b2\u0006\u0010!\u001a\u00020\b2\n\b\u0002\u0010\"\u001a\u0004\u0018\u00010\b2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00010\fH\u0007¢\u0006\u0002\u0010$¨\u0006%²\u0006\n\u0010\u0010\u001a\u00020\bX\u008a\u008e\u0002²\u0006\n\u0010\u0011\u001a\u00020\bX\u008a\u008e\u0002²\u0006\n\u0010\u0012\u001a\u00020\bX\u008a\u008e\u0002²\u0006\n\u0010\u0014\u001a\u00020\u0013X\u008a\u008e\u0002²\u0006\n\u0010\u0016\u001a\u00020\u0015X\u008a\u008e\u0002²\u0006\n\u0010\u0017\u001a\u00020\bX\u008a\u008e\u0002²\u0006\n\u0010\u0018\u001a\u00020\bX\u008a\u008e\u0002²\u0006\n\u0010\u0019\u001a\u00020\bX\u008a\u008e\u0002²\u0006\n\u0010\u001a\u001a\u00020\bX\u008a\u008e\u0002²\u0006\n\u0010\u001b\u001a\u00020\bX\u008a\u008e\u0002²\u0006\n\u0010\u001c\u001a\u00020\bX\u008a\u008e\u0002²\u0006\n\u0010\u001d\u001a\u00020\bX\u008a\u008e\u0002²\u0006\n\u0010&\u001a\u00020\bX\u008a\u008e\u0002²\u0006\n\u0010'\u001a\u00020\u0003X\u008a\u008e\u0002²\u0006\n\u0010(\u001a\u00020\u0003X\u008a\u008e\u0002²\u0006\n\u0010)\u001a\u00020\u0003X\u008a\u008e\u0002²\u0006\n\u0010*\u001a\u00020\u0003X\u008a\u008e\u0002"}, d2 = {"CardFormDialog", "", "open", "", "card", "Lcom/example/model/ResourceCard;", "categories", "", "", "categoryObjects", "Lcom/example/model/CategoryItem;", "onDismiss", "Lkotlin/Function0;", "onSubmit", "Lkotlin/Function12;", "Lkotlin/ParameterName;", HintConstants.AUTOFILL_HINT_NAME, "description", "url", "Lcom/example/model/ButtonType;", "buttonType", "Lcom/example/model/CardStatus;", NotificationCompat.CATEGORY_STATUS, "category", "subcatId", "icon", "fallbackText", "badge", "badgeType", "highlights", "(ZLcom/example/model/ResourceCard;Ljava/util/List;Ljava/util/List;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function12;Landroidx/compose/runtime/Composer;II)V", "ConfirmDeleteDialog", "title", "itemName", "extraWarning", "onConfirm", "(ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;II)V", "app", "error", "catExpanded", "subcatExpanded", "btnExpanded", "statusExpanded"}, k = 2, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class DialogsKt {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$0(boolean z, ResourceCard resourceCard, List list, List list2, Function0 function0, Function12 function12, int i, int i2, Composer composer, int i3) {
        CardFormDialog(z, resourceCard, list, list2, function0, function12, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$138(boolean z, ResourceCard resourceCard, List list, List list2, Function0 function0, Function12 function12, int i, int i2, Composer composer, int i3) {
        CardFormDialog(z, resourceCard, list, list2, function0, function12, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ConfirmDeleteDialog$lambda$139(boolean z, String str, String str2, String str3, Function0 function0, Function0 function02, int i, int i2, Composer composer, int i3) {
        ConfirmDeleteDialog(z, str, str2, str3, function0, function02, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ConfirmDeleteDialog$lambda$147(boolean z, String str, String str2, String str3, Function0 function0, Function0 function02, int i, int i2, Composer composer, int i3) {
        ConfirmDeleteDialog(z, str, str2, str3, function0, function02, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:112:0x01a1  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x01a3  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x01ef  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0238  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x023a  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0285  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x0296  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x02a1  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x02b2  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x02b4  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x02c5  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x02f2  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x02f4  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x0302  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x0316  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x0359  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x035b  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x0367  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x037b  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x03b2  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x03b4  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x03c0  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x03d4  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x040b  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x040d  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x0419  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x042a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:258:0x042f  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x0466  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x0468  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x0475  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x0486 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:277:0x048b  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x04c1  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x04c3  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x04d0  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x04e0  */
    /* JADX WARN: Removed duplicated region for block: B:296:0x04e5  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x0518  */
    /* JADX WARN: Removed duplicated region for block: B:305:0x051a  */
    /* JADX WARN: Removed duplicated region for block: B:308:0x0526  */
    /* JADX WARN: Removed duplicated region for block: B:312:0x0535 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:316:0x0568  */
    /* JADX WARN: Removed duplicated region for block: B:317:0x057f  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x05a5  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x05bc  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x05e3  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x05fa  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x0621  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x0638  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x0664  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x0682  */
    /* JADX WARN: Removed duplicated region for block: B:346:0x06b0  */
    /* JADX WARN: Removed duplicated region for block: B:352:0x07c0  */
    /* JADX WARN: Removed duplicated region for block: B:358:0x06a6 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0153  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0155  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void CardFormDialog(final boolean r51, final com.example.model.ResourceCard r52, final java.util.List<java.lang.String> r53, java.util.List<com.example.model.CategoryItem> r54, final kotlin.jvm.functions.Function0<kotlin.Unit> r55, final kotlin.jvm.functions.Function12<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super com.example.model.ButtonType, ? super com.example.model.CardStatus, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> r56, androidx.compose.runtime.Composer r57, final int r58, final int r59) {
        /*
            Method dump skipped, instructions count: 2018
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.DialogsKt.CardFormDialog(boolean, com.example.model.ResourceCard, java.util.List, java.util.List, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function12, androidx.compose.runtime.Composer, int, int):void");
    }

    private static final String CardFormDialog$lambda$2(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CardFormDialog$lambda$5(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CardFormDialog$lambda$8(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final ButtonType CardFormDialog$lambda$11(MutableState<ButtonType> mutableState) {
        return mutableState.getValue();
    }

    private static final CardStatus CardFormDialog$lambda$14(MutableState<CardStatus> mutableState) {
        return mutableState.getValue();
    }

    private static final String CardFormDialog$lambda$18(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CardFormDialog$lambda$22(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CardFormDialog$lambda$25(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CardFormDialog$lambda$28(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CardFormDialog$lambda$31(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CardFormDialog$lambda$34(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CardFormDialog$lambda$37(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String CardFormDialog$lambda$40(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final boolean CardFormDialog$lambda$43(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void CardFormDialog$lambda$44(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean CardFormDialog$lambda$46(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void CardFormDialog$lambda$47(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean CardFormDialog$lambda$49(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void CardFormDialog$lambda$50(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean CardFormDialog$lambda$52(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void CardFormDialog$lambda$53(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0135  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0138  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0190  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0193  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x01fe  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit CardFormDialog$lambda$62(boolean r51, androidx.compose.runtime.Composer r52, int r53) {
        /*
            Method dump skipped, instructions count: 516
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.DialogsKt.CardFormDialog$lambda$62(boolean, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0780 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:106:0x080d  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x081d  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x08b6  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x08c6  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x096a  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0a49  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0a55  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0a5b  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0a8c  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0aa2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0b0f  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0b21  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0b80  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0b92  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0bf5  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0c3d  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0c63  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01cb  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01d9  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0270  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x027d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0310  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x031d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0394  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x03a2  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x03eb  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x03f9  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x04a6  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x04b2  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x04b8  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x04e9  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x04ff A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x058b  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0599  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x062c  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x063a  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0725  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0731  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0737  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x076a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit CardFormDialog$lambda$137(final androidx.compose.runtime.MutableState r100, final androidx.compose.runtime.MutableState r101, final androidx.compose.runtime.MutableState r102, final androidx.compose.runtime.MutableState r103, final androidx.compose.runtime.MutableState r104, final androidx.compose.runtime.MutableState r105, final androidx.compose.runtime.MutableState r106, final java.util.List r107, final androidx.compose.runtime.MutableState r108, final androidx.compose.runtime.MutableState r109, final java.util.List r110, final androidx.compose.runtime.MutableState r111, final androidx.compose.runtime.MutableState r112, final androidx.compose.runtime.MutableState r113, final androidx.compose.runtime.MutableState r114, androidx.compose.runtime.MutableState r115, final androidx.compose.runtime.MutableState r116, androidx.compose.runtime.MutableState r117, final androidx.compose.runtime.MutableState r118, androidx.compose.runtime.Composer r119, int r120) {
        /*
            Method dump skipped, instructions count: 3177
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.DialogsKt.CardFormDialog$lambda$137(androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, java.util.List, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, java.util.List, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$64$lambda$63(MutableState $name$delegate, MutableState $error$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $name$delegate.setValue(it);
        $error$delegate.setValue("");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$66$lambda$65(MutableState $description$delegate, MutableState $error$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $description$delegate.setValue(it);
        $error$delegate.setValue("");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$68$lambda$67(MutableState $url$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $url$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$70$lambda$69(MutableState $catExpanded$delegate, boolean it) {
        CardFormDialog$lambda$44($catExpanded$delegate, !CardFormDialog$lambda$43($catExpanded$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$81(final MutableState $category$delegate, final MutableState $catExpanded$delegate, final List $categories, final MutableState $subcatId$delegate, ExposedDropdownMenuBoxScope ExposedDropdownMenuBox, Composer $composer, int $changed) {
        Object obj;
        final MutableState mutableState;
        Object obj2;
        Intrinsics.checkNotNullParameter(ExposedDropdownMenuBox, "$this$ExposedDropdownMenuBox");
        ComposerKt.sourceInformation($composer, "C144@5797L2,147@5944L68,142@5696L561,155@6390L23,157@6488L587,153@6278L797:Dialogs.kt#qonjpd");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= ($changed & 8) == 0 ? $composer.changed(ExposedDropdownMenuBox) : $composer.changedInstance(ExposedDropdownMenuBox) ? 4 : 2;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(552760536, $dirty2, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:142)");
            }
            String CardFormDialog$lambda$18 = CardFormDialog$lambda$18($category$delegate);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            Modifier fillMaxWidth$default = SizeKt.fillMaxWidth$default(ExposedDropdownMenuBoxScope.m2079menuAnchorfsE2BvY$default(ExposedDropdownMenuBox, Modifier.Companion, MenuAnchorType.Companion.m2225getPrimaryNotEditableMg6Rgbw(), false, 2, null), 0.0f, 1, null);
            ComposerKt.sourceInformationMarkerStart($composer, -1373065670, "CC(remember):Dialogs.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function1() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda41
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj3) {
                        return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$81$lambda$72$lambda$71((String) obj3);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            OutlinedTextFieldKt.OutlinedTextField(CardFormDialog$lambda$18, (Function1<? super String, Unit>) obj, fillMaxWidth$default, false, true, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$DialogsKt.INSTANCE.getLambda$437706494$app(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableLambdaKt.rememberComposableLambda(-967075647, true, new Function2() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda42
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj3, Object obj4) {
                    return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$81$lambda$73(MutableState.this, (Composer) obj3, ((Integer) obj4).intValue());
                }
            }, $composer, 54), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, false, 0, 0, (MutableInteractionSource) null, (Shape) m953RoundedCornerShape0680j_4, (TextFieldColors) null, $composer, 806903856, 0, 0, 6290856);
            boolean CardFormDialog$lambda$43 = CardFormDialog$lambda$43($catExpanded$delegate);
            ComposerKt.sourceInformationMarkerStart($composer, -1373046673, "CC(remember):Dialogs.kt#9igjgp");
            Object rememberedValue2 = $composer.rememberedValue();
            if (rememberedValue2 == Composer.Companion.getEmpty()) {
                mutableState = $catExpanded$delegate;
                obj2 = new Function0() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda43
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$81$lambda$75$lambda$74(MutableState.this);
                    }
                };
                $composer.updateRememberedValue(obj2);
            } else {
                mutableState = $catExpanded$delegate;
                obj2 = rememberedValue2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ExposedDropdownMenuBox.m2081ExposedDropdownMenuvNxi1II(CardFormDialog$lambda$43, (Function0) obj2, null, null, false, null, ColorKt.getPaperSoft(), 0.0f, 0.0f, null, ComposableLambdaKt.rememberComposableLambda(-1850634022, true, new Function3() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda45
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj3, Object obj4, Object obj5) {
                    return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$81$lambda$80($categories, $category$delegate, $subcatId$delegate, mutableState, (ColumnScope) obj3, (Composer) obj4, ((Integer) obj5).intValue());
                }
            }, $composer, 54), $composer, 1572912, (ExposedDropdownMenuBoxScope.$stable << 3) | 6 | (($dirty2 << 3) & 112), 956);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$81$lambda$72$lambda$71(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$81$lambda$73(MutableState $catExpanded$delegate, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C147@5974L36:Dialogs.kt#qonjpd");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-967075647, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:147)");
            }
            ExposedDropdownMenuDefaults.INSTANCE.TrailingIcon(CardFormDialog$lambda$43($catExpanded$delegate), null, $composer, ExposedDropdownMenuDefaults.$stable << 6, 2);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$81$lambda$75$lambda$74(MutableState $catExpanded$delegate) {
        CardFormDialog$lambda$44($catExpanded$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$81$lambda$80(List $categories, final MutableState $category$delegate, final MutableState $subcatId$delegate, final MutableState $catExpanded$delegate, ColumnScope ExposedDropdownMenu, Composer $composer, int $changed) {
        Object obj;
        Composer composer = $composer;
        Intrinsics.checkNotNullParameter(ExposedDropdownMenu, "$this$ExposedDropdownMenu");
        ComposerKt.sourceInformation(composer, "C*161@6739L16,162@6799L198,160@6682L345:Dialogs.kt#qonjpd");
        if (($changed & 17) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1850634022, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:158)");
            }
            boolean z = true;
            Iterable<String> options = !$categories.isEmpty() ? $categories : CollectionsKt.listOf((Object[]) new String[]{"推荐", "AI工具", "实用工具"});
            for (final String str : options) {
                ComposableLambda rememberComposableLambda = ComposableLambdaKt.rememberComposableLambda(763812442, z, new Function2() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda20
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj2, Object obj3) {
                        return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$81$lambda$80$lambda$79$lambda$76(str, (Composer) obj2, ((Integer) obj3).intValue());
                    }
                }, composer, 54);
                ComposerKt.sourceInformationMarkerStart(composer, 364236912, "CC(remember):Dialogs.kt#9igjgp");
                boolean changed = composer.changed($category$delegate) | composer.changed(str) | composer.changed($subcatId$delegate);
                Object rememberedValue = $composer.rememberedValue();
                if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                    obj = new Function0() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda21
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$81$lambda$80$lambda$79$lambda$78$lambda$77(str, $category$delegate, $subcatId$delegate, $catExpanded$delegate);
                        }
                    };
                    $composer.updateRememberedValue(obj);
                } else {
                    obj = rememberedValue;
                }
                ComposerKt.sourceInformationMarkerEnd(composer);
                AndroidMenu_androidKt.DropdownMenuItem(rememberComposableLambda, (Function0) obj, null, null, null, false, null, null, null, composer, 6, 508);
                composer = $composer;
                z = z;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$81$lambda$80$lambda$79$lambda$76(String $option, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C161@6741L12:Dialogs.kt#qonjpd");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(763812442, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:161)");
            }
            TextKt.m2693Text4IGK_g($option, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$81$lambda$80$lambda$79$lambda$78$lambda$77(String $option, MutableState $category$delegate, MutableState $subcatId$delegate, MutableState $catExpanded$delegate) {
        $category$delegate.setValue($option);
        $subcatId$delegate.setValue("all");
        CardFormDialog$lambda$44($catExpanded$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$83$lambda$82(MutableState $subcatExpanded$delegate, boolean it) {
        CardFormDialog$lambda$47($subcatExpanded$delegate, !CardFormDialog$lambda$46($subcatExpanded$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x009e, code lost:
        if (r3 == null) goto L43;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit CardFormDialog$lambda$137$lambda$136$lambda$96(final java.util.List r33, final androidx.compose.runtime.MutableState r34, final androidx.compose.runtime.MutableState r35, androidx.compose.material3.ExposedDropdownMenuBoxScope r36, androidx.compose.runtime.Composer r37, int r38) {
        /*
            Method dump skipped, instructions count: 474
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$96(java.util.List, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.material3.ExposedDropdownMenuBoxScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$96$lambda$87$lambda$86(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$96$lambda$88(MutableState $subcatExpanded$delegate, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C183@7746L39:Dialogs.kt#qonjpd");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(715585066, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:183)");
            }
            ExposedDropdownMenuDefaults.INSTANCE.TrailingIcon(CardFormDialog$lambda$46($subcatExpanded$delegate), null, $composer, ExposedDropdownMenuDefaults.$stable << 6, 2);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$96$lambda$90$lambda$89(MutableState $subcatExpanded$delegate) {
        CardFormDialog$lambda$47($subcatExpanded$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$96$lambda$95(List $activeSubcats, final MutableState $subcatId$delegate, final MutableState $subcatExpanded$delegate, ColumnScope ExposedDropdownMenu, Composer $composer, int $changed) {
        Object obj;
        Composer composer = $composer;
        Intrinsics.checkNotNullParameter(ExposedDropdownMenu, "$this$ExposedDropdownMenu");
        ComposerKt.sourceInformation(composer, "C*196@8410L33,197@8487L147,195@8353L311:Dialogs.kt#qonjpd");
        if (($changed & 17) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(2019511299, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:194)");
            }
            Iterator it = $activeSubcats.iterator();
            while (it.hasNext()) {
                final SubCategoryItem subCategoryItem = (SubCategoryItem) it.next();
                ComposableLambda rememberComposableLambda = ComposableLambdaKt.rememberComposableLambda(2099624121, true, new Function2() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda23
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj2, Object obj3) {
                        return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$96$lambda$95$lambda$94$lambda$91(SubCategoryItem.this, (Composer) obj2, ((Integer) obj3).intValue());
                    }
                }, composer, 54);
                ComposerKt.sourceInformationMarkerStart(composer, -2109508068, "CC(remember):Dialogs.kt#9igjgp");
                boolean changed = composer.changed($subcatId$delegate) | composer.changed(subCategoryItem);
                Object rememberedValue = $composer.rememberedValue();
                if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                    obj = new Function0() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda24
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$96$lambda$95$lambda$94$lambda$93$lambda$92(SubCategoryItem.this, $subcatId$delegate, $subcatExpanded$delegate);
                        }
                    };
                    $composer.updateRememberedValue(obj);
                } else {
                    obj = rememberedValue;
                }
                ComposerKt.sourceInformationMarkerEnd(composer);
                AndroidMenu_androidKt.DropdownMenuItem(rememberComposableLambda, (Function0) obj, null, null, null, false, null, null, null, composer, 6, 508);
                composer = $composer;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$96$lambda$95$lambda$94$lambda$91(SubCategoryItem $sc, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C196@8412L29:Dialogs.kt#qonjpd");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(2099624121, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:196)");
            }
            String name = $sc.getName();
            TextKt.m2693Text4IGK_g(name + " (" + $sc.getId() + ")", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$96$lambda$95$lambda$94$lambda$93$lambda$92(SubCategoryItem $sc, MutableState $subcatId$delegate, MutableState $subcatExpanded$delegate) {
        $subcatId$delegate.setValue($sc.getId());
        CardFormDialog$lambda$47($subcatExpanded$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$101$lambda$98$lambda$97(MutableState $badge$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $badge$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$101$lambda$100$lambda$99(MutableState $badgeType$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $badgeType$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$106$lambda$103$lambda$102(MutableState $fallbackText$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $fallbackText$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$106$lambda$105$lambda$104(MutableState $icon$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $icon$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$108$lambda$107(MutableState $highlights$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $highlights$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$110$lambda$109(MutableState $btnExpanded$delegate, boolean it) {
        CardFormDialog$lambda$50($btnExpanded$delegate, !CardFormDialog$lambda$49($btnExpanded$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$121(final MutableState $buttonType$delegate, final MutableState $btnExpanded$delegate, ExposedDropdownMenuBoxScope ExposedDropdownMenuBox, Composer $composer, int $changed) {
        Object obj;
        final MutableState mutableState;
        Object obj2;
        Intrinsics.checkNotNullParameter(ExposedDropdownMenuBox, "$this$ExposedDropdownMenuBox");
        ComposerKt.sourceInformation($composer, "C276@11874L2,279@12020L68,274@11757L596,287@12498L23,289@12604L469,285@12378L695:Dialogs.kt#qonjpd");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= ($changed & 8) == 0 ? $composer.changed(ExposedDropdownMenuBox) : $composer.changedInstance(ExposedDropdownMenuBox) ? 4 : 2;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1530198908, $dirty2, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:274)");
            }
            String label = CardFormDialog$lambda$11($buttonType$delegate).getLabel();
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            Modifier fillMaxWidth$default = SizeKt.fillMaxWidth$default(ExposedDropdownMenuBoxScope.m2079menuAnchorfsE2BvY$default(ExposedDropdownMenuBox, Modifier.Companion, MenuAnchorType.Companion.m2225getPrimaryNotEditableMg6Rgbw(), false, 2, null), 0.0f, 1, null);
            ComposerKt.sourceInformationMarkerStart($composer, 1642339934, "CC(remember):Dialogs.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function1() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj3) {
                        return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$121$lambda$112$lambda$111((String) obj3);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            OutlinedTextFieldKt.OutlinedTextField(label, (Function1<? super String, Unit>) obj, fillMaxWidth$default, false, true, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$DialogsKt.INSTANCE.m6941getLambda$460701278$app(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableLambdaKt.rememberComposableLambda(-1897313947, true, new Function2() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda11
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj3, Object obj4) {
                    return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$121$lambda$113(MutableState.this, (Composer) obj3, ((Integer) obj4).intValue());
                }
            }, $composer, 54), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, false, 0, 0, (MutableInteractionSource) null, (Shape) m953RoundedCornerShape0680j_4, (TextFieldColors) null, $composer, 806903856, 0, 0, 6290856);
            boolean CardFormDialog$lambda$49 = CardFormDialog$lambda$49($btnExpanded$delegate);
            ComposerKt.sourceInformationMarkerStart($composer, 1642359923, "CC(remember):Dialogs.kt#9igjgp");
            Object rememberedValue2 = $composer.rememberedValue();
            if (rememberedValue2 == Composer.Companion.getEmpty()) {
                mutableState = $btnExpanded$delegate;
                obj2 = new Function0() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda22
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$121$lambda$115$lambda$114(MutableState.this);
                    }
                };
                $composer.updateRememberedValue(obj2);
            } else {
                mutableState = $btnExpanded$delegate;
                obj2 = rememberedValue2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ExposedDropdownMenuBox.m2081ExposedDropdownMenuvNxi1II(CardFormDialog$lambda$49, (Function0) obj2, null, null, false, null, ColorKt.getPaperSoft(), 0.0f, 0.0f, null, ComposableLambdaKt.rememberComposableLambda(-1109439618, true, new Function3() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda33
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj3, Object obj4, Object obj5) {
                    return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$121$lambda$120(MutableState.this, mutableState, (ColumnScope) obj3, (Composer) obj4, ((Integer) obj5).intValue());
                }
            }, $composer, 54), $composer, 1572912, (ExposedDropdownMenuBoxScope.$stable << 3) | 6 | (($dirty2 << 3) & 112), 956);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$121$lambda$112$lambda$111(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$121$lambda$113(MutableState $btnExpanded$delegate, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C279@12050L36:Dialogs.kt#qonjpd");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1897313947, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:279)");
            }
            ExposedDropdownMenuDefaults.INSTANCE.TrailingIcon(CardFormDialog$lambda$49($btnExpanded$delegate), null, $composer, ExposedDropdownMenuDefaults.$stable << 6, 2);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$121$lambda$115$lambda$114(MutableState $btnExpanded$delegate) {
        CardFormDialog$lambda$50($btnExpanded$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$121$lambda$120(final MutableState $buttonType$delegate, final MutableState $btnExpanded$delegate, ColumnScope ExposedDropdownMenu, Composer $composer, int $changed) {
        Object obj;
        Composer composer = $composer;
        Intrinsics.checkNotNullParameter(ExposedDropdownMenu, "$this$ExposedDropdownMenu");
        ComposerKt.sourceInformation(composer, "C*292@12762L18,293@12828L155,291@12701L316:Dialogs.kt#qonjpd");
        if (($changed & 17) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1109439618, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:290)");
            }
            for (final ButtonType buttonType : ButtonType.getEntries()) {
                ComposableLambda rememberComposableLambda = ComposableLambdaKt.rememberComposableLambda(1278877027, true, new Function2() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda48
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj2, Object obj3) {
                        return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$121$lambda$120$lambda$119$lambda$116(ButtonType.this, (Composer) obj2, ((Integer) obj3).intValue());
                    }
                }, composer, 54);
                ComposerKt.sourceInformationMarkerStart(composer, 1958592078, "CC(remember):Dialogs.kt#9igjgp");
                boolean changed = composer.changed($buttonType$delegate) | composer.changed(buttonType.ordinal());
                Object rememberedValue = $composer.rememberedValue();
                if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                    obj = new Function0() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda49
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$121$lambda$120$lambda$119$lambda$118$lambda$117(ButtonType.this, $buttonType$delegate, $btnExpanded$delegate);
                        }
                    };
                    $composer.updateRememberedValue(obj);
                } else {
                    obj = rememberedValue;
                }
                ComposerKt.sourceInformationMarkerEnd(composer);
                AndroidMenu_androidKt.DropdownMenuItem(rememberComposableLambda, (Function0) obj, null, null, null, false, null, null, null, composer, 6, 508);
                composer = $composer;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$121$lambda$120$lambda$119$lambda$116(ButtonType $bt, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C292@12764L14:Dialogs.kt#qonjpd");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1278877027, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:292)");
            }
            TextKt.m2693Text4IGK_g($bt.getLabel(), (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$121$lambda$120$lambda$119$lambda$118$lambda$117(ButtonType $bt, MutableState $buttonType$delegate, MutableState $btnExpanded$delegate) {
        $buttonType$delegate.setValue($bt);
        CardFormDialog$lambda$50($btnExpanded$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$123$lambda$122(MutableState $statusExpanded$delegate, boolean it) {
        CardFormDialog$lambda$53($statusExpanded$delegate, !CardFormDialog$lambda$52($statusExpanded$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$134(final MutableState $status$delegate, final MutableState $statusExpanded$delegate, ExposedDropdownMenuBoxScope ExposedDropdownMenuBox, Composer $composer, int $changed) {
        Object obj;
        final MutableState mutableState;
        Object obj2;
        Intrinsics.checkNotNullParameter(ExposedDropdownMenuBox, "$this$ExposedDropdownMenuBox");
        ComposerKt.sourceInformation($composer, "C310@13515L2,313@13661L71,308@13402L595,321@14145L26,323@14254L468,319@14022L700:Dialogs.kt#qonjpd");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= ($changed & 8) == 0 ? $composer.changed(ExposedDropdownMenuBox) : $composer.changedInstance(ExposedDropdownMenuBox) ? 4 : 2;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-977381083, $dirty2, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:308)");
            }
            String label = CardFormDialog$lambda$14($status$delegate).getLabel();
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            Modifier fillMaxWidth$default = SizeKt.fillMaxWidth$default(ExposedDropdownMenuBoxScope.m2079menuAnchorfsE2BvY$default(ExposedDropdownMenuBox, Modifier.Companion, MenuAnchorType.Companion.m2225getPrimaryNotEditableMg6Rgbw(), false, 2, null), 0.0f, 1, null);
            ComposerKt.sourceInformationMarkerStart($composer, 1334621511, "CC(remember):Dialogs.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function1() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda53
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj3) {
                        return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$134$lambda$125$lambda$124((String) obj3);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            OutlinedTextFieldKt.OutlinedTextField(label, (Function1<? super String, Unit>) obj, fillMaxWidth$default, false, true, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$DialogsKt.INSTANCE.getLambda$1322954187$app(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableLambdaKt.rememberComposableLambda(-577318706, true, new Function2() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda54
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj3, Object obj4) {
                    return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$134$lambda$126(MutableState.this, (Composer) obj3, ((Integer) obj4).intValue());
                }
            }, $composer, 54), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, false, 0, 0, (MutableInteractionSource) null, (Shape) m953RoundedCornerShape0680j_4, (TextFieldColors) null, $composer, 806903856, 0, 0, 6290856);
            boolean CardFormDialog$lambda$52 = CardFormDialog$lambda$52($statusExpanded$delegate);
            ComposerKt.sourceInformationMarkerStart($composer, 1334641695, "CC(remember):Dialogs.kt#9igjgp");
            Object rememberedValue2 = $composer.rememberedValue();
            if (rememberedValue2 == Composer.Companion.getEmpty()) {
                mutableState = $statusExpanded$delegate;
                obj2 = new Function0() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$134$lambda$128$lambda$127(MutableState.this);
                    }
                };
                $composer.updateRememberedValue(obj2);
            } else {
                mutableState = $statusExpanded$delegate;
                obj2 = rememberedValue2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ExposedDropdownMenuBox.m2081ExposedDropdownMenuvNxi1II(CardFormDialog$lambda$52, (Function0) obj2, null, null, false, null, ColorKt.getPaperSoft(), 0.0f, 0.0f, null, ComposableLambdaKt.rememberComposableLambda(655667367, true, new Function3() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj3, Object obj4, Object obj5) {
                    return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$134$lambda$133(MutableState.this, mutableState, (ColumnScope) obj3, (Composer) obj4, ((Integer) obj5).intValue());
                }
            }, $composer, 54), $composer, 1572912, (ExposedDropdownMenuBoxScope.$stable << 3) | 6 | (($dirty2 << 3) & 112), 956);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$134$lambda$125$lambda$124(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$134$lambda$126(MutableState $statusExpanded$delegate, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C313@13691L39:Dialogs.kt#qonjpd");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-577318706, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:313)");
            }
            ExposedDropdownMenuDefaults.INSTANCE.TrailingIcon(CardFormDialog$lambda$52($statusExpanded$delegate), null, $composer, ExposedDropdownMenuDefaults.$stable << 6, 2);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$134$lambda$128$lambda$127(MutableState $statusExpanded$delegate) {
        CardFormDialog$lambda$53($statusExpanded$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$134$lambda$133(final MutableState $status$delegate, final MutableState $statusExpanded$delegate, ColumnScope ExposedDropdownMenu, Composer $composer, int $changed) {
        Object obj;
        Composer composer = $composer;
        Intrinsics.checkNotNullParameter(ExposedDropdownMenu, "$this$ExposedDropdownMenu");
        ComposerKt.sourceInformation(composer, "C*326@14412L18,327@14478L154,325@14351L315:Dialogs.kt#qonjpd");
        if (($changed & 17) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(655667367, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:324)");
            }
            for (final CardStatus cardStatus : CardStatus.getEntries()) {
                ComposableLambda rememberComposableLambda = ComposableLambdaKt.rememberComposableLambda(-1114972682, true, new Function2() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda25
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj2, Object obj3) {
                        return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$134$lambda$133$lambda$132$lambda$129(CardStatus.this, (Composer) obj2, ((Integer) obj3).intValue());
                    }
                }, composer, 54);
                ComposerKt.sourceInformationMarkerStart(composer, -2064950752, "CC(remember):Dialogs.kt#9igjgp");
                boolean changed = composer.changed($status$delegate) | composer.changed(cardStatus.ordinal());
                Object rememberedValue = $composer.rememberedValue();
                if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                    obj = new Function0() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda26
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return DialogsKt.CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$134$lambda$133$lambda$132$lambda$131$lambda$130(CardStatus.this, $status$delegate, $statusExpanded$delegate);
                        }
                    };
                    $composer.updateRememberedValue(obj);
                } else {
                    obj = rememberedValue;
                }
                ComposerKt.sourceInformationMarkerEnd(composer);
                AndroidMenu_androidKt.DropdownMenuItem(rememberComposableLambda, (Function0) obj, null, null, null, false, null, null, null, composer, 6, 508);
                composer = $composer;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$134$lambda$133$lambda$132$lambda$129(CardStatus $st, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C326@14414L14:Dialogs.kt#qonjpd");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1114972682, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (Dialogs.kt:326)");
            }
            TextKt.m2693Text4IGK_g($st.getLabel(), (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$137$lambda$136$lambda$135$lambda$134$lambda$133$lambda$132$lambda$131$lambda$130(CardStatus $st, MutableState $status$delegate, MutableState $statusExpanded$delegate) {
        $status$delegate.setValue($st);
        CardFormDialog$lambda$53($statusExpanded$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0154  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit CardFormDialog$lambda$59(final androidx.compose.runtime.MutableState r35, final androidx.compose.runtime.MutableState r36, final androidx.compose.runtime.MutableState r37, final kotlin.jvm.functions.Function12 r38, final androidx.compose.runtime.MutableState r39, final androidx.compose.runtime.MutableState r40, final androidx.compose.runtime.MutableState r41, final androidx.compose.runtime.MutableState r42, final androidx.compose.runtime.MutableState r43, final androidx.compose.runtime.MutableState r44, final androidx.compose.runtime.MutableState r45, final androidx.compose.runtime.MutableState r46, final androidx.compose.runtime.MutableState r47, final androidx.compose.runtime.MutableState r48, final kotlin.jvm.functions.Function0 r49, final boolean r50, androidx.compose.runtime.Composer r51, int r52) {
        /*
            Method dump skipped, instructions count: 346
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.DialogsKt.CardFormDialog$lambda$59(androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, kotlin.jvm.functions.Function12, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, kotlin.jvm.functions.Function0, boolean, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$59$lambda$57$lambda$56(Function12 $onSubmit, Function0 $onDismiss, MutableState $name$delegate, MutableState $error$delegate, MutableState $description$delegate, MutableState $url$delegate, MutableState $buttonType$delegate, MutableState $status$delegate, MutableState $category$delegate, MutableState $subcatId$delegate, MutableState $icon$delegate, MutableState $fallbackText$delegate, MutableState $badge$delegate, MutableState $badgeType$delegate, MutableState $highlights$delegate) {
        if (!(StringsKt.trim((CharSequence) CardFormDialog$lambda$2($name$delegate)).toString().length() == 0)) {
            if (!(StringsKt.trim((CharSequence) CardFormDialog$lambda$5($description$delegate)).toString().length() == 0)) {
                $onSubmit.invoke(CardFormDialog$lambda$2($name$delegate), CardFormDialog$lambda$5($description$delegate), CardFormDialog$lambda$8($url$delegate), CardFormDialog$lambda$11($buttonType$delegate), CardFormDialog$lambda$14($status$delegate), CardFormDialog$lambda$18($category$delegate), CardFormDialog$lambda$22($subcatId$delegate), CardFormDialog$lambda$25($icon$delegate), CardFormDialog$lambda$28($fallbackText$delegate), CardFormDialog$lambda$31($badge$delegate), CardFormDialog$lambda$34($badgeType$delegate), CardFormDialog$lambda$37($highlights$delegate));
                $onDismiss.invoke();
                return Unit.INSTANCE;
            }
            $error$delegate.setValue("请填写卡片描述");
            return Unit.INSTANCE;
        }
        $error$delegate.setValue("请填写卡片名称");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$59$lambda$58(boolean $isEdit, RowScope Button, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(Button, "$this$Button");
        ComposerKt.sourceInformation($composer, "C377@16189L36:Dialogs.kt#qonjpd");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-847011303, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous>.<anonymous> (Dialogs.kt:377)");
            }
            TextKt.m2693Text4IGK_g($isEdit ? "保存修改" : "创建卡片", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CardFormDialog$lambda$60(Function0 $onDismiss, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C381@16289L228:Dialogs.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-125718841, $changed, -1, "com.example.ui.components.CardFormDialog.<anonymous> (Dialogs.kt:381)");
            }
            ButtonKt.OutlinedButton($onDismiss, null, false, RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12)), null, null, BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist()), null, null, ComposableSingletons$DialogsKt.INSTANCE.m6940getLambda$201994091$app(), $composer, 806879232, 438);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    public static final void ConfirmDeleteDialog(final boolean open, final String title, final String itemName, String extraWarning, final Function0<Unit> onDismiss, final Function0<Unit> onConfirm, Composer $composer, final int $changed, final int i) {
        Object obj;
        final String extraWarning2;
        Composer $composer2;
        final String extraWarning3;
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(itemName, "itemName");
        Intrinsics.checkNotNullParameter(onDismiss, "onDismiss");
        Intrinsics.checkNotNullParameter(onConfirm, "onConfirm");
        Composer $composer3 = $composer.startRestartGroup(857260154);
        ComposerKt.sourceInformation($composer3, "C(ConfirmDeleteDialog)P(4,5,1!1,3)431@17759L419,444@18204L252,407@16898L167,414@17082L651,403@16752L1710:Dialogs.kt#qonjpd");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(open) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changed(title) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            $dirty |= $composer3.changed(itemName) ? 256 : 128;
        }
        int i2 = i & 8;
        if (i2 != 0) {
            $dirty |= 3072;
            obj = extraWarning;
        } else if (($changed & 3072) == 0) {
            obj = extraWarning;
            $dirty |= $composer3.changed(obj) ? 2048 : 1024;
        } else {
            obj = extraWarning;
        }
        if (($changed & 24576) == 0) {
            $dirty |= $composer3.changedInstance(onDismiss) ? 16384 : 8192;
        }
        if ((196608 & $changed) == 0) {
            $dirty |= $composer3.changedInstance(onConfirm) ? 131072 : 65536;
        }
        int $dirty2 = $dirty;
        if ((74899 & $dirty2) == 74898 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
            extraWarning3 = obj;
        } else {
            if (i2 != 0) {
                extraWarning2 = null;
            } else {
                extraWarning2 = obj;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(857260154, $dirty2, -1, "com.example.ui.components.ConfirmDeleteDialog (Dialogs.kt:400)");
            }
            if (open) {
                final String extraWarning4 = extraWarning2;
                $composer2 = $composer3;
                AndroidAlertDialog_androidKt.m1762AlertDialogOix01E0(onDismiss, ComposableLambdaKt.rememberComposableLambda(-1918662862, true, new Function2() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda36
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj2, Object obj3) {
                        return DialogsKt.ConfirmDeleteDialog$lambda$142(Function0.this, onDismiss, (Composer) obj2, ((Integer) obj3).intValue());
                    }
                }, $composer3, 54), null, ComposableLambdaKt.rememberComposableLambda(1835874928, true, new Function2() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda37
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj2, Object obj3) {
                        return DialogsKt.ConfirmDeleteDialog$lambda$143(Function0.this, (Composer) obj2, ((Integer) obj3).intValue());
                    }
                }, $composer3, 54), null, ComposableLambdaKt.rememberComposableLambda(1295445422, true, new Function2() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda38
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj2, Object obj3) {
                        return DialogsKt.ConfirmDeleteDialog$lambda$144(title, (Composer) obj2, ((Integer) obj3).intValue());
                    }
                }, $composer3, 54), ComposableLambdaKt.rememberComposableLambda(-1122252979, true, new Function2() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda39
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj2, Object obj3) {
                        return DialogsKt.ConfirmDeleteDialog$lambda$146(itemName, extraWarning4, (Composer) obj2, ((Integer) obj3).intValue());
                    }
                }, $composer3, 54), RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(20)), ColorKt.getPaperSoft(), 0L, 0L, 0L, 0.0f, null, $composer2, (($dirty2 >> 12) & 14) | 102435888, 0, 15892);
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventEnd();
                }
                extraWarning3 = extraWarning4;
            } else {
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventEnd();
                }
                ScopeUpdateScope endRestartGroup = $composer3.endRestartGroup();
                if (endRestartGroup != null) {
                    endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda35
                        @Override // kotlin.jvm.functions.Function2
                        public final Object invoke(Object obj2, Object obj3) {
                            return DialogsKt.ConfirmDeleteDialog$lambda$139(open, title, itemName, extraWarning2, onDismiss, onConfirm, $changed, i, (Composer) obj2, ((Integer) obj3).intValue());
                        }
                    });
                    return;
                }
                return;
            }
        }
        ScopeUpdateScope endRestartGroup2 = $composer2.endRestartGroup();
        if (endRestartGroup2 != null) {
            endRestartGroup2.updateScope(new Function2() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda40
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return DialogsKt.ConfirmDeleteDialog$lambda$147(open, title, itemName, extraWarning3, onDismiss, onConfirm, $changed, i, (Composer) obj2, ((Integer) obj3).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ConfirmDeleteDialog$lambda$144(String $title, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C410@16986L10,408@16912L143:Dialogs.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1295445422, $changed, -1, "com.example.ui.components.ConfirmDeleteDialog.<anonymous> (Dialogs.kt:408)");
            }
            TextKt.m2693Text4IGK_g($title, (Modifier) null, ColorKt.getInkBlack(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getTitleLarge(), $composer, 384, 0, 65530);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:34:0x01ad  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x01fc  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0222  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit ConfirmDeleteDialog$lambda$146(java.lang.String r49, java.lang.String r50, androidx.compose.runtime.Composer r51, int r52) {
        /*
            Method dump skipped, instructions count: 552
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.DialogsKt.ConfirmDeleteDialog$lambda$146(java.lang.String, java.lang.String, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ConfirmDeleteDialog$lambda$142(final Function0 $onConfirm, final Function0 $onDismiss, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C437@17932L61,433@17807L83,432@17773L395:Dialogs.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1918662862, $changed, -1, "com.example.ui.components.ConfirmDeleteDialog.<anonymous> (Dialogs.kt:432)");
            }
            ButtonColors m1809buttonColorsro_MJ88 = ButtonDefaults.INSTANCE.m1809buttonColorsro_MJ88(ColorKt.getCinnabar(), ColorKt.getPaper(), 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 54, 12);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            Modifier testTag = TestTagKt.testTag(Modifier.Companion, "confirm_delete_btn");
            ComposerKt.sourceInformationMarkerStart($composer, -1294038779, "CC(remember):Dialogs.kt#9igjgp");
            boolean changed = $composer.changed($onConfirm) | $composer.changed($onDismiss);
            Object rememberedValue = $composer.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.components.DialogsKt$$ExternalSyntheticLambda27
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DialogsKt.ConfirmDeleteDialog$lambda$142$lambda$141$lambda$140(Function0.this, $onDismiss);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button((Function0) obj, testTag, false, m953RoundedCornerShape0680j_4, m1809buttonColorsro_MJ88, null, null, null, null, ComposableSingletons$DialogsKt.INSTANCE.getLambda$163553602$app(), $composer, 805306416, 484);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ConfirmDeleteDialog$lambda$142$lambda$141$lambda$140(Function0 $onConfirm, Function0 $onDismiss) {
        $onConfirm.invoke();
        $onDismiss.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ConfirmDeleteDialog$lambda$143(Function0 $onDismiss, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C445@18218L228:Dialogs.kt#qonjpd");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1835874928, $changed, -1, "com.example.ui.components.ConfirmDeleteDialog.<anonymous> (Dialogs.kt:445)");
            }
            ButtonKt.OutlinedButton($onDismiss, null, false, RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12)), null, null, BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist()), null, null, ComposableSingletons$DialogsKt.INSTANCE.getLambda$1549803710$app(), $composer, 806879232, 438);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }
}
