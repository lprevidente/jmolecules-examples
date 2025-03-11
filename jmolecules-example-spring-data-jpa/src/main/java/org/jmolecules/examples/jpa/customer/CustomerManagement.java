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
package org.jmolecules.examples.jpa.customer;

import org.jmolecules.ddd.annotation.Service;
import org.jmolecules.event.annotation.DomainEventHandler;
import org.jmolecules.event.types.DomainEvent;

/**
 * @author Oliver Drotbohm
 */

@Service
public class CustomerManagement {

	public boolean eventReceived = false;

	@DomainEventHandler
	void on(SampleEvent event) {
		this.eventReceived = true;
	}

	public static class SampleEvent implements DomainEvent {};
}
