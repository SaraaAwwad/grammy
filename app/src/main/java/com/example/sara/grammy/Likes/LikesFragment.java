package com.example.sara.grammy.Likes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sara.grammy.R;
import com.example.sara.grammy.Utils.BottomNavigationViewHelper;
import com.example.sara.grammy.Utils.NotificationListAdapter;
import com.example.sara.grammy.models.Like;
import com.example.sara.grammy.models.Notification;
import com.example.sara.grammy.models.Photo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LikesFragment extends Fragment {

    private static final String TAG = "LikesFragment";
    private static final int ACTIVITY_NUM = 3;

    public LikesFragment(){
        super();
        setArguments(new Bundle());
    }

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseUser user;

    //widgets
    private ListView mListView;
    private BottomNavigationView bottomNavigationView;

    //vars
    private Photo mPhoto;
    private ArrayList<Notification> mNotifications;
    private Context mContext;
    List<Photo> Photos;
    List<Like> likesArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        mListView = view.findViewById(R.id.listView);
        mNotifications = new ArrayList<>();
        bottomNavigationView = view.findViewById(R.id.bottomNavViewBar);
        mContext = getActivity();

        try{
            //mPhoto = getPhotoFromBundle();
            setupFirebaseAuth();

        }catch (NullPointerException e){
            Log.e(TAG, "onCreateView: NullPointerException: " + e.getMessage() );
        }

        setUpBottomNav();
        return view;
    }

    private void setupWidgets(){

        NotificationListAdapter adapter = new NotificationListAdapter(mContext,
                R.layout.layout_notification, mNotifications);
        mListView.setAdapter(adapter);

    }

    /**
     * retrieve the photo from the incoming bundle from profileActivity interface
     * @return
     */
    private String getCallingActivityFromBundle(){
        Log.d(TAG, "getPhotoFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            return bundle.getString(getString(R.string.home_activity));
        }else{
            return null;
        }
    }

    /**
     * retrieve the photo from the incoming bundle from profileActivity interface
     * @return
     */
    private Photo getPhotoFromBundle(){
        Log.d(TAG, "getPhotoFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            return bundle.getParcelable(getString(R.string.photo));
        }else{
            return null;
        }
    }

    public void setUpBottomNav() {
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        BottomNavigationViewHelper.enableNav(mContext, getActivity(),bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

            /*
    ------------------------------------ Firebase ---------------------------------------------
     */

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
                user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        DatabaseReference user_photoRef = myRef.child(mContext.getString(R.string.dbname_user_photos)).child(mAuth.getCurrentUser().getUid());
        Photos = new ArrayList<>();
        likesArray = new ArrayList<>();
        user_photoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    try {

                    for (DataSnapshot LL :childSnapshot.child(mContext.getString(R.string.field_likes)).getChildren()){
                        Notification notify = new Notification();
                        notify.setUser_id(LL.child(mContext.getString(R.string.field_user_id)).getValue().toString());
                        notify.setPhoto_id(childSnapshot.child(mContext.getString(R.string.field_photo_id)).getValue().toString());
                        notify.setImage_path(childSnapshot.child(mContext.getString(R.string.field_image_path)).getValue().toString());
                        notify.setType("Like");
                        mNotifications.add(notify);
                    }

                        for (DataSnapshot LL :childSnapshot.child(mContext.getString(R.string.field_comments)).getChildren()){
                            Notification notify = new Notification();
                            notify.setUser_id(LL.child(mContext.getString(R.string.field_user_id)).getValue().toString());
                            notify.setPhoto_id(childSnapshot.child(mContext.getString(R.string.field_photo_id)).getValue().toString());
                            notify.setImage_path(childSnapshot.child(mContext.getString(R.string.field_image_path)).getValue().toString());
                            notify.setDate_created(LL.child(mContext.getString(R.string.field_date_created)).getValue().toString());
                            notify.setType("Comment");
                            mNotifications.add(notify);
                        }

                        Collections.sort(mNotifications, Collections.reverseOrder());
                    }
                    catch (Exception e){
                        Log.d("error",e.toString());
                    }


                }

                setupWidgets();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
