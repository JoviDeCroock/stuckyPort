package projecten3.stuckytoys.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import projecten3.stuckytoys.R;
import projecten3.stuckytoys.SelectMemberActivity;
import projecten3.stuckytoys.custom.ResourceHelper;
import projecten3.stuckytoys.domain.Member;

public class MembersAdapter extends BaseAdapter {
    private List<Member> members = new ArrayList<Member>();
    private final LayoutInflater mInflater;
    private Context context;

    //plusText = add_member string from resources; passed in SelectMemberActivity because getResources() doesn't work if not inside an activity
    public MembersAdapter(Context context, List<Member> members, String plusText) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.members = members;
        members.add(new Member(plusText, "plus_sign.png"));
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Member getItem(int i) {
        return members.get(i);
    }

    //Implementation for extended class BaseAdapter; method isn't needed for now & i can't figure out where to get any long id from
    @Override
    public long getItemId(int i) {
        Log.e("MembersAdapter", "getItemId was somehow called; where?");
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //TODO(maybe):can butterknife be used for this?? both for the binding and the onclicklistener??
        View v = view;
        ImageView picture;
        TextView name;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_member, viewGroup, false);
            v.setTag(R.id.memberImage, v.findViewById(R.id.memberImage));
            v.setTag(R.id.txtMemberName, v.findViewById(R.id.txtMemberName));
        }

        picture = (ImageView) v.getTag(R.id.memberImage);
        name = (TextView) v.getTag(R.id.txtMemberName);
        final Member member = getItem(i);

        //interestingly i haven't managed to find a better way to get a resource id (ex "R.drawable.bever") from a string
        //so i'm using a helper class for this; view ResourceHelper class for more info
        picture.setImageResource(ResourceHelper.getResId(member.getPicture(), R.drawable.class));
        name.setText(member.getNickname());

        //when view(= text+image) is clicked, alert SelectMemberActivity
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectMemberActivity mContext = (SelectMemberActivity) context;
                mContext.itemClicked(member);
            }
        });

        return v;
    }
}
