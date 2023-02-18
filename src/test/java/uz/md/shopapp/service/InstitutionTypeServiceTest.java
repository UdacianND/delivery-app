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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class InstitutionTypeServiceTest {

    private static final String NAME_UZ = "typeUz";
    private static final String NAME_RU = "typeRu";

    private static final String DESCRIPTION_UZ = "descriptionUz";
    private static final String DESCRIPTION_RU = "descriptionRu";

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
        institutionType = new InstitutionType(
                NAME_UZ,
                NAME_RU,
                DESCRIPTION_UZ,
                DESCRIPTION_RU);
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
                NAME_UZ,
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
                NAME_RU,
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

        assertEquals(NAME_UZ, data.getNameUz());
        assertEquals(NAME_RU, data.getNameRu());
        assertEquals(DESCRIPTION_UZ, data.getDescriptionUz());
        assertEquals(DESCRIPTION_RU, data.getDescriptionRu());
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
        institutionTypeRepository.saveAll(List.of(
                new InstitutionType("type1", "type1", "description1", "description1"),
                new InstitutionType("type2", "type2", "description2", "description2"),
                new InstitutionType("type3", "type3", "description3", "description3"),
                new InstitutionType("type4", "type4", "description4", "description4")
        ));

        ApiResult<List<InstitutionTypeDTO>> all = institutionTypeService.getAllByPage("0-2");
        assertTrue(all.isSuccess());
        List<InstitutionTypeDTO> data = all.getData();
        assertEquals(2, data.size());

        for (int i = 0; i < data.size(); i++) {
            assertEquals("type" + (i + 1), data.get(i).getNameUz());
            assertEquals("type" + (i + 1), data.get(i).getNameRu());
            assertEquals("description" + (i + 1), data.get(i).getDescriptionUz());
            assertEquals("description" + (i + 1), data.get(i).getDescriptionRu());
        }
    }

    @Test
    void shouldGetAllByPage2() {

        institutionTypeRepository.deleteAll();
        institutionTypeRepository.saveAll(List.of(
                new InstitutionType("type1", "type1", "description1", "description1"),
                new InstitutionType("type2", "type2", "description2", "description2"),
                new InstitutionType("type3", "type3", "description3", "description3"),
                new InstitutionType("type4", "type4", "description4", "description4")
        ));

        ApiResult<List<InstitutionTypeDTO>> all = institutionTypeService.getAllByPage("1-2");
        assertTrue(all.isSuccess());
        List<InstitutionTypeDTO> data = all.getData();
        assertEquals(2, data.size());

        for (int i = 0; i < data.size(); i++) {
            assertEquals("type" + (i + 3), data.get(i).getNameUz());
            assertEquals("type" + (i + 3), data.get(i).getNameRu());
            assertEquals("description" + (i + 3), data.get(i).getDescriptionUz());
            assertEquals("description" + (i + 3), data.get(i).getDescriptionRu());
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
        assertThrows(NotFoundException.class,() -> institutionTypeService.delete(15L));
    }





}
