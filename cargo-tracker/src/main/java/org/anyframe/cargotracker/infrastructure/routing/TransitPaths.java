package org.anyframe.cargotracker.infrastructure.routing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransitPaths implements Serializable {

	private static final long serialVersionUID = -2467364817762586824L;
	
	private List<TransitPath> transitPaths;

    public TransitPaths() {
        this.transitPaths = new ArrayList<>();
    }

    public TransitPaths(List<TransitPath> transitPaths) {
		this.transitPaths = transitPaths;
	}

    public List<TransitPath> getTransitPaths() {
        return transitPaths;
    }

    public void setTransitPaths(List<TransitPath> transitPaths) {
        this.transitPaths = transitPaths;
    }

    @Override
    public String toString() {
        return "TransitPaths{" + "transitPaths=" + transitPaths + '}';
    }
}
