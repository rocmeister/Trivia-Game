package egc3_lw31.data;

import common.IMember;
import common.message.status.IStatusSendingError;

/**
 * Status Error.
 */
public class StatusSendingError implements IStatusSendingError {
	/**
	 * Generated ID.
	 */
	private static final long serialVersionUID = 1176525632262885387L;
	/**
	 * The member that disconnected
	 */
	IMember mem;
	
	/**
	 * Constructor
	 * @param rep Member that has a Remote Exception.
	 */
	public StatusSendingError(IMember rep) {
		this.mem = rep;
	}
	
	@Override
	public IMember getMember() {
		return mem;
	}
	@Override
	public String getErrorMessage() {
		return mem.toString() + " of the chatroom has left without being properly disconnected.";
	}

}
