package org.cotrix.web.publish.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
	public static ArrayList<String[]> readFile(String filename, String regex) {
		ArrayList<String[]> data = new ArrayList<String[]>();
		try {
			File fileDir = new File(filename);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileDir), "UTF8"));
			String str;

			while ((str = in.readLine()) != null) {
				String[] token = str.split(regex);
				String[] d = new String[token.length];
				for (int i = 0; i < token.length; i++) {
					d[i] = token[i].replace("\"", "");
				}
				data.add(d);
			}
			in.close();
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return data;
	}

	public static FileInputStream readFile(String filename) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(filename);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Input Stream -->>"+is);
		return is;
	}

}
