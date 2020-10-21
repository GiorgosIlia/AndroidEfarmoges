package gr.uom.loginwithf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.WebDialog;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callBack;
    private FirebaseAuth authenti;
    private TextView TviewName;
    private ImageView FatsaMou;
    private LoginButton logButton;
    private TextView Url;
    private FirebaseAuth.AuthStateListener authListener;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG ="FacebookAuthentication";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenti = FirebaseAuth.getInstance();
        FacebookSdk.getApplicationContext();

        Url = findViewById(R.id.textView2);
        TviewName = findViewById(R.id.tName);
        FatsaMou = findViewById(R.id.FatsaM);
        logButton = findViewById(R.id.login_button);
        logButton.setReadPermissions("email" , "public_profile");

        callBack = CallbackManager.Factory.create();

        logButton.registerCallback(callBack , new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError" + error );

            }
        });

        authListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    UpdateUI(user);
                }
                else{
                    UpdateUI(null);
                }
            }
        };
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    authenti.signOut();
                }
            }
        };

    }


    private void handleFacebookToken(AccessToken token){
            Log.d(TAG , "HandleFacebookToken" + token);
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            authenti.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "Sign in with credential: Succesful" );
                        FirebaseUser user = authenti.getCurrentUser();
                        UpdateUI(user);
                    }
                    else {
                        Log.d(TAG, "Sign in with credential: Failed to login" + task.getException() );
                        Toast.makeText(MainActivity.this , "Authentication Failed", Toast.LENGTH_SHORT );
                        UpdateUI(null);
                    }
                }
            });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callBack.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void UpdateUI (FirebaseUser user){
        if(user != null ){
            TviewName.setText(user.getDisplayName());
            if (user.getPhotoUrl() != null){
                String photoUrl = user.getPhotoUrl().toString();
                Url.setText(photoUrl);
                photoUrl = photoUrl + "?type=large";
                Picasso.get().load(photoUrl).into(FatsaMou);
            }
            else {
                TviewName.setText("");
                FatsaMou.setImageResource(R.drawable.com_facebook_tooltip_black_xout);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        authenti.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authListener != null){
            authenti.removeAuthStateListener(authListener);
        }
    }
}