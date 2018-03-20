package com.performance.example;

import java.util.Random;

public class Currency {
public int getLimit() {
	Random rd = new Random();
	int limit = rd.nextInt(10);
	return limit;
}

}

