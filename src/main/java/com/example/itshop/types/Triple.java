package com.example.itshop.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Triple<First, Second, Third> {
	private First first;
	private Second second;
	private Third third;
	
	public static <F, S, T> Triple<F, S, T> of(F first, S second, T third) {
		return new Triple<>(first, second, third);
	}
}
