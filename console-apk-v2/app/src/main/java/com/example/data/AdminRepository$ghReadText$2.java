package com.example.data;

import android.util.Base64;
import com.example.model.GithubConfig;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import kotlinx.coroutines.CoroutineScope;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminRepository.kt */
@Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u0016\u0012\u0004\u0012\u00020\u0002\u0012\f\u0012\n \u0003*\u0004\u0018\u00010\u00020\u00020\u0001*\u00020\u0004H\n"}, d2 = {"<anonymous>", "Lkotlin/Pair;", "", "kotlin.jvm.PlatformType", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.data.AdminRepository$ghReadText$2", f = "AdminRepository.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
/* loaded from: classes5.dex */
public final class AdminRepository$ghReadText$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Pair<? extends String, ? extends String>>, Object> {
    final /* synthetic */ String $path;
    int label;
    final /* synthetic */ AdminRepository this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminRepository$ghReadText$2(AdminRepository adminRepository, String str, Continuation<? super AdminRepository$ghReadText$2> continuation) {
        super(2, continuation);
        this.this$0 = adminRepository;
        this.$path = str;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminRepository$ghReadText$2(this.this$0, this.$path, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super Pair<? extends String, ? extends String>> continuation) {
        return invoke2(coroutineScope, (Continuation<? super Pair<String, String>>) continuation);
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super Pair<String, String>> continuation) {
        return ((AdminRepository$ghReadText$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        OkHttpClient okHttpClient;
        Throwable th;
        String string;
        OkHttpClient okHttpClient2;
        Throwable th2;
        byte[] bytes;
        byte[] bArr;
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                GithubConfig cfg = this.this$0.getConfig();
                String token = this.this$0.getToken();
                String url = "https://api.github.com/repos/" + cfg.getOwner() + "/" + cfg.getRepo() + "/contents/" + this.$path + "?ref=" + cfg.getBranch();
                Request.Builder reqBuilder = new Request.Builder().url(url).header("Accept", "application/vnd.github+json").header("X-GitHub-Api-Version", "2022-11-28");
                if (!StringsKt.isBlank(token)) {
                    reqBuilder.header("Authorization", "Bearer " + token);
                }
                okHttpClient = this.this$0.client;
                Response execute = okHttpClient.newCall(reqBuilder.build()).execute();
                String str = this.$path;
                AdminRepository adminRepository = this.this$0;
                try {
                    Response response = execute;
                    try {
                        if (response.isSuccessful()) {
                            ResponseBody body = response.body();
                            if (body == null || (string = body.string()) == null) {
                                throw new IllegalStateException("空响应");
                            }
                            JSONObject jSONObject = new JSONObject(string);
                            String optString = jSONObject.optString("sha", "");
                            String optString2 = jSONObject.optString("content", "");
                            Intrinsics.checkNotNullExpressionValue(optString2, "optString(...)");
                            String replace$default = StringsKt.replace$default(StringsKt.replace$default(optString2, "\n", "", false, 4, (Object) null), "\r", "", false, 4, (Object) null);
                            if (!(replace$default.length() > 0)) {
                                String optString3 = jSONObject.optString("download_url", "");
                                Intrinsics.checkNotNull(optString3);
                                if (StringsKt.isBlank(optString3)) {
                                    throw new IllegalStateException("未返回文件内容");
                                }
                                Request.Builder url2 = new Request.Builder().url(optString3);
                                if (!StringsKt.isBlank(token)) {
                                    url2.header("Authorization", "Bearer " + token);
                                }
                                Request build = url2.build();
                                okHttpClient2 = adminRepository.client;
                                Response execute2 = okHttpClient2.newCall(build).execute();
                                try {
                                    Response response2 = execute2;
                                    if (!response2.isSuccessful()) {
                                        try {
                                            try {
                                                throw new IllegalStateException("下载大文件失败 HTTP " + response2.code());
                                            } catch (Throwable th3) {
                                                th2 = th3;
                                            }
                                        } catch (Throwable th4) {
                                            th2 = th4;
                                        }
                                    }
                                    try {
                                        ResponseBody body2 = response2.body();
                                        if (body2 == null || (bytes = body2.bytes()) == null) {
                                            throw new IllegalStateException("空内容");
                                        }
                                        CloseableKt.closeFinally(execute2, null);
                                        bArr = bytes;
                                    } catch (Throwable th5) {
                                        th2 = th5;
                                    }
                                } catch (Throwable th6) {
                                    th2 = th6;
                                }
                                throw th2;
                            }
                            bArr = Base64.decode(replace$default, 0);
                            Intrinsics.checkNotNull(bArr);
                            String str2 = new String(bArr, Charsets.UTF_8);
                            adminRepository.saveLocalCache(str2);
                            Pair pair = new Pair(str2, optString);
                            CloseableKt.closeFinally(execute, null);
                            return pair;
                        }
                        throw new IllegalStateException("读取失败 HTTP " + response.code() + ": " + str);
                    } catch (Throwable th7) {
                        th = th7;
                        try {
                            throw th;
                        } catch (Throwable th8) {
                            CloseableKt.closeFinally(execute, th);
                            throw th8;
                        }
                    }
                } catch (Throwable th9) {
                    th = th9;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
