package com.example.todolistapplication.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapplication.R;
import com.example.todolistapplication.databases.DatabaseHelper;
import com.example.todolistapplication.databases.entities.Step;
import com.example.todolistapplication.task.DetailTaskActivity;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private final List<Step> stepList;
    private DatabaseHelper databaseHelper;
    private Context context;
    public StepAdapter(List<Step> stepList, DatabaseHelper databaseHelper, Context context) {
        this.stepList = stepList;
        this.databaseHelper = databaseHelper;
        this.context = context;
    }
    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = stepList.get(position);

        holder.tv_step_name.setText(step.getName());
        if(step.isIs_done()) holder.img_check_done.setImageResource(R.drawable.round_check_circle_24);
        else holder.img_check_done.setImageResource(R.drawable.outline_circle_24);
        holder.img_check_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step.setIs_done(!step.isIs_done());
                databaseHelper.updateStep(step);
                holder.img_check_done.setImageResource(step.isIs_done() ? R.drawable.round_check_circle_24 : R.drawable.outline_circle_24);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBottom(step);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(stepList != null) {
            return stepList.size();
        }
        return 0;
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        private CardView card_step;
        private ImageView img_check_done, img_more;
        private TextView tv_step_name;
        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            card_step = itemView.findViewById(R.id.card_step);
            img_check_done = itemView.findViewById(R.id.img_check_done);
            img_more = itemView.findViewById(R.id.img_more);
            tv_step_name = itemView.findViewById(R.id.tv_step_name);
        }
    }
    public void reloadAdapter() {
        stepList.clear();
    }

    @SuppressLint("SetTextI18n")
    private void showDialogBottom(Step step){
        //Define dialog
        final Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_custom_dialog_bottom);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogBottomAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        //signing variable
        TextView tv_update_bnt = dialog.findViewById(R.id.tv_update_btn);
        TextView tv_delete_btn = dialog.findViewById(R.id.tv_delete_btn);
        tv_update_bnt.setVisibility(View.GONE);
        tv_delete_btn.setText("Delete step");
        tv_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = databaseHelper.deleteStep(step);
                if(result) createToast("Deleted " + step.getName(), R.drawable.baseline_check_circle_24);
                dialog.dismiss();
                int position = stepList.indexOf(step);
                stepList.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    private void createToast(String input_text_to_toast, int imageResId){
        Toast toast = new Toast(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_custom_toast, null);
        TextView text_toast = view.findViewById(R.id.text_toast);
        ImageView img_icon_toast = view.findViewById(R.id.img_icon_toast);
        text_toast.setText(input_text_to_toast);
        img_icon_toast.setImageResource(imageResId);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
