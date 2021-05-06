package com.example.amr5aled.baking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>{

    Context context ;
    ArrayList<Recipe> recipeArrayList;
    private OnItemClick onItemClick;



    public RecipesAdapter(Context context, ArrayList<Recipe> recipeArrayList) {
        this.context = context;
        this.recipeArrayList = recipeArrayList;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipes_items,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe rcp = recipeArrayList.get(position);
        holder.textView.setText(rcp.getName());
        if(!TextUtils.isEmpty(rcp.getImage())){
            Picasso.with(context).load(rcp.getImage()).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.food);
        }
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }
    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recip_image);
            textView= (TextView) itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClick != null)
                onItemClick.onItemClick(getAdapterPosition());
        }
    }
    interface OnItemClick{
        void onItemClick(int position);
    }
}
