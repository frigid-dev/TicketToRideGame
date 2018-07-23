package ticket.com.tickettoridegames.client.view.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import ticket.com.tickettoridegames.R;
import ticket.com.tickettoridegames.client.presenter.IMapPresenter;
import ticket.com.tickettoridegames.client.presenter.MapPresenter;
import ticket.com.tickettoridegames.client.view.IMapView;

public class MapFragment extends BasicFragment implements IMapView{
    // Widgets


    // Variables
    View view;
    private IMapPresenter presenter;

    @Override
    public BasicFragment provideYourFragment() {

        return new MapFragment();
    }

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.map_fragment,parent,false);
        presenter = new MapPresenter(this);

        //Now specific components here
        Button drawTrainsButton = (Button)view.findViewById(R.id.button1);
        drawTrainsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //presenter.sendMessage(chat_input.getText().toString());
            }
        });

        Button placeTrainsButton = (Button)view.findViewById(R.id.button2);
        placeTrainsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //presenter.sendMessage(chat_input.getText().toString());
            }
        });

        Button drawRoutesButton = (Button)view.findViewById(R.id.button3);
        drawRoutesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //presenter.sendMessage(chat_input.getText().toString());
            }
        });

        Button turnButton = (Button)view.findViewById(R.id.button3);
        turnButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //presenter.sendMessage(chat_input.getText().toString());
            }
        });

        Button passOffButton = (Button)view.findViewById(R.id.pass_off_button);
        drawTrainsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                presenter.passOff();
            }
        });

        return view;
    }
}