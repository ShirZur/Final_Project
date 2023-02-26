package com.example.google_try;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolders> {

    private Context context;

    private ArrayList<Nanny> list;

    private RecyclerViewClickInterface recyclerViewClickInterface;



    public MyAdapter(Context context, ArrayList<Nanny> list, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public MyViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolders holder, int position) {
        Nanny nanny = list.get(position);
        holder.first_name_list.setText((nanny.getFirstName()));
        holder.age_list.setText(" " + nanny.getAge());
        holder.hourly_rate_list.setText(nanny.getPerHour()+".00/hr");
        Picasso.get().load(nanny.getProfilePicture()).into(holder.profile_picture_list);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    public class MyViewHolders extends RecyclerView.ViewHolder {

        private TextView first_name_list, age_list, hourly_rate_list;

        private CircleImageView profile_picture_list;

        public MyViewHolders(@NonNull View itemView) {
            super(itemView);

            first_name_list = itemView.findViewById(R.id.first_name_list);
            age_list = itemView.findViewById(R.id.age_list);
            hourly_rate_list = itemView.findViewById(R.id.hourly_rate_list);
            profile_picture_list = itemView.findViewById(R.id.profile_picture_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getBindingAdapterPosition());
                }
            });

        }


    }
}
