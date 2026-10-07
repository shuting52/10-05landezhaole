package com.example.viewmodel;

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
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.MutableStateFlow;
import org.json.JSONObject;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminViewModel.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$applyToDevice$1", f = "AdminViewModel.kt", i = {0}, l = {1584}, m = "invokeSuspend", n = {"json"}, s = {"L$0"})
/* loaded from: classes4.dex */
public final class AdminViewModel$applyToDevice$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    Object L$0;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$applyToDevice$1(AdminViewModel adminViewModel, Continuation<? super AdminViewModel$applyToDevice$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$applyToDevice$1(this.this$0, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$applyToDevice$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        MutableStateFlow mutableStateFlow;
        Object value;
        MutableStateFlow mutableStateFlow2;
        Object value2;
        JSONObject json;
        MutableStateFlow mutableStateFlow3;
        Object value3;
        String nowTimeStr;
        Object persistAndRefresh;
        MutableStateFlow mutableStateFlow4;
        Object value4;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            try {
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        json = this.this$0.rootJson;
                        if (json == null) {
                            return Unit.INSTANCE;
                        }
                        mutableStateFlow3 = this.this$0._uiState;
                        do {
                            value3 = mutableStateFlow3.getValue();
                        } while (!mutableStateFlow3.compareAndSet(value3, AdminUiState.copy$default((AdminUiState) value3, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, false, false, false, false, true, false, false, null, -1, 7679, null)));
                        this.this$0.recordLog(LogActionType.UPDATE, "执行了「应用并实时同步」(mode=content)");
                        AdminViewModel adminViewModel = this.this$0;
                        nowTimeStr = this.this$0.nowTimeStr();
                        this.L$0 = SpillingKt.nullOutSpilledVariable(json);
                        this.label = 1;
                        persistAndRefresh = adminViewModel.persistAndRefresh(json, "console: 应用并实时同步 " + nowTimeStr, "content", this);
                        if (persistAndRefresh == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        break;
                    case 1:
                        JSONObject jSONObject = (JSONObject) this.L$0;
                        ResultKt.throwOnFailure($result);
                        persistAndRefresh = $result;
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                boolean remote = ((Boolean) persistAndRefresh).booleanValue();
                this.this$0.showToast(remote ? "✅ 已实时同步到本体软件（零延迟生效，不弹更新窗）" : "✅ 已应用内容同步模式（本地已生效，配置 Token 后可推送云端）");
                mutableStateFlow4 = this.this$0._uiState;
                do {
                    value4 = mutableStateFlow4.getValue();
                } while (!mutableStateFlow4.compareAndSet(value4, AdminUiState.copy$default((AdminUiState) value4, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, false, false, false, false, false, false, false, null, -1, 7679, null)));
            } catch (Exception e) {
                this.this$0.showToast("同步失败：" + e.getMessage());
                mutableStateFlow2 = this.this$0._uiState;
                do {
                    value2 = mutableStateFlow2.getValue();
                } while (!mutableStateFlow2.compareAndSet(value2, AdminUiState.copy$default((AdminUiState) value2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, false, false, false, false, false, false, false, null, -1, 7679, null)));
            }
            return Unit.INSTANCE;
        } catch (Throwable th) {
            mutableStateFlow = this.this$0._uiState;
            do {
                value = mutableStateFlow.getValue();
            } while (!mutableStateFlow.compareAndSet(value, AdminUiState.copy$default((AdminUiState) value, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, false, false, false, false, false, false, false, null, -1, 7679, null)));
            throw th;
        }
    }
}
