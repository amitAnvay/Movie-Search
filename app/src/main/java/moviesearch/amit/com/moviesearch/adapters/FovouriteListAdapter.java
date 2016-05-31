package moviesearch.amit.com.moviesearch.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import moviesearch.amit.com.moviesearch.R;
import moviesearch.amit.com.moviesearch.model.Movie;
import moviesearch.amit.com.moviesearch.util.CustomItemClickListener;


/**
 * Created by Amit_Gupta on 3/20/16.
 */
public class FovouriteListAdapter extends RecyclerView.Adapter<FovouriteListAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private ArrayList<Movie> movieList;
    CustomItemClickListener listener;

    public FovouriteListAdapter(Context context, CustomItemClickListener listener){
        mContext = context;
        movieList = new ArrayList<>();
        this.listener = listener;
    }

    public void setMoviesList(List list){
        movieList = (ArrayList<Movie>) list;
    }

    public Movie getMovie(int indx){
       return movieList.get(indx);
    }

    public void setMovie(int indx, Movie m){
        movieList.set(indx,m);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_view_item, parent, false);
        final ViewHolder vH = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, vH.getPosition());
            }
        });
        return vH;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Movie m = movieList.get(position);
        holder.listItemText.setText(m.getMovieName());
        holder.year.setText(m.getYear());
        holder.setPosition(position);
        //holder.listFavImage.setOnClickListener(this);
        holder.listFavImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, holder.getPosition());
            }
        });
            Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.favourite6);
            holder.listFavImage.setImageBitmap(bm);
        if(m != null) {
            Picasso.with(mContext)
                    .load(m.getPosterLink())
                    .placeholder(R.drawable.placeholder1)   // optional
                    //.error(R.drawable.placeholder_3)      // optional
                    .resize(200, 150)                        // optional
                    .into(holder.listItemImage);
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public void onClick(View v) {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView listItemText;
        public int position;
        public ImageView listItemImage;
        public ImageView listFavImage;
        public TextView year;


        public ViewHolder(View itemView) {
            super(itemView);
            listItemText = (TextView)itemView.findViewById(R.id.list_item_title);
            listItemImage = (ImageView)itemView.findViewById(R.id.list_item_image);
           // listItemDateTime = (TextView)itemView.findViewById(R.id.list_item_date_time);
            year = (TextView)itemView.findViewById(R.id.movie_year);
            listFavImage = (ImageView)itemView.findViewById(R.id.img_favourite);
           // listFavImage.setOnClickListener(this);
        }

        public void setPosition(int i){
            position = i;
        }
    }
}
