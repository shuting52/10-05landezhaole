package com.example.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import com.example.model.GithubConfig;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.io.CloseableKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.Dispatchers;
import okhttp3.OkHttpClient;
/* compiled from: AdminRepository.kt */
@Metadata(d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u0000 02\u00020\u0001:\u00010B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\fJ\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u0011J\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\f2\b\b\u0002\u0010\u0017\u001a\u00020\u0015J\u0016\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0019\u001a\u00020\u0015J\u0010\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\fH\u0002J$\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f0\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\fH\u0086@¢\u0006\u0002\u0010\u001fJ$\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f0\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\fH\u0086@¢\u0006\u0002\u0010\u001fJ\u0006\u0010!\u001a\u00020\fJ\u000e\u0010\"\u001a\u00020\u000e2\u0006\u0010#\u001a\u00020\fJ<\u0010$\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00150\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\f2\u0006\u0010%\u001a\u00020\f2\u0006\u0010&\u001a\u00020\f2\u0006\u0010'\u001a\u00020\fH\u0086@¢\u0006\u0002\u0010(J,\u0010)\u001a\u00020\f2\u0006\u0010*\u001a\u00020+2\n\b\u0002\u0010,\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010-\u001a\u00020\fH\u0086@¢\u0006\u0002\u0010.J\u0018\u0010/\u001a\u00020\u00152\b\b\u0002\u0010\u001e\u001a\u00020\fH\u0086@¢\u0006\u0002\u0010\u001fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000¨\u00061"}, d2 = {"Lcom/example/data/AdminRepository;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "client", "Lokhttp3/OkHttpClient;", "getToken", "", "setToken", "", "token", "getConfig", "Lcom/example/model/GithubConfig;", "setConfig", "cfg", "getNotificationPref", "", "key", "default", "setNotificationPref", "value", "looksLikeHtml", "text", "ghReadText", "Lkotlin/Pair;", "path", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "ghReadMirror", "readLocalOrBundled", "saveLocalCache", "jsonText", "ghWriteText", "content", "sha", "message", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "uploadBinaryFile", "uri", "Landroid/net/Uri;", "customFileName", "subFolder", "(Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "purgeCdn", "Companion", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class AdminRepository {
    private static final String CACHE_FILE_NAME = "cached_admin_data.json";
    public static final String CONFIG_PATH = "admin-data.json";
    public static final String DEFAULT_BRANCH = "main";
    public static final String DEFAULT_OWNER = "shuting52";
    public static final String DEFAULT_REPO = "10-05landezhaole";
    private static final String PLACEHOLDER_TOKEN = "YOUR_GITHUB_PAT_HERE";
    private final OkHttpClient client;
    private final Context context;
    private final SharedPreferences prefs;
    public static final Companion Companion = new Companion(null);
    public static final int $stable = 8;

    public AdminRepository(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
        this.prefs = this.context.getSharedPreferences("lzdz_admin_prefs", 0);
        this.client = new OkHttpClient.Builder().connectTimeout(15L, TimeUnit.SECONDS).readTimeout(15L, TimeUnit.SECONDS).writeTimeout(20L, TimeUnit.SECONDS).build();
    }

    /* compiled from: AdminRepository.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lcom/example/data/AdminRepository$Companion;", "", "<init>", "()V", "DEFAULT_OWNER", "", "DEFAULT_REPO", "DEFAULT_BRANCH", "CONFIG_PATH", "CACHE_FILE_NAME", "PLACEHOLDER_TOKEN", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    /* loaded from: classes5.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final String getToken() {
        String fromBuildConfig;
        String string = this.prefs.getString("lzdz_gh_token", "");
        String saved = (string == null || (saved = StringsKt.trim((CharSequence) string).toString()) == null) ? "" : "";
        if (!StringsKt.isBlank(saved) && !Intrinsics.areEqual(saved, "YOUR_GITHUB_PAT_HERE")) {
            return saved;
        }
        try {
            fromBuildConfig = StringsKt.trim((CharSequence) "YOUR_GITHUB_PAT_HERE").toString();
        } catch (Exception e) {
            fromBuildConfig = "";
        }
        return (StringsKt.isBlank(fromBuildConfig) || Intrinsics.areEqual(fromBuildConfig, "YOUR_GITHUB_PAT_HERE")) ? "" : fromBuildConfig;
    }

    public final void setToken(String token) {
        Intrinsics.checkNotNullParameter(token, "token");
        this.prefs.edit().putString("lzdz_gh_token", StringsKt.trim((CharSequence) token).toString()).apply();
    }

    public final GithubConfig getConfig() {
        SharedPreferences sharedPreferences = this.prefs;
        String owner = DEFAULT_OWNER;
        String string = sharedPreferences.getString("lzdz_gh_owner", DEFAULT_OWNER);
        if (string != null) {
            String str = string;
            if (!StringsKt.isBlank(str)) {
                owner = str;
            }
            owner = owner;
        }
        SharedPreferences sharedPreferences2 = this.prefs;
        String repo = DEFAULT_REPO;
        String string2 = sharedPreferences2.getString("lzdz_gh_repo", DEFAULT_REPO);
        if (string2 != null) {
            String str2 = string2;
            if (!StringsKt.isBlank(str2)) {
                repo = str2;
            }
            repo = repo;
        }
        SharedPreferences sharedPreferences3 = this.prefs;
        String branch = DEFAULT_BRANCH;
        String string3 = sharedPreferences3.getString("lzdz_gh_branch", DEFAULT_BRANCH);
        if (string3 != null) {
            String str3 = string3;
            if (!StringsKt.isBlank(str3)) {
                branch = str3;
            }
            branch = branch;
        }
        return new GithubConfig(owner, repo, branch);
    }

    public final void setConfig(GithubConfig cfg) {
        Intrinsics.checkNotNullParameter(cfg, "cfg");
        SharedPreferences.Editor edit = this.prefs.edit();
        String obj = StringsKt.trim((CharSequence) cfg.getOwner()).toString();
        if (StringsKt.isBlank(obj)) {
            obj = DEFAULT_OWNER;
        }
        SharedPreferences.Editor putString = edit.putString("lzdz_gh_owner", obj);
        String obj2 = StringsKt.trim((CharSequence) cfg.getRepo()).toString();
        if (StringsKt.isBlank(obj2)) {
            obj2 = DEFAULT_REPO;
        }
        SharedPreferences.Editor putString2 = putString.putString("lzdz_gh_repo", obj2);
        String obj3 = StringsKt.trim((CharSequence) cfg.getBranch()).toString();
        if (StringsKt.isBlank(obj3)) {
            obj3 = DEFAULT_BRANCH;
        }
        putString2.putString("lzdz_gh_branch", obj3).apply();
    }

    public static /* synthetic */ boolean getNotificationPref$default(AdminRepository adminRepository, String str, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return adminRepository.getNotificationPref(str, z);
    }

    public final boolean getNotificationPref(String key, boolean z) {
        Intrinsics.checkNotNullParameter(key, "key");
        return this.prefs.getBoolean("notif_" + key, z);
    }

    public final void setNotificationPref(String key, boolean value) {
        Intrinsics.checkNotNullParameter(key, "key");
        this.prefs.edit().putBoolean("notif_" + key, value).apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean looksLikeHtml(String text) {
        String obj = StringsKt.trim((CharSequence) text).toString();
        Locale ROOT = Locale.ROOT;
        Intrinsics.checkNotNullExpressionValue(ROOT, "ROOT");
        String t = obj.toLowerCase(ROOT);
        Intrinsics.checkNotNullExpressionValue(t, "toLowerCase(...)");
        return StringsKt.startsWith$default(t, "<!doctype", false, 2, (Object) null) || StringsKt.startsWith$default(t, "<html", false, 2, (Object) null) || StringsKt.startsWith$default(t, "<head", false, 2, (Object) null) || StringsKt.startsWith$default(t, "<body", false, 2, (Object) null);
    }

    public static /* synthetic */ Object ghReadText$default(AdminRepository adminRepository, String str, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            str = CONFIG_PATH;
        }
        return adminRepository.ghReadText(str, continuation);
    }

    public final Object ghReadText(String path, Continuation<? super Pair<String, String>> continuation) {
        return BuildersKt.withContext(Dispatchers.getIO(), new AdminRepository$ghReadText$2(this, path, null), continuation);
    }

    public static /* synthetic */ Object ghReadMirror$default(AdminRepository adminRepository, String str, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            str = CONFIG_PATH;
        }
        return adminRepository.ghReadMirror(str, continuation);
    }

    public final Object ghReadMirror(String path, Continuation<? super Pair<String, String>> continuation) {
        return BuildersKt.withContext(Dispatchers.getIO(), new AdminRepository$ghReadMirror$2(this, path, null), continuation);
    }

    public final String readLocalOrBundled() {
        try {
            File file = this.context.getFileStreamPath(CACHE_FILE_NAME);
            if (file != null && file.exists() && file.length() > 100) {
                FileInputStream openFileInput = this.context.openFileInput(CACHE_FILE_NAME);
                Intrinsics.checkNotNullExpressionValue(openFileInput, "openFileInput(...)");
                InputStreamReader inputStreamReader = new InputStreamReader(openFileInput, Charsets.UTF_8);
                BufferedReader bufferedReader = inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, 8192);
                String readText = TextStreamsKt.readText(bufferedReader);
                CloseableKt.closeFinally(bufferedReader, null);
                return readText;
            }
        } catch (Exception e) {
        }
        InputStream open = this.context.getAssets().open(CONFIG_PATH);
        Intrinsics.checkNotNullExpressionValue(open, "open(...)");
        InputStreamReader inputStreamReader2 = new InputStreamReader(open, Charsets.UTF_8);
        BufferedReader bufferedReader2 = inputStreamReader2 instanceof BufferedReader ? (BufferedReader) inputStreamReader2 : new BufferedReader(inputStreamReader2, 8192);
        try {
            String readText2 = TextStreamsKt.readText(bufferedReader2);
            CloseableKt.closeFinally(bufferedReader2, null);
            return readText2;
        } finally {
        }
    }

    public final void saveLocalCache(String jsonText) {
        Intrinsics.checkNotNullParameter(jsonText, "jsonText");
        try {
            FileOutputStream openFileOutput = this.context.openFileOutput(CACHE_FILE_NAME, 0);
            byte[] bytes = jsonText.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
            openFileOutput.write(bytes);
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(openFileOutput, null);
        } catch (Exception e) {
        }
    }

    public static /* synthetic */ Object ghWriteText$default(AdminRepository adminRepository, String str, String str2, String str3, String str4, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            str = CONFIG_PATH;
        }
        return adminRepository.ghWriteText(str, str2, str3, str4, continuation);
    }

    public final Object ghWriteText(String path, String content, String sha, String message, Continuation<? super Pair<String, Boolean>> continuation) {
        return BuildersKt.withContext(Dispatchers.getIO(), new AdminRepository$ghWriteText$2(this, content, sha, path, message, null), continuation);
    }

    public static /* synthetic */ Object uploadBinaryFile$default(AdminRepository adminRepository, Uri uri, String str, String str2, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            str = null;
        }
        if ((i & 4) != 0) {
            str2 = "dist/uploads";
        }
        return adminRepository.uploadBinaryFile(uri, str, str2, continuation);
    }

    public final Object uploadBinaryFile(Uri uri, String customFileName, String subFolder, Continuation<? super String> continuation) {
        return BuildersKt.withContext(Dispatchers.getIO(), new AdminRepository$uploadBinaryFile$2(this, customFileName, uri, subFolder, null), continuation);
    }

    public static /* synthetic */ Object purgeCdn$default(AdminRepository adminRepository, String str, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            str = CONFIG_PATH;
        }
        return adminRepository.purgeCdn(str, continuation);
    }

    public final Object purgeCdn(String path, Continuation<? super Boolean> continuation) {
        return BuildersKt.withContext(Dispatchers.getIO(), new AdminRepository$purgeCdn$2(this, path, null), continuation);
    }
}
