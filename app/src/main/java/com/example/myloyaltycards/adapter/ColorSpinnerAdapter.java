package com.example.myloyaltycards.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myloyaltycards.R;

import org.jetbrains.annotations.NotNull;

public class ColorSpinnerAdapter extends ArrayAdapter<String> {
    private int[] colors;
    private String[] colorsText;
    public ColorSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[] colorsText, int[] colors) {
        super(context, resource, textViewResourceId, colorsText);
        this.colors = colors;
        this.colorsText = colorsText;
    }

    @NonNull
    @Override
    @SuppressLint("ViewHolder")
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getInflatedItem(position, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable @org.jetbrains.annotations.Nullable View convertView, @NonNull @NotNull ViewGroup parent) {
        return getInflatedItem(position, parent);
    }

    private View getInflatedItem(int position, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spinner_item_layout, parent, false);
        View colorView = view.findViewById(R.id.display_color_spinner_item);
        colorView.setBackgroundColor(colors[position]);
        TextView colorTextSpinnerItem = view.findViewById(R.id.color_text_spinner_item);
        colorTextSpinnerItem.setText(colorsText[position]);
        return view;
    }

}
