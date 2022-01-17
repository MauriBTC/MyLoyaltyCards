package com.example.myloyaltycards.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myloyaltycards.R;
import com.example.myloyaltycards.model.CardsManager;
import com.example.myloyaltycards.model.Card;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.FileInputStream;

public class CardDetailActivity extends AppCompatActivity {
    private TextView detailCompanyNameTextView;
    private TextView detailNameSurnameTextView;
    private TextView detailClientIDTextView;
    private ImageView detailLogoImageView;
    private Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_detail_activity);
        int id = getIntent().getIntExtra("id", -1);
        if(id != -1)
            init(id);
        else
            Log.e("ERROR", "Error retrieving data");
    }

    private void init(int id) {
        detailCompanyNameTextView = findViewById(R.id.detail_company_name_text_view);
        detailNameSurnameTextView = findViewById(R.id.detail_name_surname_text_view);
        detailClientIDTextView = findViewById(R.id.detail_client_id_text_view);
        detailLogoImageView = findViewById(R.id.detail_logo_image_view);
        observeData(id);
    }

    private void observeData(int id) {
        CardsManager.getInstance(this).getCard(id).observe(this, new Observer<Card>() {
            @Override
            public void onChanged(Card card) {
                if(card != null) {
                    CardDetailActivity.this.card = card;
                    CardView detailCardView = findViewById(R.id.detail_card_view);
                    detailCardView.setCardBackgroundColor(card.getColor());
                    detailCompanyNameTextView.setText(card.getCompanyName());
                    String nameSurname = card.getOwnerName() + " " + card.getOwnerSurname();
                    detailNameSurnameTextView.setText(nameSurname);
                    detailClientIDTextView.setText(card.getClientID());
                    Bitmap bitmap = getBitmapFromInternalStorage(card, CardDetailActivity.this);
                    detailLogoImageView.setImageBitmap(bitmap);
                    printCode(card.getClientID(), card.getFormatCode());
                }
            }
        });
    }

    private void printCode(String clientID, int formatCode) {
        BarcodeFormat barcodeFormat = BarcodeFormat.QR_CODE;
        int codeWidth = 400, codeHeight = 400;
        switch(formatCode) {
            case R.id.qrcode:
                barcodeFormat = BarcodeFormat.QR_CODE;
                break;
            case R.id.barcode:
                barcodeFormat = BarcodeFormat.CODE_128;
                codeWidth = codeHeight * 2;
                break;
        }
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bm = barcodeEncoder.encodeBitmap(clientID, barcodeFormat,
                    codeWidth, codeHeight);
            ImageView codeImageView = findViewById(R.id.code_image_view);
            codeImageView.setImageBitmap(bm);
        } catch(Exception e) {
            Log.e("ERROR", "Error encoding QR code / barcode");
        }
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

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.check_delete_card_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("deletedCardID", card.getUid());
                        setResult(RESULT_OK, resultIntent);
                        CardDetailActivity.this.finish();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                delete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}