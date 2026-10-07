package com.example.viewmodel;

import com.example.model.ResourceCard;
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
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$deleteCard$1", f = "AdminViewModel.kt", i = {0, 0, 0, 0, 0, 0, 0}, l = {925}, m = "invokeSuspend", n = {"json", "catsArr", "c", "arr", "item", "i", "j"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "I$0", "I$1"})
/* loaded from: classes4.dex */
public final class AdminViewModel$deleteCard$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ ResourceCard $card;
    int I$0;
    int I$1;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$deleteCard$1(AdminViewModel adminViewModel, ResourceCard resourceCard, Continuation<? super AdminViewModel$deleteCard$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
        this.$card = resourceCard;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$deleteCard$1(this.this$0, this.$card, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$deleteCard$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x0120 A[Catch: Exception -> 0x0030, TRY_ENTER, TryCatch #0 {Exception -> 0x0030, blocks: (B:7:0x0029, B:40:0x0114, B:43:0x0120, B:45:0x0153, B:44:0x013a), top: B:56:0x0029 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x013a A[Catch: Exception -> 0x0030, TryCatch #0 {Exception -> 0x0030, blocks: (B:7:0x0029, B:40:0x0114, B:43:0x0120, B:45:0x0153, B:44:0x013a), top: B:56:0x0029 }] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r15) {
        /*
            Method dump skipped, instructions count: 408
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$deleteCard$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
