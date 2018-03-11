package com.l9e.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.web.multipart.MultipartFile;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;
/**
 * 支付宝导入专用解压 对特定文件进行了筛选解压
 * @author kongxm
 *
 */
public class RarUtils {
	
	/**
     * 使用GBK编码可以避免压缩中文文件名乱码
     */
    private static final String CHINESE_CHARSET = "GBK";
    
    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;
	
	/**
	 * 解压zip格式压缩包 对应的是ant.jar
	 */
	
	public static void unzip(String sourceFile, String destDir,List<String> fileName)
			throws Exception {
			ZipFile zipFile = new ZipFile(sourceFile, CHINESE_CHARSET);
	        Enumeration<?> emu = zipFile.getEntries();
	        BufferedInputStream bis;
	        FileOutputStream fos;
	        BufferedOutputStream bos;
	        File file, parentFile;
	        ZipEntry entry;
	        byte[] cache = new byte[CACHE_SIZE];
	        while (emu.hasMoreElements()) {
	            entry = (ZipEntry) emu.nextElement();
	            if (entry.isDirectory()) {
	                new File(destDir + entry.getName()).mkdirs();
	                continue;
	            }
	            //只解压特定名称的文件
	            if(!entry.getName().toString().contains("汇总")){
	            	//将文件名存放到list当中
		            fileName.add(entry.getName());
		            bis = new BufferedInputStream(zipFile.getInputStream(entry));
		            file = new File(destDir + entry.getName());
		            parentFile = file.getParentFile();
		            if (parentFile != null && (!parentFile.exists())) {
		                parentFile.mkdirs();
		            }
		            fos = new FileOutputStream(file);
		            bos = new BufferedOutputStream(fos, CACHE_SIZE);
		            int nRead = 0;
		            while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
		                fos.write(cache, 0, nRead);
		            }
		            
		            bos.flush();
		            bos.close();
		            fos.close();
		            bis.close();
		        }
	        }
	        zipFile.close();
	    }

	/**
	 * 解压rar格式压缩包。
	 * 对应的是java-unrar-0.3.jar，但是java-unrar-0.3.jar又会用到commons-logging-1.1.1.jar
	 */
	public static void unrar(String sourceFile, String destDir,List<String> fileName)
			throws Exception {
		Archive a = null;
		FileOutputStream fos = null;
		String compressFileName="";
		String destFileName = "";
		String destDirName = "";
		try {
				a = new Archive(new File(sourceFile));
				FileHeader fh = a.nextFileHeader();
				while (fh != null) {
					if (!fh.isDirectory()) {
						// 1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
						if(fh.isUnicode()){
							//压缩包内含中文文件
							compressFileName=fh.getFileNameW().trim();
						}else{
							//压缩包内不含中文文件
							compressFileName = fh.getFileNameString().trim();
						}
						destFileName = destDir	+ compressFileName;
						destDirName = destFileName.substring(0,destFileName.lastIndexOf("/"));
						// 2、只解压特定名称的文件
						if(!compressFileName.toString().contains("汇总")){
							fileName.add(compressFileName);
							File dir = new File(destDirName);
							if (!dir.exists() || !dir.isDirectory()) {
								dir.mkdirs();
							}
							// 3解压缩文件
							fos = new FileOutputStream(new File(destFileName));
							a.extractFile(fh, fos);
							fos.close();
							fos = null;
						}
					}
					fh = a.nextFileHeader();
				}
				a.close();
				a = null;													
		} catch (Exception e) {
			throw e;
		} finally {
			if (fos != null) {
				try {
					fos.close();
					fos = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (a != null) {
				try {
					a.close();
					a = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 解压缩
	 */
	public static void deCompress(String sourceFile, String destDir,List<String> fileName)
			throws Exception {
			// 保证文件夹路径最后是"/"或者"\"
			char lastChar = destDir.charAt(destDir.length() - 1);
			if (lastChar != '/' && lastChar != '\\') {
				destDir += File.separator;
			}
			// 根据类型，进行相应的解压缩
			String type = sourceFile.substring(sourceFile.lastIndexOf(".") + 1);
			if (type.equals("zip")) {
				RarUtils.unzip(sourceFile, destDir,fileName);
			} else if (type.equals("rar")) {
				RarUtils.unrar(sourceFile, destDir,fileName);
			} else {
				throw new Exception("只支持zip和rar格式的压缩包！");
		}
	}
}