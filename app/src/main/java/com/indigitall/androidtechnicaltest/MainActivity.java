package com.indigitall.androidtechnicaltest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.indigitall.androidtechnicaltest.models.Character;
import com.indigitall.androidtechnicaltest.models.Characters;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import interfaces.PostService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ArrayList<Character> data = new ArrayList<>();
    GenericAdapter<Character> adapter;

    Integer page = 1;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        adapter = new GenericAdapter<Character>(this, data) {
            @Override
            public View getDataRow(int position, View convertView, ViewGroup parent) {
                View row = null;
                final Character item = dataList.get(position);

                // TODO: Fill row and add an OnClickListener
                //  --> Inflate View with the item.xml layout
                //  --> Fill View with the Character data
                //  --> Set View.OnClickListener
                //  --> Show and AlertDialog when click row. You can use `showDetail() function

                View view = mActivity.getLayoutInflater().inflate(R.layout.item, parent, false);
                TextView tvName = view.findViewById(R.id.item_name);
                tvName.setText( dataList.get(position).getName());

                ImageView itemImage = view.findViewById(R.id.item_image);
                String imageUrl = dataList.get(position).getImage();
                // Use Glide to load the image from the URL
                Glide.with(mActivity)
                        .load(imageUrl)
                        .into(itemImage);

                LinearLayout linearLayout = view.findViewById(R.id.item_LinearLayout);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pos = String.valueOf(position);
                        //Toast.makeText(MainActivity.this, pos, Toast.LENGTH_SHORT).show();
                        showDetail(dataList.get(position));
                    }
                });

                return view;
            }
        };

        listView.setAdapter(adapter);
        // Attach the listener to the AdapterView onCreate
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadNextDataFromApi();
                return true;
            }
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Firebase", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("token", token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi() {
        // TODO: Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`

        //Log.v("getPosts","getPosts");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostService postService = retrofit.create(PostService.class);
        Call<Characters> call = postService.getPost(page);

        call.enqueue(new Callback<Characters>() {
            @Override
            public void onResponse(Call<Characters> call, Response<Characters> response) {
                try {
                    //Log.v("onResponse", response.body().toString());
                    if (response.isSuccessful()){
                        Log.v("count", response.body().info.count .toString());
                        Log.v("isSuccessful", response.body().results.toString());

                        adapter.setServerListSize(response.body().info.count);

                        for(Character character : response.body().results) {
                            //Log.d("character", character.getName());
                            data.add(character);
                        }

                        Toast.makeText(MainActivity.this,"Showing page "+page,Toast.LENGTH_SHORT).show();
                        //Log.d("data", data.toString());
                        page = page + 1;
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this,ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Characters> call, Throwable t) {
                Log.e("getPosts",t.getMessage());
                Toast.makeText(MainActivity.this,"Error de conexión",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDetail(Character item) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

        // TODO: Show AlertDialog with a custom dialogView
        //  --> Inflate View with the item_detail.xml layout
        //  --> Set View in dialogBuilder
        //  --> Fill View with the Character data

        LayoutInflater inflater = this.getLayoutInflater();
        View itemDetail = inflater.inflate(R.layout.item_detail, null);
        dialogBuilder.setView(itemDetail);

        TextView detailName = itemDetail.findViewById(R.id.name);
        detailName.setText( item.getName());

        ImageView detailImage = itemDetail.findViewById(R.id.image);
        String imageUrl = item.getImage();

        // Use Glide to load the image from the URL
        Glide.with(this)
                .load(imageUrl)
                .into(detailImage);

        TextView detailStatus = itemDetail.findViewById(R.id.status);
        TextView detailSpecies = itemDetail.findViewById(R.id.species);
        TextView detailType = itemDetail.findViewById(R.id.type);
        TextView detailGender = itemDetail.findViewById(R.id.gender);
        detailStatus.setText( item.getStatus());
        detailSpecies.setText( item.getSpecies());
        detailType.setText( item.getType());
        detailGender.setText( item.getGender());

        dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
            // send data from the AlertDialog to the Activity
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.test_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("Token FCM");
        dialogBuilder.setMessage(token);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        return super.onOptionsItemSelected(item);
    }
}

