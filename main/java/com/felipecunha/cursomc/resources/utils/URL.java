package com.felipecunha.cursomc.resources.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

//converte uma string da URI para inteiros
public class URL {
	
	//decodifica a URI para que não haja caracteres especiais
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
			}
		catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static List<Integer> decodeIntList(String s) {
		String[] vet = s.split(",");
		List<Integer> list = new ArrayList<>();
		for (int i=0; i<vet.length; i++) {
			list .add(Integer.parseInt(vet[i]));
		}
		return list;
		//pode também implementar com LAMBDA como no código abaixo, resolvendo em apenas uma linha
		//return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	}
}
