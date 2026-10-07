package com.example.ui;

import com.example.model.ResourceCard;
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
public final /* synthetic */ class AdminAppShellKt$AdminAppShell$4$4$1$7$1 extends FunctionReferenceImpl implements Function1<ResourceCard, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public AdminAppShellKt$AdminAppShell$4$4$1$7$1(Object obj) {
        super(1, obj, AdminViewModel.class, "deleteCard", "deleteCard(Lcom/example/model/ResourceCard;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Unit invoke(ResourceCard resourceCard) {
        invoke2(resourceCard);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(ResourceCard p0) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        ((AdminViewModel) this.receiver).deleteCard(p0);
    }
}
