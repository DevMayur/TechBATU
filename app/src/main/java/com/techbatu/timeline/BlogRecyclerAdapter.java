package com.techbatu.timeline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.techbatu.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogPost> blog_list;
    private Context context;
    private String prn;
    private FirebaseFirestore firebaseFirestore;
    private int count;
    public BlogRecyclerAdapter(List<BlogPost> blogPost) {

        this.blog_list = blogPost;
    }

    @NonNull

    @Override
    public BlogRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_list_item,viewGroup,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BlogRecyclerAdapter.ViewHolder viewHolder, int i) {

        SharedPreferences sharedPreferences1 = context.getSharedPreferences("No_Document_status",Context.MODE_PRIVATE);
        String docstatus = sharedPreferences1.getString("status","");

        if (docstatus.equals("null")) {

            Toast.makeText(context, "No posts found!", Toast.LENGTH_SHORT).show();

        }else{

        //viewHolder.setIsRecyclable(false);

        SharedPreferences sharedPreferences = context.getSharedPreferences("basic_student_info",Context.MODE_PRIVATE);
        prn = sharedPreferences.getString("prn","");
        if(prn.equals(blog_list.get(i).getPrn()))
        {
            viewHolder.bt_del.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.bt_del.setVisibility(View.INVISIBLE);
        }

        final String blogPostId = blog_list.get(i).BlogPostId;



        firebaseFirestore.collection("posts/"+blogPostId+"/likes").document(prn).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    viewHolder.iv_like.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.action_like));

                }else{
                    viewHolder.iv_like.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.action_like_inactive));
                }
            }
        });


        //Count Likes

        firebaseFirestore.collection("posts/"+blogPostId+"/likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty())
                {

                    count = queryDocumentSnapshots.size();
                    viewHolder.updatelikecount(count);
                    viewHolder.setfullpost(blogPostId,count);

                }else{

                    viewHolder.updatelikecount(0);

                }
            }
        });

        //End of count Likes


    //delete feature

            viewHolder.bt_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    firebaseFirestore.collection("posts").document(blogPostId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                firebaseFirestore.collection("posts").document(blogPostId).delete();
                                deletelikes();
                                Toast.makeText(context, "Delete Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Error - " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        private void deletelikes() {

                            firebaseFirestore.collection("posts/" + blogPostId + "/likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    if (!queryDocumentSnapshots.isEmpty()) {

                                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                            String likeid = doc.getDocument().getId();
                                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                                firebaseFirestore.collection("posts/" + blogPostId + "/likes").document(likeid).delete();

                                            }
                                        }

                                    }
                                }
                            });

                        }
                    });
                }
            });


            //end of delete feature


            //Like feature
            viewHolder.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseFirestore.collection("posts/" + blogPostId + "/likes").document(prn).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (!task.getResult().exists()) {
                                Date currentTime = Calendar.getInstance().getTime();
                                Map<String, Object> likeMap = new HashMap<>();
                                likeMap.put(prn, currentTime.toString());
                                firebaseFirestore.collection("posts/" + blogPostId + "/likes").document(prn).set(likeMap);

                            } else {
                                firebaseFirestore.collection("posts/" + blogPostId + "/likes").document(prn).delete();

                            }

                        }
                    });


                }
            });
            //End of like functionality
        }

            viewHolder.setDate(blog_list.get(i).getTimestamp());

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference pathReference = storageRef.child("profile/profile_pic/" + blog_list.get(i).getPrn() + ".jpg");
            viewHolder.setProfilePost(pathReference);
            String desk_data = blog_list.get(i).getDescription();
            viewHolder.setDeskText(desk_data);
            String user = blog_list.get(i).getUsername();
            viewHolder.setUsername(user);
            String imageUrl = blog_list.get(i).getImage_Uir();
            viewHolder.setBlogImage(imageUrl);



    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mView, username, date, tv_like_count;
        private ImageView iv_post_image;
        private ImageView iv_post_profile;
        private ImageView iv_like;
        private Button bt_del;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_like_count = itemView.findViewById(R.id.tv_like_count);
            iv_like = itemView.findViewById(R.id.iv_like);
            bt_del = itemView.findViewById(R.id.bt_del_post);
        }

        public void setUsername(String user) {
            username = itemView.findViewById(R.id.tv_username_post);
            username.setText(user);
        }

        public void setDeskText(String deskText) {
            mView = itemView.findViewById(R.id.tv_dsc_final);
            mView.setText(deskText);
        }

        public void setBlogImage(String downloadUri) {
            iv_post_image = itemView.findViewById(R.id.iv_post_image_final);
            Glide.with(context).load(downloadUri).into(iv_post_image);
        }

        public void setProfilePost(StorageReference downloadUri) {
            iv_post_profile = itemView.findViewById(R.id.iv_post_profile_final);
            Glide.with(context).load(downloadUri).into(iv_post_profile);
        }

        public void setDate(String datestr) {
            date = itemView.findViewById(R.id.tv_date_post);
            date.setText("Date : "+datestr);
        }

        public void updatelikecount(int count) {
            tv_like_count = itemView.findViewById(R.id.tv_like_count);
            tv_like_count.setText(count + " Likes");
        }

        public void setfullpost(final String id, final int likes)
        {
            iv_post_image = itemView.findViewById(R.id.iv_post_image_final);
            iv_post_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context, ViewPost.class);
                    intent.putExtra("POST_IMAGE_PATH",id);
                    intent.putExtra("likes",likes);
                    context.startActivity(intent);
                }
            });
        }

    }
}
