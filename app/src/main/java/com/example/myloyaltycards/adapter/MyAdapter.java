package com.example.myloyaltycards.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myloyaltycards.R;
import com.example.myloyaltycards.activity.CardDetailActivity;
import com.example.myloyaltycards.model.Card;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Card> mDataset;
    private Activity mainActivity;
    private ActivityResultLauncher<Intent> getActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.item_text_view);
            imageView = view.findViewById(R.id.item_image_view);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() {
            return imageView;
        }
        public void setBackgroundColor(int color) {
            CardView cardView = itemView.findViewById(R.id.list_item_card_view);
            cardView.setCardBackgroundColor(color);
        }
    }

    public MyAdapter(Activity activity, ActivityResultLauncher<Intent> getActivity) {
        this.mainActivity = activity;
        this.getActivity = getActivity;
        mDataset = new ArrayList<>();
    }

    public void updateDataSet(List<Card> cards) {
        if(cards != null) {
            mDataset = cards;
            notifyDataSetChanged();
        }
    }

    public Card deleteCard(int deleteCardID) {
        Card cardToDelete = null;
        for(Card card : mDataset)
            if(card.getUid() == deleteCardID)
                cardToDelete = card;
        if(cardToDelete != null) {
            int position = mDataset.indexOf(cardToDelete);
            mDataset.remove(cardToDelete);
            notifyItemRemoved(position);
        }
        return cardToDelete;
    }

    public void addCard(Card card) {
        if(card != null) {
            mDataset.add(card);
            int position = mDataset.indexOf(card);
            notifyItemInserted(position);
        }
    }

    private void showCardDetails(Context context, Card card) {
        Intent intent = new Intent(context, CardDetailActivity.class);
        intent.putExtra("id", card.getUid());
        getActivity.launch(intent);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Card card = mDataset.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCardDetails(view.getContext(), card);
            }
        });
        viewHolder.setBackgroundColor(card.getColor());
        if(card.getLogoPath() == null) {
            viewHolder.getImageView().setVisibility(View.GONE);
            viewHolder.getTextView().setVisibility(View.VISIBLE);
            viewHolder.getTextView().setText(card.getCompanyName());
        } else {
            viewHolder.getTextView().setVisibility(View.GONE);
            viewHolder.getImageView().setVisibility(View.VISIBLE);
            setLogoImage(viewHolder, card);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void setLogoImage(ViewHolder viewHolder, Card card) {
        ImageView imageView = viewHolder.getImageView();
        Bitmap bitmap = getBitmapFromInternalStorage(card, imageView.getContext());
        imageView.setImageBitmap(bitmap);
    }

    private Bitmap getBitmapFromInternalStorage(Card card, Context context) {
        FileInputStream fileInputStream;
        Bitmap bitmap = null;
        try {
            fileInputStream = context.openFileInput(card.getLogoPath());
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
