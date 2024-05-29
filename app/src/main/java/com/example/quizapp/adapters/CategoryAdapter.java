package com.example.quizapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.quizapp.R;
import com.example.quizapp.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<CategoryModel> {

    private final Context context;
    private final List<CategoryModel> categories;

    public CategoryAdapter(Context context, List<CategoryModel> categories) {
        super(context, R.layout.category_item, categories);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.category_item, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.category_name);
        textView.setText(categories.get(position).getName());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(CategoryModel selectedCategory);
    }

    @Override
    public long getItemId(int position) {
        return categories.get(position).getId(); // Use category id for unique identifier
    }

    @Override
    public CategoryModel getItem(int position) {
        return categories.get(position);
    }



    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
