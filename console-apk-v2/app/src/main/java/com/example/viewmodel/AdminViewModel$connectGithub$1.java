package com.example.viewmodel;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminViewModel.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$connectGithub$1", f = "AdminViewModel.kt", i = {0, 1, 1, 1, 1, 1, 2, 3, 3, 3, 3, 4, 4, 5, 5, 5, 5, 5}, l = {223, 228, 231, 235, 240, 244}, m = "invokeSuspend", n = {"state", "state", "content", "sha", "json", "syncTime", "state", "state", "content", "json", "syncTime", "state", "e", "state", "e", "content", "json", "syncTime"}, s = {"L$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$0", "L$0", "L$1", "L$2", "L$3", "L$0", "L$1", "L$0", "L$1", "L$2", "L$3", "L$4"})
/* loaded from: classes4.dex */
public final class AdminViewModel$connectGithub$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$connectGithub$1(AdminViewModel adminViewModel, Continuation<? super AdminViewModel$connectGithub$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$connectGithub$1(this.this$0, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$connectGithub$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x01d7 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x01d8  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0280 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0281  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0343 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0344  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0369  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0396 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0397  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r60) {
        /*
            Method dump skipped, instructions count: 1248
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$connectGithub$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
