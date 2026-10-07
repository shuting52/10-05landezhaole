package com.example.viewmodel;

import com.example.model.ButtonType;
import com.example.model.CardStatus;
import com.example.model.ResourceCard;
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
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$saveCard$1", f = "AdminViewModel.kt", i = {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1}, l = {875, 903}, m = "invokeSuspend", n = {"json", "catsArr", "targetCatObj", "catId", "cardsArr", "finalFallback", "updatedInPlace", "json", "catsArr", "targetCatObj", "catId", "cardsArr", "finalFallback", "newId", "newObj", "rebuilt"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "I$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8"})
/* loaded from: classes4.dex */
public final class AdminViewModel$saveCard$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ String $badge;
    final /* synthetic */ String $badgeType;
    final /* synthetic */ ButtonType $buttonType;
    final /* synthetic */ String $categoryName;
    final /* synthetic */ String $description;
    final /* synthetic */ ResourceCard $existingCard;
    final /* synthetic */ String $fallbackText;
    final /* synthetic */ String $highlights;
    final /* synthetic */ String $icon;
    final /* synthetic */ String $name;
    final /* synthetic */ CardStatus $status;
    final /* synthetic */ String $subcatId;
    final /* synthetic */ String $url;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    Object L$6;
    Object L$7;
    Object L$8;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$saveCard$1(AdminViewModel adminViewModel, String str, String str2, ResourceCard resourceCard, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, ButtonType buttonType, CardStatus cardStatus, Continuation<? super AdminViewModel$saveCard$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
        this.$categoryName = str;
        this.$fallbackText = str2;
        this.$existingCard = resourceCard;
        this.$name = str3;
        this.$description = str4;
        this.$url = str5;
        this.$icon = str6;
        this.$badge = str7;
        this.$badgeType = str8;
        this.$subcatId = str9;
        this.$highlights = str10;
        this.$buttonType = buttonType;
        this.$status = cardStatus;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$saveCard$1(this.this$0, this.$categoryName, this.$fallbackText, this.$existingCard, this.$name, this.$description, this.$url, this.$icon, this.$badge, this.$badgeType, this.$subcatId, this.$highlights, this.$buttonType, this.$status, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$saveCard$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0094, code lost:
        if (r4 == null) goto L37;
     */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0509 A[Catch: Exception -> 0x0073, TRY_ENTER, TryCatch #3 {Exception -> 0x0073, blocks: (B:7:0x003f, B:136:0x0740, B:139:0x074c, B:140:0x076b, B:142:0x0790, B:141:0x0770, B:10:0x0066, B:112:0x04fd, B:115:0x0509, B:116:0x0528, B:118:0x054d, B:117:0x052d), top: B:154:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:117:0x052d A[Catch: Exception -> 0x0073, TryCatch #3 {Exception -> 0x0073, blocks: (B:7:0x003f, B:136:0x0740, B:139:0x074c, B:140:0x076b, B:142:0x0790, B:141:0x0770, B:10:0x0066, B:112:0x04fd, B:115:0x0509, B:116:0x0528, B:118:0x054d, B:117:0x052d), top: B:154:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:139:0x074c A[Catch: Exception -> 0x0073, TRY_ENTER, TryCatch #3 {Exception -> 0x0073, blocks: (B:7:0x003f, B:136:0x0740, B:139:0x074c, B:140:0x076b, B:142:0x0790, B:141:0x0770, B:10:0x0066, B:112:0x04fd, B:115:0x0509, B:116:0x0528, B:118:0x054d, B:117:0x052d), top: B:154:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0770 A[Catch: Exception -> 0x0073, TryCatch #3 {Exception -> 0x0073, blocks: (B:7:0x003f, B:136:0x0740, B:139:0x074c, B:140:0x076b, B:142:0x0790, B:141:0x0770, B:10:0x0066, B:112:0x04fd, B:115:0x0509, B:116:0x0528, B:118:0x054d, B:117:0x052d), top: B:154:0x0010 }] */
    /* JADX WARN: Type inference failed for: r10v17, types: [org.json.JSONObject, T] */
    /* JADX WARN: Type inference failed for: r7v27, types: [org.json.JSONObject, T] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r49) {
        /*
            Method dump skipped, instructions count: 1990
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$saveCard$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
