package com.example.ui;

import android.net.Uri;
import com.example.viewmodel.AdminViewModel;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminAppShell.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final /* synthetic */ class AdminAppShellKt$AdminAppShell$4$4$1$20$1 extends FunctionReferenceImpl implements Function3<Uri, String, Function2<? super String, ? super String, ? extends Unit>, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public AdminAppShellKt$AdminAppShell$4$4$1$20$1(Object obj) {
        super(3, obj, AdminViewModel.class, "uploadLocalFile", "uploadLocalFile(Landroid/net/Uri;Ljava/lang/String;Lkotlin/jvm/functions/Function2;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function3
    public /* bridge */ /* synthetic */ Unit invoke(Uri uri, String str, Function2<? super String, ? super String, ? extends Unit> function2) {
        invoke2(uri, str, (Function2<? super String, ? super String, Unit>) function2);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(Uri p0, String p1, Function2<? super String, ? super String, Unit> p2) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        Intrinsics.checkNotNullParameter(p1, "p1");
        Intrinsics.checkNotNullParameter(p2, "p2");
        ((AdminViewModel) this.receiver).uploadLocalFile(p0, p1, p2);
    }
}
