package cn.hclee.attextview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hclee.attextview.bean.AtBean;

public class MainActivity extends Activity {

    private EditText inputEt;
    private Button parseBtn;
    private TextView showTv;

    String str = "@jackson 你好!@疯狂的大狸子 @hc_lll how are you？ @你 @哦11111  ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindEvent();
        inputEt.setText(str);
    }

    private void initView() {
        inputEt = (EditText) findViewById(R.id.et_input);
        parseBtn = (Button) findViewById(R.id.btn_parse);
        showTv = (TextView) findViewById(R.id.tv_show);
    }

    private void bindEvent() {
        parseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseString(inputEt.getText().toString());
            }
        });
    }

    private void parseString(String str) {
        List<AtBean> atBeanList = getAtBeanList(str);
        SpannableString spannableStr = getClickSpannableString(str, atBeanList);
        showTv.setText(spannableStr);
        //激活点击事件
        showTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 获取字符串中所有 @用户名 的信息
     * @param str
     * @return
     */
    private List<AtBean> getAtBeanList(String str) {

        List<AtBean> atBeanList = new ArrayList<>();

        String NAME_RULE = "@[a-zA-Z_\u4e00-\u9fa5]{4,30} ";

        // 编译正则表达式
        Pattern pattern = Pattern.compile(NAME_RULE);

        Matcher m = pattern.matcher(str);

        while (m.find()) {
            AtBean bean = new AtBean(m.group(), m.start(), m.end());
            atBeanList.add(bean);
            Log.i("Find AT String", bean.toString());
        }
        return atBeanList;
    }

    private SpannableString getClickSpannableString(String str, List<AtBean> atBeanList) {
        SpannableString spannableStr = new SpannableString(str);
        for (final AtBean atBean : atBeanList) {
            spannableStr.setSpan(new Clickable(MainActivity.this,new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //每个 @用户名 字符串的点击事件
                    Toast.makeText(MainActivity.this, "点击了 ————> " + atBean.getName(), Toast.LENGTH_SHORT).show();
                }
            }), atBean.getStartPos(), atBean.getEndPos(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableStr;
    }


    private class Clickable extends ClickableSpan implements View.OnClickListener {
        private View.OnClickListener mListener;
        private Context context;

        private Clickable(Context context,View.OnClickListener mListener) {
            this.context = context;
            this.mListener = mListener;
        }

        //设置显示样式
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(R.color.colorPrimary));//设置颜色
            ds.setUnderlineText(false);//设置下划线
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}
