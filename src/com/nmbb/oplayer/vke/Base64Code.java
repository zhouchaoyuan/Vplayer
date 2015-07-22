package com.nmbb.oplayer.vke;



public class Base64Code {
	
	private final static String table= 
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	
	// ~~~比特转二进制
	public static String ByteToBinary(byte ch){
		String res="";
		for(int i=7;i>=0;i--){
			res+=((ch>>i)&1);
		}
		return res;
	}
	
	
	// ~~~01串转64编码
	public static String BinaryToBase64(String src){
		int index=0;
		String res="";
		for(int i=0;i<src.length();i++){
			index=index*2+(src.charAt(i)-'0');
			if((i+1)%6==0){
				res+=table.charAt(index);
				index = 0;
			}
		}
		return res;
	}
	// ~~~加密
	public static String EnCode(byte bytes []){
		String res = "",temp = "";
		for(int i=0;i<bytes.length;i+=3){
			if(i+2<bytes.length){
				temp = ByteToBinary(bytes[i]);
				temp+=ByteToBinary(bytes[i+1]);
				temp+=ByteToBinary(bytes[i+2]);
				res+=BinaryToBase64(temp);
			}
			else if(i+1<bytes.length){
				temp = ByteToBinary(bytes[i]);
				temp+=ByteToBinary(bytes[i+1]);
				res+=BinaryToBase64(temp+"00")+"=";
			}
			else{
				temp = ByteToBinary(bytes[i]);
				res+=BinaryToBase64(temp+"0000")+"==";
			}
			
			
		}
		return res;
	}
	// ~~~获得比特串
	public static byte[] getBytes(String src){
		return src.getBytes();
	}
	
	// ~~~Base64字符串转ASCII字符串
	public static byte [] FromBase64String(String src){
		String bin="";
		for(int i=0;i<src.length();i++){
			int in=-1;
			for(int j=0;j<64;j++){
				if(src.charAt(i)==table.charAt(j)){
					in=j;
				}
			}
			if(in==-1)continue;
			for(int j=5;j>=0;j--){
				bin+=((in>>j)&1);
			}
		}
		byte num=0;
		byte bytes[] = new byte[bin.length()/8];
		for(int i=0;i<bin.length();i++){
			num = (byte) (num * 2+(bin.charAt(i)-'0'));
			if((i+1)%8==0){
				bytes[i/8]=num;
				num=0;
			}
		}
		return bytes;
	}
	// ~~~解码
	public static String DeCode(byte [] bytes){
		String res="";
		for(int i=0;i<bytes.length;i++){
			res+=(char)bytes[i];
		}
		return res;
	}

}
