package com.example.kuba.tvmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kuba.tvmanager.Favourite;
import com.example.kuba.tvmanager.Mappers.FavouriteMapper;
import com.example.kuba.tvmanager.R;
import com.example.kuba.tvmanager.TVShow;

import java.util.ArrayList;

/**
 * Created by Kuba on 12/16/2017.
 */

public class FragmentFavourite extends Fragment {
    private static final String TAG = "FragmentFavourite";
    private ArrayList<Favourite> favourites;
    private ArrayList<Favourite> newFavourites;
    private ArrayList<TVShow> shows;
    private ListView listView;
    private String accountId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_favourite_list, container, false);

        listView = (ListView)view.findViewById(R.id.listWiev);
        shows = new ArrayList<>();
        newFavourites = new ArrayList<>();
        Bundle extra = getActivity().getIntent().getExtras();
        if(extra != null){
            accountId = extra.getString("accountId");
            favourites = FavouriteMapper.getFavourites(getActivity().getApplicationContext());


            for(int i = 0; i < favourites.size(); i++){
                if(accountId.equals(favourites.get(i).getAccountId().getId())){
                    newFavourites.add(favourites.get(i));
                }
            }


            for(int i = 0; i < newFavourites.size(); i++){
                shows.add(newFavourites.get(i).getShowId());
            }

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TVShow show = shows.get(position);
                Intent intent = new Intent(getActivity(), ShowDetailActivity.class);
                intent.putExtra("showId", show.getId());
                intent.putExtra("accountId", accountId);
                startActivity(intent);
            }
        });

        FragmentFavourite.CustomAdapter adapter = new CustomAdapter();
        listView.setAdapter(adapter);

        return view;
    }


    class CustomAdapter extends BaseAdapter {

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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_layout, null);
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
                case "Spartacus":
                    imageView.setImageResource(R.drawable.spartacus);
                    break;
                case "The Big Bang Theory":
                    imageView.setImageResource(R.drawable.the_big_bang_theory);
                    break;
                case "How I Meet Your Mother":
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
