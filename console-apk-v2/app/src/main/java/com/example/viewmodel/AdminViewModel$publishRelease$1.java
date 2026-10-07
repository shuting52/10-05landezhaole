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
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$publishRelease$1", f = "AdminViewModel.kt", i = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, l = {1650}, m = "invokeSuspend", n = {"json", "verObj", "oldName", "parts", "newName", "oldChangelog", "newChangelog", "updDialog", "ar", "lastRel", "oldCode", "newCode"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "I$0", "I$1"})
/* loaded from: classes4.dex */
public final class AdminViewModel$publishRelease$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ String $changeDesc;
    int I$0;
    int I$1;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    Object L$6;
    Object L$7;
    Object L$8;
    Object L$9;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$publishRelease$1(AdminViewModel adminViewModel, String str, Continuation<? super AdminViewModel$publishRelease$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
        this.$changeDesc = str;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$publishRelease$1(this.this$0, this.$changeDesc, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$publishRelease$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:71:0x036c A[Catch: Exception -> 0x041a, all -> 0x04b5, TRY_ENTER, TryCatch #1 {all -> 0x04b5, blocks: (B:68:0x0360, B:71:0x036c, B:73:0x039b, B:72:0x0384, B:79:0x041b, B:64:0x02ad), top: B:94:0x001a }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0384 A[Catch: Exception -> 0x041a, all -> 0x04b5, TryCatch #1 {all -> 0x04b5, blocks: (B:68:0x0360, B:71:0x036c, B:73:0x039b, B:72:0x0384, B:79:0x041b, B:64:0x02ad), top: B:94:0x001a }] */
    /* JADX WARN: Type inference failed for: r9v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v8 */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r66) {
        /*
            Method dump skipped, instructions count: 1354
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$publishRelease$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
