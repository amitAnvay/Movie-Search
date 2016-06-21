package moviesearch.amit.com.moviesearch.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import moviesearch.amit.com.moviesearch.R;
import moviesearch.amit.com.moviesearch.activities.MainTabsActivity;
import moviesearch.amit.com.moviesearch.adapters.MovieListAdapter;
import moviesearch.amit.com.moviesearch.db.MySqliteHelper;
import moviesearch.amit.com.moviesearch.model.Movie;
import moviesearch.amit.com.moviesearch.util.CustomItemClickListener;
import moviesearch.amit.com.moviesearch.util.DividerItemDecoration;

/***
 *  This fragment holds the Movies search result of first Tab in a RecylerView.
 */
public class MovieSearchFragment extends Fragment {


    RecyclerView recyclerView;
    TextView noMoviesFound;
    private MovieListAdapter movieListAdapter;
    private static final String TAG = MovieSearchFragment.class.getSimpleName();

    public MovieSearchFragment() {
        Log.d(TAG, "--->OneFragment Constructor 1"+this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "--->onCreateView 1"+this);
        View view = inflater.inflate(R.layout.fragment_movie_search, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        noMoviesFound = (TextView)view.findViewById(R.id.no_movies_found);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        movieListAdapter = new MovieListAdapter(this.getActivity(), new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                int id=v.getId();
                Movie m = movieListAdapter.getMovie(position);
                MainTabsActivity activity = (MainTabsActivity) getActivity();
        switch (id){
            case R.id.img_favourite:
                //Toast.makeText(v.getContext(),"Favourite Image",Toast.LENGTH_LONG).show();
                ImageView  imageView = (ImageView)v;
                Bitmap bm = null;
                MySqliteHelper sqliteHelper = activity.getSqliteHelper();
                if(m.isFavourite()){
                    bm = BitmapFactory.decodeResource(v.getContext().getResources(), R.drawable.favourite7);
                    imageView.setImageBitmap(bm);
                    m.setFavourite(false);
                    sqliteHelper.deleteMovie(m);

                }else {
                    bm = BitmapFactory.decodeResource(v.getContext().getResources(), R.drawable.favourite6);
                    imageView.setImageBitmap(bm);
                    m.setFavourite(true);
                    sqliteHelper.insertMovie(m);
                }
                movieListAdapter.setMovie(position,m);
                break;
            case R.id.list_view_row:
                activity.showMovieDetail(m);

                break;
            default:Toast.makeText(v.getContext(),"Nohting",Toast.LENGTH_LONG).show();

        }
            }
        });
        recyclerView.setAdapter(movieListAdapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), null));
        Log.d(TAG, "--->onCreateView 2"+this);
        return view;
    }

    public void showList(){
        if(recyclerView != null)
        recyclerView.setVisibility(View.VISIBLE);
        if(noMoviesFound != null)
        noMoviesFound.setVisibility(View.GONE);
    }

    public void showMsgText(){
        recyclerView.setVisibility(View.GONE);
        noMoviesFound.setVisibility(View.VISIBLE);
    }

    public void setListData(List list){
        showList();
        Log.d(TAG, "--->OneFragment Constructor 1"+this);
        MainTabsActivity activity = (MainTabsActivity) getActivity();
        MySqliteHelper sqliteHelper = activity.getSqliteHelper();
        movieListAdapter.setMoviesList(sqliteHelper.udpateMovieListWitIsFavourite(list));
        movieListAdapter.notifyDataSetChanged();
    }

}
