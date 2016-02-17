package utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by julianschembri on 12/02/16.
 */
public class DateUtil {

    public static java.util.Date fromString(String date,String format) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.parse(date);
    }

}
