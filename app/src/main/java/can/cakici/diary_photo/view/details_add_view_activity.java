package can.cakici.diary_photo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.snackbar.Snackbar;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import can.cakici.diary_photo.R;
import can.cakici.diary_photo.databinding.ActivityDetailsAddViewBinding;
import can.cakici.diary_photo.model.Place;

public class details_add_view_activity extends AppCompatActivity {

    Place geldi;
    private ActivityDetailsAddViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailsAddViewBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);



        binding.backgraundText.setVisibility(View.INVISIBLE);
        binding.closeActionButton.setVisibility(View.INVISIBLE);
        binding.not.setVisibility(View.INVISIBLE);

        Intent intent=getIntent();
        geldi = (Place) intent.getSerializableExtra("sifre");

       binding.not.setText(geldi.note);
       binding.mainPicture.setImageBitmap(BitmapFactory.decodeByteArray(geldi.image, 0, geldi.image.length));
    }
    public void note_add_open(View view){

        binding.not.setVisibility(View.VISIBLE);
        binding.backgraundText.setVisibility(View.VISIBLE);
        binding.closeActionButton.setVisibility(View.VISIBLE);
        binding.noteView.setEnabled(false);
        binding.calendar.setEnabled(false);
        binding.mapVieww.setEnabled(false);

    }
    public void calendar_view(View view){

        DatePickerDialog datePickerDialog = new DatePickerDialog(details_add_view_activity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

            }
        }, geldi.year, geldi.mounth, geldi.day);
        datePickerDialog.setTitle("Takvim");
        datePickerDialog.show();


    }
    public void close_button(View view){

        binding.backgraundText.setVisibility(View.INVISIBLE);
        binding.closeActionButton.setVisibility(View.INVISIBLE);
        binding.not.setVisibility(View.INVISIBLE);

        binding.noteView.setEnabled(true);
        binding.calendar.setEnabled(true);
        binding.mapVieww.setEnabled(true);

    }
    public void back_button(View view){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
        finish();
    }
    public void map_viewwv (View view){
        if (internetErisim()){
        Intent myIntent = new Intent(getApplicationContext(), MapsActivity.class);
        myIntent.putExtra("view","view");
        myIntent.putExtra("sifree",geldi);
        startActivity(myIntent); }
        else{
            Snackbar.make(view,"İnternet erişimi sağlanamadı !",Snackbar.LENGTH_LONG).show();
        }

    }
    private boolean internetErisim() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



}