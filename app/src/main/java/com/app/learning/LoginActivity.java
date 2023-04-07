package com.app.learning;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.learning.data.DatabaseClient;
import com.app.learning.data.dao.UserDao;
import com.app.learning.data.entities.UserData;
import com.app.learning.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });

        binding.btnLogin.setOnClickListener(v -> {
            loginUser();
        });
    }

    private void loginUser() {
        UserDao userDao = DatabaseClient.getInstance(this).getAppDatabase().userDao();
        UserData userData = userDao.getUserByEmailAndPassword(binding.tvEmail.getText().toString(),
                binding.tvPassword.getText().toString());
        if (userData != null) {
            Toast.makeText(this, "Successfully logged in as " + userData.getRole(), Toast.LENGTH_SHORT).show();
            Intent intent = userData.getRole().equals(Constants.INSTRUCTOR) ? new Intent(this, AddScheduleActivity.class) : new Intent(this, BookScheduleActivity.class);

            intent.putExtra(Constants.USER_ID, userData.getId());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
        }
    }
}