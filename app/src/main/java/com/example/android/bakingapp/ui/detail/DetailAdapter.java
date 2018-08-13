package com.example.android.bakingapp.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.RecipeStep;

import java.util.ArrayList;
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private final DetailActivity mParent;
    private final List<RecipeStep> mValues;
    private final boolean mMasterDetail;

    private int mSelected = RecyclerView.NO_POSITION;

    public DetailAdapter(DetailActivity parent, List<RecipeStep> items, boolean masterDetail) {
        mValues = items;
        mParent = parent;
        mMasterDetail = masterDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_step_list_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String stepNum = "";
        if (position > 0) stepNum = position + ". ";
        String stepDescription = stepNum + mValues.get(position).getShortDescription();
        holder.mView.setText(stepDescription);
        holder.itemView.setTag(position);
        if (mSelected == position) {
            int mColor = ContextCompat.getColor(mParent, R.color.colorPrimaryDark);
            holder.mView.setTextColor(mColor);
        } else {
            holder.mView.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mView;

        ViewHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            if (mMasterDetail) {
                notifyItemChanged(mSelected);

                mSelected = getAdapterPosition();
                notifyItemChanged(mSelected);

                Bundle arguments = new Bundle();
                arguments.putParcelable(DetailStepFragment.ITEM_ID, mValues.get(position));
                DetailStepFragment fragment = new DetailStepFragment();
                fragment.setArguments(arguments);
                mParent.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_step_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, DetailStepActivity.class);
                intent.putExtra(DetailStepActivity.RECIPE_NAME, mParent.mRecipe.getName());
                intent.putExtra(DetailStepActivity.STEP_INDEX, position);
                intent.putParcelableArrayListExtra(DetailStepActivity.STEPS_LIST,
                        (ArrayList<RecipeStep>) mParent.mRecipe.getSteps());

                context.startActivity(intent);
            }
        }
    }
}
