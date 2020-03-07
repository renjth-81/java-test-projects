package com.ren.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamsTest {

	public static void main(String[] args) {
		Invoice inv1 = new Invoice(1, "15-30", 2000);
		Invoice inv2 = new Invoice(1, "30-45", 4000);
		Invoice inv3 = new Invoice(2, "30-45", 9000);
		Invoice inv4 = new Invoice(1, "30-45", 10000);

		List<Invoice> invs = new ArrayList<>();
		invs.add(inv1);
		invs.add(inv2);
		invs.add(inv3);
		invs.add(inv4);

		Map<Integer, List<Invoice>> customerInvs = invs.stream().collect(Collectors.groupingBy(inv -> inv.getId()));

		Map<Integer, Map<String, List<Invoice>>> customerAgeInvs = invs.stream().collect(
				Collectors.groupingBy(inv->inv.getId(), 
						Collectors.groupingBy(inv -> inv.getAge())
						)
				);
		System.out.println(customerInvs);
		System.out.println(customerAgeInvs);
	}

}
