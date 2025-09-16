package util;

import java.io.*;
import java.nio.file.*;

public class FileHelper {
    public static String getFileText(String filePath) {
        String fileText = "";
        File file = new File(filePath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String fileLinetext;
            while((fileLinetext = br.readLine()) != null){
                fileText += fileLinetext + "\n";
            }
            fileText = fileText.substring(0,fileText.length()-1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileText;
    }

    public static void createFile(String filePath, String writeText) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(writeText);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteFile(String filePath){
        File file = new File(filePath);
        return file.delete();
    }

    public static void createFolder(String folderPath){
        try {
            Files.createDirectories(Paths.get(folderPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(String sourcePath, String targetPath){
        createFile(targetPath, getFileText(sourcePath));
    }

    public static void copyFolder(String source, String target){
        Path sourcePath = Paths.get(source);
        Path targetPath = Paths.get(target);
        createFolder(target);
        try {
            Files.walk(sourcePath).forEach(
                    sourceFilePath -> {
                        Path sourceFileRelatedPath = sourcePath.relativize(sourceFilePath);
                        Path targetFilePath = targetPath.resolve(sourceFileRelatedPath);

                        String regularSourceFilePath = sourceFilePath.toString().replace("\\","/");
                        String regularTargetFilePath = targetFilePath.toString().replace("\\","/");

                        if (!(sourceFileRelatedPath.toString().isEmpty())){
                            if (isFolder(sourceFilePath)) createFolder(regularTargetFilePath);
                            else copyFile(regularSourceFilePath, regularTargetFilePath);
                        }
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isFolder(Path path) {
        return Files.isDirectory(path);
    }
}
