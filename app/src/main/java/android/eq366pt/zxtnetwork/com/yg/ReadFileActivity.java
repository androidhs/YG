package android.eq366pt.zxtnetwork.com.yg;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import jp.kshoji.driver.midi.activity.AbstractSingleMidiActivity;
import jp.kshoji.driver.midi.device.MidiInputDevice;
import jp.kshoji.driver.midi.device.MidiOutputDevice;
import jp.kshoji.driver.midi.util.UsbMidiDriver;

/**
 * Created by lbk on 2018/2/5.
 */

public class ReadFileActivity extends AbstractSingleMidiActivity {
    private String filePath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/ppap.zip";
    private String fileTargetPath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/ppap/";
    LinearLayout contentLayout;//按钮的父布局
    LinearLayout ll_pages;//页数的父布局
    ArrayList<KeySound> lst = new ArrayList<>();//按键映射音源的集合
    ArrayList<Integer> pages = new ArrayList<>();
    ArrayList<keyLED> keyLEDS = new ArrayList<>();//按键对应灯光文件名的集合
    ArrayList<LED> ledOptionLst = new ArrayList<>();

    ArrayList<Autoplay> autoplays = new ArrayList<>();
    private int page = 1;
    MediaPlayer mPlayer = new MediaPlayer();
    Button[][] buttons;
    JSONArray colorjson;
    Button btn_play;
    private UsbMidiDriver usbMidiDriver;
    MidiOutputDevice midiOutputDevice;
//    ProgressDialog waitingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_activity);
        contentLayout = findViewById(R.id.ll_content);


        ll_pages = findViewById(R.id.ll_pages);
        btn_play = findViewById(R.id.btn_play);
//         waitingDialog=
//                new ProgressDialog(this);
//        waitingDialog.setIndeterminate(true);
//        waitingDialog.setCancelable(false);
//        waitingDialog.show();
        //初始化所有颜色
        try {
            String colors = getResources().getString(R.string.colors);
            colorjson = new JSONArray(colors);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //       文件 -------------------------------------------------------------------
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(this)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(ReadFileActivity.this, rationale).show();
                    }
                })
                .start();
        //      ui -------------------------------------------------------------------------------------
        LinearLayout[] linearLayout = new LinearLayout[8];
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f);
        buttons = new Button[8][8];
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        buttonParams.setMargins(1, 1, 1, 1);
        for (int i = 0; i < 8; i++) {
            linearLayout[i] = new LinearLayout(this);
            linearLayout[i].setOrientation(LinearLayout.HORIZONTAL);
            linearLayout[i].setLayoutParams(linearParams);
            for (int j = 0; j < 8; j++) {
                buttons[i][j] = new Button(this);
                buttons[i][j].setBackgroundColor(originalColor);
                buttons[i][j].setLayoutParams(buttonParams);
                linearLayout[i].addView(buttons[i][j]);
            }
            contentLayout.addView(linearLayout[i]);
        }
        //        为每个按钮绑定事件------------------------------------------------------------------

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final int finalI = i;
                final int finalJ = j;
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Log.d("lbk", (finalI + 1) + " " + (finalJ + 1) + " " + page);
                        //点击后去匹配ledkeys文件
                        selectLed(page, finalI + 1, finalJ + 1);
                        //点击后去匹配sounds文件
                        selectMusic(page, finalI + 1, finalJ + 1);

                    }
                });
            }
        }

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < autoplays.size(); i++) {
                            if (null != autoplays.get(i).getC()) {
                                page = autoplays.get(i).getPage();
                            } else if (null != autoplays.get(i).getD()) {
                                final long dt = autoplays.get(i).getTime();
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(dt);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                try {
                                    thread.start();

                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            } else if (null != autoplays.get(i).getO()) {
                                final int finalI = i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        selectLed(page, autoplays.get(finalI).getRow(), autoplays.get(finalI).getColumn());

                                    }
                                });
                                selectMusic(page, autoplays.get(i).getRow(), autoplays.get(i).getColumn());
                            } else if (null != autoplays.get(i).getF()) {
                                //                                runOnUiThread(new Runnable() {
                                //                                    @Override
                                //                                    public void run() {
                                //                                        for (int j = 0; j <8 ; j++) {
                                //                                            for (int k = 0; k <8 ; k++) {
                                //                                                buttons[j][k].setBackgroundColor(originalColor);
                                //                                            }
                                //                                        }
                                //                                    }
                                //                                });

                            }
                        }
                    }
                }).start();

            }
        });


    }

    private void selectLed(int page, int x, int y) {
        for (int i = 0; i < ledOptionLst.size(); i++) {
            int musicpage = ledOptionLst.get(i).getKeyLED().getPage();
            int mX = ledOptionLst.get(i).getKeyLED().getRow();
            int mY = ledOptionLst.get(i).getKeyLED().getColumn();
            //String path = lst.get(i).getPath();


            if (musicpage == page && mX == x && mY == y) {
                // Log.w("lbk", mX + " " + mY + " " + musicpage);
                //构造动画集合
                discolor(i);
                break;

            }
        }
    }

    int originalColor = Color.parseColor("#bec6d1");

    /**
     * 构造动画集合并播放
     *
     * @param index
     */
    private void discolor(int index) {
        final int repeat = ledOptionLst.get(index).getKeyLED().getRepeat();
        final ArrayList<LEDOPTION> templst = ledOptionLst.get(index).getOptions();
        ArrayList<LEDOPTION> animatorsResLst = new ArrayList<>();


        ArrayList<LEDOPTION> DelayLst = new ArrayList<>();//delay LEDOPTION的集合
        ArrayList<Integer> delayIndexLst = new ArrayList<>();//delay 角标的集合
        //检测一共有几个delay
        for (int j = 0; j < templst.size(); j++) {
            if (null != templst.get(j).getD()) {
                delayIndexLst.add(j);
                DelayLst.add(templst.get(j));
            }
        }
        //        BlockingQueue blockingQueue=new LinkedBlockingQueue();
        //        final ArrayList<Thread> threads = new ArrayList<>();
        //                new Thread(new Runnable() {
        //                    @Override
        //                    public void run() {
        //
        //                        for (int i = 0; i < repeat; i++) {
        //
        //                            final Collection<Animator> alst = new ArrayList<>();
        //
        //                            for (int j = 0; j < templst.size(); j++) {
        //                                ObjectAnimator objectAnimator = new ObjectAnimator();
        //                                final LEDOPTION ledoption = templst.get(j);
        //                                try {
        //                                    JSONObject colors = (JSONObject) colorjson.get(ledoption.getV());
        //                                    final String color = colors.getString("color");
        //
        //
        //                                    if (null != ledoption.getD()) {
        //                                        Thread thread = new Thread(new Runnable() {
        //                                            @Override
        //                                            public void run() {
        //                                                try {
        //                                                    Thread.sleep(ledoption.getTime());
        //                                                } catch (InterruptedException e) {
        //                                                    e.printStackTrace();
        //                                                }
        //                                            }
        //                                        });
        //                                        thread.start();
        //                                        thread.join();
        //                                    }
        //
        //                                    if (null != ledoption.getO() || null != ledoption.getF()) {
        //                                        objectAnimator.setTarget(buttons[ledoption.getRow() - 1][ledoption.getColumn() - 1]);
        //                                        objectAnimator.setPropertyName("backgroundColor");
        //                                        if (null != ledoption.getO()) {
        //
        //                                            objectAnimator.setIntValues(Color.parseColor(color));
        //                                        } else {
        //                                            objectAnimator.setIntValues(originalColor);
        //
        //                                        }
        //
        //                                        objectAnimator.setEvaluator(new MyEvaluator());
        //                                        alst.add(objectAnimator);
        //                                        if (j + 1 < templst.size()) {
        //                                            if (null != templst.get(j + 1).getD()) {
        //                                                runOnUiThread(new Runnable() {
        //                                                    @Override
        //                                                    public void run() {
        //                                                        AnimatorSet animatorSet = new AnimatorSet();
        //                                                        ArrayList<Animator> temp = new ArrayList<>();
        //                                                        temp.addAll(alst);
        //                                                        animatorSet.playTogether(temp);
        //                                                        animatorSet.start();
        //
        //                                                        alst.clear();
        //                                                    }
        //                                                });
        //
        //
        //                                            }
        //                                        } else {
        //
        //                                            runOnUiThread(new Runnable() {
        //                                                @Override
        //                                                public void run() {
        //                                                    AnimatorSet animatorSet = new AnimatorSet();
        //                                                    ArrayList<Animator> temp = new ArrayList<>();
        //                                                    temp.addAll(alst);
        //                                                    animatorSet.playTogether(temp);
        //                                                    animatorSet.start();
        //                                                    alst.clear();
        //                                                }
        //                                            });
        //
        //
        //                                        }
        //
        //                                    }
        //                                } catch (JSONException e) {
        //                                    e.printStackTrace();
        //                                } catch (InterruptedException e) {
        //                                    e.printStackTrace();
        //                                }
        //
        //
        //                            }
        //
        //                        }
        //
        //                    }
        //                }).start();


        final ArrayList<Thread> threads = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < repeat; i++) {


                    for (int j = 0; j < templst.size(); j++) {
                        ObjectAnimator objectAnimator = new ObjectAnimator();
                        final LEDOPTION ledoption = templst.get(j);

                        JSONObject colors = null;
                        try {
                            colors = (JSONObject) colorjson.get(ledoption.getV());
                            final String color = colors.getString("color");


                            if (null != ledoption.getD()) {
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(ledoption.getTime());
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                thread.start();
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (null != ledoption.getO()) {
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        midiOutputDevice = getMidiOutputDevice();
//
//                                        if (midiOutputDevice==null){
//                                            return;
//                                        }
//                                        midiOutputDevice.sendMidiNoteOn(0, page-1, Integer.parseInt((9-ledoption.getRow()) + "" + (ledoption.getColumn())), ledoption.getV());
//
//                                    }
//                                }).start();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        buttons[ledoption.getRow() - 1][ledoption.getColumn() - 1].setBackgroundColor(Color.parseColor(color));

                                    }
                                });
                            }
                            if (null != ledoption.getF()) {
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        midiOutputDevice = getMidiOutputDevice();
//                                        if (midiOutputDevice==null){
//                                            return;
//                                        }
//                                        midiOutputDevice.sendMidiNoteOff(0, page-1, Integer.parseInt((9-ledoption.getColumn()) + "" + (ledoption.getRow())), 0);
//
//                                    }
//                                }).start();


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        buttons[ledoption.getRow() - 1][ledoption.getColumn() - 1].setBackgroundColor(originalColor);


                                    }
                                });


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        }).start();


        //截取非d的集合

        //        int from = 0;
        //        ArrayList<List<LEDOPTION>> animationlst = new ArrayList<>();
        //
        //        if (delayIndexLst.size() > 0) {
        //            if (delayIndexLst.get(0) == 0) {
        //                if (delayIndexLst.size() > 1) {
        //                    from = 1;
        //                    for (int i = 1; i < delayIndexLst.size(); i++) {
        //                        animationlst.add(templst.subList(from, delayIndexLst.get(i)));
        //                        from = delayIndexLst.get(i) + 1;
        //                    }
        //                    animationlst.add(templst.subList(from, templst.size()));
        //
        //                }
        //            } else {
        //                for (int i = 0; i < delayIndexLst.size(); i++) {
        //                    animationlst.add(templst.subList(from, delayIndexLst.get(i)));
        //                    from = delayIndexLst.get(i) + 1;
        //                }
        //                animationlst.add(templst.subList(from, templst.size()));
        //            }
        //        } else {
        //            animationlst.add(templst);
        //        }
        //
        //
        //        //将截取后的集合转化为animationSet
        //        final ArrayList<AnimatorSet> animatorSetArrayList = new ArrayList<>();
        //        for (int i = 0; i < animationlst.size(); i++) {
        //            AnimatorSet animatorSet = new AnimatorSet();
        //            Collection<Animator> alst = new ArrayList<>();
        //            for (int j = 0; j < animationlst.get(i).size(); j++) {
        //
        //
        //                ObjectAnimator objectAnimator = new ObjectAnimator();
        //                LEDOPTION ledoption = animationlst.get(i).get(j);
        //                JSONObject colorobj = null;
        //                try {
        //                    colorobj = colorjson.getJSONObject(ledoption.getV());
        //                    String color = colorobj.getString("color");
        //                    if (null != ledoption.getO() || null != ledoption.getF()) {
        //                        objectAnimator.setTarget(buttons[ledoption.getRow() - 1][ledoption.getColumn() - 1]);
        //                        objectAnimator.setPropertyName("backgroundColor");
        //                        if (null != ledoption.getO()) {
        //
        //                            objectAnimator.setIntValues(Color.parseColor(color));
        //                        } else {
        //                            objectAnimator.setIntValues(originalColor);
        //
        //                        }
        //
        //                        objectAnimator.setEvaluator(new MyEvaluator());
        //                    }
        //                    alst.add(objectAnimator);
        //                } catch (JSONException e) {
        //                    e.printStackTrace();
        //                }
        //
        //            }
        //            animatorSet.playTogether(alst);
        //            animatorSetArrayList.add(animatorSet);
        //        }
        //
        //
        //        ArrayList<Integer> sumTempDelay = new ArrayList<>();
        //        int sum = 0;
        //        for (int i = 0; i < DelayLst.size(); i++) {
        //            sum += DelayLst.get(i).getTime();
        //            sumTempDelay.add(sum);
        //        }
        //
        //
        //        //将延迟加入动画集合
        //        //        int delayNum = 0;
        //        int maxDelayTime = 0;//单次最长延迟时间
        //        int delaytime = DelayLst.size() == 0 ? 0 : DelayLst.get(0).getTime();
        //        for (int i = 0; i < sumTempDelay.size(); i++) {
        //
        //            int time = sumTempDelay.get(i);
        //            if (animatorSetArrayList.size() > DelayLst.size()) {
        //
        //                animatorSetArrayList.get(i + 1).setStartDelay(time);
        //            } else {
        //                animatorSetArrayList.get(i).setStartDelay(time);
        //
        //            }
        //
        //        }
        //        new Thread(new Runnable() {
        //            @Override
        //            public void run() {
        //                for (int j = 0; j <repeat ; j++) {
        //                    for (int i = 0; i < animatorSetArrayList.size(); i++) {
        //                        final int finalI = i;
        //                        runOnUiThread(new Runnable() {
        //                            @Override
        //                            public void run() {
        //                                animatorSetArrayList.get(finalI).start();
        //
        //                            }
        //                        });
        //                    }
        //                    Thread thread=new Thread(new Runnable() {
        //                        @Override
        //                        public void run() {
        //                            try {
        //                                Thread.sleep(10);
        //                            } catch (InterruptedException e) {
        //                                e.printStackTrace();
        //                            }
        //                        }
        //                    });
        //                    thread.start();
        //                    try {
        //                        thread.join();
        //                    } catch (InterruptedException e) {
        //                        e.printStackTrace();
        //                    }
        //
        //                }
        //            }
        //        }).start();


        //        if (repeat > 1) {
        //            if (animatorSetArrayList.size() < 1)
        //                return;
        //            animatorSetArrayList.get(animatorSetArrayList.size() - 1).addListener(new Animator.AnimatorListener() {
        //                int count = 0;
        //
        //                @Override
        //                public void onAnimationStart(Animator animation) {
        //                    count++;
        //                }
        //
        //                @Override
        //                public void onAnimationEnd(Animator animation) {
        //                    if (count < repeat-1) {
        //                        for (int i = 0; i < animatorSetArrayList.size(); i++) {
        //                            animatorSetArrayList.get(i).start();
        //                        }
        //                    }
        //                }
        //
        //                @Override
        //                public void onAnimationCancel(Animator animation) {
        //
        //                }
        //
        //                @Override
        //                public void onAnimationRepeat(Animator animation) {
        //
        //                }
        //            });
        //        }


    }

    /**
     * 校验是否有资源文件（当前页 x y完全匹配才能播放）
     *
     * @param page
     * @param x
     * @param y
     */

    public void selectMusic(int page, int x, int y) {
        for (int i = 0; i < lst.size(); i++) {
            int musicpage = lst.get(i).getPage();
            int mX = lst.get(i).getRow();
            int mY = lst.get(i).getColumn();
            String path = lst.get(i).getPath();


            if (musicpage == page && mX == x && mY == y) {
                Log.w("lbk", mX + " " + mY + " " + musicpage);
                playMusic(i + 1);
                break;

            }
        }
    }

    //初始化SoundPool
    private SoundPool soundPool = new SoundPool(Integer.MAX_VALUE, AudioManager.STREAM_SYSTEM, 5);

    public void playMusic(int i) {
        Log.e("lbk", "播放音乐" + filePath);
        //加载deep 音频文件
        // soundPool.load(filePath, 1);
        //播放deep
        soundPool.play(i, 1, 1, 0, 0, 1);

    }

    /**
     * 读取zip得到keysound映射
     *
     * @param file
     * @return
     */
    public void initRes(final File file) {
        lst.clear();
        try {
            ZipFile zf = new ZipFile(file);
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            ZipInputStream zin = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                } else {
                    //声音文件读取，并存储映射
                    Log.e("getKeySounds", ze.getComment() + " " + ze.getName());
                    if (ze.getName().equals("keySound")) {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(zf.getInputStream(ze)));
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (!line.trim().equals("")) {
                                String keySound[] = line.split(" ");
                                lst.add(new KeySound(Integer.parseInt(keySound[0]), Integer.parseInt(keySound[1]), Integer.parseInt(keySound[2]), keySound[3]));
                            }

                        }
                        br.close();

                    } else if (ze.getName().contains("keyLED")) { //灯光文件读取，并存储映射
                        Log.e("lbk", ze.getName());
                        String keyled[] = ze.getName().replace("keyLED/", "").split(" ");
                        keyLEDS.add(new keyLED(Integer.parseInt(keyled[0]), Integer.parseInt(keyled[1]), Integer.parseInt(keyled[2]), Integer.parseInt(keyled[3])));
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(zf.getInputStream(ze)));
                        String line;
                        ArrayList<LEDOPTION> ledoptions = new ArrayList<>();//每个灯光文件内容的集合

                        while ((line = br.readLine()) != null) {
                            if (!line.trim().equals("")) {
                                String ledoption[] = line.split(" ");
                                if (ledoption[0].equals("o") || ledoption[0].equals("on")) {
                                    ledoptions.add(new LEDOPTION(ledoption[0], Integer.parseInt(ledoption[1]), Integer.parseInt(ledoption[2]), ledoption[3], Integer.parseInt(ledoption[4])));
                                } else if (ledoption[0].equals("f") || ledoption[0].equals("off")) {
                                    ledoptions.add(new LEDOPTION(ledoption[0], Integer.parseInt(ledoption[1]), Integer.parseInt(ledoption[2])));
                                } else if (ledoption[0].equals("d") || ledoption[0].equals("delay")) {
                                    ledoptions.add(new LEDOPTION(ledoption[0], Integer.parseInt(ledoption[1])));

                                }
                            }

                        }
                        ledOptionLst.add(new LED(new keyLED(Integer.parseInt(keyled[0]), Integer.parseInt(keyled[1]), Integer.parseInt(keyled[2]), Integer.parseInt(keyled[3])),
                                ledoptions));

                        br.close();

                    } else if (ze.getName().contains("autoplay")) {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(zf.getInputStream(ze)));
                        String line;
                        ArrayList<LEDOPTION> ledoptions = new ArrayList<>();//每个灯光文件内容的集合

                        while ((line = br.readLine()) != null) {
                            if (!line.trim().equals("")) {
                                String autoplay[] = line.split(" ");
                                if (autoplay[0].equals("c") || autoplay[0].equals("chain")) {
                                    autoplays.add(new Autoplay(autoplay[0], Integer.parseInt(autoplay[1])));
                                } else if (autoplay[0].equals("f") || autoplay[0].equals("off")) {
                                    autoplays.add(new Autoplay(autoplay[0], Integer.parseInt(autoplay[1]), Integer.parseInt(autoplay[2]), null));

                                } else if (autoplay[0].equals("d") || autoplay[0].equals("delay")) {

                                    autoplays.add(new Autoplay(autoplay[0], Long.parseLong(autoplay[1])));

                                } else if (autoplay[0].equals("o") || autoplay[0].equals("on")) {
                                    autoplays.add(new Autoplay(autoplay[0], Integer.parseInt(autoplay[1]), Integer.parseInt(autoplay[2])));

                                }
                            }
                        }
                        br.close();
                    }

                }
            }
            zin.closeEntry();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * <p>权限全部申请成功才会回调这个方法，否则回调失败的方法。</p>
     * 日历权限申请成功；使用@PermissionYes(RequestCode)注解。
     *
     * @param grantedPermissions AndPermission回调过来的申请成功的权限。
     */
    @PermissionYes(100)
    private void getCramerYes(@NonNull List<String> grantedPermissions) {
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        //        try {
        //            ZipUtils.upZipFile(new File(filePath), fileTargetPath);
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //初始化音频文件
        initMusic();

    }

    //初始化音乐信息
    private void initMusic() {
        //        lst.clear();
        //        //        lst.addAll(getKeySounds(new File(filePath)));
        //        lst.addAll(getKeySounds(new File(filePath)));
        //初始化（读文件初始化声音和灯光和其他文件）

        Assets2Sd(this,"ppap.zip",filePath);
        try {
            ZipUtils.unzip(filePath,fileTargetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initRes(new File(filePath));
        Log.d("lbk", keyLEDS.toString());
        Log.d("ledOptionLst", ledOptionLst.toString());

        //        将声音池填充-----------------------------------------
        for (int i = 0; i < lst.size(); i++) {
            String musicpath = fileTargetPath + "sounds/" + lst.get(i).getPath();
            soundPool.load(musicpath, 1);
        }


        //       分页 --------------------------------------------------------------------
        //遍历大集合得到页码集合
        for (int i = 0; i < lst.size(); i++) {
            if (!pages.contains(lst.get(i).getPage())) {
                pages.add(lst.get(i).getPage());
            }
        }
        //有几个页码，就动态添加几个页数切换按钮
        final Button[] pageButtons = new Button[pages.size()];
        LinearLayout.LayoutParams pageButtonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < pages.size(); i++) {
            pageButtons[i] = new Button(this);
            pageButtons[i].setTag(pages.get(i));
            pageButtons[i].setLayoutParams(pageButtonParams);
            ll_pages.addView(pageButtons[i]);
            pageButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    page = (int) view.getTag();//点击改变当前页
                }
            });
        }
//        waitingDialog.dismiss();
    }

    /**
     * <p>只要有一个权限申请失败就会回调这个方法，并且不会回调成功的方法。</p>
     * 日历权限申请失败，使用@PermissionNo(RequestCode)注解。
     *
     * @param deniedPermissions AndPermission回调过来的申请失败的权限。
     */
    @PermissionNo(100)
    private void getCramerNo(@NonNull List<String> deniedPermissions) {
        Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onMidiChannelAftertouch(@NonNull final MidiInputDevice sender, int cable, int channel, int pressure) {
        getMidiOutputDevice().sendMidiChannelAftertouch(cable, channel, pressure);
    }

    @Override
    public void onMidiPitchWheel(@NonNull final MidiInputDevice sender, int cable, int channel, int amount) {

        getMidiOutputDevice().sendMidiPitchWheel(cable, channel, amount);
    }

    @Override
    public void onMidiSystemExclusive(@NonNull final MidiInputDevice sender, int cable, final byte[] systemExclusive) {
        getMidiOutputDevice().sendMidiSystemExclusive(cable, systemExclusive);
    }

    @Override
    public void onMidiNoteOff(@NonNull MidiInputDevice sender, final int cable, final int channel, final int note, final int velocity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //                Toast.makeText(ReadFileActivity.this,cable+" "+" "+channel+" "+note+" "+velocity,Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onMidiNoteOn(@NonNull MidiInputDevice sender, final int cable, final int channel, final int note, final int velocity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                                Toast.makeText(ReadFileActivity.this,cable+" "+" "+channel+" "+note+" "+velocity,Toast.LENGTH_LONG).show();
                parseNote(channel, note);
//                                Toast.makeText(ReadFileActivity.this,Integer.parseInt((note+"").substring(0,1))+7+" "+ (note+"").substring(1,1),Toast.LENGTH_LONG).show();

                //                                selectLed(channel, Integer.parseInt((note+"").substring(0,1))+7, Integer.parseInt((note+"").substring(1)));
                //                //点击后去匹配sounds文件
                //                selectMusic(page, finalI + 1, finalJ + 1);
            }
        });
    }

    @Override
    public void onMidiPolyphonicAftertouch(@NonNull MidiInputDevice sender, int cable, int channel, int note, int pressure) {

    }

    @Override
    public void onMidiControlChange(@NonNull MidiInputDevice sender, int cable, int channel, int function, int value) {

    }

    @Override
    public void onMidiProgramChange(@NonNull MidiInputDevice sender, int cable, int channel, int program) {

    }

    @Override
    public void onMidiSystemCommonMessage(@NonNull final MidiInputDevice sender, int cable, final byte[] bytes) {
        getMidiOutputDevice().sendMidiSystemCommonMessage(cable, bytes);
    }

    @Override
    public void onMidiSingleByte(@NonNull final MidiInputDevice sender, int cable, int byte1) {

        getMidiOutputDevice().sendMidiSingleByte(cable, byte1);
    }

    @Override
    public void onMidiTimeCodeQuarterFrame(@NonNull MidiInputDevice sender, int cable, int timing) {
    }

    @Override
    public void onMidiSongSelect(@NonNull MidiInputDevice sender, int cable, int song) {
    }

    @Override
    public void onMidiSongPositionPointer(@NonNull MidiInputDevice sender, int cable, int position) {
    }

    @Override
    public void onMidiTuneRequest(@NonNull MidiInputDevice sender, int cable) {
    }

    @Override
    public void onMidiTimingClock(@NonNull MidiInputDevice sender, int cable) {
    }

    @Override
    public void onMidiStart(@NonNull MidiInputDevice sender, int cable) {
    }

    @Override
    public void onMidiContinue(@NonNull MidiInputDevice sender, int cable) {
    }

    @Override
    public void onMidiStop(@NonNull MidiInputDevice sender, int cable) {
    }

    @Override
    public void onMidiActiveSensing(@NonNull MidiInputDevice sender, int cable) {
    }

    @Override
    public void onMidiReset(@NonNull MidiInputDevice sender, int cable) {
    }

    @Override
    public void onMidiMiscellaneousFunctionCodes(@NonNull final MidiInputDevice sender, int cable, int byte1, int byte2, int byte3) {

        getMidiOutputDevice().sendMidiMiscellaneousFunctionCodes(cable, byte1, byte2, byte3);
    }


    @Override
    public void onMidiCableEvents(@NonNull final MidiInputDevice sender, int cable, int byte1, int byte2, int byte3) {
        getMidiOutputDevice().sendMidiCableEvents(cable, byte1, byte2, byte3);
    }

    @Override
    public void onDeviceAttached(@NonNull UsbDevice usbDevice) {

    }

    @Override
    public void onMidiInputDeviceAttached(@NonNull MidiInputDevice midiInputDevice) {

    }

    @Override
    public void onMidiOutputDeviceAttached(@NonNull MidiOutputDevice midiOutputDevice) {
        midiOutputDevice = getMidiOutputDevice();

        Toast.makeText(this, "USB MIDI Device " + midiOutputDevice.getUsbDevice().getDeviceName() + " 设备连接成功.", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDeviceDetached(@NonNull UsbDevice usbDevice) {

    }

    @Override
    public void onMidiInputDeviceDetached(@NonNull MidiInputDevice midiInputDevice) {

    }

    @Override
    public void onMidiOutputDeviceDetached(@NonNull MidiOutputDevice midiOutputDevice) {
        Toast.makeText(this, "USB MIDI Device " + midiOutputDevice.getUsbDevice().getDeviceName() + " 设备断开连接.", Toast.LENGTH_LONG).show();

    }


    /**
     * 颜色回传值
     */
    public class MyEvaluator implements TypeEvaluator<Integer> {


        @Override
        public Integer evaluate(float v, Integer start, Integer end) {
            return end;

            //            return end;
        }
    }

    public void parseNote(int channel, int note) {
        int x = 9 - Integer.parseInt((note + "").substring(0, 1));
        int y = Integer.parseInt((note + "").substring(1));
//        Toast.makeText(this, x + "," + y, Toast.LENGTH_SHORT).show();
        if (x==8&&y==9){
            if (page>=pages.size()){
                Toast.makeText(this, "该工程最大3页", Toast.LENGTH_SHORT).show();

                return;
            }
            page++;
            Toast.makeText(this, "当前页"+page, Toast.LENGTH_SHORT).show();

        }
        if (x==7&&y==9){
            if (page<=0){
                Toast.makeText(this, "已经为第一页", Toast.LENGTH_SHORT).show();
                return;
            }
            page--;
            Toast.makeText(this, "当前页"+page, Toast.LENGTH_SHORT).show();

        }
        selectLed(channel + 1, x, y);
        //点击后去匹配sounds文件
        selectMusic(channel + 1, x, y);

    }

    /***
     * 调用方式
     *
     * String path = Environment.getExternalStorageDirectory().toString() + "/" + "Tianchaoxiong/useso";
     String modelFilePath = "Model/seeta_fa_v1.1.bin";
     Assets2Sd(this, modelFilePath, path + "/" + modelFilePath);
     *
     * @param context
     * @param fileAssetPath assets中的目录
     * @param fileSdPath 要复制到sd卡中的目录
     */
    public static void Assets2Sd(Context context, String fileAssetPath, String fileSdPath){
        //测试把文件直接复制到sd卡中 fileSdPath完整路径
        File file = new File(fileSdPath);
            try {
                copyBigDataToSD(context, fileAssetPath, fileSdPath);
                Log.d("copy", "************拷贝成功");
            } catch (IOException e) {
                Log.d("copy", "************拷贝失败");
                e.printStackTrace();
            }

    }
    public static void copyBigDataToSD(Context context, String fileAssetPath, String strOutFileName) throws IOException
    {
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(strOutFileName);
        myInput = context.getAssets().open(fileAssetPath);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while(length > 0)
        {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }

        myOutput.flush();
        myInput.close();
        myOutput.close();
    }



}
