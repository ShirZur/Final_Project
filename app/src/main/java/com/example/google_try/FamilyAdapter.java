package com.example.google_try;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.MyViewHolders>{

    private Context context;

    private ArrayList<Family> familyList;

    private RecyclerViewClickInterface recyclerViewClickInterface;

    public FamilyAdapter(Context context, ArrayList<Family> familyList, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.familyList = familyList;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public MyViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_family,parent,false);
        return new FamilyAdapter.MyViewHolders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolders holder, int position) {
        Family family = familyList.get(position);
        holder.family_name_list.setText(family.getFamily_name() + " Family");
        holder.hourly_wage_list.setText(family.getHourly_wage() + ".00/hr");
        Picasso.get().load(family.getProfilePicture()).into(holder.profile_picture_family_list);


    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }

    public class MyViewHolders extends RecyclerView.ViewHolder {

        private TextView family_name_list, hourly_wage_list;

        private CircleImageView profile_picture_family_list;

        public MyViewHolders(@NonNull View itemView) {
            super(itemView);

            family_name_list = itemView.findViewById(R.id.family_name_list);
            hourly_wage_list = itemView.findViewById(R.id.hourly_wage_list);
            profile_picture_family_list = itemView.findViewById(R.id.profile_picture_family_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getBindingAdapterPosition());

                }
            });

        }


    }


}
