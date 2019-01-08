package com.example.sara.grammy.Utils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.sara.grammy.R;
import com.example.sara.grammy.models.User;
import com.example.sara.grammy.models.UserAccountSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewFollowersFragment extends Fragment {
    private static final String TAG = "FollowersFragment";
    private ArrayList<UserAccountSettings> mFollowing;
    private ListView mListView;
    private FollowListAdapter mAdapter;
    private ImageView mBackArrow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      Log.d(TAG, "viewfollowers view");
        View view = inflater.inflate(R.layout.fragment_view_followers, container, false);

        mFollowing = new ArrayList<>();
        mListView = (ListView) view.findViewById(R.id.listViewFollowers);
        mBackArrow = (ImageView) view.findViewById(R.id.backArrow);

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back");
                getActivity().getSupportFragmentManager().popBackStack();
              //  getActivity().finish();
            }
        });

        int choice = getFollowCategory();
        Log.d(TAG, "choice = "+ choice);
        if (choice == 0){

            getFollowers();

        }else if (choice == 1){
            getFollowing();
        }

        return view;
    }

    private String getProfileID(){
        Log.d(TAG, "getProfileFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            return bundle.getString(getString(R.string.intent_user));
        }else{
            return null;
        }
    }

    private int getFollowCategory(){
        Log.d(TAG, "getFollowTypeFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null ) {
            if(bundle.getString(getString(R.string.dbname_followers))!=null){
                return  0;
            }else if(bundle.getString(getString(R.string.dbname_following))!=null){
                return 1;
            }else{
                return -1;
            }
        }
        return -1;
    }

    private void getFollowing(){
        Log.d(TAG, "getFollowing: searching for following");

        if(getProfileID()!=null){

        //get the following
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_following))
                .child(getProfileID());


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found user: " +
                            singleSnapshot.child(getString(R.string.field_user_id)).getValue());

                    Query userQuery = reference
                            .child(getString(R.string.dbname_user_account_settings))
                            .orderByChild(getString(R.string.field_user_id))
                            .equalTo(singleSnapshot.child(getString(R.string.field_user_id)).getValue()+"");

                    userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                //UserAccountSettings u = new UserAccountSettings();
                                //u.setUser_id(singleSnapshot.child(getString(R.string.field_user_id)).getValue());
                                UserAccountSettings users = singleSnapshot.getValue(UserAccountSettings.class);
                                //mFollowing.add(singleSnapshot.child(getString(R.string.field_user_id)).getValue().toString());
                                Log.d(TAG, "onDataChange3: found user: " +
                                        users+"id = "+ users.getUser_id());

                                mFollowing.add(users);
                                Log.d(TAG, "following list1: "+ mFollowing);

                            }
                                Log.d(TAG, "following list: "+ mFollowing);

                                mAdapter = new FollowListAdapter(getActivity(), R.layout.layout_followers_listview, mFollowing);
                                mListView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

                }

               // mFollowing.add(FirebaseAuth.getInstance().getCurrentUser().getUid());

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        }
    }

    private void getFollowers(){

        Log.d(TAG, "getFollowers: searching for followers");

        if(getProfileID()!=null){

            //get the following
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference
                    .child(getString(R.string.dbname_followers))
                    .child(getProfileID());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Log.d(TAG, "onDataChange: found user: " +
                                singleSnapshot.child(getString(R.string.field_user_id)).getValue());

                        Query userQuery = reference
                                .child(getString(R.string.dbname_user_account_settings))
                                .orderByChild(getString(R.string.field_user_id))
                                .equalTo(singleSnapshot.child(getString(R.string.field_user_id)).getValue()+"");

                        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                    //UserAccountSettings u = new UserAccountSettings();
                                    //u.setUser_id(singleSnapshot.child(getString(R.string.field_user_id)).getValue());
                                    UserAccountSettings users = singleSnapshot.getValue(UserAccountSettings.class);
                                    //mFollowing.add(singleSnapshot.child(getString(R.string.field_user_id)).getValue().toString());
                                    Log.d(TAG, "onDataChange3: found user: " +
                                            users+"id = "+ users.getUser_id());

                                    mFollowing.add(users);
                                    Log.d(TAG, "following list1: "+ mFollowing);

                                }
                                Log.d(TAG, "following list: "+ mFollowing);

                                mAdapter = new FollowListAdapter(getActivity(), R.layout.layout_followers_listview, mFollowing);
                                mListView.setAdapter(mAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    // mFollowing.add(FirebaseAuth.getInstance().getCurrentUser().getUid());

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }

}
