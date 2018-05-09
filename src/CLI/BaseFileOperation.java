package CLI;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;

@SuppressWarnings("Duplicates")
public class BaseFileOperation {
    private final String key = new String("Mary has one cat");
    private String currentDir;

    public BaseFileOperation(String currentDir) {
        try {
            this.currentDir = new File(currentDir).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            this.currentDir = "/";
        }
    }

    public static void main(String[] args) throws IOException {
        // TODO test each method
//        try {
//            copy(System.in, new FileOutputStream("/Users/liushuheng/desktop/a.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        BaseFileOperation op = new BaseFileOperation(".");
        String desktopPath = new String("/Users/liushuheng/Desktop");
        for (int i = 0; i < 5; i++) {
            op.autoMakeDir(desktopPath);
        }


//        String original = new String("/Users/liushuheng/Desktop/tmp-folder");
//        String whereToZip = new String("/Users/liushuheng/Desktop/");
//        String zipped = new String("/Users/liushuheng/Desktop/tmp-folder zipped");
//        String whereToUnzip = new String("/Users/liushuheng/Desktop/");
//
//        op.zip(original, whereToZip);
//        op.unzip(zipped, whereToUnzip);
//        System.out.println(op.pwd());
//        File[] files = op.ls("src/CLI");
//        FileInfo[] infos = new FileInfo[files.length];
//        for (int i = 0; i < infos.length; i++) {
//            infos[i] = new FileInfo(files[i]);
//        }
//        for (FileInfo info : infos) {
//            System.out.println(info);
//        }
//        op.cd("/Users/liushuheng/");
//        System.out.println(op.pwd());c
//        System.out.print(op.cat(".inputrc"));
//        op.mkdir("/Users/liushuheng/Desktop/foo");
//        if (!op.mkdir("foo")) {
//            System.out.println("failure to mkdir");
//        }
//        if (!op.mkdir("foo/bar")) {
//            System.out.println("failure to mkdir");
//        }
//        System.out.println(op.rm("foo/bar") ? "Deleted" : "Not Deleted");
//        System.out.println(op.rm("foo") ? "Deleted" : "Not Deleted");
//
//        System.out.println(op.pwd());
//        File toBeEncrypted = new File("/Users/liushuheng/Desktop/testFile1.txt");
//        File encrypted = new File("/Users/liushuheng/Desktop/testFile2.txt");
//        File toBeDecrypted = new File("/Users/liushuheng/Desktop/testFile3.txt");
//        File decrypted = new File("/Users/liushuheng/Desktop/testFile4.txt");
//        try {
//            op.encrypt(toBeEncrypted, encrypted);
//            op.cp(encrypted, toBeDecrypted);
//            op.decrypt(toBeDecrypted, decrypted);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[1024 * 1024];
        int len = in.read(b);
        while (len != -1) {
            out.write(b, 0, len);
            len = in.read(b);
        }
        in.close();
        out.close();
    }

    // tested
    private void decrypt(File src, File dest) {
        try {
            CryptoUtils.decrypt(key, src, dest);
        } catch (CryptoException e) {
            e.printStackTrace();
        }
    }

    // tested
    private void encrypt(File src, File dest) {
        try {
            CryptoUtils.encrypt(key, src, dest);
        } catch (CryptoException e) {
            e.printStackTrace();
        }
    }

    // tested
    public void encrypt(String src, String dest) {
        File[] files = stringPair2FilePair(src, dest, "encrypted");
        encrypt(files[0], files[1]);
    }

    // tested
    public void decrypt(String src, String dest) {
        File[] files = stringPair2FilePair(src, dest, "decrypted");
        decrypt(files[0], files[1]);
    }

    private String interpretPath(String path) {
        if (path.startsWith("/")) { // if it is an absolute path
            return path;
        } else if (currentDir.endsWith("/")) { // if current dir has a trailing "/"
            return currentDir + path;
        } else { // if current dir does not have a trailing "/"
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

    public File[] ls() {
        File dir = new File(currentDir);
        return dir.listFiles((file) -> !file.isHidden());
    }

    // TODO consider if the following is also sound for decryption and encryption
    // debugging of the following method: Done
    private File[] stringPair2FilePair(String src, String dest, String suffix) {
        // make sure suffix is not empty
        if (suffix.isEmpty()) {
            suffix = "new";
        }

        // make sure src does not have a trailing slash ("/")
        if (src.endsWith("/")) {
            src = src.substring(0, src.length() - 1);
        }
        // get the child path's filename and extension, if existent
        String[] srcSlices = src.split("/");
        String srcLastComponent = srcSlices[srcSlices.length - 1];
        String srcLastFilename;
        String srcLastExtension;
        if (srcLastComponent.contains(".")) {
            int index = srcLastComponent.lastIndexOf(".");
            srcLastFilename = srcLastComponent.substring(0, index);  // does not contain the dot
            srcLastExtension = srcLastComponent.substring(index, srcLastComponent.length());  // does contain the dot
        } else {
            srcLastFilename = srcLastComponent;
            srcLastExtension = "";
        }

        System.out.println(srcLastFilename);
        System.out.println(srcLastExtension);

        // make sure dest is not its parent dir
        if (dest.endsWith("/")) {
            dest = dest + srcLastFilename;
        }
        dest = interpretPath(dest);

        // in case file/directory already exists
        while (new File(dest + srcLastExtension).exists()) {
            dest = dest + " " + suffix;
        }
        dest = dest + srcLastExtension;

        File[] files = new File[2];
        files[0] = new File(interpretPath(src));
        files[1] = new File(interpretPath(dest));
        return files;
    }

    public void cp(String src, String dest) throws IOException {
        File[] files = stringPair2FilePair(src, dest, "copy");
        cp(files[0], files[1]);
    }

    private void cp(File src, File dest) throws IOException {
        if (src.isDirectory()) { // copy recursively
            System.out.println("copying directory recursively");
            copyTree(src.toPath(), dest.toPath());
        } else { // copy a single file
            System.out.println("copying single file");
            copy(new FileInputStream(src), new FileOutputStream(dest));
        }
    }

    public String cat(String fileName) {
        try {
            File file = new File(interpretPath(fileName));
            Reader reader = new FileReader(file);
            StringBuffer buffer = new StringBuffer();
            int c = reader.read();
            while (c != -1) {
                buffer.append((char) c);
                c = reader.read();
            }
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void copyTree(Path src, Path dest) {
        try {
            Files.walk(src).forEach(s -> {
                try {
                    Path d = dest.resolve(src.relativize(s));
                    if (Files.isDirectory(s)) {
                        if (!Files.exists(d))
                            Files.createDirectory(d);
                        return;
                    }
                    Files.copy(s, d);// use flag to override existing
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean mkdir(String dirName) {
        File dir = new File(interpretPath(dirName));
        boolean success = false;
        try {
            if (dir.mkdir()) {
                success = true;
            } else {
                throw new DirectoryNotEmptyException(dir.getAbsolutePath());
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

    // tested
    private void zip(File src, File dest) {
        try {
            Zipper.zip(src, dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // tested
    private void unzip(File src, File dest) {
        Unzipper.unzip(src, dest);
    }

    // public zip method tested
    public void zip(String src, String dest) {
        File[] files = stringPair2FilePair(src, dest, "zipped");
        zip(files[0], files[1]);
    }

    // public unzip method tested
    public void unzip(String src, String dest) {
        File[] files = stringPair2FilePair(src, dest, "unzipped");
        unzip(files[0], files[1]);
    }

    private boolean autoMakeDir(File file) {
        if (file.isFile()) {
            System.out.println(file.toString() + " is a File, recursively dealing with its parent directory");
            return autoMakeDir(file.getParentFile());
        }
        // auto-generate names until not conflicting with existent folders/files
        String newFolderName = file.getAbsolutePath() + "/folder created";
        while (new File(newFolderName).exists()) {
            System.out.println(newFolderName + " exists, try another name");
            newFolderName = newFolderName + " new";
        }
        return mkdir(newFolderName);
    }

    public boolean autoMakeDir(String filename) {
        return autoMakeDir(new File(filename));
    }
}
