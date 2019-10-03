package com.zero.music;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * APP 首页
 */
public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {

    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (EasyPermissions.hasPermissions(this, permissions)) {
            //有权限
            //Log.e("EasyPermissions", "已授权");
        } else {
            //申请权限
            EasyPermissions.requestPermissions(this, "需要获取手机卡读写权限", 1000, permissions);
        }
    }

    public void playMusic(View view) {
        startActivity(new Intent(this, MusicPlayActivity.class));
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //EasyPermissions 权限回调  授权成功 执行该方法
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //EasyPermissions 权限回调  授权失败 执行该方法
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.e("EasyPermissions", "dialog提醒点击“确定”");
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Log.e("EasyPermissions", "dialog提醒点击“取消”");
    }
}
