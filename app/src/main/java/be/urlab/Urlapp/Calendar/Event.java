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
    private String status;

    public Event(String title, String place, Date start, Date stop, String imageUrl, String description, String status) {
        super();
        this.title = title;
        this.place = place;
        this.start = start;
        this.stop = stop;
        this.imageUrl = imageUrl;
        this.description = description;
        this.status = status;
    }

    public String getTitle() { return this.title; }
    public String getPlace() {
        return this.place;
    }
    public String getImageUrl() { return this.imageUrl; }
    public String getDescription() { return this.description; }
    public String getStatus() { return this.status; }

    public boolean hasImage() { return this.imageUrl!="null"; }
    public boolean isIncubated() { return this.status.equals("i"); }

    public String getTime() {
        String res = "";
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");

        if ((start.getYear()==stop.getYear()) && (start.getDay()==stop.getDay())) {
            res = res.concat(dayFormat.format(start)+", ");
            res = res.concat(hourFormat.format(start));
            res = res.concat(" - "+hourFormat.format(stop));
        } else {
            res = res.concat(dayFormat.format(start)+" ");
            res = res.concat(hourFormat.format(start)+" - ");
            res = res.concat(dayFormat.format(stop)+" ");
            res = res.concat(hourFormat.format(stop));
        }

        return res;
    }

    public boolean isPassed() {
        return !isIncubated() && stop.before(new Date());
    }

}
