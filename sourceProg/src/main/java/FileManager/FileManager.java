package FileManager;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {

    private String currentFolder;
    private String root;

    public FileManager(String currentFolder){
        this.currentFolder = currentFolder;
        this.root = currentFolder;
    }

    public String getCurrentFolder() {
        return currentFolder;
    }

    public void listOfFiles(boolean withSize){
        File currentFolderAsFile = new File(currentFolder);

        File files[] = currentFolderAsFile.listFiles();

        for (File file : files){
            if(file.isDirectory()){
                if(withSize){
                    System.out.print(file.getName() + "\\ " + FileUtils.sizeOfDirectory(file));
                } else {
                    System.out.print(file.getName() + "\\ ");
                }
                System.out.println();
            } else {
                if(withSize){
                    System.out.print(file.getName() + " " + file.length());
                } else {
                    System.out.print(file.getName() + " ");
                }
                System.out.println();
            }
        }
    }

    public void copyFile(String sourceFileName, String destFileName) {
        File source = new File(currentFolder + "\\" + sourceFileName);
        File dest = new File(currentFolder + "\\" + destFileName);
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            System.err.println("Произошла ошибка!");
        }
    }

    public void copyFile(String sourceFileName) {
        File source = new File(currentFolder + "\\" + sourceFileName);
        String [] arr = sourceFileName.split("\\.");
        File dest = new File(currentFolder + "\\"+arr[arr.length-2] /*sourceFileName*/ + " - copy"+"."+arr[arr.length-1]);
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            System.err.println("Произошла ошибка!");
        }
    }

    public void createFile(String fileName) {
        File file = new File(currentFolder + "\\" + fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Произошла ошибка!");
        }
    }

    public void fileContent(String fileName) {
        File file = new File(currentFolder + "\\" + fileName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException e){
            System.err.println("Произошла ошибка!");
        }
    }

    public void changeDirectory(String folderName) {
        if(folderName.equals("/")){
            this.currentFolder = this.root;
        } else if (folderName.equals("..")){
            int startLastFolderPosition = this.currentFolder.lastIndexOf("\\");
            this.currentFolder = this.currentFolder.substring(0, startLastFolderPosition);
        } else {
            this.currentFolder = this.currentFolder + "\\" + folderName;
        }
    }
}