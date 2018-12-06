package com.company.ice.sitegrabber.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.ice.sitegrabber.Model.DataAboutShortArticle;
import com.company.ice.sitegrabber.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ice on 14.09.2017.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesHolder>{

    List<DataAboutShortArticle> mDataList = new ArrayList<>();
    private Context context;

    public ArticlesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ArticlesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPES.Footer:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_list, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_articles_list, parent, false);
        }
        return new ArticlesHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticlesHolder holder, int position) {
        if (position == getItemCount()-1){
            return;
        }
        DataAboutShortArticle item = mDataList.get(position);

        holder.articleTitle.setText(item.getTitle());
        holder.shortDescription.setText(item.getShortDescription());
//        holder.amountLike.setText(String.valueOf(item.getAmountLike()));
//        holder.amountDislike.setText(String.valueOf(item.getAmountDislike()));
        holder.amountView.setText(String.valueOf(item.getAmountViews()));
        holder.namePersonAndTime.setText((item.getAuthor() + "  " + item.getTime()));
//        Bitmap b = item.getArticleImageBitmap();
//        Log.d("Main", "Width = " + holder.articleImageView.getWidth());
//        Log.d("Main", "Height = " + holder.articleImageView.getHeight());
//        holder.articleImageView.setImageBitmap(b);
//        holder.articleImageView.setImageBitmap(Bitmap.createScaledBitmap(b, 499,
//                200, true));
        Picasso.with(context).load(item.getUrlImageToDownload().toString())
//        Picasso.with(context).load(item.getUrlImageToDownload().toString())
                //.centerCrop()
                .resize(688, 400)

                .into(holder.articleImageView);
//        Log.d("Main", "myBitmap in adapter = " + mDataList.get(position).getArticleImageBitmap().toString());
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return VIEW_TYPES.Header;
        else if(position == mDataList.size())
            return VIEW_TYPES.Footer;
        else
            return VIEW_TYPES.Normal;
    }

    @Override
    public int getItemCount() {
        if (mDataList.size() > 0) {
            return mDataList.size() + 1;
        } else {
            return 0;
        }
    }

    public void setData(List<DataAboutShortArticle> articles){
        mDataList.clear();
        mDataList.addAll(articles);
        notifyDataSetChanged();
    }

    public void insert(int position, DataAboutShortArticle data) {
        mDataList.add(position, data);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    private class VIEW_TYPES {
        public static final int Header = 1;
        public static final int Normal = 2;
        public static final int Footer = 3;
    }

    static class ArticlesHolder extends RecyclerView.ViewHolder {
        public TextView articleTitle;
        public TextView shortDescription;
//        public TextView amountLike;
//        public TextView amountDislike;
        public TextView amountView;
        public TextView namePersonAndTime;
        public ImageView articleImageView;

        // TODO ADD VIEWS
        public ArticlesHolder(View itemView) {
            super(itemView);
            articleTitle = itemView.findViewById(R.id.tx_article_title);
            shortDescription = itemView.findViewById(R.id.shortDescription);
//            amountLike = itemView.findViewById(R.id.textAmountLike);
//            amountDislike = itemView.findViewById(R.id.textAmountDislike);
            amountView = itemView.findViewById(R.id.textAmountView);
            namePersonAndTime = itemView.findViewById(R.id.textNamePersonAndTime);
            articleImageView = itemView.findViewById(R.id.articleImageView);
        }

    }


}
