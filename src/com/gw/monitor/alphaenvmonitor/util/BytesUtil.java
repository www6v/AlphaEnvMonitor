package com.gw.monitor.alphaenvmonitor.util;

public class BytesUtil {

//    /** 
//     * 将int类型的数据转换为byte数组 
//     * 原理：将int数据中的四个byte取出，分别存储 
//     * @param n int数据 
//     * @return 生成的byte数组 
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
//    * 注释：short到字节数组的转换！ 
//    * 
//    * @param s 
//    * @return 
//    */ 
//   public static byte[] shortToByte(short number) { 
//       int temp = number; 
//       byte[] b = new byte[2]; 
//       for (int i = 0; i < b.length; i++) { 
//           b[i] = new Integer(temp & 0xff).byteValue();// 
//           //将最低位保存在最低位 
//           temp = temp >> 8; // 向右移8位 
//       } 
//       return b; 
//   } 


   //java 合并两个byte数组
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
		byte[] byte_3 = new byte[byte_1.length+byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}
}
