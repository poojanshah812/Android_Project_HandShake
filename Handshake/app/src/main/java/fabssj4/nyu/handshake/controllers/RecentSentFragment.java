package fabssj4.nyu.handshake.controllers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;

import fabssj4.nyu.handshake.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecentSentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecentSentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentSentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TABLE_USERCONTACT = "usercontact";
    private static final String COL_USERCONTACT_ID = "contactid";
    private static final String COL_USERCONTACT_CONTACT = "contact";
    private static final String COL_USERCONTACT_NAME = "name";
    private static final String COL_USERCONTACT_LASTSENTTIMESTAMP = "lastsenttimestamp";
    private static final String COL_USERCONTACT_LASTSENTIP = "lastsentip";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private HandshakeAppDbHelper hadh;

    private RVAdapter mAdapter;
    private ArrayList<String> myDataset;
    private RecyclerView mRecycler;

    // An array of meaningless titles
    private static final String[] someTitles = {
            "This is a Title",
            "Another Short Title",
            "Here's a Much Longer Title for Everyone to Deal With!!",
            "Coming Soon...",
            "Hey Bud, How About That Local Sports Team?",
            "What's Up Pussycat? Whoaaa"
    };

    // For randomizing titles across our dataset
    private Random randy = new Random();

    private OnFragmentInteractionListener mListener;

    public RecentSentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecentSentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentSentFragment newInstance(String param1, String param2) {
        RecentSentFragment fragment = new RecentSentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_recent_sent, container, false);
        mRecycler = (RecyclerView) view.findViewById(R.id.rvSentGrid);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        myDataset = new ArrayList<String>();


        hadh = new HandshakeAppDbHelper(getContext());
        LinkedHashMap<Integer, HashMap<String, String>> lhp = hadh.getRecentSent();


        if (lhp == null)
            myDataset.add("NO Objects");
        else {
            Iterator<HashMap<String, String>> iterator = lhp.values().iterator();
            while (iterator.hasNext()) {
                HashMap<String, String> currenthmap = iterator.next();
                myDataset.add(currenthmap.get(COL_USERCONTACT_NAME));
            }
        }


        // Load up the dataset with random titles
        /*for (int x = 0; x < 50; x++) {
            myDataset.add(someTitles[randy.nextInt(someTitles.length)]);
        }*/

        mAdapter = new RVAdapter(getContext(), myDataset);


        // Set the RecyclerView's Adapter
        mRecycler.setAdapter(mAdapter);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
