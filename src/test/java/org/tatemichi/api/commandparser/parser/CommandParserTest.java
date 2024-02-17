package org.tatemichi.api.commandparser.parser;

import java.util.Arrays;

import org.junit.Test;
import org.tatemichi.api.commandparser.exception.InvalidCommandParameterException;
import org.tatemichi.api.commandparser.parammarker.Parameter;

public class CommandParserTest {

	@Test
	public void parseTest() {

		SafeParameter spc = new SafeParameter();
		try {
			String[] args = {
					"-singleparam","singlevalue",
					"-novalue",
					"-m","value01","value02","value03",
					"-singleparam2","singlevalue2"};
			spc.parse(args);
			System.out.println(spc);

		} catch (IllegalArgumentException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (InvalidCommandParameterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private class SafeParameter extends CommandParameter {

		@Override
		public String toString() {
			return "SafeParameter [singleparam=" + singleparam + ", multiparams=" + Arrays.toString(multiparams)
					+ ", novalue=" + novalue + "]";
		}

		@Parameter(parameterName = "-singleparam",aliasName = "-s")
		String singleparam;

		@Parameter(parameterName = "-multiparams", aliasName = {"-m","--multi-parameters"})
		String[] multiparams;

		@Parameter(parameterName = "-novalue", aliasName = "-n")
		boolean novalue;

		@Parameter(parameterName = "-singleparam2",aliasName = "-s2")
		String singleparam2;

	}
}
