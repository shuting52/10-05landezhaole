package com.example.ui;

import com.example.viewmodel.AdminViewModel;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminAppShell.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final /* synthetic */ class AdminAppShellKt$AdminAppShell$4$4$1$28$1 extends FunctionReferenceImpl implements Function3<String, String, Boolean, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public AdminAppShellKt$AdminAppShell$4$4$1$28$1(Object obj) {
        super(3, obj, AdminViewModel.class, "saveText", "saveText(Ljava/lang/String;Ljava/lang/String;Z)V", 0);
    }

    @Override // kotlin.jvm.functions.Function3
    public /* bridge */ /* synthetic */ Unit invoke(String str, String str2, Boolean bool) {
        invoke(str, str2, bool.booleanValue());
        return Unit.INSTANCE;
    }

    public final void invoke(String p0, String p1, boolean p2) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        Intrinsics.checkNotNullParameter(p1, "p1");
        ((AdminViewModel) this.receiver).saveText(p0, p1, p2);
    }
}
