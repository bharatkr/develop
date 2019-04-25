
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class Client {

    public static String SERVER_ADDRESS = "127.0.0.1";
    public static int SERVER_PORT_NUMBER = 8080;

    private static Socket connectToServer() {
        try {
            return new Socket(SERVER_ADDRESS, SERVER_PORT_NUMBER);
        } catch (Exception e) {
            System.out.println("Failed to connect to server " + SERVER_ADDRESS + " port " + SERVER_PORT_NUMBER + ".");
            System.exit(0);
        }

        return null;
    }

    public static void uploadFile(String clientPath, String serverPath) {
        File uploadFile = new File(clientPath);
    	int errorCode=-1;

        if (!uploadFile.exists()) {
            System.out.println(clientPath + " does not exist.");
            return;
        }

        try {
            Socket socket = connectToServer();

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("upload");
            dos.writeUTF(serverPath);
            dos.writeLong(uploadFile.length());
            FileInputStream fis = new FileInputStream(uploadFile);
            System.out.println("Size of total data uploaded: " + uploadFile.length()/1024 + "KB");
            long bytesUploaded = dis.readLong();
            if (bytesUploaded == 0) {
                System.out.println("Uploading a new file...");
            } else {
                System.out.println("Upload resumed at" + (bytesUploaded/uploadFile.length())*100 + "%");
            }
            for (int i = 0; i < bytesUploaded; i++) {
                fis.read();
            }

            byte[] data = new byte[1024];
            int read;
            while ((read = fis.read(data)) >= 0) {
                dos.write(data, 0, read);
            }

            dos.flush();
            fis.close();
            System.out.println("File uploaded successfully..");
        } catch (Exception e) {
            System.out.println("Upload failed and returned" +" " +errorCode);
        }
    }

    // Download file from server
    public static void downloadFile(String serverPath, String clientPath) {
    	int errorCode=-1;
        try {
            // Connect to server
            Socket socket = connectToServer();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            // Send to server the file to be downloaded
            dos.writeUTF("download");
            dos.writeUTF(serverPath);
            System.out.println("Downloading " + serverPath );
            // Stop if file doesn't exist
            if (!dis.readBoolean()) {
                System.out.println("File or directory does not exist" +" errorcode"+" "+errorCode);
                return;
            }
            if (new File(clientPath).exists()) {
                new File(clientPath).delete();
            }
            long totalBytesToDownload = dis.readLong();
            long bytesDownloaded = 0;
            File tempDownloadFile = new File(clientPath + ".tmp");
            if (tempDownloadFile.exists()) {
                bytesDownloaded = tempDownloadFile.length();
                System.out.println("Resuming download from " + (bytesDownloaded/totalBytesToDownload)*100 + "%");
            } else {
                tempDownloadFile.createNewFile();
                System.out.println("Starting a new download..");
            }
            dos.writeLong(bytesDownloaded);
            FileOutputStream fos = new FileOutputStream(tempDownloadFile, true);
            byte[] data = new byte[1024];
            int read = 0;
            try {
                while ((read = dis.read(data, 0, data.length)) >= 0) {
                    fos.write(data, 0, read);
                }

                fos.flush();
                fos.close();
                tempDownloadFile.renameTo(new File(clientPath));
                tempDownloadFile.delete();
                System.out.println("File downloaded succesfully");
            } catch (Exception e) {
                fos.flush();
                fos.close();
            }
            dis.close();
            dos.close();
        } catch (Exception e) {
            System.out.println("Download failed either the client or server path was wrong"+" "+errorCode);
        }
    }

    public static void listDirectory(String serverPath) {
    	int errorCode=-1;
        try {
            Socket socket = connectToServer();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            System.out.println(dis.readUTF().trim()+"\nListing selected directory");
            dos.writeUTF("listing directory");
            dos.writeUTF(serverPath);
            if (!dis.readBoolean()) {
                System.out.println(serverPath + " does not exist"+" "+errorCode);
                return;
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // New directory to server
    public static void createDirectory(String serverPath) {
    	int errorCode=-1;
        try {
            // Connect to server
            Socket socket = connectToServer();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("Creating directory: " + serverPath );
          
            dos.writeUTF("createdir");
            dos.writeUTF(serverPath);
            if (!dis.readBoolean()) {
                System.out.println(serverPath + " does not exist"+" "+errorCode);
                return;
            }
            System.out.println("Directory created successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void removeDirectory(String serverPath) {
    	int errorCode=-1;
        try {
            // Connect to server
        
            Socket socket = connectToServer();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("Deleting directory: " + serverPath );
            // Send to server the file to be downloaded
            dos.writeUTF("removedir");
            dos.writeUTF(serverPath);
            if (!dis.readBoolean()) {
                System.out.println(serverPath + " does not exist"+"returned "+ errorCode);
                return;
            }
            System.out.println("Directory deleted");
        } catch (Exception e) {
            System.out.println("The directory did not delete"+" "+ errorCode);
        }
    }
    public static void removeFile(String serverPath) {
    	int errorCode=-1;
        try {
            Socket socket = connectToServer();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("Deleting file " + serverPath + "...");
            dos.writeUTF("remove");
            dos.writeUTF(serverPath);
            if (!dis.readBoolean()) {
                System.out.println(serverPath + " does not exist");
                return;
            }
            System.out.println("File removed successfully");
        } catch (Exception e) {
        	  System.out.println("The file did not delete"+" "+ errorCode);
        }
    }
    public static void shutdown() {
        try {
            Socket socket = connectToServer();
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("Shutting down server..");
            dos.writeUTF("shutdown");
            System.out.println("Server shutdown successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) {
        String envVariable = System.getenv("PA1_SERVER");
        if (envVariable == null) {
            System.out.println("Error: PA1_SERVER environment variable not set");
            return;
        }
        String[] envVariableFields = envVariable.split(":");
        try {
            Client.SERVER_ADDRESS = envVariableFields[0];
            Client.SERVER_PORT_NUMBER = Integer.parseInt(envVariableFields[1]);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        if (args.length < 1) {
            System.out.println("Error: Client command was not found");
            return;
        }
        String command = args[0];
        if (command.equalsIgnoreCase("upload")) {
            if (args.length < 3) {
                System.out.println("Error: Either client or server path is missing");
                return;
            }
            String clientPath = args[1];
            String serverPath = args[2];
            Client.uploadFile(clientPath, serverPath);
        } else if (command.equalsIgnoreCase("download")) {
            if (args.length < 3) {
                System.out.println("Error: Either client or server path is missing");
                return;
            }
            String serverPath = args[1];
            String clientPath = args[2];
            Client.downloadFile(serverPath, clientPath);
        } else if (command.equalsIgnoreCase("ls")) {
            if (args.length < 2) {
                System.out.println("Error: Either client or server path is missing");
                return;
            }
            String serverPath = args[1];
            Client.listDirectory(serverPath);
        } else if (command.equalsIgnoreCase("mkdir")) {
            if (args.length < 2) {
                System.out.println("Error: Client or Server path was not found");
                return;
            }
            String serverPath = args[1];
            Client.createDirectory(serverPath);
        } else if (command.equalsIgnoreCase("rmdir")) {
            if (args.length < 2) {
                System.out.println("Error: Client or Server path was not found");
                return;
            }
            String serverPath = args[1];
            Client.removeDirectory(serverPath);
        } else if (command.equalsIgnoreCase("rmfile")) {
            if (args.length < 2) {
                System.out.println("Error: Server path was not found");
                return;
            }
            String serverPath = args[1];
            Client.removeFile(serverPath);
        } else if (command.equalsIgnoreCase("shutdown")) {
            Client.shutdown();
        }
    }
}
