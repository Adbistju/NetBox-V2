import FileManager.Commands;
import FileManager.FileManager;

public class ServerControl {

    int corrector = 1;

    FileManager fileManager = new FileManager(".\\SourceTest");

    public String getCurrentDir() {
        return fileManager.getCurrentFolder();
    }

    public void command(String commandClient){
        String input = commandClient;
        String tokens[] = input.split("\\s");

       // tokens = buildierM(tokens);
       /* if(){

        }*/

        String command = tokens[0+corrector];

        switch (command) {
            case Commands.LIST_OF_FILES:
                fileManager.listOfFiles(false);
                break;
            case Commands.LIST_OF_WITH_SIZE:
                fileManager.listOfFiles(true);
                break;
            case Commands.COPY_FILE:
                String sourceFileName = tokens[1+corrector];
                if(2+corrector == tokens.length-1){
                    String destFileName = tokens[2+corrector];
                    fileManager.copyFile(sourceFileName, destFileName);
                } else {
                    fileManager.copyFile(sourceFileName);
                }
                break;
            case Commands.CREATE_FILE: {
                String fileName = tokens[1+corrector];
                fileManager.createFile(fileName);
                break;
            }
            case Commands.FILE_CONTENT:
                String fileName = tokens[1+corrector];
                fileManager.fileContent(fileName);
                break;
            case Commands.CHANGE_DIRECTORY:
                String folderName = tokens[1+corrector];
                fileManager.changeDirectory(folderName);
                break;
        }
    }

    public String[] buildierM(String[] credentialValues){
        String[] add = new String[credentialValues.length-1];
        for (int j = 0; j < credentialValues.length-1; j++) {
            add[j]=credentialValues[j+1];
        }
        return add;
    }
}
