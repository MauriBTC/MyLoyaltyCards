package com.example.myloyaltycards.activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.myloyaltycards.R;
import com.example.myloyaltycards.adapter.ColorSpinnerAdapter;
import com.example.myloyaltycards.model.CardsManager;
import com.example.myloyaltycards.model.Card;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddCardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText ownerNameEditText;
    private EditText ownerSurnameEditText;
    private EditText clientIDEditText;
    private EditText companyNameEditText;
    private String logoFileName;
    private String[] colorsText;
    private int[] colors;
    private int colorSelected;
    //necessario per il bottone "seleziona logo" che lancia l'activity della galleria
    private final ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    ImageView addCardLogoImageView = findViewById(R.id.add_card_logo_image_view);
                    addCardLogoImageView.setImageURI(uri);
                    saveImageIntoInternalStorage(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card_activity);
        init();
    }

    private void init() {
        ownerNameEditText = findViewById(R.id.owner_name_edit_text);
        ownerSurnameEditText = findViewById(R.id.owner_surname_edit_text);
        clientIDEditText = findViewById(R.id.client_id_edit_text);
        companyNameEditText = findViewById(R.id.company_name_edit_text);
        Button selectLogoButton = findViewById(R.id.select_logo_button);
        selectLogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input da passare al metodo launch per aprire la galleria, selezionata l'immagine viene attivata la onActivityResult
                getContent.launch("image/*");
            }
        });
        Spinner colorSpinner = findViewById(R.id.color_spinner);
        colorsText = getResources().getStringArray(R.array.colors_text);
        colors = new int[]{
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.red_spinner),
                ContextCompat.getColor(this, R.color.blue_spinner),
                ContextCompat.getColor(this, R.color.yellow_spinner),
                ContextCompat.getColor(this, R.color.green_spinner),
        };
        ColorSpinnerAdapter spinnerAdapter = new ColorSpinnerAdapter(this,
                R.layout.spinner_item_layout, R.id.color_text_spinner_item, colorsText, colors);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_layout);
        colorSpinner.setAdapter(spinnerAdapter);
        colorSpinner.setOnItemSelectedListener(this);
    }

    private void saveImageIntoInternalStorage(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] pathParts = uri.getPath().split("/");
        logoFileName = pathParts[pathParts.length-1];
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(logoFileName, Context.MODE_PRIVATE);
            compressBitmapToOutputStream(bitmap, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void compressBitmapToOutputStream(Bitmap bitmap, FileOutputStream fos) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void done() {
        String ownerName = ownerNameEditText.getText().toString();
        String ownerSurname = ownerSurnameEditText.getText().toString();
        String clientID= clientIDEditText.getText().toString();
        String companyName = companyNameEditText.getText().toString();
        RadioGroup radioGroup = findViewById(R.id.code_format_radio_group);
        int selectedFormatCode = radioGroup.getCheckedRadioButtonId();
        if(checkEditTexts(ownerName, ownerSurname, clientID, companyName)) {
            Card card = new Card(clientID,
                    companyName,
                    ownerName,
                    ownerSurname,
                    logoFileName,
                    selectedFormatCode,
                    colorSelected);
            addCardToDatabase(card); //room notifica il live data preso inizialmente
            finish();
        } else {
            showDialogMissingInformation();
        }
    }

    private boolean checkEditTexts(String... editTexts) {
        if(editTexts.length > 0) {
            for(String info : editTexts) {
                if(info == null || info.isEmpty())
                    return false;
            }
        }
        return true;
    }

    private void showDialogMissingInformation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.please_fill_all_fields_alert))
                .setCancelable(false)
                .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void addCardToDatabase(Card card) {
        CardsManager.getInstance(this).addCard(card);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                done();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        colorSelected = colors[pos];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        /* Empty */
    }
}