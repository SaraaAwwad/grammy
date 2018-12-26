package com.example.sara.grammy.Login;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sara.grammy.R;
import com.example.sara.grammy.Utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends Activity {


    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private String email, username, password;
    private EditText mEmail, mPassword, mUsername;
    private TextView loadingPleaseWait;
    private Button btnRegister;
    private ProgressBar mProgressBar;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private String append="";

    private static final int GET_FROM_GALLERY = 3;
    private static final int REQUEST_CODE = 1;
    Button upload_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        Log.d(TAG, "onCreate: started.");
        mContext = RegisterActivity.this;
        firebaseMethods = new FirebaseMethods(mContext);

        mProgressBar = (ProgressBar) findViewById(R.id.registerProgressBar);
        loadingPleaseWait = (TextView) findViewById(R.id.waitRegister);
        mProgressBar.setVisibility(View.GONE);
        loadingPleaseWait.setVisibility(View.INVISIBLE);

        initWidgets();
        setupFirebaseAuth();
        init();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(android.R.id.content).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

    }


    private void init(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString();
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();

                if(checkInputs(email, username, password)){
                    mProgressBar.setVisibility(View.VISIBLE);
                    loadingPleaseWait.setVisibility(View.VISIBLE);

                    firebaseMethods.registerNewEmail(email, password, username);
                }
            }
        });
    }

    private boolean checkInputs(String email, String username, String password){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(email.equals("") || username.equals("") || password.equals("")){
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initWidgets() {
        Log.d(TAG, "initWidgets: Initializing Widgets.");
        mEmail = (EditText) findViewById(R.id.edtEmail);

        mPassword = (EditText) findViewById(R.id.edtPassword);
        mUsername = (EditText) findViewById(R.id.edtUsername);
        btnRegister = findViewById(R.id.btnRegister);

    }

    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull: checking string if null.");

        if(string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }


    /*_______________________FIREBASE__________________*/

    private void checkIfUsernameExists(final String username) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        append = myRef.push().getKey().substring(3,10);
                        Log.d(TAG, "onDataChange: username already exists. Appending random string to name: " + append);
                    }
                }

                String mUsername = "";
                mUsername = username + append;
                //add new user to the database
                firebaseMethods.addNewUser(email, mUsername, "", "", "");
                Toast.makeText(mContext, "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Setup the firebase auth object
     */

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            checkIfUsernameExists(username);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    finish();


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
//            Uri selectedImage = data.getData();
//            Bitmap bitmap = null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }