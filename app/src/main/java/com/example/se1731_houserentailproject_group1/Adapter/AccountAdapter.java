package com.example.se1731_houserentailproject_group1.Adapter;

import android.accounts.Account;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import com.example.se1731_houserentailproject_group1.Model.User;
import com.example.se1731_houserentailproject_group1.R;

import java.util.List;

// Trong AccountAdapter
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

        holder.tvIdValue.setText(String.valueOf(user.getId()));
        holder.tvFullNameValue.setText(user.getFullName());
        holder.tvEmailValue.setText(user.getEmail());
        holder.tvRoleValue.setText(user.getRoles());

        // Kiểm tra role và cập nhật nút Lock/Unlock
        if ("admin".equalsIgnoreCase(user.getRoles())) {
            holder.btnLockUnlock.setVisibility(View.GONE);
        } else {
            holder.btnLockUnlock.setVisibility(View.VISIBLE);
            holder.btnLockUnlock.setText("Lock".equalsIgnoreCase(user.getRoles()) ? "Unlock" : "Lock");
        }

        // Xử lý sự kiện nhấn nút Lock/Unlock
        holder.btnLockUnlock.setOnClickListener(v -> {
            if ("Lock".equalsIgnoreCase(holder.btnLockUnlock.getText().toString())) {
                user.setRoles("Lock");
                holder.btnLockUnlock.setText("Unlock");
            } else {
                user.setRoles("User");
                holder.btnLockUnlock.setText("Lock");
            }
            notifyItemChanged(position);
            // Ghi cập nhật vào database
            new DatabaseHelper(v.getContext()).updateUserRole(user.getId(), user.getRoles());
        });
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdValue, tvFullNameValue, tvEmailValue, tvRoleValue;
        Button btnLockUnlock;

        AccountViewHolder(View itemView) {
            super(itemView);
            tvIdValue = itemView.findViewById(R.id.tvIdValue);
            tvFullNameValue = itemView.findViewById(R.id.tvFullNameValue);
            tvEmailValue = itemView.findViewById(R.id.tvEmailValue);
            tvRoleValue = itemView.findViewById(R.id.tvRoleValue);
            btnLockUnlock = itemView.findViewById(R.id.btnLockUnlock);
        }
    }
}



