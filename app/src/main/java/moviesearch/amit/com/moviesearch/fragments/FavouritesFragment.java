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

import moviesearch.amit.com.moviesearch.R;
import moviesearch.amit.com.moviesearch.activities.MainTabsActivity;
import moviesearch.amit.com.moviesearch.adapters.FovouriteListAdapter;
import moviesearch.amit.com.moviesearch.db.MySqliteHelper;
import moviesearch.amit.com.moviesearch.model.Movie;
import moviesearch.amit.com.moviesearch.util.CustomItemClickListener;
import moviesearch.amit.com.moviesearch.util.DividerItemDecoration;


public class FavouritesFragment extends Fragment {

    public FavouritesFragment() {
        // Required empty public constructor
        Log.d(TAG, "--->TwoFragment Constructor 1"+this);
    }

    RecyclerView recyclerView;
    TextView noMoviesFound;
    private FovouriteListAdapter fovouriteListAdapter;
    private static final String TAG = MovieSearchFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "--->TwoFragment onCreate 1"+this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "--->TwoFragment onResume 1"+this);
        setListData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "--->onCreateView 1"+this);
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.favourite_tab_recycler_view);
        noMoviesFound = (TextView)view.findViewById(R.id.no_favourites_found);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
//        fovouriteListAdapter = new FovouriteListAdapter(this.getActivity(), new CustomItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position) {
//                int id=v.getId();
//
//                switch (id){
//                    case R.id.list_view_row:
//                        Movie m = fovouriteListAdapter.getMovie(position);
//                        MainTabsActivity activity = (MainTabsActivity) getActivity();
//                        activity.showMovieDetail(m);
//                        break;
//                    //default:Toast.makeText(v.getContext(),"Nohting",Toast.LENGTH_LONG).show();
//
//                }
//            }
//        });
        fovouriteListAdapter = new FovouriteListAdapter(this.getActivity(), new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                int id=v.getId();
                Movie m = fovouriteListAdapter.getMovie(position);
                MainTabsActivity activity = (MainTabsActivity) getActivity();
                switch (id){
                    case R.id.img_favourite:
                        //Toast.makeText(v.getContext(),"Favourite Image",Toast.LENGTH_LONG).show();
                        ImageView imageView = (ImageView)v;
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
                        fovouriteListAdapter.setMovie(position,m);
                        break;
                    case R.id.list_view_row:
                        activity.showMovieDetail(m);
                        break;
                    default:
                        Toast.makeText(v.getContext(),"Nohting",Toast.LENGTH_LONG).show();

                }
            }
        });

        recyclerView.setAdapter(fovouriteListAdapter);
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

    public void setListData(){
        showList();
        Log.d(TAG, "--->TwoFragment Constructor 1"+this);
        MainTabsActivity activity = (MainTabsActivity) getActivity();
         MySqliteHelper sqliteHelper = activity.getSqliteHelper();
        fovouriteListAdapter.setMoviesList(sqliteHelper.getAllMovies());
        fovouriteListAdapter.notifyDataSetChanged();
    }

}
