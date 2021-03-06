import java.util.List;

public class Utils {

    public static byte[] toPrimitive(Byte[] toArray) {
        byte[] bytes = new byte[toArray.length];
        for(int i = 0; i < toArray.length; i++){
            bytes[i] = toArray[i];
        }
        return bytes;
    }

    static String toString(List str){
        return str.toString()
                .substring(1, 3 * str.size() - 1)
                .replaceAll(", ", "");
    }
}
