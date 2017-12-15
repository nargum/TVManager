package com.example.kuba.tvmanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
            ImageView imageView = (ImageView)convertView.findViewById(R.id.picture);
            TextView textViewName = (TextView)convertView.findViewById(R.id.textViewName);
            textViewName.setText(shows.get(position).getName());

            switch(shows.get(position).getName()){
                case "The Walking Dead":
                    imageView.setImageResource(R.drawable.the_walking_dead);
                    break;
                case "Game of Thrones":
                    imageView.setImageResource(R.drawable.game_of_thrones);
                    break;
                case "Lucifer":
                    imageView.setImageResource(R.drawable.lucifer);
                    break;
                case "Arrow":
                    imageView.setImageResource(R.drawable.arrow);
                    break;
                case "The Flash":
                    imageView.setImageResource(R.drawable.the_flash);
                    break;
                case "Spactacus":
                    imageView.setImageResource(R.drawable.spartacus);
                    break;
                case "The Big Bang Theory":
                    imageView.setImageResource(R.drawable.the_big_bang_theory);
                    break;
                case "How I Met Your Mother":
                    imageView.setImageResource(R.drawable.how_i_met_your_mother);
                    break;
                case "Two and a half men":
                    imageView.setImageResource(R.drawable.two_and_a_half_men);
                    break;
                case "Band of Brothers":
                    imageView.setImageResource(R.drawable.band_of_brothers);
                    break;
                default:
                    imageView.setImageResource(R.drawable.game_of_thrones);
                    break;
            }
            return convertView;
        }
    }
}
