package cn.dubby.what.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.dubby.what.R;
import cn.dubby.what.application.MyApplication;
import cn.dubby.what.domain.circle.Circle;
import cn.dubby.what.utils.MessagesContainer;

/**
 * Created by dubby on 16/5/14.
 */
public class MainContentRecyclerAdapter extends RecyclerView.Adapter<MainContentRecyclerAdapter.ViewHolder> {

    private List<Circle> mDatas;
    private LayoutInflater mInflater;
    private View.OnClickListener onItemClickListener;


    public MainContentRecyclerAdapter(Context context, List<Circle> data) {
        mDatas = data;
        mInflater = LayoutInflater.from(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView idTv;
        CircleNetworkImageView logo;
        TextView description;

    }

    public void setOnItemClickListener(View.OnClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.main_content_recycler_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.logo = (CircleNetworkImageView) view.findViewById(R.id.logo);
        viewHolder.description = (TextView) view.findViewById(R.id.description);
        viewHolder.idTv = (TextView) view.findViewById(R.id.idTv);

        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.logo.setImageUrl(mDatas.get(i).logo, MyApplication.getImageLoader());
        viewHolder.description.setText(mDatas.get(i).description);
        viewHolder.idTv.setText(mDatas.get(i).serverId + "");
        if (onItemClickListener != null) {
            viewHolder.description.setOnClickListener(onItemClickListener);
        }
    }

}