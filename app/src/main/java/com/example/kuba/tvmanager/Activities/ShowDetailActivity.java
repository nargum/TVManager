package com.example.kuba.tvmanager.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuba.tvmanager.Episode;
import com.example.kuba.tvmanager.Mappers.EpisodeMapper;
import com.example.kuba.tvmanager.Mappers.TVShowMapper;
import com.example.kuba.tvmanager.R;
import com.example.kuba.tvmanager.TVShow;

import java.util.ArrayList;

public class ShowDetailActivity extends AppCompatActivity {
    private TextView textMessage;
    private TextView textScore;
    private TVShow show;
    private ListView seasonList;
    private ArrayList<Episode> episodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        episodes = EpisodeMapper.getEpisodes(this);


        seasonList = (ListView)findViewById(R.id.seasonList);
        CustomAdapter2 adapter = new CustomAdapter2();
        seasonList.setAdapter(adapter);
        textMessage = (TextView)findViewById(R.id.textMessage);
        textScore = (TextView)findViewById(R.id.textScore);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String value = extras.getString("showId");
            show = TVShowMapper.selectShow(this, Integer.valueOf(value));
        }

        textMessage.setText(show.getName());
        textScore.setText(String.valueOf(show.getScore()));
        Toast.makeText(this, String.valueOf(episodes.size()), Toast.LENGTH_SHORT).show();
    }

    class CustomAdapter2 extends BaseAdapter{

        @Override
        public int getCount() {
            return episodes.size();
        }

        @Override
        public Object getItem(int position) {
            return episodes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Long.valueOf(episodes.get(position).getId());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_season, null);
            //TextView textViewName = (TextView)convertView.findViewById(R.id.textViewName);
            CheckBox seasonCheck = (CheckBox)convertView.findViewById(R.id.boxSeason);
            seasonCheck.setText(episodes.get(position).getName());
            return convertView;
        }
    }
}
