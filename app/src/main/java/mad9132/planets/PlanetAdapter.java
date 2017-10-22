package mad9132.planets;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import mad9132.planets.model.PlanetPOJO;


/**
 * PlanetAdapter.
 *
 */
public class PlanetAdapter extends RecyclerView.Adapter<PlanetAdapter.ViewHolder> {

    private Map<Integer, Bitmap> mBitmaps;
    private Context              mContext;
    private List<PlanetPOJO>     mPlanets;

    public PlanetAdapter(Context context, List<PlanetPOJO> planets, Map<Integer, Bitmap> bitmaps) {
        this.mContext = context;
        this.mPlanets = planets;
        this.mBitmaps = bitmaps;
    }

    @Override
    public PlanetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View planetView = inflater.inflate(R.layout.list_planet, parent, false);
        ViewHolder viewHolder = new ViewHolder(planetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PlanetAdapter.ViewHolder holder, int position) {
        final PlanetPOJO aPlanet = mPlanets.get(position);

        holder.tvName.setText(aPlanet.getName());
        Bitmap bitmap = mBitmaps.get(aPlanet.getPlanetId());
        holder.imageView.setImageBitmap(bitmap);

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "You long clicked " + aPlanet.getName(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlanets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView imageView;
        public View mView;

        public ViewHolder(View planetView) {
            super(planetView);

            tvName = (TextView) planetView.findViewById(R.id.planetNameText);
            imageView = (ImageView) planetView.findViewById(R.id.imageView);
            mView = planetView;
        }
    }
}
