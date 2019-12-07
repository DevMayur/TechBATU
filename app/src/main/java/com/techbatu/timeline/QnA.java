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
import android.widget.Button;
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
public class QnA extends Fragment {


    private FloatingActionButton bt_new_question;
    private RecyclerView recyclerView;
    private List<QuestionsModel> qlist;
    private QuestionAdapter questionAdapter;
    private FirebaseFirestore firebaseFirestore;

    public QnA() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qn, container, false);

        bt_new_question = view.findViewById(R.id.bt_new_question_qna);
        recyclerView = view.findViewById(R.id.question_recycler_view_qna);
        qlist = new ArrayList<>();
        questionAdapter = new QuestionAdapter(qlist,container.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView.setAdapter(questionAdapter);
        bt_new_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(container.getContext(),NewQuestion.class));
            }
        });


        firebaseFirestore.collection("questions").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
               if(!queryDocumentSnapshots.isEmpty())
               {

                   for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                       if (doc.getType() == DocumentChange.Type.ADDED) {
                           String blogPostId = doc.getDocument().getId();
                           QuestionsModel questionsModel = doc.getDocument().toObject(QuestionsModel.class).withId(blogPostId);
                           qlist.add(questionsModel);
                           questionAdapter.notifyDataSetChanged();
                       }
                   }

               }else{
                   Toast.makeText(container.getContext(), "No questions !", Toast.LENGTH_SHORT).show();
               }
            }
        });


        return view;
    }


}
