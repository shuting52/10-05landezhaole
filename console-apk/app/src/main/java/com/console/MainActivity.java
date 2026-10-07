package com.yuntai;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 云台-懒得找了 · 云端总控台（WebView 壳应用）
 * 加载内置的 console/index.html，通过 GitHub API 完全对接仓库，
 * 在手机上即可后台维护更新项目全部内容。
 *
 * 1. WebChromeClient.onShowFileChooser：页面 <input type="file"> 打开系统文件选择器（本地上传）
 * 2. JS 桥 Android.download(url, name)：页面调用原生系统下载管理器下载 APK/文件
 * 3. JS 桥 Android.downloadAndInstall(url, name)：控制台自更新——下载新版本 APK 并自动安装替换老版本
 *    （控制台程序更新与本体软件完全分离）
 * 4. JS 桥 Android.getVersionCode() / getVersionName()：把「真实已安装版本号」暴露给页面，
 *    控制台据此判断是否需要更新，避免依赖容易漏改的硬编码常量。
 *
 * ── v1.0.12 修复「控制台新版本推不到老版本」──
 *   · 下载改到应用私有目录（cacheDir），不再写公共 Download 目录，无需任何存储权限，
 *     彻底解决 Android 10+ 上 FileOutputStream 抛 EACCES 导致「控制台更新下载失败」。
 *   · 本地版本以真实安装包 versionCode 为准（getVersionCode），不再依赖过期的内置常量。
 *   · 安装优先走 PackageInstaller 系统会话（原子替换、保留数据），失败回退 FileProvider。
 *   · 签名不一致（INSTALL_FAILED_UPDATE_INCOMPATIBLE）时明确提示并引导卸载旧版本。
 *   · 下载失败自动尝试 jsDelivr 镜像，避免 raw.githubusercontent.com 被墙导致更新失败。
 *
 * ── v1.0.6 更新弹窗直装优化 ──
 *   · 彻底移除「安装未知应用授权」设置页跳转（不再引导 Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES）：
 *     页面点击更新 → 进度条走完 → 直接调用系统安装器呈现新版本安装（PackageInstaller 会话优先）。
 *   · 新增 getDownloadProgress()/getUpdateStatus() JS 桥，页面可轮询显示下载进度条。
 *   · 安装结果（成功/失败/签名冲突）通过 evaluateJavascript 实时回传页面弹窗。
 */
public class MainActivity extends Activity {

    private static final int FILE_CHOOSER_RESULT_CODE = 101;
    private static final String ACTION_INSTALL_RESULT = "com.yuntai.CONSOLE_INSTALL_RESULT";
    private static final int INSTALL_RESULT_REQUEST_CODE = 200;

    private WebView webView;
    private ValueCallback<Uri[]> uploadMessage;
    private BroadcastReceiver installReceiver;

    // 控制台自更新下载进度（0~100），供 JS 进度条轮询显示
    private volatile int downloadProgress = 0;
    // 最近一次下载/安装结果描述，供 JS 弹窗展示
    private volatile String lastUpdateStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerInstallReceiver();

        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowFileAccessFromFileURLs(true);
        s.setAllowUniversalAccessFromFileURLs(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient());

        // 关键：WebChromeClient 支持文件选择（本地上传 APK/MD/ZIP/图片/视频）
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(
                    WebView wv,
                    ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams
            ) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                uploadMessage = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    return false;
                }
                return true;
            }

            @Override
            public void onPermissionRequest(android.webkit.PermissionRequest request) {
                try {
                    runOnUiThread(() -> request.grant(request.getResources()));
                } catch (Exception e) {
                    request.deny();
                }
            }
        });

        // JS 桥：Android.download(url, name) -> 系统下载管理器下载 APK/文件
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void download(String url, String name) {
                try {
                    String safeName = (name == null || name.trim().isEmpty())
                            ? "yuntai_download.bin"
                            : name.replaceAll("[^a-zA-Z0-9._\\-]", "_");
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));
                    req.setTitle("云台-懒得找了 · 文件下载");
                    req.setDescription(safeName);
                    req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    req.setAllowedOverMetered(true);
                    req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, safeName);
                    dm.enqueue(req);
                } catch (Exception e) {
                    // 下载器失败时退回浏览器打开
                    runOnUiThread(() -> {
                        try {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } catch (Exception ignored) {
                        }
                    });
                }
            }

            // 控制台自更新：下载新版本 APK 到应用私有目录，然后自动触发安装（替换老版本）
            // v1.0.6 优化：一律不走「安装未知应用授权」设置页——进度条走完后直接调用系统安装器
            // 呈现新版本安装（PackageInstaller 会话优先，失败回退 FileProvider，全程不再跳系统设置页）
            @JavascriptInterface
            public void downloadAndInstall(final String url, final String name) {
                final String safeName = (name == null || name.trim().isEmpty())
                        ? "console-update.apk"
                        : name.replaceAll("[^a-zA-Z0-9._\\-]", "_");
                downloadProgress = 0;
                lastUpdateStatus = "downloading";
                new Thread(() -> {
                    File apk = null;
                    String lastError = "";
                    for (String candidate : mirrorCandidates(url)) {
                        try {
                            apk = downloadToPrivate(candidate, safeName);
                            lastError = "";
                            break;
                        } catch (Exception e) {
                            lastError = e.getMessage() == null ? e.toString() : e.getMessage();
                        }
                    }
                    final File result = apk;
                    final String err = lastError;
                    runOnUiThread(() -> {
                        if (result != null && isValidApk(result)) {
                            downloadProgress = 100;
                            lastUpdateStatus = "installing";
                            notifyJs("window.__consoleUpdate && window.__consoleUpdate(100,'installing','')");
                            installApk(result);
                        } else if (result != null) {
                            lastUpdateStatus = "error";
                            notifyJs("window.__consoleUpdate && window.__consoleUpdate(100,'error','控制台安装包不完整，请稍后重试')");
                            toast("控制台安装包不完整，请稍后重试");
                        } else {
                            lastUpdateStatus = "error";
                            String jsErr = (err == null ? "" : err);
                            jsErr = jsErr.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"").replace("\r", " ").replace("\n", " ");
                            notifyJs("window.__consoleUpdate && window.__consoleUpdate(0,'error','" + jsErr + "')");
                            toast("控制台更新下载失败：" + err + "（请检查网络后重试）");
                        }
                    });
                }).start();
            }

            // 下载进度百分比（0~100），供页面进度条轮询
            @JavascriptInterface
            public String getDownloadProgress() {
                return String.valueOf(downloadProgress);
            }

            // 最近一次自更新状态：downloading / installing / success / error
            @JavascriptInterface
            public String getUpdateStatus() {
                return lastUpdateStatus;
            }

            // 真实已安装版本号（versionCode）——控制台据此判断是否需要更新
            @JavascriptInterface
            public String getVersionCode() {
                try {
                    PackageManager pm = getPackageManager();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        return String.valueOf(pm.getPackageInfo(getPackageName(), 0).getLongVersionCode());
                    }
                    return String.valueOf(pm.getPackageInfo(getPackageName(), 0).versionCode);
                } catch (Exception e) {
                    return "0";
                }
            }

            // 真实已安装版本名（versionName）
            @JavascriptInterface
            public String getVersionName() {
                try {
                    return String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                } catch (Exception e) {
                    return "";
                }
            }

            @JavascriptInterface
            public void toast(String msg) {
                runOnUiThread(() -> toastInternal(msg));
            }
        }, "Android");

        setContentView(webView);
        // v2.0.0：新版管理工作台（GitHub Pages 在线版，React 管理台），替代内嵌 WebView 页面
        // 旧版控制台（内嵌 index.html）→ 升级安装本版后直接打开新版管理工作台
        webView.loadUrl("https://shuting52.github.io/10-05landezhaole/");
    }

    // ==================== 控制台自更新（安装） ====================

    /** 下载到应用私有目录（无需任何存储权限，避免 Android 10+ 公共目录写入失败） */
    private File downloadToPrivate(String url, String safeName) throws IOException {
        File dir = new File(getCacheDir(), "console_update");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("无法创建缓存目录");
        }
        File apkFile = new File(dir, safeName);
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(20000);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android) YuntaiUpdater/1.1");
        int code = conn.getResponseCode();
        if (code < 200 || code >= 300) {
            throw new IOException("HTTP " + code);
        }
        long total = 0;
        final long contentLength = conn.getContentLengthLong();
        try (InputStream in = conn.getInputStream();
             OutputStream out = new FileOutputStream(apkFile)) {
            byte[] buf = new byte[16384];
            int n;
            while ((n = in.read(buf)) > 0) {
                out.write(buf, 0, n);
                total += n;
                // 实时进度回传：JS 进度条轮询 getDownloadProgress() 即可看到增长
                // v1.0.11 修复：chunked/无 Content-Length 时按 3.5MB 估算推进，避免进度条卡住不动
                if (contentLength > 0) {
                    downloadProgress = (int) (total * 100 / contentLength);
                } else {
                    downloadProgress = (int) (total * 100 / (3_500_000L)); // 控制台 APK 约 2.8MB，估 3.5MB
                }
                if (downloadProgress > 99) downloadProgress = 99;
            }
            out.flush();
        } finally {
            conn.disconnect();
        }
        if (total <= 0) {
            throw new IOException("下载内容为空");
        }
        return apkFile;
    }

    /** 备用镜像：国内加速镜像/ jsDelivr CDN 优先，raw.githubusercontent.com 最后（v1.1.23 调整顺序） */
    private List<String> mirrorCandidates(String url) {
        List<String> list = new ArrayList<>();
        try {
            String marker = "raw.githubusercontent.com/";
            if (url != null && url.contains(marker)) {
                String rest = url.substring(url.indexOf(marker) + marker.length());
                String[] parts = rest.split("/", 4);
                if (parts.length == 4) {
                    String gh = parts[0] + "/" + parts[1] + "@" + parts[2] + "/" + parts[3];
                    // 国内加速镜像（优先，速度快）
                    list.add("https://ghfast.top/https://raw.githubusercontent.com/" + rest);
                    list.add("https://ghproxy.net/https://raw.githubusercontent.com/" + rest);
                    list.add("https://cdn.jsdmir.cn/gh/" + gh);
                    // jsDelivr 多节点
                    list.add("https://testingcf.jsdelivr.net/gh/" + gh);
                    list.add("https://cdn.jsdelivr.net/gh/" + gh);
                    list.add("https://fastly.jsdelivr.net/gh/" + gh);
                    list.add("https://gcore.jsdelivr.net/gh/" + gh);
                    list.add("https://raw.gitmirror.com/" + rest);
                    // GitHub 官方 raw（最后兜底）
                    list.add(url);
                }
            } else if (url != null && !url.isEmpty()) {
                list.add(url);
            }
        } catch (Exception ignored) {
        }
        return list;
    }

    /** 校验下载产物是否为有效 APK（ZIP 魔数 + 最小体积） */
    private boolean isValidApk(File f) {
        try {
            if (f == null || !f.exists() || f.length() < 100 * 1024) return false;
            try (InputStream in = new FileInputStream(f)) {
                byte[] h = new byte[2];
                if (in.read(h) != 2) return false;
                return h[0] == 'P' && h[1] == 'K';
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void installApk(File apkFile) {
        // 优先 PackageInstaller 系统会话（原子替换旧版本、保留数据），失败回退 FileProvider
        if (installViaPackageInstaller(apkFile)) return;
        installViaFileProvider(apkFile);
    }

    /** PackageInstaller 会话安装（参照本体软件 AppUpdater 实现） */
    private boolean installViaPackageInstaller(File apkFile) {
        try {
            PackageInstaller pi = getPackageManager().getPackageInstaller();
            PackageInstaller.SessionParams params =
                    new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
            params.setAppPackageName(getPackageName());
            int sessionId = pi.createSession(params);
            PackageInstaller.Session session = pi.openSession(sessionId);
            try {
                try (OutputStream out = session.openWrite("console_update.apk", 0, apkFile.length());
                     InputStream in = new FileInputStream(apkFile)) {
                    byte[] buf = new byte[16384];
                    int n;
                    while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
                    session.fsync(out);
                }
            } finally {
                session.close();
            }
            Intent resultIntent = new Intent(ACTION_INSTALL_RESULT).setPackage(getPackageName());
            // v1.0.14 修复：FLAG_IMMUTABLE 在 Android 12+ 上系统无法向 PendingIntent 附加安装状态回调，
            // 导致「安装完成/失败」永远不送达 → 弹窗卡死、下载完"安装错误"。必须与本体一致改用 FLAG_MUTABLE。
            PendingIntent pending = PendingIntent.getBroadcast(
                    this,
                    INSTALL_RESULT_REQUEST_CODE,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
            );
            session.commit(pending.getIntentSender());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** FileProvider + 系统安装器（通用兜底方案） */
    private void installViaFileProvider(File file) {
        try {
            Uri apkUri = FileProvider.getUriForFile(this, "com.yuntai.fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            toastInternal("控制台新版本已下载，请点击安装完成更新");
        } catch (Exception e) {
            toastInternal("安装被拦截，请到系统设置允许安装未知应用后重试");
        }
    }

    /** 安装结果接收器：驱动「安装中 → 完成 / 失败（含签名冲突引导卸载）」，并同步给 JS 弹窗 */
    private void registerInstallReceiver() {
        installReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent == null) return;
                int status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, Integer.MIN_VALUE);
                String message = intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE);
                if (status == PackageInstaller.STATUS_PENDING_USER_ACTION) {
                    // 系统安装确认页（直接呈现新版本安装），不是「允许未知应用」设置页
                    Intent confirm = intent.getParcelableExtra(Intent.EXTRA_INTENT);
                    if (confirm != null) {
                        confirm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            startActivity(confirm);
                        } catch (Exception ignored) {
                        }
                    }
                    return;
                }
                if (status == PackageInstaller.STATUS_SUCCESS) {
                    lastUpdateStatus = "success";
                    notifyJs("window.__consoleUpdate && window.__consoleUpdate(100,'success','控制台已更新到新版本，请重新打开')");
                    toastInternal("控制台已更新到新版本，请重新打开");
                    return;
                }
                String msg = message == null ? "" : message;
                boolean signatureConflict = msg.contains("INSTALL_FAILED_UPDATE_INCOMPATIBLE")
                        || msg.contains("signatures do not match")
                        || msg.contains("signature");
                if (signatureConflict) {
                    lastUpdateStatus = "error";
                    notifyJs("window.__consoleUpdate && window.__consoleUpdate(100,'error','新版本与已安装版本签名不一致，需先卸载旧版控制台再安装')");
                    toastInternal("新版本与已安装版本签名不一致，需先卸载旧版控制台再安装");
                    openUninstall();
                } else if (status == PackageInstaller.STATUS_FAILURE_ABORTED) {
                    lastUpdateStatus = "error";
                    notifyJs("window.__consoleUpdate && window.__consoleUpdate(100,'error','已取消安装')");
                    toastInternal("已取消安装");
                } else {
                    lastUpdateStatus = "error";
                    notifyJs("window.__consoleUpdate && window.__consoleUpdate(100,'error','安装失败，请稍后重试')");
                    toastInternal("安装失败，请稍后重试");
                }
            }
        };
        IntentFilter filter = new IntentFilter(ACTION_INSTALL_RESULT);
        ContextCompat.registerReceiver(this, installReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED);
    }

    /** 向页面 JS 推送更新进度/结果（evaluateJavascript，UI 线程执行） */
    private void notifyJs(final String js) {
        runOnUiThread(() -> {
            try {
                if (webView != null) {
                    webView.evaluateJavascript(js, null);
                }
            } catch (Exception ignored) {
            }
        });
    }

    /** 控制台自更新 v1.0.6：不再提供「允许安装未知应用」设置页引导（已删除 ensureInstallPermission），
     *  进度条走完直接调用系统安装器呈现新版本安装。 */

    private void openUninstall() {
        try {
            Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } catch (Exception ignored) {
        }
    }

    private void toastInternal(String msg) {
        try {
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }
    }

    // ==================== 生命周期 ====================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (uploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            if (resultCode == Activity.RESULT_OK && data != null) {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
            uploadMessage.onReceiveValue(results);
            uploadMessage = null;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (installReceiver != null) {
            try {
                unregisterReceiver(installReceiver);
            } catch (Exception ignored) {
            }
            installReceiver = null;
        }
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
