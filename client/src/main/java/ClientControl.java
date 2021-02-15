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
                    String sourceFileName = buildierM(tokens);
                    String destFileName = buildierTwoNameFile(tokens);
                    if(destFileName.equals("")){
                        fileManager.copyFile(sourceFileName);
                    } else {
                        fileManager.copyFile(sourceFileName, destFileName);
                    }
                    break;
                case Commands.CREATE_FILE: {
                    String folderName = buildierM(tokens);
                    fileManager.createFile(folderName);
                    break;
                }
                case Commands.FILE_CONTENT:{
                    String folderName = buildierM(tokens);
                    fileManager.fileContent(folderName);
                    break;
                }
                case Commands.CHANGE_DIRECTORY:
                    String folderName = buildierM(tokens);
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

    public String buildierM(String[] credentialValues){
        String stopNameFileOneToNameFileTwo = "->";
        int corrector = 1;
        int index = 0;
        int lengthStr = 0;
        for (int j = 0; j < credentialValues.length-(1+corrector); j++) {
            if(credentialValues[j+(1+corrector)].equals(stopNameFileOneToNameFileTwo)){
                index = j;
                break;
            }
        }
        if(index ==0){
            lengthStr = credentialValues.length-(1+corrector);
        } else {
            lengthStr = (credentialValues.length - (credentialValues.length - index));
        }
        String[] add = new String[lengthStr];
        for (int i = 0; i < add.length; i++) {
            add[i]=credentialValues[i+(1+corrector)];
        }
        return stringConstruction(add);
    }

    protected String stringConstruction(String[] strings){
        String str = null;
        for (int i = 0; i < strings.length; i++) {
            if (i==0){
                str=strings[i];
            }else {
                str = str +" "+strings[i];
            }
        }
        return str;
    }

    public String buildierTwoNameFile(String[] credentialValues){
        String stopNameFileOneToNameFileTwo = "->";
        int corrector = 1;
        int index = 0;
        boolean serach = false;
        for (int i = 0; i < credentialValues.length; i++) {
            if(credentialValues[i].equals(stopNameFileOneToNameFileTwo)){
                System.out.println(credentialValues[i]);
                index = i;
                serach = true;
                break;
            }
        }
        if(serach == false){
            return "";
        }
        String add[] = new String[credentialValues.length-index-corrector];

        for (int i = 0; i < add.length; i++) {
            add[i] = credentialValues[i+index+corrector];
        }

        return stringConstruction(add);
    }

}
