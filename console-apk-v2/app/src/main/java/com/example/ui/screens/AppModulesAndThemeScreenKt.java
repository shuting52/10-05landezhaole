package com.example.ui.screens;

import androidx.autofill.HintConstants;
import androidx.compose.foundation.BorderStrokeKt;
import androidx.compose.foundation.interaction.MutableInteractionSource;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.shape.RoundedCornerShape;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.foundation.text.KeyboardActions;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.OutlinedTextFieldKt;
import androidx.compose.material3.SurfaceKt;
import androidx.compose.material3.TextFieldColors;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.MutableIntState;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.ScopeUpdateScope;
import androidx.compose.runtime.SnapshotStateKt__SnapshotStateKt;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Shape;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.VisualTransformation;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.unit.Dp;
import com.example.model.ConsoleConfig;
import com.example.model.IpMonitorConfig;
import com.example.model.MarqueeConfig;
import com.example.model.SplashConfig;
import com.example.model.ThemeKitConfig;
import com.example.model.ToolItem;
import com.example.model.UpdateDialogConfig;
import com.example.model.WelcomeConfig;
import com.example.ui.theme.ColorKt;
import com.example.viewmodel.AdminUiState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
/* compiled from: AppModulesAndThemeScreen.kt */
@Metadata(d1 = {"\u0000n\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b/\u001aÇ\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u00052\u008b\u0001\u0010\u000b\u001a\u0086\u0001\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0013\u0012\u00110\u0013¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0014\u0012\u0013\u0012\u00110\u0015¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0016\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0017\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0018\u0012\u0004\u0012\u00020\u00010\f2f\u0010\u0019\u001ab\u0012\u0013\u0012\u00110\u001b¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u001c\u0012\u0013\u0012\u00110\u001d¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u001e\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u001f\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020!0 ¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\"\u0012\u0004\u0012\u00020\u00010\u001aH\u0007¢\u0006\u0002\u0010#\u001a)\u0010$\u001a\u00020\u00012\u0006\u0010%\u001a\u00020\n2\u0012\u0010&\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u0005H\u0003¢\u0006\u0002\u0010'\u001a)\u0010(\u001a\u00020\u00012\u0006\u0010%\u001a\u00020\b2\u0012\u0010&\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003¢\u0006\u0002\u0010)\u001a)\u0010*\u001a\u00020\u00012\u0006\u0010%\u001a\u00020\u00062\u0012\u0010&\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u0005H\u0003¢\u0006\u0002\u0010+\u001a£\u0001\u0010,\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u008b\u0001\u0010&\u001a\u0086\u0001\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0013\u0012\u00110\u0013¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0014\u0012\u0013\u0012\u00110\u0015¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0016\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0017\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0018\u0012\u0004\u0012\u00020\u00010\fH\u0003¢\u0006\u0002\u0010-\u001a}\u0010.\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032f\u0010&\u001ab\u0012\u0013\u0012\u00110\u001b¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u001c\u0012\u0013\u0012\u00110\u001d¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u001e\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u001f\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020!0 ¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\"\u0012\u0004\u0012\u00020\u00010\u001aH\u0003¢\u0006\u0002\u0010/\u001a)\u00100\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u00101\u001a\u000e\u0012\u0004\u0012\u000202\u0012\u0004\u0012\u00020\u00010\u0005H\u0007¢\u0006\u0002\u00103\u001a1\u00104\u001a\u00020\u00012\u0006\u00105\u001a\u00020\u00112\u0006\u00106\u001a\u00020\u00112\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00010\u0005H\u0003¢\u0006\u0002\u00108¨\u00069²\u0006\n\u0010:\u001a\u00020\u0013X\u008a\u008e\u0002²\u0006\n\u0010;\u001a\u00020\u0015X\u008a\u008e\u0002²\u0006\n\u0010<\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010=\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010>\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010;\u001a\u00020\u0015X\u008a\u008e\u0002²\u0006\n\u0010?\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010@\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010A\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010B\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010C\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010D\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010E\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010F\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010G\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010H\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010I\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010\u0012\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010J\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010\u0016\u001a\u00020\u0015X\u008a\u008e\u0002²\u0006\n\u0010\u0017\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010\u0018\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010?\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010K\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010L\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010M\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010N\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010I\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010O\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010P\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010Q\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010R\u001a\u00020\u0015X\u008a\u008e\u0002²\u0006\n\u0010S\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010T\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010U\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010V\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010W\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010X\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010Y\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010Z\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010[\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010\\\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010]\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010^\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010_\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010`\u001a\u00020\u0011X\u008a\u008e\u0002²\u0006\n\u0010a\u001a\u00020\u0011X\u008a\u008e\u0002"}, d2 = {"AppModulesScreen", "", "uiState", "Lcom/example/viewmodel/AdminUiState;", "onSaveSplash", "Lkotlin/Function1;", "Lcom/example/model/SplashConfig;", "onSaveWelcome", "Lcom/example/model/WelcomeConfig;", "onSaveMarquee", "Lcom/example/model/MarqueeConfig;", "onSaveUpdateDialogAndVersion", "Lkotlin/Function6;", "Lcom/example/model/UpdateDialogConfig;", "Lkotlin/ParameterName;", HintConstants.AUTOFILL_HINT_NAME, "updCfg", "", "vName", "", "vCode", "", "vForce", "vApkUrl", "vApkUrlRaw", "onSaveMiscModules", "Lkotlin/Function4;", "Lcom/example/model/ConsoleConfig;", "consoleCfg", "Lcom/example/model/IpMonitorConfig;", "ipCfg", "aiNoticeText", "", "Lcom/example/model/ToolItem;", "tools", "(Lcom/example/viewmodel/AdminUiState;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function6;Lkotlin/jvm/functions/Function4;Landroidx/compose/runtime/Composer;I)V", "MarqueeEditorCard", "config", "onSave", "(Lcom/example/model/MarqueeConfig;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "WelcomeEditorCard", "(Lcom/example/model/WelcomeConfig;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "SplashEditorCard", "(Lcom/example/model/SplashConfig;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "UpdateDialogEditorCard", "(Lcom/example/viewmodel/AdminUiState;Lkotlin/jvm/functions/Function6;Landroidx/compose/runtime/Composer;I)V", "MiscModulesEditorCard", "(Lcom/example/viewmodel/AdminUiState;Lkotlin/jvm/functions/Function4;Landroidx/compose/runtime/Composer;I)V", "ThemeKitScreen", "onSaveThemeKit", "Lcom/example/model/ThemeKitConfig;", "(Lcom/example/viewmodel/AdminUiState;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "CssField", "label", "value", "onValueChange", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "app", "selectedTab", "enabled", "icon", "defaultText", "segmentsText", "title", "welcomeText", "content", "ratio", "imageUrl", "buttonText", "type", "durationStr", "bgColor", "mediaUrl", "customHtml", "vCodeStr", "changelogText", "confirmText", "cancelText", "customCss", "consoleVer", "consoleCode", "consoleApk", "ipEnabled", "ipUrl", "aiNotice", "toolsText", "appBarCss", "bottomBarCss", "splashCss", "statusBarCss", "cardCss", "buttonCss", "dialogCss", "searchCss", "globalCss", "settingsPageCss", "customThemeCss", "customThemeHtml"}, k = 2, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class AppModulesAndThemeScreenKt {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AppModulesScreen$lambda$9(AdminUiState adminUiState, Function1 function1, Function1 function12, Function1 function13, Function6 function6, Function4 function4, int i, Composer composer, int i2) {
        AppModulesScreen(adminUiState, function1, function12, function13, function6, function4, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CssField$lambda$293(String str, String str2, Function1 function1, int i, Composer composer, int i2) {
        CssField(str, str2, function1, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MarqueeEditorCard$lambda$38(MarqueeConfig marqueeConfig, Function1 function1, int i, Composer composer, int i2) {
        MarqueeEditorCard(marqueeConfig, function1, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MiscModulesEditorCard$lambda$224(AdminUiState adminUiState, Function4 function4, int i, Composer composer, int i2) {
        MiscModulesEditorCard(adminUiState, function4, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SplashEditorCard$lambda$113(SplashConfig splashConfig, Function1 function1, int i, Composer composer, int i2) {
        SplashEditorCard(splashConfig, function1, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$291(AdminUiState adminUiState, Function1 function1, int i, Composer composer, int i2) {
        ThemeKitScreen(adminUiState, function1, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UpdateDialogEditorCard$lambda$180(AdminUiState adminUiState, Function6 function6, int i, Composer composer, int i2) {
        UpdateDialogEditorCard(adminUiState, function6, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeEditorCard$lambda$81(WelcomeConfig welcomeConfig, Function1 function1, int i, Composer composer, int i2) {
        WelcomeEditorCard(welcomeConfig, function1, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:108:0x054d  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0554  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0570  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x058c  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x05a6  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x05c2  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x060f  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0314  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0320  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0326  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0357  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x036d  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x03d8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void AppModulesScreen(final com.example.viewmodel.AdminUiState r95, final kotlin.jvm.functions.Function1<? super com.example.model.SplashConfig, kotlin.Unit> r96, final kotlin.jvm.functions.Function1<? super com.example.model.WelcomeConfig, kotlin.Unit> r97, final kotlin.jvm.functions.Function1<? super com.example.model.MarqueeConfig, kotlin.Unit> r98, final kotlin.jvm.functions.Function6<? super com.example.model.UpdateDialogConfig, ? super java.lang.String, ? super java.lang.Integer, ? super java.lang.Boolean, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> r99, final kotlin.jvm.functions.Function4<? super com.example.model.ConsoleConfig, ? super com.example.model.IpMonitorConfig, ? super java.lang.String, ? super java.util.List<com.example.model.ToolItem>, kotlin.Unit> r100, androidx.compose.runtime.Composer r101, final int r102) {
        /*
            Method dump skipped, instructions count: 1586
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.AppModulesAndThemeScreenKt.AppModulesScreen(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function6, kotlin.jvm.functions.Function4, androidx.compose.runtime.Composer, int):void");
    }

    private static final int AppModulesScreen$lambda$1(MutableIntState $selectedTab$delegate) {
        return $selectedTab$delegate.getIntValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AppModulesScreen$lambda$8$lambda$7$lambda$6$lambda$4$lambda$3(int $idx, MutableIntState $selectedTab$delegate) {
        $selectedTab$delegate.setIntValue($idx);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AppModulesScreen$lambda$8$lambda$7$lambda$6$lambda$5(String $label, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C71@2501L10,71@2467L56:AppModulesAndThemeScreen.kt#2thlc2");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(603172382, $changed, -1, "com.example.ui.screens.AppModulesScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous> (AppModulesAndThemeScreen.kt:71)");
            }
            TextKt.m2693Text4IGK_g($label, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelLarge(), $composer, 0, 0, 65534);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    private static final void MarqueeEditorCard(final MarqueeConfig config, final Function1<? super MarqueeConfig, Unit> function1, Composer $composer, final int $changed) {
        MutableState mutableStateOf$default;
        MutableState mutableStateOf$default2;
        MutableState icon$delegate;
        MutableState mutableStateOf$default3;
        Object mutableStateOf$default4;
        Composer $composer2;
        Composer $composer3 = $composer.startRestartGroup(16219414);
        ComposerKt.sourceInformation($composer3, "C(MarqueeEditorCard)113@4003L51,114@4071L48,115@4143L55,116@4223L71,124@4503L3034,118@4300L3237:AppModulesAndThemeScreen.kt#2thlc2");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changedInstance(config) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changedInstance(function1) ? 32 : 16;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(16219414, $dirty2, -1, "com.example.ui.screens.MarqueeEditorCard (AppModulesAndThemeScreen.kt:112)");
            }
            ComposerKt.sourceInformationMarkerStart($composer3, -1593008151, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed = $composer3.changed(config);
            Object rememberedValue = $composer3.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                mutableStateOf$default = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(Boolean.valueOf(config.getEnabled()), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default);
            } else {
                mutableStateOf$default = rememberedValue;
            }
            final MutableState enabled$delegate = mutableStateOf$default;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, -1593005978, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed2 = $composer3.changed(config);
            Object rememberedValue2 = $composer3.rememberedValue();
            if (changed2 || rememberedValue2 == Composer.Companion.getEmpty()) {
                mutableStateOf$default2 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(config.getIcon(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default2);
            } else {
                mutableStateOf$default2 = rememberedValue2;
            }
            MutableState icon$delegate2 = mutableStateOf$default2;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, -1593003667, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed3 = $composer3.changed(config);
            Object rememberedValue3 = $composer3.rememberedValue();
            if (changed3 || rememberedValue3 == Composer.Companion.getEmpty()) {
                icon$delegate = icon$delegate2;
                mutableStateOf$default3 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(config.getDefaultText(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default3);
            } else {
                icon$delegate = icon$delegate2;
                mutableStateOf$default3 = rememberedValue3;
            }
            final MutableState defaultText$delegate = mutableStateOf$default3;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, -1593001091, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed4 = $composer3.changed(config);
            Object rememberedValue4 = $composer3.rememberedValue();
            if (changed4 || rememberedValue4 == Composer.Companion.getEmpty()) {
                mutableStateOf$default4 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(CollectionsKt.joinToString$default(config.getSegments(), "\n", null, null, 0, null, null, 62, null), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default4);
            } else {
                mutableStateOf$default4 = rememberedValue4;
            }
            final MutableState segmentsText$delegate = (MutableState) mutableStateOf$default4;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final MutableState icon$delegate3 = icon$delegate;
            $composer2 = $composer3;
            SurfaceKt.m2543SurfaceT9BRK9s(SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, null), RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(16)), ColorKt.getPaperSoft(), 0L, 0.0f, Dp.m6622constructorimpl(2), BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist()), ComposableLambdaKt.rememberComposableLambda(434242267, true, new Function2() { // from class: com.example.ui.screens.AppModulesAndThemeScreenKt$$ExternalSyntheticLambda59
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AppModulesAndThemeScreenKt.MarqueeEditorCard$lambda$37(MutableState.this, defaultText$delegate, segmentsText$delegate, function1, enabled$delegate, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), $composer2, 14352774, 24);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.screens.AppModulesAndThemeScreenKt$$ExternalSyntheticLambda60
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AppModulesAndThemeScreenKt.MarqueeEditorCard$lambda$38(MarqueeConfig.this, function1, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    private static final boolean MarqueeEditorCard$lambda$11(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void MarqueeEditorCard$lambda$12(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final String MarqueeEditorCard$lambda$14(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String MarqueeEditorCard$lambda$17(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String MarqueeEditorCard$lambda$20(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01e5  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01f7  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0310  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x031c  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0322  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0353  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0369  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0446  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x050c  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x059c  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x062d  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x06e0  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x073b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit MarqueeEditorCard$lambda$37(final androidx.compose.runtime.MutableState r105, final androidx.compose.runtime.MutableState r106, final androidx.compose.runtime.MutableState r107, final kotlin.jvm.functions.Function1 r108, final androidx.compose.runtime.MutableState r109, androidx.compose.runtime.Composer r110, int r111) {
        /*
            Method dump skipped, instructions count: 1857
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.AppModulesAndThemeScreenKt.MarqueeEditorCard$lambda$37(androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, kotlin.jvm.functions.Function1, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MarqueeEditorCard$lambda$37$lambda$36$lambda$25$lambda$24$lambda$23(MutableState $enabled$delegate, boolean it) {
        MarqueeEditorCard$lambda$12($enabled$delegate, it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MarqueeEditorCard$lambda$37$lambda$36$lambda$27$lambda$26(MutableState $icon$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $icon$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MarqueeEditorCard$lambda$37$lambda$36$lambda$29$lambda$28(MutableState $defaultText$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $defaultText$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MarqueeEditorCard$lambda$37$lambda$36$lambda$31$lambda$30(MutableState $segmentsText$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $segmentsText$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MarqueeEditorCard$lambda$37$lambda$36$lambda$35$lambda$34(Function1 $onSave, MutableState $enabled$delegate, MutableState $icon$delegate, MutableState $defaultText$delegate, MutableState $segmentsText$delegate) {
        boolean MarqueeEditorCard$lambda$11 = MarqueeEditorCard$lambda$11($enabled$delegate);
        String MarqueeEditorCard$lambda$14 = MarqueeEditorCard$lambda$14($icon$delegate);
        String MarqueeEditorCard$lambda$17 = MarqueeEditorCard$lambda$17($defaultText$delegate);
        Iterable<String> lines = StringsKt.lines(MarqueeEditorCard$lambda$20($segmentsText$delegate));
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(lines, 10));
        for (String str : lines) {
            arrayList.add(StringsKt.trim((CharSequence) str).toString());
        }
        Collection arrayList2 = new ArrayList();
        for (Object obj : (List) arrayList) {
            if (((String) obj).length() > 0) {
                arrayList2.add(obj);
            }
        }
        $onSave.invoke(new MarqueeConfig(MarqueeEditorCard$lambda$11, MarqueeEditorCard$lambda$14, MarqueeEditorCard$lambda$17, (List) arrayList2));
        return Unit.INSTANCE;
    }

    private static final void WelcomeEditorCard(final WelcomeConfig config, final Function1<? super WelcomeConfig, Unit> function1, Composer $composer, final int $changed) {
        MutableState mutableStateOf$default;
        MutableState mutableStateOf$default2;
        MutableState title$delegate;
        MutableState mutableStateOf$default3;
        MutableState mutableStateOf$default4;
        MutableState welcomeText$delegate;
        MutableState mutableStateOf$default5;
        MutableState content$delegate;
        MutableState mutableStateOf$default6;
        MutableState mutableStateOf$default7;
        Composer $composer2;
        Composer $composer3 = $composer.startRestartGroup(810301078);
        ComposerKt.sourceInformation($composer3, "C(WelcomeEditorCard)208@7670L51,209@7739L49,210@7812L55,211@7887L51,212@7956L49,213@8026L52,214@8101L54,222@8364L4166,216@8161L4369:AppModulesAndThemeScreen.kt#2thlc2");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(config) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changedInstance(function1) ? 32 : 16;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(810301078, $dirty2, -1, "com.example.ui.screens.WelcomeEditorCard (AppModulesAndThemeScreen.kt:207)");
            }
            ComposerKt.sourceInformationMarkerStart($composer3, 1310259561, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean z = ($dirty2 & 14) == 4;
            Object rememberedValue = $composer3.rememberedValue();
            if (z || rememberedValue == Composer.Companion.getEmpty()) {
                mutableStateOf$default = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(Boolean.valueOf(config.getEnabled()), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default);
            } else {
                mutableStateOf$default = rememberedValue;
            }
            final MutableState enabled$delegate = mutableStateOf$default;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 1310261767, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean z2 = ($dirty2 & 14) == 4;
            Object rememberedValue2 = $composer3.rememberedValue();
            if (z2 || rememberedValue2 == Composer.Companion.getEmpty()) {
                mutableStateOf$default2 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(config.getTitle(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default2);
            } else {
                mutableStateOf$default2 = rememberedValue2;
            }
            MutableState title$delegate2 = mutableStateOf$default2;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 1310264109, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean z3 = ($dirty2 & 14) == 4;
            Object rememberedValue3 = $composer3.rememberedValue();
            if (z3 || rememberedValue3 == Composer.Companion.getEmpty()) {
                title$delegate = title$delegate2;
                mutableStateOf$default3 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(config.getWelcomeText(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default3);
            } else {
                title$delegate = title$delegate2;
                mutableStateOf$default3 = rememberedValue3;
            }
            MutableState welcomeText$delegate2 = mutableStateOf$default3;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 1310266505, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean z4 = ($dirty2 & 14) == 4;
            Object rememberedValue4 = $composer3.rememberedValue();
            if (z4 || rememberedValue4 == Composer.Companion.getEmpty()) {
                mutableStateOf$default4 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(config.getContent(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default4);
            } else {
                mutableStateOf$default4 = rememberedValue4;
            }
            MutableState content$delegate2 = mutableStateOf$default4;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 1310268711, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean z5 = ($dirty2 & 14) == 4;
            Object rememberedValue5 = $composer3.rememberedValue();
            if (z5 || rememberedValue5 == Composer.Companion.getEmpty()) {
                welcomeText$delegate = welcomeText$delegate2;
                mutableStateOf$default5 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(config.getRatio(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default5);
            } else {
                welcomeText$delegate = welcomeText$delegate2;
                mutableStateOf$default5 = rememberedValue5;
            }
            final MutableState ratio$delegate = mutableStateOf$default5;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 1310270954, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean z6 = ($dirty2 & 14) == 4;
            Object rememberedValue6 = $composer3.rememberedValue();
            if (z6 || rememberedValue6 == Composer.Companion.getEmpty()) {
                content$delegate = content$delegate2;
                mutableStateOf$default6 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(config.getImageUrl(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default6);
            } else {
                content$delegate = content$delegate2;
                mutableStateOf$default6 = rememberedValue6;
            }
            final MutableState imageUrl$delegate = mutableStateOf$default6;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 1310273356, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean z7 = ($dirty2 & 14) == 4;
            Object rememberedValue7 = $composer3.rememberedValue();
            if (z7 || rememberedValue7 == Composer.Companion.getEmpty()) {
                mutableStateOf$default7 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(config.getButtonText(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default7);
            } else {
                mutableStateOf$default7 = rememberedValue7;
            }
            final MutableState buttonText$delegate = mutableStateOf$default7;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final MutableState title$delegate3 = title$delegate;
            final MutableState welcomeText$delegate3 = welcomeText$delegate;
            final MutableState content$delegate3 = content$delegate;
            $composer2 = $composer3;
            SurfaceKt.m2543SurfaceT9BRK9s(SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, null), RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(16)), ColorKt.getPaperSoft(), 0L, 0.0f, Dp.m6622constructorimpl(2), BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist()), ComposableLambdaKt.rememberComposableLambda(1228323931, true, new Function2() { // from class: com.example.ui.screens.AppModulesAndThemeScreenKt$$ExternalSyntheticLambda57
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AppModulesAndThemeScreenKt.WelcomeEditorCard$lambda$80(MutableState.this, welcomeText$delegate3, content$delegate3, imageUrl$delegate, function1, enabled$delegate, ratio$delegate, buttonText$delegate, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), $composer2, 14352774, 24);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.screens.AppModulesAndThemeScreenKt$$ExternalSyntheticLambda58
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AppModulesAndThemeScreenKt.WelcomeEditorCard$lambda$81(WelcomeConfig.this, function1, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    private static final boolean WelcomeEditorCard$lambda$40(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void WelcomeEditorCard$lambda$41(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final String WelcomeEditorCard$lambda$43(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String WelcomeEditorCard$lambda$46(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String WelcomeEditorCard$lambda$49(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String WelcomeEditorCard$lambda$52(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String WelcomeEditorCard$lambda$55(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String WelcomeEditorCard$lambda$58(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0769 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:105:0x07f5  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0805 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0899  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x08a9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x094c  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0a0e  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0a6a  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01e4  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01f0  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x030c  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0318  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x031e  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0351  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0367 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0445  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0452  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x050a  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0517  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x059d  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x05aa  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0630  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x063d  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0710  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x071c  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0722  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0753  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit WelcomeEditorCard$lambda$80(final androidx.compose.runtime.MutableState r109, final androidx.compose.runtime.MutableState r110, final androidx.compose.runtime.MutableState r111, final androidx.compose.runtime.MutableState r112, final kotlin.jvm.functions.Function1 r113, final androidx.compose.runtime.MutableState r114, final androidx.compose.runtime.MutableState r115, final androidx.compose.runtime.MutableState r116, androidx.compose.runtime.Composer r117, int r118) {
        /*
            Method dump skipped, instructions count: 2672
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.AppModulesAndThemeScreenKt.WelcomeEditorCard$lambda$80(androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, kotlin.jvm.functions.Function1, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeEditorCard$lambda$80$lambda$79$lambda$63$lambda$62$lambda$61(MutableState $enabled$delegate, boolean it) {
        WelcomeEditorCard$lambda$41($enabled$delegate, it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeEditorCard$lambda$80$lambda$79$lambda$65$lambda$64(MutableState $title$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $title$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeEditorCard$lambda$80$lambda$79$lambda$67$lambda$66(MutableState $welcomeText$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $welcomeText$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeEditorCard$lambda$80$lambda$79$lambda$69$lambda$68(MutableState $content$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $content$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeEditorCard$lambda$80$lambda$79$lambda$74$lambda$71$lambda$70(MutableState $buttonText$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $buttonText$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeEditorCard$lambda$80$lambda$79$lambda$74$lambda$73$lambda$72(MutableState $ratio$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $ratio$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeEditorCard$lambda$80$lambda$79$lambda$76$lambda$75(MutableState $imageUrl$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $imageUrl$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeEditorCard$lambda$80$lambda$79$lambda$78$lambda$77(Function1 $onSave, MutableState $enabled$delegate, MutableState $title$delegate, MutableState $welcomeText$delegate, MutableState $content$delegate, MutableState $ratio$delegate, MutableState $imageUrl$delegate, MutableState $buttonText$delegate) {
        $onSave.invoke(new WelcomeConfig(WelcomeEditorCard$lambda$40($enabled$delegate), WelcomeEditorCard$lambda$43($title$delegate), WelcomeEditorCard$lambda$46($welcomeText$delegate), WelcomeEditorCard$lambda$49($content$delegate), WelcomeEditorCard$lambda$52($ratio$delegate), WelcomeEditorCard$lambda$55($imageUrl$delegate), WelcomeEditorCard$lambda$58($buttonText$delegate)));
        return Unit.INSTANCE;
    }

    private static final void SplashEditorCard(final SplashConfig config, final Function1<? super SplashConfig, Unit> function1, Composer $composer, final int $changed) {
        Object mutableStateOf$default;
        MutableState mutableStateOf$default2;
        MutableState durationStr$delegate;
        MutableState mutableStateOf$default3;
        MutableState mutableStateOf$default4;
        MutableState mutableStateOf$default5;
        Composer $composer2;
        Composer $composer3 = $composer.startRestartGroup(1860063812);
        ComposerKt.sourceInformation($composer3, "C(SplashEditorCard)337@12657L48,338@12729L70,339@12819L51,340@12891L52,341@12966L54,349@13229L3627,343@13026L3830:AppModulesAndThemeScreen.kt#2thlc2");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(config) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changedInstance(function1) ? 32 : 16;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1860063812, $dirty2, -1, "com.example.ui.screens.SplashEditorCard (AppModulesAndThemeScreen.kt:336)");
            }
            ComposerKt.sourceInformationMarkerStart($composer3, 814932180, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean z = ($dirty2 & 14) == 4;
            Object rememberedValue = $composer3.rememberedValue();
            if (z || rememberedValue == Composer.Companion.getEmpty()) {
                mutableStateOf$default = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(config.getType(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default);
            } else {
                mutableStateOf$default = rememberedValue;
            }
            final MutableState type$delegate = (MutableState) mutableStateOf$default;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 814934506, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean z2 = ($dirty2 & 14) == 4;
            Object rememberedValue2 = $composer3.rememberedValue();
            if (z2 || rememberedValue2 == Composer.Companion.getEmpty()) {
                mutableStateOf$default2 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(String.valueOf(config.getDurationSeconds()), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default2);
            } else {
                mutableStateOf$default2 = rememberedValue2;
            }
            MutableState durationStr$delegate2 = mutableStateOf$default2;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 814937367, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean z3 = ($dirty2 & 14) == 4;
            Object rememberedValue3 = $composer3.rememberedValue();
            if (z3 || rememberedValue3 == Composer.Companion.getEmpty()) {
                durationStr$delegate = durationStr$delegate2;
                mutableStateOf$default3 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(config.getBgColor(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default3);
            } else {
                durationStr$delegate = durationStr$delegate2;
                mutableStateOf$default3 = rememberedValue3;
            }
            final MutableState bgColor$delegate = mutableStateOf$default3;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 814939672, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean z4 = ($dirty2 & 14) == 4;
            Object rememberedValue4 = $composer3.rememberedValue();
            if (z4 || rememberedValue4 == Composer.Companion.getEmpty()) {
                mutableStateOf$default4 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(config.getMediaUrl(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default4);
            } else {
                mutableStateOf$default4 = rememberedValue4;
            }
            final MutableState mediaUrl$delegate = mutableStateOf$default4;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 814942074, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean z5 = ($dirty2 & 14) == 4;
            Object rememberedValue5 = $composer3.rememberedValue();
            if (z5 || rememberedValue5 == Composer.Companion.getEmpty()) {
                mutableStateOf$default5 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(config.getCustomHtml(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default5);
            } else {
                mutableStateOf$default5 = rememberedValue5;
            }
            final MutableState customHtml$delegate = mutableStateOf$default5;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final MutableState durationStr$delegate3 = durationStr$delegate;
            $composer2 = $composer3;
            SurfaceKt.m2543SurfaceT9BRK9s(SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, null), RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(16)), ColorKt.getPaperSoft(), 0L, 0.0f, Dp.m6622constructorimpl(2), BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist()), ComposableLambdaKt.rememberComposableLambda(-1781955255, true, new Function2() { // from class: com.example.ui.screens.AppModulesAndThemeScreenKt$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AppModulesAndThemeScreenKt.SplashEditorCard$lambda$112(MutableState.this, function1, type$delegate, mediaUrl$delegate, durationStr$delegate3, bgColor$delegate, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), $composer2, 14352774, 24);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.screens.AppModulesAndThemeScreenKt$$ExternalSyntheticLambda11
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AppModulesAndThemeScreenKt.SplashEditorCard$lambda$113(SplashConfig.this, function1, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    private static final String SplashEditorCard$lambda$83(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SplashEditorCard$lambda$86(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SplashEditorCard$lambda$89(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SplashEditorCard$lambda$92(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String SplashEditorCard$lambda$95(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:104:0x088e  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0275  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x027b  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0354  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0364  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x03fc  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x040c  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x04fd  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0509  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x050f  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0540  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0556  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x05e1  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x05f1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0689  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0699 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0779  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0831  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SplashEditorCard$lambda$112(final androidx.compose.runtime.MutableState r100, final kotlin.jvm.functions.Function1 r101, final androidx.compose.runtime.MutableState r102, final androidx.compose.runtime.MutableState r103, final androidx.compose.runtime.MutableState r104, final androidx.compose.runtime.MutableState r105, androidx.compose.runtime.Composer r106, int r107) {
        /*
            Method dump skipped, instructions count: 2196
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.AppModulesAndThemeScreenKt.SplashEditorCard$lambda$112(androidx.compose.runtime.MutableState, kotlin.jvm.functions.Function1, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SplashEditorCard$lambda$112$lambda$111$lambda$101$lambda$98$lambda$97(MutableState $type$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $type$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SplashEditorCard$lambda$112$lambda$111$lambda$101$lambda$100$lambda$99(MutableState $durationStr$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $durationStr$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SplashEditorCard$lambda$112$lambda$111$lambda$106$lambda$103$lambda$102(MutableState $bgColor$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $bgColor$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SplashEditorCard$lambda$112$lambda$111$lambda$106$lambda$105$lambda$104(MutableState $mediaUrl$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $mediaUrl$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SplashEditorCard$lambda$112$lambda$111$lambda$108$lambda$107(MutableState $customHtml$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $customHtml$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SplashEditorCard$lambda$112$lambda$111$lambda$110$lambda$109(Function1 $onSave, MutableState $type$delegate, MutableState $customHtml$delegate, MutableState $mediaUrl$delegate, MutableState $durationStr$delegate, MutableState $bgColor$delegate) {
        String SplashEditorCard$lambda$83 = SplashEditorCard$lambda$83($type$delegate);
        String SplashEditorCard$lambda$95 = SplashEditorCard$lambda$95($customHtml$delegate);
        String SplashEditorCard$lambda$92 = SplashEditorCard$lambda$92($mediaUrl$delegate);
        Integer intOrNull = StringsKt.toIntOrNull(SplashEditorCard$lambda$86($durationStr$delegate));
        $onSave.invoke(new SplashConfig(SplashEditorCard$lambda$83, SplashEditorCard$lambda$95, SplashEditorCard$lambda$92, intOrNull != null ? intOrNull.intValue() : 3, SplashEditorCard$lambda$89($bgColor$delegate)));
        return Unit.INSTANCE;
    }

    private static final void UpdateDialogEditorCard(final AdminUiState uiState, final Function6<? super UpdateDialogConfig, ? super String, ? super Integer, ? super Boolean, ? super String, ? super String, Unit> function6, Composer $composer, final int $changed) {
        MutableState mutableStateOf$default;
        int $dirty;
        MutableState mutableStateOf$default2;
        MutableState mutableStateOf$default3;
        MutableState vName$delegate;
        MutableState mutableStateOf$default4;
        MutableState vApkUrl$delegate;
        MutableState mutableStateOf$default5;
        Object mutableStateOf$default6;
        MutableState title$delegate;
        MutableState vApkUrlRaw$delegate;
        MutableState mutableStateOf$default7;
        MutableState changelogText$delegate;
        MutableState mutableStateOf$default8;
        Object mutableStateOf$default9;
        Object mutableStateOf$default10;
        MutableState customCss$delegate;
        MutableState mutableStateOf$default11;
        final AdminUiState adminUiState;
        final Function6<? super UpdateDialogConfig, ? super String, ? super Integer, ? super Boolean, ? super String, ? super String, Unit> function62;
        Composer $composer2;
        Composer $composer3 = $composer.startRestartGroup(-1105658300);
        ComposerKt.sourceInformation($composer3, "C(UpdateDialogEditorCard)P(1)454@17181L69,455@17271L80,456@17370L71,457@17461L73,458@17557L79,460@17655L43,461@17724L66,462@17814L49,463@17886L48,464@17956L47,465@18026L48,473@18283L6279,467@18080L6482:AppModulesAndThemeScreen.kt#2thlc2");
        int $dirty2 = $changed;
        if (($changed & 6) == 0) {
            $dirty2 |= $composer3.changedInstance(uiState) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty2 |= $composer3.changedInstance(function6) ? 32 : 16;
        }
        if (($dirty2 & 19) == 18 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            function62 = function6;
            adminUiState = uiState;
            $composer2 = $composer3;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1105658300, $dirty2, -1, "com.example.ui.screens.UpdateDialogEditorCard (AppModulesAndThemeScreen.kt:452)");
            }
            UpdateDialogConfig upd = uiState.getUpdateDialogConfig();
            String versionName = uiState.getVersionName();
            ComposerKt.sourceInformationMarkerStart($composer3, -1680693847, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed = $composer3.changed(versionName);
            Object rememberedValue = $composer3.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                mutableStateOf$default = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(uiState.getVersionName(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default);
            } else {
                mutableStateOf$default = rememberedValue;
            }
            MutableState vName$delegate2 = mutableStateOf$default;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            int versionCode = uiState.getVersionCode();
            ComposerKt.sourceInformationMarkerStart($composer3, -1680690956, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed2 = $composer3.changed(versionCode);
            Object rememberedValue2 = $composer3.rememberedValue();
            if (changed2 || rememberedValue2 == Composer.Companion.getEmpty()) {
                $dirty = $dirty2;
                mutableStateOf$default2 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(String.valueOf(uiState.getVersionCode()), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default2);
            } else {
                $dirty = $dirty2;
                mutableStateOf$default2 = rememberedValue2;
            }
            final MutableState vCodeStr$delegate = mutableStateOf$default2;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            boolean versionForce = uiState.getVersionForce();
            ComposerKt.sourceInformationMarkerStart($composer3, -1680687797, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed3 = $composer3.changed(versionForce);
            Object rememberedValue3 = $composer3.rememberedValue();
            if (changed3 || rememberedValue3 == Composer.Companion.getEmpty()) {
                mutableStateOf$default3 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(Boolean.valueOf(uiState.getVersionForce()), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default3);
            } else {
                mutableStateOf$default3 = rememberedValue3;
            }
            final MutableState vForce$delegate = mutableStateOf$default3;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            String versionApkUrl = uiState.getVersionApkUrl();
            ComposerKt.sourceInformationMarkerStart($composer3, -1680684883, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed4 = $composer3.changed(versionApkUrl);
            Object rememberedValue4 = $composer3.rememberedValue();
            if (changed4 || rememberedValue4 == Composer.Companion.getEmpty()) {
                vName$delegate = vName$delegate2;
                mutableStateOf$default4 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(uiState.getVersionApkUrl(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default4);
            } else {
                vName$delegate = vName$delegate2;
                mutableStateOf$default4 = rememberedValue4;
            }
            MutableState vApkUrl$delegate2 = mutableStateOf$default4;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            String versionApkUrlRaw = uiState.getVersionApkUrlRaw();
            ComposerKt.sourceInformationMarkerStart($composer3, -1680681805, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed5 = $composer3.changed(versionApkUrlRaw);
            Object rememberedValue5 = $composer3.rememberedValue();
            if (changed5 || rememberedValue5 == Composer.Companion.getEmpty()) {
                vApkUrl$delegate = vApkUrl$delegate2;
                mutableStateOf$default5 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(uiState.getVersionApkUrlRaw(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default5);
            } else {
                vApkUrl$delegate = vApkUrl$delegate2;
                mutableStateOf$default5 = rememberedValue5;
            }
            MutableState vApkUrlRaw$delegate2 = mutableStateOf$default5;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, -1680678705, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed6 = $composer3.changed(upd);
            Object rememberedValue6 = $composer3.rememberedValue();
            if (changed6 || rememberedValue6 == Composer.Companion.getEmpty()) {
                mutableStateOf$default6 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(upd.getTitle(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default6);
            } else {
                mutableStateOf$default6 = rememberedValue6;
            }
            MutableState title$delegate2 = (MutableState) mutableStateOf$default6;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, -1680676474, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed7 = $composer3.changed(upd);
            Object rememberedValue7 = $composer3.rememberedValue();
            if (changed7 || rememberedValue7 == Composer.Companion.getEmpty()) {
                title$delegate = title$delegate2;
                vApkUrlRaw$delegate = vApkUrlRaw$delegate2;
                mutableStateOf$default7 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(CollectionsKt.joinToString$default(upd.getChangelog(), "\n", null, null, 0, null, null, 62, null), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default7);
            } else {
                title$delegate = title$delegate2;
                vApkUrlRaw$delegate = vApkUrlRaw$delegate2;
                mutableStateOf$default7 = rememberedValue7;
            }
            MutableState changelogText$delegate2 = mutableStateOf$default7;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, -1680673611, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed8 = $composer3.changed(upd);
            Object rememberedValue8 = $composer3.rememberedValue();
            if (changed8 || rememberedValue8 == Composer.Companion.getEmpty()) {
                changelogText$delegate = changelogText$delegate2;
                mutableStateOf$default8 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(upd.getConfirmText(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default8);
            } else {
                changelogText$delegate = changelogText$delegate2;
                mutableStateOf$default8 = rememberedValue8;
            }
            final MutableState confirmText$delegate = mutableStateOf$default8;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, -1680671308, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed9 = $composer3.changed(upd);
            Object rememberedValue9 = $composer3.rememberedValue();
            if (changed9 || rememberedValue9 == Composer.Companion.getEmpty()) {
                mutableStateOf$default9 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(upd.getCancelText(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default9);
            } else {
                mutableStateOf$default9 = rememberedValue9;
            }
            final MutableState cancelText$delegate = (MutableState) mutableStateOf$default9;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, -1680669069, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed10 = $composer3.changed(upd);
            Object rememberedValue10 = $composer3.rememberedValue();
            if (changed10 || rememberedValue10 == Composer.Companion.getEmpty()) {
                mutableStateOf$default10 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(upd.getCustomCss(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default10);
            } else {
                mutableStateOf$default10 = rememberedValue10;
            }
            MutableState customCss$delegate2 = (MutableState) mutableStateOf$default10;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, -1680666828, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed11 = $composer3.changed(upd);
            Object rememberedValue11 = $composer3.rememberedValue();
            if (changed11 || rememberedValue11 == Composer.Companion.getEmpty()) {
                customCss$delegate = customCss$delegate2;
                mutableStateOf$default11 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(upd.getCustomHtml(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default11);
            } else {
                customCss$delegate = customCss$delegate2;
                mutableStateOf$default11 = rememberedValue11;
            }
            final MutableState customHtml$delegate = mutableStateOf$default11;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            Modifier fillMaxWidth$default = SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, null);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(16));
            RoundedCornerShape roundedCornerShape = m953RoundedCornerShape0680j_4;
            final MutableState title$delegate3 = title$delegate;
            final MutableState vName$delegate3 = vName$delegate;
            final MutableState vApkUrl$delegate3 = vApkUrl$delegate;
            final MutableState vApkUrlRaw$delegate3 = vApkUrlRaw$delegate;
            final MutableState changelogText$delegate3 = changelogText$delegate;
            final MutableState customCss$delegate3 = customCss$delegate;
            adminUiState = uiState;
            function62 = function6;
            $composer2 = $composer3;
            SurfaceKt.m2543SurfaceT9BRK9s(fillMaxWidth$default, roundedCornerShape, ColorKt.getPaperSoft(), 0L, 0.0f, Dp.m6622constructorimpl(2), BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist()), ComposableLambdaKt.rememberComposableLambda(-1357833847, true, new Function2() { // from class: com.example.ui.screens.AppModulesAndThemeScreenKt$$ExternalSyntheticLambda48
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AppModulesAndThemeScreenKt.UpdateDialogEditorCard$lambda$179(MutableState.this, changelogText$delegate3, vApkUrl$delegate3, vApkUrlRaw$delegate3, customCss$delegate3, customHtml$delegate, function6, confirmText$delegate, cancelText$delegate, vName$delegate3, vCodeStr$delegate, uiState, vForce$delegate, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), $composer2, 14352774, 24);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.screens.AppModulesAndThemeScreenKt$$ExternalSyntheticLambda49
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AppModulesAndThemeScreenKt.UpdateDialogEditorCard$lambda$180(AdminUiState.this, function62, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    private static final String UpdateDialogEditorCard$lambda$115(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String UpdateDialogEditorCard$lambda$118(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final boolean UpdateDialogEditorCard$lambda$121(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void UpdateDialogEditorCard$lambda$122(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final String UpdateDialogEditorCard$lambda$124(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String UpdateDialogEditorCard$lambda$127(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String UpdateDialogEditorCard$lambda$130(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String UpdateDialogEditorCard$lambda$133(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String UpdateDialogEditorCard$lambda$136(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String UpdateDialogEditorCard$lambda$139(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String UpdateDialogEditorCard$lambda$142(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String UpdateDialogEditorCard$lambda$145(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x07d8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:104:0x086d  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x087b  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0915  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0922  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x09a8  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x09b5  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0a88  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0a94  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0a9a  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0acb  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0ae1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0b67  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0b77  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0c0d  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0c1d  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0cba  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0d4c  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x0d59  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x0de1  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0dee  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x0e79  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0e87 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0f64  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0fd9  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01fd  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0203  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0319  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0325  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x032b  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x035e  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0374 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x04b4  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x04c0  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x04c6  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x04f9  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x050f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x05b2  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x05c2  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x06e6  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x06f2  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x06f8  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x072b  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0741 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x07c8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit UpdateDialogEditorCard$lambda$179(final androidx.compose.runtime.MutableState r126, final androidx.compose.runtime.MutableState r127, final androidx.compose.runtime.MutableState r128, final androidx.compose.runtime.MutableState r129, final androidx.compose.runtime.MutableState r130, final androidx.compose.runtime.MutableState r131, final kotlin.jvm.functions.Function6 r132, final androidx.compose.runtime.MutableState r133, final androidx.compose.runtime.MutableState r134, final androidx.compose.runtime.MutableState r135, final androidx.compose.runtime.MutableState r136, final com.example.viewmodel.AdminUiState r137, final androidx.compose.runtime.MutableState r138, androidx.compose.runtime.Composer r139, int r140) {
        /*
            Method dump skipped, instructions count: 4063
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.AppModulesAndThemeScreenKt.UpdateDialogEditorCard$lambda$179(androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, kotlin.jvm.functions.Function6, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, com.example.viewmodel.AdminUiState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UpdateDialogEditorCard$lambda$179$lambda$178$lambda$151$lambda$150$lambda$149$lambda$148(MutableState $vForce$delegate, boolean it) {
        UpdateDialogEditorCard$lambda$122($vForce$delegate, it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UpdateDialogEditorCard$lambda$179$lambda$178$lambda$156$lambda$153$lambda$152(MutableState $vName$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $vName$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UpdateDialogEditorCard$lambda$179$lambda$178$lambda$156$lambda$155$lambda$154(MutableState $vCodeStr$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $vCodeStr$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UpdateDialogEditorCard$lambda$179$lambda$178$lambda$158$lambda$157(MutableState $title$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $title$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UpdateDialogEditorCard$lambda$179$lambda$178$lambda$160$lambda$159(MutableState $changelogText$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $changelogText$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UpdateDialogEditorCard$lambda$179$lambda$178$lambda$165$lambda$162$lambda$161(MutableState $confirmText$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $confirmText$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UpdateDialogEditorCard$lambda$179$lambda$178$lambda$165$lambda$164$lambda$163(MutableState $cancelText$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $cancelText$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UpdateDialogEditorCard$lambda$179$lambda$178$lambda$167$lambda$166(MutableState $vApkUrl$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $vApkUrl$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UpdateDialogEditorCard$lambda$179$lambda$178$lambda$169$lambda$168(MutableState $vApkUrlRaw$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $vApkUrlRaw$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UpdateDialogEditorCard$lambda$179$lambda$178$lambda$171$lambda$170(MutableState $customCss$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $customCss$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UpdateDialogEditorCard$lambda$179$lambda$178$lambda$173$lambda$172(MutableState $customHtml$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $customHtml$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UpdateDialogEditorCard$lambda$179$lambda$178$lambda$177$lambda$176(Function6 $onSave, AdminUiState $uiState, MutableState $title$delegate, MutableState $changelogText$delegate, MutableState $confirmText$delegate, MutableState $cancelText$delegate, MutableState $customCss$delegate, MutableState $customHtml$delegate, MutableState $vName$delegate, MutableState $vCodeStr$delegate, MutableState $vForce$delegate, MutableState $vApkUrl$delegate, MutableState $vApkUrlRaw$delegate) {
        String UpdateDialogEditorCard$lambda$130 = UpdateDialogEditorCard$lambda$130($title$delegate);
        Iterable<String> lines = StringsKt.lines(UpdateDialogEditorCard$lambda$133($changelogText$delegate));
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(lines, 10));
        for (String str : lines) {
            arrayList.add(StringsKt.trim((CharSequence) str).toString());
        }
        Collection arrayList2 = new ArrayList();
        for (Object obj : (List) arrayList) {
            if (((String) obj).length() > 0) {
                arrayList2.add(obj);
            }
        }
        UpdateDialogConfig updateDialogConfig = new UpdateDialogConfig(UpdateDialogEditorCard$lambda$130, (List) arrayList2, UpdateDialogEditorCard$lambda$136($confirmText$delegate), UpdateDialogEditorCard$lambda$139($cancelText$delegate), UpdateDialogEditorCard$lambda$142($customCss$delegate), UpdateDialogEditorCard$lambda$145($customHtml$delegate));
        String UpdateDialogEditorCard$lambda$115 = UpdateDialogEditorCard$lambda$115($vName$delegate);
        Integer intOrNull = StringsKt.toIntOrNull(UpdateDialogEditorCard$lambda$118($vCodeStr$delegate));
        $onSave.invoke(updateDialogConfig, UpdateDialogEditorCard$lambda$115, Integer.valueOf(intOrNull != null ? intOrNull.intValue() : $uiState.getVersionCode()), Boolean.valueOf(UpdateDialogEditorCard$lambda$121($vForce$delegate)), UpdateDialogEditorCard$lambda$124($vApkUrl$delegate), UpdateDialogEditorCard$lambda$127($vApkUrlRaw$delegate));
        return Unit.INSTANCE;
    }

    private static final void MiscModulesEditorCard(final AdminUiState uiState, final Function4<? super ConsoleConfig, ? super IpMonitorConfig, ? super String, ? super List<ToolItem>, Unit> function4, Composer $composer, final int $changed) {
        MutableState mutableStateOf$default;
        MutableState mutableStateOf$default2;
        MutableState mutableStateOf$default3;
        MutableState consoleApk$delegate;
        Object mutableStateOf$default4;
        MutableState ipEnabled$delegate;
        Object mutableStateOf$default5;
        MutableState ipUrl$delegate;
        MutableState mutableStateOf$default6;
        MutableState mutableStateOf$default7;
        Composer $composer2;
        Composer $composer3 = $composer.startRestartGroup(-42229004);
        ComposerKt.sourceInformation($composer3, "C(MiscModulesEditorCard)P(1)641@24821L81,642@24926L89,643@25038L80,645@25141L85,646@25244L81,648@25347L63,649@25432L144,659@25785L5155,653@25582L5358:AppModulesAndThemeScreen.kt#2thlc2");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changedInstance(uiState) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changedInstance(function4) ? 32 : 16;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-42229004, $dirty2, -1, "com.example.ui.screens.MiscModulesEditorCard (AppModulesAndThemeScreen.kt:640)");
            }
            ConsoleConfig consoleConfig = uiState.getConsoleConfig();
            ComposerKt.sourceInformationMarkerStart($composer3, -1927358939, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed = $composer3.changed(consoleConfig);
            Object rememberedValue = $composer3.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                mutableStateOf$default = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(uiState.getConsoleConfig().getVersion(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default);
            } else {
                mutableStateOf$default = rememberedValue;
            }
            final MutableState consoleVer$delegate = mutableStateOf$default;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ConsoleConfig consoleConfig2 = uiState.getConsoleConfig();
            ComposerKt.sourceInformationMarkerStart($composer3, -1927355571, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed2 = $composer3.changed(consoleConfig2);
            Object rememberedValue2 = $composer3.rememberedValue();
            if (changed2 || rememberedValue2 == Composer.Companion.getEmpty()) {
                mutableStateOf$default2 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(String.valueOf(uiState.getConsoleConfig().getCode()), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default2);
            } else {
                mutableStateOf$default2 = rememberedValue2;
            }
            final MutableState consoleCode$delegate = mutableStateOf$default2;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ConsoleConfig consoleConfig3 = uiState.getConsoleConfig();
            ComposerKt.sourceInformationMarkerStart($composer3, -1927351996, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed3 = $composer3.changed(consoleConfig3);
            Object rememberedValue3 = $composer3.rememberedValue();
            if (changed3 || rememberedValue3 == Composer.Companion.getEmpty()) {
                mutableStateOf$default3 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(uiState.getConsoleConfig().getApkUrl(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default3);
            } else {
                mutableStateOf$default3 = rememberedValue3;
            }
            MutableState consoleApk$delegate2 = mutableStateOf$default3;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            IpMonitorConfig ipMonitorConfig = uiState.getIpMonitorConfig();
            ComposerKt.sourceInformationMarkerStart($composer3, -1927348695, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed4 = $composer3.changed(ipMonitorConfig);
            Object rememberedValue4 = $composer3.rememberedValue();
            if (changed4 || rememberedValue4 == Composer.Companion.getEmpty()) {
                consoleApk$delegate = consoleApk$delegate2;
                mutableStateOf$default4 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(Boolean.valueOf(uiState.getIpMonitorConfig().getEnabled()), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default4);
            } else {
                consoleApk$delegate = consoleApk$delegate2;
                mutableStateOf$default4 = rememberedValue4;
            }
            MutableState ipEnabled$delegate2 = (MutableState) mutableStateOf$default4;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            IpMonitorConfig ipMonitorConfig2 = uiState.getIpMonitorConfig();
            ComposerKt.sourceInformationMarkerStart($composer3, -1927345403, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed5 = $composer3.changed(ipMonitorConfig2);
            Object rememberedValue5 = $composer3.rememberedValue();
            if (changed5 || rememberedValue5 == Composer.Companion.getEmpty()) {
                ipEnabled$delegate = ipEnabled$delegate2;
                mutableStateOf$default5 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(uiState.getIpMonitorConfig().getUrl(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default5);
            } else {
                ipEnabled$delegate = ipEnabled$delegate2;
                mutableStateOf$default5 = rememberedValue5;
            }
            MutableState ipUrl$delegate2 = (MutableState) mutableStateOf$default5;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            String aiNotice = uiState.getAiNotice();
            ComposerKt.sourceInformationMarkerStart($composer3, -1927342125, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed6 = $composer3.changed(aiNotice);
            Object rememberedValue6 = $composer3.rememberedValue();
            if (changed6 || rememberedValue6 == Composer.Companion.getEmpty()) {
                ipUrl$delegate = ipUrl$delegate2;
                mutableStateOf$default6 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(uiState.getAiNotice(), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default6);
            } else {
                ipUrl$delegate = ipUrl$delegate2;
                mutableStateOf$default6 = rememberedValue6;
            }
            final MutableState aiNotice$delegate = mutableStateOf$default6;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            List<ToolItem> toolsList = uiState.getToolsList();
            ComposerKt.sourceInformationMarkerStart($composer3, -1927339324, "CC(remember):AppModulesAndThemeScreen.kt#9igjgp");
            boolean changed7 = $composer3.changed(toolsList);
            Object rememberedValue7 = $composer3.rememberedValue();
            if (changed7 || rememberedValue7 == Composer.Companion.getEmpty()) {
                mutableStateOf$default7 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(CollectionsKt.joinToString$default(uiState.getToolsList(), "\n", null, null, 0, null, new Function1() { // from class: com.example.ui.screens.AppModulesAndThemeScreenKt$$ExternalSyntheticLambda23
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return AppModulesAndThemeScreenKt.MiscModulesEditorCard$lambda$200$lambda$199((ToolItem) obj);
                    }
                }, 30, null), null, 2, null);
                $composer3.updateRememberedValue(mutableStateOf$default7);
            } else {
                mutableStateOf$default7 = rememberedValue7;
            }
            final MutableState toolsText$delegate = mutableStateOf$default7;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final MutableState consoleApk$delegate3 = consoleApk$delegate;
            final MutableState ipUrl$delegate3 = ipUrl$delegate;
            final MutableState ipEnabled$delegate3 = ipEnabled$delegate;
            $composer2 = $composer3;
            SurfaceKt.m2543SurfaceT9BRK9s(SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, null), RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(16)), ColorKt.getPaperSoft(), 0L, 0.0f, Dp.m6622constructorimpl(2), BorderStrokeKt.m252BorderStrokecXLIe8U(Dp.m6622constructorimpl(1), ColorKt.getMist()), ComposableLambdaKt.rememberComposableLambda(-2128573681, true, new Function2() { // from class: com.example.ui.screens.AppModulesAndThemeScreenKt$$ExternalSyntheticLambda24
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AppModulesAndThemeScreenKt.MiscModulesEditorCard$lambda$223(MutableState.this, ipUrl$delegate3, aiNotice$delegate, toolsText$delegate, function4, consoleVer$delegate, consoleCode$delegate, ipEnabled$delegate3, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), $composer2, 14352774, 24);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.screens.AppModulesAndThemeScreenKt$$ExternalSyntheticLambda25
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AppModulesAndThemeScreenKt.MiscModulesEditorCard$lambda$224(AdminUiState.this, function4, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    private static final String MiscModulesEditorCard$lambda$182(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String MiscModulesEditorCard$lambda$185(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String MiscModulesEditorCard$lambda$188(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final boolean MiscModulesEditorCard$lambda$191(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void MiscModulesEditorCard$lambda$192(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final String MiscModulesEditorCard$lambda$194(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String MiscModulesEditorCard$lambda$197(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String MiscModulesEditorCard$lambda$201(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final CharSequence MiscModulesEditorCard$lambda$200$lambda$199(ToolItem it) {
        Intrinsics.checkNotNullParameter(it, "it");
        String id = it.getId();
        String title = it.getTitle();
        String url = it.getUrl();
        return id + "|" + title + "|" + url + "|" + it.getDesc();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0865  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x092b  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0987  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x026f  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x027b  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0281  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x035a  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x036a  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x03fe  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x040e  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x04ae  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x04bb A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x059c  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x05a8  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x05ae  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x05df  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x05f5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0699  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x06a7  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x074d  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x07d9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit MiscModulesEditorCard$lambda$223(final androidx.compose.runtime.MutableState r107, final androidx.compose.runtime.MutableState r108, final androidx.compose.runtime.MutableState r109, final androidx.compose.runtime.MutableState r110, final kotlin.jvm.functions.Function4 r111, final androidx.compose.runtime.MutableState r112, final androidx.compose.runtime.MutableState r113, final androidx.compose.runtime.MutableState r114, androidx.compose.runtime.Composer r115, int r116) {
        /*
            Method dump skipped, instructions count: 2445
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.AppModulesAndThemeScreenKt.MiscModulesEditorCard$lambda$223(androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, kotlin.jvm.functions.Function4, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MiscModulesEditorCard$lambda$223$lambda$222$lambda$207$lambda$204$lambda$203(MutableState $consoleVer$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $consoleVer$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MiscModulesEditorCard$lambda$223$lambda$222$lambda$207$lambda$206$lambda$205(MutableState $consoleCode$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $consoleCode$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MiscModulesEditorCard$lambda$223$lambda$222$lambda$209$lambda$208(MutableState $consoleApk$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $consoleApk$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MiscModulesEditorCard$lambda$223$lambda$222$lambda$212$lambda$211$lambda$210(MutableState $ipEnabled$delegate, boolean it) {
        MiscModulesEditorCard$lambda$192($ipEnabled$delegate, it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MiscModulesEditorCard$lambda$223$lambda$222$lambda$214$lambda$213(MutableState $ipUrl$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $ipUrl$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MiscModulesEditorCard$lambda$223$lambda$222$lambda$216$lambda$215(MutableState $aiNotice$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $aiNotice$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MiscModulesEditorCard$lambda$223$lambda$222$lambda$218$lambda$217(MutableState $toolsText$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $toolsText$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit MiscModulesEditorCard$lambda$223$lambda$222$lambda$221$lambda$220(Function4 $onSave, MutableState $toolsText$delegate, MutableState $consoleVer$delegate, MutableState $consoleCode$delegate, MutableState $consoleApk$delegate, MutableState $ipEnabled$delegate, MutableState $ipUrl$delegate, MutableState $aiNotice$delegate) {
        Iterable iterable;
        boolean z;
        Iterable iterable2;
        boolean z2;
        String str;
        String str2;
        ToolItem toolItem;
        Iterable lines = StringsKt.lines(MiscModulesEditorCard$lambda$201($toolsText$delegate));
        boolean z3 = false;
        Collection arrayList = new ArrayList();
        Iterable<String> iterable3 = lines;
        boolean z4 = false;
        for (String str3 : iterable3) {
            String obj = StringsKt.trim((CharSequence) str3).toString();
            if (obj.length() == 0) {
                toolItem = null;
                iterable = lines;
                z = z3;
                iterable2 = iterable3;
                z2 = z4;
            } else {
                List split$default = StringsKt.split$default((CharSequence) obj, new String[]{"|"}, false, 0, 6, (Object) null);
                iterable = lines;
                String str4 = (String) CollectionsKt.getOrNull(split$default, 0);
                if (str4 == null || (str = StringsKt.trim((CharSequence) str4).toString()) == null) {
                    z = z3;
                    iterable2 = iterable3;
                    z2 = z4;
                    str = "tool_" + System.currentTimeMillis();
                } else {
                    z = z3;
                    iterable2 = iterable3;
                    z2 = z4;
                }
                String str5 = (String) CollectionsKt.getOrNull(split$default, 1);
                if (str5 == null || (str2 = StringsKt.trim((CharSequence) str5).toString()) == null) {
                    str2 = obj;
                }
                String str6 = (String) CollectionsKt.getOrNull(split$default, 2);
                String str7 = (str6 == null || (str7 = StringsKt.trim((CharSequence) str6).toString()) == null) ? "" : "";
                String str8 = (String) CollectionsKt.getOrNull(split$default, 3);
                toolItem = new ToolItem(str, str2, str7, (str8 == null || (r4 = StringsKt.trim((CharSequence) str8).toString()) == null) ? "" : "");
            }
            if (toolItem != null) {
                arrayList.add(toolItem);
            }
            lines = iterable;
            z3 = z;
            iterable3 = iterable2;
            z4 = z2;
        }
        ArrayList arrayList2 = (List) arrayList;
        String MiscModulesEditorCard$lambda$182 = MiscModulesEditorCard$lambda$182($consoleVer$delegate);
        Integer intOrNull = StringsKt.toIntOrNull(MiscModulesEditorCard$lambda$185($consoleCode$delegate));
        $onSave.invoke(new ConsoleConfig(MiscModulesEditorCard$lambda$182, intOrNull != null ? intOrNull.intValue() : 56, MiscModulesEditorCard$lambda$188($consoleApk$delegate)), new IpMonitorConfig(MiscModulesEditorCard$lambda$191($ipEnabled$delegate), MiscModulesEditorCard$lambda$194($ipUrl$delegate)), MiscModulesEditorCard$lambda$197($aiNotice$delegate), arrayList2);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:126:0x0503  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void ThemeKitScreen(final com.example.viewmodel.AdminUiState r66, kotlin.jvm.functions.Function1<? super com.example.model.ThemeKitConfig, kotlin.Unit> r67, androidx.compose.runtime.Composer r68, final int r69) {
        /*
            Method dump skipped, instructions count: 1310
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.AppModulesAndThemeScreenKt.ThemeKitScreen(com.example.viewmodel.AdminUiState, kotlin.jvm.functions.Function1, androidx.compose.runtime.Composer, int):void");
    }

    private static final String ThemeKitScreen$lambda$226(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ThemeKitScreen$lambda$229(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ThemeKitScreen$lambda$232(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ThemeKitScreen$lambda$235(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ThemeKitScreen$lambda$238(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ThemeKitScreen$lambda$241(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ThemeKitScreen$lambda$244(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ThemeKitScreen$lambda$247(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ThemeKitScreen$lambda$250(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ThemeKitScreen$lambda$253(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ThemeKitScreen$lambda$256(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final String ThemeKitScreen$lambda$259(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0155  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit ThemeKitScreen$lambda$290$lambda$263(final kotlin.jvm.functions.Function1 r33, final androidx.compose.runtime.MutableState r34, final androidx.compose.runtime.MutableState r35, final androidx.compose.runtime.MutableState r36, final androidx.compose.runtime.MutableState r37, final androidx.compose.runtime.MutableState r38, final androidx.compose.runtime.MutableState r39, final androidx.compose.runtime.MutableState r40, final androidx.compose.runtime.MutableState r41, final androidx.compose.runtime.MutableState r42, final androidx.compose.runtime.MutableState r43, final androidx.compose.runtime.MutableState r44, final androidx.compose.runtime.MutableState r45, androidx.compose.foundation.layout.RowScope r46, androidx.compose.runtime.Composer r47, int r48) {
        /*
            Method dump skipped, instructions count: 347
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.AppModulesAndThemeScreenKt.ThemeKitScreen$lambda$290$lambda$263(kotlin.jvm.functions.Function1, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.RowScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$290$lambda$263$lambda$262$lambda$261(Function1 $onSaveThemeKit, MutableState $appBarCss$delegate, MutableState $bottomBarCss$delegate, MutableState $splashCss$delegate, MutableState $statusBarCss$delegate, MutableState $cardCss$delegate, MutableState $buttonCss$delegate, MutableState $dialogCss$delegate, MutableState $searchCss$delegate, MutableState $globalCss$delegate, MutableState $settingsPageCss$delegate, MutableState $customThemeCss$delegate, MutableState $customThemeHtml$delegate) {
        $onSaveThemeKit.invoke(new ThemeKitConfig(ThemeKitScreen$lambda$226($appBarCss$delegate), ThemeKitScreen$lambda$229($bottomBarCss$delegate), ThemeKitScreen$lambda$232($splashCss$delegate), ThemeKitScreen$lambda$235($statusBarCss$delegate), ThemeKitScreen$lambda$238($cardCss$delegate), ThemeKitScreen$lambda$241($buttonCss$delegate), ThemeKitScreen$lambda$244($dialogCss$delegate), ThemeKitScreen$lambda$247($searchCss$delegate), ThemeKitScreen$lambda$250($globalCss$delegate), ThemeKitScreen$lambda$253($settingsPageCss$delegate), ThemeKitScreen$lambda$256($customThemeCss$delegate), ThemeKitScreen$lambda$259($customThemeHtml$delegate)));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x046a  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0477 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:108:0x04fa  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x053a  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0547 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0580  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0260  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x026e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x02a2  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x02b0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x02e4  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x02f1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0325  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0332 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0366  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0373 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x03a7  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x03b4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x03e8  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x03f5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0429  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0436 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit ThemeKitScreen$lambda$290$lambda$289(final androidx.compose.runtime.MutableState r62, final androidx.compose.runtime.MutableState r63, final androidx.compose.runtime.MutableState r64, final androidx.compose.runtime.MutableState r65, final androidx.compose.runtime.MutableState r66, final androidx.compose.runtime.MutableState r67, final androidx.compose.runtime.MutableState r68, final androidx.compose.runtime.MutableState r69, final androidx.compose.runtime.MutableState r70, final androidx.compose.runtime.MutableState r71, final androidx.compose.runtime.MutableState r72, final androidx.compose.runtime.MutableState r73, androidx.compose.runtime.Composer r74, int r75) {
        /*
            Method dump skipped, instructions count: 1414
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.screens.AppModulesAndThemeScreenKt.ThemeKitScreen$lambda$290$lambda$289(androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$290$lambda$289$lambda$288$lambda$265$lambda$264(MutableState $buttonCss$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $buttonCss$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$290$lambda$289$lambda$288$lambda$267$lambda$266(MutableState $searchCss$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $searchCss$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$290$lambda$289$lambda$288$lambda$269$lambda$268(MutableState $cardCss$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $cardCss$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$290$lambda$289$lambda$288$lambda$271$lambda$270(MutableState $appBarCss$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $appBarCss$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$290$lambda$289$lambda$288$lambda$273$lambda$272(MutableState $bottomBarCss$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $bottomBarCss$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$290$lambda$289$lambda$288$lambda$275$lambda$274(MutableState $statusBarCss$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $statusBarCss$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$290$lambda$289$lambda$288$lambda$277$lambda$276(MutableState $dialogCss$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $dialogCss$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$290$lambda$289$lambda$288$lambda$279$lambda$278(MutableState $splashCss$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $splashCss$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$290$lambda$289$lambda$288$lambda$281$lambda$280(MutableState $settingsPageCss$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $settingsPageCss$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$290$lambda$289$lambda$288$lambda$283$lambda$282(MutableState $globalCss$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $globalCss$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$290$lambda$289$lambda$288$lambda$285$lambda$284(MutableState $customThemeCss$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $customThemeCss$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ThemeKitScreen$lambda$290$lambda$289$lambda$288$lambda$287$lambda$286(MutableState $customThemeHtml$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $customThemeHtml$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    private static final void CssField(final String label, final String value, final Function1<? super String, Unit> function1, Composer $composer, final int $changed) {
        Composer $composer2;
        Composer $composer3 = $composer.startRestartGroup(1410993146);
        ComposerKt.sourceInformation($composer3, "C(CssField)P(!1,2)901@36129L10,898@36034L15,895@35937L346:AppModulesAndThemeScreen.kt#2thlc2");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(label) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changed(value) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            $dirty |= $composer3.changedInstance(function1) ? 256 : 128;
        }
        if (($dirty & 147) == 146 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1410993146, $dirty, -1, "com.example.ui.screens.CssField (AppModulesAndThemeScreen.kt:894)");
            }
            $composer2 = $composer3;
            OutlinedTextFieldKt.OutlinedTextField(value, function1, SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, null), false, false, TextStyle.m6101copyp1EtxEg$default(MaterialTheme.INSTANCE.getTypography($composer3, MaterialTheme.$stable).getBodyMedium(), 0L, 0L, null, null, null, FontFamily.Companion.getMonospace(), null, 0L, null, null, null, 0L, null, null, null, 0, 0, 0L, null, null, null, 0, 0, null, 16777183, null), (Function2<? super Composer, ? super Integer, Unit>) ComposableLambdaKt.rememberComposableLambda(-1267863788, true, new Function2() { // from class: com.example.ui.screens.AppModulesAndThemeScreenKt$$ExternalSyntheticLambda46
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AppModulesAndThemeScreenKt.CssField$lambda$292(label, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, false, 6, 2, (MutableInteractionSource) null, (Shape) RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(12)), (TextFieldColors) null, $composer2, (($dirty >> 3) & 14) | 1573248 | (($dirty >> 3) & 112), 905969664, 0, 5504920);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.screens.AppModulesAndThemeScreenKt$$ExternalSyntheticLambda47
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AppModulesAndThemeScreenKt.CssField$lambda$293(label, value, function1, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CssField$lambda$292(String $label, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C898@36036L11:AppModulesAndThemeScreen.kt#2thlc2");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1267863788, $changed, -1, "com.example.ui.screens.CssField.<anonymous> (AppModulesAndThemeScreen.kt:898)");
            }
            TextKt.m2693Text4IGK_g($label, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }
}
