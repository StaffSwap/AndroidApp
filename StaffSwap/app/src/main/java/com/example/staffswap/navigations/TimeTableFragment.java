package com.example.staffswap.navigations;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.staffswap.AddTimeTableActivity;
import com.example.staffswap.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class TimeTableFragment extends Fragment {
    Spinner spinner;
    List<SessionItem> sessionItemList;
    TableListAdapter tableListAdapter;
    RecyclerView recyclerView;
    LottieAnimationView lottie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);

        spinner = view.findViewById(R.id.AddTimeTableSpinner);
        recyclerView = view.findViewById(R.id.classRecyclerview);
        lottie = view.findViewById(R.id.lottie_view02);
        FloatingActionButton add_timeTable = view.findViewById(R.id.floatingActionButtonAddTimeTable);

        add_timeTable.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AddTimeTableActivity.class);
            startActivity(intent);
        });

        ArrayList<String> days = new ArrayList<>();
        days.add("Select Day ---");
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        sessionItemList = new ArrayList<>();
        tableListAdapter = new TableListAdapter(sessionItemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(tableListAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDay = parent.getItemAtPosition(position).toString();

                    loadTimeTable(selectedDay);
                
                    sessionItemList.clear();
                    tableListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    private void loadTimeTable(String day) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("UserName", "");

        db.collection("Schedule")
                .document(userName)
                .collection("TimeTable")
                .document(day)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    sessionItemList.clear();

                    if (documentSnapshot.exists()) {
                        Map<String, String> sessions = (Map<String, String>) documentSnapshot.get("sessions");

                        if (sessions != null) {
                            List<Map.Entry<String, String>> sortedEntries = new ArrayList<>(sessions.entrySet());

                            SimpleDateFormat format = new SimpleDateFormat("h.mm a", Locale.US);

                            Collections.sort(sortedEntries, (entry1, entry2) -> {
                                try {
                                    String time1 = entry1.getKey().split(" - ")[0];
                                    String time2 = entry2.getKey().split(" - ")[0];
                                    Date date1 = format.parse(time1);
                                    Date date2 = format.parse(time2);
                                    return date1.compareTo(date2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                            });

                            for (Map.Entry<String, String> entry : sortedEntries) {
                                sessionItemList.add(new SessionItem(entry.getKey(), entry.getValue()));
                            }
                        }

                    }
                   requireActivity().runOnUiThread(() -> {
                        // directly
                        if (sessionItemList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            lottie.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            lottie.setVisibility(View.GONE);
                        }

                        tableListAdapter.notifyDataSetChanged();

                    });



                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error loading " + day + " timetable", e));
    }
}


 class SessionItem {
    private String sessionNumber;
    private String className;

    public SessionItem(String sessionNumber, String className) {
        this.sessionNumber = sessionNumber;
        this.className = className;
    }

    public String getSessionNumber() {
        return sessionNumber;
    }

    public String getClassName() {
        return className;
    }
}



 class TableListAdapter extends RecyclerView.Adapter<TableListAdapter.TableViewHolder> {
    private final List<SessionItem> sessionItems;

    public TableListAdapter(List<SessionItem> sessionItems) {
        this.sessionItems = sessionItems;
    }

    static class TableViewHolder extends RecyclerView.ViewHolder {
        TextView ClassTextView;
        TextView TimeTextView;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            ClassTextView = itemView.findViewById(R.id.ClassText);
            TimeTextView = itemView.findViewById(R.id.TimeText);
        }
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_item, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        SessionItem item = sessionItems.get(position);
        holder.ClassTextView.setText(item.getClassName());
        holder.TimeTextView.setText( item.getSessionNumber());
    }

    @Override
    public int getItemCount() {
        return sessionItems.size();
    }
}

