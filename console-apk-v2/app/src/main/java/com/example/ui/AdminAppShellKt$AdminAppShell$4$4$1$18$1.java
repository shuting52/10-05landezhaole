package com.example.ui;

import com.example.model.SkillItem;
import com.example.viewmodel.AdminViewModel;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminAppShell.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final /* synthetic */ class AdminAppShellKt$AdminAppShell$4$4$1$18$1 extends FunctionReferenceImpl implements Function13<SkillItem, String, String, String, String, String, String, String, String, String, String, String, String, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public AdminAppShellKt$AdminAppShell$4$4$1$18$1(Object obj) {
        super(13, obj, AdminViewModel.class, "saveSkill", "saveSkill(Lcom/example/model/SkillItem;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function13
    public /* bridge */ /* synthetic */ Unit invoke(SkillItem skillItem, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12) {
        invoke2(skillItem, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(SkillItem p0, String p1, String p2, String p3, String p4, String p5, String p6, String p7, String p8, String p9, String p10, String p11, String p12) {
        Intrinsics.checkNotNullParameter(p1, "p1");
        Intrinsics.checkNotNullParameter(p2, "p2");
        Intrinsics.checkNotNullParameter(p3, "p3");
        Intrinsics.checkNotNullParameter(p4, "p4");
        Intrinsics.checkNotNullParameter(p5, "p5");
        Intrinsics.checkNotNullParameter(p6, "p6");
        Intrinsics.checkNotNullParameter(p7, "p7");
        Intrinsics.checkNotNullParameter(p8, "p8");
        Intrinsics.checkNotNullParameter(p9, "p9");
        Intrinsics.checkNotNullParameter(p10, "p10");
        Intrinsics.checkNotNullParameter(p11, "p11");
        Intrinsics.checkNotNullParameter(p12, "p12");
        ((AdminViewModel) this.receiver).saveSkill(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12);
    }
}
