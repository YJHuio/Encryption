import java.io.FileInputStream;
import java.io.FileOutputStream;

class ClassFileEncoder {
    public static void main(String[] args) {
        String inFile = "./a.enc";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inFile);
            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data);
            fis.close();
            data = DESUtil.deCode("1234567890qwertyuiopasdf".getBytes(), data);
            String outFile = "./a.txt";
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}