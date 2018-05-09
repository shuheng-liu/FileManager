package CLI;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileInfo {
    private String name;
    private String permission;
    private String date;
    private boolean isDir;

    public FileInfo(String name, String permission, String date, boolean isDir) {
        this.name = name;
        this.permission = permission;
        this.date = date;
        this.isDir = isDir;
    }

    public FileInfo() {
        this.name = "";
        this.permission = "";
        this.date = "";
        this.isDir = false;
    }

    public FileInfo(File f) {
        try {
            if (!f.exists()) {
                throw new FileNotFoundException(f.getName());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.isDir = f.isDirectory();
        this.name = f.getName();
        long lastModified = f.lastModified();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = sdf.format(new Date(lastModified));

        StringBuilder permission = new StringBuilder();
//        StringBuffer permission = new StringBuffer();
        permission.append(f.canRead() ? "r" : "-").append(f.canWrite() ? "w" : "-").append(f.canExecute() ? "x" : "-");
        this.permission = permission.toString();
    }

    public FileInfo(String path) {
        this(new File(path));
    }

    public static FileInfo[] paths2infos(String[] paths) {
        FileInfo[] infos = new FileInfo[paths.length];
        for (int i = 0; i < infos.length; i++) {
            infos[i] = new FileInfo(paths[i]);
        }
        return infos;
    }

    public static void main(String[] args) {
        // TODO class test
        System.out.println(new FileInfo("name", "perm", "Date", true));
        File f = new File("/Users/liushuheng/.vimrc");
        FileInfo info = new FileInfo(f);
        System.out.println(info);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    @Override
    public String toString() {
        return (isDir ? "Dir" : "File") + "\t" + permission + "\t" + date + "\t" + name;
    }
}
