package projecten3.stuckytoys;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import projecten3.stuckytoys.adapters.MembersAdapter;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Member;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectMemberActivity extends AppCompatActivity {

    @BindView(R.id.gridMembers) GridView gridView;
    @BindView(R.id.txtError) TextView txtError;

    private DomainController dc;
    private MembersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_member);

        ButterKnife.bind(this);

        dc = DomainController.getInstance();

        //TODO: Get from backend
        //hardcoded!
        List<Member> members = new ArrayList();
        members.add(new Member("Test", "bever.png"));
        members.add(new Member("teadae", "placerholderpic.png"));
        members.add(new Member("dcddvq", "placerholderpic.png"));
        members.add(new Member("qsf xvqgacbf", "geit.png"));

        // getting the add_member string from resources here because getResources() doesn't work if not inside an activity
        String plusText = getResources().getString(R.string.add_member);
        mAdapter = new MembersAdapter(this, members, plusText);

        gridView.setAdapter(mAdapter);

        fillMembers();

    }

    // called when griditem is clicked
    // redirects to homepage of clicked member OR to new member page if plus sign clicked
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

    private void fillMembers() {
        Call<List<Member>> call = dc.getAllMembers();
        final Context context = this;
        call.enqueue(new Callback<List<Member>>() {

            //TODO: document
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                if (response.isSuccessful()) {
                    List<Member> rMembers = response.body();
                    dc.getUser().setMembers(rMembers);

                    //getting the add_member string from resources here because getResources() doesn't work if not inside an activity
                    String plusText = getResources().getString(R.string.add_member);
                    mAdapter = new MembersAdapter(context, rMembers, plusText);
                    gridView.setAdapter(mAdapter);
                } else {
                    txtError.setText(response.message());
                    Log.e("members", response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Member>> call, Throwable t) {
                txtError.setText("a");
                t.printStackTrace();
            }
        });
    }
}
