package com.cn.conciseframe.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p> Description: MD5 加密工具类 </p>
 * <p> Copyright: Copyright (c) 2015 </p>
 * <p> Create Date: 2015-8-24 </p>
 * <p> Company:  </p> 
 * @author 
 * @version $Id: MD5Util.java,v 1.1 $
 */
public class MD5Util {
		static MessageDigest md = null;
	
	    static {
	        try {
	            md = MessageDigest.getInstance("MD5");
	        } catch (NoSuchAlgorithmException ne) {
	        }
	    }
	    
	    /**
	     * 对字符串做md5 加密
	     * @param data 字符串 
	     * @return  加密后结果
	     * @throws NoSuchAlgorithmException
	     */
		public static String md5(String data)  {
	
			md.update(data.getBytes());
	
			StringBuffer buf = new StringBuffer();
	
			byte[] bits = md.digest();
	
			for (int i = 0; i < bits.length; i++) {
	
				int a = bits[i];
	
				if (a < 0)
					a += 256;
	
				if (a < 16)
					buf.append("0");
	
				buf.append(Integer.toHexString(a));
	
			}
	
			return buf.toString();
	
		}
	
	     /**
	      * 对文件做md5加密 
	      * @param file 文件对象
	      * @return 加密后结果 
	      * @throws IOException
	      */
        @SuppressWarnings("resource")
		public static String getFileMD5String(File file) throws IOException { 
                FileInputStream in = new FileInputStream(file);   
                FileChannel ch = in.getChannel();   
                MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());   
                md.update(byteBuffer);   
                return bufferToHex(md.digest());   
        } 

        /**
         * 对字符串进行MD5加密 
         * @param s 字符串
         * @return
         */
        public static String getMD5String(String s) { 
                return getMD5String(s.getBytes());   
        } 
        
        
        protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',  '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };   

        /**
         * 对byte类型的数组进行MD5加密 
         * @param bytes
         * @return
         */
        public static String getMD5String(byte[] bytes) { 
                md.update(bytes);   
                return bufferToHex(md.digest());   
        } 

        private static String bufferToHex(byte bytes[]) { 
                return bufferToHex(bytes, 0, bytes.length);   
        } 

        private static String bufferToHex(byte bytes[], int m, int n) { 
                StringBuffer stringbuffer = new StringBuffer(2 * n);   
                int k = m + n;   
                for (int l = m; l < k; l++) {   
                         char c0 = hexDigits[(bytes[l] & 0xf0) >> 4]; 
                        char c1 = hexDigits[bytes[l] & 0xf]; 
                        stringbuffer.append(c0);   
                        stringbuffer.append(c1);   
                }   
                return stringbuffer.toString();   
        } 
    
}
