package com.example.staffswap.navigations;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.staffswap.R;
import com.example.staffswap.UserLoginActivity;
import com.example.staffswap.model.CustomAlert;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestLeaveFragment extends Fragment {

    Spinner spinner;
    List<Leave> LeaveList;
    LeaveListAdapter leaveListAdapter;
    RecyclerView recyclerView;
    CalendarView calendarView;
    String selectedLeaveType ,selectedDate, leaveDescription;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_request_leave, container, false);

        calendarView = view.findViewById(R.id.calendarView01);
        TextView dateTextView = view.findViewById(R.id.SelectDateTV);
        EditText leaveReason = view.findViewById(R.id.Leavediscription);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                dateTextView.setText(selectedDate);
            }
        });

        spinner = view.findViewById(R.id.LeaveSpinner);
        recyclerView = view.findViewById(R.id.LeaveRecylerView);
        Button addLeaveButton = view.findViewById(R.id.RequestLeaveSubmitBtn);

        addLeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveDescription = leaveReason.getText().toString();

                if (selectedLeaveType.equals("Select Leave Type ---")) {
                    CustomAlert.showCustomAlert(requireActivity(),"Error ","Please select a Leave Type",R.drawable.cancel);
                } else if (selectedDate == null) {
                    CustomAlert.showCustomAlert(requireActivity(),"Error ","Please select a date",R.drawable.cancel);
                } else if (leaveDescription.isEmpty()) {
                    CustomAlert.showCustomAlert(requireActivity(),"Error ","Please enter a description",R.drawable.cancel);
                } else {
                    Log.e("Leave Request", "Leave Type: " + selectedLeaveType + ", Date: " + selectedDate + ", Description: " + leaveDescription);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Map<String, Object> Leave = new HashMap<>();
                    Leave.put("User", "Ravishka");
                    Leave.put("LeaveType", selectedLeaveType);
                    Leave.put("LeaveDate", selectedDate);
                    Leave.put("LeaveDescription", leaveDescription);

                    db.collection("Leave")
                            .add(Leave)
                            .addOnSuccessListener(documentReference -> {
                                CustomAlert.showCustomAlert(getContext(),"Success","Successfully Add Field",R.drawable.checked);
//                                jobFiled.setText("");
//                                refresh();

                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(),"Job field added Error" ,Toast.LENGTH_SHORT).show();
                                CustomAlert.showCustomAlert(getContext(),"Error","Job field added Error",R.drawable.cancel);

                            });

                }

            }
        });

        ArrayList<String> leaveTypes = new ArrayList<>();
        leaveTypes.add("Select Leave Type ---");
        leaveTypes.add("Casual Leave");
        leaveTypes.add("Sick Leave");
        leaveTypes.add("Annual Leave");
        leaveTypes.add("Maternity Leave");
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
                selectedLeaveType = parent.getItemAtPosition(position).toString();
                Log.e("Selected Leave Type", selectedLeaveType);
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
        LeaveList = new ArrayList<>();
        leaveListAdapter = new LeaveListAdapter(LeaveList);
        recyclerView.setAdapter(leaveListAdapter);

        loadLeaves();

        return view;
    }
    private void loadLeaves(){

        LeaveList.add(new Leave("John Doe", "2023-10-00", "Pending"));
        LeaveList.add(new Leave("John Doe", "2023-10-01", "Pending"));
        LeaveList.add(new Leave("John Doe", "2023-10-02", "Pending"));
        LeaveList.add(new Leave("John Doe", "2023-10-02", "Pending"));
        LeaveList.add(new Leave("John Doe", "2023-10-06", "Pending"));
        leaveListAdapter.notifyDataSetChanged();
    }
}




class Leave{

    private String name;
    private String date;
    private String status;

    public Leave( String name, String date, String status) {

        this.name = name;
        this.date = date;
        this.status = status;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
class LeaveListAdapter extends RecyclerView.Adapter<LeaveListAdapter.LeaveViewHolder> {
    private final List<Leave> leavedetails;

    public LeaveListAdapter(List<Leave> ldetails) {
        this.leavedetails = ldetails;
    }

    static class LeaveViewHolder extends RecyclerView.ViewHolder {

        TextView LeaveNameTextView;
        TextView LeaveDateTextView;
        Button Status;
        View ContainerView;
        public LeaveViewHolder(@NonNull View itemView) {
            super(itemView);
            LeaveNameTextView = itemView.findViewById(R.id.LeaveNameTV);
            LeaveDateTextView = itemView.findViewById(R.id.LeaveDateTV);
            Status = itemView.findViewById(R.id.LeaveStatusBtn);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public LeaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.leave_item, parent, false);
        return new LeaveViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull LeaveViewHolder holder, int position) {
        Leave LDetails = leavedetails.get(position);
        holder.LeaveNameTextView.setText(LDetails.getName());
        holder.LeaveDateTextView.setText(LDetails.getDate());
        holder.Status.setText(LDetails.getStatus());


    }

    @Override
    public int getItemCount() {
        return leavedetails.size();
    }
}