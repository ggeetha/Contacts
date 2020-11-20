package com.example.contacts.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.contacts.App;
import com.example.contacts.model.USerDetailsPojo;
import com.example.contacts.R;
import com.example.contacts.ui.adapter.PaginationListener;
import com.example.contacts.ui.adapter.UserDetailsAdapter;
import com.example.contacts.ui.viewmodel.UserViewModel;
import com.example.contacts.ui.viewmodel.factory.UserViewModelFactory;
import com.example.contacts.utils.ConfigConstants;
import com.example.contacts.vo.Status;

import javax.inject.Inject;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";
    RecyclerView recyclerView;
    UserDetailsAdapter adapter;
    @Inject
    UserViewModelFactory userViewModelFactory;
    UserViewModel userViewModel;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getApplicationComponent().inject(this);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new UserDetailsAdapter(this);
        init();
        observeFetchUsers();
        userViewModel.fetchUser();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init(){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                Log.e(TAG, "loadMoreItems: " );
                if(ConfigConstants.PAGE_NUMBER < 2) {
                    ConfigConstants.PAGE_NUMBER = ConfigConstants.PAGE_NUMBER+1;
                    observeFetchUsers();
                    userViewModel.fetchUser();
                    isLoading = false;
                }

            }

            @Override
            public int getTotalPageCount() {
                return 2;
            }

            @Override
            public boolean isLastPage() {
                if(ConfigConstants.PAGE_NUMBER == 2) isLastPage = true;
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }


    private void observeFetchUsers() {
        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel.class);
        userViewModel.getData().observe(this, response -> {
            if (response == null) {
                return;
            }
            String message = response.message;
            if (message != null && response.status.equals(Status.ERROR)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else {
                List<USerDetailsPojo> users = response.data;
                adapter.updateList(users);
                recyclerView.post(new Runnable() {
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

}
