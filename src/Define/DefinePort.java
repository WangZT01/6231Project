package Define;

public class DefinePort {

    public static final int FE_ACKOWLEDGE_PORT=4000;
    public static final int FE_OPEARION_PORT=5000;
    public static final int FE_PRIMARY=4888;
    public static final int FailureDetector=4999;
    public static final int FE_INITIAL_PORT=4777;

    public static final int DDO_CLIENT_PORT=6051;
    public static final int DDO_ACKOWLEDGE_PORT=4001;

    public static final int DDO_OPEARION_PORT=5051;
    public static final int DDO_OPEARION_PORT2=5061;
    public static final int DDO_OPEARION_PORT3=5071;

    public static final int DDO_ELECTION_PORT=6001;

    public static final int LVL_CLIENT_PORT=6052;

    public static final int MTL_CLIENT_PORT=6053;

    private static final String separator = ";";

    public static String getSeparator() {
        return separator;
    }
}
