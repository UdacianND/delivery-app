package uz.md.shopapp.mapper;

import java.util.List;

public interface EntityMapper<E, D> {

    D toDTO(E entity);

    List<D> toDTOList(List<E> entities);

    E fromDTO(D dto);

    List<E> fromDTOList(List<D> dtoList);
}
