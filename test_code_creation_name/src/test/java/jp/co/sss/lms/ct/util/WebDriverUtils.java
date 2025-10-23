package jp.co.sss.lms.ct.util;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

/**
 * Webドライバーユーティリティ
 * @author holy
 */
public class WebDriverUtils {

	/** Webドライバ */
	public static WebDriver webDriver;

	/**
	 * インスタンス取得
	 * @return Webドライバ
	 */
	public static void createDriver() {
		System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");

		ChromeOptions options = new ChromeOptions();
		options.addArguments("force-device-scale-factor=0.9"); // 表示90％

		webDriver = new ChromeDriver(options);
	}

	/**
	 * インスタンス終了
	 */
	public static void closeDriver() {
		webDriver.quit();
	}

	/**
	 * 画面遷移
	 * @param url
	 */
	public static void goTo(String url) {
		webDriver.get(url);
		pageLoadTimeout(5);
	}

	/**
	 * ページロードタイムアウト設定
	 * @param second
	 */
	public static void pageLoadTimeout(int second) {
		webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(second));
	}

	/**
	 * 要素の可視性タイムアウト設定
	 * @param locater
	 * @param second
	 */
	public static void visibilityTimeout(By locater, int second) {
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(second));
		wait.until(ExpectedConditions.visibilityOfElementLocated(locater));
	}

	/**
	 * 指定ピクセル分だけスクロール
	 * @param pixel
	 */
	public static void scrollBy(String pixel) {
		((JavascriptExecutor) webDriver).executeScript("window.scrollBy(0," + pixel + ");");
	}

	/**
	 * 指定位置までスクロール
	 * @param pixel
	 */
	public static void scrollTo(String pixel) {
		((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0," + pixel + ");");
	}

	/**
	 * エビデンス取得
	 * @param relativePath フォルダ＋ファイル名
	 */
	public static void getEvidence(String relativePath) {
		File tempFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		try {
			String baseDir = "evidence"; // 基準フォルダ
			File file = new File(baseDir, relativePath + ".png");

			// フォルダがなければ作成
			File parentDir = file.getParentFile();
			if (!parentDir.exists()) {
				parentDir.mkdirs();
			}

			// スクリーンショットを移動
			Files.move(tempFile, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * エビデンス取得（サフィックスあり）
	 * @param instance
	 * @param suffix
	 */
	//	public static void getEvidence(Object instance, String suffix) {
	//		File tempFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
	//		try {
	//			String className = instance.getClass().getEnclosingClass().getSimpleName();
	//			String methodName = instance.getClass().getEnclosingMethod().getName();
	//			String dirPath = "test_code_creation_onuma/evidence"; // 相対パス
	//			File dir = new File(dirPath);
	//			Files.move(tempFile, new File(dir, className + "_" + methodName + "_" + suffix + ".png"));
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}
}
