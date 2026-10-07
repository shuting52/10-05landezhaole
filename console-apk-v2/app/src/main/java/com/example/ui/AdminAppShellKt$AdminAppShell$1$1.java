package com.example.ui;

import androidx.compose.material3.SnackbarDuration;
import androidx.compose.material3.SnackbarHostState;
import androidx.compose.runtime.State;
import androidx.core.app.NotificationCompat;
import androidx.core.view.MotionEventCompat;
import com.example.viewmodel.AdminUiState;
import com.example.viewmodel.AdminViewModel;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.text.StringsKt;
import kotlinx.coroutines.CoroutineScope;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminAppShell.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.ui.AdminAppShellKt$AdminAppShell$1$1", f = "AdminAppShell.kt", i = {0}, l = {MotionEventCompat.AXIS_GENERIC_16}, m = "invokeSuspend", n = {NotificationCompat.CATEGORY_MESSAGE}, s = {"L$0"})
/* loaded from: classes6.dex */
public final class AdminAppShellKt$AdminAppShell$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ SnackbarHostState $snackbarHostState;
    final /* synthetic */ State<AdminUiState> $uiState$delegate;
    final /* synthetic */ AdminViewModel $viewModel;
    Object L$0;
    int label;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminAppShellKt$AdminAppShell$1$1(SnackbarHostState snackbarHostState, AdminViewModel adminViewModel, State<AdminUiState> state, Continuation<? super AdminAppShellKt$AdminAppShell$1$1> continuation) {
        super(2, continuation);
        this.$snackbarHostState = snackbarHostState;
        this.$viewModel = adminViewModel;
        this.$uiState$delegate = state;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminAppShellKt$AdminAppShell$1$1(this.$snackbarHostState, this.$viewModel, this.$uiState$delegate, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminAppShellKt$AdminAppShell$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        AdminUiState AdminAppShell$lambda$0;
        String msg;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                AdminAppShell$lambda$0 = AdminAppShellKt.AdminAppShell$lambda$0(this.$uiState$delegate);
                String msg2 = AdminAppShell$lambda$0.getToastMessage();
                String str = msg2;
                if (!(str == null || StringsKt.isBlank(str))) {
                    this.L$0 = SpillingKt.nullOutSpilledVariable(msg2);
                    this.label = 1;
                    if (SnackbarHostState.showSnackbar$default(this.$snackbarHostState, msg2, null, false, SnackbarDuration.Short, this, 6, null) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    msg = msg2;
                    this.$viewModel.clearToast();
                    break;
                }
                break;
            case 1:
                msg = (String) this.L$0;
                ResultKt.throwOnFailure($result);
                this.$viewModel.clearToast();
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
