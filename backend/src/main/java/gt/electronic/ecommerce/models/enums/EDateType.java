package gt.electronic.ecommerce.models.enums;

public enum EDateType {
    DAY(Names.DAY_LABEL, Names.DAY_TIME),
    WEEK(Names.WEEK_LABEL, Names.WEEK_TIME),
    MONTH(Names.MONTH_LABEL, Names.MONTH_TIME),
    SEASON(Names.SEASON_LABEL, Names.SEASON_TIME),
    YEAR(Names.YEAR_LABEL, Names.YEAR_TIME);
    public static class Names{
        public static final String DAY_LABEL = "Ngày";
        public static final long DAY_TIME = 86400000L;
        public static final String WEEK_LABEL = "Tuần";
        public static final long WEEK_TIME = 604800000L;
        public static final String MONTH_LABEL = "Tháng";
        public static final long MONTH_TIME = 2592000000L;
        public static final String SEASON_LABEL = "Mùa";
        public static final long SEASON_TIME = 7776000000L;
        public static final String YEAR_LABEL = "Năm";
        public static final long YEAR_TIME = 31536000000L;
    }

    private final String label;
    private final long time;

    private EDateType(String label, long time) {
        this.label = label;
        this.time = time;
    }

    public long getTime() {
        return this.time;
    }

    public String toString() {
        return this.label;
    }
}
