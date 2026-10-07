package com.example.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import androidx.autofill.HintConstants;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelKt;
import com.example.data.AdminRepository;
import com.example.model.ActivityLog;
import com.example.model.AdminScreen;
import com.example.model.ButtonType;
import com.example.model.CardStatus;
import com.example.model.CategoryItem;
import com.example.model.ConnState;
import com.example.model.ConsoleConfig;
import com.example.model.FullSettingsConfig;
import com.example.model.GithubConfig;
import com.example.model.IpMonitorConfig;
import com.example.model.LogActionType;
import com.example.model.MarqueeConfig;
import com.example.model.ResourceButton;
import com.example.model.ResourceCard;
import com.example.model.SkillItem;
import com.example.model.SplashConfig;
import com.example.model.SubCategoryItem;
import com.example.model.TextItem;
import com.example.model.ThemeKitConfig;
import com.example.model.ToolItem;
import com.example.model.UpdateDialogConfig;
import com.example.model.WelcomeConfig;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;
import org.json.JSONObject;
/* compiled from: AdminViewModel.kt */
@Metadata(d1 = {"\u0000\u0080\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019J\u000e\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u001b\u001a\u00020\u0012J\u0006\u0010\u001c\u001a\u00020\u0017J\u000e\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u001e\u001a\u00020\u0012J(\u0010\u001f\u001a\u00020\u00172\u0006\u0010 \u001a\u00020\u00122\u0006\u0010!\u001a\u00020\u00122\u0006\u0010\"\u001a\u00020\u00122\b\b\u0002\u0010#\u001a\u00020\u0012J\u0016\u0010$\u001a\u00020\u00172\u0006\u0010%\u001a\u00020\u00122\u0006\u0010&\u001a\u00020'J\u0016\u0010(\u001a\u00020\u00172\u0006\u0010)\u001a\u00020\u00122\u0006\u0010*\u001a\u00020\u0012J\b\u0010+\u001a\u00020\u0012H\u0002J\u0010\u0010,\u001a\u00020\u00172\b\b\u0002\u0010-\u001a\u00020'J\u0006\u0010.\u001a\u00020\u0017J.\u0010/\u001a\u00020\u00172\u0006\u00100\u001a\u00020\u00102\u0006\u00101\u001a\u0002022\u0006\u00103\u001a\u00020\u00122\u0006\u00104\u001a\u00020\u0012H\u0082@¢\u0006\u0002\u00105J\u0018\u00106\u001a\u00020\u00172\u0006\u00107\u001a\u0002082\u0006\u00109\u001a\u00020\u0012H\u0002J(\u0010:\u001a\u00020'2\u0006\u00100\u001a\u00020\u00102\u0006\u0010;\u001a\u00020\u00122\b\b\u0002\u0010<\u001a\u00020\u0012H\u0082@¢\u0006\u0002\u0010=J|\u0010>\u001a\u00020\u00172\b\u0010?\u001a\u0004\u0018\u00010@2\u0006\u0010A\u001a\u00020\u00122\u0006\u0010B\u001a\u00020\u00122\u0006\u0010C\u001a\u00020\u00122\u0006\u0010D\u001a\u00020E2\u0006\u0010F\u001a\u00020G2\u0006\u0010H\u001a\u00020\u00122\b\b\u0002\u0010I\u001a\u00020\u00122\b\b\u0002\u0010J\u001a\u00020\u00122\b\b\u0002\u0010K\u001a\u00020\u00122\b\b\u0002\u0010L\u001a\u00020\u00122\b\b\u0002\u0010M\u001a\u00020\u00122\b\b\u0002\u0010N\u001a\u00020\u0012J\u000e\u0010O\u001a\u00020\u00172\u0006\u0010P\u001a\u00020@JP\u0010Q\u001a\u00020\u00172\u0006\u0010R\u001a\u00020S2\b\b\u0002\u0010T\u001a\u00020\u001226\u0010U\u001a2\u0012\u0013\u0012\u00110\u0012¢\u0006\f\bW\u0012\b\bA\u0012\u0004\b\b(X\u0012\u0013\u0012\u00110\u0012¢\u0006\f\bW\u0012\b\bA\u0012\u0004\b\b(Y\u0012\u0004\u0012\u00020\u00170VJx\u0010Z\u001a\u00020\u00172\b\u0010[\u001a\u0004\u0018\u00010\\2\u0006\u0010A\u001a\u00020\u00122\u0006\u0010C\u001a\u00020\u00122\u0006\u0010]\u001a\u00020\u00122\b\b\u0002\u0010^\u001a\u00020\u00122\b\b\u0002\u0010L\u001a\u00020\u00122\b\b\u0002\u0010M\u001a\u00020\u00122\b\b\u0002\u0010_\u001a\u00020\u00122\b\b\u0002\u0010`\u001a\u00020\u00122\b\b\u0002\u0010a\u001a\u00020\u00122\b\b\u0002\u0010b\u001a\u00020\u00122\b\b\u0002\u0010c\u001a\u00020\u0012J\u000e\u0010d\u001a\u00020\u00172\u0006\u0010e\u001a\u00020\\Jp\u0010f\u001a\u00020\u00172\b\u0010[\u001a\u0004\u0018\u00010g2\u0006\u0010h\u001a\u00020\u00122\u0006\u0010]\u001a\u00020\u00122\u0006\u0010i\u001a\u00020\u00122\u0006\u0010j\u001a\u00020\u00122\u0006\u0010C\u001a\u00020\u00122\u0006\u0010^\u001a\u00020\u00122\u0006\u0010L\u001a\u00020\u00122\u0006\u0010_\u001a\u00020\u00122\u0006\u0010a\u001a\u00020\u00122\u0006\u0010k\u001a\u00020\u00122\u0006\u0010b\u001a\u00020\u00122\u0006\u0010c\u001a\u00020\u0012J\u000e\u0010l\u001a\u00020\u00172\u0006\u0010m\u001a\u00020gJ\u001e\u0010n\u001a\u00020\u00172\u0006\u0010o\u001a\u00020\u00122\u0006\u00109\u001a\u00020\u00122\u0006\u0010p\u001a\u00020'J\u000e\u0010q\u001a\u00020\u00172\u0006\u0010r\u001a\u00020sJ<\u0010t\u001a\u00020\u00172\b\u0010[\u001a\u0004\u0018\u00010u2\u0006\u0010A\u001a\u00020\u00122\b\b\u0002\u0010v\u001a\u00020\u00122\b\b\u0002\u0010]\u001a\u00020\u00122\u000e\b\u0002\u0010w\u001a\b\u0012\u0004\u0012\u00020y0xJ\u000e\u0010z\u001a\u00020\u00172\u0006\u0010{\u001a\u00020uJ\u0016\u0010|\u001a\u00020\u00172\u0006\u0010}\u001a\u00020\u00122\u0006\u0010~\u001a\u00020\u007fJ\u0007\u0010\u0080\u0001\u001a\u00020\u0017J\u0011\u0010\u0081\u0001\u001a\u00020\u00172\b\u0010\u0082\u0001\u001a\u00030\u0083\u0001J\u0011\u0010\u0084\u0001\u001a\u00020\u00172\b\u0010\u0082\u0001\u001a\u00030\u0085\u0001J\u0011\u0010\u0086\u0001\u001a\u00020\u00172\b\u0010\u0082\u0001\u001a\u00030\u0087\u0001J>\u0010\u0088\u0001\u001a\u00020\u00172\b\u0010\u0089\u0001\u001a\u00030\u008a\u00012\u0007\u0010\u008b\u0001\u001a\u00020\u00122\u0007\u0010\u008c\u0001\u001a\u00020\u007f2\u0007\u0010\u008d\u0001\u001a\u00020'2\u0007\u0010\u008e\u0001\u001a\u00020\u00122\u0007\u0010\u008f\u0001\u001a\u00020\u0012J4\u0010\u0090\u0001\u001a\u00020\u00172\b\u0010\u0091\u0001\u001a\u00030\u0092\u00012\b\u0010\u0093\u0001\u001a\u00030\u0094\u00012\u0007\u0010\u0095\u0001\u001a\u00020\u00122\u000e\u0010\u0096\u0001\u001a\t\u0012\u0005\u0012\u00030\u0097\u00010xJ\u0011\u0010\u0098\u0001\u001a\u00020\u00172\b\u0010\u0082\u0001\u001a\u00030\u0099\u0001J\u0011\u0010\u009a\u0001\u001a\u00020\u00172\b\u0010\u0082\u0001\u001a\u00030\u009b\u0001J\u0007\u0010\u009c\u0001\u001a\u00020\u0017J\u0007\u0010\u009d\u0001\u001a\u00020\u0017J\u0012\u0010\u009e\u0001\u001a\u00020\u00172\t\b\u0002\u0010\u009f\u0001\u001a\u00020\u0012R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\f¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006 \u0001"}, d2 = {"Lcom/example/viewmodel/AdminViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "<init>", "(Landroid/app/Application;)V", "repo", "Lcom/example/data/AdminRepository;", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/viewmodel/AdminUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "rootJson", "Lorg/json/JSONObject;", "currentSha", "", "customLogs", "", "Lcom/example/model/ActivityLog;", "navigateTo", "", "screen", "Lcom/example/model/AdminScreen;", "showToast", NotificationCompat.CATEGORY_MESSAGE, "clearToast", "setDateRange", "range", "updateGithubInputs", "token", "owner", "repoName", "branch", "toggleNotification", "key", "enabled", "", "updateBasicSettingsInputs", "appName", "slogan", "nowTimeStr", "loadAdmin", "preferMirror", "connectGithub", "parseAndPublishState", "json", "connState", "Lcom/example/model/ConnState;", "syncTime", "errMsg", "(Lorg/json/JSONObject;Lcom/example/model/ConnState;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "recordLog", "action", "Lcom/example/model/LogActionType;", "content", "persistAndRefresh", "commitMsg", "targetMode", "(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveCard", "existingCard", "Lcom/example/model/ResourceCard;", HintConstants.AUTOFILL_HINT_NAME, "description", "url", "buttonType", "Lcom/example/model/ButtonType;", NotificationCompat.CATEGORY_STATUS, "Lcom/example/model/CardStatus;", "categoryName", "subcatId", "icon", "fallbackText", "badge", "badgeType", "highlights", "deleteCard", "card", "uploadLocalFile", "uri", "Landroid/net/Uri;", "subFolder", "onSuccess", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "downloadUrl", "fileName", "saveSoftware", "existing", "Lcom/example/model/ResourceButton;", "desc", "author", "tags", "apkUrl", "previewUrl", "iconUrl", "mode", "deleteSoftware", "btn", "saveSkill", "Lcom/example/model/SkillItem;", "title", "promptType", "prompt", "mediaUrl", "deleteSkill", "skill", "saveText", "keyName", "isNew", "deleteText", "item", "Lcom/example/model/TextItem;", "saveCategory", "Lcom/example/model/CategoryItem;", "iconKey", "subcategories", "", "Lcom/example/model/SubCategoryItem;", "deleteCategory", "cat", "moveCategory", "id", "dir", "", "saveCategoryOrder", "saveSplashConfig", "cfg", "Lcom/example/model/SplashConfig;", "saveWelcomeConfig", "Lcom/example/model/WelcomeConfig;", "saveMarqueeConfig", "Lcom/example/model/MarqueeConfig;", "saveUpdateDialogAndVersionConfig", "updCfg", "Lcom/example/model/UpdateDialogConfig;", "vName", "vCode", "vForce", "vApkUrl", "vApkUrlRaw", "saveMiscModulesConfig", "consoleCfg", "Lcom/example/model/ConsoleConfig;", "ipCfg", "Lcom/example/model/IpMonitorConfig;", "aiNoticeText", "tools", "Lcom/example/model/ToolItem;", "saveThemeKitConfig", "Lcom/example/model/ThemeKitConfig;", "saveFullSettings", "Lcom/example/model/FullSettingsConfig;", "saveBasicSettings", "applyToDevice", "publishRelease", "changeDesc", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes4.dex */
public final class AdminViewModel extends AndroidViewModel {
    public static final int $stable = 8;
    private final MutableStateFlow<AdminUiState> _uiState;
    private String currentSha;
    private final List<ActivityLog> customLogs;
    private final AdminRepository repo;
    private JSONObject rootJson;
    private final StateFlow<AdminUiState> uiState;

    /* compiled from: AdminViewModel.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    /* loaded from: classes4.dex */
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[LogActionType.values().length];
            try {
                iArr[LogActionType.CREATE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[LogActionType.UPDATE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[LogActionType.PUBLISH.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[LogActionType.DELETE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[LogActionType.REVIEW.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel(Application application) {
        super(application);
        Intrinsics.checkNotNullParameter(application, "application");
        Context applicationContext = application.getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "getApplicationContext(...)");
        this.repo = new AdminRepository(applicationContext);
        this._uiState = StateFlowKt.MutableStateFlow(new AdminUiState(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, false, false, false, false, false, false, false, null, -1, 8191, null));
        this.uiState = FlowKt.asStateFlow(this._uiState);
        this.currentSha = "";
        this.customLogs = new ArrayList();
        GithubConfig cfg = this.repo.getConfig();
        MutableStateFlow mutableStateFlow = this._uiState;
        while (true) {
            AdminUiState value = mutableStateFlow.getValue();
            GithubConfig cfg2 = cfg;
            if (mutableStateFlow.compareAndSet(value, AdminUiState.copy$default(value, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, cfg.getOwner(), cfg.getRepo(), cfg.getBranch(), this.repo.getToken(), this.repo.getNotificationPref("review", false), this.repo.getNotificationPref("download", false), this.repo.getNotificationPref("weekly", false), false, false, false, false, null, -1, 7937, null))) {
                loadAdmin(false);
                return;
            }
            cfg = cfg2;
        }
    }

    public final StateFlow<AdminUiState> getUiState() {
        return this.uiState;
    }

    public final void navigateTo(AdminScreen screen) {
        AdminScreen screen2 = screen;
        Intrinsics.checkNotNullParameter(screen2, "screen");
        MutableStateFlow mutableStateFlow = this._uiState;
        while (true) {
            AdminUiState value = mutableStateFlow.getValue();
            MutableStateFlow mutableStateFlow2 = mutableStateFlow;
            if (mutableStateFlow2.compareAndSet(value, AdminUiState.copy$default(value, screen2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, false, false, false, false, false, false, false, null, -2, 8191, null))) {
                return;
            }
            screen2 = screen;
            mutableStateFlow = mutableStateFlow2;
        }
    }

    public final void showToast(String msg) {
        String msg2 = msg;
        Intrinsics.checkNotNullParameter(msg2, "msg");
        MutableStateFlow mutableStateFlow = this._uiState;
        while (true) {
            AdminUiState value = mutableStateFlow.getValue();
            MutableStateFlow mutableStateFlow2 = mutableStateFlow;
            if (mutableStateFlow2.compareAndSet(value, AdminUiState.copy$default(value, null, null, null, null, null, msg2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, false, false, false, false, false, false, false, null, -33, 8191, null))) {
                return;
            }
            msg2 = msg;
            mutableStateFlow = mutableStateFlow2;
        }
    }

    public final void clearToast() {
        AdminUiState value;
        MutableStateFlow mutableStateFlow = this._uiState;
        do {
            value = mutableStateFlow.getValue();
        } while (!mutableStateFlow.compareAndSet(value, AdminUiState.copy$default(value, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, false, false, false, false, false, false, false, null, -33, 8191, null)));
    }

    public final void setDateRange(String range) {
        Intrinsics.checkNotNullParameter(range, "range");
        MutableStateFlow mutableStateFlow = this._uiState;
        while (true) {
            AdminUiState value = mutableStateFlow.getValue();
            MutableStateFlow mutableStateFlow2 = mutableStateFlow;
            if (mutableStateFlow2.compareAndSet(value, AdminUiState.copy$default(value, null, null, null, null, null, null, range, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, false, false, false, false, false, false, false, null, -65, 8191, null))) {
                showToast("已切换到「" + range + "」");
                return;
            }
            mutableStateFlow = mutableStateFlow2;
        }
    }

    public static /* synthetic */ void updateGithubInputs$default(AdminViewModel adminViewModel, String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 8) != 0) {
            str4 = adminViewModel._uiState.getValue().getGithubBranch();
        }
        adminViewModel.updateGithubInputs(str, str2, str3, str4);
    }

    public final void updateGithubInputs(String token, String owner, String repoName, String branch) {
        Intrinsics.checkNotNullParameter(token, "token");
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(repoName, "repoName");
        Intrinsics.checkNotNullParameter(branch, "branch");
        MutableStateFlow mutableStateFlow = this._uiState;
        while (true) {
            AdminUiState value = mutableStateFlow.getValue();
            MutableStateFlow mutableStateFlow2 = mutableStateFlow;
            if (mutableStateFlow2.compareAndSet(value, AdminUiState.copy$default(value, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, owner, repoName, branch, token, false, false, false, false, false, false, false, null, -1, 8161, null))) {
                return;
            }
            mutableStateFlow = mutableStateFlow2;
        }
    }

    public final void toggleNotification(String key, boolean enabled) {
        MutableStateFlow mutableStateFlow;
        AdminUiState adminUiState;
        AdminUiState copy$default;
        Intrinsics.checkNotNullParameter(key, "key");
        this.repo.setNotificationPref(key, enabled);
        MutableStateFlow mutableStateFlow2 = this._uiState;
        while (true) {
            AdminUiState value = mutableStateFlow2.getValue();
            AdminUiState adminUiState2 = value;
            switch (key.hashCode()) {
                case -934348968:
                    mutableStateFlow = mutableStateFlow2;
                    adminUiState = value;
                    if (key.equals("review")) {
                        copy$default = AdminUiState.copy$default(adminUiState2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, enabled, false, false, false, false, false, false, null, -1, 8159, null);
                        break;
                    }
                    copy$default = adminUiState2;
                    break;
                case -791707519:
                    mutableStateFlow = mutableStateFlow2;
                    adminUiState = value;
                    if (key.equals("weekly")) {
                        copy$default = AdminUiState.copy$default(adminUiState2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, false, false, enabled, false, false, false, false, null, -1, 8063, null);
                        break;
                    }
                    copy$default = adminUiState2;
                    break;
                case 1427818632:
                    if (key.equals("download")) {
                        mutableStateFlow = mutableStateFlow2;
                        adminUiState = value;
                        copy$default = AdminUiState.copy$default(adminUiState2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, false, enabled, false, false, false, false, false, null, -1, 8127, null);
                        break;
                    } else {
                        mutableStateFlow = mutableStateFlow2;
                        adminUiState = value;
                        copy$default = adminUiState2;
                        break;
                    }
                default:
                    mutableStateFlow = mutableStateFlow2;
                    adminUiState = value;
                    copy$default = adminUiState2;
                    break;
            }
            if (mutableStateFlow.compareAndSet(adminUiState, copy$default)) {
                showToast(enabled ? "已开启通知" : "已关闭通知");
                return;
            }
            mutableStateFlow2 = mutableStateFlow;
        }
    }

    public final void updateBasicSettingsInputs(String appName, String slogan) {
        String appName2 = appName;
        Intrinsics.checkNotNullParameter(appName2, "appName");
        String slogan2 = slogan;
        Intrinsics.checkNotNullParameter(slogan2, "slogan");
        MutableStateFlow mutableStateFlow = this._uiState;
        while (true) {
            AdminUiState value = mutableStateFlow.getValue();
            AdminUiState adminUiState = value;
            MutableStateFlow mutableStateFlow2 = mutableStateFlow;
            if (mutableStateFlow2.compareAndSet(value, AdminUiState.copy$default(adminUiState, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, FullSettingsConfig.copy$default(adminUiState.getFullSettings(), appName2, slogan2, null, null, null, null, null, null, null, null, null, null, false, null, false, null, 65532, null), 0, null, false, null, null, null, appName, slogan, null, null, null, null, false, false, false, false, false, false, false, null, 2130706431, 8190, null))) {
                return;
            }
            appName2 = appName;
            slogan2 = slogan;
            mutableStateFlow = mutableStateFlow2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String nowTimeStr() {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date());
        Intrinsics.checkNotNullExpressionValue(format, "format(...)");
        return format;
    }

    public static /* synthetic */ void loadAdmin$default(AdminViewModel adminViewModel, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        adminViewModel.loadAdmin(z);
    }

    public final void loadAdmin(boolean preferMirror) {
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$loadAdmin$1(this, preferMirror, null), 3, null);
    }

    public final void connectGithub() {
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$connectGithub$1(this, null), 3, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object parseAndPublishState(JSONObject json, ConnState connState, String syncTime, String errMsg, Continuation<? super Unit> continuation) {
        Object withContext = BuildersKt.withContext(Dispatchers.getDefault(), new AdminViewModel$parseAndPublishState$2(json, syncTime, this, connState, errMsg, null), continuation);
        return withContext == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? withContext : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void recordLog(LogActionType action, String content) {
        long color;
        String t = nowTimeStr();
        switch (WhenMappings.$EnumSwitchMapping$0[action.ordinal()]) {
            case 1:
                color = 4281302363L;
                break;
            case 2:
                color = 4291402301L;
                break;
            case 3:
                color = 4291181618L;
                break;
            case 4:
                color = 4291181618L;
                break;
            case 5:
                color = 4279843645L;
                break;
            default:
                throw new NoWhenBranchMatchedException();
        }
        this.customLogs.add(0, new ActivityLog("log_" + System.currentTimeMillis(), "管理员", color, action, content, t, this.repo.getConfig().getOwner() + "/" + this.repo.getConfig().getRepo()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:10:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0034  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x007a  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00f4  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00f7  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0145 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0146  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object persistAndRefresh(org.json.JSONObject r18, java.lang.String r19, java.lang.String r20, kotlin.coroutines.Continuation<? super java.lang.Boolean> r21) {
        /*
            Method dump skipped, instructions count: 350
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel.persistAndRefresh(org.json.JSONObject, java.lang.String, java.lang.String, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Object persistAndRefresh$default(AdminViewModel adminViewModel, JSONObject jSONObject, String str, String str2, Continuation continuation, int i, Object obj) {
        if ((i & 4) != 0) {
            str2 = "content";
        }
        return adminViewModel.persistAndRefresh(jSONObject, str, str2, continuation);
    }

    public final void saveCard(ResourceCard existingCard, String name, String description, String url, ButtonType buttonType, CardStatus status, String categoryName, String subcatId, String icon, String fallbackText, String badge, String badgeType, String highlights) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(description, "description");
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(buttonType, "buttonType");
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(categoryName, "categoryName");
        Intrinsics.checkNotNullParameter(subcatId, "subcatId");
        Intrinsics.checkNotNullParameter(icon, "icon");
        Intrinsics.checkNotNullParameter(fallbackText, "fallbackText");
        Intrinsics.checkNotNullParameter(badge, "badge");
        Intrinsics.checkNotNullParameter(badgeType, "badgeType");
        Intrinsics.checkNotNullParameter(highlights, "highlights");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$saveCard$1(this, categoryName, fallbackText, existingCard, name, description, url, icon, badge, badgeType, subcatId, highlights, buttonType, status, null), 3, null);
    }

    public final void deleteCard(ResourceCard card) {
        Intrinsics.checkNotNullParameter(card, "card");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$deleteCard$1(this, card, null), 3, null);
    }

    public static /* synthetic */ void uploadLocalFile$default(AdminViewModel adminViewModel, Uri uri, String str, Function2 function2, int i, Object obj) {
        if ((i & 2) != 0) {
            str = DebugKt.DEBUG_PROPERTY_VALUE_AUTO;
        }
        adminViewModel.uploadLocalFile(uri, str, function2);
    }

    public final void uploadLocalFile(Uri uri, String subFolder, Function2<? super String, ? super String, Unit> onSuccess) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        Intrinsics.checkNotNullParameter(subFolder, "subFolder");
        Intrinsics.checkNotNullParameter(onSuccess, "onSuccess");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$uploadLocalFile$1(this, uri, subFolder, onSuccess, null), 3, null);
    }

    public static /* synthetic */ void saveSoftware$default(AdminViewModel adminViewModel, ResourceButton resourceButton, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, int i, Object obj) {
        if ((i & 16) != 0) {
            str4 = "";
        }
        if ((i & 32) != 0) {
            str5 = "";
        }
        if ((i & 64) != 0) {
            str6 = "";
        }
        if ((i & 128) != 0) {
            str7 = "";
        }
        if ((i & 256) != 0) {
            str8 = "";
        }
        if ((i & 512) != 0) {
            str9 = "";
        }
        if ((i & 1024) != 0) {
            str10 = "";
        }
        if ((i & 2048) != 0) {
            str11 = "url";
        }
        adminViewModel.saveSoftware(resourceButton, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11);
    }

    public final void saveSoftware(ResourceButton existing, String name, String url, String desc, String author, String badge, String badgeType, String tags, String apkUrl, String previewUrl, String iconUrl, String mode) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(desc, "desc");
        Intrinsics.checkNotNullParameter(author, "author");
        Intrinsics.checkNotNullParameter(badge, "badge");
        Intrinsics.checkNotNullParameter(badgeType, "badgeType");
        Intrinsics.checkNotNullParameter(tags, "tags");
        Intrinsics.checkNotNullParameter(apkUrl, "apkUrl");
        Intrinsics.checkNotNullParameter(previewUrl, "previewUrl");
        Intrinsics.checkNotNullParameter(iconUrl, "iconUrl");
        Intrinsics.checkNotNullParameter(mode, "mode");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$saveSoftware$1(this, url, apkUrl, mode, existing, name, desc, author, badge, badgeType, tags, previewUrl, iconUrl, null), 3, null);
    }

    public final void deleteSoftware(ResourceButton btn) {
        Intrinsics.checkNotNullParameter(btn, "btn");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$deleteSoftware$1(this, btn, null), 3, null);
    }

    public final void saveSkill(SkillItem existing, String title, String desc, String promptType, String prompt, String url, String author, String badge, String tags, String previewUrl, String mediaUrl, String iconUrl, String mode) {
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(desc, "desc");
        Intrinsics.checkNotNullParameter(promptType, "promptType");
        Intrinsics.checkNotNullParameter(prompt, "prompt");
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(author, "author");
        Intrinsics.checkNotNullParameter(badge, "badge");
        Intrinsics.checkNotNullParameter(tags, "tags");
        Intrinsics.checkNotNullParameter(previewUrl, "previewUrl");
        Intrinsics.checkNotNullParameter(mediaUrl, "mediaUrl");
        Intrinsics.checkNotNullParameter(iconUrl, "iconUrl");
        Intrinsics.checkNotNullParameter(mode, "mode");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$saveSkill$1(this, url, mode, existing, title, desc, promptType, prompt, author, badge, tags, previewUrl, mediaUrl, iconUrl, null), 3, null);
    }

    public final void deleteSkill(SkillItem skill) {
        Intrinsics.checkNotNullParameter(skill, "skill");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$deleteSkill$1(this, skill, null), 3, null);
    }

    public final void saveText(String keyName, String content, boolean isNew) {
        Intrinsics.checkNotNullParameter(keyName, "keyName");
        Intrinsics.checkNotNullParameter(content, "content");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$saveText$1(this, keyName, content, isNew, null), 3, null);
    }

    public final void deleteText(TextItem item) {
        Intrinsics.checkNotNullParameter(item, "item");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$deleteText$1(this, item, null), 3, null);
    }

    public final void saveCategory(CategoryItem existing, String name, String iconKey, String desc, List<SubCategoryItem> subcategories) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(iconKey, "iconKey");
        Intrinsics.checkNotNullParameter(desc, "desc");
        Intrinsics.checkNotNullParameter(subcategories, "subcategories");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$saveCategory$1(this, subcategories, existing, name, iconKey, desc, null), 3, null);
    }

    public final void deleteCategory(CategoryItem cat) {
        Intrinsics.checkNotNullParameter(cat, "cat");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$deleteCategory$1(this, cat, null), 3, null);
    }

    public final void moveCategory(String id, int dir) {
        AdminUiState value;
        Intrinsics.checkNotNullParameter(id, "id");
        List currentList = CollectionsKt.toMutableList((Collection) CollectionsKt.sortedWith(this._uiState.getValue().getCategories(), new Comparator() { // from class: com.example.viewmodel.AdminViewModel$moveCategory$$inlined$sortedBy$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return ComparisonsKt.compareValues(Integer.valueOf(((CategoryItem) t).getOrder()), Integer.valueOf(((CategoryItem) t2).getOrder()));
            }
        }));
        int idx = 0;
        Iterator it = currentList.iterator();
        while (true) {
            if (it.hasNext()) {
                if (Intrinsics.areEqual(((CategoryItem) it.next()).getId(), id)) {
                    break;
                }
                idx++;
            } else {
                idx = -1;
                break;
            }
        }
        int target = idx + dir;
        if (idx < 0 || target < 0 || target >= currentList.size()) {
            return;
        }
        CategoryItem tmp = (CategoryItem) currentList.get(idx);
        currentList.set(idx, currentList.get(target));
        currentList.set(target, tmp);
        List list = currentList;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        int i = 0;
        for (Object obj : list) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            arrayList.add(CategoryItem.copy$default((CategoryItem) obj, null, null, 0, i + 1, null, null, null, null, 247, null));
            i = i2;
        }
        List reindexed = (List) arrayList;
        MutableStateFlow mutableStateFlow = this._uiState;
        do {
            value = mutableStateFlow.getValue();
        } while (!mutableStateFlow.compareAndSet(value, AdminUiState.copy$default(value, null, null, null, null, null, null, null, null, null, null, null, null, reindexed, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, false, false, false, false, false, false, false, null, -4097, 8191, null)));
        showToast("排序已调整，请点「保存排序」同步到本体");
    }

    public final void saveCategoryOrder() {
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$saveCategoryOrder$1(this, null), 3, null);
    }

    public final void saveSplashConfig(SplashConfig cfg) {
        Intrinsics.checkNotNullParameter(cfg, "cfg");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$saveSplashConfig$1(this, cfg, null), 3, null);
    }

    public final void saveWelcomeConfig(WelcomeConfig cfg) {
        Intrinsics.checkNotNullParameter(cfg, "cfg");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$saveWelcomeConfig$1(this, cfg, null), 3, null);
    }

    public final void saveMarqueeConfig(MarqueeConfig cfg) {
        Intrinsics.checkNotNullParameter(cfg, "cfg");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$saveMarqueeConfig$1(this, cfg, null), 3, null);
    }

    public final void saveUpdateDialogAndVersionConfig(UpdateDialogConfig updCfg, String vName, int vCode, boolean vForce, String vApkUrl, String vApkUrlRaw) {
        Intrinsics.checkNotNullParameter(updCfg, "updCfg");
        Intrinsics.checkNotNullParameter(vName, "vName");
        Intrinsics.checkNotNullParameter(vApkUrl, "vApkUrl");
        Intrinsics.checkNotNullParameter(vApkUrlRaw, "vApkUrlRaw");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$saveUpdateDialogAndVersionConfig$1(this, updCfg, vName, vCode, vForce, vApkUrl, vApkUrlRaw, null), 3, null);
    }

    public final void saveMiscModulesConfig(ConsoleConfig consoleCfg, IpMonitorConfig ipCfg, String aiNoticeText, List<ToolItem> tools) {
        Intrinsics.checkNotNullParameter(consoleCfg, "consoleCfg");
        Intrinsics.checkNotNullParameter(ipCfg, "ipCfg");
        Intrinsics.checkNotNullParameter(aiNoticeText, "aiNoticeText");
        Intrinsics.checkNotNullParameter(tools, "tools");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$saveMiscModulesConfig$1(this, consoleCfg, ipCfg, aiNoticeText, tools, null), 3, null);
    }

    public final void saveThemeKitConfig(ThemeKitConfig cfg) {
        Intrinsics.checkNotNullParameter(cfg, "cfg");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$saveThemeKitConfig$1(this, cfg, null), 3, null);
    }

    public final void saveFullSettings(FullSettingsConfig cfg) {
        Intrinsics.checkNotNullParameter(cfg, "cfg");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$saveFullSettings$1(this, cfg, null), 3, null);
    }

    public final void saveBasicSettings() {
        saveFullSettings(FullSettingsConfig.copy$default(this._uiState.getValue().getFullSettings(), this._uiState.getValue().getAppName(), this._uiState.getValue().getSlogan(), null, null, null, null, null, null, null, null, null, null, false, null, false, null, 65532, null));
    }

    public final void applyToDevice() {
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$applyToDevice$1(this, null), 3, null);
    }

    public static /* synthetic */ void publishRelease$default(AdminViewModel adminViewModel, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "更新";
        }
        adminViewModel.publishRelease(str);
    }

    public final void publishRelease(String changeDesc) {
        Intrinsics.checkNotNullParameter(changeDesc, "changeDesc");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AdminViewModel$publishRelease$1(this, changeDesc, null), 3, null);
    }
}
