package be.urlab.Urlapp.Calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by julianschembri on 12/02/16.
 */
public class Event extends Object {
    private String title;
    private String place;
    private Date start;
    private Date stop;
    private String imageUrl;
    private String description;

    public Event(String title, String place, Date start, Date stop, String imageUrl, String description) {
        super();
        this.title = title;
        this.place = place;
        this.start = start;
        this.stop = stop;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPlace() {
        return this.place;
    }

    public String getImageUrl() { return this.imageUrl; }

    public String getDescription() { return this.description; }

    public boolean hasImage() { return this.imageUrl!="null"; }

    public String getTime() {
        String res = "";
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");

        if ((this.start.getYear()==this.stop.getYear()) && (this.start.getDay()==this.stop.getDay())) {
            res = res.concat(dayFormat.format(this.start)+", ");
            res = res.concat(hourFormat.format(this.start));
            res = res.concat(" - "+hourFormat.format(this.stop));
        } else {
            res = res.concat(dayFormat.format(this.start)+" ");
            res = res.concat(hourFormat.format(this.start)+" - ");
            res = res.concat(dayFormat.format(this.stop)+" ");
            res = res.concat(hourFormat.format(this.stop));
        }

        return res;
    }

    public boolean isPassed() {
        Date nowDate = new Date();
        return this.stop.before(nowDate);
    }

}
