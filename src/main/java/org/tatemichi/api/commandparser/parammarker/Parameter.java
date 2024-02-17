package org.tatemichi.api.commandparser.parammarker;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * コマンドパラメーターで指定された値を格納するフィールドに設定するアノテーションです。
 * <p>
 * 本アノテーションを設定できるフィールドの型は以下です。
 * <ul>
 * <li>String 単一の値を指定するパラメーターの値格納
 * <li>String[] 複数の値を指定できるパラメーターの値格納
 * <li>boolean 値を持たないパラメーターの指定有無格納 (指定あり: true , 指定なし: false)
 * </ul>

 *
 * アノテーションには次のパラメーターを設定できます。
 * <ul>
 * <li>parameterName パラメーター名
 * <li>aliasName エイリアス名 複数指定可能
 * </ul>
 * 定義例:
 * <pre>
 * {@literal @}Parameter(parameterName = "-parameter" , aliasName = {"-param", "-p"})
 * private String parameter:
 * </pre>
 *
 * @see org.tatemichi.api.commandparser.parser.CommandParameter
 *
 * @author sf0273
 *
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface Parameter {

	public static enum ParameterType {
		NOVALUE,
		SINGLE,
		MULTI
	}

	/** パラメーター名 */
	String parameterName();

	/** 別名 */
	String[] aliasName() default {};

	/** 値指定要否 */
	boolean hasValue() default false;

	/** 値タイプ（単独 or 複数） */
	ParameterType parameterTtype() default ParameterType.SINGLE;

}
