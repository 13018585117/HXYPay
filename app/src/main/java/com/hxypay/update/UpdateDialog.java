package com.hxypay.update;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;
import com.hxypay.R;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.hxypay.bean.BuildConfig.VERSION_URL;


public class UpdateDialog extends Dialog {

    private Context context;
    private String message;
    private String title;
    private String confirmButtonText;
    private String cacelButtonText;
    private boolean isClese;
    private String fileName = "好信用.apk";
    private boolean isDownloading = false;
    private boolean isDownload_Ok = false;
    private String version;



    /* ----------------------------------接口监听---------------------------------------- */

    private ClickListenerInterface clickListenerInterface;
    private ProgressBar pb;
    private TextView tv_statusTv;
    private DownloadTask task;
    private TextView tvConfirm;
    private Button tvCancel;
    private final int INSTALL_APK_REQUESTCODE=10010;

    public interface ClickListenerInterface {
        void doConfirm();

        void doCancel();
    }

    public void setClickListener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    /* ----------------------------------构造方法：传值------------------------------------- */

    public UpdateDialog(Context context, String title, String message,String version, boolean isClose) {
        super(context, R.style.MyDialog);
        this.version = version;
        this.context = context;
        this.title = title;
        this.message = message;
        this.isClese = isClose;
    }


    /* ----------------------------------  onCreate  ------------------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("InflateParams")
    public void init() {
        setContentView(R.layout.update_dialog);

        TextView tvTitle = (TextView) findViewById(R.id.title);
        TextView tvMessage = (TextView) findViewById(R.id.message);
        TextView tv_hulue = (TextView) findViewById(R.id.tv_hulue);
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        tvConfirm = findViewById(R.id.yes);
        tvCancel = findViewById(R.id.no);
        pb = findViewById(R.id.pb);
        tv_statusTv = findViewById(R.id.statusTv);

        tv_version.setText(version);
        tvTitle.setText(title);
        tvMessage.setText(message);

        tvConfirm.setOnClickListener(new clickListener());
        tvCancel.setOnClickListener(new clickListener());
        tv_hulue.setOnClickListener(new clickListener());

        setCanceledOnTouchOutside(isClese);//设置窗口外是否可关闭

        setOnKeyListener(new OnKeyListener() {//设置按back键不关闭
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);//设置窗口位置
//            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口进出动画

            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高
            if (lp != null) {
                lp.width = (int) (d.widthPixels * 0.75); // 设置为屏幕宽度
            }
            dialogWindow.setAttributes(lp);
        }
        fileName = "hxygj"+version+".apk"/*+ getAppVersionName(context)*/;
        initTask();
        initStatus();
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.yes:
                    if (isDownloading){
                        Toast.makeText(context, "正在下载中,请耐心等待！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //已经下好了去安装；
                    if (isDownload_Ok){
                        checkIsAndroidO();
                        return;
                    }
                    clickListenerInterface.doConfirm();
                    startTask();
                    break;
                case R.id.no:
                    if (isDownloading){
                        Toast.makeText(context, "正在下载中,请耐心等待！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    clickListenerInterface.doCancel();
                    break;
                case R.id.tv_hulue: //忽略此版本；
            }
        }
    }

    private void initTask() {
        task = new DownloadTask.Builder(VERSION_URL, Environment.getExternalStorageDirectory())
                .setFilename(fileName)
                .setMinIntervalMillisCallbackProcess(15)
                .setPassIfAlreadyCompleted(false)
                .build();
    }
    private void initStatus() {
        final StatusUtil.Status status = StatusUtil.getStatus(task);
        if (status == StatusUtil.Status.COMPLETED) {
            pb.setProgress(pb.getMax());
        }

        tv_statusTv.setText(status.toString());
        final BreakpointInfo info = StatusUtil.getCurrentInfo(task);
        if (info != null) {
            Log.d("MainActivity", "初始化状态" + info.toString());
            Util_Update.calcProgressToView(pb, info.getTotalOffset(), info.getTotalLength());
        }
    }

    private void startTask() {
        task.enqueue(new DownloadListener4WithSpeed() {
            private long totalLength;
            private String readableTotalLength;
            @Override
            public void taskStart(@NonNull DownloadTask task) {
                tv_statusTv.setText("开始下载...");
                isDownloading = true;
                pb.setVisibility(View.VISIBLE);
                tvConfirm.setText("下载中...");
                tv_statusTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {
                final String status = "开始连接 " + blockIndex;
                tv_statusTv.setText(status);
            }

            @Override
            public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {
                final String status = "结束连接 " + blockIndex;
                tv_statusTv.setText(status);
            }

            @Override
            public void infoReady(@NonNull DownloadTask task, @NonNull BreakpointInfo info, boolean fromBreakpoint, @NonNull Listener4SpeedAssistExtend.Listener4SpeedModel model) {
                totalLength = info.getTotalLength();
                readableTotalLength = Util_Update.humanReadableBytes(totalLength, true);
                Util_Update.calcProgressToView(pb,info.getTotalOffset(),totalLength);
            }

            @Override
            public void progressBlock(@NonNull DownloadTask task, int blockIndex, long currentBlockOffset, @NonNull SpeedCalculator blockSpeed) {

            }

            @Override
            public void progress(@NonNull DownloadTask task, long currentOffset, @NonNull SpeedCalculator taskSpeed) {
                final String readableOffset = com.liulishuo.okdownload.core.Util.humanReadableBytes(currentOffset, true);
                final String progressStatus = readableOffset + "/" + readableTotalLength;
                final String speed = taskSpeed.speed();
                final String progressStatusWithSpeed = progressStatus + "(" + speed + ")";

                tv_statusTv.setText(progressStatusWithSpeed);
                Util_Update.calcProgressToView(pb, currentOffset, totalLength);
            }

            @Override
            public void blockEnd(@NonNull DownloadTask task, int blockIndex, BlockInfo info, @NonNull SpeedCalculator blockSpeed) {

            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull SpeedCalculator taskSpeed) {
                final String statusWithSpeed = cause.toString() + " " + taskSpeed.averageSpeed();
                tv_statusTv.setText(statusWithSpeed);

                // mark
                task.setTag(null);
                if (cause == EndCause.COMPLETED) {
                    Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                    checkIsAndroidO();
                    isDownload_Ok = true;
                    tvConfirm.setText("去安装(已下载完成)");
                    isDownloading = false;
                    cancel();
                }else {
                    isDownloading = false;
                    tvConfirm.setText("开始下载");
                }
            }
        });
    }
    /**
     * 安装apk
     * @param
     * @param
     *
     */
    public void openFile() {
        try {
            String filePath = Environment.getExternalStorageDirectory() + File.separator + fileName;
            File apkFile = new File(filePath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context,"com.hxypay.fileprovider",apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否是8.0,8.0需要处理未知应用来源权限问题,否则直接安装
     */
    private void checkIsAndroidO() {
        if (Build.VERSION.SDK_INT >= 26) {
            //来判断应用是否有权限安装apk
            boolean installAllowed= context.getPackageManager().canRequestPackageInstalls();
            //有权限
            if (installAllowed) {
                //安装apk
                openFile();
            } else {
                //无权限 申请权限
                //将用户引导至安装未知应用界面。
                Uri packageURI = Uri.parse("package:"+context.getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
                context.startActivity(intent);
                Toast.makeText(context, "请允许来自此来源的应用，才能进行安装", Toast.LENGTH_SHORT).show();
            }
        } else {
            openFile();
        }
    }

}
