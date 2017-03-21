package com.zysm.curtain.uidemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zysm.curtain.mwidgetgroup.alerter.Alerter;
import com.zysm.curtain.mwidgetgroup.alerter.OnHideAlertListener;
import com.zysm.curtain.mwidgetgroup.alerter.OnShowAlertListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlerterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnAlertDefault)
    AppCompatButton btnAlertDefault;
    @BindView(R.id.btnAlertColoured)
    AppCompatButton btnAlertColoured;
    @BindView(R.id.btnAlertCustomIcon)
    AppCompatButton btnAlertCustomIcon;
    @BindView(R.id.btnAlertTextOnly)
    AppCompatButton btnAlertTextOnly;
    @BindView(R.id.btnAlertOnClick)
    AppCompatButton btnAlertOnClick;
    @BindView(R.id.btnAlertVerbose)
    AppCompatButton btnAlertVerbose;
    @BindView(R.id.btnAlertCallback)
    AppCompatButton btnAlertCallback;
    @BindView(R.id.btnAlertInfiniteDuration)
    AppCompatButton btnAlertInfiniteDuration;
    @BindView(R.id.content_example)
    LinearLayout contentExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerter);
        ButterKnife.bind(this);
    }

    @OnClick({ R.id.btnAlertColoured,
            R.id.btnAlertCustomIcon,
            R.id.btnAlertTextOnly,
            R.id.btnAlertOnClick,
            R.id.btnAlertVerbose,
            R.id.btnAlertCallback,
            R.id.btnAlertInfiniteDuration})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAlertColoured: {
                showAlertColoured();
                break;
            }
            case R.id.btnAlertCustomIcon: {
                showAlertWithIcon();
                break;
            }
            case R.id.btnAlertTextOnly: {
                showAlertTextOnly();
                break;
            }
            case R.id.btnAlertOnClick: {
                showAlertWithOnClick();
                break;
            }
            case R.id.btnAlertVerbose: {
                showAlertVerbose();
                break;
            }
            case R.id.btnAlertCallback: {
                showAlertCallbacks();
                break;
            }
            case R.id.btnAlertInfiniteDuration: {
                showAlertInfiniteDuration();
            }
            default: {
                showAlertDefault();
            }
        }
    }

    private void showAlertDefault() {
        Alerter.create(AlerterActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .enableInfiniteDuration(true)
                .show();
    }

    private void showAlertColoured() {
        Alerter.create(AlerterActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setBackgroundColor(R.color.colorAccent)
                .show();
    }

    private void showAlertWithIcon() {
        Alerter.create(AlerterActivity.this)
                .setText("Alert text...")
                .setIcon(R.drawable.alerter_ic_face)
                .show();
    }

    private void showAlertTextOnly() {
        Alerter.create(AlerterActivity.this)
                .setText("Alert text...")
                .show();
    }

    private void showAlertWithOnClick() {
        Alerter.create(AlerterActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setDuration(10000)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(AlerterActivity.this, "OnClick Called", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void showAlertVerbose() {
        Alerter.create(AlerterActivity.this)
                .setTitle("Alert Title")
                .setText("The alert scales to accommodate larger bodies of text. " +
                        "The alert scales to accommodate larger bodies of text. " +
                        "The alert scales to accommodate larger bodies of text.")
                .show();
    }

    private void showAlertCallbacks(){
        Alerter.create(AlerterActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setDuration(10000)
                .setOnShowListener(new OnShowAlertListener() {
                    @Override
                    public void onShow() {
                        Toast.makeText(AlerterActivity.this, "Show Alert", Toast.LENGTH_LONG).show();
                    }
                })
                .setOnHideListener(new OnHideAlertListener() {
                    @Override
                    public void onHide() {
                        Toast.makeText(AlerterActivity.this, "Hide Alert", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void showAlertInfiniteDuration() {
        Alerter.create(AlerterActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .enableInfiniteDuration(true)
                .show();
    }
}
