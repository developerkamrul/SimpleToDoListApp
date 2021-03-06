package falcon_007.kamrulhasan.simpletodolistapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import data.DatabaseHandler;
import data.ToDoList;

public class TodoEditorActivity extends AppCompatActivity {

    //----xml field initialization--------
    private EditText taskNameEditText;
    private EditText taskDescriptionEditText;
    private TextView dateTextView,timeTextView;

    //----data handler class called
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_editor);
        getSupportActionBar().setTitle("ToDo Editor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        taskNameEditText = (EditText) findViewById(R.id.taskNameEditText);
        taskDescriptionEditText = (EditText) findViewById(R.id.taskDescriptionEditText);
        dateTextView = (TextView) findViewById(R.id.activity_editor_dateText);
        timeTextView = (TextView) findViewById(R.id.activity_editor_timeText);

    }

    /*----------manipulate set date button with date picker-------*/
    public void setDate(View view){
        final Calendar calendar = Calendar.getInstance();
        int cYear = calendar.get(Calendar.YEAR);
        int cMonth = calendar.get(Calendar.MONTH);
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateTextView.setText(dayOfMonth+"-"+month+"-"+year);
            }
        },cYear,cMonth,cDay);
        datePickerDialog.show();
    }

    /*----------manipulate set time button with time picker-------*/
    public void setTime(View view){
        final Calendar calendar = Calendar.getInstance();
        int cHour = calendar.get(Calendar.HOUR_OF_DAY);
        int cMinutes = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String AM_PM;
                if (hourOfDay<12){
                    AM_PM = "AM";
                }else {
                    AM_PM = "PM";
                }
                timeTextView.setText(hourOfDay+":"+minute+" "+AM_PM);
            }
        },cHour,cMinutes,true);
        timePickerDialog.show();
    }

    /*------save data in database---------*/
    public void saveToDoInDb(){
        databaseHandler = new DatabaseHandler(getApplicationContext());
        if (!taskNameEditText.getText().toString().equals("") && !taskDescriptionEditText.getText().toString().equals("")){

            ToDoList list = new ToDoList();
            list.setTaskName(taskNameEditText.getText().toString());
            list.setTaskDescription(taskDescriptionEditText.getText().toString());
            list.setTimeAndDateText("date : " + dateTextView.getText().toString() + " , " + "time : " + timeTextView.getText().toString());
            databaseHandler.addTaskOnDb(list);
            databaseHandler.close();
            Toast.makeText(getApplicationContext(),"Save!!",Toast.LENGTH_SHORT).show();
            taskNameEditText.setText("");
            taskDescriptionEditText.setText("");
            timeTextView.setText("");
            dateTextView.setText("");
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }else {
            taskNameEditText.setError("Please put task name");
            taskDescriptionEditText.setError("Please put task Description");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done:
                saveToDoInDb();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //---CONFIGURE SAVE BUTTON
    public void save(View view){
        saveToDoInDb();
    }
    //------CONFIGURE CANCEL BUTTON
    public void cancel(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
