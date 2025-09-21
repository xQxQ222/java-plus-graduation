package ru.yandex.practicum.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.location.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
