package com.indigitall.androidtechnicaltest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.indigitall.androidtechnicaltest.models.Character;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ArrayList<Characters> data = new ArrayList<>();
    GenericAdapter<Character> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (RelativeLayout) findViewById(R.id.listView);

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

                return row;
            }
        };

        listView.setAdapter(adapter)
        // Attach the listener to the AdapterView onCreate
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadNextDataFromApi();
                return true;
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
