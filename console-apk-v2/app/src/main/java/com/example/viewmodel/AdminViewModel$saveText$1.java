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
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$saveText$1", f = "AdminViewModel.kt", i = {0, 0}, l = {1217}, m = "invokeSuspend", n = {"json", "key"}, s = {"L$0", "L$1"})
/* loaded from: classes4.dex */
public final class AdminViewModel$saveText$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ String $content;
    final /* synthetic */ boolean $isNew;
    final /* synthetic */ String $keyName;
    Object L$0;
    Object L$1;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$saveText$1(AdminViewModel adminViewModel, String str, String str2, boolean z, Continuation<? super AdminViewModel$saveText$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
        this.$keyName = str;
        this.$content = str2;
        this.$isNew = z;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$saveText$1(this.this$0, this.$keyName, this.$content, this.$isNew, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$saveText$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0100 A[Catch: Exception -> 0x01b4, TryCatch #1 {Exception -> 0x01b4, blocks: (B:16:0x0042, B:20:0x005a, B:23:0x0063, B:25:0x0069, B:26:0x0073, B:59:0x010f, B:61:0x0115, B:63:0x011a, B:67:0x0123, B:62:0x0118, B:27:0x007c, B:30:0x0085, B:32:0x008b, B:33:0x0095, B:34:0x009e, B:37:0x00a5, B:39:0x00ab, B:40:0x00b5, B:41:0x00bb, B:44:0x00c2, B:46:0x00c8, B:47:0x00d2, B:48:0x00d8, B:51:0x00e2, B:53:0x00e8, B:54:0x00f2, B:55:0x00fa, B:57:0x0100, B:58:0x010a), top: B:84:0x0042 }] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0181 A[Catch: Exception -> 0x002f, TryCatch #0 {Exception -> 0x002f, blocks: (B:7:0x0027, B:71:0x0177, B:73:0x0181, B:75:0x01ac, B:74:0x0197), top: B:82:0x0027 }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0197 A[Catch: Exception -> 0x002f, TryCatch #0 {Exception -> 0x002f, blocks: (B:7:0x0027, B:71:0x0177, B:73:0x0181, B:75:0x01ac, B:74:0x0197), top: B:82:0x0027 }] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r17) {
        /*
            Method dump skipped, instructions count: 500
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$saveText$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
