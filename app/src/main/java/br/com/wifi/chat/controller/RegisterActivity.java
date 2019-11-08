package br.com.wifi.chat.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import br.com.wifi.chat.R;
import br.com.wifi.chat.model.User;
import br.com.wifi.chat.module.AppModule;
import br.com.wifi.chat.module.UserModule;
import br.com.wifi.chat.utils.DialogUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class RegisterActivity extends AppCompatActivity {

    private Disposable userDisposable;
    private UserModule userModule;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText nomeEditText = findViewById(R.id.username);

        final Button loginButton = findViewById(R.id.login);

        userModule = AppModule.getInstance().userModule;

        loginButton.setOnClickListener(v -> {
            if (nomeEditText.getTextSize() == 0) {
                nomeEditText.setError("Informe seu nick name");
            } else {
                User user = new User(nomeEditText.getText().toString());
                DialogUtils.showLoading(this, "Registrando...");
                userModule.addUser(user)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user1 -> {
                    DialogUtils.hideLoading();
                    Log.d(RegisterActivity.class.getName(), user1.toString());
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userDisposable = userModule.loggedUser
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((user) -> {
                    if (user.isPresent()) {
                        Intent discoverIntent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(discoverIntent);
                        finish();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        userDisposable.dispose();
    }
}
