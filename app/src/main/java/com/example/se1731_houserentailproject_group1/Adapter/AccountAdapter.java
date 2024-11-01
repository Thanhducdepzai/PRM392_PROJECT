package com.example.se1731_houserentailproject_group1.Adapter;

import android.accounts.Account;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1731_houserentailproject_group1.Model.User;
import com.example.se1731_houserentailproject_group1.R;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private List<User> accountList;

    public AccountAdapter(List<User> accountList) {
        this.accountList = accountList;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        User user = accountList.get(position);
        holder.tvId.setText(String.valueOf(user.getId()));
        holder.tvFullName.setText(user.getFullName());
        holder.tvEmail.setText(user.getEmail());
        holder.tvRole.setText(user.getRoles()); // Cập nhật cột role
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvFullName, tvEmail, tvRole;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvIdValue);
            tvFullName = itemView.findViewById(R.id.tvFullNameValue);
            tvEmail = itemView.findViewById(R.id.tvEmailValue);
            tvRole = itemView.findViewById(R.id.tvRoleValue); // Khai báo TextView cho role
        }
    }
}


