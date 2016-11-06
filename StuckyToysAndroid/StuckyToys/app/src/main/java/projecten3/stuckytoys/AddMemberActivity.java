package projecten3.stuckytoys;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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

public class AddMemberActivity extends AppCompatActivity {

    @BindView(R.id.editDateOfBirth)
    TextView editDateOfBirth;

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
    private String selectedImageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        ButterKnife.bind(this);

        dc = DomainController.getInstance();
        if (savedInstanceState != null) {
            selectedImageString = savedInstanceState.getString("selectedImageString");
        } else {
            selectedImageString = "";
        }

        //TODO:backend! hardcoded for now
        List<Member> members = new ArrayList();
        members.add(new Member("Bever", "bever.png"));
        members.add(new Member("Wasbeer", "wasbeer.png"));
        members.add(new Member("Geit", "geit.png"));
        mAdapter = new MembersAdapter(this, selectedImageString, members);
        gridImages.setAdapter(mAdapter);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        setDateTimeField();

        //upon pressing "next" on keyboard after entering nickname: show calendar dialog
        editNickname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                showDobDialog();
                return true;
            }
        });
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        dateOfBirthDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editDateOfBirth.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @OnClick(R.id.editDateOfBirth)
    public void showDobDialog() {
        dateOfBirthDialog.show();

        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @OnClick(R.id.btnAddMember)
    public void addMember() {
        txtError.setText("");

        //alles ingevuld?
        if( editFirstName.getText().toString().isEmpty()||
                editNickname.getText().toString().isEmpty() ||
                editDateOfBirth.getText().toString().isEmpty()) {
            txtError.setText(R.string.fill_all_fields);
        } else {
            if (selectedImageString.isEmpty()) {
                txtError.setText(R.string.didnt_select_image);
            } else {
                //TODO
            }
        }
    }

    public void itemClicked(Member member) {
        selectedImageString = member.getPicture();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.add_member_cancel)
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("selectedImageString", selectedImageString);
        super.onSaveInstanceState(savedInstanceState);
    }

}
