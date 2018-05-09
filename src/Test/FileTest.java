package Test;

import java.io.File;

public class FileTest {
    public static void main(String[] args) {
        try {
            File file1 = new File("/Users/liushuheng/Desktop/./../Desktop/");
            System.out.println(file1.isDirectory());
            System.out.println(file1.getAbsolutePath());
            System.out.println(file1.getCanonicalPath());
            System.out.println(file1.getPath());

            File file2 = new File("../..");
            System.out.println(file2.isDirectory());
            System.out.println(file2.getAbsolutePath());
            System.out.println(file2.getCanonicalPath());
            System.out.println(file2.getPath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
