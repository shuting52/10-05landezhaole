package com.example.ui;

import com.example.model.UpdateDialogConfig;
import com.example.viewmodel.AdminViewModel;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminAppShell.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final /* synthetic */ class AdminAppShellKt$AdminAppShell$4$4$1$25$1 extends FunctionReferenceImpl implements Function6<UpdateDialogConfig, String, Integer, Boolean, String, String, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public AdminAppShellKt$AdminAppShell$4$4$1$25$1(Object obj) {
        super(6, obj, AdminViewModel.class, "saveUpdateDialogAndVersionConfig", "saveUpdateDialogAndVersionConfig(Lcom/example/model/UpdateDialogConfig;Ljava/lang/String;IZLjava/lang/String;Ljava/lang/String;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function6
    public /* bridge */ /* synthetic */ Unit invoke(UpdateDialogConfig updateDialogConfig, String str, Integer num, Boolean bool, String str2, String str3) {
        invoke(updateDialogConfig, str, num.intValue(), bool.booleanValue(), str2, str3);
        return Unit.INSTANCE;
    }

    public final void invoke(UpdateDialogConfig p0, String p1, int p2, boolean p3, String p4, String p5) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        Intrinsics.checkNotNullParameter(p1, "p1");
        Intrinsics.checkNotNullParameter(p4, "p4");
        Intrinsics.checkNotNullParameter(p5, "p5");
        ((AdminViewModel) this.receiver).saveUpdateDialogAndVersionConfig(p0, p1, p2, p3, p4, p5);
    }
}
