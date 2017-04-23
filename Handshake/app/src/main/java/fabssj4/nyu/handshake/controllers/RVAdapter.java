package fabssj4.nyu.handshake.controllers;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fabssj4.nyu.handshake.R;

/**
 * Created by FabSSJ4 on 4/3/2016.
 */
public class RVAdapter
        extends RecyclerView.Adapter<RVAdapter.ViewHolder>
        implements View.OnClickListener {



    // Hold the position of the expanded item
    private int expandedPosition = -1;


    private ArrayList<String> mDataset;
    private Context mContext;


    public RVAdapter (Context context, ArrayList<String> myDataset) {
        this.mDataset = myDataset;
        this.mContext = context;
    }

    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_card, parent, false);

        ViewHolder holder = new ViewHolder(v);

        // Sets the click adapter for the entire cell
        // to the one in this class.
        holder.itemView.setOnClickListener(RVAdapter.this);
        holder.itemView.setTag(holder);

        return holder;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onBindViewHolder(RVAdapter.ViewHolder holder, int position) {

        holder.cardtitle.setText(mDataset.get(position));

        if (position == expandedPosition) {
            holder.cardexpandarea.setVisibility(View.VISIBLE);
        } else {
            holder.cardexpandarea.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        String theString = mDataset.get(holder.getPosition());

        // Check for an expanded view, collapse if you find one
        if (expandedPosition >= 0) {
            int prev = expandedPosition;
            notifyItemChanged(prev);
        }
        // Set the current position to "expanded"
        expandedPosition = holder.getPosition();
        notifyItemChanged(expandedPosition);

        Toast.makeText(mContext, "Clicked: "+theString, Toast.LENGTH_SHORT).show();
    }

    /**
     * Create a ViewHolder to represent your cell layout
     * and data element structure
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardtitle;
        LinearLayout cardexpandarea;

        public ViewHolder(View itemView) {
            super(itemView);

            cardtitle = (TextView) itemView.findViewById(R.id.cardtitle);
            cardexpandarea = (LinearLayout) itemView.findViewById(R.id.cardexpandarea);
        }
    }
}
