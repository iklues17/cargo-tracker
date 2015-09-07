package org.anyframe.cargotracker.infrastructure.events.spring;

import org.anyframe.cargotracker.domain.model.cargo.Cargo;
import org.springframework.context.ApplicationEvent;

public class CargoEvent extends ApplicationEvent {

	private static final long serialVersionUID = 4174514438173543541L;
	
	private Cargo cargo;
	
	public CargoEvent(Object source, Cargo cargo) {
		super(source);
		this.cargo = cargo;
	}
	
	public Cargo getCargo() {
		return cargo;
	}

}
