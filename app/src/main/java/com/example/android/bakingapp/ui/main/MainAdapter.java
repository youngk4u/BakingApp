package com.example.android.bakingapp.ui.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<Recipe> mRecipes;
    private OnClickHandler mClickHandler;

    public MainAdapter(OnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        Context mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String recipeName = mRecipes.get(position).getName();

        switch (recipeName) {
            case "Nutella Pie":
                holder.recipeImageView.setImageResource(R.drawable.nutella);
                break;
            case "Brownies":
                holder.recipeImageView.setImageResource(R.drawable.brownie);
                break;
            case "Yellow Cake":
                holder.recipeImageView.setImageResource(R.drawable.yellow);
                break;
            case "Cheesecake":
                holder.recipeImageView.setImageResource(R.drawable.cheese);
                break;
        }
        holder.nameTextView.setText(recipeName);
    }

    @Override
    public int getItemCount() {
        return (mRecipes == null ? 0 : mRecipes.size());
    }

    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nameTextView;
        final ImageView recipeImageView;

        ViewHolder(CardView cardView) {
            super(cardView);
            nameTextView = cardView.findViewById(R.id.tv_recipe_name);
            recipeImageView = cardView.findViewById(R.id.recipe_image);
            recipeImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick(mRecipes.get(getAdapterPosition()));
        }
    }

    public interface OnClickHandler {
        void onClick(Recipe recipe);
    }

}

