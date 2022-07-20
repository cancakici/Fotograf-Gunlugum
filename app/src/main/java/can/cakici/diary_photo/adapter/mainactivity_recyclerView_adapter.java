package can.cakici.diary_photo.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

import can.cakici.diary_photo.databinding.RecyclearviewRowBinding;
import can.cakici.diary_photo.model.Place;
import can.cakici.diary_photo.roomdb.PlaceDao;
import can.cakici.diary_photo.roomdb.PlaceDatabase;
import can.cakici.diary_photo.view.MainActivity;
import can.cakici.diary_photo.view.details_add_view_activity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class mainactivity_recyclerView_adapter extends RecyclerView.Adapter<mainactivity_recyclerView_adapter.mainactivity_recyclerView_adapter_holder> {

    List<Place> placeList;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public mainactivity_recyclerView_adapter(List<Place> placeList) {
        this.placeList = placeList;
    }
    @NonNull
    @Override
    public mainactivity_recyclerView_adapter_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclearviewRowBinding recyclearviewRowBinding = RecyclearviewRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new mainactivity_recyclerView_adapter_holder(recyclearviewRowBinding);
    }
    @Override
    public void onBindViewHolder(@NonNull mainactivity_recyclerView_adapter_holder holder, @SuppressLint("RecyclerView") int position) {

        if (placeList.get(position).baslik.length()>18){
            holder.binding.titleView.setText(placeList.get(position).baslik.substring(0,17)+"...");
        }else{
            holder.binding.titleView.setText(placeList.get(position).baslik);
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(placeList.get(position).image,0,placeList.get(position).image.length);
        holder.binding.setImage.setImageBitmap(bitmap);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(holder.itemView.getContext(), details_add_view_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("sifre",placeList.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.binding.floatingDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceDatabase db = Room.databaseBuilder(holder.itemView.getContext(),
                        PlaceDatabase.class, "Places")
                        .build();

                PlaceDao placeDao = db.placeDao();

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Uyarı");
                builder.setMessage("Seçilen veri silinecek eminmisiniz ?");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDisposable.add(placeDao.delete(placeList.get(position))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe());

                        Intent intent =new Intent(holder.itemView.getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        holder.itemView.getContext().startActivity(intent);

                    }
                });
                builder.show();



            }
        });
    }
    @Override
    public int getItemCount() {
        return placeList.size();
    }
    public class mainactivity_recyclerView_adapter_holder extends RecyclerView.ViewHolder {

        private RecyclearviewRowBinding binding;

        public mainactivity_recyclerView_adapter_holder(RecyclearviewRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }




}