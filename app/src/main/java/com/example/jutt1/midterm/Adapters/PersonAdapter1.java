package com.example.jutt1.midterm.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jutt1.midterm.AllComplain;
import com.example.jutt1.midterm.PersonModel.PersonModel;
import com.example.jutt1.midterm.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PersonAdapter1 extends RecyclerView.Adapter<PersonAdapter1.PersonViewHolder> {
    private Context ctx;
    private ArrayList<PersonModel> list;

    private OnItemClickListener onItemClickListener;


    public PersonAdapter1(Context ctx, ArrayList<PersonModel> list, OnItemClickListener onItemClickListener) {
        this.ctx = ctx;
        this.list = list;

        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_design_complain,parent,false);
        return new PersonViewHolder(v, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        PersonModel model = list.get(position);

        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        holder.complain.setText(model.getComplain());
        String url = model.getImageURL();

        Picasso.with(ctx).load(url).fit().into(holder.img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, complain, email;
        ImageView img;

        OnItemClickListener onItemClickListener;


        public PersonViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            name = itemView.findViewById(R.id.patientName);
            email = itemView.findViewById(R.id.patientEmail);
            complain = itemView.findViewById(R.id.patientComplain);
            img = itemView.findViewById(R.id.patientImage);

            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
