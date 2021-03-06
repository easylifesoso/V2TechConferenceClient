package com.v2tech.logic;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.v2tech.util.StorageUtil;
import com.v2tech.util.V2Log;

public class VImageMessage extends VMessage {

	private String mExtension;
	private String mUUID;
	private int mHeight = -1;
	private int mWidth = -1;
	private byte[] originImageData;
	private String mImagePath;

	public VImageMessage(User u, User toUser, String imagePath, boolean isRemote) {
		super(u, toUser, null, isRemote);
		if (imagePath == null) {
			throw new NullPointerException(" image path can not be null");
		}
		this.mImagePath = imagePath;
		this.mType = VMessage.MessageType.IMAGE;
		init();
	}

	public VImageMessage(User u, User toUser, byte[] data) {
		super(u, toUser, null, true);
		this.mType = VMessage.MessageType.IMAGE;
		this.originImageData = data;
		if (this.originImageData != null) {
			if (this.originImageData.length < 52) {
				throw new RuntimeException("Illegal image data");
			}
			byte[] uuidBytes = new byte[38];
			System.arraycopy(originImageData, 0, uuidBytes, 0, 38);
			mUUID = new String(uuidBytes);
			byte[] extensionBytes = new byte[4];
			System.arraycopy(originImageData, 41, extensionBytes, 0, 4);
			mExtension = new String(extensionBytes);
			mImagePath = StorageUtil.getAbsoluteSdcardPath() + "/v2tech/pics/"
					+ mUUID + mExtension;
			File f = new File(mImagePath);
			OutputStream os = null;
			try {
				os = new FileOutputStream(f);
				os.write(data, 52, data.length-52);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public VImageMessage(User u, User toUser, String uuid, String ext) {
		super(u, toUser, null, true);
		this.mType = VMessage.MessageType.IMAGE;
		this.mUUID = uuid;
		this.mExtension = ext;
		mImagePath = StorageUtil.getAbsoluteSdcardPath() + "/v2tech/pics/"
				+ uuid + ext;
	}

	@Override
	public String getText() {
		return mUUID + "|" + mExtension + "|" + mHeight + "|" + mWidth + "|"
				+ mImagePath;
	}

	private void init() {
		String uuid = UUID.randomUUID().toString();
		mUUID = "{" + uuid + "}";
		int pos = mImagePath.lastIndexOf(".");
		if (pos != -1) {
			mExtension = mImagePath.substring(pos);
		}
	}

	public byte[] getWrapperData() {
		if (originImageData == null && !loadImageData()) {
			return null;
		}
		byte[] d = new byte[52 + originImageData.length];
		byte[] uud = mUUID.getBytes();
		System.arraycopy(uud, 0, d, 0, uud.length);
		byte[] et = mExtension.getBytes();
		System.arraycopy(et, 0, d, 41, et.length);
		System.arraycopy(originImageData, 0, d, 52, originImageData.length);
		return d;
	}

	public int getHeight() {
		if (mHeight <= 0) {
			loadImageData();
		}
		return mHeight;
	}

	public int getWidth() {
		if (mWidth <= 0) {
			loadImageData();
		}
		return mWidth;
	}

	public String getImagePath() {
		return this.mImagePath;
	}

	private boolean loadImageData() {
		if (originImageData == null) {
			File f = new File(mImagePath);
			if (!f.exists()) {
				V2Log.e(" file doesn't exist " + mImagePath);
				return false;
			}
			originImageData = new byte[(int) f.length()];
			InputStream is = null;
			try {
				is = new FileInputStream(f);
				is.read(originImageData);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		InputStream is = null;
		is = new ByteArrayInputStream(originImageData);
		Bitmap bm = BitmapFactory.decodeStream(is);
		mHeight = bm.getHeight();
		mWidth = bm.getWidth();
		bm.recycle();
		try {
			is.close();
		} catch (IOException e) {
			V2Log.e("can not close stream for bitmap");
		}
		return true;
	}

	// < TPictureChatItem NewLine="False" AutoResize="True" FileExt=".png"
	// GUID="{F3870296-746D-4E11-B69B-050B2168C624}" Height="109" Width="111"/>
	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
				.append("<TChatData IsAutoReply=\"False\">\n")
				.append("<FontList>\n")
				.append("<TChatFont Color=\"255\" Name=\"Segoe UI\" Size=\"18\" Style=\"\"/>")
				.append("</FontList>\n")
				.append("<ItemList>\n")
				.append("<TPictureChatItem NewLine=\"False\" AutoResize=\"True\" FileExt=\""
						+ mExtension
						+ "\" GUID=\""
						+ mUUID
						+ "\" Height=\""
						+ getHeight() + "\" Width=\"" + getWidth() + "\"/>")
				.append("</ItemList>").append("</TChatData>");
		return sb.toString();
	}
	
	
	Bitmap mFullQualityBitmap = null;
	Bitmap mCompressedBitmap = null;
	public Bitmap getFullQuantityBitmap() {
		if (mFullQualityBitmap == null || mFullQualityBitmap.isRecycled()) {
			mFullQualityBitmap = BitmapFactory.decodeFile(this.mImagePath);
			if (mFullQualityBitmap.getWidth() > 1024 || mFullQualityBitmap.getHeight() > 768) {
				Bitmap tmp = Bitmap.createScaledBitmap(mFullQualityBitmap, 1024, 768, true);
				mFullQualityBitmap.recycle();
				mFullQualityBitmap = tmp;
			}
		}
		return mFullQualityBitmap;
	}
	
	public Bitmap getCompressedBitmap() {
		if (mCompressedBitmap == null || mFullQualityBitmap.isRecycled()) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			options.outHeight = 200;
			options.outWidth = 200;
			mCompressedBitmap = BitmapFactory.decodeFile(this.mImagePath, options);
		}
		return mCompressedBitmap;
	}
	
	public void recycle() {
		if (mFullQualityBitmap != null) {
			mFullQualityBitmap.recycle();
			mFullQualityBitmap = null;
		}
		if (mCompressedBitmap != null) {
			mCompressedBitmap.recycle();
			mCompressedBitmap = null;
		}
	}
}
