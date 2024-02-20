package com.indigitall.androidtechnicaltest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.indigitall.androidtechnicaltest.models.Character;
import com.indigitall.androidtechnicaltest.models.Characters;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import interfaces.PostService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ArrayList<Character> data = new ArrayList<>();
    GenericAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        data.add( new Character( "Rick"));
        //getPosts();

        adapter = new GenericAdapter(this, data);

        listView.setAdapter(adapter);
        // Attach the listener to the AdapterView onCreate
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                //loadNextDataFromApi();
                getPosts();
                return true;
            }
        });
    }

    private void getPosts() {
        Log.v("getPosts","getPosts");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostService postService = retrofit.create(PostService.class);
        Call<Characters> call = postService.getPost();

        call.enqueue(new Callback<Characters>() {
            @Override
            public void onResponse(Call<Characters> call, Response<Characters> response) {
                Log.v("onResponse", response.body().toString());
                Toast.makeText(MainActivity.this,response.body().toString(),Toast.LENGTH_SHORT);
                try {
                    if (response.isSuccessful()){
                        Log.v("isSuccessful", response.body().info.toString());
                        Log.v("isSuccessful", response.body().results.toString());
                        for(Character character : response.body().results) {
                            Log.d("character", character.getName());
                            //titles.add(character.getTitle());
                            data.add(character);
                        }

                        Log.d("data", data.toString());
                        //arrayAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this,ex.getMessage(),Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Characters> call, Throwable t) {
                Log.e("getPosts",t.getMessage());
                Toast.makeText(MainActivity.this,"Error de conexión",Toast.LENGTH_SHORT);
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
    }

    public void showDetail(Character item) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

        // TODO: Show AlertDialog with a custom dialogView
        //  --> Inflate View with the item_detail.xml layout
        //  --> Set View in dialogBuilder
        //  --> Fill View with the Character data

        AlertDialog alertDialog = dialogBuilder.create();
    }
}

