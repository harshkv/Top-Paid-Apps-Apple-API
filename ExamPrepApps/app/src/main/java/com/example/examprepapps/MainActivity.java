package com.example.examprepapps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Appinfo> appList;
    ProgressBar progressBar;
    static ArrayList<Appinfo> selectedList;
    private RecyclerView recyclerView;
    static RecyclerView.Adapter mAdapter;
    static AppInfoAdapter adapter;
    ImageButton ib_refresh;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Top Paid Apps on Apple store");
        selectedList = new ArrayList<>();
        ib_refresh = (ImageButton) findViewById(R.id.ib_refresh);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        ib_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedList.removeAll(selectedList);
                mAdapter.notifyDataSetChanged();
            }
        });

        if (isConnected() == true) {
            new GetDataAsync().execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");
        } else {
            Toast.makeText(MainActivity.this, "Not connected", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || (networkInfo.getType() != connectivityManager.TYPE_WIFI &&
                networkInfo.getType() != connectivityManager.TYPE_MOBILE)) {
            return false;
        }

        return true;
    }


    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<Appinfo>> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            appList = new ArrayList<>();

        }


        @Override
        protected ArrayList<Appinfo> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            String json = null;


            try {
                URL urls = new URL(strings[0]);
                connection = (HttpURLConnection) urls.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    json = IOUtils.toString(connection.getInputStream(), "utf-8");
                    JSONObject root = new JSONObject(json);
                    JSONObject feed = root.getJSONObject("feed");
                    JSONArray entry = feed.getJSONArray("entry");
                    for (int i = 0; i < entry.length(); i++) {
                        Appinfo details = new Appinfo();
                        JSONObject entryJSON = entry.getJSONObject(i);
                        JSONObject nameJSON = entryJSON.getJSONObject("im:name");
                        details.name = nameJSON.getString("label");
                        JSONObject priceJSON = entryJSON.getJSONObject("im:price");
                        details.price = priceJSON.getString("label");
                        JSONArray imageArray = entryJSON.getJSONArray("im:image");
                        JSONObject imageJSON = imageArray.getJSONObject(2);
                        details.images = imageJSON.getString("label");
                        appList.add(details);

                    }


                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return appList;
        }


        @Override
        protected void onPostExecute(final ArrayList<Appinfo> applist) {
            progressBar.setVisibility(View.INVISIBLE);
             adapter = new AppInfoAdapter(MainActivity.this, R.layout.each_item, applist);
            final ListView listView = (ListView) findViewById(R.id.listview);
            listView.setAdapter(adapter);
            recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);

            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new AppInforFilterAdapter(selectedList);
            recyclerView.setAdapter(mAdapter);
//            adapter.notifyDataSetChanged();


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("demo", "selected item  " + applist.get(position));
                        selectedList.add(applist.get(position));
                    applist.remove(applist.get(position));
                        mAdapter.notifyDataSetChanged();
                            adapter.notifyDataSetChanged();
                    return false;
                }
            });

        }
    }
}

