package com.example.data;

import androidx.core.view.InputDeviceCompat;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminRepository.kt */
@Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u000b\n\u0002\u0018\u0002\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001*\u00020\u0004H\n"}, d2 = {"<anonymous>", "Lkotlin/Pair;", "", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.data.AdminRepository$ghWriteText$2", f = "AdminRepository.kt", i = {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, l = {211, 251, InputDeviceCompat.SOURCE_KEYBOARD}, m = "invokeSuspend", n = {"token", "cfg", "currentSha", "attempt", "token", "cfg", "currentSha", "url", "encoded", "payload", "body", "req", "shouldRetry409", "resultSha", "attempt", "token", "cfg", "currentSha", "url", "encoded", "payload", "body", "req", "shouldRetry409", "resultSha", "attempt"}, s = {"L$0", "L$1", "L$2", "I$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "I$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "I$0"})
/* loaded from: classes5.dex */
public final class AdminRepository$ghWriteText$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Pair<? extends String, ? extends Boolean>>, Object> {
    final /* synthetic */ String $content;
    final /* synthetic */ String $message;
    final /* synthetic */ String $path;
    final /* synthetic */ String $sha;
    int I$0;
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
    final /* synthetic */ AdminRepository this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminRepository$ghWriteText$2(AdminRepository adminRepository, String str, String str2, String str3, String str4, Continuation<? super AdminRepository$ghWriteText$2> continuation) {
        super(2, continuation);
        this.this$0 = adminRepository;
        this.$content = str;
        this.$sha = str2;
        this.$path = str3;
        this.$message = str4;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminRepository$ghWriteText$2(this.this$0, this.$content, this.$sha, this.$path, this.$message, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super Pair<? extends String, ? extends Boolean>> continuation) {
        return invoke2(coroutineScope, (Continuation<? super Pair<String, Boolean>>) continuation);
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super Pair<String, Boolean>> continuation) {
        return ((AdminRepository$ghWriteText$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x023c, code lost:
        if (r22 == null) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0258, code lost:
        if (r4 == null) goto L45;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x01a2  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x022e  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0264 A[Catch: all -> 0x038a, TryCatch #4 {all -> 0x038a, blocks: (B:49:0x024b, B:51:0x0254, B:54:0x025b, B:73:0x02b9, B:57:0x0264, B:59:0x0270, B:62:0x0279, B:64:0x027f, B:66:0x0285, B:70:0x028f, B:71:0x02b5, B:72:0x02b6), top: B:105:0x024b }] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02c4  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0328  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0399  */
    /* JADX WARN: Type inference failed for: r0v12, types: [T, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r7v0, types: [T, java.lang.String] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:85:0x036b -> B:86:0x037a). Please submit an issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r25) {
        /*
            Method dump skipped, instructions count: 942
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.data.AdminRepository$ghWriteText$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
