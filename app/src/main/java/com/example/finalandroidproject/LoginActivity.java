package com.example.finalandroidproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity {


        private static final int RC_SIGN_IN = 123;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                // already signed in
                FirebaseUser user = auth.getCurrentUser();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("user", user);
                startActivityForResult(intent, 61);
            } else {
                // not signed in
                createSignInIntent();
            }

        }

        public void createSignInIntent() {
            // [START auth_fui_create_intent]
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            // You must provide a custom layout XML resource and configure at least one
            // provider button ID. It's important that that you set the button ID for every provider
            // that you have enabled.
            AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                    .Builder(R.layout.custom_auth_layout)
                    .setGoogleButtonId(R.id.google_button)
                    .setEmailButtonId(R.id.email_button)
                    .build();

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setAuthMethodPickerLayout(customLayout)
                            .build(),
                    RC_SIGN_IN);
            // [END auth_fui_create_intent]

        }

        // [START auth_fui_result]z
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                IdpResponse response = IdpResponse.fromResultIntent(data);

                if (resultCode == RESULT_OK) {
                    // Successfully signed in
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("user", user);
                    startActivityForResult(intent, 61);
                    // ...
                } else {
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    // ...
                }
            } else if (requestCode == 61) {
                createSignInIntent();
            }
        }
        // [END auth_fui_result]



        public void signOut() {
            // [START auth_fui_signout]
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
            // [END auth_fui_signout]
        }

        public void delete() {
            // [START auth_fui_delete]
            AuthUI.getInstance()
                    .delete(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
            // [END auth_fui_delete]
        }

        public void themeAndLogo() {
            List<AuthUI.IdpConfig> providers = Collections.emptyList();

            // [START auth_fui_theme_logo]
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.fui_ic_twitter_bird_white_24dp)      // Set logo drawable
                            .setTheme(R.style.AppTheme)      // Set theme
                            .build(),
                    RC_SIGN_IN);
            // [END auth_fui_theme_logo]
        }

        public void privacyAndTerms() {
            List<AuthUI.IdpConfig> providers = Collections.emptyList();
            // [START auth_fui_pp_tos]
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setTosAndPrivacyPolicyUrls(
                                    "https://example.com/terms.html",
                                    "https://example.com/privacy.html")
                            .build(),
                    RC_SIGN_IN);
            // [END auth_fui_pp_tos]
        }

    }



