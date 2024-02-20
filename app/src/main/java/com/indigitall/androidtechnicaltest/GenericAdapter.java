package com.indigitall.androidtechnicaltest;

/**
 * Created by indigitall.
 */

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indigitall.androidtechnicaltest.R;
import com.indigitall.androidtechnicaltest.models.Character;
import com.indigitall.androidtechnicaltest.models.Characters;

import java.util.List;

/**
 *  A child class shall subclass this Adapter and
 *  implement method getDataRow(int position, View convertView, ViewGroup parent),
 *  which supplies a View present data in a ListRow.
 *
 *  This parent Adapter takes care of displaying ProgressBar in a row or
 *  indicating that it has reached the last row.
 *
 */
public class GenericAdapter extends BaseAdapter {

    // the main data list to save loaded data
    protected List<Character> dataList;

    protected Activity mActivity;

    // the serverListSize is the total number of items on the server side,
    // which should be returned from the web request results
    protected int serverListSize = -1;

    // Two view types which will be used to determine whether a row should be displaying
    // data or a Progressbar
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_ACTIVITY = 1;


    public GenericAdapter(Activity activity, List<Character> list) {
        mActivity = activity;
        dataList = list;
    }


    public void setServerListSize(int serverListSize){
        this.serverListSize = serverListSize;
    }


    /**
     * disable click events on indicating rows
     */
    @Override
    public boolean isEnabled(int position) {

        return getItemViewType(position) == VIEW_TYPE_ACTIVITY;
    }

    /**
     * One type is normal data row, the other type is Progressbar
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }


    /**
     * the size of the List plus one, the one is the last row, which displays a Progressbar
     */
    @Override
    public int getCount() {
        return dataList.size();
    }


    /**
     * return the type of the row,
     * the last row indicates the user that the ListView is loading more data
     */
    @Override
    public int getItemViewType(int position) {
        return (position >= dataList.size()) ? VIEW_TYPE_LOADING
                : VIEW_TYPE_ACTIVITY;
    }

    @Override
    public Character getItem(int position) {
        return dataList.get(position); //(getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? dataList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position; //(getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? position: -1;
    }

    /**
     *  returns the correct view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        /*if (getItemViewType(position) == VIEW_TYPE_LOADING) {
            // display the last row
            return getFooterView(position, convertView, parent);
        }
        View dataRow = convertView;
        dataRow = getDataRow(position, convertView, parent);*/

        View view = mActivity.getLayoutInflater().inflate(R.layout.item, parent, false);
        LinearLayout linearLayout = view.findViewById(R.id.item_LinearLayout);
        TextView tvName = view.findViewById(R.id.item_name);

        tvName.setText( dataList.get(position).getName());

        return view;
    };

    /**
     * A subclass should override this method to supply the data row.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getDataRow(int position, View convertView, ViewGroup parent) {
        View row = null;
        final Character item = dataList.get(position);

        // TODO: Fill row and add an OnClickListener
        //  --> Inflate View with the item.xml layout
        //  --> Fill View with the Character data
        //  --> Set View.OnClickListener
        //  --> Show and AlertDialog when click row. You can use `showDetail() function

        return convertView;
    }

    /**
     * returns the dataList
     * @return
     */
    public List<Character> getData() {
        return dataList;
    }

    /**
     * returns a View to be displayed in the last row.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getFooterView(int position, View convertView, ViewGroup parent) {
        if (position >= serverListSize && serverListSize > 0) {
            // the ListView has reached the last row
            TextView tvLastRow = new TextView(mActivity);
            tvLastRow.setHint("Reached the last row.");
            tvLastRow.setGravity(Gravity.CENTER);
            return tvLastRow;
        }

        View row = convertView;
        if (row == null) {
            row = mActivity.getLayoutInflater().inflate(
                    R.layout.progress, parent, false);
        }

        return row;
    }

}