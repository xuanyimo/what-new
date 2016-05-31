package cn.dubby.what.component;

import android.content.Context;
import android.view.View;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

import cn.dubby.what.application.MyApplication;
import cn.dubby.what.utils.ToastUtils;

/**
 * Created by dubby on 16/5/20.
 */
public class AdapterWithNetwork extends SimpleAdapter {

    public AdapterWithNetwork(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

        super.setViewBinder(new ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                //判断是否为我们要处理的对象
                if (view instanceof CircleNetworkImageView && data instanceof String) {
                    CircleNetworkImageView iv = (CircleNetworkImageView) view;
                    iv.setImageUrl(data.toString(), MyApplication.getImageLoader());
                    return true;
                } else
                    return false;
            }
        });
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
