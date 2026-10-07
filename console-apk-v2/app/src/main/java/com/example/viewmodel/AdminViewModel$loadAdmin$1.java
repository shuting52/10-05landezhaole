package com.example.viewmodel;

import androidx.compose.runtime.ComposerKt;
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
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$loadAdmin$1", f = "AdminViewModel.kt", i = {1, 1, 1, 3, 3, 3, 5, 5, 5, 5, 6, 7, 7, 7, 7, 8, 8}, l = {166, 170, 178, 182, 184, 189, 193, 197, ComposerKt.invocationKey}, m = "invokeSuspend", n = {"localStr", "localJson", "syncTime", "content", "json", "syncTime", "content", "sha", "json", "syncTime", "e", "e", "content", "json", "syncTime", "e", "e2"}, s = {"L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3", "L$0", "L$0", "L$1", "L$2", "L$3", "L$0", "L$1"})
/* loaded from: classes4.dex */
public final class AdminViewModel$loadAdmin$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ boolean $preferMirror;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$loadAdmin$1(AdminViewModel adminViewModel, boolean z, Continuation<? super AdminViewModel$loadAdmin$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
        this.$preferMirror = z;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$loadAdmin$1(this.this$0, this.$preferMirror, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$loadAdmin$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x0174 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0175  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x017d A[Catch: Exception -> 0x0080, TryCatch #0 {Exception -> 0x0080, blocks: (B:18:0x005c, B:19:0x0061, B:55:0x01a9, B:22:0x0074, B:23:0x0079, B:63:0x0217, B:47:0x0179, B:49:0x017d, B:52:0x0190, B:60:0x01fe), top: B:98:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01f9 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01fa  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0216 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0255 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0256  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x02d6  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x030b  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r62) {
        /*
            Method dump skipped, instructions count: 942
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$loadAdmin$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
