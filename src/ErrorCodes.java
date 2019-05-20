public class ErrorCodes {

    enum ErrorCode {
        NOTDEFINED(0), NOTFOUND(1), ACCESSVIOLATION(2), DISKFULL(3), ILLEGALOP(4), UNKNOWNTRANSFERID(5), FILEALREADYEXISTS(6), NOSUCHUSER(7);

        private int code;

        ErrorCode(int code) {
            this.code = code;
        }
    }

    public static ErrorCode getErrorCode(int opCode) {
        switch (opCode) {
            case 0:
                return ErrorCode.NOTDEFINED;
            case 1:
                return ErrorCode.NOTFOUND;
            case 2:
                return ErrorCode.ACCESSVIOLATION;
            case 3:
                return ErrorCode.DISKFULL;
            case 4:
                return ErrorCode.ILLEGALOP;
            case 5:
                return ErrorCode.UNKNOWNTRANSFERID;
            case 6:
                return ErrorCode.FILEALREADYEXISTS;
            case 7:
                return ErrorCode.NOSUCHUSER;
            default:
                System.err.println("Incorrect Error code.");
                return null;
        }
    }
}
