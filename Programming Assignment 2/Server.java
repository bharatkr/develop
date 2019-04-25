
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements ServerInterface {

    // Initialize the server
    public Server() throws RemoteException {
        super();
    }

    // Get filesize of a file
    @Override
    public long getFileSize(String serverFilename) throws RemoteException {
        try {
            if (!(new File(serverFilename).exists())) {
                // Signal client the path is invalid
                return -1;
            }

            // Send to the client the file size
            return new File(serverFilename).length();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return -1;
    }

    // Client initiates a download of a file, it will send the bytes downloaded if it has to resume
    @Override
    public boolean downloadFileFromClient(ClientInterface client, String serverFilename, String clientFilename, long bytesDownloaded) throws RemoteException {
        try {

            if (!(new File(serverFilename).exists())) {
                // Signal client the path is invalid
                return false;
            }

            // Send to the client the data
            FileInputStream fis = new FileInputStream(serverFilename);

            // Advance the reading of the file input stream to the resume point
            for (long i = 0; i < bytesDownloaded; i++) {
                fis.read();
            }

            // Now perform the sending of data to client
            byte[] data = new byte[1024];
            int read;

            while ((read = fis.read(data)) >= 0) {
                if (!client.downloadData(clientFilename, data, read)) {
                    throw new Exception();
                }
            }

            fis.close();
            System.out.println("Download complete!");
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return false;
        }

        return true;
    }

    // Get the bytes uploaded to check for resuming upload
    @Override
    public long getBytesUploaded(String serverFilePath) throws RemoteException {
        File tempUploadFile = new File(serverFilePath + ".tmp");

        if (tempUploadFile.exists()) {
            return tempUploadFile.length();
        }

        // No file
        return 0;
    }

    // Upload a chunk of data to server
    @Override
    public boolean uploadDataFromClient(String serverFilePath, byte[] data, int dataLength) throws RemoteException {
        try {
            FileOutputStream fos = new FileOutputStream(serverFilePath + ".tmp", true);
            fos.write(data, 0, dataLength);
            fos.flush();
            fos.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return false;
    }

    // Signal server that upload is complete, delete the temp file, and show the original
    @Override
    public void uploadComplete(String serverFilePath) throws RemoteException {
        // If an original file exists, delete it
        File file = new File(serverFilePath);

        if (file.exists()) {
            file.delete();
        }

        File tempUploadFile = new File(serverFilePath + ".tmp");
        tempUploadFile.renameTo(new File(serverFilePath));
        tempUploadFile.delete();
    }

    // List directory in server
    @Override
    public String listDirectory(String serverPath) throws RemoteException {
        // Make sure path is a directory
        if (!(new File(serverPath).exists()) || !(new File(serverPath).isDirectory())) {
            return "";
        }

        String listing = "";

        for (File file : new File(serverPath).listFiles()) {
            if (file.isDirectory()) {
                listing += file.getName() + " <DIR>\n";
            } else {
                listing += file.getName() + " <FILE>\n";
            }
        }

        listing = listing.trim();
        return listing;
    }

    // New directory in server
    @Override
    public boolean createDirectory(String serverPath) throws RemoteException {
        return new File(serverPath).mkdir();
    }

    // Remove directory in server
    @Override
    public boolean removeDirectory(String serverPath) throws RemoteException {
        if (!(new File(serverPath).exists()) || !(new File(serverPath).isDirectory())) {
            return false;
        }

        return new File(serverPath).delete();
    }

    // Remove file in server
    @Override
    public boolean removeFile(String serverPath) throws RemoteException {
        if (!(new File(serverPath).exists()) || (new File(serverPath).isDirectory())) {
            return false;
        }

        return new File(serverPath).delete();
    }

    // Signal shutdown
    @Override
    public void shutdown() throws RemoteException {
        System.out.println("Server shutdown!");
        System.exit(0);
    }

    // Start the server
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Error: Specify server start argument");
            return;
        }

        if (args[0].equalsIgnoreCase("start")) {
            // Start a server
            String serverAddress = InetAddress.getLocalHost().getHostAddress();
            // String serverAddress = "127.0.0.1";
            int portNumber = Integer.parseInt(args[1]);

            LocateRegistry.createRegistry(1099);
            Server server = new Server();
            Naming.rebind("rmi://" + serverAddress + "/fileserver", server);
            System.out.println("Running Server at " + serverAddress + ":" + portNumber + "...");
        }
    }
}
