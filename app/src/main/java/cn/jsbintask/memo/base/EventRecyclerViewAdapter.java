package cn.jsbintask.memo.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.jsbintask.memo.Constants;
import cn.jsbintask.memo.R;
import cn.jsbintask.memo.entity.Event;
import cn.jsbintask.memo.ui.activity.EventDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jsbintask.memo.entity.Event;


public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.EventViewHolder> {

    private List<Event> mDatabases;

    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    private boolean mIsDeleteMode = false;

    private List<Integer> mSelectedEventIds = new ArrayList<>();

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setDeleteMode(boolean mIsDeleteMenu) {

        mSelectedEventIds.clear();
        this.mIsDeleteMode = mIsDeleteMenu;

        notifyDataSetChanged();
    }

    public boolean getIsDeleteMode() {
        return mIsDeleteMode;
    }

    public List<Integer> getSelectedEventIds() {
        return mSelectedEventIds;
    }


    public EventRecyclerViewAdapter(Context context) {
        super();
        mContext = context;
    }


    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo_layout, parent, false);
        return new EventViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, int position) {
        final Event event = mDatabases.get(position);

        if (!mIsDeleteMode) {
            if (event.getmIsImportant() == Constants.EventFlag.IMPORTANT) {
                holder.ivMemoIcon.setImageResource(R.drawable.ic_important_event);
                holder.tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                holder.ivMemoIcon.setImageResource(R.drawable.ic_normal_event);
                holder.tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        } else {
            holder.ivMemoIcon.setImageResource(R.drawable.ic_circle);
            if (event.getmIsImportant() == Constants.EventFlag.IMPORTANT) {
                holder.tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                holder.tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
        holder.tvTitle.setText(event.getmTitle());

        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mIsDeleteMode) {
                    if (Constants.MemoIconTag.FIRST == (Integer) holder.ivMemoIcon.getTag()) {
                        holder.ivMemoIcon.setTag(Constants.MemoIconTag.OTHER);

                        holder.ivMemoIcon.setImageResource(R.drawable.ic_selected);
                        mSelectedEventIds.add(event.getmId());
                    } else {
                        mSelectedEventIds.remove(event.getmId());
                        holder.ivMemoIcon.setTag(Constants.MemoIconTag.FIRST);
                        holder.ivMemoIcon.setImageResource(R.drawable.ic_circle);
                    }
                } else if (mOnItemClickListener != null) {

                    mOnItemClickListener.onItemClick(v, holder.getLayoutPosition());
                }
            }
        });


        holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemLongClick(v, holder.getLayoutPosition());
                }
                return false;
            }
        });


        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, EventDetailActivity.class);
                intent.putExtra(EventDetailActivity.EXTRA_IS_EDIT_EVENT, true);
                intent.putExtra(EventDetailActivity.EXTRA_EVENT_DATA, event);
                mContext.startActivity(intent);
            }
        });
    }


    public void setDatabases(List<Event> events) {
        mDatabases = events;
        mIsDeleteMode = false;

        notifyDataSetChanged();
    }

    public List<Event> getDatabases() {
        return mDatabases;
    }

    @Override
    public int getItemCount() {
        return mDatabases == null ? 0 : mDatabases.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_memo)
        ImageView ivMemoIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.iv_edit)
        ImageView ivEdit;

        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivMemoIcon.setTag(Constants.MemoIconTag.FIRST);
        }

        public View getItemView() {
            return this.itemView;
        }
    }
}
