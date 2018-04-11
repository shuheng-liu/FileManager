package CommandLine;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NotDirectoryException;

public class FileOperation {

    private String currentDir;

    public FileOperation(String currentDir) {
        this.currentDir = currentDir;
    }

    public static void main(String[] args) {
        // TODO test each method
//        try {
//            copy(System.in, new FileOutputStream("/Users/liushuheng/desktop/a.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        FileOperation op = new FileOperation(".");
        System.out.println(op.pwd());
        File[] files = op.ls("src/CommandLine");
        FileInfo[] infos = new FileInfo[files.length];
        for (int i = 0; i < infos.length; i++) {
            infos[i] = new FileInfo(files[i]);
        }
        for (FileInfo info : infos) {
            System.out.println(info);
        }
//        op.cd("/Users/liushuheng/");
//        System.out.println(op.pwd());c
//        System.out.print(op.cat(".inputrc"));
//        op.mkdir("/Users/liushuheng/Desktop/foo");
        if (!op.mkdir("foo")) {
            System.out.println("failure to mkdir");
        }
        if (!op.mkdir("foo/bar")) {
            System.out.println("failure to mkdir");
        }
        System.out.println(op.rm("foo/bar") ? "Deleted" : "Not Deleted");
        System.out.println(op.rm("foo") ? "Deleted" : "Not Deleted");

        System.out.println(op.pwd());
    }

    private String interpretPath(String path) {
        if (path.startsWith("/")) {
            return path;
        } else if (currentDir.endsWith("/")) {
            return currentDir + path;
        } else {
            return currentDir + "/" + path;
        }
    }

    public String getCurrentDir() {
        return currentDir;
    }

    private void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
    }

    public File[] ls(String dirName) {
        if (dirName == null) { // fall back to default
            dirName = currentDir;
        }
        File dir = new File(interpretPath(dirName));
        return dir.listFiles((file) -> !file.isHidden());
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte b[] = new byte[1024 * 1024];
        int len = in.read(b);
        while (len != -1) {
            out.write(b, 0, len);
            len = in.read(b);
        }
        in.close();
        out.close();
    }

    public void cp(String src, String dest) throws FileNotFoundException {
//        Input Stream --> Output Stream (byte)
//        reader <--> writer (char)
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dest);
        try {
            copy(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String cat(String fileName) throws FileNotFoundException {
        InputStream in;
        try {
            in = new FileInputStream(new File(interpretPath(fileName)));
            copy(in, System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            File file = new File(interpretPath(fileName));
//            Reader reader = new FileReader(file);
//            StringBuffer buffer = new StringBuffer();
//            int c = reader.read();
//            while (c != -1) {
//                buffer.append((char) c);
//                c = reader.read();
//            }
//            return buffer.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return "";
    }

    public boolean mkdir(String dirName) {
        File dir = new File(interpretPath(dirName));
        boolean success = false;
        try {
            if (!dir.mkdir()) {
                throw new DirectoryNotEmptyException(dir.getAbsolutePath());
            } else {
                success = true;
            }
        } catch (DirectoryNotEmptyException e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean rm(String path) {
        return new File(interpretPath(path)).delete();
    }

    public String pwd() {
        String dir;
        File f = new File(currentDir);
        try {
            dir = f.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            dir = f.getAbsolutePath();
        }
        return dir;
    }

    public void cd(String dir) {
        File f = new File(interpretPath(dir));
        try {
            if (f.isDirectory()) {
                setCurrentDir(f.getCanonicalPath());
            } else {
                throw new NotDirectoryException(f.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("current Directory remained " + currentDir);
            e.printStackTrace();
        }
    }
}
