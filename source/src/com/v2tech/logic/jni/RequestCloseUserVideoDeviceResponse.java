package com.v2tech.logic.jni;

/**
 * Used to wrap response data from JNI when receive call from JNI
 * @author 28851274
 *
 */
public class RequestCloseUserVideoDeviceResponse extends JNIResponse {

	
	
	
	
	
	long nConfID;
	long nTime;
	Result er;

	/**
	 * This class is wrapper that wrap response of request to close user video device
	 * @param nConfID
	 * @param nTime
	 * @param result {@link Result}
	 */
	public RequestCloseUserVideoDeviceResponse(long nConfID, long nTime,
			Result result) {
		super(result);
		this.nConfID = nConfID;
		this.nTime = nTime;
		er = result;
	}

}
