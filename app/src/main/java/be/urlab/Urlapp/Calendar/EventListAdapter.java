package be.urlab.Urlapp.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import be.urlab.Urlapp.R;
import utils.image.ImageAnalyzer;
import utils.web.DownloadImageTask;

/**
 * Created by julianschembri on 12/02/16.
 */
public class EventListAdapter extends ArrayAdapter<Event> {
    private Context context;
    private List<Event> objects;

    private class ImageDownloader extends DownloadImageTask {
        public ImageDownloader(ImageView bmImage) {
            super(bmImage);
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            int width = result.getWidth();
            int height = result.getHeight();
            if ((width>height) && ((width/height)>=1)) {
                bmImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (ImageAnalyzer.isDark(result)) {
                    ViewGroup viewGroup = (ViewGroup) bmImage.getParent();
                    TextView titleView = (TextView) viewGroup.findViewById(R.id.title);
                    titleView.setTextColor(Color.parseColor("#FFFFFF"));
                    TextView placeView = (TextView) viewGroup.findViewById(R.id.place);
                    placeView.setTextColor(Color.parseColor("#D8D8D8"));
                    TextView dateView = (TextView) viewGroup.findViewById(R.id.date);
                    dateView.setTextColor(Color.parseColor("#D8D8D8"));
                    LinearLayout layoutV = (LinearLayout) viewGroup.findViewById(R.id.event_layout_v);
                    if (dateView.getTextAlignment()==View.TEXT_ALIGNMENT_TEXT_END) {
                        layoutV.setBackground(layoutV.getResources().getDrawable(R.drawable.event_layout_bg_dr));
                    } else {
                        layoutV.setBackground(layoutV.getResources().getDrawable(R.drawable.event_layout_bg_dl));
                    }
                } else {
                    ViewGroup viewGroup = (ViewGroup) bmImage.getParent();
                    TextView titleView = (TextView) viewGroup.findViewById(R.id.title);
                    LinearLayout layoutV = (LinearLayout) viewGroup.findViewById(R.id.event_layout_v);
                    if (titleView.getTextAlignment()==View.TEXT_ALIGNMENT_TEXT_END) {
                        layoutV.setBackground(layoutV.getResources().getDrawable(R.drawable.event_layout_bg_lr));
                    } else {
                        layoutV.setBackground(layoutV.getResources().getDrawable(R.drawable.event_layout_bg_ll));
                    }
                }
            }
        }
    }

    public List<Event> getObjects() {
        return this.objects;
    }

    public EventListAdapter(Context context, List<Event> objects) {
        super(context,-1, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.event_item, parent, false);
        Event anEvent = (Event) this.objects.get(position);
        TextView titleView = (TextView) rowView.findViewById(R.id.title);
        TextView placeView = (TextView) rowView.findViewById(R.id.place);
        TextView dateView = (TextView) rowView.findViewById(R.id.date);
        LinearLayout layout = (LinearLayout) rowView.findViewById(R.id.event_layout_h);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.event_image);

        titleView.setText(anEvent.getTitle());
        placeView.setText(anEvent.getPlace());
        if (!anEvent.isIncubated()) {
            dateView.setText(anEvent.getTime());
        } else {
            dateView.setVisibility(View.GONE);
        }

        if (anEvent.hasImage()) {
            new ImageDownloader(imageView).execute(anEvent.getImageUrl());
            if (position%2==0) {
                imageView.setScaleType(ImageView.ScaleType.FIT_START);
            } else {
                imageView.setScaleType(ImageView.ScaleType.FIT_END);
            }
        }

        if (position%2==0){
            titleView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            placeView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            dateView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END,RelativeLayout.TRUE);
            layout.setLayoutParams(layoutParams);
        }
        if (anEvent.isPassed()) {
            rowView.setBackgroundColor(rowView.getResources().getColor(R.color.calendar_item_foreground));
        }

        return rowView;
    }

}
