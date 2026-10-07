package com.example.ui.screens;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.activity.compose.ManagedActivityResultLauncher;
import androidx.autofill.HintConstants;
import androidx.compose.foundation.BorderStroke;
import androidx.compose.foundation.BorderStrokeKt;
import androidx.compose.foundation.layout.PaddingKt;
import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.SpacerKt;
import androidx.compose.foundation.shape.RoundedCornerShape;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.material3.ButtonColors;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.platform.TestTagKt;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.unit.Dp;
import com.example.model.ResourceButton;
import com.example.model.SkillItem;
import com.example.model.TextItem;
import com.example.ui.theme.ColorKt;
import com.example.viewmodel.AdminUiState;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.DebugKt;
/* compiled from: ButtonAndTextScreens.kt */
@Metadata(d1 = {"\u0000\\\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0019\u001aÊ\u0003\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u008b\u0002\u0010\u0004\u001a\u0086\u0002\u0012\u0015\u0012\u0013\u0018\u00010\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\b\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\r\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0011\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0012\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0013\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0014\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00162}\b\u0002\u0010\u0017\u001aw\u0012\u0013\u0012\u00110\u0019¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u001a\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u001b\u0012C\u0012A\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u001d\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u001e\u0012\u0004\u0012\u00020\u00010\u001c¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u001f\u0012\u0004\u0012\u00020\u00010\u00182\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u0016H\u0007¢\u0006\u0002\u0010!\u001aß\u0003\u0010\"\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032 \u0002\u0010#\u001a\u009b\u0002\u0012\u0015\u0012\u0013\u0018\u00010%¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(&\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b('\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b((\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\r\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0012\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b()\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0013\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0014\u0012\u0004\u0012\u00020\u00010$2\u0012\u0010*\u001a\u000e\u0012\u0004\u0012\u00020%\u0012\u0004\u0012\u00020\u00010\u00162}\b\u0002\u0010\u0017\u001aw\u0012\u0013\u0012\u00110\u0019¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u001a\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u001b\u0012C\u0012A\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u001d\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u001e\u0012\u0004\u0012\u00020\u00010\u001c¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u001f\u0012\u0004\u0012\u00020\u00010\u00182\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u0016H\u0007¢\u0006\u0002\u0010+\u001a\u008a\u0001\u0010,\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032K\u0010-\u001aG\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(.\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(/\u0012\u0013\u0012\u001100¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(1\u0012\u0004\u0012\u00020\u00010\u00182\u0012\u00102\u001a\u000e\u0012\u0004\u0012\u000203\u0012\u0004\u0012\u00020\u00010\u00162\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u0016H\u0007¢\u0006\u0002\u00104¨\u00065²\u0006\n\u00106\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u00107\u001a\u000200X\u008a\u008e\u0002²\u0006\f\u00108\u001a\u0004\u0018\u00010\u0006X\u008a\u008e\u0002²\u0006\f\u00109\u001a\u0004\u0018\u00010\u0006X\u008a\u008e\u0002²\u0006\n\u0010:\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010;\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010<\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010=\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010>\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010?\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010@\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010A\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010B\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010C\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010D\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u00106\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u00107\u001a\u000200X\u008a\u008e\u0002²\u0006\f\u0010E\u001a\u0004\u0018\u00010%X\u008a\u008e\u0002²\u0006\f\u0010F\u001a\u0004\u0018\u00010%X\u008a\u008e\u0002²\u0006\n\u0010G\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010<\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010H\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010I\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010;\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010=\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010>\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010@\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010B\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010J\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010C\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010D\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u00106\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u00107\u001a\u000200X\u008a\u008e\u0002²\u0006\n\u0010K\u001a\u000200X\u008a\u008e\u0002²\u0006\n\u0010.\u001a\u00020\nX\u008a\u008e\u0002²\u0006\n\u0010/\u001a\u00020\nX\u008a\u008e\u0002²\u0006\f\u0010L\u001a\u0004\u0018\u000103X\u008a\u008e\u0002"}, d2 = {"ButtonManagementScreen", "", "uiState", "Lcom/example/viewmodel/AdminUiState;", "onSaveSoftware", "Lkotlin/Function12;", "Lcom/example/model/ResourceButton;", "Lkotlin/ParameterName;", HintConstants.AUTOFILL_HINT_NAME, "existing", "", "url", "desc", "author", "badge", "badgeType", "tags", "apkUrl", "previewUrl", "iconUrl", "mode", "onDeleteSoftware", "Lkotlin/Function1;", "onUploadFile", "Lkotlin/Function3;", "Landroid/net/Uri;", "uri", "subFolder", "Lkotlin/Function2;", "downloadUrl", "fileName", "onSuccess", "onShowToast", "(Lcom/example/viewmodel/AdminUiState;Lkotlin/jvm/functions/Function12;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;II)V", "SkillManagementScreen", "onSaveSkill", "Lkotlin/Function13;", "Lcom/example/model/SkillItem;", "title", "promptType", "prompt", "mediaUrl", "onDeleteSkill", "(Lcom/example/viewmodel/AdminUiState;Lkotlin/jvm/functions/Function13;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;II)V", "TextManagementScreen", "onSaveText", "keyName", "content", "", "isNew", "onDeleteText", "Lcom/example/model/TextItem;", "(Lcom/example/viewmodel/AdminUiState;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "app", "keyword", "dialogOpen", "editingBtn", "deletingBtn", "formName", "formUrl", "formDesc", "formAuthor", "formBadge", "formBadgeType", "formTags", "formApkUrl", "formPreviewUrl", "formIconUrl", "formMode", "editingSkill", "deletingSkill", "formTitle", "formPromptType", "formPrompt", "formMediaUrl", "isNewText", "deletingItem"}, k = 2, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class ButtonAndTextScreensKt {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$143(AdminUiState adminUiState, Function12 function12, Function1 function1, Function3 function3, Function1 function13, int i, int i2, Composer composer, int i3) {
        ButtonManagementScreen(adminUiState, function12, function1, function3, function13, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$287(AdminUiState adminUiState, Function13 function13, Function1 function1, Function3 function3, Function1 function12, int i, int i2, Composer composer, int i3) {
        SkillManagementScreen(adminUiState, function13, function1, function3, function12, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$350(AdminUiState adminUiState, Function3 function3, Function1 function1, Function1 function12, int i, Composer composer, int i2) {
        TextManagementScreen(adminUiState, function3, function1, function12, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$1$lambda$0(Uri uri, String str, Function2 function2) {
        Intrinsics.checkNotNullParameter(uri, "<unused var>");
        Intrinsics.checkNotNullParameter(str, "<unused var>");
        Intrinsics.checkNotNullParameter(function2, "<unused var>");
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:132:0x0441  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0443  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0450  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x045e  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x048c  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x048e  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0498  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x04a5  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x04fa  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0543  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0546  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x054a  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x055c  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x060a  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x0610  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x06e7  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x06f3  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x06f9  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x072a  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0740  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x0890  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x09a7  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x09d6  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x09d9  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x09e2  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x0a05  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x0a15  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0a32  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0a3d  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0a7b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void ButtonManagementScreen(final com.example.viewmodel.AdminUiState r79, final kotlin.jvm.functions.Function12<? super com.example.model.ResourceButton, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> r80, final kotlin.jvm.functions.Function1<? super com.example.model.ResourceButton, kotlin.Unit> r81, kotlin.jvm.functions.Function3<? super android.net.Uri, ? super java.lang.String, ? super kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit>, kotlin.Unit> r82, final kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> r83, androidx.compose.runtime.Composer r84, final int r85, final int r86) {
        /*
            Method dump skipped, instructions count: 2715
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.ButtonManagementScreen(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function12, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function3, kotlin.jvm.functions.Function1, androidx.compose.runtime.Composer, int, int):void");
    }

    private static final String ButtonManagementScreen$lambda$3(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final boolean ButtonManagementScreen$lambda$6(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void ButtonManagementScreen$lambda$7(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final ResourceButton ButtonManagementScreen$lambda$9(MutableState<ResourceButton> mutableState) {
        return mutableState.getValue();
    }

    private static final ResourceButton ButtonManagementScreen$lambda$12(MutableState<ResourceButton> mutableState) {
        return mutableState.getValue();
    }

    private static final String ButtonManagementScreen$lambda$15(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ButtonManagementScreen$lambda$18(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ButtonManagementScreen$lambda$21(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ButtonManagementScreen$lambda$24(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ButtonManagementScreen$lambda$27(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ButtonManagementScreen$lambda$30(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ButtonManagementScreen$lambda$33(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ButtonManagementScreen$lambda$36(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ButtonManagementScreen$lambda$39(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ButtonManagementScreen$lambda$42(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ButtonManagementScreen$lambda$45(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$49$lambda$48(Function3 $onUploadFile, final MutableState $formApkUrl$delegate, final MutableState $formMode$delegate, final MutableState $formName$delegate, Uri uri) {
        if (uri != null) {
            $onUploadFile.invoke(uri, DebugKt.DEBUG_PROPERTY_VALUE_AUTO, new Function2() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda33
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return ButtonAndTextScreensKt.ButtonManagementScreen$lambda$49$lambda$48$lambda$47(MutableState.this, $formMode$delegate, $formName$delegate, (String) obj, (String) obj2);
                }
            });
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$49$lambda$48$lambda$47(MutableState $formApkUrl$delegate, MutableState $formMode$delegate, MutableState $formName$delegate, String downloadUrl, String fileName) {
        Intrinsics.checkNotNullParameter(downloadUrl, "downloadUrl");
        Intrinsics.checkNotNullParameter(fileName, "fileName");
        $formApkUrl$delegate.setValue(downloadUrl);
        $formMode$delegate.setValue("file");
        if (StringsKt.isBlank(ButtonManagementScreen$lambda$15($formName$delegate))) {
            $formName$delegate.setValue(StringsKt.substringBeforeLast$default(fileName, '.', (String) null, 2, (Object) null));
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$52$lambda$51(Function3 $onUploadFile, final MutableState $formIconUrl$delegate, Uri uri) {
        if (uri != null) {
            $onUploadFile.invoke(uri, "dist/uploads", new Function2() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda54
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return ButtonAndTextScreensKt.ButtonManagementScreen$lambda$52$lambda$51$lambda$50(MutableState.this, (String) obj, (String) obj2);
                }
            });
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$52$lambda$51$lambda$50(MutableState $formIconUrl$delegate, String downloadUrl, String str) {
        Intrinsics.checkNotNullParameter(downloadUrl, "downloadUrl");
        Intrinsics.checkNotNullParameter(str, "<unused var>");
        $formIconUrl$delegate.setValue(downloadUrl);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$55$lambda$54(Function3 $onUploadFile, final MutableState $formPreviewUrl$delegate, Uri uri) {
        if (uri != null) {
            $onUploadFile.invoke(uri, "dist/uploads", new Function2() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda52
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return ButtonAndTextScreensKt.ButtonManagementScreen$lambda$55$lambda$54$lambda$53(MutableState.this, (String) obj, (String) obj2);
                }
            });
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$55$lambda$54$lambda$53(MutableState $formPreviewUrl$delegate, String downloadUrl, String str) {
        Intrinsics.checkNotNullParameter(downloadUrl, "downloadUrl");
        Intrinsics.checkNotNullParameter(str, "<unused var>");
        $formPreviewUrl$delegate.setValue(downloadUrl);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$84$lambda$60(final MutableState $editingBtn$delegate, final MutableState $formName$delegate, final MutableState $formUrl$delegate, final MutableState $formDesc$delegate, final MutableState $formAuthor$delegate, final MutableState $formBadge$delegate, final MutableState $formBadgeType$delegate, final MutableState $formTags$delegate, final MutableState $formApkUrl$delegate, final MutableState $formPreviewUrl$delegate, final MutableState $formIconUrl$delegate, final MutableState $formMode$delegate, final MutableState $dialogOpen$delegate, RowScope PageHeader, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter(PageHeader, "$this$PageHeader");
        ComposerKt.sourceInformation($composer, "C139@5155L38,156@5854L61,141@5248L560,140@5210L1071:ButtonAndTextScreens.kt#2thlc2");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer.changed(PageHeader) ? 4 : 2;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1457287230, $dirty2, -1, "com.example.ui.screens.ButtonManagementScreen.<anonymous>.<anonymous> (ButtonAndTextScreens.kt:139)");
            }
            SpacerKt.Spacer(RowScope.weight$default(PageHeader, Modifier.Companion, 1.0f, false, 2, null), $composer, 0);
            ButtonColors m1809buttonColorsro_MJ88 = ButtonDefaults.INSTANCE.m1809buttonColorsro_MJ88(ColorKt.getCinnabar(), ColorKt.getPaper(), 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 54, 12);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            Modifier testTag = TestTagKt.testTag(Modifier.Companion, "create_button_btn");
            ComposerKt.sourceInformationMarkerStart($composer, -293685422, "CC(remember):ButtonAndTextScreens.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda34
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return ButtonAndTextScreensKt.ButtonManagementScreen$lambda$84$lambda$60$lambda$59$lambda$58(MutableState.this, $formName$delegate, $formUrl$delegate, $formDesc$delegate, $formAuthor$delegate, $formBadge$delegate, $formBadgeType$delegate, $formTags$delegate, $formApkUrl$delegate, $formPreviewUrl$delegate, $formIconUrl$delegate, $formMode$delegate, $dialogOpen$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button((Function0) obj, testTag, false, m953RoundedCornerShape0680j_4, m1809buttonColorsro_MJ88, null, null, null, null, ComposableSingletons$ButtonAndTextScreensKt.INSTANCE.getLambda$2110279122$app(), $composer, 805306422, 484);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$84$lambda$60$lambda$59$lambda$58(MutableState $editingBtn$delegate, MutableState $formName$delegate, MutableState $formUrl$delegate, MutableState $formDesc$delegate, MutableState $formAuthor$delegate, MutableState $formBadge$delegate, MutableState $formBadgeType$delegate, MutableState $formTags$delegate, MutableState $formApkUrl$delegate, MutableState $formPreviewUrl$delegate, MutableState $formIconUrl$delegate, MutableState $formMode$delegate, MutableState $dialogOpen$delegate) {
        $editingBtn$delegate.setValue(null);
        $formName$delegate.setValue("");
        $formUrl$delegate.setValue("");
        $formDesc$delegate.setValue("");
        $formAuthor$delegate.setValue("");
        $formBadge$delegate.setValue("最新版");
        $formBadgeType$delegate.setValue("");
        $formTags$delegate.setValue("软件 安装包 APK");
        $formApkUrl$delegate.setValue("");
        $formPreviewUrl$delegate.setValue("");
        $formIconUrl$delegate.setValue("");
        $formMode$delegate.setValue("file");
        ButtonManagementScreen$lambda$7($dialogOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01d3  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01df  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01e5  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0327  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0339  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0385  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x03a8  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x04ff A[LOOP:0: B:64:0x04f9->B:66:0x04ff, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x05f0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit ButtonManagementScreen$lambda$84$lambda$83(java.util.List r85, com.example.viewmodel.AdminUiState r86, final androidx.compose.runtime.MutableState r87, final android.content.Context r88, final kotlin.jvm.functions.Function1 r89, final androidx.compose.runtime.MutableState r90, final androidx.compose.runtime.MutableState r91, final androidx.compose.runtime.MutableState r92, final androidx.compose.runtime.MutableState r93, final androidx.compose.runtime.MutableState r94, final androidx.compose.runtime.MutableState r95, final androidx.compose.runtime.MutableState r96, final androidx.compose.runtime.MutableState r97, final androidx.compose.runtime.MutableState r98, final androidx.compose.runtime.MutableState r99, final androidx.compose.runtime.MutableState r100, final androidx.compose.runtime.MutableState r101, final androidx.compose.runtime.MutableState r102, final androidx.compose.runtime.MutableState r103, androidx.compose.runtime.Composer r104, int r105) {
        /*
            Method dump skipped, instructions count: 1526
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.ButtonManagementScreen$lambda$84$lambda$83(java.util.List, com.example.viewmodel.AdminUiState, androidx.compose.runtime.MutableState, android.content.Context, kotlin.jvm.functions.Function1, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$84$lambda$83$lambda$82$lambda$63$lambda$62$lambda$61(MutableState $keyword$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $keyword$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0846  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x08d7  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x08e3  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x08e9  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x091a  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0930  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x098d  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0a3a  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0aa8  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01df  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x030d  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0319  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x031f  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0352  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0368 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0413  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x041f  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x046c  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x04bd  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x04ec  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x054c  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x05c6  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x05d2  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x05d8  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x060b  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0621 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x067a  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x06e9  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0700  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x075e  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x078a  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x079c  */
    /* JADX WARN: Type inference failed for: r3v79 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit ButtonManagementScreen$lambda$84$lambda$83$lambda$82$lambda$81$lambda$80$lambda$79(final com.example.model.ResourceButton r114, final android.content.Context r115, final kotlin.jvm.functions.Function1 r116, final androidx.compose.runtime.MutableState r117, final androidx.compose.runtime.MutableState r118, final androidx.compose.runtime.MutableState r119, final androidx.compose.runtime.MutableState r120, final androidx.compose.runtime.MutableState r121, final androidx.compose.runtime.MutableState r122, final androidx.compose.runtime.MutableState r123, final androidx.compose.runtime.MutableState r124, final androidx.compose.runtime.MutableState r125, final androidx.compose.runtime.MutableState r126, final androidx.compose.runtime.MutableState r127, final androidx.compose.runtime.MutableState r128, final androidx.compose.runtime.MutableState r129, final androidx.compose.runtime.MutableState r130, androidx.compose.runtime.Composer r131, int r132) {
        /*
            Method dump skipped, instructions count: 2734
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.ButtonManagementScreen$lambda$84$lambda$83$lambda$82$lambda$81$lambda$80$lambda$79(com.example.model.ResourceButton, android.content.Context, kotlin.jvm.functions.Function1, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0159  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x017a  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x017f  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01d3  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x01ef  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01f4  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0247  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit ButtonManagementScreen$lambda$84$lambda$83$lambda$82$lambda$81$lambda$80$lambda$79$lambda$78$lambda$72$lambda$67$lambda$65(com.example.model.ResourceButton r51, androidx.compose.runtime.Composer r52, int r53) {
        /*
            Method dump skipped, instructions count: 589
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.ButtonManagementScreen$lambda$84$lambda$83$lambda$82$lambda$81$lambda$80$lambda$79$lambda$78$lambda$72$lambda$67$lambda$65(com.example.model.ResourceButton, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$84$lambda$83$lambda$82$lambda$81$lambda$80$lambda$79$lambda$78$lambda$72$lambda$67$lambda$66(ResourceButton $btn, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C274@12462L10,272@12304L428:ButtonAndTextScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1724472511, $changed, -1, "com.example.ui.screens.ButtonManagementScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (ButtonAndTextScreens.kt:272)");
            }
            String badge = $btn.getBadge();
            TextStyle labelSmall = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall();
            TextKt.m2693Text4IGK_g(badge, PaddingKt.m671paddingVpY3zN4(Modifier.Companion, Dp.m6622constructorimpl(6), Dp.m6622constructorimpl(2)), ColorKt.getGoldDark(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, labelSmall, $composer, 432, 0, 65528);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$84$lambda$83$lambda$82$lambda$81$lambda$80$lambda$79$lambda$78$lambda$72$lambda$71$lambda$70(String $linkUrl, Context $context, Function1 $onShowToast) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse($linkUrl));
            $context.startActivity(intent);
        } catch (Exception e) {
            $onShowToast.invoke("链接：" + $linkUrl);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$84$lambda$83$lambda$82$lambda$81$lambda$80$lambda$79$lambda$78$lambda$77$lambda$74$lambda$73(ResourceButton $btn, MutableState $editingBtn$delegate, MutableState $formName$delegate, MutableState $formUrl$delegate, MutableState $formDesc$delegate, MutableState $formAuthor$delegate, MutableState $formBadge$delegate, MutableState $formBadgeType$delegate, MutableState $formTags$delegate, MutableState $formApkUrl$delegate, MutableState $formPreviewUrl$delegate, MutableState $formIconUrl$delegate, MutableState $formMode$delegate, MutableState $dialogOpen$delegate) {
        $editingBtn$delegate.setValue($btn);
        $formName$delegate.setValue($btn.getName());
        $formUrl$delegate.setValue($btn.getUrl());
        $formDesc$delegate.setValue($btn.getDesc());
        $formAuthor$delegate.setValue($btn.getAuthor());
        $formBadge$delegate.setValue($btn.getBadge());
        $formBadgeType$delegate.setValue($btn.getBadgeType());
        $formTags$delegate.setValue($btn.getTags());
        $formApkUrl$delegate.setValue($btn.getApkUrl());
        $formPreviewUrl$delegate.setValue($btn.getPreviewUrl());
        $formIconUrl$delegate.setValue($btn.getIconUrl());
        $formMode$delegate.setValue($btn.getMode());
        ButtonManagementScreen$lambda$7($dialogOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$84$lambda$83$lambda$82$lambda$81$lambda$80$lambda$79$lambda$78$lambda$77$lambda$76$lambda$75(ResourceButton $btn, MutableState $deletingBtn$delegate) {
        $deletingBtn$delegate.setValue($btn);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$86$lambda$85(MutableState $dialogOpen$delegate) {
        ButtonManagementScreen$lambda$7($dialogOpen$delegate, false);
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
    public static final kotlin.Unit ButtonManagementScreen$lambda$94(androidx.compose.runtime.MutableState r51, androidx.compose.runtime.Composer r52, int r53) {
        /*
            Method dump skipped, instructions count: 484
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.ButtonManagementScreen$lambda$94(androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0a57  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0a6d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0afd  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0b11  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0b9e  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0bb2  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0c8d  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0c99  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0c9f  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0cd0  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0ce6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0d73  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0d87  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0e13  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0e25  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0ea2  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x022e  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x023a  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0240  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0349  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0359  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x038b  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x038d  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x03eb  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x03fb  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x04d4  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x04e6  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0571  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0583  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x060f  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0621  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x06ac  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x06be  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0794  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x07a0  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x07a6  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x07d9  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x07ef A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x087d  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0891  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x091e  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x092e  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0a12  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0a1e  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0a24  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit ButtonManagementScreen$lambda$137(final androidx.compose.runtime.MutableState r124, final com.example.viewmodel.AdminUiState r125, final androidx.activity.compose.ManagedActivityResultLauncher r126, final androidx.activity.compose.ManagedActivityResultLauncher r127, final androidx.activity.compose.ManagedActivityResultLauncher r128, final androidx.compose.runtime.MutableState r129, final androidx.compose.runtime.MutableState r130, final androidx.compose.runtime.MutableState r131, final androidx.compose.runtime.MutableState r132, final androidx.compose.runtime.MutableState r133, final androidx.compose.runtime.MutableState r134, final androidx.compose.runtime.MutableState r135, final androidx.compose.runtime.MutableState r136, final androidx.compose.runtime.MutableState r137, androidx.compose.runtime.Composer r138, int r139) {
        /*
            Method dump skipped, instructions count: 3752
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.ButtonManagementScreen$lambda$137(androidx.compose.runtime.MutableState, com.example.viewmodel.AdminUiState, androidx.activity.compose.ManagedActivityResultLauncher, androidx.activity.compose.ManagedActivityResultLauncher, androidx.activity.compose.ManagedActivityResultLauncher, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$99$lambda$96$lambda$95(MutableState $formMode$delegate) {
        $formMode$delegate.setValue("file");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$99$lambda$98$lambda$97(MutableState $formMode$delegate) {
        $formMode$delegate.setValue("url");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:103:0x085d  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x086b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:111:0x08eb  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x08f8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0961  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01ec  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01f2  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0308  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0314  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x031a  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x034d  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0363 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0457  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x05b9  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0616  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0699  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x06a5  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x06ab  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x06dc  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x06f2  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x07bf  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x07cf A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$112(com.example.viewmodel.AdminUiState r108, final androidx.activity.compose.ManagedActivityResultLauncher r109, final androidx.activity.compose.ManagedActivityResultLauncher r110, final androidx.activity.compose.ManagedActivityResultLauncher r111, androidx.compose.runtime.Composer r112, int r113) {
        /*
            Method dump skipped, instructions count: 2407
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.ButtonManagementScreen$lambda$137$lambda$136$lambda$112(com.example.viewmodel.AdminUiState, androidx.activity.compose.ManagedActivityResultLauncher, androidx.activity.compose.ManagedActivityResultLauncher, androidx.activity.compose.ManagedActivityResultLauncher, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$112$lambda$111$lambda$110$lambda$105$lambda$104(ManagedActivityResultLauncher $swPackageLauncher) {
        $swPackageLauncher.launch(new String[]{"*/*"});
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$112$lambda$111$lambda$110$lambda$107$lambda$106(ManagedActivityResultLauncher $swIconLauncher) {
        $swIconLauncher.launch(new String[]{"image/*"});
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$112$lambda$111$lambda$110$lambda$109$lambda$108(ManagedActivityResultLauncher $swPreviewLauncher) {
        $swPreviewLauncher.launch(new String[]{"image/*"});
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$114$lambda$113(MutableState $formName$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formName$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$116$lambda$115(MutableState $formDesc$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formDesc$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$118$lambda$117(MutableState $formApkUrl$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formApkUrl$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$120$lambda$119(MutableState $formUrl$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formUrl$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$125$lambda$122$lambda$121(MutableState $formAuthor$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formAuthor$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$125$lambda$124$lambda$123(MutableState $formMode$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formMode$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$130$lambda$127$lambda$126(MutableState $formBadge$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formBadge$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$130$lambda$129$lambda$128(MutableState $formTags$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formTags$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$135$lambda$132$lambda$131(MutableState $formIconUrl$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formIconUrl$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$137$lambda$136$lambda$135$lambda$134$lambda$133(MutableState $formPreviewUrl$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formPreviewUrl$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$89(final Function1 $onShowToast, final Function12 $onSaveSoftware, final MutableState $formName$delegate, final MutableState $editingBtn$delegate, final MutableState $formUrl$delegate, final MutableState $formDesc$delegate, final MutableState $formAuthor$delegate, final MutableState $formBadge$delegate, final MutableState $formBadgeType$delegate, final MutableState $formTags$delegate, final MutableState $formApkUrl$delegate, final MutableState $formPreviewUrl$delegate, final MutableState $formIconUrl$delegate, final MutableState $formMode$delegate, final MutableState $dialogOpen$delegate, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C654@33000L61,633@32171L783,632@32133L1052:ButtonAndTextScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1197572139, $changed, -1, "com.example.ui.screens.ButtonManagementScreen.<anonymous> (ButtonAndTextScreens.kt:632)");
            }
            ButtonColors m1809buttonColorsro_MJ88 = ButtonDefaults.INSTANCE.m1809buttonColorsro_MJ88(ColorKt.getCinnabar(), ColorKt.getPaper(), 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 54, 12);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            ComposerKt.sourceInformationMarkerStart($composer, -184407174, "CC(remember):ButtonAndTextScreens.kt#9igjgp");
            boolean changed = $composer.changed($onShowToast) | $composer.changed($onSaveSoftware);
            Object rememberedValue = $composer.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda55
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return ButtonAndTextScreensKt.ButtonManagementScreen$lambda$89$lambda$88$lambda$87(Function1.this, $onSaveSoftware, $formName$delegate, $editingBtn$delegate, $formUrl$delegate, $formDesc$delegate, $formAuthor$delegate, $formBadge$delegate, $formBadgeType$delegate, $formTags$delegate, $formApkUrl$delegate, $formPreviewUrl$delegate, $formIconUrl$delegate, $formMode$delegate, $dialogOpen$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button((Function0) obj, null, false, m953RoundedCornerShape0680j_4, m1809buttonColorsro_MJ88, null, null, null, null, ComposableSingletons$ButtonAndTextScreensKt.INSTANCE.getLambda$1940972091$app(), $composer, 805306368, 486);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$89$lambda$88$lambda$87(Function1 $onShowToast, Function12 $onSaveSoftware, MutableState $formName$delegate, MutableState $editingBtn$delegate, MutableState $formUrl$delegate, MutableState $formDesc$delegate, MutableState $formAuthor$delegate, MutableState $formBadge$delegate, MutableState $formBadgeType$delegate, MutableState $formTags$delegate, MutableState $formApkUrl$delegate, MutableState $formPreviewUrl$delegate, MutableState $formIconUrl$delegate, MutableState $formMode$delegate, MutableState $dialogOpen$delegate) {
        if (StringsKt.trim((CharSequence) ButtonManagementScreen$lambda$15($formName$delegate)).toString().length() == 0) {
            $onShowToast.invoke("请填写软件名称");
            return Unit.INSTANCE;
        }
        $onSaveSoftware.invoke(ButtonManagementScreen$lambda$9($editingBtn$delegate), ButtonManagementScreen$lambda$15($formName$delegate), ButtonManagementScreen$lambda$18($formUrl$delegate), ButtonManagementScreen$lambda$21($formDesc$delegate), ButtonManagementScreen$lambda$24($formAuthor$delegate), ButtonManagementScreen$lambda$27($formBadge$delegate), ButtonManagementScreen$lambda$30($formBadgeType$delegate), ButtonManagementScreen$lambda$33($formTags$delegate), ButtonManagementScreen$lambda$36($formApkUrl$delegate), ButtonManagementScreen$lambda$39($formPreviewUrl$delegate), ButtonManagementScreen$lambda$42($formIconUrl$delegate), ButtonManagementScreen$lambda$45($formMode$delegate));
        ButtonManagementScreen$lambda$7($dialogOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$92(final MutableState $dialogOpen$delegate, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C662@33293L22,661@33247L265:ButtonAndTextScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-305526167, $changed, -1, "com.example.ui.screens.ButtonManagementScreen.<anonymous> (ButtonAndTextScreens.kt:661)");
            }
            BorderStroke m252BorderStrokecXLIe8U = BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist());
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            ComposerKt.sourceInformationMarkerStart($composer, -1552868289, "CC(remember):ButtonAndTextScreens.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda36
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return ButtonAndTextScreensKt.ButtonManagementScreen$lambda$92$lambda$91$lambda$90(MutableState.this);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.OutlinedButton((Function0) obj, null, false, m953RoundedCornerShape0680j_4, null, null, m252BorderStrokecXLIe8U, null, null, ComposableSingletons$ButtonAndTextScreensKt.INSTANCE.getLambda$996118455$app(), $composer, 806879238, 438);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$92$lambda$91$lambda$90(MutableState $dialogOpen$delegate) {
        ButtonManagementScreen$lambda$7($dialogOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$139$lambda$138(MutableState $deletingBtn$delegate) {
        $deletingBtn$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ButtonManagementScreen$lambda$142$lambda$141(MutableState $deletingBtn$delegate, Function1 $onDeleteSoftware) {
        ResourceButton ButtonManagementScreen$lambda$12 = ButtonManagementScreen$lambda$12($deletingBtn$delegate);
        if (ButtonManagementScreen$lambda$12 != null) {
            $onDeleteSoftware.invoke(ButtonManagementScreen$lambda$12);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$145$lambda$144(Uri uri, String str, Function2 function2) {
        Intrinsics.checkNotNullParameter(uri, "<unused var>");
        Intrinsics.checkNotNullParameter(str, "<unused var>");
        Intrinsics.checkNotNullParameter(function2, "<unused var>");
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:147:0x04b1  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x04b3  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x051f  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x053b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:166:0x056c  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x056f  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0573  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x0585  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0633  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0639  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x0710  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x071c  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0722  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0753  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x0769  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x08c2  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x09e0  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x0a0f  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0a12  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0a1b  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x0a3e  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0a4e  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0a6a  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x0a75  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x0ab3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void SkillManagementScreen(final com.example.viewmodel.AdminUiState r80, final kotlin.jvm.functions.Function13<? super com.example.model.SkillItem, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> r81, final kotlin.jvm.functions.Function1<? super com.example.model.SkillItem, kotlin.Unit> r82, kotlin.jvm.functions.Function3<? super android.net.Uri, ? super java.lang.String, ? super kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit>, kotlin.Unit> r83, final kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> r84, androidx.compose.runtime.Composer r85, final int r86, final int r87) {
        /*
            Method dump skipped, instructions count: 2771
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.SkillManagementScreen(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function13, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function3, kotlin.jvm.functions.Function1, androidx.compose.runtime.Composer, int, int):void");
    }

    private static final String SkillManagementScreen$lambda$147(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final boolean SkillManagementScreen$lambda$150(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void SkillManagementScreen$lambda$151(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final SkillItem SkillManagementScreen$lambda$153(MutableState<SkillItem> mutableState) {
        return mutableState.getValue();
    }

    private static final SkillItem SkillManagementScreen$lambda$156(MutableState<SkillItem> mutableState) {
        return mutableState.getValue();
    }

    private static final String SkillManagementScreen$lambda$159(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SkillManagementScreen$lambda$162(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SkillManagementScreen$lambda$165(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SkillManagementScreen$lambda$168(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SkillManagementScreen$lambda$171(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SkillManagementScreen$lambda$174(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SkillManagementScreen$lambda$177(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SkillManagementScreen$lambda$180(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SkillManagementScreen$lambda$183(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SkillManagementScreen$lambda$186(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SkillManagementScreen$lambda$189(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SkillManagementScreen$lambda$192(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$196$lambda$195(Function3 $onUploadFile, final MutableState $formUrl$delegate, final MutableState $formMode$delegate, final MutableState $formTitle$delegate, Uri uri) {
        if (uri != null) {
            $onUploadFile.invoke(uri, "dist/uploads", new Function2() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda35
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return ButtonAndTextScreensKt.SkillManagementScreen$lambda$196$lambda$195$lambda$194(MutableState.this, $formMode$delegate, $formTitle$delegate, (String) obj, (String) obj2);
                }
            });
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$196$lambda$195$lambda$194(MutableState $formUrl$delegate, MutableState $formMode$delegate, MutableState $formTitle$delegate, String downloadUrl, String fileName) {
        Intrinsics.checkNotNullParameter(downloadUrl, "downloadUrl");
        Intrinsics.checkNotNullParameter(fileName, "fileName");
        $formUrl$delegate.setValue(downloadUrl);
        $formMode$delegate.setValue("file");
        if (StringsKt.isBlank(SkillManagementScreen$lambda$159($formTitle$delegate))) {
            $formTitle$delegate.setValue(StringsKt.substringBeforeLast$default(fileName, '.', (String) null, 2, (Object) null));
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$199$lambda$198(Function3 $onUploadFile, final MutableState $formPreviewUrl$delegate, Uri uri) {
        if (uri != null) {
            $onUploadFile.invoke(uri, "dist/uploads", new Function2() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda71
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return ButtonAndTextScreensKt.SkillManagementScreen$lambda$199$lambda$198$lambda$197(MutableState.this, (String) obj, (String) obj2);
                }
            });
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$199$lambda$198$lambda$197(MutableState $formPreviewUrl$delegate, String downloadUrl, String str) {
        Intrinsics.checkNotNullParameter(downloadUrl, "downloadUrl");
        Intrinsics.checkNotNullParameter(str, "<unused var>");
        $formPreviewUrl$delegate.setValue(downloadUrl);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$202$lambda$201(Function3 $onUploadFile, final MutableState $formMediaUrl$delegate, Uri uri) {
        if (uri != null) {
            $onUploadFile.invoke(uri, "dist/uploads", new Function2() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda29
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return ButtonAndTextScreensKt.SkillManagementScreen$lambda$202$lambda$201$lambda$200(MutableState.this, (String) obj, (String) obj2);
                }
            });
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$202$lambda$201$lambda$200(MutableState $formMediaUrl$delegate, String downloadUrl, String str) {
        Intrinsics.checkNotNullParameter(downloadUrl, "downloadUrl");
        Intrinsics.checkNotNullParameter(str, "<unused var>");
        $formMediaUrl$delegate.setValue(downloadUrl);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$230$lambda$207(final MutableState $editingSkill$delegate, final MutableState $formTitle$delegate, final MutableState $formDesc$delegate, final MutableState $formPromptType$delegate, final MutableState $formPrompt$delegate, final MutableState $formUrl$delegate, final MutableState $formAuthor$delegate, final MutableState $formBadge$delegate, final MutableState $formTags$delegate, final MutableState $formPreviewUrl$delegate, final MutableState $formMediaUrl$delegate, final MutableState $formIconUrl$delegate, final MutableState $formMode$delegate, final MutableState $dialogOpen$delegate, RowScope PageHeader, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter(PageHeader, "$this$PageHeader");
        ComposerKt.sourceInformation($composer, "C783@37357L38,801@38100L61,785@37450L604,784@37412L1112:ButtonAndTextScreens.kt#2thlc2");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer.changed(PageHeader) ? 4 : 2;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1343160662, $dirty2, -1, "com.example.ui.screens.SkillManagementScreen.<anonymous>.<anonymous> (ButtonAndTextScreens.kt:783)");
            }
            SpacerKt.Spacer(RowScope.weight$default(PageHeader, Modifier.Companion, 1.0f, false, 2, null), $composer, 0);
            ButtonColors m1809buttonColorsro_MJ88 = ButtonDefaults.INSTANCE.m1809buttonColorsro_MJ88(ColorKt.getCinnabar(), ColorKt.getPaper(), 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 54, 12);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            Modifier testTag = TestTagKt.testTag(Modifier.Companion, "create_skill_btn");
            ComposerKt.sourceInformationMarkerStart($composer, 2007764070, "CC(remember):ButtonAndTextScreens.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda88
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return ButtonAndTextScreensKt.SkillManagementScreen$lambda$230$lambda$207$lambda$206$lambda$205(MutableState.this, $formTitle$delegate, $formDesc$delegate, $formPromptType$delegate, $formPrompt$delegate, $formUrl$delegate, $formAuthor$delegate, $formBadge$delegate, $formTags$delegate, $formPreviewUrl$delegate, $formMediaUrl$delegate, $formIconUrl$delegate, $formMode$delegate, $dialogOpen$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button((Function0) obj, testTag, false, m953RoundedCornerShape0680j_4, m1809buttonColorsro_MJ88, null, null, null, null, ComposableSingletons$ButtonAndTextScreensKt.INSTANCE.getLambda$1404321434$app(), $composer, 805306422, 484);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$230$lambda$207$lambda$206$lambda$205(MutableState $editingSkill$delegate, MutableState $formTitle$delegate, MutableState $formDesc$delegate, MutableState $formPromptType$delegate, MutableState $formPrompt$delegate, MutableState $formUrl$delegate, MutableState $formAuthor$delegate, MutableState $formBadge$delegate, MutableState $formTags$delegate, MutableState $formPreviewUrl$delegate, MutableState $formMediaUrl$delegate, MutableState $formIconUrl$delegate, MutableState $formMode$delegate, MutableState $dialogOpen$delegate) {
        $editingSkill$delegate.setValue(null);
        $formTitle$delegate.setValue("");
        $formDesc$delegate.setValue("");
        $formPromptType$delegate.setValue("skill");
        $formPrompt$delegate.setValue("");
        $formUrl$delegate.setValue("");
        $formAuthor$delegate.setValue("懒得找了");
        $formBadge$delegate.setValue("推荐");
        $formTags$delegate.setValue("");
        $formPreviewUrl$delegate.setValue("");
        $formMediaUrl$delegate.setValue("");
        $formIconUrl$delegate.setValue("");
        $formMode$delegate.setValue("file");
        SkillManagementScreen$lambda$151($dialogOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01d5  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01e1  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0329  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x033b  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0387  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x03aa  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0502 A[LOOP:0: B:64:0x04fc->B:66:0x0502, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x05f5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SkillManagementScreen$lambda$230$lambda$229(java.util.List r86, com.example.viewmodel.AdminUiState r87, final androidx.compose.runtime.MutableState r88, final android.content.Context r89, final kotlin.jvm.functions.Function1 r90, final androidx.compose.runtime.MutableState r91, final androidx.compose.runtime.MutableState r92, final androidx.compose.runtime.MutableState r93, final androidx.compose.runtime.MutableState r94, final androidx.compose.runtime.MutableState r95, final androidx.compose.runtime.MutableState r96, final androidx.compose.runtime.MutableState r97, final androidx.compose.runtime.MutableState r98, final androidx.compose.runtime.MutableState r99, final androidx.compose.runtime.MutableState r100, final androidx.compose.runtime.MutableState r101, final androidx.compose.runtime.MutableState r102, final androidx.compose.runtime.MutableState r103, final androidx.compose.runtime.MutableState r104, final androidx.compose.runtime.MutableState r105, androidx.compose.runtime.Composer r106, int r107) {
        /*
            Method dump skipped, instructions count: 1531
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.SkillManagementScreen$lambda$230$lambda$229(java.util.List, com.example.viewmodel.AdminUiState, androidx.compose.runtime.MutableState, android.content.Context, kotlin.jvm.functions.Function1, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$230$lambda$229$lambda$228$lambda$210$lambda$209$lambda$208(MutableState $keyword$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $keyword$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:106:0x08ea  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0978  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0984  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x098a  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x09bb  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x09cf  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0a2a  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0adc  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0b4a  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01db  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01ed  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0309  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0315  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x031b  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x034e  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0364 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x042d  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0485  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x04ad  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x04b9  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x051c  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x057c  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x058e  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0601  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x067b  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0687  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x068d  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x06c0  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x06d6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x072f  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x079e  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x07b5  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0813  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0841  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SkillManagementScreen$lambda$230$lambda$229$lambda$228$lambda$227$lambda$226$lambda$225(final com.example.model.SkillItem r115, final android.content.Context r116, final kotlin.jvm.functions.Function1 r117, final androidx.compose.runtime.MutableState r118, final androidx.compose.runtime.MutableState r119, final androidx.compose.runtime.MutableState r120, final androidx.compose.runtime.MutableState r121, final androidx.compose.runtime.MutableState r122, final androidx.compose.runtime.MutableState r123, final androidx.compose.runtime.MutableState r124, final androidx.compose.runtime.MutableState r125, final androidx.compose.runtime.MutableState r126, final androidx.compose.runtime.MutableState r127, final androidx.compose.runtime.MutableState r128, final androidx.compose.runtime.MutableState r129, final androidx.compose.runtime.MutableState r130, final androidx.compose.runtime.MutableState r131, final androidx.compose.runtime.MutableState r132, androidx.compose.runtime.Composer r133, int r134) {
        /*
            Method dump skipped, instructions count: 2896
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.SkillManagementScreen$lambda$230$lambda$229$lambda$228$lambda$227$lambda$226$lambda$225(com.example.model.SkillItem, android.content.Context, kotlin.jvm.functions.Function1, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$230$lambda$229$lambda$228$lambda$227$lambda$226$lambda$225$lambda$224$lambda$218$lambda$214$lambda$211(SkillItem $sk, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C902@43226L10,900@43069L517:ButtonAndTextScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1332478585, $changed, -1, "com.example.ui.screens.SkillManagementScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (ButtonAndTextScreens.kt:900)");
            }
            String badge = $sk.getBadge();
            TextStyle labelSmall = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall();
            TextKt.m2693Text4IGK_g(badge, PaddingKt.m671paddingVpY3zN4(Modifier.Companion, Dp.m6622constructorimpl(6), Dp.m6622constructorimpl(2)), ColorKt.getCinnabar(), 0L, (FontStyle) null, FontWeight.Companion.getSemiBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, labelSmall, $composer, 197040, 0, 65496);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0159  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x017a  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x017f  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01d3  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x01ef  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01f4  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0247  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SkillManagementScreen$lambda$230$lambda$229$lambda$228$lambda$227$lambda$226$lambda$225$lambda$224$lambda$218$lambda$214$lambda$213(com.example.model.SkillItem r51, androidx.compose.runtime.Composer r52, int r53) {
        /*
            Method dump skipped, instructions count: 589
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.SkillManagementScreen$lambda$230$lambda$229$lambda$228$lambda$227$lambda$226$lambda$225$lambda$224$lambda$218$lambda$214$lambda$213(com.example.model.SkillItem, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$230$lambda$229$lambda$228$lambda$227$lambda$226$lambda$225$lambda$224$lambda$218$lambda$217$lambda$216(SkillItem $sk, Context $context, Function1 $onShowToast) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse($sk.getUrl()));
            $context.startActivity(intent);
        } catch (Exception e) {
            $onShowToast.invoke("资源链接：" + $sk.getUrl());
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$230$lambda$229$lambda$228$lambda$227$lambda$226$lambda$225$lambda$224$lambda$223$lambda$220$lambda$219(SkillItem $sk, MutableState $editingSkill$delegate, MutableState $formTitle$delegate, MutableState $formDesc$delegate, MutableState $formPromptType$delegate, MutableState $formPrompt$delegate, MutableState $formUrl$delegate, MutableState $formAuthor$delegate, MutableState $formBadge$delegate, MutableState $formTags$delegate, MutableState $formPreviewUrl$delegate, MutableState $formMediaUrl$delegate, MutableState $formIconUrl$delegate, MutableState $formMode$delegate, MutableState $dialogOpen$delegate) {
        $editingSkill$delegate.setValue($sk);
        $formTitle$delegate.setValue($sk.getTitle());
        $formDesc$delegate.setValue($sk.getDesc());
        $formPromptType$delegate.setValue($sk.getPromptType());
        $formPrompt$delegate.setValue($sk.getPrompt());
        $formUrl$delegate.setValue($sk.getUrl());
        $formAuthor$delegate.setValue($sk.getAuthor());
        $formBadge$delegate.setValue($sk.getBadge());
        $formTags$delegate.setValue($sk.getTags());
        $formPreviewUrl$delegate.setValue($sk.getPreviewUrl());
        $formMediaUrl$delegate.setValue($sk.getMediaUrl());
        $formIconUrl$delegate.setValue($sk.getIconUrl());
        $formMode$delegate.setValue($sk.getMode());
        SkillManagementScreen$lambda$151($dialogOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$230$lambda$229$lambda$228$lambda$227$lambda$226$lambda$225$lambda$224$lambda$223$lambda$222$lambda$221(SkillItem $sk, MutableState $deletingSkill$delegate) {
        $deletingSkill$delegate.setValue($sk);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$232$lambda$231(MutableState $dialogOpen$delegate) {
        SkillManagementScreen$lambda$151($dialogOpen$delegate, false);
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
    public static final kotlin.Unit SkillManagementScreen$lambda$240(androidx.compose.runtime.MutableState r51, androidx.compose.runtime.Composer r52, int r53) {
        /*
            Method dump skipped, instructions count: 484
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.SkillManagementScreen$lambda$240(androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0a62  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0a78 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0b08  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0b1c  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0ba9  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0bb9  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0c97  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0ca3  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0ca9  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0cda  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0cf0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0d7f  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0d93  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0e1f  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0e31  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0eae  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x022e  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x023a  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0240  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0349  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0359  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x038b  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x038d  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x03eb  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x03fb  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x04d4  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x04e6  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0571  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0583  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x060f  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0621  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x06ad  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x06bf  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0795  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x07a1  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x07a7  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x07da  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x07f0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x087e  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0892  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0923  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0937  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0a1d  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0a29  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0a2f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SkillManagementScreen$lambda$281(final androidx.compose.runtime.MutableState r124, final com.example.viewmodel.AdminUiState r125, final androidx.activity.compose.ManagedActivityResultLauncher r126, final androidx.activity.compose.ManagedActivityResultLauncher r127, final androidx.activity.compose.ManagedActivityResultLauncher r128, final androidx.compose.runtime.MutableState r129, final androidx.compose.runtime.MutableState r130, final androidx.compose.runtime.MutableState r131, final androidx.compose.runtime.MutableState r132, final androidx.compose.runtime.MutableState r133, final androidx.compose.runtime.MutableState r134, final androidx.compose.runtime.MutableState r135, final androidx.compose.runtime.MutableState r136, final androidx.compose.runtime.MutableState r137, androidx.compose.runtime.Composer r138, int r139) {
        /*
            Method dump skipped, instructions count: 3764
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.SkillManagementScreen$lambda$281(androidx.compose.runtime.MutableState, com.example.viewmodel.AdminUiState, androidx.activity.compose.ManagedActivityResultLauncher, androidx.activity.compose.ManagedActivityResultLauncher, androidx.activity.compose.ManagedActivityResultLauncher, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$245$lambda$242$lambda$241(MutableState $formMode$delegate) {
        $formMode$delegate.setValue("file");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$245$lambda$244$lambda$243(MutableState $formMode$delegate) {
        $formMode$delegate.setValue("url");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x034d  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x03a8  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0436  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0442  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0448  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x055a  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0568  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x05f1  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x05fe A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x067c  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0689 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x06f2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SkillManagementScreen$lambda$281$lambda$280$lambda$256(com.example.viewmodel.AdminUiState r84, final androidx.activity.compose.ManagedActivityResultLauncher r85, final androidx.activity.compose.ManagedActivityResultLauncher r86, final androidx.activity.compose.ManagedActivityResultLauncher r87, androidx.compose.runtime.Composer r88, int r89) {
        /*
            Method dump skipped, instructions count: 1784
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.SkillManagementScreen$lambda$281$lambda$280$lambda$256(com.example.viewmodel.AdminUiState, androidx.activity.compose.ManagedActivityResultLauncher, androidx.activity.compose.ManagedActivityResultLauncher, androidx.activity.compose.ManagedActivityResultLauncher, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$256$lambda$255$lambda$254$lambda$249$lambda$248(ManagedActivityResultLauncher $skillFileLauncher) {
        $skillFileLauncher.launch(new String[]{"*/*"});
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$256$lambda$255$lambda$254$lambda$251$lambda$250(ManagedActivityResultLauncher $skillPreviewLauncher) {
        $skillPreviewLauncher.launch(new String[]{"image/*"});
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$256$lambda$255$lambda$254$lambda$253$lambda$252(ManagedActivityResultLauncher $skillMediaLauncher) {
        $skillMediaLauncher.launch(new String[]{"video/*", "image/*"});
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$258$lambda$257(MutableState $formTitle$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formTitle$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$260$lambda$259(MutableState $formDesc$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formDesc$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$262$lambda$261(MutableState $formPrompt$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formPrompt$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$264$lambda$263(MutableState $formUrl$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formUrl$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$269$lambda$266$lambda$265(MutableState $formBadge$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formBadge$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$269$lambda$268$lambda$267(MutableState $formTags$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formTags$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$274$lambda$271$lambda$270(MutableState $formAuthor$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formAuthor$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$274$lambda$273$lambda$272(MutableState $formMode$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formMode$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$279$lambda$276$lambda$275(MutableState $formPreviewUrl$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formPreviewUrl$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$281$lambda$280$lambda$279$lambda$278$lambda$277(MutableState $formMediaUrl$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $formMediaUrl$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$235(final Function1 $onShowToast, final Function13 $onSaveSkill, final MutableState $formTitle$delegate, final MutableState $editingSkill$delegate, final MutableState $formDesc$delegate, final MutableState $formPromptType$delegate, final MutableState $formPrompt$delegate, final MutableState $formUrl$delegate, final MutableState $formAuthor$delegate, final MutableState $formBadge$delegate, final MutableState $formTags$delegate, final MutableState $formPreviewUrl$delegate, final MutableState $formMediaUrl$delegate, final MutableState $formIconUrl$delegate, final MutableState $formMode$delegate, final MutableState $dialogOpen$delegate, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C1315@66139L61,1293@65261L832,1292@65223L1101:ButtonAndTextScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1097784609, $changed, -1, "com.example.ui.screens.SkillManagementScreen.<anonymous> (ButtonAndTextScreens.kt:1292)");
            }
            ButtonColors m1809buttonColorsro_MJ88 = ButtonDefaults.INSTANCE.m1809buttonColorsro_MJ88(ColorKt.getCinnabar(), ColorKt.getPaper(), 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 54, 12);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            ComposerKt.sourceInformationMarkerStart($composer, -1590942943, "CC(remember):ButtonAndTextScreens.kt#9igjgp");
            boolean changed = $composer.changed($onShowToast) | $composer.changed($onSaveSkill);
            Object rememberedValue = $composer.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda53
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return ButtonAndTextScreensKt.SkillManagementScreen$lambda$235$lambda$234$lambda$233(Function1.this, $onSaveSkill, $formTitle$delegate, $editingSkill$delegate, $formDesc$delegate, $formPromptType$delegate, $formPrompt$delegate, $formUrl$delegate, $formAuthor$delegate, $formBadge$delegate, $formTags$delegate, $formPreviewUrl$delegate, $formMediaUrl$delegate, $formIconUrl$delegate, $formMode$delegate, $dialogOpen$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button((Function0) obj, null, false, m953RoundedCornerShape0680j_4, m1809buttonColorsro_MJ88, null, null, null, null, ComposableSingletons$ButtonAndTextScreensKt.INSTANCE.getLambda$1537407249$app(), $composer, 805306368, 486);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$235$lambda$234$lambda$233(Function1 $onShowToast, Function13 $onSaveSkill, MutableState $formTitle$delegate, MutableState $editingSkill$delegate, MutableState $formDesc$delegate, MutableState $formPromptType$delegate, MutableState $formPrompt$delegate, MutableState $formUrl$delegate, MutableState $formAuthor$delegate, MutableState $formBadge$delegate, MutableState $formTags$delegate, MutableState $formPreviewUrl$delegate, MutableState $formMediaUrl$delegate, MutableState $formIconUrl$delegate, MutableState $formMode$delegate, MutableState $dialogOpen$delegate) {
        if (StringsKt.trim((CharSequence) SkillManagementScreen$lambda$159($formTitle$delegate)).toString().length() == 0) {
            $onShowToast.invoke("请填写 Skill 名称");
            return Unit.INSTANCE;
        }
        $onSaveSkill.invoke(SkillManagementScreen$lambda$153($editingSkill$delegate), SkillManagementScreen$lambda$159($formTitle$delegate), SkillManagementScreen$lambda$162($formDesc$delegate), SkillManagementScreen$lambda$165($formPromptType$delegate), SkillManagementScreen$lambda$168($formPrompt$delegate), SkillManagementScreen$lambda$171($formUrl$delegate), SkillManagementScreen$lambda$174($formAuthor$delegate), SkillManagementScreen$lambda$177($formBadge$delegate), SkillManagementScreen$lambda$180($formTags$delegate), SkillManagementScreen$lambda$183($formPreviewUrl$delegate), SkillManagementScreen$lambda$186($formMediaUrl$delegate), SkillManagementScreen$lambda$189($formIconUrl$delegate), SkillManagementScreen$lambda$192($formMode$delegate));
        SkillManagementScreen$lambda$151($dialogOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$238(final MutableState $dialogOpen$delegate, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C1323@66432L22,1322@66386L265:ButtonAndTextScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1187844899, $changed, -1, "com.example.ui.screens.SkillManagementScreen.<anonymous> (ButtonAndTextScreens.kt:1322)");
            }
            BorderStroke m252BorderStrokecXLIe8U = BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist());
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            ComposerKt.sourceInformationMarkerStart($composer, -942350727, "CC(remember):ButtonAndTextScreens.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda32
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return ButtonAndTextScreensKt.SkillManagementScreen$lambda$238$lambda$237$lambda$236(MutableState.this);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.OutlinedButton((Function0) obj, null, false, m953RoundedCornerShape0680j_4, null, null, m252BorderStrokecXLIe8U, null, null, ComposableSingletons$ButtonAndTextScreensKt.INSTANCE.m6967getLambda$1125471211$app(), $composer, 806879238, 438);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$238$lambda$237$lambda$236(MutableState $dialogOpen$delegate) {
        SkillManagementScreen$lambda$151($dialogOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$283$lambda$282(MutableState $deletingSkill$delegate) {
        $deletingSkill$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SkillManagementScreen$lambda$286$lambda$285(MutableState $deletingSkill$delegate, Function1 $onDeleteSkill) {
        SkillItem SkillManagementScreen$lambda$156 = SkillManagementScreen$lambda$156($deletingSkill$delegate);
        if (SkillManagementScreen$lambda$156 != null) {
            $onDeleteSkill.invoke(SkillManagementScreen$lambda$156);
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:110:0x052a  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x05eb  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0607  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0609  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0611  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0630  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x063c  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0651  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0654  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x065f  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x069f  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x02df A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x02dc  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x03b1  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x03bd  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x03c3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void TextManagementScreen(final com.example.viewmodel.AdminUiState r57, final kotlin.jvm.functions.Function3<? super java.lang.String, ? super java.lang.String, ? super java.lang.Boolean, kotlin.Unit> r58, final kotlin.jvm.functions.Function1<? super com.example.model.TextItem, kotlin.Unit> r59, final kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> r60, androidx.compose.runtime.Composer r61, final int r62) {
        /*
            Method dump skipped, instructions count: 1723
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.TextManagementScreen(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function3, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function1, androidx.compose.runtime.Composer, int):void");
    }

    private static final String TextManagementScreen$lambda$289(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final boolean TextManagementScreen$lambda$292(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void TextManagementScreen$lambda$293(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean TextManagementScreen$lambda$295(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void TextManagementScreen$lambda$296(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final String TextManagementScreen$lambda$298(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String TextManagementScreen$lambda$301(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final TextItem TextManagementScreen$lambda$304(MutableState<TextItem> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$328$lambda$310(final MutableState $isNewText$delegate, final MutableState $keyName$delegate, final MutableState $content$delegate, final MutableState $dialogOpen$delegate, RowScope PageHeader, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter(PageHeader, "$this$PageHeader");
        ComposerKt.sourceInformation($composer, "C1377@68279L38,1385@68598L61,1379@68372L180,1378@68334L683:ButtonAndTextScreens.kt#2thlc2");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer.changed(PageHeader) ? 4 : 2;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(413166368, $dirty2, -1, "com.example.ui.screens.TextManagementScreen.<anonymous>.<anonymous> (ButtonAndTextScreens.kt:1377)");
            }
            SpacerKt.Spacer(RowScope.weight$default(PageHeader, Modifier.Companion, 1.0f, false, 2, null), $composer, 0);
            ButtonColors m1809buttonColorsro_MJ88 = ButtonDefaults.INSTANCE.m1809buttonColorsro_MJ88(ColorKt.getCinnabar(), ColorKt.getPaper(), 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 54, 12);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            Modifier testTag = TestTagKt.testTag(Modifier.Companion, "create_text_btn");
            ComposerKt.sourceInformationMarkerStart($composer, 1915892148, "CC(remember):ButtonAndTextScreens.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda46
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return ButtonAndTextScreensKt.TextManagementScreen$lambda$328$lambda$310$lambda$309$lambda$308(MutableState.this, $keyName$delegate, $content$delegate, $dialogOpen$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button((Function0) obj, testTag, false, m953RoundedCornerShape0680j_4, m1809buttonColorsro_MJ88, null, null, null, null, ComposableSingletons$ButtonAndTextScreensKt.INSTANCE.getLambda$1993347856$app(), $composer, 805306422, 484);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$328$lambda$310$lambda$309$lambda$308(MutableState $isNewText$delegate, MutableState $keyName$delegate, MutableState $content$delegate, MutableState $dialogOpen$delegate) {
        TextManagementScreen$lambda$296($isNewText$delegate, true);
        $keyName$delegate.setValue("");
        $content$delegate.setValue("");
        TextManagementScreen$lambda$293($dialogOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01d2  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01e4  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0326  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0338  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0384  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x03a7  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x04ff A[LOOP:0: B:64:0x04f9->B:66:0x04ff, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x05ed  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit TextManagementScreen$lambda$328$lambda$327(java.util.List r85, com.example.viewmodel.AdminUiState r86, final androidx.compose.runtime.MutableState r87, final androidx.compose.runtime.MutableState r88, final androidx.compose.runtime.MutableState r89, final androidx.compose.runtime.MutableState r90, final androidx.compose.runtime.MutableState r91, final androidx.compose.runtime.MutableState r92, androidx.compose.runtime.Composer r93, int r94) {
        /*
            Method dump skipped, instructions count: 1523
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.TextManagementScreen$lambda$328$lambda$327(java.util.List, com.example.viewmodel.AdminUiState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$328$lambda$327$lambda$326$lambda$313$lambda$312$lambda$311(MutableState $keyword$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $keyword$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01ef  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01f5  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0315  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0321  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0327  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x035a  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0370 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0547  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0553  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0557  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0585  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x059b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x05f8  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0613  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x066b  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x06e1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit TextManagementScreen$lambda$328$lambda$327$lambda$326$lambda$325$lambda$324$lambda$323(final com.example.model.TextItem r110, final androidx.compose.runtime.MutableState r111, final androidx.compose.runtime.MutableState r112, final androidx.compose.runtime.MutableState r113, final androidx.compose.runtime.MutableState r114, final androidx.compose.runtime.MutableState r115, androidx.compose.runtime.Composer r116, int r117) {
        /*
            Method dump skipped, instructions count: 1767
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.TextManagementScreen$lambda$328$lambda$327$lambda$326$lambda$325$lambda$324$lambda$323(com.example.model.TextItem, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$328$lambda$327$lambda$326$lambda$325$lambda$324$lambda$323$lambda$322$lambda$316$lambda$315$lambda$314(TextItem $item, Composer $composer, int $changed) {
        long m4157copywmQWz5c;
        ComposerKt.sourceInformation($composer, "C1479@73183L10,1477@73029L431:ButtonAndTextScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(786765158, $changed, -1, "com.example.ui.screens.TextManagementScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (ButtonAndTextScreens.kt:1477)");
            }
            String category = $item.getCategory();
            TextStyle labelSmall = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall();
            m4157copywmQWz5c = Color.m4157copywmQWz5c(r3, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r3) : 0.7f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r3) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r3) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
            TextKt.m2693Text4IGK_g(category, PaddingKt.m671paddingVpY3zN4(Modifier.Companion, Dp.m6622constructorimpl(6), Dp.m6622constructorimpl(2)), m4157copywmQWz5c, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, labelSmall, $composer, 432, 0, 65528);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$328$lambda$327$lambda$326$lambda$325$lambda$324$lambda$323$lambda$322$lambda$321$lambda$318$lambda$317(TextItem $item, MutableState $isNewText$delegate, MutableState $keyName$delegate, MutableState $content$delegate, MutableState $dialogOpen$delegate) {
        TextManagementScreen$lambda$296($isNewText$delegate, false);
        $keyName$delegate.setValue($item.getKey());
        $content$delegate.setValue($item.getContent());
        TextManagementScreen$lambda$293($dialogOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$328$lambda$327$lambda$326$lambda$325$lambda$324$lambda$323$lambda$322$lambda$321$lambda$320$lambda$319(TextItem $item, MutableState $deletingItem$delegate) {
        $deletingItem$delegate.setValue($item);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$330$lambda$329(MutableState $dialogOpen$delegate) {
        TextManagementScreen$lambda$293($dialogOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x013d  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01fb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit TextManagementScreen$lambda$338(androidx.compose.runtime.MutableState r51, androidx.compose.runtime.MutableState r52, androidx.compose.runtime.Composer r53, int r54) {
        /*
            Method dump skipped, instructions count: 513
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.TextManagementScreen$lambda$338(androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x017e  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0196  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0227  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0239  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x02a7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit TextManagementScreen$lambda$344(final androidx.compose.runtime.MutableState r55, final androidx.compose.runtime.MutableState r56, final androidx.compose.runtime.MutableState r57, androidx.compose.runtime.Composer r58, int r59) {
        /*
            Method dump skipped, instructions count: 685
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.ButtonAndTextScreensKt.TextManagementScreen$lambda$344(androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$344$lambda$343$lambda$340$lambda$339(MutableState $isNewText$delegate, MutableState $keyName$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if (TextManagementScreen$lambda$295($isNewText$delegate)) {
            $keyName$delegate.setValue(it);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$344$lambda$343$lambda$342$lambda$341(MutableState $content$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $content$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$333(final Function1 $onShowToast, final Function3 $onSaveText, final MutableState $keyName$delegate, final MutableState $content$delegate, final MutableState $isNewText$delegate, final MutableState $dialogOpen$delegate, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C1585@78265L61,1577@77911L308,1576@77873L577:ButtonAndTextScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1591149591, $changed, -1, "com.example.ui.screens.TextManagementScreen.<anonymous> (ButtonAndTextScreens.kt:1576)");
            }
            ButtonColors m1809buttonColorsro_MJ88 = ButtonDefaults.INSTANCE.m1809buttonColorsro_MJ88(ColorKt.getCinnabar(), ColorKt.getPaper(), 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 54, 12);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            ComposerKt.sourceInformationMarkerStart($composer, 86401675, "CC(remember):ButtonAndTextScreens.kt#9igjgp");
            boolean changed = $composer.changed($onShowToast) | $composer.changed($onSaveText);
            Object rememberedValue = $composer.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda45
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return ButtonAndTextScreensKt.TextManagementScreen$lambda$333$lambda$332$lambda$331(Function1.this, $onSaveText, $keyName$delegate, $content$delegate, $isNewText$delegate, $dialogOpen$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button((Function0) obj, null, false, m953RoundedCornerShape0680j_4, m1809buttonColorsro_MJ88, null, null, null, null, ComposableSingletons$ButtonAndTextScreensKt.INSTANCE.m6975getLambda$1864267769$app(), $composer, 805306368, 486);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$333$lambda$332$lambda$331(Function1 $onShowToast, Function3 $onSaveText, MutableState $keyName$delegate, MutableState $content$delegate, MutableState $isNewText$delegate, MutableState $dialogOpen$delegate) {
        if (StringsKt.trim((CharSequence) TextManagementScreen$lambda$298($keyName$delegate)).toString().length() == 0) {
            $onShowToast.invoke("请填写键名（key）");
            return Unit.INSTANCE;
        }
        $onSaveText.invoke(TextManagementScreen$lambda$298($keyName$delegate), TextManagementScreen$lambda$301($content$delegate), Boolean.valueOf(TextManagementScreen$lambda$295($isNewText$delegate)));
        TextManagementScreen$lambda$293($dialogOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$336(final MutableState $dialogOpen$delegate, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C1593@78558L22,1592@78512L265:ButtonAndTextScreens.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1935977753, $changed, -1, "com.example.ui.screens.TextManagementScreen.<anonymous> (ButtonAndTextScreens.kt:1592)");
            }
            BorderStroke m252BorderStrokecXLIe8U = BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist());
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            ComposerKt.sourceInformationMarkerStart($composer, 753783535, "CC(remember):ButtonAndTextScreens.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.ButtonAndTextScreensKt$$ExternalSyntheticLambda47
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return ButtonAndTextScreensKt.TextManagementScreen$lambda$336$lambda$335$lambda$334(MutableState.this);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.OutlinedButton((Function0) obj, null, false, m953RoundedCornerShape0680j_4, null, null, m252BorderStrokecXLIe8U, null, null, ComposableSingletons$ButtonAndTextScreensKt.INSTANCE.getLambda$1241161995$app(), $composer, 806879238, 438);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$336$lambda$335$lambda$334(MutableState $dialogOpen$delegate) {
        TextManagementScreen$lambda$293($dialogOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$346$lambda$345(MutableState $deletingItem$delegate) {
        $deletingItem$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TextManagementScreen$lambda$349$lambda$348(MutableState $deletingItem$delegate, Function1 $onDeleteText) {
        TextItem TextManagementScreen$lambda$304 = TextManagementScreen$lambda$304($deletingItem$delegate);
        if (TextManagementScreen$lambda$304 != null) {
            $onDeleteText.invoke(TextManagementScreen$lambda$304);
        }
        return Unit.INSTANCE;
    }
}
