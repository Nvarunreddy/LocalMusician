package com.app.learning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.app.learning.data.DatabaseClient;
import com.app.learning.data.dao.UserDao;
import com.app.learning.data.entities.UserData;
import com.app.learning.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSignup.setOnClickListener(v -> {
            signupUser();
        });

        binding.tvSignIn.setOnClickListener(v->{
            finish();
        });
    }

    private void signupUser() {
        UserDao userDao = DatabaseClient.getInstance(this).getAppDatabase().userDao();

        if(userDao.getUserDataByEmail(binding.tvEmail.getText().toString()) == null) {

            UserData userData = new UserData(binding.tvEmail.getText().toString(), binding.tvPassword.getText().toString(), binding.roleCheckbox.isChecked() ? Constants.INSTRUCTOR : Constants.LEARNER);
            userDao.insertUser(userData);

            Toast.makeText(this, "User created Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Email/UserName  already exists", Toast.LENGTH_SHORT).show();
        }
    }
}