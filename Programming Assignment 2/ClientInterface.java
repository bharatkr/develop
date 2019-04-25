
import java.rmi.Remote;
import java.rmi.RemoteException;

// Communication methods for server to call client methods
public interface ClientInterface extends Remote {
    
    // Server sends data to client
    public boolean downloadData(String clientFilePath, byte[] data, int dataLength) throws RemoteException;    
}
