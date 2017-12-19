package com.example.kuba.tvmanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kuba.tvmanager.Account;
import com.example.kuba.tvmanager.Mappers.AccountMapper;
import com.example.kuba.tvmanager.R;


import java.util.ArrayList;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText nameTxt;
    private EditText surnameTxt;
    private EditText emailTxt;
    private EditText loginTxt;
    private EditText passwordTxt;
    private EditText password2Txt;
    private TextView messageTxt;
    private Button createBtn;
    private Button delBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        nameTxt = (EditText)findViewById(R.id.nameTxt);
        surnameTxt = (EditText)findViewById(R.id.surnameTxt);
        emailTxt = (EditText)findViewById(R.id.emailTxt);
        loginTxt = (EditText)findViewById(R.id.loginTxt);
        passwordTxt = (EditText)findViewById(R.id.passwordTxt);
        password2Txt = (EditText)findViewById(R.id.password2Txt);
        messageTxt = (TextView)findViewById(R.id.messageTxt);
        createBtn = (Button)findViewById(R.id.createBtn);
        delBtn = (Button)findViewById(R.id.delBtn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(checkInformationInserted()){
                    test();
                    nameTxt.setText("");
                    surnameTxt.setText("");
                    emailTxt.setText("");
                    loginTxt.setText("");
                    password2Txt.setText("");
                    passwordTxt.setText("");
                    messageTxt.setText("");
                    Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
               // test();

            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AccountMapper.delete(getApplicationContext());

            }
        });
    }

    private boolean checkInformationInserted(){
        boolean ok = true;
        if(nameTxt.getText().toString().equals("")){
            messageTxt.setText("Name missing");
            ok = false;
        }

        if(surnameTxt.getText().toString().equals("")){
            messageTxt.setText("Surname is missing");
            ok = false;
        }

        if(emailTxt.getText().toString().equals("")){
            messageTxt.setText("Email is missing");
            ok = false;
        }

        if(loginTxt.getText().toString().equals("")){
            messageTxt.setText("Login name is missing");
            ok = false;
        }

        if(passwordTxt.getText().toString().equals("") || password2Txt.getText().toString().equals("")){
            messageTxt.setText("Password is missing");
            ok = false;
        }

        return ok;
    }

    /*private boolean createAccount(){

    }*/

    private void test(){

        ArrayList<Account> accounts = AccountMapper.getAccounts(this);
        Account account = new Account(String.valueOf(accounts.size() + 1), String.valueOf(nameTxt.getText()), String.valueOf(surnameTxt.getText()),
                String.valueOf(emailTxt.getText()), String.valueOf(loginTxt.getText()), String.valueOf(passwordTxt.getText()));
        AccountMapper.add(this, account);
        //messageTxt.setText(accounts.get(1).getLogin() + " " + accounts.get(1).getPassword());
    }
}
