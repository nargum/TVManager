package com.example.kuba.tvmanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuba.tvmanager.Account;
import com.example.kuba.tvmanager.Mappers.AccountMapper;
import com.example.kuba.tvmanager.R;
import com.example.kuba.tvmanager.TabActivity;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText nameTxt;
    private EditText passwordTxt;
    private Button loginBtn;
    private Button createBtn;
    private TextView warningTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameTxt = (EditText)findViewById(R.id.nameTxt);
        passwordTxt = (EditText)findViewById(R.id.passwordTxt);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        createBtn = (Button)findViewById(R.id.createBtn);
        warningTxt = (TextView)findViewById(R.id.warningTxt);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                validate();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Intent intent = new Intent(this, CreateAccountActivity.class);
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validate(){
        boolean ok = false;
        ArrayList<Account> accounts = AccountMapper.getAccounts(getApplicationContext());
        for(int i = 0; i < accounts.size(); i++){
            String name = String.valueOf(nameTxt.getText());
            String password = String.valueOf(passwordTxt.getText().toString());
            if(name.equals(accounts.get(i).getLogin()) && password.equals(accounts.get(i).getPassword())){
                ok = true;
                //break;
            }
        }

        if(ok){
            //warningTxt.setText("you have been logged in");
            nameTxt.setText("");
            passwordTxt.setText("");
            //Intent intent = new Intent(LoginActivity.this, ShowListActivity.class);
            Intent intent = new Intent(LoginActivity.this, TabActivity.class);
            startActivity(intent);
        } else{
            Toast.makeText(this, "Wrong name or password", Toast.LENGTH_SHORT).show();
            nameTxt.setText("");
            passwordTxt.setText("");
        }

        /*if(nameTxt.getText().toString().equals("admin") && passwordTxt.getText().toString().equals("1234")) {
            warningTxt.setText("you have been logged in");
            nameTxt.setText("");
            passwordTxt.setText("");
        }else{
            warningTxt.setText("wrong name or password");
            nameTxt.setText("");
            passwordTxt.setText("");
        }*/
    }
}
