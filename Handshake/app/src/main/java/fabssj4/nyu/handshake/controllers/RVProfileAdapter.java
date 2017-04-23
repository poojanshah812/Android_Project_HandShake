package fabssj4.nyu.handshake.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fabssj4.nyu.handshake.R;

/**
 * Created by FabSSJ4 on 4/6/2016.
 */
public class RVProfileAdapter extends RecyclerView.Adapter<RVProfileAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private ArrayList<String> myProfile;
    private ArrayList<Integer> myduration;
    private ArrayList<Integer> mymillisvibration;
    private ArrayList<Integer> mypattern;
    private ArrayList<Boolean> myflashlight;

    public RVProfileAdapter(Context context, ArrayList<String> myProfile, ArrayList<Integer> myduration, ArrayList<Integer> mymillisvibration, ArrayList<Integer> mypattern, ArrayList<Boolean> myflashlight) {
        this.mContext = context;
        this.myProfile = myProfile;
        this.myduration = myduration;
        this.mymillisvibration = mymillisvibration;
        this.mypattern = mypattern;
        this.myflashlight = myflashlight;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_profile_card, parent, false);

        ViewHolder holder = new ViewHolder(v);

        // Sets the click adapter for the entire cell
        // to the one in this class.
        holder.itemView.setOnClickListener(RVProfileAdapter.this);
        holder.itemView.setTag(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.profilename.setText(myProfile.get(position));
        holder.profileduration.setText(myduration.get(position)+"");
        holder.profilemillisvibration.setText(mymillisvibration.get(position)+"");
        holder.profileflashlightsetting.setChecked(myflashlight.get(position));
        String hexpattern = Integer.toHexString(mypattern.get(position));
        /*holder.profilepianobtn1.setPressed(hexpattern.charAt(0) == 0 ? Boolean.FALSE : Boolean.TRUE);
        holder.profilepianobtn2.setPressed(hexpattern.charAt(1) == 0 ? Boolean.FALSE : Boolean.TRUE);
        holder.profilepianobtn3.setPressed(hexpattern.charAt(2) == 0 ? Boolean.FALSE : Boolean.TRUE);
        holder.profilepianobtn4.setPressed(hexpattern.charAt(3) == 0 ? Boolean.FALSE : Boolean.TRUE);*/
        holder.profilepianobtn1.setPressed(Boolean.TRUE);
        holder.profilepianobtn2.setPressed(Boolean.FALSE);
        holder.profilepianobtn3.setPressed(Boolean.TRUE);
        holder.profilepianobtn4.setPressed(Boolean.TRUE);
    }

    @Override
    public int getItemCount() {
        return myProfile.size();
    }

    @Override
    public void onClick(View v) {

        ViewHolder holder = (ViewHolder) v.getTag();
        String theString = myProfile.get(holder.getPosition());

        Toast.makeText(mContext, "Clicked: " + theString, Toast.LENGTH_SHORT).show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView profilename;
        TextView profileduration;
        TextView profilemillisvibration;
        Button profilepianobtn1;
        Button profilepianobtn2;
        Button profilepianobtn3;
        Button profilepianobtn4;
        Switch profileflashlightsetting;

        public ViewHolder(View itemView) {
            super(itemView);

            profilename = (TextView) itemView.findViewById(R.id.profilenametv);
            profileduration = (TextView) itemView.findViewById(R.id.profiledurationtv);
            profilemillisvibration = (TextView) itemView.findViewById(R.id.profiledurationtv);
            profilepianobtn1 = (Button) itemView.findViewById(R.id.profilepianobtn1);
            profilepianobtn2 = (Button) itemView.findViewById(R.id.profilepianobtn2);
            profilepianobtn3 = (Button) itemView.findViewById(R.id.profilepianobtn3);
            profilepianobtn4 = (Button) itemView.findViewById(R.id.profilepianobtn4);
            profileflashlightsetting = (Switch) itemView.findViewById(R.id.profileflashlightswitch);
        }
    }
}
