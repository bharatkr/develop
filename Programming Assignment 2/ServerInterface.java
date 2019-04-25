
import java.rmi.Remote;
import java.rmi.RemoteException;

// Communication methods for client to call server methods
public interface ServerInterface extends Remote {
    
    // Get the filesize of a file
    public long getFileSize(String serverFilePath) throws RemoteException;
    
    // Download file from server to client
    public boolean downloadFileFromClient(ClientInterface client, String serverFilePath, String clientFilePath, long bytesDownloaded) throws RemoteException;
    
    // Get the bytes uploaded to check for resuming upload
    public long getBytesUploaded(String serverFilePath) throws RemoteException;
    
    // Upload a chunk of data to server
    public boolean uploadDataFromClient(String serverFilePath, byte[] data, int dataLength) throws RemoteException;
    
    // Signal server that upload is complete
    public void uploadComplete(String serverFilePath) throws RemoteException;
    
    // List directory in server
    public String listDirectory(String serverPath) throws RemoteException;
    
    // New directory in server
    public boolean createDirectory(String serverPath) throws RemoteException;
    
    // Remove directory in server
    public boolean removeDirectory(String serverPath) throws RemoteException;
    
    // Remove file in server
    public boolean removeFile(String serverPath) throws RemoteException;
    
    // Signal shutdown
    public void shutdown() throws RemoteException;
}
