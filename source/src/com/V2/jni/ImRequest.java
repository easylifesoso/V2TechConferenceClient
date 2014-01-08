package com.V2.jni;

import java.io.ByteArrayInputStream;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

//import com.xinlan.im.adapter.XiuLiuApplication;
//import com.xinlan.im.bean.msgtype.LoginMsgType;
//import com.xinlan.im.bean.msgtype.MoveGroupMsgType;
//import com.xinlan.im.bean.msgtype.MsgType;
//import com.xinlan.im.bean.msgtype.SearchMsgType;
//import com.xinlan.im.bean.msgtype.UpdateMsgType;
//import com.xinlan.im.bean.msgtype.UserStatusMsgType;
//import com.xinlan.im.ui.SplashActivity;
//import com.xinlan.im.ui.chat.bean.NYXUser;
//import com.xinlan.im.ui.chat.db.DbHelper;
//import com.xinlan.im.utils.Logger;
//import com.xinlan.im.utils.XmlParserUtils;

public class ImRequest {
	private Activity context;
	public boolean loginResult;
	private static ImRequest mImRequest;
//	private XiuLiuApplication application;
//	private DbHelper dbHelper;
//	private LoginMsgType loginMsgType;

	private ImRequest(Activity context) {
		this.context = context;
//		application = (XiuLiuApplication) context.getApplication();
//		dbHelper = DbHelper.getInstance(context);
	};

	public static synchronized ImRequest getInstance(Activity context) {
		if (mImRequest == null) {
			mImRequest = new ImRequest(context);
		}

		return mImRequest;
	}

	public native boolean initialize(ImRequest request);

	public native void unInitialize();

	public native void login(String szName, String szPassword, int status,
			int type);

	// 鏇存柊鐨勬垜鐨勫湪绾跨姸鎬�
	public native void updateMyStatus(int nStatus, String szStatusDesc);

	// 淇敼濂藉弸澶囨敞鍚�
	public native void modifyCommentName(long nUserId, String sCommentName);

	// 鑾峰緱涓汉淇℃伅
	public native void getUserBaseInfo(long nUserID);

	// 淇敼涓汉淇℃伅
/*
 * 
 */
	public native void modifyBaseInfo(String InfoXml);

	// 鏇存敼绯荤粺澶村儚
	public native void changeSystemAvatar(String szAvatarName);

	// 鏇存敼鑷畾涔夊ご鍍�
	public native void changeCustomAvatar(String szAvatar, int len,
			String szExtensionName);

	// 鍚姩鑷姩鏇存柊
	public native void onStartUpdate();

	// 鍋滄鑷姩鏇存柊
	public native void onStopUpdate();

	// 鎼滅储鐢ㄦ埛淇℃伅
	public native void searchMember(String szUnsharpName, int nStartNum,
			int nSearchNum);

	// 鎼滅储缇�
	public native void searchCrowd(String szUnsharpName, int nStartNum,
			int nSearchNum);

	// 鍒犻櫎缇ゅ叡浜枃浠�
	public native void delCrowdFile(long nCrowdId, String sFileID);

	public native void getCrowdFileInfo(long nCrowdId);

	/*
	 */
	private void OnLogin(long nUserID, int nStatus, int nResult) {
		Log.e("ImRequest UI", "鐧诲綍绗竴娆″洖璋�" + nUserID + ":锛氾細" + "锛涳紱锛�-:" + nStatus + ":"+ nResult);
//		loginMsgType = new LoginMsgType();
//		loginMsgType.setResult(nResult);
//		loginMsgType.setStatus(nStatus);
//		loginMsgType.setUserid(nUserID);
//		
//		//璇存槑鐧婚檰澶辫触
//		if(nResult==1){
//			Intent intent = new Intent(SplashActivity.IM);
//			intent.putExtra("MsgType", MsgType.LOGIN);
//			intent.putExtra("MSG", loginMsgType);
//			context.sendOrderedBroadcast(intent,null);
//		}
	}

	// 娉ㄩ攢鐨勬柟娉�
	private void OnLogout(int nUserID) {
		Log.e("ImRequest UI", "OnLogout::" + nUserID);
	}




	// 濂藉弸鐨勪釜浜轰俊鎭慨鏀瑰悗杩斿洖锛屼慨鏀逛粈涔堬紝杩斿洖閭ｄ釜瀛楁
	private void OnUpdateBaseInfo(long nUserID, String udatexml) {
		Log.e("ImRequest UI", "OnUpdateBaseInfo::" + nUserID + "  " + udatexml);
		// 鎷艰涓汉淇℃伅
//		UpdateMsgType updateMsgType = new UpdateMsgType();
//		updateMsgType.setUpdatexml(udatexml);
//		updateMsgType.setUserid(nUserID);
//
//		Intent addIntent = new Intent(SplashActivity.IM);
//		addIntent.putExtra("MsgType", MsgType.UPDATEPERSON);
//		addIntent.putExtra("MSG", updateMsgType);
//		context.sendOrderedBroadcast(addIntent,null);
	}



	// 杈撳叆鍏抽敭璇嶈繘琛屾悳绱�
	private void OnGetSearchMember(String xmlinfo) {
		Log.e("ImRequest UI", "OnGetSearchMember:" + xmlinfo);
//		List<NYXUser> searchUsers = XmlParserUtils
//				.parserSearchUsers(new ByteArrayInputStream(xmlinfo.getBytes()));
//		// 鎷艰娑堟伅
//		SearchMsgType searchMsgType = new SearchMsgType();
//		searchMsgType.setSearchUsers(searchUsers);
//
//		Intent addIntent = new Intent(SplashActivity.IM);
//		addIntent.putExtra("MsgType", MsgType.SEARCH);
//		addIntent.putExtra("MSG", searchMsgType);
//		context.sendOrderedBroadcast(addIntent,null);
	}

	// nStatus鍙涓嶆槸0 灏卞湪绾�
	private void OnUserStatusUpdated(long nUserID, int nStatus,
			String szStatusDesc) {
		Log.e("ImRequest UI","鐢ㄦ埛鐘舵�鏇存柊" + nUserID + ":" + nStatus + ":" + szStatusDesc);

		// 鎷艰娑堟伅
//		UserStatusMsgType statusMsgType = new UserStatusMsgType();
//		statusMsgType.setUserid(nUserID);
//		statusMsgType.setOnline(nStatus != 0 ? true : false);
//		Intent addIntent = new Intent(SplashActivity.IM);
//		addIntent.putExtra("MsgType", MsgType.USER_STATUS);
//		addIntent.putExtra("MSG", statusMsgType);
//		context.sendOrderedBroadcast(addIntent, null);
	}



	private void OnUserPrivacyUpdated(long nUserID, int nPrivacy) {
		Log.e("ImRequest UI", "OnUserPrivacyUpdated");
	}

	// 鍒涘缓缁勬垚鍔熷悗鐨勫洖璋�
	private void OnCreateFriendGroup(long nGroupID, String szGroupName) {
		Log.e("ImRequest UI", "OnCreateFriendGroup锛氾細" + nGroupID + ":"
				+ szGroupName);
	}


	// 淇敼缁勬垚鍔熷悗鐨勫洖璋�
	private void OnModifyFriendGroup(long nGroupID, String szGroupName) {
		Log.e("ImRequest UI", "OnModifyFriendGroup::" + nGroupID + ":"
				+ szGroupName);

	
	}


	// 鍙楀埌濂藉弸鎴愬姛绉诲姩缁勭殑鍥炶皟
	private void OnMoveFriendsToGroup(long nDstUserID, long nDstGroupID) {
		Log.e("ImRequest UI", "OnMoveFriendsToGroup" + nDstUserID + ":"+ nDstGroupID);

//		// 鎷艰娑堟伅
//		MoveGroupMsgType moveMsgtype = new MoveGroupMsgType();
//		moveMsgtype.setnDstGroupID(nDstGroupID);
//		moveMsgtype.setnDstUserID(nDstUserID);
//
//		// 閫氳繃骞挎挱鍙戦�娑堟伅鏉ラ�鐭�鏇存柊鏈�繎杩炵画浜虹敾闈�
//		Intent moveIntent = new Intent(SplashActivity.IM);
//		moveIntent.putExtra("MsgType", MsgType.MOVE_GROUP);
//		moveIntent.putExtra("MSG", moveMsgtype);
//		context.sendOrderedBroadcast(moveIntent,null);
	}


	private void OnChangeAvatar(int nAvatarType, long nUserID, String AvatarName) {
		Log.e("ImRequest UI", "OnChangeAvatar");
	}

	private void OnHaveUpdateNotify(String updatefilepath, String updatetext) {
		Log.e("ImRequest UI", "OnHaveUpdateNotify");
	}

	private void OnServerFaild(String sModuleName) {
		Log.e("ImRequest UI", "OnServerFaild");
	}

	private void OnConnectResponse(int nResult) {
		Log.e("ImRequest UI", "OnConnectResponse::" + nResult);
		//杩炴帴鏈嶅姟鍣ㄥけ璐�
//		if(nResult==301){
//			Intent intent = new Intent(SplashActivity.IM);
//			intent.putExtra("MsgType", MsgType.SERVER);
//			context.sendOrderedBroadcast(intent,null);
//		}
		
		
	}

	private void OnUpdateDownloadBegin(long filesize) {
		Log.e("ImRequest UI", "OnUpdateDownloadBegin::" + filesize);
	}

	private void OnUpdateDownloading(long size) {
		Log.e("ImRequest UI", "OnUpdateDownloading::" + size);
	}

	private void OnUpdateDownloadEnd(boolean error)

	{
		Log.e("ImRequest UI", "OnUpdateDownloadEnd:" + error);
	}

	private void OnCreateCrowd(String sCrowdXml, int nResult) {
		Log.e("ImRequest UI", "OnCreateCrowd  " + "sCrowdXml:" + sCrowdXml
				+ "  nResult:" + nResult);
	}


	// 琚Щ鍑虹兢
	private void OnKickCrowd(long nCrowdId, long nAdminId) {
		Log.e("ImRequest UI", "OnKickCrowd:" + nCrowdId);
	}


	// 鎼滅储缇ゅ洖璋�
	private void OnSearchCrowd(String InfoXml) {
		Log.e("ImRequest UI", "OnSearchCrowd::" + InfoXml);
	}


	// 缇ゅ叡浜洖璋�
	private void Oncrowdfile(long nCrowdId, String InfoXml) {
		Log.e("ImRequest UI", "Oncrowdfile:" + nCrowdId);
	}

	// 鑾峰彇缇ゅ叡浜俊鎭�
	private void OnGetCrowdFileInfo(long nCrowdId, String InfoXml) {
		Log.e("ImRequest UI", "OnGetCrowdFileInfo:" + nCrowdId);
	}

	// 鍒犻櫎缇ゅ叡浜枃浠跺洖璋�
	private void OnDelCrowdFile(long nCrowdId, String sFileID) {
		Log.e("ImRequest UI", "OnDelCrowdFile:" + nCrowdId);
	}


	// 淇敼澶囨敞濮撳悕
	private void OnModifyCommentName(long nUserId, String sCommmentName) {
		Log.e("ImRequest UI", "OnModifyCommentName::" + "nUserId:" + nUserId
				+ "  sCommmentName" + sCommmentName);
	}

	private void OnSignalDisconnected() {
		Log.e("ImRequest UI", "OnSignalDisconnected");
	}

	private void OnDelGroupInfo(int type, long groupid, boolean isdel) {
		Log.e("ImRequest UI", "OnDelGroupInfo" + type + ":" + groupid + ":"+ isdel);
	}

	// 鏍囧織鐫�幏鍙栫敤鎴峰紑濮�
	private void OnGetGroupsInfoBegin() {
		Log.e("ImRequest UI", "寮�鑾峰彇缁凮nGetGroupsInfoBegin");
	}

	// 鏍囧織鐫�幏鍙栫敤鎴风粨鏉�
	
	private boolean haslogin=false; //鏍囧織宸茬粡鍙戦�浜嗙櫥闄嗙姸鎬�
	private void OnGetGroupsInfoEnd() {
		Log.e("ImRequest UI", "OnGetGroupsInfoEnd");

		// 鐧婚檰瀹屾垚锛屽苟涓旇幏寰楃敤鎴峰垪琛ㄥ悗鍐嶅紑濮嬭繘鍏ヤ富鐣岄潰
//		Intent intent = new Intent(SplashActivity.IM);
//		intent.putExtra("MsgType", MsgType.LOGIN);
//		intent.putExtra("MSG", loginMsgType);
//		
//		if(!haslogin){
//			context.sendOrderedBroadcast(intent,null);
//			haslogin=true;
//		}
	}


}