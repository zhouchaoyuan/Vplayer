package com.nmbb.oplayer.vke;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Algorithm {
	
	private String url=null;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static String getSuffix(String url){
		String suffix="";
		String strRegex = "(?<=id_)(\\w+)";
		Pattern pat = Pattern.compile(strRegex);
		Matcher mat = pat.matcher(url);
		//System.out.println(mat.matches());
		mat.find();
		for(int i=1;i<=mat.groupCount();i++){
			suffix+=mat.group(i).toString();
			//System.out.println(mat.group(i));
		}
		return suffix;
	}

	@SuppressLint("UseValueOf")
	private static String myEncoder(String a,byte[]c,boolean isToBase64){
		String result = "";
		ArrayList<Byte> bytesR = new ArrayList<Byte>();
		//List<byte> bytesR = new List<byte>();
        int f = 0, h = 0, q = 0;
        int[] b = new int[256];
        for (int i = 0; i < 256; i++)
                b[i] = i;
        while (h < 256)
        {
            f = (f + b[h] + a.charAt(h % a.length())) % 256;
            int temp = b[h];
            b[h] = b[f];
            b[f] = temp;
            h++;
        }
        f = 0; h = 0; q = 0;
        while (q < c.length)
        {
            h = (h + 1) % 256;
            f = (f + b[h]) % 256;
            int temp = b[h];
            b[h] = b[f];
            b[f] = temp;
            byte[] bytes = new byte[] { (byte)(c[q] ^ b[(b[h] + b[f]) % 256]) };
            bytesR.add(new Byte(bytes[0]));
            result += Base64Code.DeCode(bytes);
            //result += System.Text.ASCIIEncoding.ASCII.GetString(bytes);
            q++;
        }
        if (isToBase64)
        {
        	Byte [] byteR = new Byte[bytesR.size()];
    		for(int i=0;i<bytesR.size();i++){
    			byteR[i]=bytesR.get(i).byteValue();
    		}
    		
            byte[] bytes = new byte[byteR.length];
            for(int i=0;i<byteR.length;i++){
            	bytes[i]=byteR[i].byteValue();
            }
            result = Base64Code.EnCode(bytes);
            //result = Convert.ToBase64String(byteR);
        }
		return result;
	}
	
	public static String pNew ,token,sid;
	public static void getEp(String vid, String ep)
    {
        String template1 = "becaf9be";
        String template2 = "bf7e5f01";
        //System.out.println(ep.length());
        byte[] bytes = Base64Code.FromBase64String(ep);
        ep = Base64Code.DeCode(bytes);
        String temp = myEncoder(template1, bytes, false);
        String[] part = temp.split("_");
        sid = part[0];
        token = part[1];
        String whole = sid+"_"+vid+"_"+token;
        byte[] newbytes = Base64Code.getBytes(whole);
        pNew = myEncoder(template2, newbytes, true);
    }
}
