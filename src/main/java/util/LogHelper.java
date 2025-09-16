package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LogHelper {
    private static final List<LogData> myLog = new LinkedList<>();

    public static void debug(String data) {
        String logData = getTime("yyyy-MM-dd HH:mm") + " DEBUG: " + data;
        myLog.add(new LogData("DEBUG", logData));
    }

    public static void info(String data) {
        String logData = getTime("yyyy-MM-dd HH:mm") + " INFO: " + data;
        myLog.add(new LogData("INFO", logData));
    }

    public static void warning(String data) {
        String logData = getTime("yyyy-MM-dd HH:mm") + " WARNING: " + data;
        myLog.add(new LogData("WARNING", logData));
    }

    public static void error(String data) {
        String logData = getTime("yyyy-MM-dd HH:mm") + " ERROR: " + data;
        myLog.add(new LogData("ERROR", logData));
    }

    public static void critical(String data) {
        String logData = getTime("yyyy-MM-dd HH:mm") + " CRITICAL: " + data;
        myLog.add(new LogData("CRITICAL", logData));
    }

    public static void summary(String data) {
        String logData = getTime("yyyy-MM-dd HH:mm") + " SUMMARY: " + data;
        myLog.add(new LogData("SUMMARY", logData));
    }

    public static void writeAllLog() {
        String file_name = "./log/" + getTime("yyyy_MM_dd_HH_mm") + " Log.txt";
        makeLogDir("./log");
        try {
            FileWriter logFileWriter = new FileWriter(file_name);
            logFileWriter.write("------------------------ Log ------------------------" + '\n');
            for(LogData d: myLog) logFileWriter.write(d.logData+'\n');
            logFileWriter.close();
        }catch (IOException e){
            System.out.println("Write log file error!!!");
            e.printStackTrace();
        }
    }

    private static boolean makeLogDir(String path) {
        File folder = new File(path);
        return folder.mkdir();
    }

    private static String getTime(String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    private static class LogData {
        String logType;
        String logData;

        LogData(String logType, String logData) {
            this.logType = logType;
            this.logData = logData;
        }
    }
}
