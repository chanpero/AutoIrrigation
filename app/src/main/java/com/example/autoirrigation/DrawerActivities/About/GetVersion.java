package com.example.autoirrigation.DrawerActivities.About;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetVersion extends AppCompatActivity{

    //获取网络版本号
    public int getUpdatecode() {
        int vcode = 0;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://119.23.42.156:8080/apk/output.json")
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            JSONArray jsonarray = new JSONArray(responseData);
            JSONObject jsonObject = jsonarray.getJSONObject(0);
            JSONObject apkData = jsonObject.getJSONObject("apkData");
            vcode = apkData.getInt("versionCode");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vcode;
    }
    //获取当前版本号
    public static int getVersionCode(Context context) {
        int version = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    //更新界面
    public void loadNewVersionAlertDiaLog(final Context context) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("说明");
        alert.setMessage("发现新版本,是否立即更新?");
        alert.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadApk(context);
            }

        });
        alert.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create();
        alert.show();

    }


    /**

     * 从服务器端下载最新apk

     */

    private void downloadApk(Context context) {
        //显示下载进度
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();
        new Thread(new DownloadApk(dialog,context)).start();
    }

    /**

     * 访问网络下载apk

     */

    private class DownloadApk implements Runnable {
        private ProgressDialog dialog;
        private Context context;
        InputStream is;
        FileOutputStream fos;

        public DownloadApk(ProgressDialog dialog,Context context) {
            this.dialog = dialog;
            this.context = context;
        }

        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            String url = "http://119.23.42.156:8080/apk/app-release.apk";
            Request request = new Request.Builder().get().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    //获取内容总长度
                    long contentLength = response.body().contentLength();
                    //设置最大值
                    dialog.setMax((int) contentLength);
                    //保存到sd卡
                    File apkFile = new File(Environment.getExternalStorageDirectory(),  "test.apk");
                    fos = new FileOutputStream(apkFile);
                    //获得输入流
                    is = response.body().byteStream();
                    //定义缓冲区大小
                    byte[] bys = new byte[1024];
                    int progress = 0;
                    int len = -1;
                    while ((len = is.read(bys)) != -1) {
                        try {
                            Thread.sleep(1);
                            fos.write(bys, 0, len);
                            fos.flush();
                            progress += len;
                            //设置进度
                            dialog.setProgress(progress);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    installApk(Environment.getExternalStorageDirectory()+"/test.apk",context);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //关闭io流
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    is = null;
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fos = null;
                }
            }
            dialog.dismiss();
        }
    }

    private void installApk(String file,Context context) {
        //调用系统安装程序
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File apkFile = new File(file);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(context,  "com.example.autoirrigation"+ ".fileprovider", apkFile);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
