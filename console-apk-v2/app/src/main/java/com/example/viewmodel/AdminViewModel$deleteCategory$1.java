package com.example.viewmodel;

import com.example.model.CategoryItem;
import com.example.model.LogActionType;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import org.json.JSONArray;
import org.json.JSONObject;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminViewModel.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$deleteCategory$1", f = "AdminViewModel.kt", i = {0, 0, 0}, l = {1309}, m = "invokeSuspend", n = {"json", "home", "catsArr"}, s = {"L$0", "L$1", "L$2"})
/* loaded from: classes4.dex */
public final class AdminViewModel$deleteCategory$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ CategoryItem $cat;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$deleteCategory$1(AdminViewModel adminViewModel, CategoryItem categoryItem, Continuation<? super AdminViewModel$deleteCategory$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
        this.$cat = categoryItem;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$deleteCategory$1(this.this$0, this.$cat, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$deleteCategory$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        JSONObject json;
        JSONObject home;
        JSONArray catsArr;
        Object persistAndRefresh$default;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    json = this.this$0.rootJson;
                    if (json != null && (home = json.optJSONObject("home")) != null && (catsArr = home.optJSONArray("categories")) != null) {
                        int i = 0;
                        int length = catsArr.length();
                        while (true) {
                            if (i < length) {
                                JSONObject c = catsArr.optJSONObject(i);
                                if (c != null && Intrinsics.areEqual(c.optString("id"), this.$cat.getId())) {
                                    catsArr.remove(i);
                                }
                                i++;
                            }
                        }
                        this.this$0.recordLog(LogActionType.DELETE, "删除了分类「" + this.$cat.getName() + "」及其下卡片");
                        AdminViewModel adminViewModel = this.this$0;
                        String id = this.$cat.getId();
                        this.L$0 = SpillingKt.nullOutSpilledVariable(json);
                        this.L$1 = SpillingKt.nullOutSpilledVariable(home);
                        this.L$2 = SpillingKt.nullOutSpilledVariable(catsArr);
                        this.label = 1;
                        persistAndRefresh$default = AdminViewModel.persistAndRefresh$default(adminViewModel, json, "console: 删除分类 " + id, null, this, 4, null);
                        if (persistAndRefresh$default != coroutine_suspended) {
                            break;
                        } else {
                            return coroutine_suspended;
                        }
                    }
                    return Unit.INSTANCE;
                case 1:
                    JSONArray jSONArray = (JSONArray) this.L$2;
                    JSONObject jSONObject = (JSONObject) this.L$1;
                    JSONObject jSONObject2 = (JSONObject) this.L$0;
                    ResultKt.throwOnFailure($result);
                    persistAndRefresh$default = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            boolean remote = ((Boolean) persistAndRefresh$default).booleanValue();
            AdminViewModel adminViewModel2 = this.this$0;
            CategoryItem categoryItem = this.$cat;
            adminViewModel2.showToast((remote ? new StringBuilder().append("✅ 已删除分类「").append(categoryItem.getName()).append("」并同步到本体") : new StringBuilder().append("已删除分类「").append(categoryItem.getName()).append("」（本地已生效）")).toString());
        } catch (Exception e) {
            this.this$0.showToast("删除失败：" + e.getMessage());
        }
        return Unit.INSTANCE;
    }
}
