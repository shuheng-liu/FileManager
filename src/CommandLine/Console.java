package CommandLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Console {
    public static void main(String[] args) {
        System.out.println("Welcome to use the File Management Program");
        FileOperation op = new FileOperation(".");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("prompt> ");
            String command = scanner.nextLine();
            if (command.trim().isEmpty()) {
                continue;
            }
            String str[] = splitCommand(command);
            switch (str[0]) {
                case "ls":
                    ls(op, str);
                    break;
                case "cp":
                    cp(op, str);
                    break;
                case "rm":
                    rm(op, str);
                    break;
                case "cd":
                    cd(op, str);
                    break;
                case "cat":
                    cat(op, str);
                    break;
                case "mkdir":
                    mkdir(op, str);
                    break;
                case "pwd":
                    pwd(op);
                    break;
                case "exit":
                    System.out.println("File Opeartion exited");
                    return;
                default:
                    System.out.println("File Operation: " + str[0] + " - command not found");
            }
        }
//        // TODO test splitCommand() method
//        String str = "00000 11111\\ 22222\\ 33333\\ 44444 00000 00000 55555\\ 66666 00000 00000";
//        System.out.println(str);
//        for (String segment : splitCommand(str)) {
//            System.out.println(segment);
//        }
//
//        // TODO test joinPath() method
//        String[] strs = new String[]{"00000", "11111\\", "22222\\ "};
//        System.out.println(joinPath(strs, 0, 3));
    }

    private static String joinPath(String[] str) {
        return joinPath(str, 1, str.length);
    }

    private static String joinPath(String[] str, int start, int end) {
        // consider the case where there are spaces in string
        String[] strSlice = Arrays.copyOfRange(str, start, end);
        String strWithSlashes = String.join(" ", strSlice);
        return strWithSlashes.replace("\\ ", " ");
    }

    private static String[] splitCommand(String command) {
        String[] roughSegments = command.split(" ");
        List<String> segments = new ArrayList<String>();
        int head = 0;
        int tail = 0;
        while (head < roughSegments.length && tail < roughSegments.length) {
            // TODO set tail, since head is already set before each iteration
            while (roughSegments[tail].endsWith("\\") && tail + 1 < roughSegments.length) {
                tail++;
            }

            // TODO append new element in segments
            segments.add(joinPath(roughSegments, head, tail + 1));

            // TODO set new head and tail for the next iteration
            head = ++tail;
        }
        return segments.toArray(new String[segments.size()]);
    }

    private static void ls(FileOperation op, String[] str) {
        File[] files = null;
        if (str.length == 1) {
            files = op.ls();
        } else {
            // TODO further consider the scenario where spaces exist in path
            files = op.ls(joinPath(str));
        }
        for (File file : files) {
            System.out.println(new FileInfo(file));
        }
    }

    private static void cp(FileOperation op, String[] str) {
        try {
            if (str.length != 3) {
                throw new ArgumentNumberException(2, str.length - 1);
            }
            op.cp(str[1], str[2]);
        } catch (ArgumentNumberException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void rm(FileOperation op, String[] str) {
        for (int i = 1; i < str.length; i++) {
            if (!op.rm(str[i])) {
                System.out.println("Failed to remove " + str[i]);
            }
        }
    }

    private static void cd(FileOperation op, String[] str) {
        try {
            if (str.length != 2) {
                throw new ArgumentNumberException(1, str.length - 1);
            }
        } catch (ArgumentNumberException e) {
            e.printStackTrace();
        }
        op.cd(str[1]);
    }

    private static void cat(FileOperation op, String[] str) {
        for (int i = 1; i < str.length; i++) {
            System.out.println(op.cat(str[i]));
        }
    }

    private static void mkdir(FileOperation op, String[] str) {
        for (int i = 1; i < str.length; i++) {
            op.mkdir(str[i]);
        }
    }

    private static void pwd(FileOperation op) {
        System.out.println(op.pwd());
    }
}
