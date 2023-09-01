import org.apache.commons.lang3.StringEscapeUtils;

public class TestEscape {

    public static void main(String[] args) {
        System.out.println(StringEscapeUtils.unescapeJava("\\\"c1\\\""));
        System.out.println(StringEscapeUtils.unescapeJava("\\\"c1\\\""));
        System.out.println(StringEscapeUtils.unescapeJava("\\\"\\\\\\\"\\\"c1\\\\\\\"\\\"\\\""));
    }
}
