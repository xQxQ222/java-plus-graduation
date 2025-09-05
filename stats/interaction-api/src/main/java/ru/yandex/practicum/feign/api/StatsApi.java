package ru.yandex.practicum.feign.api;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsApi {

    @GetMapping("/stats")
    ResponseEntity<List<ViewStatsDto>> getStats(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                @RequestParam(name = "uris", required = false) List<String> uris,
                                                @RequestParam(name = "unique", defaultValue = "false") Boolean unique);

    @PostMapping("/hit")
    ResponseEntity<String> hitStat(@Valid @RequestBody EndpointHitDto hitDto);
}
