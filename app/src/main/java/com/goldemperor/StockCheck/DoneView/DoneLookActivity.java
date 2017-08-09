package com.goldemperor.StockCheck.DoneView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.goldemperor.sql.stock_check;
import com.goldemperor.R;
import com.goldemperor.MainActivity.define;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by Nova on 2017/7/19.
 */

public class DoneLookActivity extends AppCompatActivity {

    private ImageView image1;
    private ImageView image2;

    private TextView info;
    private TextView auditor;

    private TextView exceptional;
    private TextView caseclose;

    //图片网络加载设置
    ImageOptions imageOptions;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_look);
        //隐藏标题栏
        getSupportActionBar().hide();
        mContext = this;
        //设置图片加载属性
        imageOptions = new ImageOptions.Builder()
                .setLoadingDrawableId(R.drawable.loading)
                .setFailureDrawableId(R.drawable.loading_failure)
                .setUseMemCache(true)
                .build();

        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        info = (TextView) findViewById(R.id.info);
        auditor = (TextView) findViewById(R.id.auditor);

        exceptional = (TextView) findViewById(R.id.exceptional);
        caseclose = (TextView) findViewById(R.id.caseclose);

        RequestParams params = new RequestParams(define.GetDataById);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("id") != null) {
                params.addQueryStringParameter("id", bundle.getString("id"));
            }
        }

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                //解析result
                if (result != null) {
                    Gson gson = new Gson();
                    stock_check sc = gson.fromJson(result, stock_check.class);
                    x.image().bind(image1,
                            define.endpoint + "/" + sc.getImage1(),
                            imageOptions);
                    x.image().bind(image2,
                            define.endpoint + "/" + sc.getImage2(),
                            imageOptions);
                    if (sc.getInfo() != null) {
                        info.setText("稽查结果:"+sc.getInfo());
                        SpannableStringBuilder builder = new SpannableStringBuilder(info.getText().toString());
                        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                        builder.setSpan(redSpan, 5, info.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        info.setText(builder);
                    }

                    if (sc.getAuditor() != null) {
                        info.setText("稽查人员:" + sc.getAuditor());
                    }

                    if(sc.getExceptional()==null){
                        exceptional.setVisibility(View.GONE);
                        caseclose.setVisibility(View.GONE);
                    }else{
                        exceptional.setText("异常因素:"+sc.getExceptional());

                        caseclose.setText("处理结果:"+sc.getCaseclose());
                        SpannableStringBuilder builder = new SpannableStringBuilder(caseclose.getText().toString());
                        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                        builder.setSpan(redSpan, 5, caseclose.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        caseclose.setText(builder);
                    }
                }
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });

    }
}
