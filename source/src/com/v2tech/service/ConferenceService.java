package com.v2tech.service;

import android.os.Handler;
import android.os.Message;

import com.V2.jni.ConfRequest;
import com.V2.jni.ConfRequestCallback;
import com.V2.jni.VideoRequest;
import com.V2.jni.VideoRequestCallback;
import com.v2tech.logic.AsynResult;
import com.v2tech.logic.CameraConfiguration;
import com.v2tech.logic.Conference;
import com.v2tech.logic.ConferencePermission;
import com.v2tech.logic.UserDeviceConfig;
import com.v2tech.logic.AsynResult.AsynState;
import com.v2tech.logic.jni.JNIResponse;
import com.v2tech.logic.jni.RequestCloseUserVideoDeviceResponse;
import com.v2tech.logic.jni.RequestEnterConfResponse;
import com.v2tech.logic.jni.RequestExitedConfResponse;
import com.v2tech.logic.jni.RequestOpenUserVideoDeviceResponse;
import com.v2tech.logic.jni.RequestPermissionResponse;
import com.v2tech.logic.jni.RequestUpdateCameraParametersResponse;
import com.v2tech.util.V2Log;

/**
 * <ul>
 * This class is use to conference business.
 * </ul>
 * <ul>
 * When user entered conference room, user can use
 * {@link #requestOpenVideoDevice(Conference, UserDeviceConfig, Message)} and
 * {@link #requestCloseVideoDevice(Conference, UserDeviceConfig, Message)} to
 * open or close video include self.
 * </ul>
 * <ul>
 * <li>User request to enter conference :
 * {@link #requestEnterConference(Conference, Message)}</li>
 * <li>User request to exit conference :
 * {@link #requestExitConference(Conference, Message)}</li>
 * <li>User request to open video device :
 * {@link #requestOpenVideoDevice(Conference, UserDeviceConfig, Message)}</li>
 * <li>User request to close video device:
 * {@link #requestCloseVideoDevice(Conference, UserDeviceConfig, Message)}</li>
 * <li>User request to request speak in conference
 * {@link #applyForControlPermission(ConferencePermission, Message)}</li>
 * <li>User request to release speaker in conference
 * {@link #applyForReleasePermission(ConferencePermission, Message)}</li>
 * </ul>
 * 
 * @author 28851274
 * 
 */
public class ConferenceService extends AbstractHandler {

	private static final int JNI_REQUEST_ENTER_CONF = 1;
	private static final int JNI_REQUEST_EXIT_CONF = 2;
	private static final int JNI_REQUEST_OPEN_VIDEO = 3;
	private static final int JNI_REQUEST_CLOSE_VIDEO = 4;
	private static final int JNI_REQUEST_SPEAK = 5;
	private static final int JNI_REQUEST_RELEASE_SPEAK = 6;

	private static final int JNI_ATTENDEE_ENTERED_NOTIFICATION = 57;
	private static final int JNI_ATTENDEE_EXITED_NOTIFICATION = 58;
	private static final int JNI_UPDATE_CAMERA_PAR = 75;

	private VideoRequestCB videoCallback;
	private ConfRequestCB confCallback;

	public ConferenceService() {
		super();
		videoCallback = new VideoRequestCB(this);
		VideoRequest.getInstance().addCallback(videoCallback);
		confCallback = new ConfRequestCB(this);
		ConfRequest.getInstance().addCallback(confCallback);
	}

	/**
	 * User request to enter conference.<br>
	 * 
	 * @param conf
	 *            {@link Conference} object which user wants to enter
	 * @param caller
	 *            if input is null, ignore response Message.object is
	 *            {@link AsynResult} AsynResult.obj is
	 *            {@link com.v2tech.logic.jni.RequestEnterConfResponse}
	 * 
	 * @see com.v2tech.logic.jni.RequestEnterConfResponse
	 */
	public void requestEnterConference(Conference conf, Message caller) {
		initTimeoutMessage(JNI_REQUEST_ENTER_CONF, null, DEFAULT_TIME_OUT_SECS,
				caller);
		ConfRequest.getInstance().enterConf(conf.getID());
	}

	/**
	 * User request to quit conference
	 * 
	 * @param conf
	 *            {@link Conference} object which user wants to enter
	 * @param msg
	 *            if input is null, ignore response Message.object is
	 *            {@link AsynResult} AsynResult.obj is
	 *            {@link com.v2tech.logic.jni.RequestExitedConfResponse}
	 */
	public void requestExitConference(Conference conf, Message caller) {
		initTimeoutMessage(JNI_REQUEST_EXIT_CONF, null, DEFAULT_TIME_OUT_SECS,
				caller);
		ConfRequest.getInstance().exitConf(conf.getID());
		// send response to caller because exitConf no call back from JNI
		JNIResponse jniRes = new RequestExitedConfResponse(conf.getID(),
				System.currentTimeMillis() / 1000, JNIResponse.Result.SUCCESS);
		Message res = Message.obtain(this, JNI_REQUEST_EXIT_CONF, jniRes);
		// send delayed message for that make sure send response after JNI
		// request
		this.sendMessageDelayed(res, 300);
	}

	/**
	 * User request to open video device.
	 * 
	 * @param conf
	 *            {@link Conference} object which user entered
	 * @param userDevice
	 *            {@link UserDeviceConfig} if want to open local video,
	 *            {@link UserDeviceConfig#getVp()} should be null and
	 *            {@link UserDeviceConfig#getDeviceID()} should be ""
	 * @param caller
	 *            if input is null, ignore response Message.object is
	 *            {@link AsynResult} AsynResult.obj is
	 *            {@link com.v2tech.logic.jni.RequestOpenUserVideoDeviceResponse}
	 * 
	 * @see UserDeviceConfig
	 */
	public void requestOpenVideoDevice(Conference conf,
			UserDeviceConfig userDevice, Message caller) {
		initTimeoutMessage(JNI_REQUEST_OPEN_VIDEO, null, DEFAULT_TIME_OUT_SECS,
				caller);

		VideoRequest.getInstance().openVideoDevice(conf.getID(),
				userDevice.getUserID(), userDevice.getDeviceID(),
				userDevice.getVp(), userDevice.getBusinessType());
		JNIResponse jniRes = new RequestOpenUserVideoDeviceResponse(
				conf.getID(), System.currentTimeMillis() / 1000,
				RequestOpenUserVideoDeviceResponse.Result.SUCCESS);

		// send delayed message for that make sure send response after JNI
		Message res = Message.obtain(this, JNI_REQUEST_OPEN_VIDEO, jniRes);
		this.sendMessageDelayed(res, 300);

	}

	/**
	 * User request to close video device.
	 * 
	 * @param nGroupID
	 * @param userDevice
	 *            {@link UserDeviceConfig} if want to open local video,
	 *            {@link UserDeviceConfig#getVp()} should be null and
	 *            {@link UserDeviceConfig#getDeviceID()} should be ""
	 * @param caller
	 *            if input is null, ignore response Message.object is
	 *            {@link AsynResult} AsynResult.obj is
	 *            {@link com.v2tech.logic.jni.RequestCloseUserVideoDeviceResponse}
	 * 
	 * @see UserDeviceConfig
	 */
	public void requestCloseVideoDevice(Conference conf,
			UserDeviceConfig userDevice, Message caller) {

		initTimeoutMessage(JNI_REQUEST_CLOSE_VIDEO, null,
				DEFAULT_TIME_OUT_SECS, caller);

		VideoRequest.getInstance().closeVideoDevice(conf.getID(),
				userDevice.getUserID(), userDevice.getDeviceID(),
				userDevice.getVp(), userDevice.getBusinessType());
		JNIResponse jniRes = new RequestCloseUserVideoDeviceResponse(
				conf.getID(), System.currentTimeMillis() / 1000,
				RequestCloseUserVideoDeviceResponse.Result.SUCCESS);

		// send delayed message for that make sure send response after JNI
		Message res = Message.obtain(this, JNI_REQUEST_CLOSE_VIDEO, jniRes);
		this.sendMessageDelayed(res, 300);
	}

	/**
	 * User request speak permission on the conference.
	 * 
	 * @param type
	 *            speak type should be {@link ConferencePermission#SPEAKING}
	 * @param caller
	 *            if input is null, ignore response Message.object is
	 *            {@link AsynResult} AsynResult.obj is
	 *            {@link com.v2tech.logic.jni.RequestPermissionResponse}
	 * 
	 * @see ConferencePermission
	 */
	public void applyForControlPermission(ConferencePermission type,
			Message caller) {
		initTimeoutMessage(JNI_REQUEST_SPEAK, null, DEFAULT_TIME_OUT_SECS,
				caller);

		ConfRequest.getInstance().applyForControlPermission(type.intValue());

		JNIResponse jniRes = new RequestPermissionResponse(
				RequestPermissionResponse.Result.SUCCESS);

		// send delayed message for that make sure send response after JNI
		Message res = Message.obtain(this, JNI_REQUEST_SPEAK, jniRes);
		this.sendMessageDelayed(res, 300);
	}

	/**
	 * Request release permission on the conference.
	 * 
	 * @param type
	 *            speak type should be {@link ConferencePermission#SPEAKING}
	 * @param caller
	 *            if input is null, ignore response Message.object is
	 *            {@link AsynResult} AsynResult.obj is
	 *            {@link com.v2tech.logic.jni.RequestPermissionResponse}
	 * 
	 * @see ConferencePermission
	 */
	public void applyForReleasePermission(ConferencePermission type,
			Message caller) {

		initTimeoutMessage(JNI_REQUEST_RELEASE_SPEAK, null,
				DEFAULT_TIME_OUT_SECS, caller);

		ConfRequest.getInstance().releaseControlPermission(type.intValue());

		JNIResponse jniRes = new RequestPermissionResponse(
				RequestPermissionResponse.Result.SUCCESS);

		// send delayed message for that make sure send response after JNI
		Message res = Message.obtain(this, JNI_REQUEST_RELEASE_SPEAK, jniRes);
		this.sendMessageDelayed(res, 300);
	}

	/**
	 * Update current user's camera. Including front-side or back-side camera
	 * switch.
	 * 
	 * @param cc
	 *            {@link CameraConfiguration}
	 * @param caller
	 *            if input is null, ignore response Message.object is
	 *            {@link AsynResult} AsynResult.obj is
	 *            {@link com.v2tech.logic.jni.RequestUpdateCameraParametersResponse}
	 */
	public void updateCameraParameters(CameraConfiguration cc, Message caller) {
		initTimeoutMessage(JNI_UPDATE_CAMERA_PAR, null, DEFAULT_TIME_OUT_SECS,
				caller);
		VideoRequest.getInstance().setCapParam(cc.getDeviceId(),
				cc.getCameraIndex(), cc.getFrameRate(), cc.getBitRate());
	}

	@Override
	public void handleMessage(Message msg) {
		// Do time out handle
		super.handleMessage(msg);
		// remove time out message
		Message caller = super.removeTimeoutMessage(msg.what);
		if (caller == null) {
			V2Log.w("Igore message client don't expect callback");
			return;
		}
		switch (msg.what) {
		case JNI_REQUEST_ENTER_CONF:
		case JNI_REQUEST_EXIT_CONF:
		case JNI_REQUEST_OPEN_VIDEO:
		case JNI_REQUEST_SPEAK:
		case JNI_REQUEST_RELEASE_SPEAK:
		case JNI_UPDATE_CAMERA_PAR:
			Object origObject = caller.obj;
			caller.obj = new AsynResult(AsynResult.AsynState.SUCCESS, msg.obj);
			JNIResponse jniRes = (JNIResponse) msg.obj;
			jniRes.callerObject = origObject;
			break;
		default:
			break;
		}

		caller.sendToTarget();
	}

	class ConfRequestCB implements ConfRequestCallback {

		private Handler mCallbackHandler;

		public ConfRequestCB(Handler mCallbackHandler) {
			this.mCallbackHandler = mCallbackHandler;
		}

		@Override
		public void OnEnterConfCallback(long nConfID, long nTime,
				String szConfData, int nJoinResult) {
			JNIResponse jniRes = new RequestEnterConfResponse(
					nConfID,
					nTime,
					szConfData,
					nJoinResult == JNIResponse.Result.SUCCESS.value() ? JNIResponse.Result.SUCCESS
							: JNIResponse.Result.FAILED);
			Message.obtain(mCallbackHandler, JNI_REQUEST_ENTER_CONF, jniRes)
					.sendToTarget();
		}

		@Override
		public void OnConfMemberEnterCallback(long nConfID, long nTime,
				String szUserInfos) {
			int start = szUserInfos.indexOf("id='");
			if (start != -1) {
				int end = szUserInfos.indexOf("'", start + 4);
				if (end != -1) {
					String id = szUserInfos.substring(start + 4, end);
					Message.obtain(mCallbackHandler,
							JNI_ATTENDEE_ENTERED_NOTIFICATION, id)
							.sendToTarget();
				} else {
					V2Log.e("Invalid attendee user id ignore callback message");
				}
			} else {
				V2Log.e("Invalid attendee user id ignore callback message");
			}
		}

		@Override
		public void OnConfMemberExitCallback(long nConfID, long nTime,
				long nUserID) {
			Message.obtain(mCallbackHandler, JNI_ATTENDEE_EXITED_NOTIFICATION,
					0, 0, nUserID).sendToTarget();
		}

	}

	class VideoRequestCB implements VideoRequestCallback {

		private Handler mCallbackHandler;

		public VideoRequestCB(Handler mCallbackHandler) {
			this.mCallbackHandler = mCallbackHandler;
		}

		@Override
		public void OnRemoteUserVideoDevice(String szXmlData) {

		}

		@Override
		public void OnVideoChatInviteCallback(long nGroupID, int nBusinessType,
				long nFromUserID, String szDeviceID) {

		}

		@Override
		public void OnSetCapParamDone(String szDevID, int nSizeIndex,
				int nFrameRate, int nBitRate) {
			JNIResponse jniRes = new RequestUpdateCameraParametersResponse(
					new CameraConfiguration(szDevID, 1, nFrameRate, nBitRate),
					RequestUpdateCameraParametersResponse.Result.SUCCESS);
			Message.obtain(mCallbackHandler, JNI_UPDATE_CAMERA_PAR, jniRes)
					.sendToTarget();

		}

	}

}
