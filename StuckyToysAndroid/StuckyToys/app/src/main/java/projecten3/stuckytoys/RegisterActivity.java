package projecten3.stuckytoys;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends Activity implements OnClickListener{

    @BindView(R.id.editDateOfBirth)
    EditText editDateOfBirth;

    private DatePickerDialog dateOfBirthDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);

        setDateTimeField();
    }

    private void setDateTimeField()
    {
        //editDateOfBirth = (EditText)findViewById(R.id.editDateOfBirth);
        editDateOfBirth.setInputType(InputType.TYPE_NULL);
        editDateOfBirth.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        dateOfBirthDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editDateOfBirth.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //inflate the menu, this adds items to the action bar if it is present
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/


    @Override
    public void onClick(View view)
    {
        if(view == editDateOfBirth)
            dateOfBirthDialog.show();
    }
}
