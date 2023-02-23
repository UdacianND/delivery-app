package uz.md.shopapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uz.md.shopapp.domain.InstitutionType;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.institution_type.InstitutionTypeAddDTO;
import uz.md.shopapp.dtos.institution_type.InstitutionTypeDTO;
import uz.md.shopapp.dtos.institution_type.InstitutionTypeEditDTO;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.repository.InstitutionTypeRepository;
import uz.md.shopapp.service.contract.InstitutionTypeService;
import uz.md.shopapp.util.Mock;
import uz.md.shopapp.util.TestUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class InstitutionTypeServiceTest {

    private static final String ADDING_NAME_UZ = "addingTypeUz";
    private static final String ADDING_NAME_RU = "addingTypeRu";

    private static final String ADDING_DESCRIPTION_UZ = "addingDescriptionUz";
    private static final String ADDING_DESCRIPTION_RU = "addingDescriptionRu";

    private InstitutionType institutionType;

    @Autowired
    private InstitutionTypeService institutionTypeService;
    @Autowired
    private InstitutionTypeRepository institutionTypeRepository;

    @BeforeEach
    void setup() {
        institutionType = Mock.getInstitutionType();
    }

    @Test
    void shouldAddInstitutionType() {
        InstitutionTypeAddDTO addDTO = new InstitutionTypeAddDTO(
                ADDING_NAME_UZ,
                ADDING_NAME_RU,
                ADDING_DESCRIPTION_UZ,
                ADDING_DESCRIPTION_RU
        );

        ApiResult<InstitutionTypeDTO> add = institutionTypeService.add(addDTO);

        assertTrue(add.isSuccess());
        InstitutionTypeDTO data = add.getData();

        assertEquals(data.getNameUz(), ADDING_NAME_UZ);
        assertEquals(data.getNameRu(), ADDING_NAME_RU);
        assertEquals(data.getDescriptionUz(), ADDING_DESCRIPTION_UZ);
        assertEquals(data.getDescriptionRu(), ADDING_DESCRIPTION_RU);
    }

    @Test
    void shouldNotAddAlreadyExistedName1() {
        institutionTypeRepository.saveAndFlush(institutionType);
        InstitutionTypeAddDTO addDTO = new InstitutionTypeAddDTO(
                institutionType.getNameUz(),
                ADDING_NAME_RU,
                ADDING_DESCRIPTION_UZ,
                ADDING_DESCRIPTION_RU
        );
        assertThrows(AlreadyExistsException.class, () -> institutionTypeService.add(addDTO));
    }

    @Test
    void shouldNotAddAlreadyExistedName2() {
        institutionTypeRepository.saveAndFlush(institutionType);
        InstitutionTypeAddDTO addDTO = new InstitutionTypeAddDTO(
                ADDING_NAME_UZ,
                institutionType.getNameRu(),
                ADDING_DESCRIPTION_UZ,
                ADDING_DESCRIPTION_RU
        );
        assertThrows(AlreadyExistsException.class, () -> institutionTypeService.add(addDTO));
    }

    @Test
    void shouldFindById() {
        institutionTypeRepository.saveAndFlush(institutionType);

        ApiResult<InstitutionTypeDTO> byId = institutionTypeService.findById(institutionType.getId());

        assertTrue(byId.isSuccess());
        InstitutionTypeDTO data = byId.getData();

        assertEquals(institutionType.getNameUz(), data.getNameUz());
        assertEquals(institutionType.getNameRu(), data.getNameRu());
        assertEquals(institutionType.getDescriptionUz(), data.getDescriptionUz());
        assertEquals(institutionType.getDescriptionRu(), data.getDescriptionRu());
    }

    @Test
    void shouldNotFindDeletedById() {
        assertThrows(NotFoundException.class, () -> institutionTypeService
                .findById(14L));
    }

    @Test
    void shouldEdit() {
        institutionTypeRepository.deleteAll();
        institutionTypeRepository.saveAndFlush(institutionType);
        InstitutionTypeEditDTO editDTO = new InstitutionTypeEditDTO(
                ADDING_NAME_UZ,
                ADDING_NAME_RU,
                ADDING_DESCRIPTION_UZ,
                ADDING_DESCRIPTION_RU,
                institutionType.getId()
        );

        ApiResult<InstitutionTypeDTO> edit = institutionTypeService.edit(editDTO);

        assertTrue(edit.isSuccess());
        InstitutionTypeDTO data = edit.getData();

        assertEquals(data.getNameUz(), ADDING_NAME_UZ);
        assertEquals(data.getNameRu(), ADDING_NAME_RU);
        assertEquals(data.getDescriptionUz(), ADDING_DESCRIPTION_UZ);
        assertEquals(data.getDescriptionRu(), ADDING_DESCRIPTION_RU);
    }

    @Test
    void shouldNotEditNotFountType() {
        InstitutionTypeEditDTO editDTO = new InstitutionTypeEditDTO(
                ADDING_NAME_UZ,
                ADDING_NAME_RU,
                ADDING_DESCRIPTION_UZ,
                ADDING_DESCRIPTION_RU,
                15L
        );
        assertThrows(NotFoundException.class, () -> institutionTypeService.edit(editDTO));
    }

    @Test
    void shouldNotEditToAlreadyExistedName() {
        institutionTypeRepository.saveAndFlush(institutionType);
        institutionTypeRepository.saveAndFlush(new InstitutionType("n", "r", "du", "dr"));
        InstitutionTypeEditDTO editDTO = new InstitutionTypeEditDTO(
                "n",
                ADDING_NAME_RU,
                ADDING_DESCRIPTION_UZ,
                ADDING_DESCRIPTION_RU,
                institutionType.getId()
        );
        assertThrows(AlreadyExistsException.class,
                () -> institutionTypeService.edit(editDTO));
    }

    @Test
    void shouldGetAll() {
        institutionTypeRepository.deleteAll();
        institutionTypeRepository.saveAll(List.of(
                new InstitutionType("type1", "type1", "description1", "description1"),
                new InstitutionType("type2", "type2", "description2", "description2"),
                new InstitutionType("type3", "type3", "description3", "description3"),
                new InstitutionType("type4", "type4", "description4", "description4")
        ));

        ApiResult<List<InstitutionTypeDTO>> all = institutionTypeService.getAll();

        assertTrue(all.isSuccess());
        List<InstitutionTypeDTO> data = all.getData();
        assertEquals(institutionTypeRepository.count(), data.size());

    }

    @Test
    void shouldGetAllByPage() {
        institutionTypeRepository.deleteAll();
        institutionTypeRepository.saveAll(TestUtil.generateMockInstitutionTypes(5));

        ApiResult<List<InstitutionTypeDTO>> all = institutionTypeService.getAllByPage("0-2");
        assertTrue(all.isSuccess());
        List<InstitutionTypeDTO> data = all.getData();
        assertEquals(2, data.size());
        List<InstitutionType> list = institutionTypeRepository.findAll();
        checkTypesEquality(data, list.subList(0, 2));
    }

    @Test
    void shouldGetAllByPage2() {

        institutionTypeRepository.deleteAll();
        institutionTypeRepository.saveAll(TestUtil.generateMockInstitutionTypes(5));
        ApiResult<List<InstitutionTypeDTO>> all = institutionTypeService.getAllByPage("1-2");
        assertTrue(all.isSuccess());
        List<InstitutionTypeDTO> data = all.getData();
        assertEquals(2, data.size());
        List<InstitutionType> list = institutionTypeRepository.findAll();
        checkTypesEquality(data, list.subList(2, 4));
    }

    private void checkTypesEquality(List<InstitutionTypeDTO> actual, List<InstitutionType> expected) {
        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i).getId(), expected.get(i).getId());
            assertEquals(actual.get(i).getNameUz(), expected.get(i).getNameUz());
            assertEquals(actual.get(i).getNameRu(), expected.get(i).getNameRu());
            assertEquals(actual.get(i).getDescriptionUz(), expected.get(i).getDescriptionUz());
            assertEquals(actual.get(i).getDescriptionRu(), expected.get(i).getDescriptionRu());
        }
    }

    @Test
    void shouldDelete() {
        institutionTypeRepository.saveAndFlush(institutionType);
        ApiResult<Void> delete = institutionTypeService.delete(institutionType.getId());
        assertTrue(delete.isSuccess());
    }

    @Test
    void shouldNotDeleteNotFound() {
        assertThrows(NotFoundException.class, () -> institutionTypeService.delete(15L));
    }


}
