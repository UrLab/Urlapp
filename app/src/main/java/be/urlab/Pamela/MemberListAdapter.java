package be.urlab.Pamela;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by julianschembri on 7/02/16.
 */

public class MemberListAdapter extends ArrayAdapter<Member> {
    private Context context;
    private List<Member> objects;

    public MemberListAdapter(Context context, List<Member> objects) {
        super(context,-1, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.member_item, parent, false);
        TextView userNameView = (TextView) rowView.findViewById(R.id.username);
        ImageView hasKeyView = (ImageView) rowView.findViewById(R.id.has_key);
        Member member = (Member) objects.get(position);
        userNameView.setText(member.getUserName());
        if (member.hasKey()) {
            hasKeyView.setVisibility(View.VISIBLE);
        } else {
            hasKeyView.setVisibility(View.INVISIBLE);
        }
        ImageView gravatarNameView = (ImageView) rowView.findViewById(R.id.gravatar);
        new DownloadImageTask(gravatarNameView).execute("http:"+member.getGravatar());
        return rowView;
    }

}
