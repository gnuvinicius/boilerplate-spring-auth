package br.com.garage.commons.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaginationResult<T> {

    private int firstItem;
	private int lastItem;
	private int lastPage;
	private int page;
	private int perPage;
	private int size;
	private List<T> result = new ArrayList<>();
}
