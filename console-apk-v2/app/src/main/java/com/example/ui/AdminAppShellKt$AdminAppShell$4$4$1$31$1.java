package com.example.ui;

import com.example.viewmodel.AdminViewModel;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.AdaptedFunctionReference;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminAppShell.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final /* synthetic */ class AdminAppShellKt$AdminAppShell$4$4$1$31$1 extends AdaptedFunctionReference implements Function3<String, String, String, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public AdminAppShellKt$AdminAppShell$4$4$1$31$1(Object obj) {
        super(3, obj, AdminViewModel.class, "updateGithubInputs", "updateGithubInputs(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function3
    public /* bridge */ /* synthetic */ Unit invoke(String str, String str2, String str3) {
        invoke2(str, str2, str3);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(String p0, String p1, String p2) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        Intrinsics.checkNotNullParameter(p1, "p1");
        Intrinsics.checkNotNullParameter(p2, "p2");
        AdminViewModel.updateGithubInputs$default((AdminViewModel) this.receiver, p0, p1, p2, null, 8, null);
    }
}
