package com.example.se1731_houserentailproject_group1.AdminActivity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1731_houserentailproject_group1.Adapter.AccountAdapter;
import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import com.example.se1731_houserentailproject_group1.Model.User;
import com.example.se1731_houserentailproject_group1.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AccountAdapter accountAdapter;
    private List<User> accountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account_management);

        recyclerView = findViewById(R.id.recyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load accounts from the database
        loadAccounts();
    }

    private void loadAccounts() {
        // Use ExecutorService for background task
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            accountList = databaseHelper.getAllAccounts();

            runOnUiThread(() -> {
                if (accountList != null && !accountList.isEmpty()) {
                    accountAdapter = new AccountAdapter(accountList);
                    recyclerView.setAdapter(accountAdapter);
                } else {
                    Toast.makeText(AccountManagementActivity.this, "No accounts found", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
