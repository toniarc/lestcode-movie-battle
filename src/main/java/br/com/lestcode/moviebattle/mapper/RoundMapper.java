package br.com.lestcode.moviebattle.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.lestcode.moviebattle.dto.RoundDto;
import br.com.lestcode.moviebattle.model.Round;

@Mapper
public interface RoundMapper {

	RoundMapper INSTANCE = Mappers.getMapper(RoundMapper.class);
	
	RoundDto map(Round round);
}
