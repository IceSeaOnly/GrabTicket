package site.binghai.game.utils;

import java.io.*;
import java.util.Scanner;

/**
 * Created by binghai on 2017/11/27.
 *
 * @ GrabTicket
 */
public class FileReader {
    public static void main(String[] args) {
        String floor = null;
        File file = new File("map.txt");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            Scanner sc = new Scanner(inputStream);
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.equals("一楼") || line.equals("二楼")) {
                    floor = line;
                    continue;
                }
                String pai = line.substring(0, line.indexOf("["));
                String zws = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                String[] fl = zws.split("、");
                for (String p : fl) {
                    if (p.contains("~")) {
                        String[] between = p.split("~");
                        int begin = Integer.parseInt(between[0]);
                        int end = Integer.parseInt(between[1]);
                        for (int i = begin; i <= end; i++) {
                            System.out.println(floor + pai + i + "号");
                        }
                    } else {
                        System.out.println(floor + pai + p + "号");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
