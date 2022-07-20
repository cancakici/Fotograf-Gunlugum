package can.cakici.diary_photo.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import can.cakici.diary_photo.R;
import can.cakici.diary_photo.databinding.ActivityDetailsAddBinding;
import can.cakici.diary_photo.model.Place;
import can.cakici.diary_photo.roomdb.PlaceDao;
import can.cakici.diary_photo.roomdb.PlaceDatabase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class details_add_activity extends AppCompatActivity {

    private ActivityDetailsAddBinding binding;
    Bitmap selectedImage;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> stringActivityResultLauncher;
    double longitude,latiude;
    PlaceDatabase placeDatabase;
    PlaceDao placeDao;
    SharedPreferences sharedPreferences;
    String titlememory,notememory,imageMemory,img_str;
    int dayMemory,mounthMemory,yearMemory,yearr,mounthh,day;
    byte[] set_image_byte;
    private final CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailsAddBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        registerLauncher();

        Calendar calendar=Calendar.getInstance();
        yearr=calendar.get(Calendar.YEAR);
        mounthh=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DATE);

        Intent intent=getIntent();
        longitude = intent.getDoubleExtra("longitude",0);
        latiude = intent.getDoubleExtra("latiude",0);

        placeDatabase= Room.databaseBuilder(getApplicationContext(),PlaceDatabase.class,"Places").build();
        placeDao=placeDatabase.placeDao();

        sharedPreferences =details_add_activity.this.getSharedPreferences("can.cakici.diary_photo.view",MODE_PRIVATE);

        dayMemory=sharedPreferences.getInt("dayShared",0);
        mounthMemory=sharedPreferences.getInt("mounthShared",0);
        yearMemory=sharedPreferences.getInt("yearShared",0);
        notememory=sharedPreferences.getString("noteShared","none");
        titlememory=sharedPreferences.getString("titleShared","none");
        imageMemory=sharedPreferences.getString("imageShared","none");

        if(titlememory!="none"){
             binding.addTitleEditText.setText(""+titlememory);

         }else{} if(notememory!="none"){
            binding.addNoteEditText.setText(""+notememory);

         }else{}
        if(yearMemory!=0||mounthMemory!=0||dayMemory!=0){
            binding.addCalendar.setText(String.valueOf(dayMemory)+"/"+String.valueOf(mounthMemory)+"/"+String.valueOf(yearMemory));
        }else{
            binding.addCalendar.setText(String.valueOf(day)+"/"+String.valueOf(mounthh)+"/"+String.valueOf(yearr));
        }
        if(!imageMemory.equals("none")){
                byte[] imageAsBytes = Base64.decode(imageMemory.getBytes(), Base64.DEFAULT);
                binding.addImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
         }else{
            sharedPreferences.edit().putInt("dayShared",day).apply();
            sharedPreferences.edit().putInt("mounthShared",mounthh).apply();
            sharedPreferences.edit().putInt("yearShared",yearr).apply();
            binding.addImage.setImageResource(R.drawable.add_image_icon);
        }

    }
    public void add_calendar(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(details_add_activity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                yearr=i;
                mounthh=i1;
                day=i2;
                sharedPreferences.edit().putInt("dayShared",day).apply();
                sharedPreferences.edit().putInt("mounthShared",mounthh).apply();
                sharedPreferences.edit().putInt("yearShared",yearr).apply();
                binding.addCalendar.setText(day+"/"+mounthh+"/"+yearr);
            }
        }, yearr,mounthh,day);
        datePickerDialog.setTitle("Takvim");
        datePickerDialog.show();


    }
    public  void  add_map(View view){
        if (internetErisim())
        {
            if (set_image_byte!=null) {
                img_str = Base64.encodeToString(set_image_byte, 0);
            }else {
                if (imageMemory!="none") {
                    img_str=imageMemory;
                }
            }
            sharedPreferences.edit().putString("titleShared",binding.addTitleEditText.getText().toString()).apply();
            sharedPreferences.edit().putString("imageShared",img_str).apply();
            sharedPreferences.edit().putString("noteShared",binding.addNoteEditText.getText().toString()).apply();
            Intent intent =new Intent(details_add_activity.this, MapsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
        else {
            Snackbar.make(view,"İnternet erişimi sağlanamadı !",Snackbar.LENGTH_LONG).show();
        }


    }
    public void selectImage(View view) {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){//Burada izin verilmemiş  ise ne yapılacağı kısımdır.

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){//Eğer izin istenip verilmediyse ve tekrardan izin istemek yerine kullanıcıya snackbar olarak tekrardan izin isteriz.

                Snackbar.make(view,"İzin vermek istermisiniz ?",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        stringActivityResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);//galeriye gidecektir.

                    }
                }).show();

            }else{//galeriye git
                stringActivityResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);//izin verilemedi çıkar.
            }
        }else{//Burada izin verilmiş  ise galeriye gidecektir.

            stringActivityResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }


    }
    public void registerLauncher(){


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {//Eğer galeriye gidildiyse.
            @Override
            public void onActivityResult(ActivityResult result) {

                if(result.getResultCode()==Activity.RESULT_OK){//eğer veri seçilmiş ise.
                    Intent intentFromResult=result.getData();
                    if (intentFromResult!=null){//aldığımız veri boş olup olmadığını kontrol ediyoruz.
                        Uri imageData=intentFromResult.getData();

                        try {

                            if (Build.VERSION.SDK_INT >= 28) {
                                ImageDecoder.Source source=ImageDecoder.createSource(details_add_activity.this.getContentResolver(),imageData);
                                selectedImage =ImageDecoder.decodeBitmap(source);

                                Bitmap setImage = small_image(selectedImage,600);
                                ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
                                setImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
                                 set_image_byte = outputStream.toByteArray();

                                if (set_image_byte!=null) {
                                    img_str = Base64.encodeToString(set_image_byte, 0);
                                }else {
                                    if (imageMemory!="none") {
                                        img_str=imageMemory;
                                    }

                                }
                                sharedPreferences.edit().putString("imageShared",img_str).apply();
                                imageMemory=sharedPreferences.getString("imageShared","none");

                                binding.addImage.setImageBitmap(selectedImage);

                            } else {

                                selectedImage=MediaStore.Images.Media.getBitmap(details_add_activity.this.getContentResolver(),imageData);

                                Bitmap setImage = small_image(selectedImage,600);
                                ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
                                setImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
                                set_image_byte = outputStream.toByteArray();

                                if (set_image_byte!=null) {
                                    img_str = Base64.encodeToString(set_image_byte, 0);
                                }else {
                                    if (imageMemory!="none") {
                                        img_str=imageMemory;
                                    }

                                }
                                sharedPreferences.edit().putString("imageShared",img_str).apply();
                                imageMemory=sharedPreferences.getString("imageShared","none");

                                binding.addImage.setImageBitmap(selectedImage);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            }
        });


        stringActivityResultLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {

                if (result) {

                    Intent intentToGalery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGalery);
                }else{

                    Toast.makeText(details_add_activity.this,"İzin verilmedi !",Toast.LENGTH_LONG).show();

                }

            }
        });



    }
    public Bitmap small_image(Bitmap image, int maximumSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maximumSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image,width,height,true);
    }
    public void save_button(View view){
        byte[] imageAsBytes = Base64.decode(imageMemory.getBytes(), Base64.DEFAULT);

        String setTitle =binding.addTitleEditText.getText().toString();
        String setNote=binding.addNoteEditText.getText().toString();

        if (imageMemory.equals("none")){

            Toast.makeText(details_add_activity.this,"Resim seçiniz !",Toast.LENGTH_LONG).show();
        }else if(latiude==0.0||longitude==0.0){
            Toast.makeText(details_add_activity.this,"Haritada konum seçimi yapınız !",Toast.LENGTH_LONG).show();

        }else if(setTitle.equals("")){
            Toast.makeText(details_add_activity.this,"Başlık girniz !",Toast.LENGTH_LONG).show();


        }else if (setNote.equals("")) {
            Toast.makeText(details_add_activity.this,"Not girniz !",Toast.LENGTH_LONG).show();

        } else{

            if (imageMemory!=null){
                imageAsBytes = Base64.decode(imageMemory.getBytes(), Base64.DEFAULT);
            }
            Place place=new Place(imageAsBytes,setTitle,dayMemory,mounthMemory,yearMemory,setNote,latiude,longitude);
            compositeDisposable.add(placeDao.insert(place).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(details_add_activity.this::handleResponsee));
        }


    }
    private void handleResponsee() {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);

    }
    public void back_button(View view){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();

    }
    private boolean internetErisim() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}