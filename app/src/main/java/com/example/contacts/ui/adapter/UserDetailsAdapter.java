package com.example.contacts.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.contacts.model.USerDetailsPojo;
import com.example.contacts.R;

import java.util.List;

public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.UserDetailsHolder> {

    String TAG = "Adapter";
    private final LayoutInflater mInflater;
    private List<USerDetailsPojo> userList;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    public UserDetailsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void updateList(List<USerDetailsPojo> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new UserDetailsHolder(mInflater.inflate(R.layout.layout_recyclerview, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_loading, parent, false));
            default:
                return null;
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == userList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailsHolder holder, int position) {

        USerDetailsPojo user = userList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(user.getAvatar())
                .into(holder.ivAvatar);
        holder.tvName.setText(user.getFirst_name() + " " + user.getLast_name());
        holder.tvEmail.setText(user.getEmail());

    }

    @Override
    public int getItemCount() {
        if (userList != null) {
            return userList.size();
        }
        return 0;
    }

    class UserDetailsHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatar;
        TextView tvName;
        TextView tvEmail;

        public UserDetailsHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_email);

        }
    }
    public class ProgressHolder extends UserDetailsHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
