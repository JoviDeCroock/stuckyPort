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

import projecten3.stuckytoys.AddMemberActivity;
import projecten3.stuckytoys.R;
import projecten3.stuckytoys.SelectMemberActivity;
import projecten3.stuckytoys.custom.ResourceHelper;
import projecten3.stuckytoys.domain.Member;

public class MemberAdapter extends BaseAdapter {
    private List<Member> members = new ArrayList<Member>();
    private final LayoutInflater mInflater;
    private Context context;

    private int bgSelected = ResourceHelper.getResId("imageview_border_selected", R.drawable.class);
    private int bgNotSelected = ResourceHelper.getResId("imageview_border", R.drawable.class);

    //both used only in addmember; selectedImageString necessary in case of screen getting rotated
    private ImageView selectedPicture; private String selectedImageString;

    //addmemberactivity constructor
    public MemberAdapter(Context context, String selectedImageString, List<Member> members) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.members = members;
        this.selectedImageString = selectedImageString;

        //setting selectedPicture to a new never-used ImageView to avoid null-reference calls
        selectedPicture = new ImageView(context);
    }

    //selectmemberactivity constructor
    //plusText = add_member string from resources; passed in SelectMemberActivity because getResources() doesn't work if not inside an activity
    //starting to get the feeling i shoulda used two different adapters for selectmemberactivity & addmemberactivity...
    public MemberAdapter(Context context, List<Member> members, String plusText) {
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
        final ImageView picture;
        TextView name;
        boolean addMemberActivity = context instanceof AddMemberActivity;

        final Member member = getItem(i);

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.itemImage, v.findViewById(R.id.itemImage));
            if (addMemberActivity) {
                TextView txtMemberName = (TextView) v.findViewById(R.id.txtItemName);
                txtMemberName.setVisibility(View.GONE);
            } else {
                v.setTag(R.id.txtItemName, v.findViewById(R.id.txtItemName));
                name = (TextView) v.getTag(R.id.txtItemName);
                name.setText(member.getNickname());
            }
        }

        picture = (ImageView) v.getTag(R.id.itemImage);

        //interestingly i haven't managed to find a better way to get a resource id (ex "R.drawable.bever") from a string
        //so i'm using a helper class for this; view ResourceHelper class for more info
        picture.setImageResource(ResourceHelper.getResId(member.getPicture(), R.drawable.class));

        if(addMemberActivity) {
            //if screen rotates: addmemberactivity's onCreate() method is called anew: new membersadapter is created
            //so if an image was selected before rotating, this will reselect that image after rotating
            //seems like a lot of extra resources just to do this though so maybe TODO: makes this simpler? (maybe use fragment to store images)
            if (member.getPicture().equals(selectedImageString)) {
                picture.setBackgroundResource(bgSelected);
                selectedPicture = picture;
            } else {
                picture.setBackgroundResource(bgNotSelected);
            }

            //when view(= text+image) is clicked, alert AddMemberActivity
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddMemberActivity mContext = (AddMemberActivity) context;
                    mContext.itemClicked(member);
                    selectedPicture.setBackgroundResource(bgNotSelected);
                    picture.setBackgroundResource(bgSelected);
                    selectedPicture = picture;
                }
            });
        } else {
            //when view(= text+image) is clicked, alert SelectMemberActivity
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectMemberActivity mContext = (SelectMemberActivity) context;
                    mContext.itemClicked(member);
                }
            });
        }

        return v;
    }
}
