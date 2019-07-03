package com.cqz.code;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

//判断12306验证码图片是8图还是18图
public class JudgePicType {
	private static final Logger logger = Logger.getLogger(JudgePicType.class);
	/**
	 * 取得图像上指定位置像素的 rgb 颜色分量。
	 * @param image 源图像。
	 * @param x 图像上指定像素位置的 x 坐标。
	 * @param y 图像上指定像素位置的 y 坐标。
	 * @return 返回包含 rgb 颜色分量值的数组。元素 index 由小到大分别对应 r，g，b。
	 */
	public static int[] getRGB(BufferedImage image, int x, int y) {
		int[] rgb = new int[3];
		int pixel = image.getRGB(x, y);
		rgb[0] = (pixel & 0xff0000) >> 16;
		rgb[1] = (pixel & 0xff00) >> 8;
		rgb[2] = (pixel & 0xff);

		return rgb;
	}

    //判断12306验证码图片是8图还是18图:根据每个像素点的rgb判断
	public static String checkImage(String path) {
		File picture = new File(path);
		BufferedImage sourceImg = null;
		try {
			sourceImg = ImageIO.read(new FileInputStream(picture));
		} catch (IOException e) {
			logger.info(path+"图片IO异常"+e);
			e.printStackTrace();
		}
		int width = sourceImg.getWidth();
		int height = sourceImg.getHeight();
		int countX = 0;
		int countY = 0;
		Map<String, String> map = new HashMap<String, String>();
		//遍历每个像素点对应的rgb值
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int[] rgb = getRGB(sourceImg, x, y);
				map.put(x+"-"+y, Arrays.toString(rgb));
			}
		}
		
		for (int x = 0; x < width; x++) {
			//横坐标的中间线位值（竖着的中间线）
			if (x == 146 || x == 147 || x == 148) {
				for (int y = 0; y < height; y++) {
					String rgb = map.get(x+"-"+y);//[247, 247, 247]
					rgb = rgb.substring(1, rgb.length()-1).replaceAll(" ", "");
					String[] arr = rgb.split(",");
					//白色线
					if ((Integer.parseInt(arr[0]) >= 240) && (Integer.parseInt(arr[1]) >= 240) 
							&& (Integer.parseInt(arr[2]) >= 240)) {
						countY++;
					}
				}
			}
		}
		
		for (int yy = 0; yy < height; yy++) {
			if (yy == 110 || yy == 111) {//8图的纵坐标中间位置（横着的中间线）
				for (int xx = 0; xx < width; xx++) {
					String rgb = map.get(xx+"-"+yy);//[247, 247, 247]
					rgb = rgb.substring(1, rgb.length()-1).replaceAll(" ", "");
					String[] arr = rgb.split(",");
					//白色线
					if (Integer.parseInt(arr[0]) >= 240 && Integer.parseInt(arr[1]) >= 240 
							&& Integer.parseInt(arr[2]) >= 240) {
						countX++;
					}
				}
			}
		}
//		System.out.println("countX="+countX+",width="+width+",countY="+countY+",height="+height);
		if ((countX / 2) > (width / 2) && (countY / 3) > (height / 2)) {
			return "8";//8图
		} else {
			return "18";//18图
		}
	}

	
	//8图验证码1-8对应的坐标转换
	public static String getCode(String code){
		Random r=new Random();
		int a;
		int b;
		int length=code.length();
		StringBuffer sb=new StringBuffer();
//1		35,45,
//2		110,45,
//3		187,42,
//4		255,39,
//5		32,115,
//6		116,114,
//7		181,116,
//8		253,109
		for(int i=0;i<length;i++){
			char c=code.charAt(i);
			if((c+"").equals("1")){
				a=35-r.nextInt(5);
				b=45-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if((c+"").equals("2")){
				a=111-r.nextInt(5);
				b=45-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if((c+"").equals("3")){
				a=187-r.nextInt(5);
				b=45-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if((c+"").equals("4")){
				a=256-r.nextInt(5);
				b=44-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if((c+"").equals("5")){
				a=40-r.nextInt(5);
				b=119-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if((c+"").equals("6")){
				a=109-r.nextInt(5);
				b=117-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if((c+"").equals("7")){
				a=186-r.nextInt(5);
				b=119-r.nextInt(5);
				sb.append(a+","+b+",");
			}else{
				a=258-r.nextInt(5);
				b=118-r.nextInt(5);
				sb.append(a+","+b+",");
			}
		}
		String returnStr=sb.toString();
		return returnStr.substring(0, returnStr.lastIndexOf(","));	
	}
	
	//18图的1-18，中间用,隔开的坐标值转换
	public static String get18Code(String code){
		Random r=new Random();
		int a;
		int b;
		String[] arr=code.trim().split(",");
		StringBuffer sb=new StringBuffer();

		for(int i=0;i<arr.length;i++){
			String c=arr[i];
			if("1".equals(c)){		//1		30,35,
				a=30-r.nextInt(5);
				b=35-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("2".equals(c)){//2		75,35,
				a=75-r.nextInt(5);
				b=35-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("3".equals(c)){//3		126,35
				a=126-r.nextInt(5);
				b=35-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("4".equals(c)){//4		170,35,
				a=170-r.nextInt(5);
				b=35-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("5".equals(c)){//5		215,35,
				a=215-r.nextInt(5);
				b=35-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("6".equals(c)){//6		263,35,
				a=263-r.nextInt(5);
				b=35-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("7".equals(c)){//7		30,80,
				a=30-r.nextInt(5);
				b=80-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("8".equals(c)){//8		75,80
				a=75-r.nextInt(5);
				b=80-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("9".equals(c)){//9		126,80
				a=126-r.nextInt(5);
				b=80-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("10".equals(c)){//10	170,80
				a=170-r.nextInt(5);
				b=80-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("11".equals(c)){//11	215,80
				a=215-r.nextInt(5);
				b=80-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("12".equals(c)){//12	263,80
				a=263-r.nextInt(5);
				b=80-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("13".equals(c)){//13	30,128
				a=30-r.nextInt(5);
				b=128-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("14".equals(c)){//14	75,128
				a=75-r.nextInt(5);
				b=128-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("15".equals(c)){//15	126,128
				a=126-r.nextInt(5);
				b=128-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("16".equals(c)){//16	170,128
				a=170-r.nextInt(5);
				b=128-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("17".equals(c)){//17	215,128
				a=215-r.nextInt(5);
				b=128-r.nextInt(5);
				sb.append(a+","+b+",");
			}else if("18".equals(c)){//18	263,128
				a=263-r.nextInt(5);
				b=128-r.nextInt(5);
				sb.append(a+","+b+",");
			}
		}
		String returnStr=sb.toString();
		return returnStr.substring(0, returnStr.lastIndexOf(","));	
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		File f = new File("d:/coder");
		int x = 0;
		for (File target : f.listFiles()) {
			String result = checkImage(target.getAbsolutePath());
			System.out.println(target.getName()+"-----"+result);
			if("18".equals(result)){
				x++;
//				System.out.println(x);
			}
		}
		System.out.println(x);
		
		
		
//		System.out.println(checkImage("d:/code/PIC1512040912251012_18.GIF"));
	}

}
