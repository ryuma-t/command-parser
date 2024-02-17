package org.tatemichi.api.commandparser.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.tatemichi.api.commandparser.exception.ErrorCode;
import org.tatemichi.api.commandparser.exception.InvalidCommandParameterException;
import org.tatemichi.api.commandparser.parammarker.Parameter;
import org.tatemichi.api.commandparser.parammarker.Parameter.ParameterType;

public abstract class CommandParameter {

	private ArrayList<ParameterInfo> infos = new ArrayList<ParameterInfo>();

	/**
	 *
	 * 指定されたコマンドラインパラメーターを解析する。
	 * この抽象クラスを継承したクラスの紐づくメンバ変数に値を設定する。
	 *
	 * @param args
	 * @throws InvalidCommandParameterException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public final void parse(String[] args)
			throws InvalidCommandParameterException {

		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Parameter.class)) {
				ParameterInfo parameterInfo = ParameterInfo.createInstance(field);
				for (ParameterInfo info : infos) {
					if (info.equalsParameterInfo(parameterInfo)) {
						throw new InvalidCommandParameterException(
								ErrorCode.DUPLICATE, field.getName());
					}
				}
				field.setAccessible(true);
				infos.add(parameterInfo);
			}
		}

		try {
			for (int i=0; i < args.length; i++) {
				String argument = args[i];
				ParameterInfo info = getByParaemeterName(argument);
				if (info == null) {
					throw new InvalidCommandParameterException();
				}

				switch (info.type) {

				case NOVALUE :
					// 値を持たない=boolean値なので true をセット
					info.field.setBoolean(this, true);
					break;
				case SINGLE :
					// シングルなので一つ後ろを値としてセット
					if (++i >= args.length) {
						// 後ろに必要な値が無ければ不正
						throw new InvalidCommandParameterException();
					}
					info.field.set(this, args[i]);
					break;
				case MULTI :
					// マルチなので、次のパラメーターが出現するまでの値をセット
					List<String> valueList = new ArrayList<String>();
					for (int j=i+1; i < args.length; j++) {
						if(getByParaemeterName(args[j]) == null) {
							valueList.add(args[j]);
						} else {
							break;
						}
					}
					if (valueList.isEmpty()) {
						throw new InvalidCommandParameterException();
					}
					String[] array = valueList.toArray(new String[valueList.size()]);
					info.field.set(this, array);
					i += valueList.size();
					break;
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException  e) {
			// TODO: handle exception
		}

	}

	/**
	 * パラメーター情報リストから、引数のパレメーター名を含むオブジェクトを返却する
	 * @param name
	 * @return
	 */
	private ParameterInfo getByParaemeterName(String name) {
		for (ParameterInfo info : infos) {
			if (info.parameterContains(name)) {
				return info;
			}
		}
		return null;
	}


	public static final class ParameterInfo {
		private final Field field;
		private final String name;
		private final String[] aliasNames;
		private final Parameter.ParameterType type;

		ParameterInfo(Field field, String name, String[] aliasNames, ParameterType type) {
			this.field = field;
			this.name = name;
			this.aliasNames = aliasNames;
			this.type = type;
		}

		public static ParameterInfo createInstance(
				Field field) throws InvalidCommandParameterException {

			ParameterType type;
			if (boolean.class.equals(field.getType())) {
				type = Parameter.ParameterType.NOVALUE;
			} else if (String.class.equals(field.getType())) {
				type = Parameter.ParameterType.SINGLE;
			} else if (field.getType().isArray()
					&& String.class.equals(field.getType().getComponentType())) {
				type = Parameter.ParameterType.MULTI;
			} else {
				throw new InvalidCommandParameterException(
						ErrorCode.INVALID_FIELD, field.getName());
			}
			return new ParameterInfo(
					field,
					field.getAnnotation(Parameter.class).parameterName(),
					field.getAnnotation(Parameter.class).aliasName(),
					type);
		}

		/**
		 * 指定されたパラメーたーが name あるいは alias のいずれかに一致したら ture
		 */
		public boolean parameterContains(String parameter) {

			if(parameter == null) {
				return false;
			}
			if (parameter.equals(name)) {
				return true;
			}
			for (String alias: aliasNames) {
				if (parameter.equals(alias)) {
					return true;
				}
			}
			return false;
		}

		public boolean equalsParameterInfo(ParameterInfo info) {
			if(parameterContains(info.name)) {
				return true;
			}
			for (String alias :info.aliasNames ) {
				if(parameterContains(alias)) {
					return true;
				}
			}
			return false;

		}

	}
}
