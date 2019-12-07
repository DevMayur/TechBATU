package com.techbatu.timeline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techbatu.GlideApp;
import com.techbatu.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private List<QuestionsModel> qlist;
    private Context context;

    public QuestionAdapter(List<QuestionsModel> qlist, Context context) {
        this.qlist = qlist;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_question_item,viewGroup,false);
        context = viewGroup.getContext();
        return new QuestionAdapter.ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.setusername(qlist.get(i).getUsername());
        viewHolder.setquestion(qlist.get(i).getQuestion());
        viewHolder.setpostimage(qlist.get(i).getImage_Uir());

        final int position = i;
        bt_add_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences sharedPreferences = context.getSharedPreferences("add_answer_to",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username",qlist.get(position).getUsername());
                editor.putString("question",qlist.get(position).getQuestion());
                editor.putString("imgUri",qlist.get(position).getImage_Uir());
                editor.putString("postId",qlist.get(position).BlogPostId);
                editor.commit();

                context.startActivity(new Intent(context,AddAnswers.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return qlist.size();
    }

    private TextView question, username;
    private ImageView iv_post_image;
    private Button bt_add_answer;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.tv_question_single_post_final);
            username = itemView.findViewById(R.id.tv_username_single_question_final);
            iv_post_image = itemView.findViewById(R.id.iv_image_single_post_final);
            bt_add_answer = itemView.findViewById(R.id.bt_add_answer_final);

        }

        public void setquestion(String que)
        {
            question.setText(que);
        }
        public void setusername(String user)
        {
            username.setText(user);
        }
        public void setpostimage(String uri)
        {
            GlideApp.with(itemView).load(uri).into(iv_post_image);
        }


    }
}
