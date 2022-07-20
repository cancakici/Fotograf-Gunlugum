package can.cakici.diary_photo.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;
import java.util.List;

import can.cakici.diary_photo.R;
import can.cakici.diary_photo.adapter.mainactivity_recyclerView_adapter;
import can.cakici.diary_photo.databinding.ActivityMainBinding;
import can.cakici.diary_photo.model.Place;
import can.cakici.diary_photo.roomdb.PlaceDao;
import can.cakici.diary_photo.roomdb.PlaceDatabase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    SharedPreferences sharedPreferences;
    PlaceDatabase db;
    PlaceDao placeDao;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);



        sharedPreferences =MainActivity.this.getSharedPreferences("can.cakici.diary_photo.view",MODE_PRIVATE);

        db = Room.databaseBuilder(getApplicationContext(),
                PlaceDatabase.class, "Places")
                //.allowMainThreadQueries()
                .build();

        placeDao = db.placeDao();

        mDisposable.add(placeDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse));
    }
    private void handleResponse(List<Place> placeList) {
        mainactivity_recyclerView_adapter mainactivity_recyclerView_adapter=new mainactivity_recyclerView_adapter(placeList);
        binding.rectclearView.setLayoutManager(new LinearLayoutManager(this));
        binding.rectclearView.setAdapter(mainactivity_recyclerView_adapter);
    }

    public void floatingActionButtonClick(View view){
        sharedPreferences.edit().putString("titleShared","").apply();
        sharedPreferences.edit().putString("imageShared","none").apply();
        sharedPreferences.edit().putInt("dayShared",0).apply();
        sharedPreferences.edit().putInt("mounthShared",0).apply();
        sharedPreferences.edit().putInt("yearShared",0).apply();
        sharedPreferences.edit().putString("noteShared","").apply();
        Intent intent=new Intent(MainActivity.this, details_add_activity.class);
        startActivity(intent);
    }
}