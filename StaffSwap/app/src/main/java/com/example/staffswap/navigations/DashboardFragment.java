package com.example.staffswap.navigations;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.staffswap.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {

    RecyclerView sessionRecyclerView;
    List<Sessions> SessionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String UserName = sharedPreferences.getString("UserName", "");

        Log.i("username", UserName);

        TextView textViewDashboardUsername = view.findViewById(R.id.textViewDashboardUsername);
        textViewDashboardUsername.setText("Hey, "+UserName);

        sessionRecyclerView = view.findViewById(R.id.dashboardRecyclerView);
        LinearLayoutManager linearLayoutManager01 = new LinearLayoutManager(requireActivity());
        linearLayoutManager01.setOrientation(LinearLayoutManager.HORIZONTAL);
        sessionRecyclerView.setLayoutManager(linearLayoutManager01);


        SessionList = new ArrayList<>();

        loadSessions();

        return view;
    }

    private void loadSessions(){

        SessionList.add(new Sessions("8.00am - 8.40am", "class 10 A") );
        SessionList.add(new Sessions("8.40am - 9.20am", "class 11 B") );
        SessionList.add(new Sessions("9.20am - 10.00am", "class 09 A") );
        SessionList.add(new Sessions("10.00am - 10.40am", "class 07 C") );
        SessionListAdapter adapter = new SessionListAdapter(SessionList);
        sessionRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

class Sessions{

    private String time;
    private String sessionClass;

    public Sessions(String time, String sessionClass) {
        this.time = time;
        this.sessionClass = sessionClass;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSessionClass() {
        return sessionClass;
    }

    public void setSessionClass(String sessionClass) {
        this.sessionClass = sessionClass;
    }
}

class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.SessionViewHolder> {
    private final List<Sessions> sessiondetails;

    public SessionListAdapter(List<Sessions> sdetails) {
        this.sessiondetails = sdetails;
    }

    static class SessionViewHolder extends RecyclerView.ViewHolder {

        TextView sessionTime;
        TextView sessionClass;
        Button Status;
        View ContainerView;
        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionTime = itemView.findViewById(R.id.textViewSessionTime);
            sessionClass = itemView.findViewById(R.id.textViewSessionClass);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.dashboard_session_item, parent, false);
        return new SessionViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Sessions session = sessiondetails.get(position);
        holder.sessionTime.setText(session.getTime());
        holder.sessionClass.setText(session.getSessionClass());


    }

    @Override
    public int getItemCount() {
        return sessiondetails.size();
    }
}

