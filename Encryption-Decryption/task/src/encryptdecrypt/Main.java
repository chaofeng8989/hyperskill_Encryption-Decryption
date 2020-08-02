package encryptdecrypt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Map<String, String> cmds = new HashMap<>();
        cmds.put("-mode", "enc");
        cmds.put("-key", "0");
        cmds.put("-data", "");
        cmds.put("-in", "");
        cmds.put("-out", "");
        cmds.put("-alg", "shift");
        if (args.length % 2 == 1) {
            System.out.println("error");
            return;
        }
        int i = 0;
        while (i+1 < args.length) {
            cmds.put(args[i], args[i+1]);
            i+=2;
        }


        int shift = Integer.parseInt(cmds.get("-key"));
        String data = cmds.get("-data");
        String mode = cmds.get("-mode");
        String in = cmds.get("-in");
        String out = cmds.get("-out");
        String alg = cmds.get("-alg");
        if (data.isEmpty() && !in.isEmpty()) {
            try {
                data = Files.readString(Path.of(in));
            } catch (IOException e) {
                System.out.println("error");
            }
        }

        System.out.println(data);
        String result = "";
        if (mode.equals("dec")) shift = - shift;
        if (alg.equals("shift")) {
            result = shift(data, shift);
        } else {
            result = unicode(data, shift);
        }

        if (out.isEmpty()) {
            System.out.println(result);
        } else {
            try {
                Files.writeString(Path.of(out), result);
            } catch (IOException e) {
                System.out.println("error");
            }
        }
        System.out.println(result);


    }

    private static String shift(String text, int n) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                sb.append((char)('a' + (c - 'a' + n + 26) %26));
            } else if (c >= 'A' && c <= 'Z') {
                sb.append((char)('A' + (c - 'A' + n + 26) % 26));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }



    private static String unicode(String text, int n) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append((char) ((c + 0xffff + n) % 0xffff));
        }
        return sb.toString();
    }
}
