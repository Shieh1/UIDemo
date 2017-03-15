package com.zysm.curtain.uidemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zysm.curtain.mwidgetgroup.util.ToActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.m_refresh_list_view_button)
    Button mRefreshListViewButton;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick({R.id.m_refresh_list_view_button})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_refresh_list_view_button:
                ToActivityUtil.toNextActivity(mContext, MRefreshListViewActivity.class);
                break;
        }
    }
}
