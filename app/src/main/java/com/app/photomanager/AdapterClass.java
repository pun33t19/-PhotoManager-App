package com.app.photomanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;


import java.io.File;
import java.util.ArrayList;

class ProgrammingAdapterClass extends RecyclerView.Adapter<ProgrammingAdapterClass.ProgrammingViewHolder>{

private final Context context;
private final ArrayList<String> imagePathList;



    //constructor used for initialising the values of the imagePath and to get the context
    public ProgrammingAdapterClass(Context context,ArrayList<String> imagePathList){
        this.context=context;
        this.imagePathList=imagePathList;
}




    public ProgrammingViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.sample_list_view,parent,false);
        return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ProgrammingAdapterClass.ProgrammingViewHolder holder, int position) {
       String imageloc=imagePathList.get(position);

        RequestOptions requestOptions=new RequestOptions().placeholder(R.drawable.ic_launcher_background);

            Glide.with(context).load(imageloc).into(holder.imageView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context,imageDisplay.class);
                    i.putExtra("strPath",imagePathList.get(position));
                    context.startActivity(i);
                }
            });


    }

    @Override
    public int getItemCount() {
        //the method to return the size of the arraylist(imagePathList)
        return imagePathList.size();
    }

        //used to initialise the views
    public static class ProgrammingViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;


        public ProgrammingViewHolder( View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageview);

        }
    }
}
