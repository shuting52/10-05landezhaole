package com.example.ui;

import com.example.model.AdminScreen;
import com.example.viewmodel.AdminViewModel;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminAppShell.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final /* synthetic */ class AdminAppShellKt$AdminAppShell$4$4$1$2$1 extends FunctionReferenceImpl implements Function1<AdminScreen, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public AdminAppShellKt$AdminAppShell$4$4$1$2$1(Object obj) {
        super(1, obj, AdminViewModel.class, "navigateTo", "navigateTo(Lcom/example/model/AdminScreen;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Unit invoke(AdminScreen adminScreen) {
        invoke2(adminScreen);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(AdminScreen p0) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        ((AdminViewModel) this.receiver).navigateTo(p0);
    }
}
