package v2av;

import android.view.SurfaceHolder;

/*
 * ��Ƶ¼�Ʋ���
 */
public class VideoRecordInfo 
{
	//��Ƶ¼�Ƴߴ�
	public int mVideoWidth = 176;
	public int mVideoHeight = 144;
	//��Ƶ¼��֡��,���ֻ�ϵͳ֧��
	public int mFrameRate = 15;//10��ʱ������4.0ϵͳ����ͷ����
	
	//ʹ��ǰ������ͷʱ,�Ƿ�Ϊ"�վ���"Ч��
	public boolean mbMirror = false;
	
	//��ǰʹ������ͷ�ĽǶ�
	public int mCameraRotation = 0;
	
	//��ǰ�豸�Ƕ�
	public int mDisplayRotation = 0;
	
	public SurfaceHolder mSurfaceHolder = null;
}