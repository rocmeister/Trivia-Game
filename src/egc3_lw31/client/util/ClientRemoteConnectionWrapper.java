package egc3_lw31.client.util;

import java.rmi.RemoteException;

import common.IRemoteConnection;

/**
 * Wrapper around the remote connection
 */
public class ClientRemoteConnectionWrapper {
	/**
	 * The user stub wrapped in 
	 */
	private IRemoteConnection remoteConnectionStub;
	
	/**
	 * Constructor
	 * @param iRemoteConnection The remote connection stub
	 */
	public ClientRemoteConnectionWrapper(IRemoteConnection iRemoteConnection) {
		this.remoteConnectionStub = iRemoteConnection;
	}
	
	/**
	 * Gets the user of the remote connection stub
	 * @return The remote connection user
	 */
	public IRemoteConnection getUser() {
		return this.remoteConnectionStub;
	}
	
	@Override
	public String toString() {
		String name = "unknown user";
		try {
			name = remoteConnectionStub.getName();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return name;
	}
}
