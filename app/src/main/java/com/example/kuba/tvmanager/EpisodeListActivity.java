package com.example.kuba.tvmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kuba.tvmanager.Activities.ShowDetailActivity;
import com.example.kuba.tvmanager.Mappers.EpisodeMapper;

import java.util.ArrayList;
import java.util.Iterator;

public class EpisodeListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Episode> episodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_list);

        listView = (ListView)findViewById(R.id.listWiev);
        episodes = filterEpisodes();

        CustomAdapter adapter = new CustomAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private ArrayList<Episode> filterEpisodes(){
        ArrayList<Episode> episodes = EpisodeMapper.getEpisodes(this);
        Bundle extra = getIntent().getExtras();
        String showId = extra.getString("showId");
        //String accountId = extra.getString("accountId");
        String season = extra.getString("season");

        Iterator<Episode> iterator = episodes.iterator();
        while(iterator.hasNext()){
            Episode e = iterator.next();
            if(!(e.getShowId().getId().equals(showId) && e.getSeason().equals(season))){
                iterator.remove();
            }
        }


        return episodes;
    }

    class CustomAdapter extends BaseAdapter {

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
            //CheckBox checkEpisode = (CheckBox)convertView.findViewById(R.id.checkEpisode);
            final CheckedTextView seasonCheck = (CheckedTextView) convertView.findViewById(R.id.simpleCheckedTextView);
            seasonCheck.setText(episodes.get(position).getEpisodeNumber() + ". " + episodes.get(position).getName());
            seasonCheck.setCheckMarkDrawable(null);
            seasonCheck.setChecked(false);

            seasonCheck.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(!seasonCheck.isChecked()){
                        seasonCheck.setChecked(true);
                        seasonCheck.setCheckMarkDrawable(R.drawable.checked);
                    }else{
                        seasonCheck.setChecked(false);
                        seasonCheck.setCheckMarkDrawable(null);
                    }
                }
            });


            return convertView;
        }
    }
}
