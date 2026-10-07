package com.example.data;

import com.example.model.GithubConfig;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminRepository.kt */
@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\n"}, d2 = {"<anonymous>", "Lkotlin/Pair;", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.data.AdminRepository$ghReadMirror$2", f = "AdminRepository.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
/* loaded from: classes5.dex */
public final class AdminRepository$ghReadMirror$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Pair<? extends String, ? extends String>>, Object> {
    final /* synthetic */ String $path;
    int label;
    final /* synthetic */ AdminRepository this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminRepository$ghReadMirror$2(AdminRepository adminRepository, String str, Continuation<? super AdminRepository$ghReadMirror$2> continuation) {
        super(2, continuation);
        this.this$0 = adminRepository;
        this.$path = str;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminRepository$ghReadMirror$2(this.this$0, this.$path, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super Pair<? extends String, ? extends String>> continuation) {
        return invoke2(coroutineScope, (Continuation<? super Pair<String, String>>) continuation);
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super Pair<String, String>> continuation) {
        return ((AdminRepository$ghReadMirror$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        OkHttpClient okHttpClient;
        ResponseBody body;
        String string;
        boolean looksLikeHtml;
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                GithubConfig cfg = this.this$0.getConfig();
                String cacheBust = new SimpleDateFormat("yyyyMMddHHmm", Locale.US).format(new Date());
                String owner = cfg.getOwner();
                String repo = cfg.getRepo();
                String branch = cfg.getBranch();
                String str = this.$path;
                String owner2 = cfg.getOwner();
                String repo2 = cfg.getRepo();
                String branch2 = cfg.getBranch();
                String str2 = this.$path;
                String owner3 = cfg.getOwner();
                String repo3 = cfg.getRepo();
                String branch3 = cfg.getBranch();
                String str3 = this.$path;
                String owner4 = cfg.getOwner();
                String repo4 = cfg.getRepo();
                String branch4 = cfg.getBranch();
                String str4 = this.$path;
                String owner5 = cfg.getOwner();
                String repo5 = cfg.getRepo();
                String branch5 = cfg.getBranch();
                String str5 = this.$path;
                String owner6 = cfg.getOwner();
                String repo6 = cfg.getRepo();
                String branch6 = cfg.getBranch();
                String str6 = this.$path;
                String owner7 = cfg.getOwner();
                String repo7 = cfg.getRepo();
                String branch7 = cfg.getBranch();
                String str7 = this.$path;
                String owner8 = cfg.getOwner();
                List<String> mirrors = CollectionsKt.listOf((Object[]) new String[]{"https://testingcf.jsdelivr.net/gh/" + owner + "/" + repo + "@" + branch + "/" + str + "?v=" + cacheBust, "https://cdn.jsdelivr.net/gh/" + owner2 + "/" + repo2 + "@" + branch2 + "/" + str2 + "?v=" + cacheBust, "https://fastly.jsdelivr.net/gh/" + owner3 + "/" + repo3 + "@" + branch3 + "/" + str3 + "?v=" + cacheBust, "https://gcore.jsdelivr.net/gh/" + owner4 + "/" + repo4 + "@" + branch4 + "/" + str4 + "?v=" + cacheBust, "https://ghfast.top/https://raw.githubusercontent.com/" + owner5 + "/" + repo5 + "/" + branch5 + "/" + str5, "https://ghproxy.net/https://raw.githubusercontent.com/" + owner6 + "/" + repo6 + "/" + branch6 + "/" + str6, "https://raw.gitmirror.com/" + owner7 + "/" + repo7 + "/" + branch7 + "/" + str7, "https://raw.githubusercontent.com/" + owner8 + "/" + cfg.getRepo() + "/" + cfg.getBranch() + "/" + this.$path});
                for (String url : mirrors) {
                    try {
                        Request req = new Request.Builder().url(url).build();
                        okHttpClient = this.this$0.client;
                        Response execute = okHttpClient.newCall(req).execute();
                        AdminRepository adminRepository = this.this$0;
                        Response response = execute;
                        if (response.isSuccessful() && (body = response.body()) != null && (string = body.string()) != null) {
                            looksLikeHtml = adminRepository.looksLikeHtml(string);
                            if (!looksLikeHtml) {
                                new JSONObject(string);
                                adminRepository.saveLocalCache(string);
                                Pair pair = new Pair(string, "");
                                CloseableKt.closeFinally(execute, null);
                                return pair;
                            }
                        }
                        Unit unit = Unit.INSTANCE;
                        CloseableKt.closeFinally(execute, null);
                    } catch (Exception e) {
                    }
                }
                String localText = this.this$0.readLocalOrBundled();
                return new Pair(localText, "");
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
