package com.example.viewmodel;

import com.example.model.TextItem;
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
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$deleteText$1", f = "AdminViewModel.kt", i = {0, 0}, l = {1233}, m = "invokeSuspend", n = {"json", "uiText"}, s = {"L$0", "L$1"})
/* loaded from: classes4.dex */
public final class AdminViewModel$deleteText$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ TextItem $item;
    Object L$0;
    Object L$1;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$deleteText$1(AdminViewModel adminViewModel, TextItem textItem, Continuation<? super AdminViewModel$deleteText$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
        this.$item = textItem;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$deleteText$1(this.this$0, this.$item, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$deleteText$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00bf A[Catch: Exception -> 0x0022, TRY_ENTER, TryCatch #1 {Exception -> 0x0022, blocks: (B:7:0x001b, B:24:0x00b3, B:27:0x00bf, B:29:0x00f2, B:28:0x00d9), top: B:39:0x001b }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00d9 A[Catch: Exception -> 0x0022, TryCatch #1 {Exception -> 0x0022, blocks: (B:7:0x001b, B:24:0x00b3, B:27:0x00bf, B:29:0x00f2, B:28:0x00d9), top: B:39:0x001b }] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r11) {
        /*
            Method dump skipped, instructions count: 302
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$deleteText$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
