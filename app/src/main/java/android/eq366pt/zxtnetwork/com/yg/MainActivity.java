package android.eq366pt.zxtnetwork.com.yg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {
    WebView webview;
    @SuppressLint("JavascriptInterface")
    LinearLayout linearLayout;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout=findViewById(R.id.ll_content);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.goBack();
            }
        });
        //        unzip("ppap.zip");
//        FileUtils.createFile("ppap.zip");
        webview=findViewById(R.id.webview);
//        webView.loadUrl("https://sale.jd.com/act/3f50caPYsxd1MJmQ.html?cpdad=1DLSUE");
//
//        webView.registerHandler("close", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                Toast.makeText(MainActivity.this,data,Toast.LENGTH_LONG).show();
//                finish();
//            }
//        });
//        copyFilesFassets(this, "ppap.zip", Environment.getExternalStorageDirectory() + "/ppap.zip" );
//        unzip(Environment.getExternalStorageDirectory() + "/ppap.zip");



        webview.getSettings().setJavaScriptEnabled(true);
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS
         * ：适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN : 适应屏幕，内容将自动缩放
         */
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setHorizontalScrollbarOverlay(true);
        webview.setHorizontalScrollBarEnabled(true);
        // webview.addJavascriptInterface(this, "javatojs");
        webview.requestFocus();
        // 开启Application H5 Caches 功能
        webview.getSettings().setAppCacheEnabled(false);
        // 设置Application Caches 缓存目录
        // 开启 database storage API 功能
        webview.getSettings().setDatabaseEnabled(false);
        // 设置数据库缓存路径
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
            }
        });
        webview.setWebViewClient(new WebViewClient() {

        });

        webview.evaluateJavascript("alert('ddddd')", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

               // Log.d("shouldOverrideUrlLoading", url);
                // 处理自定义scheme
                if (!url.startsWith("http")) {
                   // Log.i("shouldOverrideUrlLoading", "处理自定义scheme");

                    try {
                        // 以下固定写法
                        final Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        MainActivity.this.startActivity(intent);
                    } catch (Exception e) {
                        // 防止没有安装的情况
                        Toast.makeText(MainActivity.this, "请安装百度地图", Toast.LENGTH_LONG)
                                .show();
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Auto-generated method stub
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

        });

//        webview.addJavascriptInterface(getDataFromAndroid(), "jsEqObj");
//        webview.loadUrl("http://192.168.16.162:52311/test.html");
        webview.loadUrl("https://item.jd.com/5270331.html");
        webview.loadUrl("http://www.baidu.com");
//        webview.loadUrl("url");


       // webview.loadUrl(HttpContants.Knowledgelist);
//        tvBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (webview.canGoBack()){
//                    webview.goBack();
//                }else {
//                    finish();
//                }
//            }
//        });
    }

//    keyLED keyLED = new keyLED();

    /**
     * 读取assets中的zip
     *
     * @param assestsname
     */
    public void unzip(final String assestsname) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    InputStream open = getResources().getAssets().open(assestsname);
                    InputStream open=new FileInputStream(new File(assestsname));
                    ZipInputStream zipinputstream = new ZipInputStream(open);
                    ZipEntry nextEntry = zipinputstream.getNextEntry();
                    int size = (int) nextEntry.getSize();
                    byte buf[] = new byte[size];
                    int i = 0;
                    while (nextEntry != null) {

                        if (!nextEntry.isDirectory()) {//判断是文件夹

                            if (nextEntry.getName().contains("keyLED")) {


                                zipinputstream.read(buf, 0, size);
                                i++;
                                ArrayList<Integer> temp = new ArrayList<>();
                                for (int j = 0; j < 4; j++) {
                                    temp.add(Integer.parseInt(nextEntry.getName().replace("keyLED/", "").split(" ")[j]));

                                }
//                                keyLED.getLed().add(temp);


                            }

                        } else {

                        }

                        nextEntry = zipinputstream.getNextEntry();
                    }

//                                        for (int j = 0; j <keyLED.getLed().size() ; j++) {
//                                            Log.i("lbk",keyLED.getLed().get(j).toString());
//                                        }


                    zipinputstream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void readfile(ZipFile zf, ZipEntry nextEntry) throws IOException {
        long size = nextEntry.getSize();
        if (size > 0) {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(zf.getInputStream(nextEntry)));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        }
    }

    /**
     * 读取二进制
     *
     * @param fileName
     * @return
     */
    public String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String  原文件路径  如：/aa
     * @param newPath String  复制后路径  如：xx:/bb/cc
     */
    public void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
        }
    }
    private Object getDataFromAndroid() {
        Object insertObj = new Object() {
            String result = "";

            @JavascriptInterface
            public void openactivity(String unique) {
                //                Bundle bundle = new Bundle();
                //                bundle.putString("unique", unique);
                //                Intent intent = new Intent(ProductCenterDetailsActivity.this,
                //                        ShiwuShangChengActivity.class);
                //                intent.putExtras(bundle);
                //                startActivity(intent);
            }

            @JavascriptInterface
            public void close() {
                finish();
            }
            @JavascriptInterface
            public void getNameAge(String name,int age){
                Toast.makeText(MainActivity.this,name+" "+age,Toast.LENGTH_LONG).show();
            }
            @JavascriptInterface
            public void shareWithTitleAndTextAndUrl(String name,String age,String url){
                Toast.makeText(MainActivity.this,"分享"+name+" "+age+" "+url,Toast.LENGTH_LONG).show();

            }
            @JavascriptInterface
            public void openNewWebViewPageWithTypeAndJson(String name,String age){
                Toast.makeText(MainActivity.this,name+" "+age,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this,
                                        MainActivity2.class);
                                startActivity(intent);
            }
            @JavascriptInterface
            public void favoriteWithIdAndTypeAndUrl(String name,String age,String url){
                Toast.makeText(MainActivity.this,"收藏"+name+" "+age+" "+url,Toast.LENGTH_LONG).show();
            }

        };

        return insertObj;
    }

}
