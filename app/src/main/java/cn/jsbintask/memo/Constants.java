package cn.jsbintask.memo;


public class Constants {
    public static final String EMPTY = "";
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_CHINESE_TIME_FORMAT = "yyyy년MM월dd일 HH:mm";
    public static final String CLOCK_RECEIVER_ACTION = "cn.jsbintask.memo.action.CLOCK_RECEIVER";

    public interface EventFlag {
        int IMPORTANT = 1;
        int NORMAL = 0;
    }

    public interface MemoIconTag {
        int FIRST = 1;
        int OTHER = 2;
    }

    public interface EventClockFlag {
        int NONE = 0;
        int CLOCKED = 10;
    }

    public static final int HANDLER_SUCCESS = 0x0001;
    public static final int HANDLER_FAILED = 0x0000;
}
