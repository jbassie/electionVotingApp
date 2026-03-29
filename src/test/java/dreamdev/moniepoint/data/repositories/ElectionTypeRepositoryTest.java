package dreamdev.moniepoint.data.repositories;

import dreamdev.moniepoint.data.models.ElectionStatus;
import dreamdev.moniepoint.data.models.ElectionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ElectionTypeRepositoryTest {

    @Autowired
    private ElectionTypeRepository electionTypeRepository;

    @AfterEach
    void tearDown() {
        electionTypeRepository.deleteAll();
    }

    private ElectionType buildElection(String name, String description) {
        ElectionType election = new ElectionType();
        election.setName(name);
        election.setDescription(description);
        election.setCreatedAt(LocalDateTime.now());
        return election;
    }

    @Test
    void newRepository_ReturnsZero() {
        assertEquals(0L, electionTypeRepository.count());
    }

    @Test
    void testSaveElection_statusIsPendingAndCandidateIdsIsEmpty() {
        ElectionType election = buildElection("Presidential 2026", "National presidential election");

        ElectionType saved = electionTypeRepository.save(election);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Presidential 2026");
        assertThat(saved.getDescription()).isEqualTo("National presidential election");
        assertThat(saved.getStatus()).isEqualTo(ElectionStatus.PENDING);
        assertThat(saved.getCandidateIds().size()).isEqualTo(0);
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void testFindById_returnsCorrectElection() {
        ElectionType election = buildElection("Gubernatorial 2026", "State gubernatorial election");
        ElectionType saved = electionTypeRepository.save(election);

        Optional<ElectionType> found = electionTypeRepository.findById(saved.getId());
        assertThat(found.get().getName()).isEqualTo("Gubernatorial 2026");

    }

    @Test
    void testFindById_ReturnsEmpty_WhenNotFound() {
        Optional<ElectionType> found = electionTypeRepository.findById("non-existent-id");
        assertThat(found).isEmpty();
    }

    @Test
    void testFindByName_ReturnsCorrectElection() {
        electionTypeRepository.save(buildElection("Senatorial 2026", "Senate election"));

        Optional<ElectionType> found = electionTypeRepository.findByName("Senatorial 2026");


        assertThat(found.get().getName()).isEqualTo("Senatorial 2026");
    }

}