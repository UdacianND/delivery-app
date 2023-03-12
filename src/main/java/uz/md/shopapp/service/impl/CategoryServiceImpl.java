package uz.md.shopapp.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.domain.Institution;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.category.CategoryAddDTO;
import uz.md.shopapp.dtos.category.CategoryDTO;
import uz.md.shopapp.dtos.category.CategoryEditDTO;
import uz.md.shopapp.dtos.category.CategoryInfoDTO;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.BadRequestException;
import uz.md.shopapp.exceptions.NotAllowedException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.CategoryMapper;
import uz.md.shopapp.repository.CategoryRepository;
import uz.md.shopapp.repository.InstitutionRepository;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.contract.CategoryService;
import uz.md.shopapp.utils.CommonUtils;

import java.util.List;

import static uz.md.shopapp.utils.MessageConstants.ERROR_IN_REQUEST_RU;
import static uz.md.shopapp.utils.MessageConstants.ERROR_IN_REQUEST_UZ;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper,
                               InstitutionRepository institutionRepository,
                               UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.institutionRepository = institutionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ApiResult<CategoryDTO> add(CategoryAddDTO dto) {

        if (dto == null || dto.getNameUz() == null
                || dto.getNameRu() == null
                || dto.getInstitutionId() == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();

        User currentUser = getCurrentUser();

        Institution institution = institutionRepository
                .findById(dto.getInstitutionId())
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("Muassasa topilmadi")
                        .messageRu("Объект не найден")
                        .build());

        if (!currentUser.getRole().getName().equals("ADMIN"))
            if (!institution.getManager().getId().equals(currentUser.getId()))
                throw NotAllowedException.builder()
                        .messageUz("Sizda ruxsat yo'q")
                        .messageRu("У вас нет разрешения")
                        .build();

        if (categoryRepository.existsByNameUzOrNameRu(dto.getNameUz(), dto.getNameRu()) ||
                categoryRepository.existsByNameUzOrNameRu(dto.getNameUz(), dto.getNameRu()))
            throw AlreadyExistsException.builder()
                    .messageUz("Kategoriya nomi allaqachon mavjud")
                    .messageRu("Название категории уже существует")
                    .build();

        Category category = categoryMapper
                .fromAddDTO(dto);

        category.setInstitution(institution);

        return ApiResult
                .successResponse(categoryMapper
                        .toDTO(categoryRepository
                                .save(categoryRepository.save(category))));
    }

    @Override
    public ApiResult<CategoryDTO> findById(Long id) {

        if (id == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();

        return ApiResult.successResponse(categoryMapper
                .toDTO(categoryRepository
                        .findById(id)
                        .orElseThrow(() -> NotFoundException.builder()
                                .messageUz("Kategoriya topilmadi")
                                .messageRu("Категория не найдена")
                                .build())));
    }

    @Override
    public ApiResult<CategoryDTO> edit(CategoryEditDTO editDTO) {

        if (editDTO == null || editDTO.getNameRu() == null
                || editDTO.getNameUz() == null
                || editDTO.getInstitutionId() == null || editDTO.getId() == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();

        User currentUser = getCurrentUser();
        Institution institution = institutionRepository
                .findById(editDTO.getInstitutionId())
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("Muassasa topilmadi")
                        .messageRu("Объект не найден")
                        .build());

        if (!currentUser.getRole().getName().equals("ADMIN"))
            if (!institution.getManager().getId().equals(currentUser.getId()))
                throw NotAllowedException
                        .builder()
                        .messageUz("Sizda ruxsat yo'q")
                        .messageRu("У вас нет разрешения")
                        .build();

        Category editing = categoryRepository
                .findById(editDTO.getId())
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("Kategoriya topilmadi")
                        .messageRu("Категория не найдена")
                        .build());

        if (categoryRepository
                .existsByNameUzOrNameRuAndIdIsNot(editDTO.getNameUz(), editDTO.getNameRu(), editing.getId()))
            throw AlreadyExistsException.builder()
                    .messageUz("Kategoriya nomi allaqachon mavjud")
                    .messageRu("Название категории уже существует")
                    .build();

        Category category = categoryMapper.fromEditDTO(editDTO, editing);
        category.setInstitution(institution);
        return ApiResult.successResponse(categoryMapper
                .toDTO(categoryRepository.save(category)));
    }

    private User getCurrentUser() {
        String phoneNumber = CommonUtils.getCurrentUserPhoneNumber();
        if (phoneNumber != null)
            return userRepository
                    .findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> NotFoundException.builder()
                            .messageUz("Ushbu raqamli Foydalanuvchi topilmadi")
                            .messageRu("Этот цифровой Пользователь не найден")
                            .build());
        throw NotFoundException.builder()
                .messageUz("Ushbu raqamli Foydalanuvchi topilmadi")
                .messageRu("Этот цифровой Пользователь не найден")
                .build();
    }

    @Override
    public ApiResult<List<CategoryDTO>> getAll() {
        return ApiResult.successResponse(
                categoryMapper.toDTOList(
                        categoryRepository.findAll()
                )
        );
    }

    @Override
    public ApiResult<List<CategoryInfoDTO>> getAllForInfo() {
        return ApiResult.successResponse(categoryRepository
                .findAllForInfo());
    }

    @Override
    public ApiResult<List<CategoryInfoDTO>> getAllByInstitutionId(Long id) {

        if (id == null)
            throw BadRequestException.builder()
                    .messageUz("Id bo'sh bo'lishi mumkin emas")
                    .messageRu("Идентификатор не может быть пустым")
                    .build();

        return ApiResult.successResponse(categoryRepository
                .findAllForInfoByInstitutionId(id));
    }

    @Override
    public ApiResult<List<CategoryInfoDTO>> getAllByInstitutionIdAndPage(Long id, String page) {

        if (page == null || id == null)
            throw BadRequestException.builder()
                    .messageUz("Id yoki sahifa bo'sh bo'lishi mumkin emas")
                    .messageRu("Идентификатор или страница не могут быть пустыми")
                    .build();

        int[] paged = {Integer.parseInt(page.split("-")[0]),
                Integer.parseInt(page.split("-")[1])};
        return ApiResult.successResponse(categoryMapper
                .toInfoDTOList(categoryRepository
                        .findAllForInfoByInstitutionId(id, PageRequest.of(paged[0], paged[1]))
                        .getContent()));
    }

    @Override
    public ApiResult<Void> delete(Long id) {

        if (id == null)
            throw BadRequestException.builder()
                    .messageUz("Id bo'sh bo'lishi mumkin emas")
                    .messageRu("Идентификатор не может быть пустым")
                    .build();

        if (!categoryRepository.existsById(id))
            throw NotFoundException.builder()
                    .messageUz("Kategoriya topilmadi")
                    .messageRu("категория не найдена")
                    .build();
        Long managerId = categoryRepository.findMangerIdByCategoryId(id);
        User currentUser = getCurrentUser();

        if (!currentUser.getRole().getName().equals("ADMIN"))
            if (!currentUser.getId().equals(managerId))
                throw NotAllowedException.builder()
                        .messageUz("Sizda ruxsat yo'q")
                        .messageRu("У вас нет разрешения")
                        .build();

        categoryRepository.deleteById(id);
        return ApiResult.successResponse();
    }
}
