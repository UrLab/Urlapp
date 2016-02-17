package utils.format;

/**
 * Created by julianschembri on 15/02/16.
 */
public class MarkDownToHtml {
    public static String format(String text) {


        text = text.replaceAll("##(.+)(\r\n)*", "<h4> $1 </h4>");
        text = text.replaceAll("#(.+)(\r\n)*", "<h3> $1 </h3>");
        text = text.replaceAll("(.+)(\r\n)*---+(\r\n)*","<h3> $1 </h3>");
        text = text.replaceAll("\\[(.+)\\]\\((.+)\\)", "<a href='$2'>$1</a>");
        text = text.replaceAll("\\*\\*(.+)\\*\\*","<b>$1</b>");
        text = text.replaceAll("\\*(.+)(\r\n)*","<li class='lvl1'>$1</li>");
        text = text.replaceAll("(    - (.+)(\r\n)*)+","<ul>$0</ul>");
        text = text.replaceAll("    - (.+)(\r\n)*","<li>$1</li>");
        text = text.replaceAll("(- (.+)(\r\n)*(<ul>(.+)</ul>)*)+","<ul>$0</ul>");
        text = text.replaceAll("- ((.+)(\r\n)*(<ul>(.+)</ul>)*)","<li>$1</li>");

        text = text.replaceAll("(\r\n)+", "<br>");

        return "<html><head><style type='text/css'>body{color: #fff;}</style></head><body>"+text+"</body></html>";
    }
}
