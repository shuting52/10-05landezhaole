package com.example.viewmodel;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminViewModel.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel", f = "AdminViewModel.kt", i = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1}, l = {768, 778}, m = "persistAndRefresh", n = {"json", "commitMsg", "targetMode", "ar", "serialized", "json", "commitMsg", "targetMode", "ar", "serialized", "newSha", "syncTime", "nextConn", "pushedRemote"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "Z$0"})
/* loaded from: classes4.dex */
public final class AdminViewModel$persistAndRefresh$1 extends ContinuationImpl {
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    Object L$6;
    Object L$7;
    boolean Z$0;
    int label;
    /* synthetic */ Object result;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$persistAndRefresh$1(AdminViewModel adminViewModel, Continuation<? super AdminViewModel$persistAndRefresh$1> continuation) {
        super(continuation);
        this.this$0 = adminViewModel;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object persistAndRefresh;
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        persistAndRefresh = this.this$0.persistAndRefresh(null, null, null, this);
        return persistAndRefresh;
    }
}
