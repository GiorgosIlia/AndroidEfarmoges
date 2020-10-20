package gr.uom.loginwithf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;


import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callBack;
    private FirebaseAuth authenti;
    private TextView TviewName;
    private ImageView FatsaMou;
    private LoginButton logButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenti = FirebaseAuth.getInstance();
        FacebookSdk.getApplicationContext();

        TviewName = findViewById(R.id.tName);
        FatsaMou = findViewById(R.id.FatsaM);
        logButton = findViewById(R.id.login_button);

        callBack = CallbackManager.Factory.create();

        logButton.registerCallback(callBack , new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                TviewName.append();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }




}