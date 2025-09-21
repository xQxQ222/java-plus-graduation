package ru.yandex.practicum.mapper.compilation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.dto.compilation.CompilationDto;
import ru.yandex.practicum.dto.compilation.NewCompilationDto;
import ru.yandex.practicum.dto.compilation.UpdateCompilationRequest;
import ru.yandex.practicum.model.compilation.Compilation;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapperCompilation {
    @Mapping(source = "events", target = "events", ignore = true)
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    @Mapping(source = "events", target = "events", ignore = true)
    Compilation toCompilation(UpdateCompilationRequest updateCompilationRequest);

    CompilationDto toCompilationDto(Compilation compilation);
}
