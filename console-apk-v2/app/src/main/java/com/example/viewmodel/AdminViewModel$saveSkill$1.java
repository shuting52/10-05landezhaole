package com.example.viewmodel;

import com.example.model.SkillItem;
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
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$saveSkill$1", f = "AdminViewModel.kt", i = {0, 0, 0, 0, 0}, l = {1152}, m = "invokeSuspend", n = {"json", "cleanUrl", "finalMode", "skArr", "isFileExt"}, s = {"L$0", "L$1", "L$2", "L$3", "I$0"})
/* loaded from: classes4.dex */
public final class AdminViewModel$saveSkill$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ String $author;
    final /* synthetic */ String $badge;
    final /* synthetic */ String $desc;
    final /* synthetic */ SkillItem $existing;
    final /* synthetic */ String $iconUrl;
    final /* synthetic */ String $mediaUrl;
    final /* synthetic */ String $mode;
    final /* synthetic */ String $previewUrl;
    final /* synthetic */ String $prompt;
    final /* synthetic */ String $promptType;
    final /* synthetic */ String $tags;
    final /* synthetic */ String $title;
    final /* synthetic */ String $url;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$saveSkill$1(AdminViewModel adminViewModel, String str, String str2, SkillItem skillItem, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, Continuation<? super AdminViewModel$saveSkill$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
        this.$url = str;
        this.$mode = str2;
        this.$existing = skillItem;
        this.$title = str3;
        this.$desc = str4;
        this.$promptType = str5;
        this.$prompt = str6;
        this.$author = str7;
        this.$badge = str8;
        this.$tags = str9;
        this.$previewUrl = str10;
        this.$mediaUrl = str11;
        this.$iconUrl = str12;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$saveSkill$1(this.this$0, this.$url, this.$mode, this.$existing, this.$title, this.$desc, this.$promptType, this.$prompt, this.$author, this.$badge, this.$tags, this.$previewUrl, this.$mediaUrl, this.$iconUrl, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$saveSkill$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x009e A[Catch: Exception -> 0x0033, TryCatch #5 {Exception -> 0x0033, blocks: (B:7:0x002a, B:16:0x0047, B:18:0x005d, B:20:0x0065, B:25:0x0071, B:30:0x008a, B:33:0x009e, B:37:0x00a9, B:39:0x00b0, B:40:0x00ba), top: B:122:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00b0 A[Catch: Exception -> 0x0033, TryCatch #5 {Exception -> 0x0033, blocks: (B:7:0x002a, B:16:0x0047, B:18:0x005d, B:20:0x0065, B:25:0x0071, B:30:0x008a, B:33:0x009e, B:37:0x00a9, B:39:0x00b0, B:40:0x00ba), top: B:122:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x023b  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x03b8  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x03bb  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0405 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0406  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0414 A[Catch: Exception -> 0x045b, TRY_ENTER, TryCatch #1 {Exception -> 0x045b, blocks: (B:93:0x0408, B:96:0x0414, B:98:0x0453, B:97:0x0434, B:85:0x03a6, B:89:0x03bd, B:84:0x037a), top: B:114:0x037a }] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0434 A[Catch: Exception -> 0x045b, TryCatch #1 {Exception -> 0x045b, blocks: (B:93:0x0408, B:96:0x0414, B:98:0x0453, B:97:0x0434, B:85:0x03a6, B:89:0x03bd, B:84:0x037a), top: B:114:0x037a }] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r41) {
        /*
            Method dump skipped, instructions count: 1168
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$saveSkill$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
