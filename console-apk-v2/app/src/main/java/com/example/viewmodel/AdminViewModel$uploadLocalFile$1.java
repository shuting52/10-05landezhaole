package com.example.viewmodel;

import android.net.Uri;
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
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$uploadLocalFile$1", f = "AdminViewModel.kt", i = {0}, l = {959}, m = "invokeSuspend", n = {"resolvedName"}, s = {"L$0"})
/* loaded from: classes4.dex */
public final class AdminViewModel$uploadLocalFile$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2<String, String, Unit> $onSuccess;
    final /* synthetic */ String $subFolder;
    final /* synthetic */ Uri $uri;
    Object L$0;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public AdminViewModel$uploadLocalFile$1(AdminViewModel adminViewModel, Uri uri, String str, Function2<? super String, ? super String, Unit> function2, Continuation<? super AdminViewModel$uploadLocalFile$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
        this.$uri = uri;
        this.$subFolder = str;
        this.$onSuccess = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$uploadLocalFile$1(this.this$0, this.$uri, this.$subFolder, this.$onSuccess, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$uploadLocalFile$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0074 A[Catch: all -> 0x007e, TryCatch #5 {Exception -> 0x0086, blocks: (B:14:0x0034, B:16:0x004a, B:32:0x007a, B:17:0x004d, B:19:0x0059, B:21:0x005f, B:23:0x0068, B:29:0x0074, B:30:0x0076), top: B:75:0x0034 }] */
    /* JADX WARN: Type inference failed for: r8v9, types: [T, java.lang.String] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r58) {
        /*
            Method dump skipped, instructions count: 780
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$uploadLocalFile$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
