package app.entities;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * CrudRepository implementation for DialogChunk objects
 */
@Repository
public interface DialogChunkRepository extends CrudRepository<DialogChunk, Integer> {
}