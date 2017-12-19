package com.example.kuba.tvmanager.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuba.tvmanager.Account;
import com.example.kuba.tvmanager.Episode;
import com.example.kuba.tvmanager.EpisodeListActivity;
import com.example.kuba.tvmanager.Favourite;
import com.example.kuba.tvmanager.Mappers.AccountMapper;
import com.example.kuba.tvmanager.Mappers.EpisodeMapper;
import com.example.kuba.tvmanager.Mappers.FavouriteMapper;
import com.example.kuba.tvmanager.Mappers.ScoreMapper;
import com.example.kuba.tvmanager.Mappers.TVShowMapper;
import com.example.kuba.tvmanager.R;
import com.example.kuba.tvmanager.Score;
import com.example.kuba.tvmanager.ScoreTable;
import com.example.kuba.tvmanager.TVShow;

import java.util.ArrayList;

public class ShowDetailActivity extends AppCompatActivity {
    private TextView textMessage;
    private TextView textScore;
    private TVShow show;
    private ListView seasonList;
    private TextView textScoreYour;
    private ArrayList<Episode> episodes;
    private ArrayList<Episode> sEpisodes;
    private ArrayList<String> numberOfSeasons;
    private Button buttonAddFavourite;
    private Button buttonInsert;
    private EditText textYourScore;
    private ArrayList<Favourite> favourites;
    private ArrayList<Score> scoreList;
    private String showId;
    private String accountId;
    private int currentScore;
    private Button buttonCheck;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            buttonAddFavourite.setText("Remove from favourites");
            buttonAddFavourite.setBackgroundColor(Color.RED);
            Toast.makeText(ShowDetailActivity.this, "added" , Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onRestart() {

        // TODO Auto-generated method stub
        super.onRestart();
        Intent i = new Intent(ShowDetailActivity.this, ShowDetailActivity.class);  //your class
        startActivity(i);
        finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        textScoreYour = (TextView)findViewById(R.id.textScoreYour);
        buttonInsert = (Button)findViewById(R.id.buttonInsert);
        textYourScore = (EditText)findViewById(R.id.textYourScore);
        buttonAddFavourite = (Button)findViewById(R.id.buttonAddFavourite);
        episodes = EpisodeMapper.getEpisodes(this);
        sEpisodes = new ArrayList<>();
        numberOfSeasons = new ArrayList<>();
        favourites = null;
        favourites = FavouriteMapper.getFavourites(this);
        scoreList = ScoreMapper.getScores(this);
        currentScore = 0;
        buttonCheck = (Button)findViewById(R.id.buttonCheck);



        seasonList = (ListView)findViewById(R.id.seasonList);
        CustomAdapter2 adapter = new CustomAdapter2();
        seasonList.setAdapter(adapter);
        textMessage = (TextView)findViewById(R.id.textMessage);
        textScore = (TextView)findViewById(R.id.textScore2);
        final Bundle extras = getIntent().getExtras();
        if(extras != null){
            showId = extras.getString("showId");
            accountId = extras.getString("accountId");

            if(favourites.size() == 0){
                buttonAddFavourite.setText("Add to favourites");
                buttonAddFavourite.setBackgroundColor(Color.GREEN);
            }

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

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //addScore();
                addScore();
            }
        });

        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //addScore();
                Intent intent = new Intent(ShowDetailActivity.this, ScoreTable.class);
                startActivity(intent);
            }
        });

        seasonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle extra = getIntent().getExtras();
                String accountId = extra.getString("accountId");
                String showId = extra.getString("showId");
                Intent intent = new Intent(ShowDetailActivity.this, EpisodeListActivity.class);
                intent.putExtra("accountId", accountId);
                intent.putExtra("showId", showId);
                intent.putExtra("season", numberOfSeasons.get(position));
                startActivity(intent);
            }
        });



        //setYourScore();
        initialSet();

        textMessage.setText(show.getName());
    }

    private void initialSet(){
        Bundle extra = getIntent().getExtras();
        String accountId = extra.getString("accountId");
        String showId = extra.getString("showId");
        TVShow show = TVShowMapper.selectShow(this, Integer.parseInt(showId));
        Account account = AccountMapper.selectAccount(this, Integer.parseInt(accountId));
        ArrayList<Score> scoreList = ScoreMapper.getScores(this);
        Score score = null;
        for(int i = 0; i < scoreList.size(); i++){
            if(scoreList.get(i).getShowId().getId().equals(showId) && scoreList.get(i).getAccountId().getId().equals(accountId)){
                score = scoreList.get(i);
                break;
            }
        }

        textScore.setText(String.valueOf(show.getScore()));
        if(score == null){
            textScoreYour.setText("0");
        }else{
            textScoreYour.setText(String.valueOf(score.getScore()));
            Toast.makeText(this, score.info(), Toast.LENGTH_SHORT).show();
        }

        if(textScoreYour.getText().equals("0"))
            buttonInsert.setText("insert score");
        else
            buttonInsert.setText("update score");
    }

    private void addScore(){
        Bundle extra = getIntent().getExtras();
        String accountId = extra.getString("accountId");
        String showId = extra.getString("showId");
        Account account = AccountMapper.selectAccount(this, Integer.parseInt(accountId));
        TVShow show = TVShowMapper.selectShow(this, Integer.parseInt(showId));

        Score score = new Score(show, account, Integer.parseInt(textYourScore.getText().toString()));
        if(buttonInsert.getText().equals("insert score"))
            ScoreMapper.add(this, score);
        else
            ScoreMapper.update(this, score);
        textYourScore.setText("");
        textScoreYour.setText(String.valueOf(score.getScore()));
        int totalScore = 0;
        ArrayList<Score> scoreList = ScoreMapper.getScores(this);
        ArrayList<Score> newList = new ArrayList<>();
        for(int i = 0; i < scoreList.size(); i++){
            if(scoreList.get(i).getShowId().getId().equals(showId)){
                newList.add(scoreList.get(i));
            }
        }

        for(int i = 0; i < newList.size(); i++){
            totalScore += newList.get(i).getScore();
        }


        TVShowMapper.update(this, showId, totalScore / newList.size());
        textScore.setText(String.valueOf(totalScore / newList.size()));
        buttonInsert.setText("update score");
    }

    /*private void setYourScore(){
        Bundle extra = getIntent().getExtras();
        String accountId = extra.getString("accountId");
        String showId = extra.getString("showId");

        Score myScore = ScoreMapper.selectSpecific(this, accountId, showId);



        if(myScore.getScore() == 0){
            buttonInsert.setText("insert score");
        }else{
            buttonInsert.setText("update score");
            Toast.makeText(this, String.valueOf(myScore.getAccountId().getId()) + " " + String.valueOf(myScore.getShowId().getId()) + " " + String.valueOf(myScore.getScore()), Toast.LENGTH_SHORT).show();

        }
        textScoreYour.setText(String.valueOf(myScore.getScore()));
    }

    public void addScore(){
        Bundle extra = getIntent().getExtras();
        String accountId = extra.getString("accountId");
        String showId = extra.getString("showId");


        Account account = AccountMapper.selectAccount(getApplicationContext(), Integer.valueOf(accountId));
        TVShow show = TVShowMapper.selectShow(getApplicationContext(), Integer.valueOf(showId));


        if(buttonInsert.getText().equals("insert score")){
            if(textYourScore.equals("")){
                Toast.makeText(this, "Insert your score pleas.", Toast.LENGTH_SHORT).show();
            }
            else{
                ScoreMapper.add(this, account, show, Integer.parseInt(textYourScore.getText().toString()));
                scoreList = ScoreMapper.getScores(this);
                ArrayList<Score> list = new ArrayList<>();
                int controlSum = 0;
                for(int i = 0; i < scoreList.size(); i++){
                    if(scoreList.get(i).getShowId().getId().equals(showId)){
                        list.add(scoreList.get(i));
                    }
                }

                for(int i = 0; i < list.size(); i++){
                    controlSum += list.get(i).getScore();
                }

                textScore.setText(String.valueOf(controlSum / list.size()));
                TVShowMapper.update(this, showId, controlSum / list.size());
                textYourScore.setText("");
                setYourScore();
                buttonInsert.setText("Update score");
            }
        }
        else{
            //ScoreMapper.update(this, account, show, Integer.parseInt(textYourScore.getText().toString()));
            ScoreMapper.update(this, accountId, showId, Integer.parseInt(textYourScore.getText().toString()));
            scoreList = ScoreMapper.getScores(this);
            ArrayList<Score> list = new ArrayList<>();
            for(int i = 0; i < scoreList.size(); i++){
                if(scoreList.get(i).getShowId().getId().equals(showId)){
                    list.add(scoreList.get(i));
                }
            }
            int controlSum = 0;
            for(int i = 0; i < list.size(); i++){
                controlSum += list.get(i).getScore();
            }

            textScore.setText(String.valueOf(controlSum / list.size()));
            TVShowMapper.update(this, showId, controlSum / list.size());
            textYourScore.setText("");
            textScoreYour.setText(String.valueOf(ScoreMapper.selectSpecific(this, accountId, showId).getScore()));
            setYourScore();
            //buttonInsert.setText("Update score");
        }

    }*/

    public void addFavourites(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Bundle extra = getIntent().getExtras();
                String accountId = extra.getString("accountId");
                String showId = extra.getString("showId");

                Account account = AccountMapper.selectAccount(getApplicationContext(), Integer.valueOf(accountId));
                TVShow show = TVShowMapper.selectShow(getApplicationContext(), Integer.valueOf(showId));
                FavouriteMapper.add(getApplicationContext(), account, show);

                handler.sendEmptyMessage(0);
            }
        };

        Thread thread2 = new Thread(r);
        thread2.start();




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
            final CheckedTextView seasonCheck = (CheckedTextView) convertView.findViewById(R.id.simpleCheckedTextView);
            //seasonCheck.setText(episodes.get(position).getName());
            //seasonCheck.setCheckMarkDrawable(0);
            seasonCheck.setText("Season " + numberOfSeasons.get(position));
            seasonCheck.setCheckMarkDrawable(null);


            return convertView;
        }
    }
}
