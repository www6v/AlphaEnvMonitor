package com.gw.monitor.alphaenvmonitor.util;

public class BytesUtil {

//    /** 
//     * ��int���͵�����ת��Ϊbyte���� 
//     * ԭ����int�����е��ĸ�byteȡ�����ֱ�洢 
//     * @param n int���� 
//     * @return ���ɵ�byte���� 
//     */  
//   public static byte[] intToBytes(int n){  
//       byte[] b = new byte[4];  
//       for(int i = 0;i < 4;i++){  
//           b[i] = (byte)(n >> (24 - i * 8));   
//       }  
//       return b;  
//   }  
//   
//
//   /** 
//    * ע�ͣ�short���ֽ������ת���� 
//    * 
//    * @param s 
//    * @return 
//    */ 
//   public static byte[] shortToByte(short number) { 
//       int temp = number; 
//       byte[] b = new byte[2]; 
//       for (int i = 0; i < b.length; i++) { 
//           b[i] = new Integer(temp & 0xff).byteValue();// 
//           //�����λ���������λ 
//           temp = temp >> 8; // ������8λ 
//       } 
//       return b; 
//   } 


   //java �ϲ�����byte����
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
		byte[] byte_3 = new byte[byte_1.length+byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}
}
