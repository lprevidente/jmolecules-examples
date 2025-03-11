/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmolecules.examples.jpa;

import static org.assertj.core.api.Assertions.*;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.jmolecules.examples.jpa.customer.Address;
import org.jmolecules.examples.jpa.customer.Customer;
import org.jmolecules.examples.jpa.customer.CustomerManagement;
import org.jmolecules.examples.jpa.customer.Customers;
import org.jmolecules.examples.jpa.order.Order;
import org.jmolecules.examples.jpa.order.Orders;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oliver Drotbohm
 */
@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
class ApplicationIntegrationTests {

	private final ConfigurableApplicationContext context;
	private final EntityManager em;

	@Test
	void bootstrapsContainer() {

		assertThat(AssertableApplicationContext.get(() -> context)) //
				.hasSingleBean(CustomerManagement.class)
				.satisfies(ctx -> {

					ctx.publishEvent(new CustomerManagement.SampleEvent());

					CustomerManagement bean = ctx.getBean(CustomerManagement.class);

					assertThat(bean.eventReceived).isTrue();
				});
	}

	@Test // #24
	@Transactional
	void exposesPersistenceComponents() {

		var address = new Address("41 Greystreet", "Dreaming Tree", "2731");

		var customers = context.getBean(Customers.class);
		var customer = customers.save(new Customer("Dave", "Matthews", address));

		var orders = context.getBean(Orders.class);
		var order = orders.save(new Order(customer));

		em.flush();
		em.clear();

		var resolved = customers.resolveRequired(order.getCustomer());

		assertThat(resolved.getName().firstname()).isEqualTo("Dave");
		assertThat(resolved.getName().lastname()).isEqualTo("Matthews");
	}
}
