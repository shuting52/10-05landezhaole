package com.example.ui;

import com.example.model.ConsoleConfig;
import com.example.model.IpMonitorConfig;
import com.example.model.ToolItem;
import com.example.viewmodel.AdminViewModel;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminAppShell.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final /* synthetic */ class AdminAppShellKt$AdminAppShell$4$4$1$26$1 extends FunctionReferenceImpl implements Function4<ConsoleConfig, IpMonitorConfig, String, List<? extends ToolItem>, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public AdminAppShellKt$AdminAppShell$4$4$1$26$1(Object obj) {
        super(4, obj, AdminViewModel.class, "saveMiscModulesConfig", "saveMiscModulesConfig(Lcom/example/model/ConsoleConfig;Lcom/example/model/IpMonitorConfig;Ljava/lang/String;Ljava/util/List;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function4
    public /* bridge */ /* synthetic */ Unit invoke(ConsoleConfig consoleConfig, IpMonitorConfig ipMonitorConfig, String str, List<? extends ToolItem> list) {
        invoke2(consoleConfig, ipMonitorConfig, str, (List<ToolItem>) list);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(ConsoleConfig p0, IpMonitorConfig p1, String p2, List<ToolItem> p3) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        Intrinsics.checkNotNullParameter(p1, "p1");
        Intrinsics.checkNotNullParameter(p2, "p2");
        Intrinsics.checkNotNullParameter(p3, "p3");
        ((AdminViewModel) this.receiver).saveMiscModulesConfig(p0, p1, p2, p3);
    }
}
