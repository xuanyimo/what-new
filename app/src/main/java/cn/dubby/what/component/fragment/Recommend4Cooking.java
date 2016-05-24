package cn.dubby.what.component.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.dubby.what.R;

/**
 * Created by dubby on 16/5/24.
 */
public class Recommend4Cooking  extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recommend_cooking, container, false);

        return rootView;
    }
}
