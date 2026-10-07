package com.example.viewmodel;

import androidx.core.view.accessibility.AccessibilityEventCompat;
import com.example.model.ActivityLog;
import com.example.model.AdminScreen;
import com.example.model.CategoryItem;
import com.example.model.ConnState;
import com.example.model.ConsoleConfig;
import com.example.model.FullSettingsConfig;
import com.example.model.IpMonitorConfig;
import com.example.model.MarqueeConfig;
import com.example.model.ResourceButton;
import com.example.model.ResourceCard;
import com.example.model.SkillItem;
import com.example.model.SplashConfig;
import com.example.model.StatItem;
import com.example.model.TextItem;
import com.example.model.ThemeKitConfig;
import com.example.model.ToolItem;
import com.example.model.UpdateDialogConfig;
import com.example.model.WelcomeConfig;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminViewModel.kt */
@Metadata(d1 = {"\u0000\u0096\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b}\b\u0087\b\u0018\u00002\u00020\u0001B\u0087\u0004\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0007\u0012\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u0012\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\r\u0012\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\r\u0012\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\r\u0012\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\r\u0012\u000e\b\u0002\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\r\u0012\u000e\b\u0002\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\r\u0012\u000e\b\u0002\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001a0\r\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u001d\u0012\b\b\u0002\u0010\u001e\u001a\u00020\u001f\u0012\b\b\u0002\u0010 \u001a\u00020!\u0012\b\b\u0002\u0010\"\u001a\u00020#\u0012\b\b\u0002\u0010$\u001a\u00020%\u0012\b\b\u0002\u0010&\u001a\u00020'\u0012\u000e\b\u0002\u0010(\u001a\b\u0012\u0004\u0012\u00020)0\r\u0012\b\b\u0002\u0010*\u001a\u00020\u0007\u0012\b\b\u0002\u0010+\u001a\u00020,\u0012\b\b\u0002\u0010-\u001a\u00020.\u0012\b\b\u0002\u0010/\u001a\u000200\u0012\b\b\u0002\u00101\u001a\u00020\u0007\u0012\b\b\u0002\u00102\u001a\u000203\u0012\b\b\u0002\u00104\u001a\u00020\u0007\u0012\b\b\u0002\u00105\u001a\u00020\u0007\u0012\u000e\b\u0002\u00106\u001a\b\u0012\u0004\u0012\u00020\u00070\r\u0012\b\b\u0002\u00107\u001a\u00020\u0007\u0012\b\b\u0002\u00108\u001a\u00020\u0007\u0012\b\b\u0002\u00109\u001a\u00020\u0007\u0012\b\b\u0002\u0010:\u001a\u00020\u0007\u0012\b\b\u0002\u0010;\u001a\u00020\u0007\u0012\b\b\u0002\u0010<\u001a\u00020\u0007\u0012\b\b\u0002\u0010=\u001a\u000203\u0012\b\b\u0002\u0010>\u001a\u000203\u0012\b\b\u0002\u0010?\u001a\u000203\u0012\b\b\u0002\u0010@\u001a\u000203\u0012\b\b\u0002\u0010A\u001a\u000203\u0012\b\b\u0002\u0010B\u001a\u000203\u0012\b\b\u0002\u0010C\u001a\u000203\u0012\b\b\u0002\u0010D\u001a\u00020\u0007¢\u0006\u0004\bE\u0010FJ\t\u0010~\u001a\u00020\u0003HÆ\u0003J\t\u0010\u007f\u001a\u00020\u0005HÆ\u0003J\n\u0010\u0080\u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010\u0081\u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010\u0082\u0001\u001a\u00020\u0007HÆ\u0003J\f\u0010\u0083\u0001\u001a\u0004\u0018\u00010\u0007HÆ\u0003J\n\u0010\u0084\u0001\u001a\u00020\u0007HÆ\u0003J\u0010\u0010\u0085\u0001\u001a\b\u0012\u0004\u0012\u00020\u000e0\rHÆ\u0003J\u0010\u0010\u0086\u0001\u001a\b\u0012\u0004\u0012\u00020\u00100\rHÆ\u0003J\u0010\u0010\u0087\u0001\u001a\b\u0012\u0004\u0012\u00020\u00120\rHÆ\u0003J\u0010\u0010\u0088\u0001\u001a\b\u0012\u0004\u0012\u00020\u00140\rHÆ\u0003J\u0010\u0010\u0089\u0001\u001a\b\u0012\u0004\u0012\u00020\u00160\rHÆ\u0003J\u0010\u0010\u008a\u0001\u001a\b\u0012\u0004\u0012\u00020\u00180\rHÆ\u0003J\u0010\u0010\u008b\u0001\u001a\b\u0012\u0004\u0012\u00020\u001a0\rHÆ\u0003J\u0010\u0010\u008c\u0001\u001a\b\u0012\u0004\u0012\u00020\u001a0\rHÆ\u0003J\n\u0010\u008d\u0001\u001a\u00020\u001dHÆ\u0003J\n\u0010\u008e\u0001\u001a\u00020\u001fHÆ\u0003J\n\u0010\u008f\u0001\u001a\u00020!HÆ\u0003J\n\u0010\u0090\u0001\u001a\u00020#HÆ\u0003J\n\u0010\u0091\u0001\u001a\u00020%HÆ\u0003J\n\u0010\u0092\u0001\u001a\u00020'HÆ\u0003J\u0010\u0010\u0093\u0001\u001a\b\u0012\u0004\u0012\u00020)0\rHÆ\u0003J\n\u0010\u0094\u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010\u0095\u0001\u001a\u00020,HÆ\u0003J\n\u0010\u0096\u0001\u001a\u00020.HÆ\u0003J\n\u0010\u0097\u0001\u001a\u000200HÆ\u0003J\n\u0010\u0098\u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010\u0099\u0001\u001a\u000203HÆ\u0003J\n\u0010\u009a\u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010\u009b\u0001\u001a\u00020\u0007HÆ\u0003J\u0010\u0010\u009c\u0001\u001a\b\u0012\u0004\u0012\u00020\u00070\rHÆ\u0003J\n\u0010\u009d\u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010\u009e\u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010\u009f\u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010 \u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010¡\u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010¢\u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010£\u0001\u001a\u000203HÆ\u0003J\n\u0010¤\u0001\u001a\u000203HÆ\u0003J\n\u0010¥\u0001\u001a\u000203HÆ\u0003J\n\u0010¦\u0001\u001a\u000203HÆ\u0003J\n\u0010§\u0001\u001a\u000203HÆ\u0003J\n\u0010¨\u0001\u001a\u000203HÆ\u0003J\n\u0010©\u0001\u001a\u000203HÆ\u0003J\n\u0010ª\u0001\u001a\u00020\u0007HÆ\u0003J\u008a\u0004\u0010«\u0001\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00072\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00072\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\r2\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\r2\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\r2\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\r2\u000e\b\u0002\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\r2\u000e\b\u0002\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\r2\u000e\b\u0002\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001a0\r2\b\b\u0002\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u001f2\b\b\u0002\u0010 \u001a\u00020!2\b\b\u0002\u0010\"\u001a\u00020#2\b\b\u0002\u0010$\u001a\u00020%2\b\b\u0002\u0010&\u001a\u00020'2\u000e\b\u0002\u0010(\u001a\b\u0012\u0004\u0012\u00020)0\r2\b\b\u0002\u0010*\u001a\u00020\u00072\b\b\u0002\u0010+\u001a\u00020,2\b\b\u0002\u0010-\u001a\u00020.2\b\b\u0002\u0010/\u001a\u0002002\b\b\u0002\u00101\u001a\u00020\u00072\b\b\u0002\u00102\u001a\u0002032\b\b\u0002\u00104\u001a\u00020\u00072\b\b\u0002\u00105\u001a\u00020\u00072\u000e\b\u0002\u00106\u001a\b\u0012\u0004\u0012\u00020\u00070\r2\b\b\u0002\u00107\u001a\u00020\u00072\b\b\u0002\u00108\u001a\u00020\u00072\b\b\u0002\u00109\u001a\u00020\u00072\b\b\u0002\u0010:\u001a\u00020\u00072\b\b\u0002\u0010;\u001a\u00020\u00072\b\b\u0002\u0010<\u001a\u00020\u00072\b\b\u0002\u0010=\u001a\u0002032\b\b\u0002\u0010>\u001a\u0002032\b\b\u0002\u0010?\u001a\u0002032\b\b\u0002\u0010@\u001a\u0002032\b\b\u0002\u0010A\u001a\u0002032\b\b\u0002\u0010B\u001a\u0002032\b\b\u0002\u0010C\u001a\u0002032\b\b\u0002\u0010D\u001a\u00020\u0007HÆ\u0001J\u0015\u0010¬\u0001\u001a\u0002032\t\u0010\u00ad\u0001\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\n\u0010®\u0001\u001a\u000200HÖ\u0001J\n\u0010¯\u0001\u001a\u00020\u0007HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\bG\u0010HR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\bI\u0010JR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bK\u0010LR\u0011\u0010\b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bM\u0010LR\u0011\u0010\t\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bN\u0010LR\u0013\u0010\n\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\bO\u0010LR\u0011\u0010\u000b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bP\u0010LR\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r¢\u0006\b\n\u0000\u001a\u0004\bQ\u0010RR\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\r¢\u0006\b\n\u0000\u001a\u0004\bS\u0010RR\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\r¢\u0006\b\n\u0000\u001a\u0004\bT\u0010RR\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\r¢\u0006\b\n\u0000\u001a\u0004\bU\u0010RR\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\r¢\u0006\b\n\u0000\u001a\u0004\bV\u0010RR\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\r¢\u0006\b\n\u0000\u001a\u0004\bW\u0010RR\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\r¢\u0006\b\n\u0000\u001a\u0004\bX\u0010RR\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001a0\r¢\u0006\b\n\u0000\u001a\u0004\bY\u0010RR\u0011\u0010\u001c\u001a\u00020\u001d¢\u0006\b\n\u0000\u001a\u0004\bZ\u0010[R\u0011\u0010\u001e\u001a\u00020\u001f¢\u0006\b\n\u0000\u001a\u0004\b\\\u0010]R\u0011\u0010 \u001a\u00020!¢\u0006\b\n\u0000\u001a\u0004\b^\u0010_R\u0011\u0010\"\u001a\u00020#¢\u0006\b\n\u0000\u001a\u0004\b`\u0010aR\u0011\u0010$\u001a\u00020%¢\u0006\b\n\u0000\u001a\u0004\bb\u0010cR\u0011\u0010&\u001a\u00020'¢\u0006\b\n\u0000\u001a\u0004\bd\u0010eR\u0017\u0010(\u001a\b\u0012\u0004\u0012\u00020)0\r¢\u0006\b\n\u0000\u001a\u0004\bf\u0010RR\u0011\u0010*\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bg\u0010LR\u0011\u0010+\u001a\u00020,¢\u0006\b\n\u0000\u001a\u0004\bh\u0010iR\u0011\u0010-\u001a\u00020.¢\u0006\b\n\u0000\u001a\u0004\bj\u0010kR\u0011\u0010/\u001a\u000200¢\u0006\b\n\u0000\u001a\u0004\bl\u0010mR\u0011\u00101\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bn\u0010LR\u0011\u00102\u001a\u000203¢\u0006\b\n\u0000\u001a\u0004\bo\u0010pR\u0011\u00104\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bq\u0010LR\u0011\u00105\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\br\u0010LR\u0017\u00106\u001a\b\u0012\u0004\u0012\u00020\u00070\r¢\u0006\b\n\u0000\u001a\u0004\bs\u0010RR\u0011\u00107\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bt\u0010LR\u0011\u00108\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bu\u0010LR\u0011\u00109\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bv\u0010LR\u0011\u0010:\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bw\u0010LR\u0011\u0010;\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bx\u0010LR\u0011\u0010<\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\by\u0010LR\u0011\u0010=\u001a\u000203¢\u0006\b\n\u0000\u001a\u0004\bz\u0010pR\u0011\u0010>\u001a\u000203¢\u0006\b\n\u0000\u001a\u0004\b{\u0010pR\u0011\u0010?\u001a\u000203¢\u0006\b\n\u0000\u001a\u0004\b|\u0010pR\u0011\u0010@\u001a\u000203¢\u0006\b\n\u0000\u001a\u0004\b@\u0010pR\u0011\u0010A\u001a\u000203¢\u0006\b\n\u0000\u001a\u0004\bA\u0010pR\u0011\u0010B\u001a\u000203¢\u0006\b\n\u0000\u001a\u0004\bB\u0010pR\u0011\u0010C\u001a\u000203¢\u0006\b\n\u0000\u001a\u0004\bC\u0010pR\u0011\u0010D\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b}\u0010L¨\u0006°\u0001"}, d2 = {"Lcom/example/viewmodel/AdminUiState;", "", "currentScreen", "Lcom/example/model/AdminScreen;", "connState", "Lcom/example/model/ConnState;", "mode", "", "lastSyncAt", "errorMsg", "toastMessage", "dateRange", "stats", "", "Lcom/example/model/StatItem;", "allCards", "Lcom/example/model/ResourceCard;", "buttons", "Lcom/example/model/ResourceButton;", "skills", "Lcom/example/model/SkillItem;", "texts", "Lcom/example/model/TextItem;", "categories", "Lcom/example/model/CategoryItem;", "activityLogs", "Lcom/example/model/ActivityLog;", "operationLogs", "splashConfig", "Lcom/example/model/SplashConfig;", "welcomeConfig", "Lcom/example/model/WelcomeConfig;", "marqueeConfig", "Lcom/example/model/MarqueeConfig;", "updateDialogConfig", "Lcom/example/model/UpdateDialogConfig;", "consoleConfig", "Lcom/example/model/ConsoleConfig;", "ipMonitorConfig", "Lcom/example/model/IpMonitorConfig;", "toolsList", "Lcom/example/model/ToolItem;", "aiNotice", "themeKitConfig", "Lcom/example/model/ThemeKitConfig;", "fullSettings", "Lcom/example/model/FullSettingsConfig;", "versionCode", "", "versionName", "versionForce", "", "versionApkUrl", "versionApkUrlRaw", "versionChangelog", "appName", "slogan", "githubOwner", "githubRepo", "githubBranch", "githubToken", "notifReview", "notifDownload", "notifWeekly", "isLoading", "isPublishing", "isConnecting", "isUploadingFile", "uploadingLabel", "<init>", "(Lcom/example/model/AdminScreen;Lcom/example/model/ConnState;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Lcom/example/model/SplashConfig;Lcom/example/model/WelcomeConfig;Lcom/example/model/MarqueeConfig;Lcom/example/model/UpdateDialogConfig;Lcom/example/model/ConsoleConfig;Lcom/example/model/IpMonitorConfig;Ljava/util/List;Ljava/lang/String;Lcom/example/model/ThemeKitConfig;Lcom/example/model/FullSettingsConfig;ILjava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZZZZZZLjava/lang/String;)V", "getCurrentScreen", "()Lcom/example/model/AdminScreen;", "getConnState", "()Lcom/example/model/ConnState;", "getMode", "()Ljava/lang/String;", "getLastSyncAt", "getErrorMsg", "getToastMessage", "getDateRange", "getStats", "()Ljava/util/List;", "getAllCards", "getButtons", "getSkills", "getTexts", "getCategories", "getActivityLogs", "getOperationLogs", "getSplashConfig", "()Lcom/example/model/SplashConfig;", "getWelcomeConfig", "()Lcom/example/model/WelcomeConfig;", "getMarqueeConfig", "()Lcom/example/model/MarqueeConfig;", "getUpdateDialogConfig", "()Lcom/example/model/UpdateDialogConfig;", "getConsoleConfig", "()Lcom/example/model/ConsoleConfig;", "getIpMonitorConfig", "()Lcom/example/model/IpMonitorConfig;", "getToolsList", "getAiNotice", "getThemeKitConfig", "()Lcom/example/model/ThemeKitConfig;", "getFullSettings", "()Lcom/example/model/FullSettingsConfig;", "getVersionCode", "()I", "getVersionName", "getVersionForce", "()Z", "getVersionApkUrl", "getVersionApkUrlRaw", "getVersionChangelog", "getAppName", "getSlogan", "getGithubOwner", "getGithubRepo", "getGithubBranch", "getGithubToken", "getNotifReview", "getNotifDownload", "getNotifWeekly", "getUploadingLabel", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component30", "component31", "component32", "component33", "component34", "component35", "component36", "component37", "component38", "component39", "component40", "component41", "component42", "component43", "component44", "component45", "copy", "equals", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes4.dex */
public final class AdminUiState {
    public static final int $stable = 8;
    private final List<ActivityLog> activityLogs;
    private final String aiNotice;
    private final List<ResourceCard> allCards;
    private final String appName;
    private final List<ResourceButton> buttons;
    private final List<CategoryItem> categories;
    private final ConnState connState;
    private final ConsoleConfig consoleConfig;
    private final AdminScreen currentScreen;
    private final String dateRange;
    private final String errorMsg;
    private final FullSettingsConfig fullSettings;
    private final String githubBranch;
    private final String githubOwner;
    private final String githubRepo;
    private final String githubToken;
    private final IpMonitorConfig ipMonitorConfig;
    private final boolean isConnecting;
    private final boolean isLoading;
    private final boolean isPublishing;
    private final boolean isUploadingFile;
    private final String lastSyncAt;
    private final MarqueeConfig marqueeConfig;
    private final String mode;
    private final boolean notifDownload;
    private final boolean notifReview;
    private final boolean notifWeekly;
    private final List<ActivityLog> operationLogs;
    private final List<SkillItem> skills;
    private final String slogan;
    private final SplashConfig splashConfig;
    private final List<StatItem> stats;
    private final List<TextItem> texts;
    private final ThemeKitConfig themeKitConfig;
    private final String toastMessage;
    private final List<ToolItem> toolsList;
    private final UpdateDialogConfig updateDialogConfig;
    private final String uploadingLabel;
    private final String versionApkUrl;
    private final String versionApkUrlRaw;
    private final List<String> versionChangelog;
    private final int versionCode;
    private final boolean versionForce;
    private final String versionName;
    private final WelcomeConfig welcomeConfig;

    public AdminUiState() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, false, null, null, null, null, null, null, null, null, null, false, false, false, false, false, false, false, null, -1, 8191, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ AdminUiState copy$default(AdminUiState adminUiState, AdminScreen adminScreen, ConnState connState, String str, String str2, String str3, String str4, String str5, List list, List list2, List list3, List list4, List list5, List list6, List list7, List list8, SplashConfig splashConfig, WelcomeConfig welcomeConfig, MarqueeConfig marqueeConfig, UpdateDialogConfig updateDialogConfig, ConsoleConfig consoleConfig, IpMonitorConfig ipMonitorConfig, List list9, String str6, ThemeKitConfig themeKitConfig, FullSettingsConfig fullSettingsConfig, int i, String str7, boolean z, String str8, String str9, List list10, String str10, String str11, String str12, String str13, String str14, String str15, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, String str16, int i2, int i3, Object obj) {
        AdminScreen adminScreen2 = (i2 & 1) != 0 ? adminUiState.currentScreen : adminScreen;
        return adminUiState.copy(adminScreen2, (i2 & 2) != 0 ? adminUiState.connState : connState, (i2 & 4) != 0 ? adminUiState.mode : str, (i2 & 8) != 0 ? adminUiState.lastSyncAt : str2, (i2 & 16) != 0 ? adminUiState.errorMsg : str3, (i2 & 32) != 0 ? adminUiState.toastMessage : str4, (i2 & 64) != 0 ? adminUiState.dateRange : str5, (i2 & 128) != 0 ? adminUiState.stats : list, (i2 & 256) != 0 ? adminUiState.allCards : list2, (i2 & 512) != 0 ? adminUiState.buttons : list3, (i2 & 1024) != 0 ? adminUiState.skills : list4, (i2 & 2048) != 0 ? adminUiState.texts : list5, (i2 & 4096) != 0 ? adminUiState.categories : list6, (i2 & 8192) != 0 ? adminUiState.activityLogs : list7, (i2 & 16384) != 0 ? adminUiState.operationLogs : list8, (i2 & 32768) != 0 ? adminUiState.splashConfig : splashConfig, (i2 & 65536) != 0 ? adminUiState.welcomeConfig : welcomeConfig, (i2 & 131072) != 0 ? adminUiState.marqueeConfig : marqueeConfig, (i2 & 262144) != 0 ? adminUiState.updateDialogConfig : updateDialogConfig, (i2 & 524288) != 0 ? adminUiState.consoleConfig : consoleConfig, (i2 & 1048576) != 0 ? adminUiState.ipMonitorConfig : ipMonitorConfig, (i2 & 2097152) != 0 ? adminUiState.toolsList : list9, (i2 & 4194304) != 0 ? adminUiState.aiNotice : str6, (i2 & 8388608) != 0 ? adminUiState.themeKitConfig : themeKitConfig, (i2 & 16777216) != 0 ? adminUiState.fullSettings : fullSettingsConfig, (i2 & 33554432) != 0 ? adminUiState.versionCode : i, (i2 & AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL) != 0 ? adminUiState.versionName : str7, (i2 & 134217728) != 0 ? adminUiState.versionForce : z, (i2 & 268435456) != 0 ? adminUiState.versionApkUrl : str8, (i2 & 536870912) != 0 ? adminUiState.versionApkUrlRaw : str9, (i2 & 1073741824) != 0 ? adminUiState.versionChangelog : list10, (i2 & Integer.MIN_VALUE) != 0 ? adminUiState.appName : str10, (i3 & 1) != 0 ? adminUiState.slogan : str11, (i3 & 2) != 0 ? adminUiState.githubOwner : str12, (i3 & 4) != 0 ? adminUiState.githubRepo : str13, (i3 & 8) != 0 ? adminUiState.githubBranch : str14, (i3 & 16) != 0 ? adminUiState.githubToken : str15, (i3 & 32) != 0 ? adminUiState.notifReview : z2, (i3 & 64) != 0 ? adminUiState.notifDownload : z3, (i3 & 128) != 0 ? adminUiState.notifWeekly : z4, (i3 & 256) != 0 ? adminUiState.isLoading : z5, (i3 & 512) != 0 ? adminUiState.isPublishing : z6, (i3 & 1024) != 0 ? adminUiState.isConnecting : z7, (i3 & 2048) != 0 ? adminUiState.isUploadingFile : z8, (i3 & 4096) != 0 ? adminUiState.uploadingLabel : str16);
    }

    public final AdminScreen component1() {
        return this.currentScreen;
    }

    public final List<ResourceButton> component10() {
        return this.buttons;
    }

    public final List<SkillItem> component11() {
        return this.skills;
    }

    public final List<TextItem> component12() {
        return this.texts;
    }

    public final List<CategoryItem> component13() {
        return this.categories;
    }

    public final List<ActivityLog> component14() {
        return this.activityLogs;
    }

    public final List<ActivityLog> component15() {
        return this.operationLogs;
    }

    public final SplashConfig component16() {
        return this.splashConfig;
    }

    public final WelcomeConfig component17() {
        return this.welcomeConfig;
    }

    public final MarqueeConfig component18() {
        return this.marqueeConfig;
    }

    public final UpdateDialogConfig component19() {
        return this.updateDialogConfig;
    }

    public final ConnState component2() {
        return this.connState;
    }

    public final ConsoleConfig component20() {
        return this.consoleConfig;
    }

    public final IpMonitorConfig component21() {
        return this.ipMonitorConfig;
    }

    public final List<ToolItem> component22() {
        return this.toolsList;
    }

    public final String component23() {
        return this.aiNotice;
    }

    public final ThemeKitConfig component24() {
        return this.themeKitConfig;
    }

    public final FullSettingsConfig component25() {
        return this.fullSettings;
    }

    public final int component26() {
        return this.versionCode;
    }

    public final String component27() {
        return this.versionName;
    }

    public final boolean component28() {
        return this.versionForce;
    }

    public final String component29() {
        return this.versionApkUrl;
    }

    public final String component3() {
        return this.mode;
    }

    public final String component30() {
        return this.versionApkUrlRaw;
    }

    public final List<String> component31() {
        return this.versionChangelog;
    }

    public final String component32() {
        return this.appName;
    }

    public final String component33() {
        return this.slogan;
    }

    public final String component34() {
        return this.githubOwner;
    }

    public final String component35() {
        return this.githubRepo;
    }

    public final String component36() {
        return this.githubBranch;
    }

    public final String component37() {
        return this.githubToken;
    }

    public final boolean component38() {
        return this.notifReview;
    }

    public final boolean component39() {
        return this.notifDownload;
    }

    public final String component4() {
        return this.lastSyncAt;
    }

    public final boolean component40() {
        return this.notifWeekly;
    }

    public final boolean component41() {
        return this.isLoading;
    }

    public final boolean component42() {
        return this.isPublishing;
    }

    public final boolean component43() {
        return this.isConnecting;
    }

    public final boolean component44() {
        return this.isUploadingFile;
    }

    public final String component45() {
        return this.uploadingLabel;
    }

    public final String component5() {
        return this.errorMsg;
    }

    public final String component6() {
        return this.toastMessage;
    }

    public final String component7() {
        return this.dateRange;
    }

    public final List<StatItem> component8() {
        return this.stats;
    }

    public final List<ResourceCard> component9() {
        return this.allCards;
    }

    public final AdminUiState copy(AdminScreen currentScreen, ConnState connState, String mode, String lastSyncAt, String errorMsg, String str, String dateRange, List<StatItem> stats, List<ResourceCard> allCards, List<ResourceButton> buttons, List<SkillItem> skills, List<TextItem> texts, List<CategoryItem> categories, List<ActivityLog> activityLogs, List<ActivityLog> operationLogs, SplashConfig splashConfig, WelcomeConfig welcomeConfig, MarqueeConfig marqueeConfig, UpdateDialogConfig updateDialogConfig, ConsoleConfig consoleConfig, IpMonitorConfig ipMonitorConfig, List<ToolItem> toolsList, String aiNotice, ThemeKitConfig themeKitConfig, FullSettingsConfig fullSettings, int i, String versionName, boolean z, String versionApkUrl, String versionApkUrlRaw, List<String> versionChangelog, String appName, String slogan, String githubOwner, String githubRepo, String githubBranch, String githubToken, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, String uploadingLabel) {
        Intrinsics.checkNotNullParameter(currentScreen, "currentScreen");
        Intrinsics.checkNotNullParameter(connState, "connState");
        Intrinsics.checkNotNullParameter(mode, "mode");
        Intrinsics.checkNotNullParameter(lastSyncAt, "lastSyncAt");
        Intrinsics.checkNotNullParameter(errorMsg, "errorMsg");
        Intrinsics.checkNotNullParameter(dateRange, "dateRange");
        Intrinsics.checkNotNullParameter(stats, "stats");
        Intrinsics.checkNotNullParameter(allCards, "allCards");
        Intrinsics.checkNotNullParameter(buttons, "buttons");
        Intrinsics.checkNotNullParameter(skills, "skills");
        Intrinsics.checkNotNullParameter(texts, "texts");
        Intrinsics.checkNotNullParameter(categories, "categories");
        Intrinsics.checkNotNullParameter(activityLogs, "activityLogs");
        Intrinsics.checkNotNullParameter(operationLogs, "operationLogs");
        Intrinsics.checkNotNullParameter(splashConfig, "splashConfig");
        Intrinsics.checkNotNullParameter(welcomeConfig, "welcomeConfig");
        Intrinsics.checkNotNullParameter(marqueeConfig, "marqueeConfig");
        Intrinsics.checkNotNullParameter(updateDialogConfig, "updateDialogConfig");
        Intrinsics.checkNotNullParameter(consoleConfig, "consoleConfig");
        Intrinsics.checkNotNullParameter(ipMonitorConfig, "ipMonitorConfig");
        Intrinsics.checkNotNullParameter(toolsList, "toolsList");
        Intrinsics.checkNotNullParameter(aiNotice, "aiNotice");
        Intrinsics.checkNotNullParameter(themeKitConfig, "themeKitConfig");
        Intrinsics.checkNotNullParameter(fullSettings, "fullSettings");
        Intrinsics.checkNotNullParameter(versionName, "versionName");
        Intrinsics.checkNotNullParameter(versionApkUrl, "versionApkUrl");
        Intrinsics.checkNotNullParameter(versionApkUrlRaw, "versionApkUrlRaw");
        Intrinsics.checkNotNullParameter(versionChangelog, "versionChangelog");
        Intrinsics.checkNotNullParameter(appName, "appName");
        Intrinsics.checkNotNullParameter(slogan, "slogan");
        Intrinsics.checkNotNullParameter(githubOwner, "githubOwner");
        Intrinsics.checkNotNullParameter(githubRepo, "githubRepo");
        Intrinsics.checkNotNullParameter(githubBranch, "githubBranch");
        Intrinsics.checkNotNullParameter(githubToken, "githubToken");
        Intrinsics.checkNotNullParameter(uploadingLabel, "uploadingLabel");
        return new AdminUiState(currentScreen, connState, mode, lastSyncAt, errorMsg, str, dateRange, stats, allCards, buttons, skills, texts, categories, activityLogs, operationLogs, splashConfig, welcomeConfig, marqueeConfig, updateDialogConfig, consoleConfig, ipMonitorConfig, toolsList, aiNotice, themeKitConfig, fullSettings, i, versionName, z, versionApkUrl, versionApkUrlRaw, versionChangelog, appName, slogan, githubOwner, githubRepo, githubBranch, githubToken, z2, z3, z4, z5, z6, z7, z8, uploadingLabel);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AdminUiState) {
            AdminUiState adminUiState = (AdminUiState) obj;
            return this.currentScreen == adminUiState.currentScreen && this.connState == adminUiState.connState && Intrinsics.areEqual(this.mode, adminUiState.mode) && Intrinsics.areEqual(this.lastSyncAt, adminUiState.lastSyncAt) && Intrinsics.areEqual(this.errorMsg, adminUiState.errorMsg) && Intrinsics.areEqual(this.toastMessage, adminUiState.toastMessage) && Intrinsics.areEqual(this.dateRange, adminUiState.dateRange) && Intrinsics.areEqual(this.stats, adminUiState.stats) && Intrinsics.areEqual(this.allCards, adminUiState.allCards) && Intrinsics.areEqual(this.buttons, adminUiState.buttons) && Intrinsics.areEqual(this.skills, adminUiState.skills) && Intrinsics.areEqual(this.texts, adminUiState.texts) && Intrinsics.areEqual(this.categories, adminUiState.categories) && Intrinsics.areEqual(this.activityLogs, adminUiState.activityLogs) && Intrinsics.areEqual(this.operationLogs, adminUiState.operationLogs) && Intrinsics.areEqual(this.splashConfig, adminUiState.splashConfig) && Intrinsics.areEqual(this.welcomeConfig, adminUiState.welcomeConfig) && Intrinsics.areEqual(this.marqueeConfig, adminUiState.marqueeConfig) && Intrinsics.areEqual(this.updateDialogConfig, adminUiState.updateDialogConfig) && Intrinsics.areEqual(this.consoleConfig, adminUiState.consoleConfig) && Intrinsics.areEqual(this.ipMonitorConfig, adminUiState.ipMonitorConfig) && Intrinsics.areEqual(this.toolsList, adminUiState.toolsList) && Intrinsics.areEqual(this.aiNotice, adminUiState.aiNotice) && Intrinsics.areEqual(this.themeKitConfig, adminUiState.themeKitConfig) && Intrinsics.areEqual(this.fullSettings, adminUiState.fullSettings) && this.versionCode == adminUiState.versionCode && Intrinsics.areEqual(this.versionName, adminUiState.versionName) && this.versionForce == adminUiState.versionForce && Intrinsics.areEqual(this.versionApkUrl, adminUiState.versionApkUrl) && Intrinsics.areEqual(this.versionApkUrlRaw, adminUiState.versionApkUrlRaw) && Intrinsics.areEqual(this.versionChangelog, adminUiState.versionChangelog) && Intrinsics.areEqual(this.appName, adminUiState.appName) && Intrinsics.areEqual(this.slogan, adminUiState.slogan) && Intrinsics.areEqual(this.githubOwner, adminUiState.githubOwner) && Intrinsics.areEqual(this.githubRepo, adminUiState.githubRepo) && Intrinsics.areEqual(this.githubBranch, adminUiState.githubBranch) && Intrinsics.areEqual(this.githubToken, adminUiState.githubToken) && this.notifReview == adminUiState.notifReview && this.notifDownload == adminUiState.notifDownload && this.notifWeekly == adminUiState.notifWeekly && this.isLoading == adminUiState.isLoading && this.isPublishing == adminUiState.isPublishing && this.isConnecting == adminUiState.isConnecting && this.isUploadingFile == adminUiState.isUploadingFile && Intrinsics.areEqual(this.uploadingLabel, adminUiState.uploadingLabel);
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((this.currentScreen.hashCode() * 31) + this.connState.hashCode()) * 31) + this.mode.hashCode()) * 31) + this.lastSyncAt.hashCode()) * 31) + this.errorMsg.hashCode()) * 31) + (this.toastMessage == null ? 0 : this.toastMessage.hashCode())) * 31) + this.dateRange.hashCode()) * 31) + this.stats.hashCode()) * 31) + this.allCards.hashCode()) * 31) + this.buttons.hashCode()) * 31) + this.skills.hashCode()) * 31) + this.texts.hashCode()) * 31) + this.categories.hashCode()) * 31) + this.activityLogs.hashCode()) * 31) + this.operationLogs.hashCode()) * 31) + this.splashConfig.hashCode()) * 31) + this.welcomeConfig.hashCode()) * 31) + this.marqueeConfig.hashCode()) * 31) + this.updateDialogConfig.hashCode()) * 31) + this.consoleConfig.hashCode()) * 31) + this.ipMonitorConfig.hashCode()) * 31) + this.toolsList.hashCode()) * 31) + this.aiNotice.hashCode()) * 31) + this.themeKitConfig.hashCode()) * 31) + this.fullSettings.hashCode()) * 31) + Integer.hashCode(this.versionCode)) * 31) + this.versionName.hashCode()) * 31) + Boolean.hashCode(this.versionForce)) * 31) + this.versionApkUrl.hashCode()) * 31) + this.versionApkUrlRaw.hashCode()) * 31) + this.versionChangelog.hashCode()) * 31) + this.appName.hashCode()) * 31) + this.slogan.hashCode()) * 31) + this.githubOwner.hashCode()) * 31) + this.githubRepo.hashCode()) * 31) + this.githubBranch.hashCode()) * 31) + this.githubToken.hashCode()) * 31) + Boolean.hashCode(this.notifReview)) * 31) + Boolean.hashCode(this.notifDownload)) * 31) + Boolean.hashCode(this.notifWeekly)) * 31) + Boolean.hashCode(this.isLoading)) * 31) + Boolean.hashCode(this.isPublishing)) * 31) + Boolean.hashCode(this.isConnecting)) * 31) + Boolean.hashCode(this.isUploadingFile)) * 31) + this.uploadingLabel.hashCode();
    }

    public String toString() {
        AdminScreen adminScreen = this.currentScreen;
        ConnState connState = this.connState;
        String str = this.mode;
        String str2 = this.lastSyncAt;
        String str3 = this.errorMsg;
        String str4 = this.toastMessage;
        String str5 = this.dateRange;
        List<StatItem> list = this.stats;
        List<ResourceCard> list2 = this.allCards;
        List<ResourceButton> list3 = this.buttons;
        List<SkillItem> list4 = this.skills;
        List<TextItem> list5 = this.texts;
        List<CategoryItem> list6 = this.categories;
        List<ActivityLog> list7 = this.activityLogs;
        List<ActivityLog> list8 = this.operationLogs;
        SplashConfig splashConfig = this.splashConfig;
        WelcomeConfig welcomeConfig = this.welcomeConfig;
        MarqueeConfig marqueeConfig = this.marqueeConfig;
        UpdateDialogConfig updateDialogConfig = this.updateDialogConfig;
        ConsoleConfig consoleConfig = this.consoleConfig;
        IpMonitorConfig ipMonitorConfig = this.ipMonitorConfig;
        List<ToolItem> list9 = this.toolsList;
        String str6 = this.aiNotice;
        ThemeKitConfig themeKitConfig = this.themeKitConfig;
        FullSettingsConfig fullSettingsConfig = this.fullSettings;
        int i = this.versionCode;
        String str7 = this.versionName;
        boolean z = this.versionForce;
        String str8 = this.versionApkUrl;
        String str9 = this.versionApkUrlRaw;
        List<String> list10 = this.versionChangelog;
        String str10 = this.appName;
        String str11 = this.slogan;
        String str12 = this.githubOwner;
        String str13 = this.githubRepo;
        String str14 = this.githubBranch;
        String str15 = this.githubToken;
        boolean z2 = this.notifReview;
        boolean z3 = this.notifDownload;
        boolean z4 = this.notifWeekly;
        boolean z5 = this.isLoading;
        boolean z6 = this.isPublishing;
        boolean z7 = this.isConnecting;
        boolean z8 = this.isUploadingFile;
        return "AdminUiState(currentScreen=" + adminScreen + ", connState=" + connState + ", mode=" + str + ", lastSyncAt=" + str2 + ", errorMsg=" + str3 + ", toastMessage=" + str4 + ", dateRange=" + str5 + ", stats=" + list + ", allCards=" + list2 + ", buttons=" + list3 + ", skills=" + list4 + ", texts=" + list5 + ", categories=" + list6 + ", activityLogs=" + list7 + ", operationLogs=" + list8 + ", splashConfig=" + splashConfig + ", welcomeConfig=" + welcomeConfig + ", marqueeConfig=" + marqueeConfig + ", updateDialogConfig=" + updateDialogConfig + ", consoleConfig=" + consoleConfig + ", ipMonitorConfig=" + ipMonitorConfig + ", toolsList=" + list9 + ", aiNotice=" + str6 + ", themeKitConfig=" + themeKitConfig + ", fullSettings=" + fullSettingsConfig + ", versionCode=" + i + ", versionName=" + str7 + ", versionForce=" + z + ", versionApkUrl=" + str8 + ", versionApkUrlRaw=" + str9 + ", versionChangelog=" + list10 + ", appName=" + str10 + ", slogan=" + str11 + ", githubOwner=" + str12 + ", githubRepo=" + str13 + ", githubBranch=" + str14 + ", githubToken=" + str15 + ", notifReview=" + z2 + ", notifDownload=" + z3 + ", notifWeekly=" + z4 + ", isLoading=" + z5 + ", isPublishing=" + z6 + ", isConnecting=" + z7 + ", isUploadingFile=" + z8 + ", uploadingLabel=" + this.uploadingLabel + ")";
    }

    public AdminUiState(AdminScreen currentScreen, ConnState connState, String mode, String lastSyncAt, String errorMsg, String toastMessage, String dateRange, List<StatItem> stats, List<ResourceCard> allCards, List<ResourceButton> buttons, List<SkillItem> skills, List<TextItem> texts, List<CategoryItem> categories, List<ActivityLog> activityLogs, List<ActivityLog> operationLogs, SplashConfig splashConfig, WelcomeConfig welcomeConfig, MarqueeConfig marqueeConfig, UpdateDialogConfig updateDialogConfig, ConsoleConfig consoleConfig, IpMonitorConfig ipMonitorConfig, List<ToolItem> toolsList, String aiNotice, ThemeKitConfig themeKitConfig, FullSettingsConfig fullSettings, int versionCode, String versionName, boolean versionForce, String versionApkUrl, String versionApkUrlRaw, List<String> versionChangelog, String appName, String slogan, String githubOwner, String githubRepo, String githubBranch, String githubToken, boolean notifReview, boolean notifDownload, boolean notifWeekly, boolean isLoading, boolean isPublishing, boolean isConnecting, boolean isUploadingFile, String uploadingLabel) {
        Intrinsics.checkNotNullParameter(currentScreen, "currentScreen");
        Intrinsics.checkNotNullParameter(connState, "connState");
        Intrinsics.checkNotNullParameter(mode, "mode");
        Intrinsics.checkNotNullParameter(lastSyncAt, "lastSyncAt");
        Intrinsics.checkNotNullParameter(errorMsg, "errorMsg");
        Intrinsics.checkNotNullParameter(dateRange, "dateRange");
        Intrinsics.checkNotNullParameter(stats, "stats");
        Intrinsics.checkNotNullParameter(allCards, "allCards");
        Intrinsics.checkNotNullParameter(buttons, "buttons");
        Intrinsics.checkNotNullParameter(skills, "skills");
        Intrinsics.checkNotNullParameter(texts, "texts");
        Intrinsics.checkNotNullParameter(categories, "categories");
        Intrinsics.checkNotNullParameter(activityLogs, "activityLogs");
        Intrinsics.checkNotNullParameter(operationLogs, "operationLogs");
        Intrinsics.checkNotNullParameter(splashConfig, "splashConfig");
        Intrinsics.checkNotNullParameter(welcomeConfig, "welcomeConfig");
        Intrinsics.checkNotNullParameter(marqueeConfig, "marqueeConfig");
        Intrinsics.checkNotNullParameter(updateDialogConfig, "updateDialogConfig");
        Intrinsics.checkNotNullParameter(consoleConfig, "consoleConfig");
        Intrinsics.checkNotNullParameter(ipMonitorConfig, "ipMonitorConfig");
        Intrinsics.checkNotNullParameter(toolsList, "toolsList");
        Intrinsics.checkNotNullParameter(aiNotice, "aiNotice");
        Intrinsics.checkNotNullParameter(themeKitConfig, "themeKitConfig");
        Intrinsics.checkNotNullParameter(fullSettings, "fullSettings");
        Intrinsics.checkNotNullParameter(versionName, "versionName");
        Intrinsics.checkNotNullParameter(versionApkUrl, "versionApkUrl");
        Intrinsics.checkNotNullParameter(versionApkUrlRaw, "versionApkUrlRaw");
        Intrinsics.checkNotNullParameter(versionChangelog, "versionChangelog");
        Intrinsics.checkNotNullParameter(appName, "appName");
        Intrinsics.checkNotNullParameter(slogan, "slogan");
        Intrinsics.checkNotNullParameter(githubOwner, "githubOwner");
        Intrinsics.checkNotNullParameter(githubRepo, "githubRepo");
        Intrinsics.checkNotNullParameter(githubBranch, "githubBranch");
        Intrinsics.checkNotNullParameter(githubToken, "githubToken");
        Intrinsics.checkNotNullParameter(uploadingLabel, "uploadingLabel");
        this.currentScreen = currentScreen;
        this.connState = connState;
        this.mode = mode;
        this.lastSyncAt = lastSyncAt;
        this.errorMsg = errorMsg;
        this.toastMessage = toastMessage;
        this.dateRange = dateRange;
        this.stats = stats;
        this.allCards = allCards;
        this.buttons = buttons;
        this.skills = skills;
        this.texts = texts;
        this.categories = categories;
        this.activityLogs = activityLogs;
        this.operationLogs = operationLogs;
        this.splashConfig = splashConfig;
        this.welcomeConfig = welcomeConfig;
        this.marqueeConfig = marqueeConfig;
        this.updateDialogConfig = updateDialogConfig;
        this.consoleConfig = consoleConfig;
        this.ipMonitorConfig = ipMonitorConfig;
        this.toolsList = toolsList;
        this.aiNotice = aiNotice;
        this.themeKitConfig = themeKitConfig;
        this.fullSettings = fullSettings;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.versionForce = versionForce;
        this.versionApkUrl = versionApkUrl;
        this.versionApkUrlRaw = versionApkUrlRaw;
        this.versionChangelog = versionChangelog;
        this.appName = appName;
        this.slogan = slogan;
        this.githubOwner = githubOwner;
        this.githubRepo = githubRepo;
        this.githubBranch = githubBranch;
        this.githubToken = githubToken;
        this.notifReview = notifReview;
        this.notifDownload = notifDownload;
        this.notifWeekly = notifWeekly;
        this.isLoading = isLoading;
        this.isPublishing = isPublishing;
        this.isConnecting = isConnecting;
        this.isUploadingFile = isUploadingFile;
        this.uploadingLabel = uploadingLabel;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ AdminUiState(com.example.model.AdminScreen r44, com.example.model.ConnState r45, java.lang.String r46, java.lang.String r47, java.lang.String r48, java.lang.String r49, java.lang.String r50, java.util.List r51, java.util.List r52, java.util.List r53, java.util.List r54, java.util.List r55, java.util.List r56, java.util.List r57, java.util.List r58, com.example.model.SplashConfig r59, com.example.model.WelcomeConfig r60, com.example.model.MarqueeConfig r61, com.example.model.UpdateDialogConfig r62, com.example.model.ConsoleConfig r63, com.example.model.IpMonitorConfig r64, java.util.List r65, java.lang.String r66, com.example.model.ThemeKitConfig r67, com.example.model.FullSettingsConfig r68, int r69, java.lang.String r70, boolean r71, java.lang.String r72, java.lang.String r73, java.util.List r74, java.lang.String r75, java.lang.String r76, java.lang.String r77, java.lang.String r78, java.lang.String r79, java.lang.String r80, boolean r81, boolean r82, boolean r83, boolean r84, boolean r85, boolean r86, boolean r87, java.lang.String r88, int r89, int r90, kotlin.jvm.internal.DefaultConstructorMarker r91) {
        /*
            Method dump skipped, instructions count: 886
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminUiState.<init>(com.example.model.AdminScreen, com.example.model.ConnState, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, com.example.model.SplashConfig, com.example.model.WelcomeConfig, com.example.model.MarqueeConfig, com.example.model.UpdateDialogConfig, com.example.model.ConsoleConfig, com.example.model.IpMonitorConfig, java.util.List, java.lang.String, com.example.model.ThemeKitConfig, com.example.model.FullSettingsConfig, int, java.lang.String, boolean, java.lang.String, java.lang.String, java.util.List, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, boolean, boolean, boolean, boolean, boolean, boolean, java.lang.String, int, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public final AdminScreen getCurrentScreen() {
        return this.currentScreen;
    }

    public final ConnState getConnState() {
        return this.connState;
    }

    public final String getMode() {
        return this.mode;
    }

    public final String getLastSyncAt() {
        return this.lastSyncAt;
    }

    public final String getErrorMsg() {
        return this.errorMsg;
    }

    public final String getToastMessage() {
        return this.toastMessage;
    }

    public final String getDateRange() {
        return this.dateRange;
    }

    public final List<StatItem> getStats() {
        return this.stats;
    }

    public final List<ResourceCard> getAllCards() {
        return this.allCards;
    }

    public final List<ResourceButton> getButtons() {
        return this.buttons;
    }

    public final List<SkillItem> getSkills() {
        return this.skills;
    }

    public final List<TextItem> getTexts() {
        return this.texts;
    }

    public final List<CategoryItem> getCategories() {
        return this.categories;
    }

    public final List<ActivityLog> getActivityLogs() {
        return this.activityLogs;
    }

    public final List<ActivityLog> getOperationLogs() {
        return this.operationLogs;
    }

    public final SplashConfig getSplashConfig() {
        return this.splashConfig;
    }

    public final WelcomeConfig getWelcomeConfig() {
        return this.welcomeConfig;
    }

    public final MarqueeConfig getMarqueeConfig() {
        return this.marqueeConfig;
    }

    public final UpdateDialogConfig getUpdateDialogConfig() {
        return this.updateDialogConfig;
    }

    public final ConsoleConfig getConsoleConfig() {
        return this.consoleConfig;
    }

    public final IpMonitorConfig getIpMonitorConfig() {
        return this.ipMonitorConfig;
    }

    public final List<ToolItem> getToolsList() {
        return this.toolsList;
    }

    public final String getAiNotice() {
        return this.aiNotice;
    }

    public final ThemeKitConfig getThemeKitConfig() {
        return this.themeKitConfig;
    }

    public final FullSettingsConfig getFullSettings() {
        return this.fullSettings;
    }

    public final int getVersionCode() {
        return this.versionCode;
    }

    public final String getVersionName() {
        return this.versionName;
    }

    public final boolean getVersionForce() {
        return this.versionForce;
    }

    public final String getVersionApkUrl() {
        return this.versionApkUrl;
    }

    public final String getVersionApkUrlRaw() {
        return this.versionApkUrlRaw;
    }

    public final List<String> getVersionChangelog() {
        return this.versionChangelog;
    }

    public final String getAppName() {
        return this.appName;
    }

    public final String getSlogan() {
        return this.slogan;
    }

    public final String getGithubOwner() {
        return this.githubOwner;
    }

    public final String getGithubRepo() {
        return this.githubRepo;
    }

    public final String getGithubBranch() {
        return this.githubBranch;
    }

    public final String getGithubToken() {
        return this.githubToken;
    }

    public final boolean getNotifReview() {
        return this.notifReview;
    }

    public final boolean getNotifDownload() {
        return this.notifDownload;
    }

    public final boolean getNotifWeekly() {
        return this.notifWeekly;
    }

    public final boolean isLoading() {
        return this.isLoading;
    }

    public final boolean isPublishing() {
        return this.isPublishing;
    }

    public final boolean isConnecting() {
        return this.isConnecting;
    }

    public final boolean isUploadingFile() {
        return this.isUploadingFile;
    }

    public final String getUploadingLabel() {
        return this.uploadingLabel;
    }
}
