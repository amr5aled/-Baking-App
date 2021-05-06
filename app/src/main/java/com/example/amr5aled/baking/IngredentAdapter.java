package com.example.amr5aled.baking;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;



public class IngredentAdapter extends RecyclerView.Adapter<Holder> {
    ArrayList<ingredients> ingredientses;

    public IngredentAdapter(ArrayList<ingredients> ingredientses) {
        this.ingredientses = ingredientses;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingred_item, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.id.setText(position + "");
        holder.text1.setText("Ingredent " + ingredientses.get(position).getIngredient());
        holder.text2.setText("Measure " + ingredientses.get(position).getMeasure());
        holder.text3.setText("Quantity " + ingredientses.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return ingredientses.size();
    }
}

class Holder extends RecyclerView.ViewHolder {

    TextView id, text1, text2, text3;

    public Holder(View itemView) {
        super(itemView);
        id = (TextView) itemView.findViewById(R.id.id);
        text1 = (TextView) itemView.findViewById(R.id.text1);
        text2 = (TextView) itemView.findViewById(R.id.text2);
        text3 = (TextView) itemView.findViewById(R.id.text3);
    }
}
