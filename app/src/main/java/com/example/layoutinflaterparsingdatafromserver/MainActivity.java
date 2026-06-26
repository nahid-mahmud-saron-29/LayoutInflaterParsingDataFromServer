package com.example.layoutinflaterparsingdatafromserver;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
        ListView listView;
        HashMap<String, String> hashMap;
        MyAdapter myAdapter = new MyAdapter();
        ArrayList <HashMap<String, String>> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = findViewById(R.id.listView);

        listView.setAdapter(myAdapter);

        dataSet();

    }//last_bracket_ffirst
    public class MyAdapter extends BaseAdapter{
        LayoutInflater layoutInflater;
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = layoutInflater.inflate(R.layout.layout,parent,false);
            ImageView item_image = myView.findViewById(R.id.item_image);
            TextView item_title = myView.findViewById(R.id.item_title);
            TextView item_description = myView.findViewById(R.id.item_description);

            HashMap<String, String> hashMap1 = arrayList.get(position);
            String img_St = hashMap1.get("IMG");
            Glide.with(MainActivity.this).load(img_St).into(item_image);

            String ttl_St = hashMap1.get("TTL");
            item_title.setText(ttl_St);

            String des_St = hashMap1.get("DES");
            item_description.setText(des_St);

            return myView;
        }
    }
    public void dataSet() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.0.112/getdata.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // 💡 ১. পুরো রেসপন্সটি একটি JSONArray (যেহেতু শুরু হয়েছে '[' দিয়ে)
                            JSONArray jsonArray = new JSONArray(response);

                            // 💡 ২. লুপ চালিয়ে প্রতিটা অবজেক্ট বের করা
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // 💡 ৩. পিএইচপি ফাইলের কি (Key) অনুযায়ী ডাটা রিসিভ করা
                                String img = jsonObject.getString("IMG");
                                String ttl = jsonObject.getString("TTL");
                                String des = jsonObject.getString("DES");

                                // 💡 ৪. ডাটা ম্যাপে পুশ করে arrayList এ অ্যাড করা
                                HashMap<String, String> map = new HashMap<>();
                                map.put("IMG", img);
                                map.put("TTL", ttl);
                                map.put("DES", des);
                                arrayList.add(map);
                            }

                            // 💡 ৫. ডাটা সফলভাবে যোগ হওয়ার পর অ্যাডাপ্টার রিফ্রেশ করা
                            myAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

}//last_bracket_second