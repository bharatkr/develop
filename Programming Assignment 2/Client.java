
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements ClientInterface {

    // Create the client
    public Client() throws RemoteException {
        super();
    }

    // Call from server that downloads file from server to here
    @Override
    public boolean downloadData(String filename, byte[] data, int dataLength) throws RemoteException {
        // Write data to client
        try {
            FileOutputStream fos = new FileOutputStream(filename, true);
            fos.write(data, 0, dataLength);
            fos.flush();
            fos.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            // Once interrupted, will be resumed later
        }

        return false;
    }

    // Download file from server
    public static void downloadFile(ServerInterface server, Client client, String clientPath, String serverPath) {
        try {
            // Delete file if it exists
            if (new File(clientPath).exists()) {
                new File(clientPath).delete();
            }

            System.out.print("Downloading " + serverPath + "...");

            // Stop if file doesn't exist
            long totalBytesToDownload = server.getFileSize(serverPath);

            if (totalBytesToDownload == -1) {
                System.out.println("The file does not exist.");
                return;
            }

            // Send to the server the starting pointing buffer point
            long bytesDownloaded = 0;
            File tempDownloadFile = new File(clientPath + ".tmp");

            if (tempDownloadFile.exists()) {
                bytesDownloaded = tempDownloadFile.length();
                System.out.print("Resuming download from " + (bytesDownloaded/totalBytesToDownload)*100 + "%");
            } else {
                tempDownloadFile.createNewFile();
                System.out.print("Downloading " + (totalBytesToDownload/1024) + "KB of data..");
            }

            // Start download to temporary file
            if (server.downloadFileFromClient(client, serverPath, clientPath + ".tmp", bytesDownloaded)) {
                // If download is complete, we delete the temp file and produce the actual file
                tempDownloadFile.renameTo(new File(clientPath));
                tempDownloadFile.delete();

                System.out.println("Download complete successfully");
            } else {
                System.out.println("Error: Server Connection lost");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    // Upload file to server
    public static void uploadFile(ServerInterface server, String clientPath, String serverPath) {
        // Stop if file to upload doesn't exist
        File uploadFile = new File(clientPath);

        if (!uploadFile.exists()) {
            System.out.println(clientPath + " does not exist.");
            return;
        }

        try {
            // Send the file

            // Check the starting point of upload
            long bytesUploaded = server.getBytesUploaded(serverPath);

            if (bytesUploaded == 0) {
                System.out.print("Uploading..");
            } else {
                System.out.print("Resuming upload at " + (bytesUploaded/1024) + "KB");
            }

            FileInputStream fis = new FileInputStream(clientPath);

            for (int i = 0; i < bytesUploaded; i++) {
                fis.read();
            }

            byte[] data = new byte[1024];
            int read;

            while ((read = fis.read(data)) >= 0) {
                if (!server.uploadDataFromClient(serverPath, data, read)) {
                    throw new Exception("file upload failed!");
                }
            }

            server.uploadComplete(serverPath);
            System.out.println("File uploaded successfully..");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // List server directory
    public static void listDirectory(ServerInterface server, String serverPath) {
        try {
            String listing = server.listDirectory(serverPath);

            if (listing.isEmpty()) {
                System.out.println("Could not find path " + serverPath);
                return;
            }

            System.out.println(listing);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    // Create a new directory
    public static void createDirectory(ServerInterface server, String serverPath) {
        try {
            System.out.print("Creating directory " + serverPath + "...");

            if (!server.createDirectory(serverPath)) {
                System.out.println("Could not find path " + serverPath);
            } else {
                System.out.println("Directory created successfully..");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    // Remove a file in server
    public static void removeFile(ServerInterface server, String serverPath) {
        try {
            System.out.print("Deleting file " + serverPath + "...");

            if (!server.removeFile(serverPath)) {
                System.out.println("Could not find path " + serverPath);
            } else {
                System.out.println(serverPath+ "deleted successfully..");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    // Remove a directory in server
    public static void removeDirectory(ServerInterface server, String serverPath) {
        try {
            System.out.print("Deleting directory " + serverPath + "...");

            if (!server.removeDirectory(serverPath)) {
                System.out.println("Could not find path " + serverPath);
            } else {
                System.out.println("Directory removed successfully..");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    // Shutdown the server
    public static void shutdown(ServerInterface server) {
        System.out.print("Shutting down server..");

        try {
            server.shutdown();
        } catch (Exception e) {
        }

        System.out.println("Server Shutdown successful..");
    }

    public static void main(String[] args) throws Exception {
        String envVariable = System.getenv("PA1_SERVER");
        if (envVariable == null) {
            System.out.println("Error: PA1_SERVER environment variable not set");
            return;
        }
        String[] envVariableFields = envVariable.split(":");
        String serverAddress = envVariableFields[0];
        int serverPortNumber = Integer.parseInt(envVariableFields[1]);
        if (args.length < 1) {
            System.out.println("Error: Invalid argument. Specify client command");
            return;
        }

        ServerInterface server = (ServerInterface) Naming.lookup("rmi://" + serverAddress + "/fileserver");

        String command = args[0];

        if (command.equalsIgnoreCase("upload")) {
            if (args.length < 3) {
                System.out.println("Error: Invalid argument. Specify client and/or server path");
                return;
            }

            String clientPath = args[1];
            String serverPath = args[2];

            Client.uploadFile(server, clientPath, serverPath);
        } else if (command.equalsIgnoreCase("download")) {
            if (args.length < 3) {
                System.out.println("Error: Invalid argument. Specify client and/or server path");
                return;
            }

            String serverPath = args[1];
            String clientPath = args[2];

            Client client = new Client();
            Client.downloadFile(server, client, clientPath, serverPath);
        } else if (command.equalsIgnoreCase("dir")) {
            if (args.length < 2) {
                System.out.println("Error: Invalid argument. Specify server path");
                return;
            }

            String serverPath = args[1];
            Client.listDirectory(server, serverPath);
        } else if (command.equalsIgnoreCase("mkdir")) {
            if (args.length < 2) {
                System.out.println("Error: Invalid argument. Specify server path");
                return;
            }

            String serverPath = args[1];
            Client.createDirectory(server, serverPath);
        } else if (command.equalsIgnoreCase("rmdir")) {
            if (args.length < 2) {
                System.out.println("Error: Invalid argument. Specify server path");
                return;
            }

            String serverPath = args[1];
            Client.removeDirectory(server, serverPath);
        } else if (command.equalsIgnoreCase("rm")) {
            if (args.length < 2) {
                System.out.println("Error: Invalid argument. Specify server path");
                return;
            }

            String serverPath = args[1];
            Client.removeFile(server, serverPath);
        } else if (command.equalsIgnoreCase("shutdown")) {
            Client.shutdown(server);
        }

        System.exit(0);
    }
}
