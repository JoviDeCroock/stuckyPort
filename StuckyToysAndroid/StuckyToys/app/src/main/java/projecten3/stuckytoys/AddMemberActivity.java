package projecten3.stuckytoys;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import projecten3.stuckytoys.adapters.MembersAdapter;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Member;

public class AddMemberActivity extends AppCompatActivity implements OnClickListener {

    @BindView(R.id.editDateOfBirth)
    EditText editDateOfBirth;

    @BindView(R.id.editFirstName)
    EditText editFirstName;

    @BindView(R.id.editNickname)
    EditText editNickname;

    @BindView(R.id.gridImages)
    GridView gridImages;

    @BindView(R.id.txtError)
    TextView txtError;

    private DatePickerDialog dateOfBirthDialog;
    private SimpleDateFormat dateFormatter;

    private DomainController dc;
    private MembersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        ButterKnife.bind(this);

        dc = DomainController.getInstance();

        List<Member> members = new ArrayList();
        members.add(new Member("Bever", "bever.png"));
        members.add(new Member("Wasbeer", "wasbeer.png"));
        members.add(new Member("Geit", "geit.png"));
        mAdapter = new MembersAdapter(this, members);
        gridImages.setAdapter(mAdapter);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        setDateTimeField();
    }

    private void setDateTimeField()
    {
        //editDateOfBirth = (EditText)findViewById(R.id.editDateOfBirth);
        editDateOfBirth.setInputType(InputType.TYPE_NULL);
        editDateOfBirth.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        dateOfBirthDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editDateOfBirth.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View view)
    {
        if(view == editDateOfBirth)
            dateOfBirthDialog.show();
    }

    @OnClick(R.id.btnAddMember)
    public void addMember() {
        txtError.setText("");

        //alles ingevuld?
        if( editFirstName.getText().toString().isEmpty()||
                editNickname.getText().toString().isEmpty() ||
                editDateOfBirth.getText().toString().isEmpty()) {
            txtError.setText("Gelieve alle gegevens in te vullen.");
            return;
        }
    }

    public void itemClicked(Member member) {
        if (member.getPicture().equals("plus_sign.png")) {
            Toast.makeText(this, "plus sign clicked", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Member clicked: " +
                            member.getFirstName() + "\n" +
                            member.getNickname() + "\n" +
                            member.getRole() + "\n" +
                            member.getDateOfBirth() + "\n" +
                            member.getId() + "\n" +
                            member.getPicture() + "\n",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.logout)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

}
