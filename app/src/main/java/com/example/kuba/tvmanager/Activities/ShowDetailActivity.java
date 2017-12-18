package com.example.kuba.tvmanager.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.example.kuba.tvmanager.Favourite;
import com.example.kuba.tvmanager.Mappers.AccountMapper;
import com.example.kuba.tvmanager.Mappers.EpisodeMapper;
import com.example.kuba.tvmanager.Mappers.FavouriteMapper;
import com.example.kuba.tvmanager.Mappers.TVShowMapper;
import com.example.kuba.tvmanager.R;
import com.example.kuba.tvmanager.TVShow;
import com.example.kuba.tvmanager.TabActivity;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class ShowDetailActivity extends AppCompatActivity {
    private TextView textMessage;
    private TextView textScore;
    private TVShow show;
    private ListView seasonList;
    private ArrayList<Episode> episodes;
    private ArrayList<Episode> sEpisodes;
    private ArrayList<String> numberOfSeasons;
    private Button buttonAddFavourite;
    private ArrayList<Favourite> favourites;
    private String showId;
    private String accountId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        buttonAddFavourite = (Button)findViewById(R.id.buttonAddFavourite);
        episodes = EpisodeMapper.getEpisodes(this);
        sEpisodes = new ArrayList<>();
        numberOfSeasons = new ArrayList<>();
        favourites = FavouriteMapper.getFavourites(this);


        seasonList = (ListView)findViewById(R.id.seasonList);
        CustomAdapter2 adapter = new CustomAdapter2();
        seasonList.setAdapter(adapter);
        textMessage = (TextView)findViewById(R.id.textMessage);
        textScore = (TextView)findViewById(R.id.textScore);
        final Bundle extras = getIntent().getExtras();
        if(extras != null){
            showId = extras.getString("showId");
            accountId = extras.getString("accountId");

            for(int i = 0; i < favourites.size(); i++){
                if(favourites.get(i).getShowId().getId().equals(showId) && favourites.get(i).getAccountId().getId().equals(accountId)){
                    buttonAddFavourite.setText("Remove from favourites");
                    buttonAddFavourite.setBackgroundColor(Color.RED);
                    break;
                }

                if(i == favourites.size() - 1){
                    buttonAddFavourite.setText("Add to favourites");
                    buttonAddFavourite.setBackgroundColor(Color.GREEN);
                }
            }

            show = TVShowMapper.selectShow(this, Integer.valueOf(showId));
            for(int i = 0; i < episodes.size(); i++){
                if(episodes.get(i).getShowId().getId().equals(showId)){
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
                if(buttonAddFavourite.getText().equals("Add to favourites")){
                    addFavourites();
                } else{
                    removeFavourites();
                }

            }
        });



        textMessage.setText(show.getName());
        textScore.setText(String.valueOf(show.getScore()));
        Toast.makeText(this, String.valueOf(episodes.size()), Toast.LENGTH_SHORT).show();
    }

    public void addFavourites(){

        Bundle extra = getIntent().getExtras();
        String accountId = extra.getString("accountId");
        String showId = extra.getString("showId");

        Account account = AccountMapper.selectAccount(getApplicationContext(), Integer.valueOf(accountId));
        TVShow show = TVShowMapper.selectShow(getApplicationContext(), Integer.valueOf(showId));
        FavouriteMapper.add(this, account, show);

        buttonAddFavourite.setText("Remove from favourites");
        buttonAddFavourite.setBackgroundColor(Color.RED);
        Toast.makeText(ShowDetailActivity.this, "added" , Toast.LENGTH_SHORT).show();


    }

    public void removeFavourites(){
        FavouriteMapper.deleteSpecific(this, accountId, showId);
        buttonAddFavourite.setBackgroundColor(Color.GREEN);
        buttonAddFavourite.setText("Add to favourites");
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
