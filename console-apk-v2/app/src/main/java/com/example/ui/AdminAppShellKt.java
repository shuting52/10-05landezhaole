package com.example.ui;

import androidx.activity.compose.BackHandlerKt;
import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.foundation.layout.PaddingValues;
import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.layout.WindowInsets;
import androidx.compose.foundation.layout.WindowInsets_androidKt;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.AccountTreeKt;
import androidx.compose.material.icons.filled.AutoAwesomeKt;
import androidx.compose.material.icons.filled.DashboardKt;
import androidx.compose.material.icons.filled.LayersKt;
import androidx.compose.material.icons.filled.PaletteKt;
import androidx.compose.material.icons.filled.ReceiptLongKt;
import androidx.compose.material.icons.filled.SettingsKt;
import androidx.compose.material.icons.filled.TextFieldsKt;
import androidx.compose.material.icons.filled.TouchAppKt;
import androidx.compose.material.icons.filled.WidgetsKt;
import androidx.compose.material3.DrawerState;
import androidx.compose.material3.DrawerValue;
import androidx.compose.material3.IconKt;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.NavigationBarItemColors;
import androidx.compose.material3.NavigationBarItemDefaults;
import androidx.compose.material3.NavigationBarKt;
import androidx.compose.material3.NavigationDrawerKt;
import androidx.compose.material3.ScaffoldKt;
import androidx.compose.material3.SnackbarHostKt;
import androidx.compose.material3.SnackbarHostState;
import androidx.compose.material3.SurfaceKt;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.CompositionScopedCoroutineScopeCanceller;
import androidx.compose.runtime.EffectsKt;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.ScopeUpdateScope;
import androidx.compose.runtime.SnapshotStateKt__SnapshotStateKt;
import androidx.compose.runtime.State;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.platform.TestTagKt;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.unit.Dp;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.compose.FlowExtKt;
import com.example.model.AdminScreen;
import com.example.model.ConnState;
import com.example.ui.theme.ColorKt;
import com.example.viewmodel.AdminUiState;
import com.example.viewmodel.AdminViewModel;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: AdminAppShell.kt */
@Metadata(d1 = {"\u0000D\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\u001a\u0015\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0004\u001a?\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\rH\u0003¢\u0006\u0002\u0010\u000e\u001a[\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\u000bH\u0003¢\u0006\u0002\u0010\u0017\u001a7\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00072\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\rH\u0003¢\u0006\u0002\u0010\u001a\u001a\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0007H\u0002¨\u0006\u001e²\u0006\n\u0010\u001f\u001a\u00020 X\u008a\u0084\u0002²\u0006\n\u0010!\u001a\u00020\"X\u008a\u008e\u0002"}, d2 = {"AdminAppShell", "", "viewModel", "Lcom/example/viewmodel/AdminViewModel;", "(Lcom/example/viewmodel/AdminViewModel;Landroidx/compose/runtime/Composer;I)V", "GuochaoSidebarContent", "currentScreen", "Lcom/example/model/AdminScreen;", "versionName", "", "onSelectScreen", "Lkotlin/Function1;", "onHelpClick", "Lkotlin/Function0;", "(Lcom/example/model/AdminScreen;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "GuochaoTopBar", "title", "connState", "Lcom/example/model/ConnState;", "onOpenDrawer", "onNavigateSettings", "onApplyQuickSync", "onShowToast", "(Ljava/lang/String;Lcom/example/model/ConnState;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "GuochaoBottomBar", "onOpenMoreDrawer", "(Lcom/example/model/AdminScreen;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "screenIcon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "screen", "app", "uiState", "Lcom/example/viewmodel/AdminUiState;", "userMenuExpanded", ""}, k = 2, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class AdminAppShellKt {

    /* compiled from: AdminAppShell.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    /* loaded from: classes6.dex */
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;

        static {
            int[] iArr = new int[AdminScreen.values().length];
            try {
                iArr[AdminScreen.DASHBOARD.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[AdminScreen.CARDS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[AdminScreen.CATEGORIES.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[AdminScreen.BUTTONS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[AdminScreen.SKILLS.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[AdminScreen.APP_MODULES.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[AdminScreen.THEME_KIT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[AdminScreen.TEXTS.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[AdminScreen.SETTINGS.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[AdminScreen.LOGS.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            $EnumSwitchMapping$0 = iArr;
            int[] iArr2 = new int[ConnState.values().length];
            try {
                iArr2[ConnState.CONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr2[ConnState.READONLY.ordinal()] = 2;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr2[ConnState.LOADING.ordinal()] = 3;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr2[ConnState.ERROR.ordinal()] = 4;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr2[ConnState.IDLE.ordinal()] = 5;
            } catch (NoSuchFieldError e15) {
            }
            $EnumSwitchMapping$1 = iArr2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$70(AdminViewModel adminViewModel, int i, Composer composer, int i2) {
        AdminAppShell(adminViewModel, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoBottomBar$lambda$124(AdminScreen adminScreen, Function1 function1, Function0 function0, int i, Composer composer, int i2) {
        GuochaoBottomBar(adminScreen, function1, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoSidebarContent$lambda$88(AdminScreen adminScreen, String str, Function1 function1, Function0 function0, int i, Composer composer, int i2) {
        GuochaoSidebarContent(adminScreen, str, function1, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoTopBar$lambda$116(String str, ConnState connState, Function0 function0, Function0 function02, Function0 function03, Function1 function1, int i, Composer composer, int i2) {
        GuochaoTopBar(str, connState, function0, function02, function03, function1, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    public static final void AdminAppShell(final AdminViewModel viewModel, Composer $composer, final int $changed) {
        Composer composer;
        Object obj;
        Object obj2;
        Object obj3;
        Object obj4;
        Intrinsics.checkNotNullParameter(viewModel, "viewModel");
        Composer $composer2 = $composer.startRestartGroup(-1464452720);
        ComposerKt.sourceInformation($composer2, "C(AdminAppShell)38@1577L29,39@1629L54,40@1700L24,41@1753L32,43@1828L220,43@1791L257,51@2146L225,51@2054L317,61@2459L769,81@3235L6623,59@2377L7481:AdminAppShell.kt#naom5h");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changedInstance(viewModel) ? 4 : 2;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 3) == 2 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1464452720, $dirty2, -1, "com.example.ui.AdminAppShell (AdminAppShell.kt:37)");
            }
            final State uiState$delegate = FlowExtKt.collectAsStateWithLifecycle(viewModel.getUiState(), (LifecycleOwner) null, (Lifecycle.State) null, (CoroutineContext) null, $composer2, 0, 7);
            final DrawerState drawerState = NavigationDrawerKt.rememberDrawerState(DrawerValue.Closed, null, $composer2, 6, 2);
            ComposerKt.sourceInformationMarkerStart($composer2, 773894976, "CC(rememberCoroutineScope)482@20332L144:Effects.kt#9igjgp");
            ComposerKt.sourceInformationMarkerStart($composer2, -954367824, "CC(remember):Effects.kt#9igjgp");
            Object rememberedValue = $composer2.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                composer = $composer2;
                obj = new CompositionScopedCoroutineScopeCanceller(EffectsKt.createCompositionCoroutineScope(EmptyCoroutineContext.INSTANCE, $composer2));
                $composer2.updateRememberedValue(obj);
            } else {
                composer = $composer2;
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd(composer);
            final CoroutineScope scope = ((CompositionScopedCoroutineScopeCanceller) obj).getCoroutineScope();
            ComposerKt.sourceInformationMarkerEnd(composer);
            ComposerKt.sourceInformationMarkerStart($composer2, 1410219184, "CC(remember):AdminAppShell.kt#9igjgp");
            Object rememberedValue2 = $composer2.rememberedValue();
            if (rememberedValue2 == Composer.Companion.getEmpty()) {
                obj2 = new SnackbarHostState();
                $composer2.updateRememberedValue(obj2);
            } else {
                obj2 = rememberedValue2;
            }
            final SnackbarHostState snackbarHostState = (SnackbarHostState) obj2;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            String toastMessage = AdminAppShell$lambda$0(uiState$delegate).getToastMessage();
            ComposerKt.sourceInformationMarkerStart($composer2, 1410221772, "CC(remember):AdminAppShell.kt#9igjgp");
            boolean changed = $composer2.changed(uiState$delegate) | $composer2.changedInstance(viewModel);
            Object rememberedValue3 = $composer2.rememberedValue();
            if (changed || rememberedValue3 == Composer.Companion.getEmpty()) {
                obj3 = (Function2) new AdminAppShellKt$AdminAppShell$1$1(snackbarHostState, viewModel, uiState$delegate, null);
                $composer2.updateRememberedValue(obj3);
            } else {
                obj3 = rememberedValue3;
            }
            ComposerKt.sourceInformationMarkerEnd($composer2);
            EffectsKt.LaunchedEffect(toastMessage, (Function2) obj3, $composer2, 0);
            boolean z = drawerState.isOpen() || AdminAppShell$lambda$0(uiState$delegate).getCurrentScreen() != AdminScreen.DASHBOARD;
            ComposerKt.sourceInformationMarkerStart($composer2, 1410231953, "CC(remember):AdminAppShell.kt#9igjgp");
            boolean changed2 = $composer2.changed(drawerState) | $composer2.changedInstance(scope) | $composer2.changed(uiState$delegate) | $composer2.changedInstance(viewModel);
            Object rememberedValue4 = $composer2.rememberedValue();
            if (changed2 || rememberedValue4 == Composer.Companion.getEmpty()) {
                obj4 = new Function0() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda29
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return AdminAppShellKt.AdminAppShell$lambda$4$lambda$3(DrawerState.this, scope, viewModel, uiState$delegate);
                    }
                };
                $composer2.updateRememberedValue(obj4);
            } else {
                obj4 = rememberedValue4;
            }
            ComposerKt.sourceInformationMarkerEnd($composer2);
            BackHandlerKt.BackHandler(z, (Function0) obj4, $composer2, 0, 0);
            NavigationDrawerKt.m2285ModalNavigationDrawerFHprtrg(ComposableLambdaKt.rememberComposableLambda(798775273, true, new Function2() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda30
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj5, Object obj6) {
                    return AdminAppShellKt.AdminAppShell$lambda$10(AdminViewModel.this, scope, drawerState, uiState$delegate, (Composer) obj5, ((Integer) obj6).intValue());
                }
            }, $composer2, 54), null, drawerState, false, 0L, ComposableLambdaKt.rememberComposableLambda(1886575022, true, new Function2() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda31
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj5, Object obj6) {
                    return AdminAppShellKt.AdminAppShell$lambda$69(CoroutineScope.this, drawerState, viewModel, uiState$delegate, snackbarHostState, (Composer) obj5, ((Integer) obj6).intValue());
                }
            }, $composer2, 54), $composer2, 196614, 26);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda32
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj5, Object obj6) {
                    return AdminAppShellKt.AdminAppShell$lambda$70(AdminViewModel.this, $changed, (Composer) obj5, ((Integer) obj6).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final AdminUiState AdminAppShell$lambda$0(State<AdminUiState> state) {
        return (AdminUiState) state.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$4$lambda$3(DrawerState $drawerState, CoroutineScope $scope, AdminViewModel $viewModel, State $uiState$delegate) {
        if ($drawerState.isOpen()) {
            BuildersKt__Builders_commonKt.launch$default($scope, null, null, new AdminAppShellKt$AdminAppShell$2$1$1($drawerState, null), 3, null);
        } else if (AdminAppShell$lambda$0($uiState$delegate).getCurrentScreen() != AdminScreen.DASHBOARD) {
            $viewModel.navigateTo(AdminScreen.DASHBOARD);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$10(final AdminViewModel $viewModel, final CoroutineScope $scope, final DrawerState $drawerState, final State $uiState$delegate, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C66@2643L575,62@2473L745:AdminAppShell.kt#naom5h");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(798775273, $changed, -1, "com.example.ui.AdminAppShell.<anonymous> (AdminAppShell.kt:62)");
            }
            NavigationDrawerKt.m2284ModalDrawerSheetafqeVBk(SizeKt.m720width3ABfNKs(Modifier.Companion, Dp.m6622constructorimpl(276)), null, ColorKt.getInk(), ColorKt.getPaper(), 0.0f, null, ComposableLambdaKt.rememberComposableLambda(-147248755, true, new Function3() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda16
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return AdminAppShellKt.AdminAppShell$lambda$10$lambda$9(AdminViewModel.this, $scope, $drawerState, $uiState$delegate, (ColumnScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer, 54), $composer, 1576326, 50);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$10$lambda$9(final AdminViewModel $viewModel, final CoroutineScope $scope, final DrawerState $drawerState, State $uiState$delegate, ColumnScope ModalDrawerSheet, Composer $composer, int $changed) {
        Function1 function1;
        Function0 function0;
        Intrinsics.checkNotNullParameter(ModalDrawerSheet, "$this$ModalDrawerSheet");
        ComposerKt.sourceInformation($composer, "C70@2835L147,74@3018L168,67@2661L543:AdminAppShell.kt#naom5h");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-147248755, $changed, -1, "com.example.ui.AdminAppShell.<anonymous>.<anonymous> (AdminAppShell.kt:67)");
            }
            AdminScreen currentScreen = AdminAppShell$lambda$0($uiState$delegate).getCurrentScreen();
            String versionName = AdminAppShell$lambda$0($uiState$delegate).getVersionName();
            ComposerKt.sourceInformationMarkerStart($composer, 227873504, "CC(remember):AdminAppShell.kt#9igjgp");
            boolean changedInstance = $composer.changedInstance($viewModel) | $composer.changedInstance($scope) | $composer.changed($drawerState);
            Object rememberedValue = $composer.rememberedValue();
            if (changedInstance || rememberedValue == Composer.Companion.getEmpty()) {
                function1 = new Function1() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda3
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return AdminAppShellKt.AdminAppShell$lambda$10$lambda$9$lambda$6$lambda$5(AdminViewModel.this, $scope, $drawerState, (AdminScreen) obj);
                    }
                };
                $composer.updateRememberedValue(function1);
            } else {
                function1 = rememberedValue;
            }
            Function1 function12 = function1;
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerStart($composer, 227879381, "CC(remember):AdminAppShell.kt#9igjgp");
            boolean changedInstance2 = $composer.changedInstance($scope) | $composer.changed($drawerState) | $composer.changedInstance($viewModel);
            Object rememberedValue2 = $composer.rememberedValue();
            if (changedInstance2 || rememberedValue2 == Composer.Companion.getEmpty()) {
                function0 = new Function0() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda4
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return AdminAppShellKt.AdminAppShell$lambda$10$lambda$9$lambda$8$lambda$7(CoroutineScope.this, $viewModel, $drawerState);
                    }
                };
                $composer.updateRememberedValue(function0);
            } else {
                function0 = rememberedValue2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            GuochaoSidebarContent(currentScreen, versionName, function12, function0, $composer, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$10$lambda$9$lambda$6$lambda$5(AdminViewModel $viewModel, CoroutineScope $scope, DrawerState $drawerState, AdminScreen screen) {
        Intrinsics.checkNotNullParameter(screen, "screen");
        $viewModel.navigateTo(screen);
        BuildersKt__Builders_commonKt.launch$default($scope, null, null, new AdminAppShellKt$AdminAppShell$3$1$1$1$1($drawerState, null), 3, null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$10$lambda$9$lambda$8$lambda$7(CoroutineScope $scope, AdminViewModel $viewModel, DrawerState $drawerState) {
        BuildersKt__Builders_commonKt.launch$default($scope, null, null, new AdminAppShellKt$AdminAppShell$3$1$2$1$1($drawerState, null), 3, null);
        $viewModel.showToast("已对接 GitHub 仓库 admin-data.json 云端数据中枢");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$69(final CoroutineScope $scope, final DrawerState $drawerState, final AdminViewModel $viewModel, final State $uiState$delegate, final SnackbarHostState $snackbarHostState, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C85@3385L11,86@3419L469,96@3914L272,103@4215L1371,133@5597L4255,82@3245L6607:AdminAppShell.kt#naom5h");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1886575022, $changed, -1, "com.example.ui.AdminAppShell.<anonymous> (AdminAppShell.kt:82)");
            }
            ScaffoldKt.m2408ScaffoldTvnljyQ(SizeKt.fillMaxSize$default(Modifier.Companion, 0.0f, 1, null), ComposableLambdaKt.rememberComposableLambda(1964846194, true, new Function2() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda25
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AdminAppShellKt.AdminAppShell$lambda$69$lambda$19(CoroutineScope.this, $drawerState, $viewModel, $uiState$delegate, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer, 54), ComposableLambdaKt.rememberComposableLambda(-2137189901, true, new Function2() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda26
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AdminAppShellKt.AdminAppShell$lambda$69$lambda$24(AdminViewModel.this, $scope, $drawerState, $uiState$delegate, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer, 54), ComposableLambdaKt.rememberComposableLambda(-1944258700, true, new Function2() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda27
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AdminAppShellKt.AdminAppShell$lambda$69$lambda$25(SnackbarHostState.this, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer, 54), null, 0, ColorKt.getPaper(), 0L, WindowInsets_androidKt.getSafeDrawing(WindowInsets.Companion, $composer, 6), ComposableLambdaKt.rememberComposableLambda(1748348349, true, new Function3() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda28
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return AdminAppShellKt.AdminAppShell$lambda$69$lambda$68(AdminViewModel.this, $uiState$delegate, (PaddingValues) obj, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer, 54), $composer, 806882742, 176);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$69$lambda$19(final CoroutineScope $scope, final DrawerState $drawerState, final AdminViewModel $viewModel, State $uiState$delegate, Composer $composer, int $changed) {
        Function0 function0;
        Object obj;
        Function0 function02;
        Function1 function1;
        ComposerKt.sourceInformation($composer, "C90@3595L39,91@3677L46,92@3764L29,93@3829L27,87@3437L437:AdminAppShell.kt#naom5h");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1964846194, $changed, -1, "com.example.ui.AdminAppShell.<anonymous>.<anonymous> (AdminAppShell.kt:87)");
            }
            String title = AdminAppShell$lambda$0($uiState$delegate).getCurrentScreen().getTitle();
            ConnState connState = AdminAppShell$lambda$0($uiState$delegate).getConnState();
            ComposerKt.sourceInformationMarkerStart($composer, -1568412135, "CC(remember):AdminAppShell.kt#9igjgp");
            boolean changedInstance = $composer.changedInstance($scope) | $composer.changed($drawerState);
            Object rememberedValue = $composer.rememberedValue();
            if (changedInstance || rememberedValue == Composer.Companion.getEmpty()) {
                function0 = new Function0() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda17
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return AdminAppShellKt.AdminAppShell$lambda$69$lambda$19$lambda$12$lambda$11(CoroutineScope.this, $drawerState);
                    }
                };
                $composer.updateRememberedValue(function0);
            } else {
                function0 = rememberedValue;
            }
            Function0 function03 = function0;
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerStart($composer, -1568409504, "CC(remember):AdminAppShell.kt#9igjgp");
            boolean changedInstance2 = $composer.changedInstance($viewModel);
            Object rememberedValue2 = $composer.rememberedValue();
            if (changedInstance2 || rememberedValue2 == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda18
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return AdminAppShellKt.AdminAppShell$lambda$69$lambda$19$lambda$14$lambda$13(AdminViewModel.this);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue2;
            }
            Function0 function04 = (Function0) obj;
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerStart($composer, -1568406737, "CC(remember):AdminAppShell.kt#9igjgp");
            boolean changedInstance3 = $composer.changedInstance($viewModel);
            Object rememberedValue3 = $composer.rememberedValue();
            if (changedInstance3 || rememberedValue3 == Composer.Companion.getEmpty()) {
                function02 = new Function0() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda19
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return AdminAppShellKt.AdminAppShell$lambda$69$lambda$19$lambda$16$lambda$15(AdminViewModel.this);
                    }
                };
                $composer.updateRememberedValue(function02);
            } else {
                function02 = rememberedValue3;
            }
            Function0 function05 = function02;
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerStart($composer, -1568404659, "CC(remember):AdminAppShell.kt#9igjgp");
            boolean changedInstance4 = $composer.changedInstance($viewModel);
            Object rememberedValue4 = $composer.rememberedValue();
            if (changedInstance4 || rememberedValue4 == Composer.Companion.getEmpty()) {
                function1 = new Function1() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda20
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return AdminAppShellKt.AdminAppShell$lambda$69$lambda$19$lambda$18$lambda$17(AdminViewModel.this, (String) obj2);
                    }
                };
                $composer.updateRememberedValue(function1);
            } else {
                function1 = rememberedValue4;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            GuochaoTopBar(title, connState, function03, function04, function05, function1, $composer, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$69$lambda$19$lambda$12$lambda$11(CoroutineScope $scope, DrawerState $drawerState) {
        BuildersKt__Builders_commonKt.launch$default($scope, null, null, new AdminAppShellKt$AdminAppShell$4$1$1$1$1($drawerState, null), 3, null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$69$lambda$19$lambda$14$lambda$13(AdminViewModel $viewModel) {
        $viewModel.navigateTo(AdminScreen.SETTINGS);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$69$lambda$19$lambda$16$lambda$15(AdminViewModel $viewModel) {
        $viewModel.applyToDevice();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$69$lambda$19$lambda$18$lambda$17(AdminViewModel $viewModel, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $viewModel.showToast(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$69$lambda$24(final AdminViewModel $viewModel, final CoroutineScope $scope, final DrawerState $drawerState, State $uiState$delegate, Composer $composer, int $changed) {
        Object obj;
        Object obj2;
        ComposerKt.sourceInformation($composer, "C99@4046L28,100@4115L39,97@3932L240:AdminAppShell.kt#naom5h");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-2137189901, $changed, -1, "com.example.ui.AdminAppShell.<anonymous>.<anonymous> (AdminAppShell.kt:97)");
            }
            AdminScreen currentScreen = AdminAppShell$lambda$0($uiState$delegate).getCurrentScreen();
            ComposerKt.sourceInformationMarkerStart($composer, -845123121, "CC(remember):AdminAppShell.kt#9igjgp");
            boolean changedInstance = $composer.changedInstance($viewModel);
            Object rememberedValue = $composer.rememberedValue();
            if (changedInstance || rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function1() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda5
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj3) {
                        return AdminAppShellKt.AdminAppShell$lambda$69$lambda$24$lambda$21$lambda$20(AdminViewModel.this, (AdminScreen) obj3);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            Function1 function1 = (Function1) obj;
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerStart($composer, -845120902, "CC(remember):AdminAppShell.kt#9igjgp");
            boolean changedInstance2 = $composer.changedInstance($scope) | $composer.changed($drawerState);
            Object rememberedValue2 = $composer.rememberedValue();
            if (changedInstance2 || rememberedValue2 == Composer.Companion.getEmpty()) {
                obj2 = new Function0() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda6
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return AdminAppShellKt.AdminAppShell$lambda$69$lambda$24$lambda$23$lambda$22(CoroutineScope.this, $drawerState);
                    }
                };
                $composer.updateRememberedValue(obj2);
            } else {
                obj2 = rememberedValue2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            GuochaoBottomBar(currentScreen, function1, (Function0) obj2, $composer, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$69$lambda$24$lambda$21$lambda$20(AdminViewModel $viewModel, AdminScreen it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $viewModel.navigateTo(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$69$lambda$24$lambda$23$lambda$22(CoroutineScope $scope, DrawerState $drawerState) {
        BuildersKt__Builders_commonKt.launch$default($scope, null, null, new AdminAppShellKt$AdminAppShell$4$2$2$1$1($drawerState, null), 3, null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$69$lambda$25(SnackbarHostState $snackbarHostState, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C104@4233L1339:AdminAppShell.kt#naom5h");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1944258700, $changed, -1, "com.example.ui.AdminAppShell.<anonymous>.<anonymous> (AdminAppShell.kt:104)");
            }
            SnackbarHostKt.SnackbarHost($snackbarHostState, null, ComposableSingletons$AdminAppShellKt.INSTANCE.getLambda$1790191425$app(), $composer, 390, 2);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0381 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:104:0x03b3  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x03c1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x03f1  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x03fe A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0428  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x04cf  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x04dc A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0506  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0562  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x060b  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0619 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:179:0x064b  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0659 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:187:0x0689  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0696 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x06c0  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x0769  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0777 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:220:0x07a7  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x07b4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x07e0  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0889  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x0897 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:253:0x08c7  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x08d4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:260:0x0900  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x09a9  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x09b7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:286:0x09e9  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x09f7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:294:0x0a27  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x0a34 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:301:0x0a5e  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x0b05  */
    /* JADX WARN: Removed duplicated region for block: B:323:0x0b12 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:326:0x0b3c  */
    /* JADX WARN: Removed duplicated region for block: B:344:0x0be5  */
    /* JADX WARN: Removed duplicated region for block: B:348:0x0bf3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:352:0x0c25  */
    /* JADX WARN: Removed duplicated region for block: B:356:0x0c33 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:360:0x0c63  */
    /* JADX WARN: Removed duplicated region for block: B:364:0x0c70 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:369:0x0cb5  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0175  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0277  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0285 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x02b3  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02c1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x02f3  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0301 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0333  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0341 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0373  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit AdminAppShell$lambda$69$lambda$68(final com.example.viewmodel.AdminViewModel r41, androidx.compose.runtime.State r42, androidx.compose.foundation.layout.PaddingValues r43, androidx.compose.runtime.Composer r44, int r45) {
        /*
            Method dump skipped, instructions count: 3284
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.AdminAppShellKt.AdminAppShell$lambda$69$lambda$68(com.example.viewmodel.AdminViewModel, androidx.compose.runtime.State, androidx.compose.foundation.layout.PaddingValues, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AdminAppShell$lambda$69$lambda$68$lambda$67$lambda$59$lambda$58(AdminViewModel $viewModel) {
        $viewModel.loadAdmin(false);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x06af  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0764  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0769  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0868  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0874  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x087a  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x08ad  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x08c3  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0972  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0974  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x097f  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x098c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0a41  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0a4d  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0a53  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0a84  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0a9a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:167:0x0c14  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x07be A[EDGE_INSN: B:172:0x07be->B:123:0x07be ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x025a  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x026c  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x03b8  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x03c4  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x03ca  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x03fd  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0413 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x05e9  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x05f5  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x05fb  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x062e  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0644 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static final void GuochaoSidebarContent(final com.example.model.AdminScreen r111, final java.lang.String r112, final kotlin.jvm.functions.Function1<? super com.example.model.AdminScreen, kotlin.Unit> r113, final kotlin.jvm.functions.Function0<kotlin.Unit> r114, androidx.compose.runtime.Composer r115, final int r116) {
        /*
            Method dump skipped, instructions count: 3118
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.AdminAppShellKt.GuochaoSidebarContent(com.example.model.AdminScreen, java.lang.String, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoSidebarContent$lambda$87$lambda$78$lambda$77$lambda$74$lambda$73(Function1 $onSelectScreen, AdminScreen $screen) {
        $onSelectScreen.invoke($screen);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0161  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01ae  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x01bb  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01c0  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0220  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit GuochaoSidebarContent$lambda$87$lambda$78$lambda$77$lambda$76(com.example.model.AdminScreen r49, boolean r50, androidx.compose.ui.graphics.vector.ImageVector r51, androidx.compose.runtime.Composer r52, int r53) {
        /*
            Method dump skipped, instructions count: 550
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.AdminAppShellKt.GuochaoSidebarContent$lambda$87$lambda$78$lambda$77$lambda$76(com.example.model.AdminScreen, boolean, androidx.compose.ui.graphics.vector.ImageVector, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoSidebarContent$lambda$87$lambda$86$lambda$80$lambda$79(Function0 $onHelpClick) {
        $onHelpClick.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01f3  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0205  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x036e  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x037a  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0380  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x03b1  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x03c7  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x04d4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit GuochaoSidebarContent$lambda$87$lambda$86$lambda$85(java.lang.String r77, androidx.compose.runtime.Composer r78, int r79) {
        /*
            Method dump skipped, instructions count: 1242
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.AdminAppShellKt.GuochaoSidebarContent$lambda$87$lambda$86$lambda$85(java.lang.String, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    private static final void GuochaoTopBar(final String title, final ConnState connState, final Function0<Unit> function0, final Function0<Unit> function02, final Function0<Unit> function03, final Function1<? super String, Unit> function1, Composer $composer, final int $changed) {
        final String str;
        final Function0<Unit> function04;
        final Function0<Unit> function05;
        final Function0<Unit> function06;
        Object obj;
        Object obj2;
        final String connBadgeLabel;
        long m4157copywmQWz5c;
        Composer $composer2;
        Composer $composer3 = $composer.startRestartGroup(-170688030);
        ComposerKt.sourceInformation($composer3, "C(GuochaoTopBar)P(5!1,3,2)406@16928L34,422@17438L9320,418@17305L9453:AdminAppShell.kt#naom5h");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            str = title;
            $dirty |= $composer3.changed(str) ? 4 : 2;
        } else {
            str = title;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changed(connState.ordinal()) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            function04 = function0;
            $dirty |= $composer3.changedInstance(function04) ? 256 : 128;
        } else {
            function04 = function0;
        }
        if (($changed & 3072) == 0) {
            function05 = function02;
            $dirty |= $composer3.changedInstance(function05) ? 2048 : 1024;
        } else {
            function05 = function02;
        }
        if (($changed & 24576) == 0) {
            function06 = function03;
            $dirty |= $composer3.changedInstance(function06) ? 16384 : 8192;
        } else {
            function06 = function03;
        }
        if ((196608 & $changed) == 0) {
            obj = function1;
            $dirty |= $composer3.changedInstance(obj) ? 131072 : 65536;
        } else {
            obj = function1;
        }
        if ((74899 & $dirty) == 74898 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-170688030, $dirty, -1, "com.example.ui.GuochaoTopBar (AdminAppShell.kt:405)");
            }
            ComposerKt.sourceInformationMarkerStart($composer3, -822431548, "CC(remember):AdminAppShell.kt#9igjgp");
            Object rememberedValue = $composer3.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj2 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(false, null, 2, null);
                $composer3.updateRememberedValue(obj2);
            } else {
                obj2 = rememberedValue;
            }
            final MutableState userMenuExpanded$delegate = (MutableState) obj2;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            switch (WhenMappings.$EnumSwitchMapping$1[connState.ordinal()]) {
                case 1:
                    connBadgeLabel = "云端已连接";
                    break;
                case 2:
                    connBadgeLabel = "只读模式";
                    break;
                case 3:
                    connBadgeLabel = "连接中…";
                    break;
                case 4:
                    connBadgeLabel = "连接异常";
                    break;
                case 5:
                    connBadgeLabel = "未连接";
                    break;
                default:
                    throw new NoWhenBranchMatchedException();
            }
            final boolean isConnected = connState == ConnState.CONNECTED;
            final boolean isReadonly = connState == ConnState.READONLY;
            Modifier fillMaxWidth$default = SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, null);
            m4157copywmQWz5c = Color.m4157copywmQWz5c(r22, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r22) : 0.95f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r22) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r22) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getPaper()) : 0.0f);
            final Function1<? super String, Unit> function12 = obj;
            $composer2 = $composer3;
            SurfaceKt.m2543SurfaceT9BRK9s(fillMaxWidth$default, null, m4157copywmQWz5c, 0L, 0.0f, Dp.m6622constructorimpl(1), null, ComposableLambdaKt.rememberComposableLambda(-686205593, true, new Function2() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda34
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj3, Object obj4) {
                    return AdminAppShellKt.GuochaoTopBar$lambda$115(Function0.this, str, function06, function05, isConnected, isReadonly, connBadgeLabel, userMenuExpanded$delegate, function12, (Composer) obj3, ((Integer) obj4).intValue());
                }
            }, $composer3, 54), $composer2, 12779910, 90);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda35
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj3, Object obj4) {
                    return AdminAppShellKt.GuochaoTopBar$lambda$116(title, connState, function0, function02, function03, function1, $changed, (Composer) obj3, ((Integer) obj4).intValue());
                }
            });
        }
    }

    private static final boolean GuochaoTopBar$lambda$90(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void GuochaoTopBar$lambda$91(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0768  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x077e  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x07ff  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x080d  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0877  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0885  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0935  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01f2  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01f8  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0324  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0330  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0336  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0369  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x037f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x04b8  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x04c4  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x04ca  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x04fd  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0513 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0588  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0598  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0620  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x062e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0665  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x067d  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0725  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0731  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0737  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit GuochaoTopBar$lambda$115(kotlin.jvm.functions.Function0 r115, java.lang.String r116, final kotlin.jvm.functions.Function0 r117, final kotlin.jvm.functions.Function0 r118, final boolean r119, final boolean r120, final java.lang.String r121, final androidx.compose.runtime.MutableState r122, final kotlin.jvm.functions.Function1 r123, androidx.compose.runtime.Composer r124, int r125) {
        /*
            Method dump skipped, instructions count: 2363
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.AdminAppShellKt.GuochaoTopBar$lambda$115(kotlin.jvm.functions.Function0, java.lang.String, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, boolean, boolean, java.lang.String, androidx.compose.runtime.MutableState, kotlin.jvm.functions.Function1, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoTopBar$lambda$115$lambda$114$lambda$113$lambda$112$lambda$94$lambda$93(Function0 $onApplyQuickSync) {
        $onApplyQuickSync.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoTopBar$lambda$115$lambda$114$lambda$113$lambda$112$lambda$96$lambda$95(Function0 $onNavigateSettings) {
        $onNavigateSettings.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x014f  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0190  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x01f5  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01fc  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x026e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit GuochaoTopBar$lambda$115$lambda$114$lambda$113$lambda$112$lambda$98(boolean r51, boolean r52, java.lang.String r53, androidx.compose.runtime.Composer r54, int r55) {
        /*
            Method dump skipped, instructions count: 628
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.AdminAppShellKt.GuochaoTopBar$lambda$115$lambda$114$lambda$113$lambda$112$lambda$98(boolean, boolean, java.lang.String, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoTopBar$lambda$115$lambda$114$lambda$113$lambda$112$lambda$111$lambda$100$lambda$99(MutableState $userMenuExpanded$delegate) {
        GuochaoTopBar$lambda$91($userMenuExpanded$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoTopBar$lambda$115$lambda$114$lambda$113$lambda$112$lambda$111$lambda$102$lambda$101(MutableState $userMenuExpanded$delegate) {
        GuochaoTopBar$lambda$91($userMenuExpanded$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:49:0x02f1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit GuochaoTopBar$lambda$115$lambda$114$lambda$113$lambda$112$lambda$111$lambda$110(final kotlin.jvm.functions.Function0 r56, final kotlin.jvm.functions.Function0 r57, final kotlin.jvm.functions.Function1 r58, final androidx.compose.runtime.MutableState r59, androidx.compose.foundation.layout.ColumnScope r60, androidx.compose.runtime.Composer r61, int r62) {
        /*
            Method dump skipped, instructions count: 759
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.AdminAppShellKt.GuochaoTopBar$lambda$115$lambda$114$lambda$113$lambda$112$lambda$111$lambda$110(kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function1, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoTopBar$lambda$115$lambda$114$lambda$113$lambda$112$lambda$111$lambda$110$lambda$105$lambda$104(Function0 $onNavigateSettings, MutableState $userMenuExpanded$delegate) {
        GuochaoTopBar$lambda$91($userMenuExpanded$delegate, false);
        $onNavigateSettings.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoTopBar$lambda$115$lambda$114$lambda$113$lambda$112$lambda$111$lambda$110$lambda$107$lambda$106(Function0 $onApplyQuickSync, MutableState $userMenuExpanded$delegate) {
        GuochaoTopBar$lambda$91($userMenuExpanded$delegate, false);
        $onApplyQuickSync.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoTopBar$lambda$115$lambda$114$lambda$113$lambda$112$lambda$111$lambda$110$lambda$109$lambda$108(Function1 $onShowToast, MutableState $userMenuExpanded$delegate) {
        GuochaoTopBar$lambda$91($userMenuExpanded$delegate, false);
        $onShowToast.invoke("演示环境：退出登录已模拟");
        return Unit.INSTANCE;
    }

    private static final void GuochaoBottomBar(final AdminScreen currentScreen, final Function1<? super AdminScreen, Unit> function1, final Function0<Unit> function0, Composer $composer, final int $changed) {
        Composer $composer2 = $composer.startRestartGroup(1318761265);
        ComposerKt.sourceInformation($composer2, "C(GuochaoBottomBar)P(!1,2)636@27197L2404,632@27077L2524:AdminAppShell.kt#naom5h");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changed(currentScreen.ordinal()) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer2.changedInstance(function1) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            $dirty |= $composer2.changedInstance(function0) ? 256 : 128;
        }
        if (($dirty & 147) == 146 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1318761265, $dirty, -1, "com.example.ui.GuochaoBottomBar (AdminAppShell.kt:624)");
            }
            final List primaryTabs = CollectionsKt.listOf((Object[]) new AdminScreen[]{AdminScreen.DASHBOARD, AdminScreen.CARDS, AdminScreen.APP_MODULES, AdminScreen.SETTINGS});
            NavigationBarKt.m2273NavigationBarHsRjFd4(null, ColorKt.getPaperSoft(), ColorKt.getInkBlack(), Dp.m6622constructorimpl(4), null, ComposableLambdaKt.rememberComposableLambda(259728234, true, new Function3() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda21
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return AdminAppShellKt.GuochaoBottomBar$lambda$123(primaryTabs, currentScreen, function0, function1, (RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer2, 54), $composer2, 200112, 17);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda23
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AdminAppShellKt.GuochaoBottomBar$lambda$124(AdminScreen.this, function1, function0, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoBottomBar$lambda$123(List $primaryTabs, final AdminScreen $currentScreen, Function0 $onOpenMoreDrawer, final Function1 $onSelectScreen, RowScope NavigationBar, Composer $composer, int $changed) {
        long m4157copywmQWz5c;
        long m4157copywmQWz5c2;
        long m4157copywmQWz5c3;
        long m4157copywmQWz5c4;
        Function0 function0;
        final AdminScreen adminScreen;
        AdminScreen adminScreen2 = $currentScreen;
        Intrinsics.checkNotNullParameter(NavigationBar, "$this$NavigationBar");
        ComposerKt.sourceInformation($composer, "C687@29239L286,680@28888L302,671@28591L1004:AdminAppShell.kt#naom5h");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer.changed(NavigationBar) ? 4 : 2;
        }
        if (($dirty & 19) == 18 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(259728234, $dirty, -1, "com.example.ui.GuochaoBottomBar.<anonymous> (AdminAppShell.kt:637)");
            }
            $composer.startReplaceGroup(490731712);
            ComposerKt.sourceInformation($composer, "*659@28112L310,645@27530L26,646@27581L183,652@27790L269,643@27448L1063");
            Iterator it = $primaryTabs.iterator();
            while (it.hasNext()) {
                AdminScreen adminScreen3 = (AdminScreen) it.next();
                boolean z = adminScreen2 == adminScreen3;
                String title = WhenMappings.$EnumSwitchMapping$0[adminScreen3.ordinal()] == 6 ? "本体模块" : adminScreen3.getTitle();
                int $dirty2 = $dirty;
                NavigationBarItemDefaults navigationBarItemDefaults = NavigationBarItemDefaults.INSTANCE;
                long paper = ColorKt.getPaper();
                final boolean z2 = z;
                long cinnabar = ColorKt.getCinnabar();
                final String str = title;
                long cinnabar2 = ColorKt.getCinnabar();
                m4157copywmQWz5c3 = Color.m4157copywmQWz5c(r26, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r26) : 0.6f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r26) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r26) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
                m4157copywmQWz5c4 = Color.m4157copywmQWz5c(r26, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r26) : 0.6f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r26) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r26) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
                NavigationBarItemColors m2271colors69fazGs = navigationBarItemDefaults.m2271colors69fazGs(paper, cinnabar, cinnabar2, m4157copywmQWz5c3, m4157copywmQWz5c4, 0L, 0L, $composer, (NavigationBarItemDefaults.$stable << 21) | 28086, 96);
                Modifier testTag = TestTagKt.testTag(Modifier.Companion, "bottom_nav_" + adminScreen3.getRoute());
                ComposerKt.sourceInformationMarkerStart($composer, 176545952, "CC(remember):AdminAppShell.kt#9igjgp");
                boolean changed = $composer.changed($onSelectScreen) | $composer.changed(adminScreen3.ordinal());
                Object rememberedValue = $composer.rememberedValue();
                if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                    adminScreen = adminScreen3;
                    function0 = new Function0() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda0
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return AdminAppShellKt.GuochaoBottomBar$lambda$123$lambda$121$lambda$118$lambda$117(Function1.this, adminScreen);
                        }
                    };
                    $composer.updateRememberedValue(function0);
                } else {
                    function0 = rememberedValue;
                    adminScreen = adminScreen3;
                }
                ComposerKt.sourceInformationMarkerEnd($composer);
                NavigationBarKt.NavigationBarItem(NavigationBar, z2, function0, ComposableLambdaKt.rememberComposableLambda(576077537, true, new Function2() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda11
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj, Object obj2) {
                        return AdminAppShellKt.GuochaoBottomBar$lambda$123$lambda$121$lambda$119(AdminScreen.this, (Composer) obj, ((Integer) obj2).intValue());
                    }
                }, $composer, 54), testTag, false, ComposableLambdaKt.rememberComposableLambda(-1523950428, true, new Function2() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda22
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj, Object obj2) {
                        return AdminAppShellKt.GuochaoBottomBar$lambda$123$lambda$121$lambda$120(z2, str, (Composer) obj, ((Integer) obj2).intValue());
                    }
                }, $composer, 54), false, m2271colors69fazGs, null, $composer, 1575936 | ($dirty2 & 14), 336);
                adminScreen2 = $currentScreen;
                $dirty = $dirty2;
            }
            int $dirty3 = $dirty;
            $composer.endReplaceGroup();
            final boolean isOtherSelected = !$primaryTabs.contains($currentScreen);
            NavigationBarItemDefaults navigationBarItemDefaults2 = NavigationBarItemDefaults.INSTANCE;
            long paper2 = ColorKt.getPaper();
            long cinnabar3 = ColorKt.getCinnabar();
            long cinnabar4 = ColorKt.getCinnabar();
            m4157copywmQWz5c = Color.m4157copywmQWz5c(r10, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r10) : 0.6f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r10) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r10) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
            m4157copywmQWz5c2 = Color.m4157copywmQWz5c(r12, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r12) : 0.6f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r12) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r12) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(ColorKt.getInkBlack()) : 0.0f);
            NavigationBarKt.NavigationBarItem(NavigationBar, isOtherSelected, $onOpenMoreDrawer, ComposableSingletons$AdminAppShellKt.INSTANCE.m6928getLambda$901034587$app(), TestTagKt.testTag(Modifier.Companion, "bottom_nav_more"), false, ComposableLambdaKt.rememberComposableLambda(-2125105432, true, new Function2() { // from class: com.example.ui.AdminAppShellKt$$ExternalSyntheticLambda33
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AdminAppShellKt.GuochaoBottomBar$lambda$123$lambda$122(isOtherSelected, $currentScreen, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer, 54), false, navigationBarItemDefaults2.m2271colors69fazGs(paper2, cinnabar3, cinnabar4, m4157copywmQWz5c, m4157copywmQWz5c2, 0L, 0L, $composer, (NavigationBarItemDefaults.$stable << 21) | 28086, 96), null, $composer, ($dirty3 & 14) | 1600512, 336);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoBottomBar$lambda$123$lambda$121$lambda$118$lambda$117(Function1 $onSelectScreen, AdminScreen $screen) {
        $onSelectScreen.invoke($screen);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoBottomBar$lambda$123$lambda$121$lambda$119(AdminScreen $screen, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C647@27603L143:AdminAppShell.kt#naom5h");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(576077537, $changed, -1, "com.example.ui.GuochaoBottomBar.<anonymous>.<anonymous>.<anonymous> (AdminAppShell.kt:647)");
            }
            IconKt.m2150Iconww6aTOc(screenIcon($screen), $screen.getTitle(), (Modifier) null, 0L, $composer, 0, 12);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoBottomBar$lambda$123$lambda$121$lambda$120(boolean $selected, String $shortTitle, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C655@27907L10,653@27812L229:AdminAppShell.kt#naom5h");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1523950428, $changed, -1, "com.example.ui.GuochaoBottomBar.<anonymous>.<anonymous>.<anonymous> (AdminAppShell.kt:653)");
            }
            TextStyle labelSmall = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall();
            FontWeight.Companion companion = FontWeight.Companion;
            TextKt.m2693Text4IGK_g($shortTitle, (Modifier) null, 0L, 0L, (FontStyle) null, $selected ? companion.getBold() : companion.getMedium(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, labelSmall, $composer, 0, 0, 65502);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GuochaoBottomBar$lambda$123$lambda$122(boolean $isOtherSelected, AdminScreen $currentScreen, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C683@29043L10,681@28906L270:AdminAppShell.kt#naom5h");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-2125105432, $changed, -1, "com.example.ui.GuochaoBottomBar.<anonymous>.<anonymous> (AdminAppShell.kt:681)");
            }
            String take = $isOtherSelected ? StringsKt.take($currentScreen.getTitle(), 4) : "全部模块";
            TextStyle labelSmall = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall();
            FontWeight.Companion companion = FontWeight.Companion;
            TextKt.m2693Text4IGK_g(take, (Modifier) null, 0L, 0L, (FontStyle) null, $isOtherSelected ? companion.getBold() : companion.getMedium(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, labelSmall, $composer, 0, 0, 65502);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    private static final ImageVector screenIcon(AdminScreen screen) {
        switch (WhenMappings.$EnumSwitchMapping$0[screen.ordinal()]) {
            case 1:
                return DashboardKt.getDashboard(Icons.INSTANCE.getDefault());
            case 2:
                return LayersKt.getLayers(Icons.INSTANCE.getDefault());
            case 3:
                return AccountTreeKt.getAccountTree(Icons.INSTANCE.getDefault());
            case 4:
                return TouchAppKt.getTouchApp(Icons.INSTANCE.getDefault());
            case 5:
                return AutoAwesomeKt.getAutoAwesome(Icons.INSTANCE.getDefault());
            case 6:
                return WidgetsKt.getWidgets(Icons.INSTANCE.getDefault());
            case 7:
                return PaletteKt.getPalette(Icons.INSTANCE.getDefault());
            case 8:
                return TextFieldsKt.getTextFields(Icons.INSTANCE.getDefault());
            case 9:
                return SettingsKt.getSettings(Icons.INSTANCE.getDefault());
            case 10:
                return ReceiptLongKt.getReceiptLong(Icons.INSTANCE.getDefault());
            default:
                throw new NoWhenBranchMatchedException();
        }
    }
}
