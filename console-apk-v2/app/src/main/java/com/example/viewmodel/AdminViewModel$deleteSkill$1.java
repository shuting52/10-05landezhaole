package com.example.viewmodel;

import com.example.model.SkillItem;
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
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$deleteSkill$1", f = "AdminViewModel.kt", i = {0, 0}, l = {1173}, m = "invokeSuspend", n = {"json", "skArr"}, s = {"L$0", "L$1"})
/* loaded from: classes4.dex */
public final class AdminViewModel$deleteSkill$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ SkillItem $skill;
    Object L$0;
    Object L$1;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$deleteSkill$1(AdminViewModel adminViewModel, SkillItem skillItem, Continuation<? super AdminViewModel$deleteSkill$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
        this.$skill = skillItem;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$deleteSkill$1(this.this$0, this.$skill, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$deleteSkill$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00d0 A[Catch: Exception -> 0x010b, TRY_ENTER, TryCatch #1 {Exception -> 0x010b, blocks: (B:33:0x00c4, B:36:0x00d0, B:38:0x0103, B:37:0x00ea, B:16:0x0033, B:18:0x003b, B:20:0x003e, B:22:0x0046, B:28:0x0063, B:25:0x004d, B:27:0x005f, B:29:0x0066), top: B:47:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00ea A[Catch: Exception -> 0x010b, TryCatch #1 {Exception -> 0x010b, blocks: (B:33:0x00c4, B:36:0x00d0, B:38:0x0103, B:37:0x00ea, B:16:0x0033, B:18:0x003b, B:20:0x003e, B:22:0x0046, B:28:0x0063, B:25:0x004d, B:27:0x005f, B:29:0x0066), top: B:47:0x0033 }] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r10) {
        /*
            Method dump skipped, instructions count: 310
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$deleteSkill$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
