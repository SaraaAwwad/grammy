package com.example.sara.grammy.Utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sara.grammy.R;
import com.example.sara.grammy.models.Notification;
import com.example.sara.grammy.models.UserAccountSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private Context mContext;
    private List<Notification> mNotification;
    private static final String TAG = "NotificationListAdapter";
    private LayoutInflater mInflater;
    private int layoutResource;

    public NotificationListAdapter (@NonNull Context context, @LayoutRes
    int resource, @NonNull List<Notification> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        layoutResource = resource;
    }

    private static class ViewHolder{
        TextView username,notification_type,notify_time_posted;
        CircleImageView profileImage;
        ImageView image_liked;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final NotificationListAdapter.ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new NotificationListAdapter.ViewHolder();

            holder.username = convertView.findViewById(R.id.likes_notify_username);
            holder.profileImage = convertView.findViewById(R.id.likes_notify_image);
            holder.image_liked = convertView.findViewById(R.id.image_liked);
            holder.notification_type = convertView.findViewById(R.id.notification_notify);
            holder.notify_time_posted = convertView.findViewById(R.id.notify_time_posted);

            convertView.setTag(holder);
        }else{
            holder = (NotificationListAdapter.ViewHolder) convertView.getTag();
        }


        //set the username and profile image
        if(getItem(position).getType() == "Like") {

            //set the timestamp difference
            holder.notify_time_posted.setText("");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference
                    .child(mContext.getString(R.string.dbname_user_account_settings))
                    .orderByChild(mContext.getString(R.string.field_user_id))
                    .equalTo(getItem(position).getUser_id());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        holder.username.setText(
                                singleSnapshot.getValue(UserAccountSettings.class).getUsername());

                        holder.notification_type.setText(mContext.getString(R.string.likes_descr));

                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.displayImage(
                                singleSnapshot.getValue(UserAccountSettings.class).getProfile_photo(),
                                holder.profileImage);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: query cancelled.");
                }
            });

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(getItem(position).getImage_path(),
                    holder.image_liked);


        }else if(getItem(position).getType() == "Comment") {

            //set the timestamp difference
            String timestampDifference = getTimestampDifference(getItem(position));
            if(!timestampDifference.equals("0")){
                holder.notify_time_posted.setText(timestampDifference + " d");
            }else{
                holder.notify_time_posted.setText("today");
            }

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference
                    .child(mContext.getString(R.string.dbname_user_account_settings))
                    .orderByChild(mContext.getString(R.string.field_user_id))
                    .equalTo(getItem(position).getUser_id());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        holder.username.setText(
                                singleSnapshot.getValue(UserAccountSettings.class).getUsername());
                        holder.notification_type.setText(mContext.getString(R.string.comments_descr));
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.displayImage(
                                singleSnapshot.getValue(UserAccountSettings.class).getProfile_photo(),
                                holder.profileImage);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: query cancelled.");
                }
            });

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(getItem(position).getImage_path(),
                    holder.image_liked);

        }


        return convertView;
    }

    /**
     * Returns a string representing the number of days ago the post was made
     * @return
     */
    private String getTimestampDifference(Notification notify){
        Log.d(TAG, "getTimestampDifference: getting timestamp difference.");

        String difference = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Africa/Cairo"));//google 'android list of timezones'
        Date today = c.getTime();
        sdf.format(today);
        Date timestamp;
        final String photoTimestamp = notify.getDate_created();
        Log.d(TAG, "getTimestampDifference: getting timestamp difference." + notify.getDate_created());
        try{
            timestamp = sdf.parse(photoTimestamp);
            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24 )));
        }catch (ParseException e){
            Log.e(TAG, "getTimestampDifference: ParseException: " + e.getMessage() );
            difference = "0";
        }
        return difference;
    }

}
