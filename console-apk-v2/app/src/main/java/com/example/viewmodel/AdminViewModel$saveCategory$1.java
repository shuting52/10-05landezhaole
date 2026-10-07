package com.example.viewmodel;

import com.example.model.CategoryItem;
import com.example.model.SubCategoryItem;
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
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$saveCategory$1", f = "AdminViewModel.kt", i = {0, 0, 0, 0, 0}, l = {1287}, m = "invokeSuspend", n = {"json", "home", "catsArr", "subArr", "normalizedSubs"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4"})
/* loaded from: classes4.dex */
public final class AdminViewModel$saveCategory$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ String $desc;
    final /* synthetic */ CategoryItem $existing;
    final /* synthetic */ String $iconKey;
    final /* synthetic */ String $name;
    final /* synthetic */ List<SubCategoryItem> $subcategories;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$saveCategory$1(AdminViewModel adminViewModel, List<SubCategoryItem> list, CategoryItem categoryItem, String str, String str2, String str3, Continuation<? super AdminViewModel$saveCategory$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
        this.$subcategories = list;
        this.$existing = categoryItem;
        this.$name = str;
        this.$iconKey = str2;
        this.$desc = str3;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$saveCategory$1(this.this$0, this.$subcategories, this.$existing, this.$name, this.$iconKey, this.$desc, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$saveCategory$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:84:0x02bc A[Catch: Exception -> 0x0033, TRY_ENTER, TryCatch #2 {Exception -> 0x0033, blocks: (B:7:0x002b, B:81:0x02b0, B:84:0x02bc, B:85:0x02db, B:87:0x0300, B:86:0x02e0, B:18:0x004c, B:22:0x005d, B:27:0x007a, B:34:0x0099, B:38:0x00c9, B:41:0x00e5), top: B:100:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x02e0 A[Catch: Exception -> 0x0033, TryCatch #2 {Exception -> 0x0033, blocks: (B:7:0x002b, B:81:0x02b0, B:84:0x02bc, B:85:0x02db, B:87:0x0300, B:86:0x02e0, B:18:0x004c, B:22:0x005d, B:27:0x007a, B:34:0x0099, B:38:0x00c9, B:41:0x00e5), top: B:100:0x000c }] */
    /* JADX WARN: Type inference failed for: r18v14, types: [java.lang.CharSequence] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r25) {
        /*
            Method dump skipped, instructions count: 818
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$saveCategory$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
