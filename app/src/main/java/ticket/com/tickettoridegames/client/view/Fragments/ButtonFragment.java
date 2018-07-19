package ticket.com.tickettoridegames.client.view.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import ticket.com.tickettoridegames.R;

/**
 * Created by Illuminati on 4/22/2017.
 */
public class ButtonFragment extends BasicFragment{
    @Override
    public BasicFragment provideYourFragment() {

        return new ButtonFragment();
    }

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_a,parent,false);

        //Get your parent layout of fragment
        RelativeLayout layout = (RelativeLayout)view;

        //Now specific components here

        Button b1 = (Button)layout.findViewById(R.id.button1);
        Button b2 = (Button)layout.findViewById(R.id.button2);


        return view;

    }


}