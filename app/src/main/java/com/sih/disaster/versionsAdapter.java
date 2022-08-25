package com.sih.disaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class versionsAdapter extends RecyclerView.Adapter<versionsAdapter.viewHolder> {

    List<Versions> versionsList;

    public versionsAdapter(List<Versions> versionsList) {
        this.versionsList = versionsList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Versions ver = versionsList.get(position);
        holder.questions.setText(ver.getQuestions());
        holder.quesAnswer.setText(ver.getAnswer());
        boolean isExpandable = versionsList.get(position).isExpandable();
        holder.relativeLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return versionsList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView questions, quesAnswer;
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            questions = itemView.findViewById(R.id.question);
            quesAnswer = itemView.findViewById(R.id.answers);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            relativeLayout = itemView.findViewById(R.id.expandable_answers);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Versions version = versionsList.get(getAdapterPosition());
                    version.setExpandable(!version.isExpandable());
                    notifyItemChanged(getAdapterPosition());

                }
            });

        }
    }
}
