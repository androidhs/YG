package android.eq366pt.zxtnetwork.com.yg;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by lbk on 2018/2/7.
 */

public class TestAnimation extends Activity {
    Button btn_start;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testanimation);
        btn_start = findViewById(R.id.btn_start);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn1.setBackgroundColor(Color.parseColor("#bec6d1"));
        btn2.setBackgroundColor(Color.parseColor("#bec6d1"));
        btn3.setBackgroundColor(Color.parseColor("#bec6d1"));
        btn4.setBackgroundColor(Color.parseColor("#bec6d1"));
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                ObjectAnimator objectAnimator1 = ObjectAnimator.ofInt(btn1, "backgroundColor", Color.parseColor("#81eec4"));
                //                ObjectAnimator objectAnimator2 = ObjectAnimator.ofInt(btn2, "backgroundColor", Color.parseColor("#81eec4"));
                //                ObjectAnimator objectAnimator3 = ObjectAnimator.ofInt(btn3, "backgroundColor", Color.parseColor("#81eec4"));
                //                ObjectAnimator objectAnimator4 = ObjectAnimator.ofInt(btn4, "backgroundColor", Color.parseColor("#81eec4"));

                ObjectAnimator objectAnimator1 = new ObjectAnimator();
                ObjectAnimator objectAnimator2 = new ObjectAnimator();
                ObjectAnimator objectAnimator3 = new ObjectAnimator();
                ObjectAnimator objectAnimator4 = new ObjectAnimator();

                objectAnimator1.setTarget(btn1);
                objectAnimator2.setTarget(btn2);
                objectAnimator3.setTarget(btn3);
                objectAnimator4.setTarget(btn4);

                objectAnimator1.setIntValues(Color.parseColor("#bec6d1"),Color.parseColor("#81eec4"));
                objectAnimator2.setIntValues(Color.parseColor("#bec6d1"),Color.parseColor("#81eec4"));
                objectAnimator3.setIntValues(Color.parseColor("#bec6d1"),Color.parseColor("#81eec4"));
                objectAnimator4.setIntValues(Color.parseColor("#bec6d1"),Color.parseColor("#81eec4"));

                objectAnimator1.setPropertyName("backgroundColor");
                objectAnimator2.setPropertyName("backgroundColor");
                objectAnimator3.setPropertyName("backgroundColor");
                objectAnimator4.setPropertyName("backgroundColor");


                objectAnimator2.setStartDelay(300);
                objectAnimator3.setStartDelay(600);
                objectAnimator4.setStartDelay(900);
                objectAnimator1.setEvaluator(new MyEvaluator());
                objectAnimator2.setEvaluator(new MyEvaluator());
                objectAnimator3.setEvaluator(new MyEvaluator());
                objectAnimator4.setEvaluator(new MyEvaluator());

                objectAnimator1.start();
                objectAnimator2.start();
                objectAnimator3.start();
                objectAnimator4.start();


            }
        });

    }

    public class MyEvaluator implements TypeEvaluator<Integer> {


        @Override
        public Integer evaluate(float v, Integer integer, Integer t1) {
            return t1;
        }
    }


}
