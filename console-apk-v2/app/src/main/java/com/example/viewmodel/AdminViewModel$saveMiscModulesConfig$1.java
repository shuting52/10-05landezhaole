package com.example.viewmodel;

import com.example.model.ConsoleConfig;
import com.example.model.IpMonitorConfig;
import com.example.model.ToolItem;
import java.util.List;
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
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$saveMiscModulesConfig$1", f = "AdminViewModel.kt", i = {0, 0, 0, 0}, l = {1488}, m = "invokeSuspend", n = {"json", "cs", "ip", "tArr"}, s = {"L$0", "L$1", "L$2", "L$3"})
/* loaded from: classes4.dex */
public final class AdminViewModel$saveMiscModulesConfig$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ String $aiNoticeText;
    final /* synthetic */ ConsoleConfig $consoleCfg;
    final /* synthetic */ IpMonitorConfig $ipCfg;
    final /* synthetic */ List<ToolItem> $tools;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$saveMiscModulesConfig$1(AdminViewModel adminViewModel, ConsoleConfig consoleConfig, IpMonitorConfig ipMonitorConfig, String str, List<ToolItem> list, Continuation<? super AdminViewModel$saveMiscModulesConfig$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
        this.$consoleCfg = consoleConfig;
        this.$ipCfg = ipMonitorConfig;
        this.$aiNoticeText = str;
        this.$tools = list;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$saveMiscModulesConfig$1(this.this$0, this.$consoleCfg, this.$ipCfg, this.$aiNoticeText, this.$tools, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$saveMiscModulesConfig$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x01c2  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01c5  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r21) {
        /*
            Method dump skipped, instructions count: 500
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$saveMiscModulesConfig$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
