package android.eq366pt.zxtnetwork.com.yg;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.allenliu.versionchecklib.core.http.HttpParams;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;

import ezy.boost.update.ICheckAgent;
import ezy.boost.update.IUpdateChecker;
import ezy.boost.update.IUpdateParser;
import ezy.boost.update.UpdateInfo;
import ezy.boost.update.UpdateManager;

/**
 * Created by lbk on 2018/2/8.
 */

public class DownLoadActivity extends Activity{
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        button=findViewById(R.id.btn_check_update);
       final HttpParams httpParams= new HttpParams();
        httpParams.put("flag","1");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllenVersionChecker
                        .getInstance()
                        .downloadOnly(
                                UIData.create().setDownloadUrl("http://s.caihuimall.net:81/message/version/get?flag=1")
                        )
                        .excuteMission(DownLoadActivity.this);

//                AllenVersionChecker
//                        .getInstance()
//                        .requestVersion()
//                        .setRequestUrl("http://s.caihuimall.net:81/message/version/get?flag=1")
//                        .request(new RequestVersionListener() {
//                            @Nullable
//                            @Override
//                            public UIData onRequestVersionSuccess(String result) {
//                                //拿到服务器返回的数据，解析，拿到downloadUrl和一些其他的UI数据
//                                UIData uiData = UIData
//                                        .create();
//                                Log.d("d",result);
//                                try {
//                                    JSONObject jsonObject=new JSONObject(result);
//                                    uiData
//                                            .setDownloadUrl(jsonObject.getString("url"))
//                                            .setTitle("在e企更新")
//                                            .setContent(jsonObject.getString("description"));
//                                    //放一些其他的UI参数，拿到后面自定义界面使用
////                                    uiData.getVersionBundle().putString("key", "your value");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                                return uiData;
//
//                            }
//
//                            @Override
//                            public void onRequestVersionFailure(String message) {
//
//                            }
//                        })
//                        .excuteMission(DownLoadActivity.this);


//                UpdateManager.create(DownLoadActivity.this).setUrl("http://s.caihuimall.net:81/message/version/get?flag=1").check();
//                check(true, true, false, false, false, 998);
            }
        });
    }

    void check(boolean isManual, final boolean hasUpdate, final boolean isForce, final boolean isSilent, final boolean isIgnorable, final int
            notifyId) {
        UpdateManager.create(this).setChecker(new IUpdateChecker() {
            @Override
            public void check(ICheckAgent agent, String url) {
                Log.e("ezy.update", "checking");
                agent.setInfo("");
            }
        }).setUrl("http://s.caihuimall.net:81/message/version/get?flag=1").setManual(isManual).setNotifyId(notifyId).setParser(new IUpdateParser() {
            @Override
            public UpdateInfo parse(String source) throws Exception {
                UpdateInfo info = new UpdateInfo();
                info.hasUpdate = hasUpdate;
                info.updateContent = "• 支持文字、贴纸、背景音乐，尽情展现欢乐气氛；\n• 两人视频通话支持实时滤镜，丰富滤镜，多彩心情；\n• 图片编辑新增艺术滤镜，一键打造文艺画风；\n• 资料卡新增点赞排行榜，看好友里谁是魅力之王。";
                info.versionCode = 587;
                info.versionName = "v5.8.7";
                info.url = "";
                info.md5 = "56cf48f10e4cf6043fbf53bbbc4009e3";
                info.size = 10149314;
                info.isForce = isForce;
                info.isIgnorable = isIgnorable;
                info.isSilent = isSilent;
                return info;
            }
        }).check();
    }

}
