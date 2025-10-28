package jp.co.sss.lms.ct.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {

	private static Properties prop = new Properties();

	static {
		try (InputStream in = ConfigUtils.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (in == null) {
				throw new IllegalStateException("config.properties が見つかりません");
			}
			try (java.io.Reader reader = new java.io.InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8)) {
				prop.load(reader);
			}
		} catch (Exception e) {
			throw new RuntimeException("config.properties の読み込みに失敗しました", e);
		}
	}

	/**
	 * 正常ログイン用ユーザーのIDを取得
	 * @return 正常ログイン用のユーザーID
	 */
	public static String getSuccessLoginId() {
		return prop.getProperty("successLogin.id");
	}

	/**
	 * 正常ログイン用ユーザーのパスワードを取得
	 * @return 正常ログイン用のパスワード
	 */
	public static String getSuccessPassword() {
		return prop.getProperty("successLogin.password");
	}

	/**
	 * ログイン失敗テスト用ユーザーのIDを取得
	 * @return ログイン失敗テスト用のユーザーID
	 */
	public static String getErrorLoginId() {
		return prop.getProperty("errorLogin.id");
	}

	/**
	 * ログイン失敗テスト用ユーザーのパスワードを取得
	 * @return ログイン失敗テスト用のパスワード
	 */
	public static String getErrorPassword() {
		return prop.getProperty("errorLogin.password");
	}

	/**
	 * FAQ画面で使用する検索キーワードを取得
	 * @return FAQ検索キーワード
	 */
	public static String getFaqSearchKeyword() {
		return prop.getProperty("faqSearchKeyword");
	}

	/**
	 * FAQカテゴリIDを取得
	 * @return FAQカテゴリID
	 */
	public static String getFaqCategoryId() {
		return prop.getProperty("faqCategoryId");
	}
}
