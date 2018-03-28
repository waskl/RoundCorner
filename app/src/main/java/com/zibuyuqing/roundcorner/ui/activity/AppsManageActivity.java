package com.zibuyuqing.roundcorner.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.zibuyuqing.roundcorner.R;
import com.zibuyuqing.roundcorner.adapter.AllAppsGridAdapter;
import com.zibuyuqing.roundcorner.base.BaseActivity;
import com.zibuyuqing.roundcorner.model.bean.AppInfo;
import com.zibuyuqing.roundcorner.model.bean.AppInfoWithIcon;
import com.zibuyuqing.roundcorner.model.db.AppInfoLoadTask;
import com.zibuyuqing.roundcorner.utils.Utilities;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xijun.wang on 2017/6/22.
 */

public class AppsManageActivity extends BaseActivity {
    private static final String TAG = "AppsManageActivity";
    private AllAppsGridAdapter mAdapter;
    @BindView(R.id.pb_load_progress)
    ProgressBar mPbLoadProgress;
    @BindView(R.id.rv_app_list)
    RecyclerView mRvAppList;
    private int mCurrentPage = 0;
    @OnClick(R.id.tv_cancel) void cancel(){
        mAdapter.cancel();
        finish();
    }
    @OnClick(R.id.tv_confirm) void confirm(){
        mAdapter.commitChanges();
        finish();
    }
    @Override
    protected int providedLayoutId() {
        return R.layout.activity_apps_manager;
    }
    public static void start(Context context) {
        Intent starter = new Intent(context, AppsManageActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void init() {
        mAdapter = new AllAppsGridAdapter(this);
        mRvAppList.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        mRvAppList.setLayoutManager(gridLayoutManager);
        AppInfoLoadTask.execute(this,mCurrentPage, new AppInfoLoadTask.AppInfoLoadStateListener() {
            @Override
            public void startLoad(int totalCount) {
                Log.e(TAG,"startLoad totalCount =:" + totalCount);
                mPbLoadProgress.setVisibility(View.VISIBLE);
                mPbLoadProgress.setMax(totalCount);
            }

            @Override
            public void onLoad(int process) {
                Log.e(TAG,"onLoad process =:" + process);
                mPbLoadProgress.setProgress(process);
            }

            @Override
            public void endLoad(List<AppInfoWithIcon> appInfoWithIconList) {
                mAdapter.updateData(appInfoWithIconList);
                mRvAppList.setVisibility(View.VISIBLE);
                mPbLoadProgress.setVisibility(View.GONE);
            }

            @Override
            public void onError(String msg) {
                Log.e(TAG,"onError msg =:" + msg);
            }
        });
    }
}
