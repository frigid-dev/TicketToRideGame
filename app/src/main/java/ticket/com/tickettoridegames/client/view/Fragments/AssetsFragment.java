package ticket.com.tickettoridegames.client.view.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import ticket.com.tickettoridegames.R;
import ticket.com.tickettoridegames.client.model.ClientModel;

public class AssetsFragment extends BasicFragment implements  IAssetsFragment{

    @Override
    public BasicFragment provideYourFragment() {

        return new AssetsFragment();
    }

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

//        View view = inflater.inflate(R.layout.assets_fragment,parent,false); //todo error here

        //Get your parent layout of fragment
//        RelativeLayout layout = (RelativeLayout)view;  //todo error here

        //Now specific components here
//        ListView routes = (ListView)findViewById(R.id.routes);
//        ListView ownedTrains = (ListView)findViewById(R.id.ownedTrains);
//        ListView trainBank = (ListView)findViewById(R.id.trainBank);
//
//        ListView playerList = findViewById(R.id.listViewPlayers);
//        adapter=new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1,
//                listItems);
//        playerList.setAdapter(adapter);


//        return view;  //todo error here
        return null;
    }


}