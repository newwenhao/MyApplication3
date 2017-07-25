package com.myapplication.adapter;

import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapplication.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import worldline.com.foldablelayout.FoldableLayout;
public class TestRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @BindView(R.id.imageview_cover)
    protected ImageView mImageViewCover;

    @BindView(R.id.imageview_detail)
    protected ImageView mImageViewDetail;

    @BindView(R.id.textview_cover)
    protected TextView mTextViewCover;

    @BindView(R.id.share_button)
    protected Button mButtonShare;

    private CardView card_view;
    private String[] mDataSet;
    protected FoldableLayout mFoldableLayout;

    private Map<Integer, Boolean> mFoldStates = new HashMap<>();

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    public TestRecyclerViewAdapter(String[] dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType){
            case TYPE_HEADER:
                return new ItemHead(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_big, parent, false));
            case TYPE_CELL:
                return new PhotoViewHolder(new FoldableLayout(parent.getContext()));
            default :
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(position > 0) {
            // Bind data

            // Bind state
            if (mFoldStates.containsKey(position)) {
                if (mFoldStates.get(position) == Boolean.TRUE) {
                    if (!mFoldableLayout.isFolded()) {
                        mFoldableLayout.foldWithoutAnimation();
                    }
                } else if (mFoldStates.get(position) == Boolean.FALSE) {
                    if (mFoldableLayout.isFolded()) {
                        mFoldableLayout.unfoldWithoutAnimation();
                    }
                }
            } else {
                mFoldableLayout.foldWithoutAnimation();
            }


            mFoldableLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mFoldableLayout.isFolded()) {
                        mFoldableLayout.unfoldWithAnimation();
                    } else {
                        mFoldableLayout.foldWithAnimation();
                    }
                }
            });
            mFoldableLayout.setFoldListener(new FoldableLayout.FoldListener() {
                @Override
                public void onUnFoldStart() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mFoldableLayout.setElevation(5);
                    }
                }

                @Override
                public void onUnFoldEnd() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mFoldableLayout.setElevation(0);
                    }
                    mFoldStates.put(holder.getAdapterPosition(), false);
                }

                @Override
                public void onFoldStart() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mFoldableLayout.setElevation(5);
                    }
                }

                @Override
                public void onFoldEnd() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mFoldableLayout.setElevation(0);
                    }
                    mFoldStates.put(holder.getAdapterPosition(), true);
                }
            });
            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("CARDVIEW == ", "大头像");
                }
            });
        } else {
            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("CARDVIEW == ", "大头像");
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mDataSet.length + 1;
    }

    protected class PhotoViewHolder extends RecyclerView.ViewHolder {

        public PhotoViewHolder(FoldableLayout foldableLayout) {
            super(foldableLayout);
            mFoldableLayout = foldableLayout;
            foldableLayout.setupViews(R.layout.list_item_cover, R.layout.list_item_detail, R.dimen.card_cover_height, itemView.getContext());
            ButterKnife.bind(this, foldableLayout);
        }
    }

    private class ItemHead extends RecyclerView.ViewHolder {
        public ItemHead(View viewItem) {
            super(viewItem);
            card_view = (CardView) viewItem.findViewById(R.id.card_view);
        }
    }
}
