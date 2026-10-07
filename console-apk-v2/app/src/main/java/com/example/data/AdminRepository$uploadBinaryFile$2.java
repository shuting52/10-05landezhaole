package com.example.data;

import android.net.Uri;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminRepository.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.data.AdminRepository$uploadBinaryFile$2", f = "AdminRepository.kt", i = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, l = {363}, m = "invokeSuspend", n = {"$this$withContext", "token", "resolvedName", "safeFileName", "bytes", "folder", "repoPath", "base64Content", "cfg", "apiUrl", "existingSha", "payload", "uploadClient", "req", "timestamp"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "L$10", "L$11", "L$12", "L$13", "J$0"})
/* loaded from: classes5.dex */
public final class AdminRepository$uploadBinaryFile$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super String>, Object> {
    final /* synthetic */ String $customFileName;
    final /* synthetic */ String $subFolder;
    final /* synthetic */ Uri $uri;
    long J$0;
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$10;
    Object L$11;
    Object L$12;
    Object L$13;
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
    public AdminRepository$uploadBinaryFile$2(AdminRepository adminRepository, String str, Uri uri, String str2, Continuation<? super AdminRepository$uploadBinaryFile$2> continuation) {
        super(2, continuation);
        this.this$0 = adminRepository;
        this.$customFileName = str;
        this.$uri = uri;
        this.$subFolder = str2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        AdminRepository$uploadBinaryFile$2 adminRepository$uploadBinaryFile$2 = new AdminRepository$uploadBinaryFile$2(this.this$0, this.$customFileName, this.$uri, this.$subFolder, continuation);
        adminRepository$uploadBinaryFile$2.L$0 = obj;
        return adminRepository$uploadBinaryFile$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super String> continuation) {
        return ((AdminRepository$uploadBinaryFile$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:82:0x02b7, code lost:
        if (r29 == null) goto L57;
     */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:121:0x036c  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0442 A[Catch: all -> 0x04fe, TRY_ENTER, TRY_LEAVE, TryCatch #16 {all -> 0x04fe, blocks: (B:123:0x03ed, B:139:0x0442), top: B:192:0x03ed }] */
    /* JADX WARN: Removed duplicated region for block: B:180:0x03f7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0109  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0167  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x017f  */
    /* JADX WARN: Type inference failed for: r2v18 */
    /* JADX WARN: Type inference failed for: r2v21 */
    /* JADX WARN: Type inference failed for: r2v26, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r2v27 */
    /* JADX WARN: Type inference failed for: r2v33 */
    /* JADX WARN: Type inference failed for: r2v44 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r4v9, types: [T, java.lang.Object, java.lang.String] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r32) {
        /*
            Method dump skipped, instructions count: 1342
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.data.AdminRepository$uploadBinaryFile$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
