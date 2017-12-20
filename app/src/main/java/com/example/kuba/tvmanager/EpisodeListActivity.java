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
import android.widget.Toast;

import com.example.kuba.tvmanager.Activities.ShowDetailActivity;
import com.example.kuba.tvmanager.Mappers.AccountMapper;
import com.example.kuba.tvmanager.Mappers.EpisodeMapper;
import com.example.kuba.tvmanager.Mappers.TVShowMapper;
import com.example.kuba.tvmanager.Mappers.WatchedMapper;

import java.util.ArrayList;
import java.util.Iterator;

public class EpisodeListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Episode> episodes;
    private int counter = 0;
    private ImageView imageMain;

    @Override
    public void onBackPressed() {
        finish();

        Toast.makeText(this, "good", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_list);

        imageMain = (ImageView)findViewById(R.id.imageMain);
        listView = (ListView)findViewById(R.id.listWiev);
        episodes = filterEpisodes();

        setupImage();
        final CustomAdapter adapter = new CustomAdapter();
        listView.setAdapter(adapter);

    }

    private void setupImage() {
        Bundle extra = getIntent().getExtras();
        String showId = extra.getString("showId");

        TVShow show = TVShowMapper.selectShow(getApplicationContext(), Integer.parseInt(showId));

        switch (show.getName()) {
            case "The Walking Dead":
                imageMain.setImageResource(R.drawable.twd);
                break;
            case "Game of Thrones":
                imageMain.setImageResource(R.drawable.got);
                break;
            case "Lucifer":
                imageMain.setImageResource(R.drawable.luc);
                break;
            case "Arrow":
                imageMain.setImageResource(R.drawable.arr);
                break;
            case "The Flash":
                imageMain.setImageResource(R.drawable.flash);
                break;
            case "Spartacus":
                imageMain.setImageResource(R.drawable.spar);
                break;
            case "The Big Bang Theory":
                imageMain.setImageResource(R.drawable.tbbt);
                break;
            case "How I Meet Your Mother":
                imageMain.setImageResource(R.drawable.himym);
                break;
            case "Two and a half men":
                imageMain.setImageResource(R.drawable.tam);
                break;
            case "Band of Brothers":
                imageMain.setImageResource(R.drawable.bob);
                break;
            default:
                imageMain.setImageResource(R.drawable.got);
                break;

        }
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
        View v;

        public View getV(){
            return v;
        }
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
            v = convertView;
            //CheckBox checkEpisode = (CheckBox)convertView.findViewById(R.id.checkEpisode);
            final CheckedTextView seasonCheck = (CheckedTextView) convertView.findViewById(R.id.simpleCheckedTextView);
            seasonCheck.setText(episodes.get(position).getEpisodeNumber() + ". " + episodes.get(position).getName());

            Bundle extra = getIntent().getExtras();
            String myAccount = extra.getString("accountId");
            seasonCheck.setCheckMarkDrawable(null);
            seasonCheck.setChecked(false);

            ArrayList<Watched> watchedList = WatchedMapper.getRecords(getApplicationContext());
            if(watchedList == null){
                seasonCheck.setCheckMarkDrawable(null);
                seasonCheck.setChecked(false);
            }else{
                for(int i = 0; i < watchedList.size(); i++){

                    if(watchedList.get(i).getEpisodeId().getId().equals(episodes.get(position).getId()) && watchedList.get(i).getAccountId().getId().equals(myAccount)){
                        seasonCheck.setCheckMarkDrawable(R.drawable.checked);
                        seasonCheck.setChecked(true);
                        counter++;
                    }

                }
            }



            seasonCheck.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(!seasonCheck.isChecked()){
                        seasonCheck.setChecked(true);
                        seasonCheck.setCheckMarkDrawable(R.drawable.checked);
                        Bundle extra = getIntent().getExtras();
                        int episodeCount = extra.getInt("episodeCount");
                        String accountId = extra.getString("accountId");
                        String episodeId = extra.getString(String.valueOf(seasonCheck.getText()));


                        Account account = AccountMapper.selectAccount(getApplicationContext(), Integer.parseInt(accountId));
                        Episode episode = EpisodeMapper.selectEpisode(getApplicationContext(), Integer.parseInt(episodeId));
                        WatchedMapper.add(getApplicationContext(), account, episode);

                        counter++;
                        Toast.makeText(EpisodeListActivity.this, String.valueOf(counter), Toast.LENGTH_SHORT).show();
                    }else{
                        seasonCheck.setChecked(false);
                        seasonCheck.setCheckMarkDrawable(null);

                        Bundle extra = getIntent().getExtras();
                        int episodeCount = extra.getInt("episodeCount");
                        String accountId = extra.getString("accountId");
                        String episodeId = extra.getString(String.valueOf(seasonCheck.getText()));

                        Account account = AccountMapper.selectAccount(getApplicationContext(), Integer.parseInt(accountId));
                        Episode episode = EpisodeMapper.selectEpisode(getApplicationContext(), Integer.parseInt(episodeId));
                        WatchedMapper.deleteSpecific(getApplicationContext(), account, episode);
                        counter--;
                        Toast.makeText(EpisodeListActivity.this, String.valueOf(counter), Toast.LENGTH_SHORT).show();
                    }
                }
            });


            return convertView;
        }
    }
}
