package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;

import jp.co.sss.lms.ct.util.ConfigUtils;
import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() throws Exception {
		// ログイン画面を開く
		goTo("http://localhost:8080/lms/");

		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		String title = WebDriverUtils.webDriver.getTitle();

		// 成功判定
		assertTrue(currentUrl.contains("/lms/") && title.contains("ログイン"),
				"トップページ画面への遷移に失敗しました");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() throws Exception {
		// ConfigUtilsからIDとパスワードを取得
		String loginId = ConfigUtils.getErrorLoginId();
		String password = ConfigUtils.getErrorPassword();
		WebDriverUtils.webDriver.findElement(By.id("loginId")).sendKeys(loginId);
		WebDriverUtils.webDriver.findElement(By.id("password")).sendKeys(password);

		getEvidence("Case02/01.Login_Before");

		// ログインボタンをクリック
		WebDriverUtils.webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']")).click();
		Thread.sleep(2000);

		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		String title = WebDriverUtils.webDriver.getTitle();

		getEvidence("Case02/2.Login_Error");

		// 成功判定
		assertTrue(currentUrl.contains("/lms/") && title.contains("ログイン"),
				"ログイン失敗後の画面がトップページになっていません");
	}

}
