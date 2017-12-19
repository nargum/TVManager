package com.example.kuba.tvmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kuba.tvmanager.Activities.CreateAccountActivity;
import com.example.kuba.tvmanager.Activities.LoginActivity;
import com.example.kuba.tvmanager.Activities.ShowDetailActivity;
import com.example.kuba.tvmanager.Mappers.ScoreMapper;

import java.util.ArrayList;

public class ScoreTable extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Score> scores;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);

        scores = ScoreMapper.getScores(this);
        listView = (ListView)findViewById(R.id.listWiev);
        button2 = (Button)findViewById(R.id.button2);


        ScoreTable.CustomAdapter adapter = new CustomAdapter();
        listView.setAdapter(adapter);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ScoreMapper.delete(getApplicationContext());
            }
        });
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return scores.size();
        }

        @Override
        public Object getItem(int position) {
            return scores.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Long.valueOf(scores.get(position).getAccountId().getId());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.score_resource, null);
            TextView textAccount = (TextView)convertView.findViewById(R.id.textAccount);
            TextView textShow = (TextView)convertView.findViewById(R.id.textShow);
            TextView textScore = (TextView)convertView.findViewById(R.id.textScore2);
            textAccount.setText(scores.get(position).getAccountId().getName());
            textShow.setText(scores.get(position).getShowId().getName());
            textScore.setText(String.valueOf(scores.get(position).getScore()));
            return convertView;
        }
    }
}
