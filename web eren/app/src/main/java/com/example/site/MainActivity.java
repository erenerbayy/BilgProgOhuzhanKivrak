package com.example.site;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String url;
    WebView mywebview;
    boolean connected= false;
    private ProgressBar mProgressBar;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url= getResources() .getString(R.string.url);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();

        ProgressDialog progressDialog= ProgressDialog.show(this,getResources() .getString(R.string.app_name ), getResources().getString(R.string.yükleniyor),true);
        frameLayout= findViewById(R.id.frameLayout);
        mProgressBar= findViewById(R.id.progress_horizontal);
        mProgressBar.setMax(100);
        mywebview= findViewById(R.id.vebView);
        mywebview.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView webView, int progress){
                frameLayout.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(progress);
                setTitle("yükleniyor");
                if (progress== 100){
                    frameLayout.setVisibility(View.GONE);
                    setTitle(webView.getTitle());
                }
                super.onProgressChanged(webView,progress);
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI).getState()==NetworkInfo.State.CONNECTED ){
            connected=true;
        }
        else{
            connected=false;
        }
        if (connected){
            mywebview.setWebViewClient(new WebViewClient());
            mywebview.loadUrl(url);
            mProgressBar.setProgress(0);
            progressDialog.show();
            mywebview.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap bitmap){
                    super.onPageStarted(view, url, bitmap);
                }
                @Override
                public void onPageFinished(WebView webView,String url){
                    super.onPageFinished(webView,url);
                    progressDialog.dismiss();
                }
                @Override
                public void onLoadResource(WebView webView,String url){
                    super.onLoadResource(webView,url);
                }
                @Override
                public boolean shouldOverrideUrlLoading(WebView webView,String url){
                frameLayout.setVisibility(View.VISIBLE);
                mywebview.loadUrl(url);
                return true;
                }
            });
            if (connected){
                Toast.makeText(getApplicationContext(),"internet bağlantınızı kontrol ediniz...",Toast.LENGTH_LONG).show();
            }
        }
    }
}