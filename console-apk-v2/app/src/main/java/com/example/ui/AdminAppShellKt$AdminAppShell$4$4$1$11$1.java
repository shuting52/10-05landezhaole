package com.example.ui;

import com.example.model.CategoryItem;
import com.example.model.SubCategoryItem;
import com.example.viewmodel.AdminViewModel;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminAppShell.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final /* synthetic */ class AdminAppShellKt$AdminAppShell$4$4$1$11$1 extends FunctionReferenceImpl implements Function5<CategoryItem, String, String, String, List<? extends SubCategoryItem>, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public AdminAppShellKt$AdminAppShell$4$4$1$11$1(Object obj) {
        super(5, obj, AdminViewModel.class, "saveCategory", "saveCategory(Lcom/example/model/CategoryItem;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function5
    public /* bridge */ /* synthetic */ Unit invoke(CategoryItem categoryItem, String str, String str2, String str3, List<? extends SubCategoryItem> list) {
        invoke2(categoryItem, str, str2, str3, (List<SubCategoryItem>) list);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(CategoryItem p0, String p1, String p2, String p3, List<SubCategoryItem> p4) {
        Intrinsics.checkNotNullParameter(p1, "p1");
        Intrinsics.checkNotNullParameter(p2, "p2");
        Intrinsics.checkNotNullParameter(p3, "p3");
        Intrinsics.checkNotNullParameter(p4, "p4");
        ((AdminViewModel) this.receiver).saveCategory(p0, p1, p2, p3, p4);
    }
}
