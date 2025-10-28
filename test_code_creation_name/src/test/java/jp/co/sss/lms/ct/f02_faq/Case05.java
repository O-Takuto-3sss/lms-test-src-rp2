package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

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
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

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
	void test01() {
		// ログイン画面を開く
		goTo("http://localhost:8080/lms/");

		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		String title = WebDriverUtils.webDriver.getTitle();

		// 成功判定
		assertTrue(currentUrl.contains("/lms/") && title.contains("ログイン"),
				"ログイン画面への遷移に失敗しました");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() throws Exception {
		// ConfigUtilsからIDとパスワードを取得
		String loginId = ConfigUtils.getSuccessLoginId();
		String password = ConfigUtils.getSuccessPassword();
		WebDriverUtils.webDriver.findElement(By.id("loginId")).sendKeys(loginId);
		WebDriverUtils.webDriver.findElement(By.id("password")).sendKeys(password);

		getEvidence("Case05/02-1.Login_Before");

		// ログインボタンをクリック
		WebDriverUtils.webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']")).click();
		Thread.sleep(2000);
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		String title = WebDriverUtils.webDriver.getTitle();

		getEvidence("Case05/02-2.Login_Success");

		// 成功判定
		assertTrue(currentUrl.contains("/course/detail") && title.contains("コース詳細"),
				"コース詳細画面への遷移に失敗しました");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() throws Exception {
		// 「機能」ドロップダウンをクリックして展開
		WebDriverUtils.webDriver.findElement(By.linkText("機能")).click();
		Thread.sleep(1000);

		// 「ヘルプ」リンクをクリック
		WebDriverUtils.webDriver.findElement(By.linkText("ヘルプ")).click();
		Thread.sleep(2000);
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		String title = WebDriverUtils.webDriver.getTitle();

		getEvidence("Case05/03-1.Help");

		// 成功判定
		assertTrue(currentUrl.contains("/help") && title.contains("ヘルプ"),
				"ヘルプ画面への遷移に失敗しました");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() throws Exception {
		// 現在のウィンドウハンドルを取得
		String originalWindow = WebDriverUtils.webDriver.getWindowHandle();

		// 「よくある質問」リンクをクリック）
		WebDriverUtils.webDriver.findElement(By.linkText("よくある質問")).click();
		Thread.sleep(2000);

		// すべてのウィンドウハンドルを取得
		Set<String> windowHandles = WebDriverUtils.webDriver.getWindowHandles();

		// 新しく開いたタブへ切り替え
		for (String handle : windowHandles) {
			if (!handle.equals(originalWindow)) {
				WebDriverUtils.webDriver.switchTo().window(handle);
				break;
			}
		}

		// ページ遷移待機
		Thread.sleep(2000);
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		String title = WebDriverUtils.webDriver.getTitle();

		getEvidence("Case05/04-1.Faq");

		// 成功判定
		assertTrue(currentUrl.contains("/faq") && title.contains("よくある質問"),
				"よくある質問画面への遷移に失敗しました");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() throws Exception {
		// ConfigUtilsから検索キーワードを取得
		String keyword = ConfigUtils.getFaqSearchKeyword();
		WebDriverUtils.webDriver.findElement(By.id("form")).sendKeys(keyword);

		// 検索ボタンをクリック
		WebDriverUtils.webDriver.findElement(By.cssSelector("input[type='submit'][value='検索']")).click();
		String bodyText = WebDriverUtils.webDriver.findElement(By.tagName("tbody")).getText();
		Thread.sleep(2000);

		getEvidence("Case05/05-1.KeywordSearch_Result");

		// 成功判定
		assertTrue(bodyText.contains(keyword), "検索結果に該当キーワードが含まれていません");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() throws Exception {
		// 「クリア」ボタンをクリック
		WebDriverUtils.webDriver.findElement(By.cssSelector("input[type='button'][value='クリア']")).click();
		Thread.sleep(2000);
		String valueAfterClear = WebDriverUtils.webDriver.findElement(By.id("form")).getAttribute("value");

		getEvidence("Case05/06-1.Keyword_Clear");

		// 成功判定
		assertEquals("", valueAfterClear, "クリアボタン押下後も検索ボックスに文字が残っています");
	}
}
