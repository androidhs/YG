package android.eq366pt.zxtnetwork.com.yg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Test extends AppCompatActivity {
    WebView webview;
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                        Test.this.startActivity(intent);
                    } catch (Exception e) {
                        // 防止没有安装的情况
                        Toast.makeText(Test.this, "请安装百度地图", Toast.LENGTH_LONG)
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

        webview.addJavascriptInterface(getDataFromAndroid(), "jsEqObj");
        webview.loadUrl("http://192.168.16.162:52311/test.html");
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
                Toast.makeText(Test.this,name+" "+age,Toast.LENGTH_LONG).show();
            }
            @JavascriptInterface
            public void shareWithTitleAndTextAndUrl(String name,String age,String url){
                Toast.makeText(Test.this,"分享"+name+" "+age+" "+url,Toast.LENGTH_LONG).show();

            }
            @JavascriptInterface
            public void openNewWebViewPageWithTypeAndJson(String name,String age){
                Toast.makeText(Test.this,name+" "+age,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Test.this,
                                        MainActivity2.class);
                                startActivity(intent);
            }
//            @JavascriptInterface
//            public void favoriteWithIdAndTypeAndUrl(String name,String age,String url){
//                Toast.makeText(Test.this,"收藏"+name+" "+age+" "+url,Toast.LENGTH_LONG).show();
//            }

        };

        return insertObj;
    }

}
