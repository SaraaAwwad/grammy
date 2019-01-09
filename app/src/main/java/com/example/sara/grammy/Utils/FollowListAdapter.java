package com.example.sara.grammy.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sara.grammy.Home.MainActivity;
import com.example.sara.grammy.Profile.ProfileActivity;
import com.example.sara.grammy.R;
import com.example.sara.grammy.models.Comment;
import com.example.sara.grammy.models.Photo;
import com.example.sara.grammy.models.User;
import com.example.sara.grammy.models.UserAccountSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowListAdapter extends ArrayAdapter {

    private static final String TAG = "FollowListAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private DatabaseReference mReference;
    private String currentUsername = "";

    public FollowListAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        this.mContext = context;
        mReference = FirebaseDatabase.getInstance().getReference();
    }

    //objects: list of usernames..




    static class ViewHolder{
        CircleImageView mprofileImage;
        TextView username;

        UserAccountSettings settings = new UserAccountSettings();
        User user  = new User();
        StringBuilder users;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final FollowListAdapter.ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            holder = new FollowListAdapter.ViewHolder();

            holder.username = (TextView) convertView.findViewById(R.id.username_follow);
            holder.mprofileImage = (CircleImageView) convertView.findViewById(R.id.profile_photo_follow);

            convertView.setTag(holder);

        }else{
            holder = (FollowListAdapter.ViewHolder) convertView.getTag();
            //Log.d(TAG,"Photos: adapter: view null: "+holder.photo.getPhoto_id());
            //Log.d(TAG, "Photos holder in not null: "+ holder.caption );

        }


       // holder.users = new StringBuilder();

        //set the profile image
        final ImageLoader imageLoader = ImageLoader.getInstance();

//
//               DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//                Query query = reference
//                .child(mContext.getString(R.string.dbname_user_account_settings))
//                .orderByChild(mContext.getString(R.string.field_user_id))
//                 .equalTo(String.valueOf((getItem(position))));

    holder.username.setText(((UserAccountSettings)getItem(position)).getUsername());
        imageLoader.displayImage(((UserAccountSettings)getItem(position)).getProfile_photo(),
                holder.mprofileImage);

        //get following id
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference
//                .child(mContext.getString(R.string.dbname_following))
//                .child(String.valueOf(getItem(position)));

//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
//
//
//                    Log.d(TAG, "onDataChange: found user: "
//                            + singleSnapshot.getValue(UserAccountSettings.class).getUsername());
//
//                    holder.username.setText(singleSnapshot.getValue(UserAccountSettings.class).getUsername());
////                    holder.username.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            Log.d(TAG, "onClick: navigating to profile of: " +
////                                    holder.user.getUsername());
////
////                            Intent intent = new Intent(mContext, ProfileActivity.class);
////                            intent.putExtra(mContext.getString(R.string.calling_activity),
////                                    mContext.getString(R.string.home_activity));
////                            intent.putExtra(mContext.getString(R.string.intent_user), holder.user);
////                            mContext.startActivity(intent);
////                        }
////                    });
//
//                    imageLoader.displayImage(singleSnapshot.getValue(UserAccountSettings.class).getProfile_photo(),
//                            holder.mprofileImage);
////                    holder.mprofileImage.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            Log.d(TAG, "onClick: navigating to profile of: " +
////                                    holder.user.getUsername());
////
////                            Intent intent = new Intent(mContext, ProfileActivity.class);
//////                            intent.putExtra(mContext.getString(R.string.calling_activity),
//////                                    mContext.getString(R.string.home_activity));
////                            intent.putExtra(mContext.getString(R.string.intent_user), holder.user);
////                            mContext.startActivity(intent);
////                        }
////                    });
//
//
//                    holder.settings = singleSnapshot.getValue(UserAccountSettings.class);
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        //get the user object
//        Query userQuery = mReference
//                .child(mContext.getString(R.string.dbname_users))
//                .orderByChild(mContext.getString(R.string.field_user_id))
//                .equalTo(((Photo)getItem(position)).getUser_id());

//        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
//                    Log.d(TAG, "onDataChange: found user: " +
//                            singleSnapshot.getValue(User.class).getUsername());
//
//                    holder.user = singleSnapshot.getValue(User.class);
 //               }
//            }

//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        return convertView;
    }



}

