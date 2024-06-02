package br.com.garage.commons.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public enum EnumStatus {

	INATIVO(0), ATIVO(1);

	private final int value;

	EnumStatus(int value) {
		this.value = value;
	}

	public EnumStatus valueOf(int value) {
		Map<Integer, EnumStatus> map = new HashMap<>();

		for (EnumStatus status : EnumStatus.values()) {
			map.put(status.value, status);
		}

		return map.get(value);
	}

}
