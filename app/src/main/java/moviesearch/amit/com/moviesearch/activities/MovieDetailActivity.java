package moviesearch.amit.com.moviesearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import moviesearch.amit.com.moviesearch.R;
import moviesearch.amit.com.moviesearch.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Movie movie = getIntent().getParcelableExtra("MOVIE");

        TextView movieName = (TextView) findViewById(R.id.movie_name_value);
        movieName.setText(movie.getMovieName());

        TextView movieYear = (TextView) findViewById(R.id.year_value);
        movieYear.setText(movie.getYear());

        TextView movieRelease = (TextView) findViewById(R.id.release_value);
        movieRelease.setText(movie.getReleasedDate());

        TextView movieGenre = (TextView) findViewById(R.id.genre_value);
        movieGenre.setText(movie.getGener());

        TextView movieSummary = (TextView) findViewById(R.id.summary_value);
        movieSummary.setText(movie.getSummary());

        TextView movieActors = (TextView) findViewById(R.id.actors_value);
        movieActors.setText(movie.getActors());

        TextView movieRatings = (TextView) findViewById(R.id.rating_value);
        movieRatings.setText(movie.getRatings_imDb());

        TextView movieType = (TextView) findViewById(R.id.type_value);
        movieType.setText(movie.getType());

        TextView movieDirector = (TextView) findViewById(R.id.movie_director_value);
        movieDirector.setText(movie.getDirector());

        ImageView imageView = (ImageView) findViewById(R.id.movie_detail_image_View);

        Picasso.with(this)
                .load(movie.getPosterLink())
                .placeholder(R.drawable.placeholder1)   // optional
                //.error(R.drawable.placeholder_3)      // optional
                .resize(300, 300)                        // optional
                .into(imageView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
