package com.example.kuba.tvmanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuba.tvmanager.Account;
import com.example.kuba.tvmanager.Episode;
import com.example.kuba.tvmanager.Mappers.AccountMapper;
import com.example.kuba.tvmanager.Mappers.EpisodeMapper;
import com.example.kuba.tvmanager.Mappers.FavouriteMapper;
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
    private ArrayList<Episode> sEpisodes;
    private ArrayList<String> numberOfSeasons;
    private Button buttonAddFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        buttonAddFavourite = (Button)findViewById(R.id.buttonAddFavourite);
        episodes = EpisodeMapper.getEpisodes(this);
        sEpisodes = new ArrayList<>();
        numberOfSeasons = new ArrayList<>();


        seasonList = (ListView)findViewById(R.id.seasonList);
        CustomAdapter2 adapter = new CustomAdapter2();
        seasonList.setAdapter(adapter);
        textMessage = (TextView)findViewById(R.id.textMessage);
        textScore = (TextView)findViewById(R.id.textScore);
        final Bundle extras = getIntent().getExtras();
        if(extras != null){
            String value = extras.getString("showId");
            show = TVShowMapper.selectShow(this, Integer.valueOf(value));
            for(int i = 0; i < episodes.size(); i++){
                if(episodes.get(i).getShowId().getId().equals(value)){
                    sEpisodes.add(episodes.get(i));
                }
            }
        }

        if(sEpisodes.size() > 0){
            numberOfSeasons.add(sEpisodes.get(0).getSeason());
            boolean add = true;
            for(int i = 1; i < sEpisodes.size(); i++){
                for(String s : numberOfSeasons){
                    if(s.equals(sEpisodes.get(i).getSeason())){
                        add = false;
                    }
                }
                if(add){
                    numberOfSeasons.add(sEpisodes.get(i).getSeason());
                }
                add = true;
            }
        }

        buttonAddFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String accountId = extras.getString("accountId");
                String showId = extras.getString("showId");

                Account account = AccountMapper.selectAccount(getApplicationContext(), Integer.valueOf(accountId));
                TVShow show = TVShowMapper.selectShow(getApplicationContext(), Integer.valueOf(showId));


                Toast.makeText(ShowDetailActivity.this, account.getLogin() + " " + show.getName() , Toast.LENGTH_SHORT).show();
                FavouriteMapper.add(getApplicationContext(), account, show);
            }
        });



        textMessage.setText(show.getName());
        textScore.setText(String.valueOf(show.getScore()));
        Toast.makeText(this, String.valueOf(episodes.size()), Toast.LENGTH_SHORT).show();
    }

    class CustomAdapter2 extends BaseAdapter{

        @Override
        public int getCount() {
            //return episodes.size();
            return numberOfSeasons.size();
        }

        @Override
        public Object getItem(int position) {
            //return episodes.get(position);
            return numberOfSeasons.get(position);
        }

        @Override
        public long getItemId(int position) {
            //return Long.valueOf(episodes.get(position).getId());
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_season, null);
            //TextView textViewName = (TextView)convertView.findViewById(R.id.textViewName);
            CheckBox seasonCheck = (CheckBox)convertView.findViewById(R.id.boxSeason);
            //seasonCheck.setText(episodes.get(position).getName());
            seasonCheck.setText("Season " + numberOfSeasons.get(position));
            return convertView;
        }
    }
}
