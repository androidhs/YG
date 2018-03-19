package android.eq366pt.zxtnetwork.com.yg;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

public class MainActivity2 extends AppCompatActivity {
    WebView webview;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //        unzip("ppap.zip");
        FileUtils.createFile("ppap.zip");
//        mViewPager=findViewById(R.id.vp_pager);
//        mViewPager.setPageMargin(20);
//
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView vp_iv= new ImageView(MainActivity2.this);
                vp_iv.setImageResource(R.drawable.default_banner);
              //  Glide.with(context).load(myInfoLists.get(position%myInfoLists.size()).getActivityImg()).into(vp_iv);
                container.addView(vp_iv);
                return vp_iv;
            }
        });
    }

//    keyLED keyLED = new keyLED();
//
//    /**
//     * 读取assets中的zip
//     *
//     * @param assestsname
//     */
//    public void unzip(final String assestsname) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
////                    InputStream open = getResources().getAssets().open(assestsname);
//                    InputStream open=new FileInputStream(new File(assestsname));
//                    ZipInputStream zipinputstream = new ZipInputStream(open);
//                    ZipEntry nextEntry = zipinputstream.getNextEntry();
//                    int size = (int) nextEntry.getSize();
//                    byte buf[] = new byte[size];
//                    int i = 0;
//                    while (nextEntry != null) {
//
//                        if (!nextEntry.isDirectory()) {//判断是文件夹
//
//                            if (nextEntry.getName().contains("keyLED")) {
//
//
//                                zipinputstream.read(buf, 0, size);
//                                i++;
//                                ArrayList<Integer> temp = new ArrayList<>();
//                                for (int j = 0; j < 4; j++) {
//                                    temp.add(Integer.parseInt(nextEntry.getName().replace("keyLED/", "").split(" ")[j]));
//
//                                }
//                                keyLED.getLed().add(temp);
//
//
//                            }
//
//                        } else {
//
//                        }
//
//                        nextEntry = zipinputstream.getNextEntry();
//                    }
//
//                                        for (int j = 0; j <keyLED.getLed().size() ; j++) {
//                                            Log.i("lbk",keyLED.getLed().get(j).toString());
//                                        }
//
//
//                    zipinputstream.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }
//
//    private void readfile(ZipFile zf, ZipEntry nextEntry) throws IOException {
//        long size = nextEntry.getSize();
//        if (size > 0) {
//            BufferedReader br = new BufferedReader(
//                    new InputStreamReader(zf.getInputStream(nextEntry)));
//            String line;
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }
//            br.close();
//        }
//    }
//
//    /**
//     * 读取二进制
//     *
//     * @param fileName
//     * @return
//     */
//    public String readToString(String fileName) {
//        String encoding = "UTF-8";
//        File file = new File(fileName);
//        Long filelength = file.length();
//        byte[] filecontent = new byte[filelength.intValue()];
//        try {
//            FileInputStream in = new FileInputStream(file);
//            in.read(filecontent);
//            in.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            return new String(filecontent, encoding);
//        } catch (UnsupportedEncodingException e) {
//            System.err.println("The OS does not support " + encoding);
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 从assets目录中复制整个文件夹内容
//     *
//     * @param context Context 使用CopyFiles类的Activity
//     * @param oldPath String  原文件路径  如：/aa
//     * @param newPath String  复制后路径  如：xx:/bb/cc
//     */
//    public void copyFilesFassets(Context context, String oldPath, String newPath) {
//        try {
//            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
//            if (fileNames.length > 0) {//如果是目录
//                File file = new File(newPath);
//                file.mkdirs();//如果文件夹不存在，则递归
//                for (String fileName : fileNames) {
//                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
//                }
//            } else {//如果是文件
//                InputStream is = context.getAssets().open(oldPath);
//                FileOutputStream fos = new FileOutputStream(new File(newPath));
//                byte[] buffer = new byte[1024];
//                int byteCount = 0;
//                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
//                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
//                }
//                fos.flush();//刷新缓冲区
//                is.close();
//                fos.close();
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            //如果捕捉到错误则通知UI线程
//        }
//    }
//    private Object getDataFromAndroid() {
//        Object insertObj = new Object() {
//            String result = "";
//
//            @JavascriptInterface
//            public void openactivity(String unique) {
//                //                Bundle bundle = new Bundle();
//                //                bundle.putString("unique", unique);
//                //                Intent intent = new Intent(ProductCenterDetailsActivity.this,
//                //                        ShiwuShangChengActivity.class);
//                //                intent.putExtras(bundle);
//                //                startActivity(intent);
//            }
//
//            @JavascriptInterface
//            public void close() {
//                finish();
//            }
//            @JavascriptInterface
//            public void getNameAge(String name,int age){
//                Toast.makeText(MainActivity2.this,name+" "+age,Toast.LENGTH_LONG).show();
//            }
//            @JavascriptInterface
//            public void shareWithTitleAndTextAndUrl(String name,String age,String url){
//                Toast.makeText(MainActivity2.this,name+" "+age+" "+url,Toast.LENGTH_LONG).show();
//            }
//            @JavascriptInterface
//            public void openNewWebViewPageWithTypeAndJson(String name,String age){
//                Toast.makeText(MainActivity2.this,name+" "+age,Toast.LENGTH_LONG).show();
//            }
//            @JavascriptInterface
//            public void favoriteWithIdAndTypeAndUrl(String name,String age,String url){
//                Toast.makeText(MainActivity2.this,name+" "+age+" "+url,Toast.LENGTH_LONG).show();
//            }
//
//        };
//
//        return insertObj;
//    }

}
