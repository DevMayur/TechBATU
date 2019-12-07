package com.techbatu.timeline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techbatu.R;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private List<AnswerModel> alist;
    private Context context;

    public AnswerAdapter(List<AnswerModel> alist, Context context) {

        this.alist = alist;
        this.context = context;

    }

    @NonNull
    @Override
    public AnswerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_answer_item,viewGroup,false);
        context = viewGroup.getContext();
        return new AnswerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerAdapter.ViewHolder viewHolder, int i) {

        String questionId = alist.get(i).BlogPostId;

        viewHolder.setAnswer(alist.get(i).getAnswer());
        viewHolder.setAnswer_image(alist.get(i).getImage_Uir());
        viewHolder.setUsername(alist.get(i).getUsername());

        final int position = i;

        viewHolder.cv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("Answer_Adapter",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("answer",alist.get(position).getAnswer());
                editor.putString("image_Uir",alist.get(position).getImage_Uir());
                editor.putString("username",alist.get(position).getUsername());
                editor.commit();
                context.startActivity(new Intent(context,ViewFullAnswer.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return alist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username,answer;
        private ImageView answer_image;
        private CardView cv_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_username_single_answer);
            answer_image = itemView.findViewById(R.id.iv_single_answer_image);
            answer = itemView.findViewById(R.id.tv_single_answer_answer);
            cv_card = itemView.findViewById(R.id.cv_single_answer_card);
        }

        public void setAnswer(String answer) {
            this.answer.setText(answer);
        }

        public void setAnswer_image(String answer_image) {
            Glide.with(context).load(answer_image).into(this.answer_image);
        }

        public void setUsername(String username) {
            this.username.setText(username);

        }
    }
}
