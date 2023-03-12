package uz.md.shopapp.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.InstitutionType;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.institution_type.InstitutionTypeAddDTO;
import uz.md.shopapp.dtos.institution_type.InstitutionTypeDTO;
import uz.md.shopapp.dtos.institution_type.InstitutionTypeEditDTO;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.BadRequestException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.InstitutionTypeMapper;
import uz.md.shopapp.repository.InstitutionTypeRepository;
import uz.md.shopapp.service.contract.InstitutionTypeService;

import java.util.List;

import static uz.md.shopapp.utils.MessageConstants.*;

@Service
public class InstitutionTypeServiceImpl implements InstitutionTypeService {

    private final InstitutionTypeRepository institutionTypeRepository;
    private final InstitutionTypeMapper institutionTypeMapper;

    public InstitutionTypeServiceImpl(InstitutionTypeRepository institutionTypeRepository,
                                      InstitutionTypeMapper institutionTypeMapper) {
        this.institutionTypeRepository = institutionTypeRepository;
        this.institutionTypeMapper = institutionTypeMapper;
    }

    @Override
    public ApiResult<InstitutionTypeDTO> add(InstitutionTypeAddDTO dto) {

        if (dto == null
                || dto.getNameUz() == null
                || dto.getNameRu() == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();

        if (institutionTypeRepository
                .existsByNameUzOrNameRu(dto.getNameUz(), dto.getNameRu()))
            throw AlreadyExistsException.builder()
                    .messageUz("INSTITUTION_NAME_ALREADY_EXISTS")
                    .messageRu("")
                    .build();

        return ApiResult
                .successResponse(institutionTypeMapper
                        .toDTO(institutionTypeRepository
                                .save(institutionTypeMapper
                                        .fromAddDTO(dto))));
    }

    @Override
    public ApiResult<InstitutionTypeDTO> findById(Long id) {
        if (id == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();
        return ApiResult.successResponse(institutionTypeMapper
                .toDTO(institutionTypeRepository
                        .findById(id)
                        .orElseThrow(() -> NotFoundException.builder()
                                .messageUz(INSTITUTION_NOT_FOUND_UZ)
                                .messageRu("")
                                .build())));
    }

    @Override
    public ApiResult<InstitutionTypeDTO> edit(InstitutionTypeEditDTO editDTO) {

        if (editDTO == null || editDTO.getId() == null
                || editDTO.getNameUz() == null
                || editDTO.getNameRu() == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();

        InstitutionType editing = institutionTypeRepository
                .findById(editDTO.getId())
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("INSTITUTION_TYPE_NOT_FOUND")
                        .messageRu("")
                        .build());

        if (institutionTypeRepository.existsByNameUzOrNameRuAndIdIsNot(editDTO.getNameUz(), editDTO.getNameRu(), editing.getId()))
            throw AlreadyExistsException.builder()
                    .messageUz("INSTITUTION_NAME_ALREADY_EXISTS")
                    .messageRu("")
                    .build();

        InstitutionType institutionType = institutionTypeMapper.fromEditDTO(editDTO, editing);

        return ApiResult.successResponse(institutionTypeMapper
                .toDTO(institutionTypeRepository.save(institutionType)));
    }

    @Override
    public ApiResult<List<InstitutionTypeDTO>> getAll() {
        return ApiResult.successResponse(
                institutionTypeMapper.toDTOList(
                        institutionTypeRepository.findAll()
                )
        );
    }

    @Override
    public ApiResult<List<InstitutionTypeDTO>> getAllByPage(String page) {
        if (page == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();
        int[] paged = new int[]{Integer.parseInt(page.split("-")[0]),
                Integer.parseInt(page.split("-")[1])};
        return ApiResult.successResponse(
                institutionTypeMapper.toDTOList(institutionTypeRepository
                        .findAll(PageRequest.of(paged[0], paged[1]))
                        .getContent())
        );
    }

    @Override
    public ApiResult<Void> delete(Long id) {
        if (id == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();
        if (!institutionTypeRepository.existsById(id))
            throw NotFoundException.builder()
                    .messageUz(INSTITUTION_NOT_FOUND_UZ)
                    .messageRu("")
                    .build();

        institutionTypeRepository.deleteById(id);
        return ApiResult.successResponse();
    }
}