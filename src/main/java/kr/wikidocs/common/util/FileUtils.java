package kr.wikidocs.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 파일 유틸
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

	private final static Log log = LogFactory.getLog(FileUtils.class.getName());

	/**
	 * 확장자 반환
	 */
	public static String getFileExtension(File file) throws Exception {
		String fileName = file.getName();
		return FileUtils.getFileExtension(fileName);
	}
	public static String getFileExtension(String fileName) throws Exception {
		return fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
	}

	/**
	 * 확장자를 제외한 파일명만 반환
	 */
	public static String getOnlyFileName(File file) throws Exception {
		String fileName = file.getName();
		return FileUtils.getOnlyFileName(fileName);
	}
	public static String getOnlyFileName(String fileName) throws Exception {
		return fileName.substring(0, fileName.indexOf("."));
	}

	/**
	 * 디렉토리 생성
	 * - 읽기/실행 권한주면서 생성
	 */
	public static boolean mkdirs(String path) throws Exception {

		File dir = new File(path);
		if(dir.exists()) {
			return false;
		}
		if(dir.mkdir()) {
			dir.setReadable(true, false);
			dir.setExecutable(true, false);
			return true;
		}
		File canonFile = null;
		try {
			canonFile = dir.getCanonicalFile();
		} catch (IOException e) {
			return false;
		}

		File parent = canonFile.getParentFile();
		return (parent != null && (FileUtils.mkdirs(parent.getPath()) || parent.exists())
			&& canonFile.mkdir() && canonFile.setReadable(true, false) && canonFile.setExecutable(true, false));
	}

	/**
	 * 파일생성
	 *
	 * @param text
	 * @param targetPath
	 * @throws Exception
	 */
	public static File makeFile(String path, String text) throws Exception {

		File file = null;
		BufferedWriter writer = null;

		try {

			// 파일폴더가 없을경우 폴더 생성
			String fileDir = path.substring(0, path.lastIndexOf("/"));
			File saveDir = new File(fileDir);
			if (!saveDir.exists() || saveDir.isFile()) {
				FileUtils.mkdirs(fileDir);
			}

			file = new File(path);
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(text);

//			FileWriter fw = new FileWriter("/file/path");
//			fw.write(text);
//			fw.flush();
//			fw.close();

			// 파일체크
			if (file.isFile()) {
				file.setReadable(true, false);
			} else {
				throw new Exception("파일생성 실패..");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		return file;
	}

	/**
	 * 파일복사
	 *
	 * @param sourcePath
	 * @param targetPath
	 * @throws Exception
	 */
	public static void copyFile(String sourcePath, String targetPath) throws Exception {

		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {

			fis = new FileInputStream(sourcePath);
			fos = new FileOutputStream(targetPath);

			byte[] buffer = new byte[1024];
			int cnt = 0;
			while ((cnt = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, cnt);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			if (fis != null) {
				fis.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}

	/**
	 * 파일이동
	 *
	 * @param sourcePath
	 * @param targetPath
	 * @throws Exception
	 */
	public static void moveFile(String sourcePath, String targetPath) throws Exception {

		File inFile = new File(sourcePath);
		File outFile = new File(targetPath);

		try {
			FileUtils.moveFile(inFile, outFile);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			if (inFile != null) {
				if (inFile.isFile()) {
					inFile.delete();
				}
				if (inFile.isDirectory()) {
					FileUtils.deleteDirectory(inFile);
				}
			}
		}
	}

	/**
	 * 랜덤 파일명으로 변환
	 *
	 * @param sourceFile
	 * @return targetFile
	 * @throws Exception
	 */
	public static File renameRandomFile(File sourceFile) throws Exception {

		File targetFile = null;

		try {

			if(!sourceFile.isFile()) {
				return sourceFile;
			}

			//String fileName = sourceFile.getName();
			String fileDir  = sourceFile.getParentFile().getPath();
			String fileExt  = FileUtils.getFileExtension(sourceFile);

			String newFileName = ""; // 변경된 파일명
			String newFilePath = ""; // 변경된 파일경로

			String randomName = "";
			SecureRandom random = new SecureRandom();
			for(int i=0; i < 20; i++) {
				randomName += random.nextInt(9);
			}

			int saveIndex = 0;
			newFileName = randomName + "[" + Integer.toString(saveIndex) + "]";
			newFilePath = fileDir + File.separator + newFileName + "." + fileExt;

			// 파일이 있을경우 파일명을 계속 변경한다.
			File fileCheck = new File(newFilePath);
			while(fileCheck.isFile()) {
				saveIndex += 1;
				newFileName = randomName + "[" + Integer.toString(saveIndex) + "]";
				newFilePath = fileDir + File.separator + newFileName + "." + fileExt;
				fileCheck = new File(newFilePath);
			}

			// 파일명 변경저장
			targetFile = new File(newFilePath);
			boolean success = sourceFile.renameTo(targetFile);
			if(!success) {
				throw new Exception("랜덤 파일명으로 변환실패");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return targetFile;
	}

	/**
	 * 파일업로드 확장자 확인
	 *
	 * @param String
	 * @return boolean
	 */
	public static boolean fileExtException(String fileExt) {

		boolean rtn = true;
		String[] ExceptionFileExt = {
			"htm", "html", "jsp", "php", "php3", "php5", "phtml", "asp", "aspx", "ascx",
			"cfm", "cfc", "pl", "bat", "exe", "dll", "reg", "cgi", "java"
		};

		for (int i = 0; i < ExceptionFileExt.length; i++) {
			if (fileExt.indexOf(ExceptionFileExt[i]) != -1) {
				rtn = false;
				break;
			}
		}
		return rtn;
	}
}
