package com.techbatu.timeline;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.techbatu.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView blog_list_view;
    List<BlogPost> blog_list;
    FirebaseFirestore firebaseFirestore;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstload = true;
    private FloatingActionButton bt_new_post;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        blog_list = new ArrayList<>();
        bt_new_post = view.findViewById(R.id.bt_new_post);


        bt_new_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(container.getContext(),NewPost.class));
            }
        });

        blog_list_view = view.findViewById(R.id.blog_list_view);
        blogRecyclerAdapter = new BlogRecyclerAdapter(blog_list);
        blog_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        blog_list_view.setAdapter(blogRecyclerAdapter);
        firebaseFirestore = FirebaseFirestore.getInstance();

        blog_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                SharedPreferences sharedPreferences1 = container.getContext().getSharedPreferences("No_Document_status",Context.MODE_PRIVATE);
                String docstatus = sharedPreferences1.getString("status","");
                if(docstatus.equals("null")){

                }else{
                    if (reachedBottom) {
                        loadMorePost();
                    }
                }

            }
        });
        Query firstQuery = firebaseFirestore.collection("posts")
                .orderBy("timestamp",Query.Direction.DESCENDING)
                .limit(3);

            firstQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if(!queryDocumentSnapshots.isEmpty()){
                        SharedPreferences sharedPreferences = container.getContext().getSharedPreferences("No_Document_status",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status","filled");
                        editor.commit();

                    if (isFirstPageFirstload) {
                        lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    }
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String blogPostId = doc.getDocument().getId();
                            BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);


                            if (isFirstPageFirstload) {
                                blog_list.add(blogPost);
                            } else {
                                blog_list.add(0, blogPost);
                            }


                            blogRecyclerAdapter.notifyDataSetChanged();


                        }
                    }

                    isFirstPageFirstload = false;
                }else
                    {

                        SharedPreferences sharedPreferences = container.getContext().getSharedPreferences("No_Document_status",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status","null");
                        editor.commit();
                        Toast.makeText(container.getContext(), "No Posts!", Toast.LENGTH_SHORT).show();
                    }


            }
            });


            return view;
        }


        public void loadMorePost()
        {

            Query nextQuery = firebaseFirestore.collection("posts")
                    .orderBy("timestamp",Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(5);

            nextQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                    if(!queryDocumentSnapshots.isEmpty()){

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("No_Document_status",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status","filled");
                        editor.commit();

                    lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String blogPostId = doc.getDocument().getId();
                            BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                            blog_list.add(blogPost);
                            blogRecyclerAdapter.notifyDataSetChanged();
                        }
                    }

                }else
                    {

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("No_Document_status",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status","null");
                        editor.commit();
                        Toast.makeText(getContext(), "No Posts!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }


}
