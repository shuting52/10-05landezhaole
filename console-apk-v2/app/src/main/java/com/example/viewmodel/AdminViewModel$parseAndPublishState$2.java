package com.example.viewmodel;

import com.example.model.ConnState;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import org.json.JSONObject;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminViewModel.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$parseAndPublishState$2", f = "AdminViewModel.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
/* loaded from: classes4.dex */
public final class AdminViewModel$parseAndPublishState$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ ConnState $connState;
    final /* synthetic */ String $errMsg;
    final /* synthetic */ JSONObject $json;
    final /* synthetic */ String $syncTime;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$parseAndPublishState$2(JSONObject jSONObject, String str, AdminViewModel adminViewModel, ConnState connState, String str2, Continuation<? super AdminViewModel$parseAndPublishState$2> continuation) {
        super(2, continuation);
        this.$json = jSONObject;
        this.$syncTime = str;
        this.this$0 = adminViewModel;
        this.$connState = connState;
        this.$errMsg = str2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$parseAndPublishState$2(this.$json, this.$syncTime, this.this$0, this.$connState, this.$errMsg, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$parseAndPublishState$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:154:0x0263, code lost:
        if (r4 == null) goto L530;
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x02cb, code lost:
        if (r5 == null) goto L524;
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x030e, code lost:
        if (r5 == null) goto L519;
     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x0374, code lost:
        if (r7 == null) goto L512;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x051e, code lost:
        if (r2 == null) goto L498;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x055e, code lost:
        if (r7 == null) goto L494;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00e9, code lost:
        if (r10 == null) goto L548;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x01d3  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x01d9  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x01e9  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x01f9  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0209  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0219  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0229  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0239  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0249  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0259  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x0270  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0278  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x027c  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x028c  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x0294  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0298  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x02c1  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x02ce  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x02d6  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x02e6  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x02f7  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x0300  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0304  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0311  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x032a  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0332  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0338  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0346  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x0354  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x035a  */
    /* JADX WARN: Removed duplicated region for block: B:232:0x036a  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0377  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x037f  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x038f  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x03aa  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x03d0  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x0406  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x040c  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x040f  */
    /* JADX WARN: Removed duplicated region for block: B:271:0x041d  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x0443  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x0484  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x04a1 A[LOOP:3: B:288:0x049f->B:289:0x04a1, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:292:0x04b6  */
    /* JADX WARN: Removed duplicated region for block: B:299:0x04c5  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x04d5  */
    /* JADX WARN: Removed duplicated region for block: B:309:0x04e5  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x04f5  */
    /* JADX WARN: Removed duplicated region for block: B:322:0x0516  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x0521  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x0527  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x052e  */
    /* JADX WARN: Removed duplicated region for block: B:331:0x0532  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x054a  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x0556  */
    /* JADX WARN: Removed duplicated region for block: B:342:0x0561  */
    /* JADX WARN: Removed duplicated region for block: B:346:0x0572  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x0594  */
    /* JADX WARN: Removed duplicated region for block: B:574:0x05fa A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01a9  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01bc  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x01c5  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r100) {
        /*
            Method dump skipped, instructions count: 4204
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$parseAndPublishState$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
