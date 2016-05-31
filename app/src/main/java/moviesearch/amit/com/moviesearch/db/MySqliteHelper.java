package moviesearch.amit.com.moviesearch.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import moviesearch.amit.com.moviesearch.model.Movie;

/**
 * Created by Amit_Gupta on 5/30/16.
 */
public class MySqliteHelper extends SQLiteOpenHelper {

    // database version
    private static final int database_VERSION = 1;
    // database name
    private static final String database_NAME = "MovieDB";
    private static final String table_Movies = "Movies";
    private static final String Movie_ID = "imdbID";
    private static final String Movie_Title = "title";
    private static final String Movie_Year = "year";
    private static final String Movie_Poster = "poster";

    private static final String[] COLUMNS = {Movie_ID, Movie_Title, Movie_Year, Movie_Poster};

    public MySqliteHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // SQL statement to create book table
        String CREATE_Movie_TABLE = "CREATE TABLE " + table_Movies + " ( " + "imdbID TEXT PRIMARY KEY, " + Movie_Title + " TEXT, " + Movie_Year + " TEXT, " + Movie_Poster + " TEXT )";
        sqLiteDatabase.execSQL(CREATE_Movie_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_Movies);
        this.onCreate(sqLiteDatabase);
    }

    public void insertMovie(Movie Movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Movie_ID, Movie.getImDbId());
        values.put(Movie_Title, Movie.getMovieName());
        values.put(Movie_Year, Movie.getYear());
        values.put(Movie_Poster, Movie.getPosterLink());

        db.insert(table_Movies, null, values);
        db.close();
    }

    public Movie readMovieDetail(String id) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(table_Movies, COLUMNS, Movie_ID+" = ?", new String[]{id}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Movie Movie = new Movie();
        Movie.setImDbId(id);
        Movie.setMovieName(cursor.getString(1));
        Movie.setYear(cursor.getString(2));
        Movie.setPosterLink(cursor.getString(2));
        db.close();
        return Movie;
    }

    public List getAllMovies() {

        List Movies = new ArrayList();

        String query = "SELECT  * FROM " + table_Movies;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Movie Movie = null;
        if (cursor.moveToFirst()) {

            do {
                Movie = new Movie();
                Movie.setImDbId(cursor.getString(0));
                Movie.setMovieName(cursor.getString(1));
                Movie.setYear(cursor.getString(2));
                Movie.setPosterLink(cursor.getString(3));
                Movies.add(Movie);
            } while (cursor.moveToNext());
        }
        db.close();
        return Movies;
    }

    public int updateMovieInfo(Movie Movie) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Movie_ID, Movie.getImDbId());
        values.put(Movie_Title, Movie.getMovieName());
        values.put(Movie_Year, Movie.getYear());
        values.put(Movie_Poster, Movie.getPosterLink());

        int i = db.update(table_Movies, values, Movie_ID + " = ?", new String[]{Movie.getImDbId()});
        db.close();
        return i;
    }

    public void deleteMovie(Movie Movie) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(table_Movies, Movie_ID + " = ?", new String[]{Movie.getImDbId()});
        db.close();
    }

}
