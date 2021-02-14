import FileManager.Commands;
import FileManager.FileManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ClientControl {
    public void start(){
        System.out.println("Добро пожаловать! Введите команду:");
        Scanner scanner = new Scanner(System.in);

        FileManager fileManager = new FileManager(".\\SourceTest");

        String input = scanner.nextLine();

        while (!input.equals(Commands.EXIT)) {
            String tokens[] = input.split("\\s");
            String command = tokens[0];
            switch (command) {
                case Commands.LIST_OF_FILES:
                    fileManager.listOfFiles(false);
                    break;
                case Commands.LIST_OF_WITH_SIZE:
                    fileManager.listOfFiles(true);
                    break;
                case Commands.COPY_FILE:
                    String sourceFileName = tokens[1];
                    if(2 == tokens.length-1){
                        String destFileName = tokens[2];
                        fileManager.copyFile(sourceFileName, destFileName);
                    } else {
                        fileManager.copyFile(sourceFileName);
                    }
                    break;
                /*case Commands.COPY_FILE:
                    String sourceFileName = tokens[1];
                    String destFileName = tokens[2];
                    fileManager.copyFile(sourceFileName, destFileName);
                    break;*/
                case Commands.CREATE_FILE: {
                    String fileName = tokens[1];
                    fileManager.createFile(fileName);
                    break;
                }
                case Commands.FILE_CONTENT:
                    String fileName = tokens[1];
                    fileManager.fileContent(fileName);
                    break;
                case Commands.CHANGE_DIRECTORY:
                    String folderName = tokens[1];
                    fileManager.changeDirectory(folderName);
                    break;

                case Commands.MESSAGE:
                    ProtoFileSender.sendMessage(input,Network.getInstance().getCurrentChannel());
                    break;

                case Commands.SERVER:
                    ProtoFileSender.sendCommand(input,Network.getInstance().getCurrentChannel());
                    break;
                case Commands.SERVER_DOWNLOAD:
                    ProtoFileSender.sendReQuestFile(tokens[1],Network.getInstance().getCurrentChannel());
                    break;
                case Commands.SERVER_REQUEST_FILE:
                    Path path = Paths.get(tokens[1]);
                    try {
                        ProtoFileSender.sendFile(path,Network.getInstance().getCurrentChannel());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            input = scanner.nextLine();
        }
    }
}
