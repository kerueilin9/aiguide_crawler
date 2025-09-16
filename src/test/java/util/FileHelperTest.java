package util;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileHelperTest {

    @Test
    public void testReadFile(){
        String filePath = "./src/test/java/util/readTest.txt";
        String fileText = FileHelper.getFileText(filePath);
        assertEquals("test read file\ntest write file %1", fileText);
    }

    @Test
    public void testCreateFile(){
        String filePath = "./src/test/java/util/writeTest.txt";
        String writeText = "test write file\ntest write file %1";
        FileHelper.createFile(filePath, writeText);
        String readFileText = FileHelper.getFileText(filePath);
        assertEquals(writeText, readFileText);
    }

    @Test
    public void testCreateFolder(){
        String folderPath = "./src/test/java/util/testFolder";
        FileHelper.createFolder(folderPath);
        Path folder = Paths.get(folderPath);
        assertTrue(Files.isDirectory(folder));
    }

    @Test
    public void testDeletedFile(){
        String filePath = "./src/test/java/util/writeTest.txt";
        String writeText = "test write file\ntest write file %1";
        Boolean isDelete = false;
        FileHelper.createFile(filePath, writeText);
        isDelete = FileHelper.deleteFile(filePath);
        assertTrue(isDelete);
    }

    @Test
    public void testCopyFile(){
        String sourcePath = "./src/test/java/util/writeTest.txt";
        String targetPath = "./src/test/java/util/copyTest.txt";
        String writeText = "test write file\ntest write file %1";
        FileHelper.createFile(sourcePath, writeText);
        FileHelper.copyFile(sourcePath,targetPath);
        String readFileText = FileHelper.getFileText(targetPath);
        assertEquals(writeText, readFileText);
    }

    @Test
    public void testCopySingleFolder(){
        String folderPath = "./src/test/java/util/testFolder";
        String targetPath = "./src/test/java/util/testCopyFolder";

        FileHelper.copyFolder(folderPath, targetPath);

        Path copyFolder = Paths.get(targetPath);

        assertTrue(Files.isDirectory(copyFolder));
    }

}
