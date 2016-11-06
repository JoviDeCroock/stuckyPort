package projecten3.stuckytoys;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import projecten3.stuckytoys.adapters.MembersAdapter;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Member;
import projecten3.stuckytoys.domain.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMemberActivity extends AppCompatActivity {

    @BindView(R.id.gridImages)
    GridView gridImages;

    @BindView(R.id.editFirstName)
    EditText editFirstName;

    @BindView(R.id.editNickname)
    EditText editNickname;

    @BindView(R.id.editRole)
    EditText editRole;

    @BindView(R.id.editDateOfBirth)
    TextView editDateOfBirth;

    @BindView(R.id.checkboxAuthority)
    CheckBox checkAuthority;

    @BindView(R.id.txtError)
    TextView txtError;

    @BindView(R.id.btnAddMember)
    Button btnAddMember;

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
        btnAddMember.setClickable(false);

        final String firstName = editFirstName.getText().toString();
        final String nickname = editNickname.getText().toString();
        final String role = editRole.getText().toString();
        String dateOfBirthString = editDateOfBirth.getText().toString();
        final boolean authority = checkAuthority.isChecked();

        //alles ingevuld?
        if( firstName.isEmpty() || nickname.isEmpty() || role.isEmpty() || dateOfBirthString.isEmpty()) {
            txtError.setText(R.string.fill_all_fields);
            btnAddMember.setClickable(true);
        } else if (selectedImageString.isEmpty()) {
                txtError.setText(R.string.didnt_select_image);
            btnAddMember.setClickable(true);
        } else {
            try {
                DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                Date dateOfBirth = df.parse(dateOfBirthString);
                dateOfBirthString = dateOfBirthString.replaceAll("-", " ");
                Call<Member> call = dc.addMember(firstName, nickname, role, authority, dateOfBirthString, selectedImageString);

                call.enqueue(new Callback<Member>() {

                    //Call to API using Retrofit; User as model; Body requires username, email & password;
                    //API returns a token; token is used to authenticate. also decoded token = id + exp + iat
                    //example token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ODE3ODU0YjE0NTRjNDFhODJlNmM3NzgiLCJleHAiOjE0ODMxOTc2NjYsImlhdCI6MTQ3ODAxMzY2Nn0.iDs223_K8SrtQlDHos5k1r8uRh8Pzq4-axjvZRPID4o
                    //decoded example token: {"_id":"5817854b1454c41a82e6c778","exp":1483197666,"iat":1478013666}
                    @Override
                    public void onResponse(Call<Member> call, Response<Member> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(AddMemberActivity.this, SelectMemberActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            txtError.setText(response.message());
                            Log.e("register", response.code() + " " + response.message());
                        }
                        btnAddMember.setClickable(true);
                    }

                    @Override
                    public void onFailure(Call<Member> call, Throwable t) {
                        txtError.setText("a");
                        t.printStackTrace();
                        btnAddMember.setClickable(true);
                    }
                });
            } catch (ParseException e) {
                Log.e("addmember", "Error parsing date");
                e.printStackTrace();
                btnAddMember.setClickable(true);
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
