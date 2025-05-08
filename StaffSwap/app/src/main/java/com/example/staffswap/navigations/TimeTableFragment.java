package com.example.staffswap.navigations;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.staffswap.R;

import java.util.ArrayList;
import java.util.List;


public class TimeTableFragment extends Fragment {
Spinner spinner;
    List<Table01> table01List;
    TableListAdapter table01ListAdapter;
RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view =inflater.inflate(R.layout.fragment_time_table, container, false);
        spinner = view.findViewById(R.id.AddTimeTableSpinner);
        recyclerView = view.findViewById(R.id.classRecyclerview);


        ArrayList<String> leaveTypes = new ArrayList<>();
        leaveTypes.add("Select Day ---");
        leaveTypes.add("Monday");
        leaveTypes.add("Tuesday");
        leaveTypes.add("Wednesday");
        leaveTypes.add("Thursday");
        leaveTypes.add("Friday");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                leaveTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLeave = parent.getItemAtPosition(position).toString();
                // Do something with selectedLeave
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
            }
        });

        LinearLayoutManager linearLayoutManager01 = new LinearLayoutManager(requireActivity());
        linearLayoutManager01.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager01);
        table01List = new ArrayList<>();
        table01ListAdapter = new TableListAdapter(table01List);
        recyclerView.setAdapter(table01ListAdapter);

        loadTable();
        return view;
    }
    private void loadTable(){

        table01List.add(new Table01("11-A", "8.00 - 9.00"));
        table01List.add(new Table01("11-B", "9.00 - 10.00"));
        table01List.add(new Table01("11-C", "10.00 - 11.00"));
        table01List.add(new Table01("11-E", "11.00 - 12.00"));
        table01List.add(new Table01("11-D", "12.00 - 1.00"));

        table01ListAdapter.notifyDataSetChanged();
    }
}
class Table01{


    private String ClassName;
    private String Time;


    public Table01(String className,String time) {

        ClassName = className;
        Time = time;
    }
    public String getClassName() {
        return ClassName;
    }
    public void setClassName(String className) {
        ClassName = className;
    }
    public String getTime() {
        return Time;
    }
    public void setTime(String time) {
        Time = time;
    }

}
class TableListAdapter extends RecyclerView.Adapter<TableListAdapter.TableViewHolder> {
    private final List<Table01> tabledetails;

    public TableListAdapter(List<Table01> tdetails) {
        this.tabledetails = tdetails;
    }

    static class TableViewHolder extends RecyclerView.ViewHolder {

        TextView ClassTextView;
        TextView TimeTextView;

        View ContainerView;
        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            ClassTextView = itemView.findViewById(R.id.ClassText);
            TimeTextView = itemView.findViewById(R.id.TimeText);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.class_item, parent, false);
        return new TableViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        Table01 TDetails = tabledetails.get(position);
        holder.ClassTextView.setText(TDetails.getClassName());
        holder.TimeTextView.setText(TDetails.getTime());

    }

    @Override
    public int getItemCount() {
        return tabledetails.size();
    }
}