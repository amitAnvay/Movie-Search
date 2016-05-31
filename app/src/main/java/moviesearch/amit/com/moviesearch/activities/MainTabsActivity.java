package moviesearch.amit.com.moviesearch.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import moviesearch.amit.com.moviesearch.AppController;
import moviesearch.amit.com.moviesearch.R;
import moviesearch.amit.com.moviesearch.db.MySqliteHelper;
import moviesearch.amit.com.moviesearch.fragments.MovieSearchFragment;
import moviesearch.amit.com.moviesearch.fragments.FavouritesFragment;
import moviesearch.amit.com.moviesearch.model.Movie;


public class MainTabsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean isSearchEnabled = false;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    private FrameLayout searchBar;
    private ProgressDialog pDialog;
    private static final String TAG = MainTabsActivity.class.getSimpleName();
    MySqliteHelper sqliteHelper = new MySqliteHelper(this);
    private static String TXT_SEARCH_KEY = "text_search_key";

    // Movies json url
    private static final String url = "http://www.omdbapi.com/?type=movie&plot=full&r=json&s=";
    private static final String URL_ID = "http://www.omdbapi.com/?plot=full&r=json&i=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_tabs);
        Log.d(TAG, "--->onCreate 1");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        searchBar = (FrameLayout)findViewById(R.id.search_bar);
        searchBar.setVisibility(View.VISIBLE);
        edtSeach = (EditText)findViewById(R.id.edtSearch);
        edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(edtSeach.getText() != null && edtSeach.getText().length() > 0)
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        Button clearText = (Button) findViewById(R.id.clear_txt);
        clearText.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                edtSeach.setText("");
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // Click action
                if(isSearchEnabled){
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_search);
                    fab.setImageBitmap(bm);
                    isSearchEnabled = false;
                    searchBar.setVisibility(View.GONE);
                    performSearch();
                }else {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_close_search);
                    fab.setImageBitmap(bm);
                    isSearchEnabled = true;
                    searchBar.setVisibility(View.VISIBLE);
                    edtSeach.requestFocus();
                }
            }
        });
        Log.d(TAG, "--->onCreate 2");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String txtSearch = savedInstanceState.getString(TXT_SEARCH_KEY);
            Log.d(TAG, "--->onCreate 3" + txtSearch);
            edtSeach.setText(txtSearch);
            if (edtSeach.getText() != null && edtSeach.getText().length() > 0) {
                Log.d(TAG, "--->onCreate 4");
                performSearch();
            }
            super.onRestoreInstanceState(savedInstanceState);

        }
    }

        @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(TXT_SEARCH_KEY, edtSeach.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieSearchFragment(), getResources().getString(R.string.movies_list));
        adapter.addFragment(new FavouritesFragment(), getResources().getString(R.string.favourites));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "--->ViewPagerAdapter getItem");
            return mFragmentList.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(TAG, "--->ViewPagerAdapter instantiateItem");
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            mFragmentList.set(position,fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            Log.d(TAG, "--->ViewPagerAdapter addFragment"+mFragmentList.size());
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            return;
        }
        super.onBackPressed();
    }

    private void performSearch() {
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj
        JsonObjectRequest movieReq = new JsonObjectRequest(url+edtSeach.getText(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "--->Received Response");
                        hidePDialog();
                        ArrayList<Movie> movieList = new ArrayList<>();
                        JSONArray array = null;
                        try {
                            array = response.getJSONArray("Search");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(array != null && array.length() > 0) {
                            // Parsing json
                            int l = array.length();
                            for (int i = 0; i < l; i++) {
                                try {

                                    JSONObject obj = array.getJSONObject(i);
                                    Movie movie = new Movie();
                                    movie.setMovieName(obj.getString("Title"));
                                    movie.setYear(obj.getString("Year"));
                                    movie.setPosterLink(obj.getString("Poster"));
                                    movie.setImDbId(obj.getString("imdbID"));

                                    movieList.add(movie);
                                    ViewPagerAdapter adapter = ((ViewPagerAdapter) viewPager.getAdapter());
                                    MovieSearchFragment fragment = (MovieSearchFragment) adapter.getItem(0);
                                    fragment.setListData(movieList);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else {
                            Toast.makeText(MainTabsActivity.this, "No Search Results found", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtSeach.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        viewPager.requestFocus();

    }

    public void showMovieDetail(Movie m) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj
        JsonObjectRequest movieReq = new JsonObjectRequest(URL_ID+m.getImDbId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "--->Received Response");
                        hidePDialog();
                                try {
                                    Movie movie = new Movie();
                                    movie.setMovieName(response.getString("Title"));
                                    movie.setYear(response.getString("Year"));
                                    movie.setReleasedDate(response.getString("Released"));
                                    movie.setDirector(response.getString("Director"));
                                    movie.setSummary(response.getString("Plot"));
                                    movie.setGener(response.getString("Genre"));
                                    movie.setActors(response.getString("Actors"));
                                    movie.setReleasedDate(response.getString("Released"));
                                    movie.setRatings_imDb(response.getString("imdbRating"));
                                    movie.setType(response.getString("Type"));
                                    movie.setImDbId(response.getString("imdbID"));
                                    movie.setPosterLink(response.getString("Poster"));

                                    Intent intent = new Intent(MainTabsActivity.this, MovieDetailActivity.class);
                                    intent.putExtra("MOVIE", movie);
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtSeach.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        viewPager.requestFocus();

    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    public MySqliteHelper getSqliteHelper(){
        return sqliteHelper;
    }


}
