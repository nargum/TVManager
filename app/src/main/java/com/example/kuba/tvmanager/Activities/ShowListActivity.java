package com.example.kuba.tvmanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuba.tvmanager.Mappers.TVShowMapper;
import com.example.kuba.tvmanager.R;
import com.example.kuba.tvmanager.TVShow;

import java.util.ArrayList;

public class ShowListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<TVShow> shows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        shows = TVShowMapper.getShows(this);

        listView = (ListView)findViewById(R.id.listWiev);
        CustomAdapter adapter = new CustomAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Intent intent = new Intent(ShowListActivity.this, ShowDetailActivity.class);
                intent.putExtra("showId", id);
                startActivity(intent);*/
                Toast.makeText(ShowListActivity.this, "You clicked on " + shows.get(position).getName(), Toast.LENGTH_SHORT).show();
                TVShow show = shows.get(position);
                Intent intent = new Intent(ShowListActivity.this, ShowDetailActivity.class);
                intent.putExtra("showId", show.getId());
                startActivity(intent);
            }
        });
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return shows.size();
        }

        @Override
        public Object getItem(int position) {
            return shows.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Long.valueOf(shows.get(position).getId());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_layout, null);
            TextView textViewName = (TextView)convertView.findViewById(R.id.textViewName);
            textViewName.setText(shows.get(position).getName());
            return convertView;
        }
    }
}
