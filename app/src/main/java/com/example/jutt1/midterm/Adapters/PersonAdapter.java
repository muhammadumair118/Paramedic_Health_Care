package com.example.jutt1.midterm.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jutt1.midterm.PersonModel.PersonModel;
import com.example.jutt1.midterm.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {
    private Context ctx;
    private ArrayList<PersonModel> list;

    private OnItemClickListener onItemClickListener;


    public PersonAdapter(Context ctx, ArrayList<PersonModel> list, OnItemClickListener onItemClickListener) {
        this.ctx = ctx;
        this.list = list;

        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_design,parent,false);
        return new PersonViewHolder(v, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        PersonModel model = list.get(position);

        holder.name.setText(model.getName());
        //holder.phone.setText(model.getPhone());
        holder.spec.setText(model.getSpecialization());
        holder.ratingBar.setRating(Integer.parseInt(model.getRating_bar()));

        String url = model.getImageURL();

        Picasso.with(ctx).load(url).fit().into(holder.img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, spec, phone;
        ImageView img;
        RatingBar ratingBar;

        OnItemClickListener onItemClickListener;


        public PersonViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            name = itemView.findViewById(R.id.doctorName);
            spec = itemView.findViewById(R.id.doctorSpecialization);
            //phone = itemView.findViewById(R.id.doctorPhone);
            img = itemView.findViewById(R.id.doctorImage);
            ratingBar = itemView.findViewById(R.id.ratingBar);

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
