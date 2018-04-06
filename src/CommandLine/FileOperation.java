package CommandLine;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;

public class FileOperation {

    private String currentDir;

    private String interpretPath(String path){
        if(path.startsWith("/")){
            return path;
        } else if (currentDir.endsWith("/")){
            return currentDir + path;
        } else {
            return currentDir + "/" + path;
        }
    }

    public FileOperation(String currentDir) {
        this.currentDir = currentDir;
    }

    public String getCurrentDir() {
        return currentDir;
    }

    private void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
    }

    public File[] ls(String dirName){
        if(dirName == null){ // fall back to default
            dirName = currentDir;
        }
        File dir = new File(interpretPath(dirName));
//        return dir.listFiles( (File file) -> !file.getName().startsWith(".") ); // using lambda expression
        return dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return ! file.getName().startsWith(".");
            }
        });
    }

    public void cp(){
    }

    public String cat(String fileName){
        try{
            File file = new File(interpretPath(fileName));
            Reader reader = new FileReader(file);
            StringBuffer buffer = new StringBuffer();
            int c = reader.read();
            while(c != -1){
                buffer.append((char) c);
                c = reader.read();
            }
            return buffer.toString();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean mkdir(String dirName){
        File dir = new File(interpretPath(dirName));
        boolean success = false;
        try {
            if(!dir.mkdir()){
                throw new DirectoryNotEmptyException(dir.getAbsolutePath());
            } else {
                success = true;
            }
        } catch (DirectoryNotEmptyException e) {
            e.printStackTrace();
        }
        return success;
    }

    public void rm(){

    }

    public String pwd(){
        String dir;
        File f = new File(currentDir);
        try {
            dir = f.getCanonicalPath();
        } catch (IOException e){
            e.printStackTrace();
            dir = f.getAbsolutePath();
        }
        return dir;
    }

    public void cd(String dir){
        File f = new File(interpretPath(dir));
        try{
            if(f.isDirectory()) {
                setCurrentDir(f.getCanonicalPath());
            } else {
                throw new NotDirectoryException(f.getAbsolutePath());
            }
        } catch (IOException e){
            System.out.println("current Directory remained " + currentDir);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
	// TODO test each method above
        FileOperation op = new FileOperation(".");
        System.out.println(op.pwd());
        File[] files = op.ls("src/CommandLine");
        FileInfo[] infos = new FileInfo[files.length];
        for(int i = 0; i < infos.length; i ++){
            infos[i] = new FileInfo(files[i]);
        }
        for(FileInfo info:infos){
            System.out.println(info);
        }
        op.cd("/Users/liushuheng/");
        System.out.println(op.pwd());
        System.out.print(op.cat(".inputrc"));
        op.mkdir("/Users/liushuheng/Desktop/foo");
        System.out.println(op.pwd());

    }
}
