package CLI;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressWarnings("Duplicates")
public class Unzipper {
    private static final int BUFFER_SIZE = 4096;

    private static void extractFile(ZipInputStream in, File outdir, String name) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outdir, name)));
        int count = -1;
        while ((count = in.read(buffer)) != -1)
            out.write(buffer, 0, count);
        out.close();
    }

    private static void mkdirs(File outdir, String path) {
        File d = new File(outdir, path);
        if (!d.exists())
            d.mkdirs();
    }

    private static String dirpart(String name) {
        int s = name.lastIndexOf(File.separatorChar);
        return s == -1 ? null : name.substring(0, s);
    }

    static void unzip(File zipFile, File outDir) {
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry entry;
            String name, dir;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                if (entry.isDirectory()) {
                    mkdirs(outDir, name);
                    continue;
                }
                dir = dirpart(name);
                if (dir != null)
                    mkdirs(outDir, dir);

                extractFile(zin, outDir, name);
            }
            zin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void unzip(String zipFile, String outDir) {
        unzip(new File(zipFile), new File(outDir));

    }
}

