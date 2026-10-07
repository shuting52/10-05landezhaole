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
import androidx.compose.material.icons.automirrored.filled.SendKt;
import androidx.compose.material3.ButtonColors;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.IconKt;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.RecomposeScopeImplKt;
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
import com.example.model.FullSettingsConfig;
import com.example.ui.theme.ColorKt;
import com.example.viewmodel.AdminUiState;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
/* compiled from: SettingsScreen.kt */
@Metadata(d1 = {"\u0000F\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0019\u001aÁ\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032K\u0010\u0004\u001aG\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\n\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\r2!\u0010\u0010\u001a\u001d\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u00010\u001126\u0010\u0013\u001a2\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u00142\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00010\u001126\u0010\u001a\u001a2\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u001b\u0012\u0013\u0012\u00110\u001c¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u001d\u0012\u0004\u0012\u00020\u00010\u0014H\u0007¢\u0006\u0002\u0010\u001e\u001a9\u0010\u001f\u001a\u00020\u00012\u0006\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u001c2\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00010\u0011H\u0003¢\u0006\u0002\u0010$¨\u0006%²\u0006\n\u0010&\u001a\u00020\u001cX\u008a\u008e\u0002²\u0006\n\u0010'\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010\u0015\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010\u0016\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010(\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010)\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010*\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010+\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010,\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010-\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010.\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u0010/\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u00100\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u00101\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u00102\u001a\u00020\u001cX\u008a\u008e\u0002²\u0006\n\u00103\u001a\u00020\u0006X\u008a\u008e\u0002²\u0006\n\u00104\u001a\u00020\u001cX\u008a\u008e\u0002²\u0006\n\u00105\u001a\u00020\u0006X\u008a\u008e\u0002"}, d2 = {"SettingsScreen", "", "uiState", "Lcom/example/viewmodel/AdminUiState;", "onUpdateGithubInputs", "Lkotlin/Function3;", "", "Lkotlin/ParameterName;", HintConstants.AUTOFILL_HINT_NAME, "token", "owner", "repo", "onConnectGithub", "Lkotlin/Function0;", "onRefreshAdmin", "onApplyToDevice", "onPublishRelease", "Lkotlin/Function1;", "changeDesc", "onUpdateBasicSettingsInputs", "Lkotlin/Function2;", "appName", "slogan", "onSaveBasicSettings", "onSaveFullSettings", "Lcom/example/model/FullSettingsConfig;", "onToggleNotification", "key", "", "enabled", "(Lcom/example/viewmodel/AdminUiState;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;Landroidx/compose/runtime/Composer;I)V", "SettingToggleRow", "title", "description", "checked", "onCheckedChange", "(Ljava/lang/String;Ljava/lang/String;ZLkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "app", "confirmReleaseOpen", "releaseNote", "aboutText", "logoUrl", "packageName", "officialWebsite", "feedbackEmail", "qqGroupUin", "qqGroupUrl", "contactQQ", "contactWechat", "contactAlipay", "securityEnabled", "securityExpectedSha", "serverShutdownEnabled", "serverShutdownNotice"}, k = 2, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class SettingsScreenKt {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingToggleRow$lambda$159(String str, String str2, boolean z, Function1 function1, int i, Composer composer, int i2) {
        SettingToggleRow(str, str2, z, function1, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$156(AdminUiState adminUiState, Function3 function3, Function0 function0, Function0 function02, Function0 function03, Function1 function1, Function2 function2, Function0 function04, Function1 function12, Function2 function22, int i, Composer composer, int i2) {
        SettingsScreen(adminUiState, function3, function0, function02, function03, function1, function2, function04, function12, function22, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x0220  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0231  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0265  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0276 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x02a9  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x02ba  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x02ed  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x02fe  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0331  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0342  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0375  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0386  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x03b9  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x03ca  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x03fe  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x040f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:164:0x0443  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x0454 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0489  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x0499 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:180:0x04ce  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x04de A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0517  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x0527 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:196:0x055c  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x056c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:204:0x05a5  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x05b5  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x05ec  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x062f  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x0654  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0734  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0740  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0744  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x0772  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x0788  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x09b5  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0a70  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x0a80  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void SettingsScreen(final com.example.viewmodel.AdminUiState r83, final kotlin.jvm.functions.Function3<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> r84, final kotlin.jvm.functions.Function0<kotlin.Unit> r85, final kotlin.jvm.functions.Function0<kotlin.Unit> r86, final kotlin.jvm.functions.Function0<kotlin.Unit> r87, final kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> r88, final kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> r89, final kotlin.jvm.functions.Function0<kotlin.Unit> r90, final kotlin.jvm.functions.Function1<? super com.example.model.FullSettingsConfig, kotlin.Unit> r91, final kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.Boolean, kotlin.Unit> r92, androidx.compose.runtime.Composer r93, final int r94) {
        /*
            Method dump skipped, instructions count: 2724
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.SettingsScreenKt.SettingsScreen(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function3, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function2, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function2, androidx.compose.runtime.Composer, int):void");
    }

    private static final boolean SettingsScreen$lambda$1(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void SettingsScreen$lambda$2(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final String SettingsScreen$lambda$4(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SettingsScreen$lambda$7(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SettingsScreen$lambda$10(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SettingsScreen$lambda$13(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SettingsScreen$lambda$16(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SettingsScreen$lambda$19(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SettingsScreen$lambda$22(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SettingsScreen$lambda$25(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SettingsScreen$lambda$28(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SettingsScreen$lambda$31(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SettingsScreen$lambda$34(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SettingsScreen$lambda$37(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SettingsScreen$lambda$40(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final boolean SettingsScreen$lambda$43(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void SettingsScreen$lambda$44(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final String SettingsScreen$lambda$46(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final boolean SettingsScreen$lambda$49(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void SettingsScreen$lambda$50(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final String SettingsScreen$lambda$52(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0820  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x08b8  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x08c6  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0991  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x09dc  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0a19  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01cf  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01db  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01e1  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0300  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x030c  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0312  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0345  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x035b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x053d  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0549  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x054f  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0582  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0598  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0635  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0643 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0725  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0731  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0737  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0768  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x077e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0810  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsScreen$lambda$141$lambda$72(final com.example.viewmodel.AdminUiState r115, final kotlin.jvm.functions.Function3 r116, final kotlin.jvm.functions.Function0 r117, final kotlin.jvm.functions.Function0 r118, androidx.compose.runtime.Composer r119, int r120) {
        /*
            Method dump skipped, instructions count: 2591
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.SettingsScreenKt.SettingsScreen$lambda$141$lambda$72(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function3, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$72$lambda$71$lambda$70$lambda$58$lambda$57(Function3 $onUpdateGithubInputs, AdminUiState $uiState, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $onUpdateGithubInputs.invoke(it, $uiState.getGithubOwner(), $uiState.getGithubRepo());
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$72$lambda$71$lambda$70$lambda$63$lambda$60$lambda$59(Function3 $onUpdateGithubInputs, AdminUiState $uiState, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $onUpdateGithubInputs.invoke($uiState.getGithubToken(), it, $uiState.getGithubRepo());
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$72$lambda$71$lambda$70$lambda$63$lambda$62$lambda$61(Function3 $onUpdateGithubInputs, AdminUiState $uiState, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $onUpdateGithubInputs.invoke($uiState.getGithubToken(), $uiState.getGithubOwner(), it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01f7  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01fd  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x029b  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x02f7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x03a8  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x03b4  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x03ba  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x03ed  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0403 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x05a8  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x05b4  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x05ba  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x05eb  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0601  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0764  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsScreen$lambda$141$lambda$72$lambda$71$lambda$70$lambda$69(final com.example.viewmodel.AdminUiState r106, kotlin.jvm.functions.Function0 r107, kotlin.jvm.functions.Function0 r108, androidx.compose.runtime.Composer r109, int r110) {
        /*
            Method dump skipped, instructions count: 1898
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.SettingsScreenKt.SettingsScreen$lambda$141$lambda$72$lambda$71$lambda$70$lambda$69(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$72$lambda$71$lambda$70$lambda$69$lambda$68$lambda$67$lambda$66(AdminUiState $uiState, RowScope Button, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(Button, "$this$Button");
        ComposerKt.sourceInformation($composer, "C247@12143L10,245@11981L222:SettingsScreen.kt#2thlc2");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-785742283, $changed, -1, "com.example.ui.screens.SettingsScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (SettingsScreen.kt:245)");
            }
            TextKt.m2693Text4IGK_g($uiState.isConnecting() ? "连接中…" : "保存并连接", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelMedium(), $composer, 0, 0, 65534);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01ca  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01d6  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x02fc  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0308  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x030e  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0341  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0357 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x053b  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0547  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x054d  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0580  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0596  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x06bc  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x06c8  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x06ce  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x06ff  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0715  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0851  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0863  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0927  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsScreen$lambda$141$lambda$86(final com.example.viewmodel.AdminUiState r114, kotlin.jvm.functions.Function0 r115, final androidx.compose.runtime.MutableState r116, androidx.compose.runtime.Composer r117, int r118) {
        /*
            Method dump skipped, instructions count: 2349
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.SettingsScreenKt.SettingsScreen$lambda$141$lambda$86(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function0, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01ec  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x03a9  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x03b5  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x03bb  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x03ec  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0402 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x04ac  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x04c2  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0526  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x056f  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0595  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsScreen$lambda$141$lambda$86$lambda$85$lambda$84$lambda$79(com.example.viewmodel.AdminUiState r87, androidx.compose.runtime.Composer r88, int r89) {
        /*
            Method dump skipped, instructions count: 1435
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.SettingsScreenKt.SettingsScreen$lambda$141$lambda$86$lambda$85$lambda$84$lambda$79(com.example.viewmodel.AdminUiState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$86$lambda$85$lambda$84$lambda$79$lambda$78$lambda$77$lambda$76(boolean $isRelease, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C358@17109L10,356@16957L479:SettingsScreen.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-7813055, $changed, -1, "com.example.ui.screens.SettingsScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (SettingsScreen.kt:356)");
            }
            String str = $isRelease ? "发布模式" : "内容同步模式";
            TextStyle labelSmall = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall();
            TextKt.m2693Text4IGK_g(str, PaddingKt.m671paddingVpY3zN4(Modifier.Companion, Dp.m6622constructorimpl(10), Dp.m6622constructorimpl(4)), $isRelease ? ColorKt.getGoldDark() : ColorKt.getJadeGreen(), 0L, (FontStyle) null, FontWeight.Companion.getSemiBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, labelSmall, $composer, 196656, 0, 65496);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$86$lambda$85$lambda$84$lambda$83$lambda$80(AdminUiState $uiState, RowScope Button, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(Button, "$this$Button");
        ComposerKt.sourceInformation($composer, "C392@18730L236,397@18995L39,398@19063L55:SettingsScreen.kt#2thlc2");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-787515482, $changed, -1, "com.example.ui.screens.SettingsScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (SettingsScreen.kt:392)");
            }
            IconKt.m2150Iconww6aTOc(SendKt.getSend(Icons.AutoMirrored.Filled.INSTANCE), (String) null, SizeKt.m715size3ABfNKs(Modifier.Companion, Dp.m6622constructorimpl(16)), 0L, $composer, 432, 8);
            SpacerKt.Spacer(SizeKt.m720width3ABfNKs(Modifier.Companion, Dp.m6622constructorimpl(6)), $composer, 6);
            TextKt.m2693Text4IGK_g($uiState.isPublishing() ? "推送中…" : "✅ 应用并实时同步", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$86$lambda$85$lambda$84$lambda$83$lambda$82$lambda$81(MutableState $confirmReleaseOpen$delegate) {
        SettingsScreen$lambda$2($confirmReleaseOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x07ec A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0873  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0880  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0957  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0963  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0969  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x099c  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x09b2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0a43  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0a53 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0aeb  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0afb  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0b9f  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0bad A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0c38  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0c46 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:160:0x0cd1  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x0cdf A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:168:0x0d6a  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0d78 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:176:0x0e03  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0e11 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:184:0x0eee  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x0efa  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0f00  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0f33  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x0f49 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:199:0x1025  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x1031  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x1037  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x106a  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x1080 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:214:0x1170  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x117e  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x1227  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x1235 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:230:0x1311  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x131d  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x1323  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x1356  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x136c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:245:0x1443  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x144f  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x1455  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x1486  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x149c  */
    /* JADX WARN: Removed duplicated region for block: B:260:0x1589  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x1636  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x1748  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x1761  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x17f2  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01ea  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01f0  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x03b9  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x03c5  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x03cb  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x03fe  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0414 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x04ea  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x04f6  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x04fc  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x052f  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0545 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x05e6  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x05f6  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x068a  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x069a  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x074a  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0757 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x07df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsScreen$lambda$141$lambda$130(final androidx.compose.runtime.MutableState r167, final kotlin.jvm.functions.Function2 r168, final androidx.compose.runtime.MutableState r169, final androidx.compose.runtime.MutableState r170, final androidx.compose.runtime.MutableState r171, final androidx.compose.runtime.MutableState r172, final androidx.compose.runtime.MutableState r173, final androidx.compose.runtime.MutableState r174, final androidx.compose.runtime.MutableState r175, final androidx.compose.runtime.MutableState r176, final androidx.compose.runtime.MutableState r177, final androidx.compose.runtime.MutableState r178, final kotlin.jvm.functions.Function1 r179, final androidx.compose.runtime.MutableState r180, final androidx.compose.runtime.MutableState r181, final androidx.compose.runtime.MutableState r182, final androidx.compose.runtime.MutableState r183, final androidx.compose.runtime.MutableState r184, androidx.compose.runtime.Composer r185, int r186) {
        /*
            Method dump skipped, instructions count: 6136
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.SettingsScreenKt.SettingsScreen$lambda$141$lambda$130(androidx.compose.runtime.MutableState, kotlin.jvm.functions.Function2, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, kotlin.jvm.functions.Function1, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$92$lambda$89$lambda$88(Function2 $onUpdateBasicSettingsInputs, MutableState $appName$delegate, MutableState $slogan$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $appName$delegate.setValue(it);
        $onUpdateBasicSettingsInputs.invoke(it, SettingsScreen$lambda$10($slogan$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$92$lambda$91$lambda$90(MutableState $packageName$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $packageName$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$94$lambda$93(Function2 $onUpdateBasicSettingsInputs, MutableState $slogan$delegate, MutableState $appName$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $slogan$delegate.setValue(it);
        $onUpdateBasicSettingsInputs.invoke(SettingsScreen$lambda$7($appName$delegate), it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$96$lambda$95(MutableState $aboutText$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $aboutText$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$98$lambda$97(MutableState $logoUrl$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $logoUrl$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$103$lambda$100$lambda$99(MutableState $qqGroupUin$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $qqGroupUin$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$103$lambda$102$lambda$101(MutableState $feedbackEmail$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $feedbackEmail$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$105$lambda$104(MutableState $qqGroupUrl$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $qqGroupUrl$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$107$lambda$106(MutableState $officialWebsite$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $officialWebsite$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$109$lambda$108(MutableState $contactQQ$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $contactQQ$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$111$lambda$110(MutableState $contactWechat$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $contactWechat$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$113$lambda$112(MutableState $contactAlipay$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $contactAlipay$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$117$lambda$116$lambda$115(MutableState $securityEnabled$delegate, boolean it) {
        SettingsScreen$lambda$44($securityEnabled$delegate, it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$119$lambda$118(MutableState $securityExpectedSha$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $securityExpectedSha$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$123$lambda$122$lambda$121(MutableState $serverShutdownEnabled$delegate, boolean it) {
        SettingsScreen$lambda$50($serverShutdownEnabled$delegate, it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$125$lambda$124(MutableState $serverShutdownNotice$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $serverShutdownNotice$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$130$lambda$129$lambda$128$lambda$127$lambda$126(Function1 $onSaveFullSettings, MutableState $appName$delegate, MutableState $slogan$delegate, MutableState $aboutText$delegate, MutableState $contactQQ$delegate, MutableState $contactWechat$delegate, MutableState $contactAlipay$delegate, MutableState $qqGroupUrl$delegate, MutableState $qqGroupUin$delegate, MutableState $officialWebsite$delegate, MutableState $feedbackEmail$delegate, MutableState $logoUrl$delegate, MutableState $packageName$delegate, MutableState $securityEnabled$delegate, MutableState $securityExpectedSha$delegate, MutableState $serverShutdownEnabled$delegate, MutableState $serverShutdownNotice$delegate) {
        $onSaveFullSettings.invoke(new FullSettingsConfig(SettingsScreen$lambda$7($appName$delegate), SettingsScreen$lambda$10($slogan$delegate), SettingsScreen$lambda$13($aboutText$delegate), SettingsScreen$lambda$34($contactQQ$delegate), SettingsScreen$lambda$37($contactWechat$delegate), SettingsScreen$lambda$40($contactAlipay$delegate), SettingsScreen$lambda$31($qqGroupUrl$delegate), SettingsScreen$lambda$28($qqGroupUin$delegate), SettingsScreen$lambda$22($officialWebsite$delegate), SettingsScreen$lambda$25($feedbackEmail$delegate), SettingsScreen$lambda$16($logoUrl$delegate), SettingsScreen$lambda$19($packageName$delegate), SettingsScreen$lambda$43($securityEnabled$delegate), SettingsScreen$lambda$46($securityExpectedSha$delegate), SettingsScreen$lambda$49($serverShutdownEnabled$delegate), SettingsScreen$lambda$52($serverShutdownNotice$delegate)));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01c5  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01d1  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01d7  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0385  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0391  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0397  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x03c8  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x03de  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x044b  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x045b  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x04d1  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x04de A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x054c  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0559 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x05b0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsScreen$lambda$141$lambda$140(com.example.viewmodel.AdminUiState r86, final kotlin.jvm.functions.Function2 r87, androidx.compose.runtime.Composer r88, int r89) {
        /*
            Method dump skipped, instructions count: 1462
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.SettingsScreenKt.SettingsScreen$lambda$141$lambda$140(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function2, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$140$lambda$139$lambda$138$lambda$133$lambda$132(Function2 $onToggleNotification, boolean it) {
        $onToggleNotification.invoke("review", Boolean.valueOf(it));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$140$lambda$139$lambda$138$lambda$135$lambda$134(Function2 $onToggleNotification, boolean it) {
        $onToggleNotification.invoke("download", Boolean.valueOf(it));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$141$lambda$140$lambda$139$lambda$138$lambda$137$lambda$136(Function2 $onToggleNotification, boolean it) {
        $onToggleNotification.invoke("weekly", Boolean.valueOf(it));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$143$lambda$142(MutableState $confirmReleaseOpen$delegate) {
        SettingsScreen$lambda$2($confirmReleaseOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$151(String $nextVersionName, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C754@35223L10,752@35116L184:SettingsScreen.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1062955640, $changed, -1, "com.example.ui.screens.SettingsScreen.<anonymous> (SettingsScreen.kt:752)");
            }
            TextKt.m2693Text4IGK_g("⚠️ 确认发布新版本 v" + $nextVersionName, (Modifier) null, ColorKt.getInkBlack(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getTitleLarge(), $composer, 384, 0, 65530);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01fe  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0210  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0281  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsScreen$lambda$155(java.lang.String r54, com.example.viewmodel.AdminUiState r55, final androidx.compose.runtime.MutableState r56, androidx.compose.runtime.Composer r57, int r58) {
        /*
            Method dump skipped, instructions count: 647
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.SettingsScreenKt.SettingsScreen$lambda$155(java.lang.String, com.example.viewmodel.AdminUiState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$155$lambda$154$lambda$153$lambda$152(MutableState $releaseNote$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $releaseNote$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$147(final Function1 $onPublishRelease, final MutableState $releaseNote$delegate, final MutableState $confirmReleaseOpen$delegate, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C783@36582L61,778@36343L193,777@36305L544:SettingsScreen.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1208933252, $changed, -1, "com.example.ui.screens.SettingsScreen.<anonymous> (SettingsScreen.kt:777)");
            }
            ButtonColors m1809buttonColorsro_MJ88 = ButtonDefaults.INSTANCE.m1809buttonColorsro_MJ88(ColorKt.getCinnabar(), ColorKt.getPaper(), 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 54, 12);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            Modifier testTag = TestTagKt.testTag(Modifier.Companion, "confirm_publish_release_btn");
            ComposerKt.sourceInformationMarkerStart($composer, 2143687237, "CC(remember):SettingsScreen.kt#9igjgp");
            boolean changed = $composer.changed($onPublishRelease);
            Object rememberedValue = $composer.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.SettingsScreenKt$$ExternalSyntheticLambda24
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return SettingsScreenKt.SettingsScreen$lambda$147$lambda$146$lambda$145(Function1.this, $releaseNote$delegate, $confirmReleaseOpen$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button((Function0) obj, testTag, false, m953RoundedCornerShape0680j_4, m1809buttonColorsro_MJ88, null, null, null, null, ComposableSingletons$SettingsScreenKt.INSTANCE.getLambda$2028453748$app(), $composer, 805306416, 484);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$147$lambda$146$lambda$145(Function1 $onPublishRelease, MutableState $releaseNote$delegate, MutableState $confirmReleaseOpen$delegate) {
        String SettingsScreen$lambda$4 = SettingsScreen$lambda$4($releaseNote$delegate);
        if (StringsKt.isBlank(SettingsScreen$lambda$4)) {
            SettingsScreen$lambda$4 = "常规内容与体验更新";
        }
        $onPublishRelease.invoke(SettingsScreen$lambda$4);
        $releaseNote$delegate.setValue("");
        SettingsScreen$lambda$2($confirmReleaseOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$150(final MutableState $confirmReleaseOpen$delegate, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C792@36957L30,791@36911L273:SettingsScreen.kt#2thlc2");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(72988806, $changed, -1, "com.example.ui.screens.SettingsScreen.<anonymous> (SettingsScreen.kt:791)");
            }
            BorderStroke m252BorderStrokecXLIe8U = BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist());
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12));
            ComposerKt.sourceInformationMarkerStart($composer, 1422767908, "CC(remember):SettingsScreen.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.screens.SettingsScreenKt$$ExternalSyntheticLambda18
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return SettingsScreenKt.SettingsScreen$lambda$150$lambda$149$lambda$148(MutableState.this);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.OutlinedButton((Function0) obj, null, false, m953RoundedCornerShape0680j_4, null, null, m252BorderStrokecXLIe8U, null, null, ComposableSingletons$SettingsScreenKt.INSTANCE.m7002getLambda$1439612808$app(), $composer, 806879238, 438);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsScreen$lambda$150$lambda$149$lambda$148(MutableState $confirmReleaseOpen$delegate) {
        SettingsScreen$lambda$2($confirmReleaseOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:74:0x0428  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static final void SettingToggleRow(final java.lang.String r89, final java.lang.String r90, final boolean r91, final kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> r92, androidx.compose.runtime.Composer r93, final int r94) {
        /*
            Method dump skipped, instructions count: 1090
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.SettingsScreenKt.SettingToggleRow(java.lang.String, java.lang.String, boolean, kotlin.jvm.functions.Function1, androidx.compose.runtime.Composer, int):void");
    }
}
