package org.anyframe.booking.domain.location;

import java.util.List;

public interface LocationRepository {

    Location find(UnLocode unLocode);

    List<Location> findAll();
}
